package utils;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonArray;


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
    public String getData(String scenarioName, String key) {
        JsonObject testDataObject = getScenarioTestData(scenarioName);

        if (!testDataObject.has(key) || testDataObject.get(key).isJsonNull()) {
            throw new IllegalArgumentException("Key '" + key + "' not found under scenario '" + scenarioName + "'");
        }

        JsonElement value = testDataObject.get(key);

        // Return string form (works for numbers/booleans too)
        if (value.isJsonPrimitive()) {
            return value.getAsString();
        }

        throw new IllegalArgumentException(
                "Key '" + key + "' under scenario '" + scenarioName + "' is not a primitive value."
        );
    }

    /**
     * Helper: returns the "testData" JsonObject for the matching scenarioName.
     */
    private JsonObject getScenarioTestData(String scenarioName) {
        if (!root.has("scenarios") || !root.get("scenarios").isJsonArray()) {
            throw new IllegalArgumentException("Invalid JSON: 'scenarios' array not found.");
        }

        JsonArray scenarios = root.getAsJsonArray("scenarios");

        for (JsonElement element : scenarios) {
            if (!element.isJsonObject()) continue;

            JsonObject scenarioObj = element.getAsJsonObject();

            if (!scenarioObj.has("scenarioName")) continue;

            String name = scenarioObj.get("scenarioName").getAsString();
            if (scenarioName.equals(name)) {
                if (!scenarioObj.has("testData") || !scenarioObj.get("testData").isJsonObject()) {
                    throw new IllegalArgumentException(
                            "Invalid JSON: 'testData' object missing for scenario '" + scenarioName + "'"
                    );
                }
                return scenarioObj.getAsJsonObject("testData");
            }
        }
        throw new IllegalArgumentException("Scenario not found: '" + scenarioName + "'");

}
}
