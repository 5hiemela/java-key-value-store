package engine;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

// Handles appending write mutations (PUT, DELETE) to an Append-Only File on disk.

public class AOFLogger {

    private final String filePath;

    public AOFLogger(String filePath) {
        this.filePath = filePath;
    }

    // Appends a formatted command line to the disk log.
    public synchronized void log(String command) {
        try (FileWriter fw = new FileWriter(filePath, true);
             BufferedWriter bw = new BufferedWriter(fw);
             PrintWriter out = new PrintWriter(bw)) {
            out.println(command);
        } catch (IOException e) {
            System.err.println("Failed to write to AOF file: " + e.getMessage());
        }
    }
}
