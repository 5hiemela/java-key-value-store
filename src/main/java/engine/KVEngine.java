package engine;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class KVEngine {

    private final ConcurrentHashMap<String, String> store;

    // Constructor
    // Create an empty map in memory whenever a new KVEngine object is created
    public KVEngine() {
        this.store = new ConcurrentHashMap<>();
    }

    // put - Stores a key-value pair in memory
    public void put(String key, String value) {
        if (key == null || value == null) { // Check if key or value is null
            throw new IllegalArgumentException("Key and value cannot be null");
        }
        store.put(key, value);
    }
}
