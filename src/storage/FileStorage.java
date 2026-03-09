package storage;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Utility class for reading and writing text files.
 * Handles pure I/O operations.
 */
public class FileStorage {

    /**
     * Reads a file line by line and returns a list of strings.
     *
     * @param filePath The path of the file to read.
     * @return A list containing all non-empty lines from the file.
     */
    public static List<String> readLines(String filePath) {
        List<String> lines = new ArrayList<>();
        File file = new File(filePath);

        // If the file does not exist (e.g., first run), return an empty list to prevent errors
        if (!file.exists()) {
            return lines;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.trim().isEmpty()) {
                    lines.add(line.trim());
                }
            }
        } catch (IOException e) {
            System.err.println("Error: " + filePath + " - " + e.getMessage());
        }
        return lines;
    }

    /**
     * Writes a list of strings to a file line by line.
     *
     * @param filePath The path of the file to write to.
     * @param lines The list of strings to be written.
     */
    public static void writeLines(String filePath, List<String> lines) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            for (String line : lines) {
                writer.write(line);
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error: " + filePath + " - " + e.getMessage());
        }
    }
}