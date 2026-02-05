package utils;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class TestDataReader {
    private final JsonObject root;

    public TestDataReader(String resourcePath) {
        try {
            InputStream is = getClass().getClassLoader().getResourceAsStream(resourcePath);
            if (is == null) {
                throw new RuntimeException("Test data file not found on classpath: " + resourcePath +
                        " (Expected under src/test/resources/)");
            }
            root = JsonParser.parseReader(new InputStreamReader(is, StandardCharsets.UTF_8)).getAsJsonObject();
        } catch (Exception e) {
            throw new RuntimeException("Failed to load test data file: " + resourcePath, e);
        }
    }

    // Supports both flat keys ("username") and nested keys ("register.username")
    public String getData(String keyPath) {
        String[] parts = keyPath.split("\\.");
        JsonElement current = root;

        for (String part : parts) {
            if (current == null || !current.isJsonObject()) {
                throw new IllegalArgumentException("Invalid JSON path: '" + keyPath + "'");
            }
            JsonObject obj = current.getAsJsonObject();
            if (!obj.has(part) || obj.get(part).isJsonNull()) {
                throw new IllegalArgumentException("Test data key missing: '" + keyPath + "'. Check your JSON file.");
            }
            current = obj.get(part);
        }

        return current.getAsString();
    }
}
