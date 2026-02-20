package utils;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class LocatorReader {


private static final Map<String, JsonObject> MODULE_CACHE = new ConcurrentHashMap<>();


private static JsonObject loadModuleFile(String module) {
        return MODULE_CACHE.computeIfAbsent(module, m -> {
            String filePath = "locators/" + m + "_locator.json";

            try (InputStream is = LocatorReader.class.getClassLoader().getResourceAsStream(filePath)) 
            {
                if (is == null) {
                    throw new RuntimeException("Locator file not found: " + filePath +
                            " (keep it under src/test/resources/locators/)");
                }

                return JsonParser.parseReader(
                        new InputStreamReader(is, StandardCharsets.UTF_8)
                ).getAsJsonObject();

            } catch (Exception e) {
                throw new RuntimeException("Failed to load locator file: " + filePath, e);
            }
            });
    }
    

    public static String getLocator(String module,String page, String locatorName, String key) 
    {
        JsonObject moduleJson = loadModuleFile(module);
        if (!moduleJson.has(page)) {
            throw new RuntimeException("Page '" + page + "' not found in module '" + module + "'");
        }
        JsonObject pageObj = moduleJson.getAsJsonObject(page);

        if (!pageObj.has(locatorName)) {
            throw new RuntimeException("Locator '" + locatorName + "' not found under page '" + page +
                    "' in module '" + module + "'");
        }
        JsonObject locatorObj = pageObj.getAsJsonObject(locatorName);

        if (!locatorObj.has(key)) {
            throw new RuntimeException("Key '" + key + "' not found for locator '" + locatorName +
                    "' under page '" + page + "' in module '" + module + "'");
        }
        return locatorObj.get(key).getAsString();




        }
}
