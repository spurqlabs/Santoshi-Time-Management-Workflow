package pages;

import com.microsoft.playwright.Page;
import utils.LocatorReader;
import utils.TestDataReader;
import utils.ScreenshotUtils;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.options.LoadState;

public class LoginPage {

    private Page page;
    private TestDataReader testData;

    public LoginPage(Page page) {
        this.page = page;
        this.testData = new TestDataReader("Testdata/timesheetData.json");
    }

    public void enterUsername() {
        page.setDefaultTimeout(1000);

        page.locator(LocatorReader.getLocator("login", "username", "selector"))
            .fill(testData.getData("register.username"));
        ScreenshotUtils.captureScreenshot(page, "Username_Entered");
    }

    public void enterPassword() {
        page.setDefaultTimeout(1000);

        page.locator(LocatorReader.getLocator("login", "password", "selector"))
            .fill(testData.getData("register.password"));
        ScreenshotUtils.captureScreenshot(page, "Password_Entered");
    }

   public void clickLogin() {
    page.setDefaultTimeout(10000);

    Locator loginButton =
        page.locator(LocatorReader.getLocator("login", "loginButton", "selector"));

    loginButton.click(new Locator.ClickOptions().setNoWaitAfter(true));

    page.waitForLoadState(LoadState.DOMCONTENTLOADED);
    ScreenshotUtils.captureScreenshot(page, "Login_Clicked");
}

    public boolean isDashboardDisplayed() {
        return page.locator(
            LocatorReader.getLocator("login", "dashboardHeader", "selector")
        ).isVisible();
    }
}
