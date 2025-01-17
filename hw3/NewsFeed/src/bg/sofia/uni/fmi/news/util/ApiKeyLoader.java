package bg.sofia.uni.fmi.news.util;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class ApiKeyLoader {
    private static final String API_KEY_FILE_PATH = "src/api_key.txt";

    public static String loadApiKey() {
        try (BufferedReader reader = new BufferedReader(new FileReader(API_KEY_FILE_PATH))) {
            return reader.readLine();
        } catch (IOException e) {
            throw new RuntimeException("Failed to read the file at: " + API_KEY_FILE_PATH, e);
        }
    }
}
