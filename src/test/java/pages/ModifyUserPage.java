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
import org.slf4j.Logger;
import utils.LoggerUtil;

public class ModifyUserPage
{
    private static final Logger log = LoggerUtil.getLogger(ModifyUserPage.class);
private Page page;

public ModifyUserPage(Page page) {
        this.page = page;
         log.info("ModifyUserdetail object initialized");
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


public void clickdelete()
{
log.info("Clicking on Delete icon");
String deleteBtn = LocatorReader.getLocator("locatoradmin","userManagement","deleteIcon", "selector");
log.debug("Delete Icon selector: {}", deleteBtn);

waitVisible(deleteBtn);
safeClick(deleteBtn);

String popup = LocatorReader.getLocator("locatoradmin","userManagement","confirmDeleteButton", "selector");
log.info("Validating confirmation popup. Actual: {}", popup);

Assert.assertTrue(page.locator(popup).isVisible(), "Delete confirmation popup is not displayed");
log.info("Confirmation pop up validated successfully");
}

public void confirmdelete()
{
log.info("Clicking on Confirmdelete button");

String confirmdelete = LocatorReader.getLocator("locatoradmin","userManagement","confirmDeleteButton", "selector");
log.debug("Confirmdelete button selector: {}", confirmdelete);

waitVisible(confirmdelete);
safeClick(confirmdelete);
page.evaluate("window.scrollBy(0, 500)");

String toastSelector = LocatorReader.getLocator("locatoradmin","userManagement", "toast", "selector");
    log.info("Waiting for toast after saving candidate details");
    page.waitForSelector(toastSelector,
            new Page.WaitForSelectorOptions()
                    .setState(WaitForSelectorState.VISIBLE)
                    .setTimeout(90000));

    Assert.assertTrue(page.locator(toastSelector).isVisible(),
            "User details not deleted successfully");
             log.info("User details deleted successfully");
            }

public void click_logout()
    {
      log.info("Clicking on profile icon");
      String profilebtn = LocatorReader.getLocator("locatortimesheet","profile", "profileMenu","selector");
    Locator profileMenu = page.locator(profilebtn).first();
    log.debug("Profile Icon selector: {}", profileMenu);

    profileMenu.waitFor(new Locator.WaitForOptions()
            .setState(com.microsoft.playwright.options.WaitForSelectorState.VISIBLE));

    profileMenu.click();

    log.info("Clicking on logout option");
    String logbtn = LocatorReader.getLocator("locatortimesheet","profile", "logout","selector");
    Locator logoutBtn = page.locator(logbtn).first();
    
    log.debug("Logout option selector: {}", logoutBtn);

    logoutBtn.waitFor(new Locator.WaitForOptions()
            .setState(com.microsoft.playwright.options.WaitForSelectorState.VISIBLE));

    logoutBtn.click();
    log.info("User logout Successfully");
    
    }







}