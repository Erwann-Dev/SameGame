package model;

import java.io.*;

/**
 * Handles saving and loading of the high score from a file.
 */
public class HighScoreManager {

    private static final String FILE_NAME = "highscore.txt";

    /**
     * Saves the high score to a file.
     */
    public static void saveHighScore(int score) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(FILE_NAME))) {
            writer.println(score);
        } catch (IOException e) {
            System.out.println("Error saving high score.");
        }
    }

    /**
     * Loads the high score from a file.
     */
    public static int loadHighScore() {
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {
            return Integer.parseInt(reader.readLine());
        } catch (Exception e) {
            return 0; // default if file doesn't exist
        }
    }
}