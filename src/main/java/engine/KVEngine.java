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

    // get - Retrieves the value associated with a key
    public Optional<String> get(String key) {
        if (key == null) {
            return Optional.empty();
        }
        return Optional.ofNullable(store.get(key));
    }

    // delete - Deletes a key-value pair from memory
    public boolean delete(String key) {
        if (key == null) {
            return false;
        }
        return store.remove(key) != null;
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
    }
}
