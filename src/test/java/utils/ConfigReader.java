package utils;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.FileReader;

public class ConfigReader {

    private static JsonObject config;

    static {
        try {
            FileReader reader = new FileReader(
                    "src/test/resources/config/config.json"
            );
            config = JsonParser.parseReader(reader).getAsJsonObject();
        } catch (Exception e) {
             e.printStackTrace();
        }
    }

    public static String getAppUrl() {
        return config.getAsJsonObject("application")
                     .get("url")
                     .getAsString();
    }

    public static String getBrowserName() {
        return config.getAsJsonObject("browser")
                     .get("name")
                     .getAsString();
    }

    public static boolean isHeadless() {
        return config.getAsJsonObject("browser")
                     .get("headless")
                     .getAsBoolean();
    }
    public static int getSlowMo() {
        return config.getAsJsonObject("browser")
                     .get("slowMo")
                     .getAsInt();
    }
}
