package utils;

import com.microsoft.playwright.Page;
import io.cucumber.java.Scenario;
import io.qameta.allure.Allure;
import java.io.ByteArrayInputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ScreenshotUtils {

    /**
     * Capture screenshot and attach to both Allure and Cucumber report
     * @param page Playwright Page object
     * @param scenario Cucumber Scenario object for embedding
     * @param stepName Name of the step (for file naming)
     */
    public static void captureScreenshot(Page page, Scenario scenario, String stepName) {
        try {
            if (page != null && !page.isClosed()) {
                // Generate timestamp for unique file naming
                String timestamp = LocalDateTime.now()
                        .format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss-SSS"));
                
                // Create screenshot filename
                String screenshotName = stepName.replaceAll("[^a-zA-Z0-9]", "_") 
                        + "_" + timestamp + ".png";
                
                // Capture screenshot
                byte[] screenshotBytes = page.screenshot();
                
                // Attach to Cucumber HTML report
                if (scenario != null) {
                    scenario.attach(screenshotBytes, "image/png", screenshotName);
                }
                
                // Attach to Allure report
                Allure.addAttachment(screenshotName, "image/png", 
                        new ByteArrayInputStream(screenshotBytes), "png");
                
                System.out.println("Screenshot captured and attached: " + screenshotName);
            }
        } catch (Exception e) {
            System.err.println("Failed to capture screenshot: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Capture screenshot with default naming (legacy method)
     * @param page Playwright Page object
     */
    public static void captureScreenshot(Page page) {
        captureScreenshot(page, null, "screenshot");
    }

    /**
     * Capture screenshot with step name (2-parameter overload for backward compatibility)
     * @param page Playwright Page object
     * @param stepName Name of the step
     */
    public static void captureScreenshot(Page page, String stepName) {
        captureScreenshot(page, null, stepName);
    }
}
