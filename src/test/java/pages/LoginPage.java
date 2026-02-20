package pages;

import com.microsoft.playwright.Page;
import utils.LocatorReader;
import utils.TestDataReader;
import utils.ScreenshotUtils;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.options.LoadState;
import com.microsoft.playwright.options.WaitForSelectorState;
import org.testng.Assert;
import org.slf4j.Logger;
import utils.LoggerUtil;


public class LoginPage {
    private static final Logger log = LoggerUtil.getLogger(LoginPage.class);

    private Page page;
    private TestDataReader testData;

    private static final String LOGIN_SCENARIO = "TC001 - Login to application";


    public LoginPage(Page page) {
        this.page = page;
        this.testData = new TestDataReader("Testdata/timesheetData.json");
         log.info("LoginPage object initialized");
    }
 private void waitVisible(String selector) {
     log.debug("Waiting for element to be visible: {}", selector);

        page.waitForSelector(selector,
                new Page.WaitForSelectorOptions()
                        .setState(WaitForSelectorState.VISIBLE)
                        .setTimeout(60000));
    }

    private void safeClick(String selector) {
    waitVisible(selector);
    Locator loc = page.locator(selector);
    log.info("Clicking element: {}", selector);

    try {
        loc.click(new Locator.ClickOptions().setTimeout(30000));
    } catch (Exception ignored) {
        log.warn("Normal click failed, trying force click for selector: {}", selector);

        loc.click(
            new Locator.ClickOptions()
                .setForce(true)
                .setTimeout(30000)
        );
    }
}

    public void enterUsername() {
        String uname = testData.getData(LOGIN_SCENARIO, "username");
        String userInput = LocatorReader.getLocator("locatortimesheet","login", "username", "selector");

        log.info("Entering Username value");
        log.debug("Username selector: {}", userInput);
        log.debug("Username value: {}", uname);
         
        waitVisible(userInput);
        page.locator(userInput).fill(uname);
        String actualuname = page.locator(userInput).inputValue();
        log.info("Validating Username value. Actual: {}", actualuname);

        Assert.assertEquals(actualuname, uname, "Username value does not match");
        log.info("Username value validated successfully");

        ScreenshotUtils.captureScreenshot(page, "Username_Entered");
        
        
    }

    public void enterPassword() {
        String pwd = testData.getData(LOGIN_SCENARIO, "password");
        String pwdInput = LocatorReader.getLocator("locatortimesheet","login", "password","selector");
        
        log.info("Entering Password");
        log.debug("Password selector: {}", pwdInput);
        log.debug("Password value: {}", pwd);
        
        
        waitVisible(pwdInput);
        page.locator(pwdInput).fill(pwd);
        String actualpwd = page.locator(pwdInput).inputValue();
         log.info("Validating Password. Actual: {}", actualpwd);


        Assert.assertEquals(actualpwd, pwd, "Password value does not match");
            log.info("Password value validated successfully");

        ScreenshotUtils.captureScreenshot(page, "Password_Entered");
    }

   public void clickLogin() {
    log.info("Clicking Login button");
    
    String loginbutton = LocatorReader.getLocator("locatortimesheet","login", "loginButton","selector");
    safeClick(loginbutton);
    
    // Wait for page to load after login - allow time for dashboard to render

     String dashboardHeader1 = LocatorReader.getLocator("locatortimesheet","dashboard", "dashboardHeader", "selector");
        page.waitForSelector(dashboardHeader1,
            new Page.WaitForSelectorOptions()
                    .setState(WaitForSelectorState.VISIBLE)
                    .setTimeout(60000));
                    

        Assert.assertTrue(page.locator(dashboardHeader1).first().isVisible(), "Dashboard did not load after login");
           
           log.info("Login successful, dashboard loaded");

        ScreenshotUtils.captureScreenshot(page, "Login_Clicked");
}

    public boolean isDashboardDisplayed() {
        log.info("Checking if dashboard is displayed after login");

        try {
            String dashboardSelector = LocatorReader.getLocator("locatortimesheet","dashboard", "dashboardHeader", "selector");
            
            log.info("Checking dashboard with selector: {}", dashboardSelector);
            
            // Wait for the element to be visible with timeout
            page.waitForSelector(dashboardSelector,
                    new Page.WaitForSelectorOptions()
                            .setState(WaitForSelectorState.VISIBLE)
                            .setTimeout(15000));
            
            boolean isVisible = page.locator(dashboardSelector).isVisible();
            log.info("Dashboard element visible: {}", isVisible);
            return isVisible;
            
        } catch (Exception e) {
            log.error("Dashboard check failed: {}", e.getMessage());
            e.printStackTrace();
            
            // Alternative check - look for any page content indicating successful login
            try {
                // Check if we're on a page with the topbar (common in OrangeHRM dashboard)

                String topbarSelector = LocatorReader.getLocator("locatortimesheet","dashboard", "top1", "selector");

                boolean hasTopbar = page.locator(topbarSelector).isVisible();
                log.info("Alternative check - Topbar visible: {}", hasTopbar);
                return hasTopbar;
            } catch (Exception e2) {
                log.error("Alternative check also failed: {}", e2.getMessage());
                return false;
            }
        }
    }
}
