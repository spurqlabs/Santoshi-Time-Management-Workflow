package pages;

import com.microsoft.playwright.Page;
import utils.LocatorReader;
import utils.TestDataReader;
import utils.ScreenshotUtils;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.options.LoadState;
import com.microsoft.playwright.options.WaitForSelectorState;
import org.testng.Assert;


public class LoginPage {

    private Page page;
    private TestDataReader testData;

    public LoginPage(Page page) {
        this.page = page;
        this.testData = new TestDataReader("Testdata/timesheetData.json");
    }
 private void waitVisible(String selector) {
        page.waitForSelector(selector,
                new Page.WaitForSelectorOptions()
                        .setState(WaitForSelectorState.VISIBLE)
                        .setTimeout(60000));
    }

    private void safeClick(String selector) {
    waitVisible(selector);
    Locator loc = page.locator(selector);

    try {
        loc.click(new Locator.ClickOptions().setTimeout(30000));
    } catch (Exception ignored) {
        loc.click(
            new Locator.ClickOptions()
                .setForce(true)
                .setTimeout(30000)
        );
    }
}

    public void enterUsername() {
        String uname = testData.getData("register.username");
        String userInput = LocatorReader.getLocator("login", "username", "selector");

        waitVisible(userInput);
        page.locator(userInput).fill(uname);
        String actualuname = page.locator(userInput).inputValue();
        Assert.assertEquals(actualuname, uname, "Username value is entered correctly");

        ScreenshotUtils.captureScreenshot(page, "Username_Entered");
        
        
    }

    public void enterPassword() {
        String pwd = testData.getData("register.password");
        String pwdInput = LocatorReader.getLocator("login", "password","selector");
        waitVisible(pwdInput);
        page.locator(pwdInput).fill(pwd);
        String actualpwd = page.locator(pwdInput).inputValue();
        Assert.assertEquals(actualpwd, pwd, "Password value is not entered correctly");
        ScreenshotUtils.captureScreenshot(page, "Password_Entered");
    }

   public void clickLogin() {
    String loginbutton = LocatorReader.getLocator("login", "loginButton","selector");
    safeClick(loginbutton);
    
    // Wait for page to load after login - allow time for dashboard to render
    try {
        Thread.sleep(2000);  // Wait for dashboard to load
    } catch (InterruptedException e) {
        Thread.currentThread().interrupt();
    }
    
    ScreenshotUtils.captureScreenshot(page, "Login_Clicked");
}

    public boolean isDashboardDisplayed() {
        try {
            String dashboardSelector = LocatorReader.getLocator("dashboard", "dashboardHeader", "selector");
            System.out.println("Checking dashboard with selector: " + dashboardSelector);
            
            // Wait for the element to be visible with timeout
            page.waitForSelector(dashboardSelector,
                    new Page.WaitForSelectorOptions()
                            .setState(WaitForSelectorState.VISIBLE)
                            .setTimeout(15000));
            
            boolean isVisible = page.locator(dashboardSelector).isVisible();
            System.out.println("Dashboard element visible: " + isVisible);
            return isVisible;
            
        } catch (Exception e) {
            System.err.println("Dashboard check failed: " + e.getMessage());
            e.printStackTrace();
            
            // Alternative check - look for any page content indicating successful login
            try {
                // Check if we're on a page with the topbar (common in OrangeHRM dashboard)
                boolean hasTopbar = page.locator("//div[contains(@class,'oxd-topbar')]").isVisible();
                System.out.println("Alternative check - Topbar visible: " + hasTopbar);
                return hasTopbar;
            } catch (Exception e2) {
                System.err.println("Alternative check also failed: " + e2.getMessage());
                return false;
            }
        }
    }
}
