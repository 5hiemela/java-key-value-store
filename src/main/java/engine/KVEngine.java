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
}
