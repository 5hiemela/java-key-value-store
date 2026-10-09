package engine;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class KVEngine {

    private final ConcurrentHashMap<String, CacheEntry> store;
    private final AOFLogger aofLogger;

    // Default constructor: logs to appendonly.aof
    public KVEngine() {
        this("appendonly.aof");
    }

    // Constructor with custom AOF file path
    public KVEngine(String aofFilePath) {
        this.store = new ConcurrentHashMap<>();
        this.aofLogger = new AOFLogger(aofFilePath);
    }

    // put - Stores a key-value pair that never expires
    public void put(String key, String value) {
        put(key, value, -1);
    }

    // put - Stores a key-value pair with a Time-To-Live (TTL) in milliseconds
    public void put(String key, String value, long ttlMillis) {
        if (key == null || value == null) {
            throw new IllegalArgumentException("Key and value cannot be null");
        }

        long expiryTimestamp = (ttlMillis > 0) ? System.currentTimeMillis() + ttlMillis : -1;
        store.put(key, new CacheEntry(value, expiryTimestamp));

        // Log to AOF file
        if (ttlMillis > 0) {
            aofLogger.log("PUT " + key + " " + value + " " + ttlMillis);
        } else {
            aofLogger.log("PUT " + key + " " + value);
        }
    }

    // get - Retrieves value with Lazy Expiration handling
    public Optional<String> get(String key) {
        if (key == null) {
            return Optional.empty();
        }

        CacheEntry entry = store.get(key);
        if (entry == null) {
            return Optional.empty();
        }

        // Lazy Expiration Check
        if (entry.isExpired()) {
            store.remove(key, entry); // Thread-safe eviction
            aofLogger.log("DELETE " + key); // Log eviction to keep disk in sync
            return Optional.empty();
        }

        return Optional.ofNullable(entry.value());
    }

    // delete - Deletes a key-value pair from memory
    public boolean delete(String key) {
        if (key == null) {
            return false;
        }

        boolean removed = store.remove(key) != null;
        if (removed) {
            aofLogger.log("DELETE " + key);
        }
        return removed;
    }

    // containsKey - Checks if a key exists in the store
    public boolean containsKey(String key) {
        return key != null && store.containsKey(key);
    }

    // size - Returns the total amount of keys stored
    public int size() {
        return store.size();
    }

    // clear - Clears all stored data
    public void clear() {
        store.clear();
        aofLogger.log("CLEAR");
    }
}