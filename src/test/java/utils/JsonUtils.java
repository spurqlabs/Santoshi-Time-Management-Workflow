package utils;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.FileReader;

public class JsonUtils
{
   private static JsonObject jsonData;

   static
   {
try
{
    jsonData = JsonParser.parseReader(new FileReader("src/test/resources/Testdata/timesheetData.json")).getAsJsonObject();
}
catch (Exception e)
{
    throw new RuntimeException("Failed to load Json Test data", e);
}
   }
   public static String getTimesheetData(String key)

   {
       return jsonData.getAsJsonObject("timesheet").get(key).getAsString();
   }

}
