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

    public static boolean isMaximize() {

if (config.has("window") && config.getAsJsonObject("window").has("maximize")) {
        return config.getAsJsonObject("window")
                     .get("maximize")
                     .getAsBoolean();
    }
    return false; //default
    }
    public static boolean isParallelEnabled()
    {
        try
        {
            return config.getAsJsonObject("parallel")
                         .get("enabled")
                         .getAsBoolean();
        } catch(Exception e)
        {
            // Default to false if parallel config is missing
            return false;
        }
    }

    public static int getparlalelThreads()
    {
        try
        {
            return config.getAsJsonObject("parallel")
                         .get("threadCount")
                         .getAsInt();
        } catch(Exception e)
        {
            // Default to 1 thread if parallel config is missing
            return 1;
        }
    }
}

