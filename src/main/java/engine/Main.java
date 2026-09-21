package engine;

public class Main {
    public static void main(String[] args) {
        KVEngine engine = new KVEngine();

        // Test put and get + size
        engine.put("player_1", "Guts");
        engine.put("player_2", "Griffith");

        System.out.println("PLayer 1: " + engine.get("player_1").orElse("Not Found"));
        System.out.println("Total keys stored: " + engine.size());

        // Test delete and containsKey
        engine.delete("player_2");
        System.out.println("Player 2 exists: " + engine.containsKey("player)2"));
        System.out.println("Total keys after deletion: " + engine.size());

        // Test clear
        engine.clear();
        System.out.println("Total keys after clear: " + engine.size());
    }
}
