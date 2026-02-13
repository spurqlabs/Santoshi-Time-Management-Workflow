package pages;

import com.microsoft.playwright.Page;
import utils.LocatorReader;
import utils.TestDataReader;
import utils.ScreenshotUtils;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.options.LoadState;
import com.microsoft.playwright.options.WaitForSelectorState;
import org.testng.Assert;
import com.microsoft.playwright.options.WaitUntilState;
import java.time.LocalDate;
import java.util.Locale;
import java.time.format.TextStyle;

public class ModifyUserPage
{
private Page page;

public ModifyUserPage(Page page) {
        this.page = page;
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


public void clickdelete()
{

String deleteBtn = LocatorReader.getLocator("userManagement","deleteIcon", "selector");
waitVisible(deleteBtn);
safeClick(deleteBtn);

String popup = LocatorReader.getLocator("userManagement","confirmDeleteButton", "selector");
Assert.assertTrue(page.locator(popup).isVisible(), "Delete confirmation popup is not displayed");
}

public void confirmdelete()
{

String confirmdelete = LocatorReader.getLocator("userManagement","confirmDeleteButton", "selector");
waitVisible(confirmdelete);
safeClick(confirmdelete);
page.evaluate("window.scrollBy(0, 500)");

String toastSelector = LocatorReader.getLocator("userManagement", "toast", "selector");
    page.waitForSelector(toastSelector,
            new Page.WaitForSelectorOptions()
                    .setState(WaitForSelectorState.VISIBLE)
                    .setTimeout(90000));

    Assert.assertTrue(page.locator(toastSelector).isVisible(),
            "User details not deleted successfully");
            }

public void click_logout()
    {

    String profilebtn = LocatorReader.getLocator("profile", "profileMenu","selector");
    Locator profileMenu = page.locator(profilebtn).first();
    profileMenu.waitFor(new Locator.WaitForOptions()
            .setState(com.microsoft.playwright.options.WaitForSelectorState.VISIBLE));

    profileMenu.click();

    String logbtn = LocatorReader.getLocator("profile", "logout","selector");
    Locator logoutBtn = page.locator(logbtn).first();
    logoutBtn.waitFor(new Locator.WaitForOptions()
            .setState(com.microsoft.playwright.options.WaitForSelectorState.VISIBLE));

    logoutBtn.click();
    
    }







}