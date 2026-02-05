package step_definition;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.AfterStep;
import io.cucumber.java.Scenario;
import utils.ConfigReader;
import utils.ScreenshotUtils;

public class Hooks
{

public static Playwright playwright;
    public static Browser browser;
    public static BrowserContext context;
    public static Page page;

    @Before
    public void setUp() {
        playwright = Playwright.create();
        BrowserType.LaunchOptions options = new BrowserType.LaunchOptions()
                .setHeadless(ConfigReader.isHeadless())
                .setSlowMo(ConfigReader.getSlowMo());
        switch (ConfigReader.getBrowserName().toLowerCase()) {
            case "chromium":
                browser = playwright.chromium().launch(options);
        }

        context = browser.newContext();
        page = context.newPage();
        page.navigate(ConfigReader.getAppUrl());
page.waitForLoadState();
page.setDefaultTimeout(60000);
        
    }


    @After
    public void afterScenario(Scenario scenario) {
        // Take screenshot on failure
        if (scenario.isFailed()) {
            ScreenshotUtils.captureScreenshot(page, scenario, "Failed_Step_" + scenario.getName());
        }
        
        if (page != null) page.close();
        if (context != null) context.close();
        if (browser != null) browser.close();
        if (playwright != null) playwright.close();
    }

    @AfterStep
    public void afterEachStep(Scenario scenario) {
        // Capture screenshot after each step
        ScreenshotUtils.captureScreenshot(page, scenario, "Step_Screenshot");
    }

}