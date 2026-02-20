package step_definition;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.*;
import com.microsoft.playwright.options.WaitUntilState;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.AfterStep;
import io.cucumber.java.Scenario;
import io.qameta.allure.Allure;
import utils.ConfigReader;
import utils.ScreenshotUtils;
import io.cucumber.java.*;
import java.nio.file.Paths;
import java.nio.file.Files;
import org.slf4j.Logger;
import utils.LoggerUtil;

public class Hooks
{


private static final ThreadLocal<Playwright> tlPlaywright = new ThreadLocal<>();
    private static final ThreadLocal<Browser> tlBrowser = new ThreadLocal<>();
    private static final ThreadLocal<BrowserContext> tlContext = new ThreadLocal<>();
    private static final ThreadLocal<Page> tlPage = new ThreadLocal<>();

    private static final Logger log = LoggerUtil.getLogger(Hooks.class);


    public static Page getPage()
    {
        return tlPage.get();
    }

    @Before
    public void setUp() {
        Playwright playwright = Playwright.create();
        tlPlaywright.set(playwright);
        BrowserType.LaunchOptions options = new BrowserType.LaunchOptions()
                .setHeadless(ConfigReader.isHeadless())
                .setSlowMo(ConfigReader.getSlowMo());

                if (ConfigReader.isMaximize())
                {
                    options.setArgs(java.util.Arrays.asList("--start-maximized"));
                }

        Browser browser;      
        switch (ConfigReader.getBrowserName().toLowerCase()) {
            case "chromium":
                browser = playwright.chromium().launch(options);
                break;
            case "firefox":
                browser = playwright.firefox().launch(options);
                break;
            case "webkit":
                browser = playwright.webkit().launch(options);
                break;
            default:
                browser = playwright.chromium().launch(options);
                break;
        }
        tlBrowser.set(browser);

       Browser.NewContextOptions options1 = new Browser.NewContextOptions();
       options1.setAcceptDownloads(true);

       if (ConfigReader.isMaximize())
       {
        options1.setViewportSize(null);
       }
       log.info("Starting browser: {}", ConfigReader.getBrowserName());

        BrowserContext context = browser.newContext(options1);
        tlContext.set(context);

        Page page = context.newPage();
        tlPage.set(page);

        page.setDefaultTimeout(60000);
        page.setDefaultNavigationTimeout(60000);
        page.navigate(
                ConfigReader.getAppUrl(),
                new Page.NavigateOptions()
                        .setWaitUntil(WaitUntilState.DOMCONTENTLOADED)
                        .setTimeout(60000));
    }

    @After
    public void afterScenario(Scenario scenario) {
        // Clean up thread resources safely
        try {
            Page page = tlPage.get();
            if (page != null) page.close();
        } catch (Exception ignored) {}

        try {
            BrowserContext context = tlContext.get();
            if (context != null) context.close();
        } catch (Exception ignored) {}

        try {
            Browser browser = tlBrowser.get();
            if (browser != null) browser.close();
        } catch (Exception ignored) {}

        try {
            Playwright playwright = tlPlaywright.get();
            if (playwright != null) playwright.close();
        } catch (Exception ignored) {}

        tlPage.remove();
        tlContext.remove();
        tlBrowser.remove();
        tlPlaywright.remove();

        log.info("Closing browser after scenario: {}", scenario.getName());

    }

    @AfterStep
    public void afterEachStep(Scenario scenario) {
        // Get the last step text from the scenario
        String stepText = getLastStepText(scenario);
        
        // Create a proper Allure step context for the screenshot
        // This ensures the screenshot appears under the correct step
        Page page = tlPage.get();
        Allure.step("Screenshot after step: " , () -> {
            ScreenshotUtils.captureScreenshotForStep(page, scenario, stepText);
        });
    }
    
    /**
     * Extract the text of the last executed step from the scenario
     */
    private String getLastStepText(Scenario scenario) {
        try {
            java.lang.reflect.Method method = scenario.getClass().getMethod("getSteps");
            java.util.List<?> steps = (java.util.List<?>) method.invoke(scenario);

            if (steps != null && !steps.isEmpty()) {
                Object lastStep = steps.get(steps.size() - 1);
                java.lang.reflect.Method getText = lastStep.getClass().getMethod("getText");
                Object stepText = getText.invoke(lastStep);

                if (stepText != null) {
                    String text = stepText.toString();
                    return text.length() > 80 ? text.substring(0, 77) + "..." : text;
                }
            }
        } catch (Exception e) {
            System.out.println("Could not extract step text: " + e.getMessage());
        }
        return "Step Complete";
        }

}