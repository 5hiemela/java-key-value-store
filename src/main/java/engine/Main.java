package engine;

public class Main {
    public static void main(String[] args) {
        // Start engine and write data
        KVEngine engine = new KVEngine("test_persistence.aof");

        if (engine.size() == 0) {
            System.out.println("Engine started fresh. Inserting test data...");
            engine.put("hero", "Guts");
            engine.put("weapon", "Dragonslayer");
        } else {
            System.out.println("Engine recovered data from disk!");
        }

        System.out.println("Current Hero: " + engine.get("hero").orElse("Not Found"));
        System.out.println("Current Weapon: " + engine.get("weapon").orElse("Not Found"));
        System.out.println("Total keys in store: " + engine.size());

        System.out.println("\nRestart the program to see data recovery in action!");
    }
}