package utils;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class LocatorReader {

    private static JsonObject locatorData;

    static {
        try {
            InputStream is = LocatorReader.class
                    .getClassLoader()
                    .getResourceAsStream("locators/testdata_locator.json");

            if (is == null) {
                throw new RuntimeException(
                        "testdata_locator.json not found. " +
                        "Make sure it is under src/test/resources/locators/"
                );
            }

            locatorData = JsonParser.parseReader(
                    new InputStreamReader(is, StandardCharsets.UTF_8)
            ).getAsJsonObject();

        } catch (Exception e) {
            throw new RuntimeException("Failed to load testdata_locator.json", e);
        }
    }

    public static String getLocator(String page, String locatorName, String key) {
        return locatorData
                .getAsJsonObject(page)
                .getAsJsonObject(locatorName)
                .get(key)
                .getAsString();
    }
}
