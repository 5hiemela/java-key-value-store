package engine;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        KVEngine engine = new KVEngine();

        // Test put and get + size
        engine.put("player_1", "Guts");
        engine.put("player_2", "Griffith");

        System.out.println("Player 1: " + engine.get("player_1").orElse("Not Found"));
        System.out.println("Total keys stored: " + engine.size());

        // Test delete and containsKey
        engine.delete("player_2");
        System.out.println("Player 2 exists: " + engine.containsKey("player_2"));
        System.out.println("Total keys after deletion: " + engine.size());

        // Test clear
        engine.clear();
        System.out.println("Total keys after clear: " + engine.size());

        System.out.println("\n--- TTL Expiration Test ---");
        // Store key with a 2-second (2000ms) TTL
        engine.put("session_token", "secret_123", 2000);

        // Immediate check (Should exist)
        System.out.println("Immediate check: " + engine.get("session_token").orElse("Expired / Not Found"));

        System.out.println("Waiting 2.1 seconds for TTL to expire...");
        Thread.sleep(2100);

        // Post-expiration check (Should be expired and lazily deleted)
        System.out.println("Post-sleep check: " + engine.get("session_token").orElse("Expired / Not Found"));
        System.out.println("Engine size after lazy deletion: " + engine.size());
    }
}
