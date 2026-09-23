package engine;

// Wraps a stored value along with its expiration timestamp.
public record CacheEntry(String value, long expiryTimestamp) {

    // Helper to check if this entry has passed its expiration time
    // An expiryTimestamp of -1 means the key never expires.
    public boolean isExpired() {
        if (expiryTimestamp == -1) {
            return false;
        }
        return System.currentTimeMillis() > expiryTimestamp;
    }
}
