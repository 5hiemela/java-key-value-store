package engine;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class KVEngine {

    private final ConcurrentHashMap<String, CacheEntry> store;
    private final AOFLogger aofLogger;

    // Default constructor: logs to appendonly.aof
    public KVEngine() {
        this("appendonly.aof");
    }

    // Constructor with custom AOF file path (triggers recovery on startup)
    public KVEngine(String aofFilePath) {
        this.store = new ConcurrentHashMap<>();
        this.aofLogger = new AOFLogger(aofFilePath);
        loadAof(); // Recover state from disk on startup
    }

    /**
     * Reads the AOF file line by line and rebuilds the in-memory state.
     */
    private void loadAof() {
        File file = new File(aofLogger.getFilePath());
        if (!file.exists()) {
            return; // No log file yet, start fresh
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) {
                    continue;
                }

                String[] parts = line.split(" ", 4);
                String command = parts[0];

                switch (command) {
                    case "PUT":
                        if (parts.length >= 3) {
                            String key = parts[1];
                            String value = parts[2];
                            long expiryTimestamp = (parts.length >= 4) ? Long.parseLong(parts[3]) : -1;

                            // Skip loading if the key expired while the engine was shut down
                            if (expiryTimestamp == -1 || expiryTimestamp > System.currentTimeMillis()) {
                                store.put(key, new CacheEntry(value, expiryTimestamp));
                            }
                        }
                        break;
                    case "DELETE":
                        if (parts.length >= 2) {
                            store.remove(parts[1]);
                        }
                        break;
                    case "CLEAR":
                        store.clear();
                        break;
                    default:
                        break;
                }
            }
        } catch (IOException e) {
            System.err.println("Failed to read AOF file during recovery: " + e.getMessage());
        }
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

        // Log absolute expiry timestamp to AOF file
        if (expiryTimestamp != -1) {
            aofLogger.log("PUT " + key + " " + value + " " + expiryTimestamp);
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
            store.remove(key, entry);
            aofLogger.log("DELETE " + key);
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