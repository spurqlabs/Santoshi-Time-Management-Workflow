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

public class EditdetailPage
{

private static final Logger log = LoggerUtil.getLogger(EditdetailPage.class);
private Page page;

public EditdetailPage(Page page) {
        this.page = page;
         log.info("Editdetail object initialized");
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

public void clickedit()
{

log.info("Clicking on Edit icon");
String editBtn = LocatorReader.getLocator("locatoradmin","userManagement","editButton", "selector");
log.debug("Edit Icon selector: {}", editBtn);
waitVisible(editBtn);
safeClick(editBtn);

String currentUrl = page.url();
log.info("Current URL after clicking Edit icon: {}", currentUrl);
        Assert.assertTrue(
                currentUrl.contains("saveSystemUser"),
                "Not navigated to saveSystemUser page after clicking edit button. Current URL: " + currentUrl
        );
        log.info("SaveSystemUser page pagenavigation successful");
}

public void editUserRole(String newUserRole)
{
        log.info("Selecting New User role value");
String editRoleInput = LocatorReader.getLocator("locatoradmin","userManagement","userRoleDropdown", "selector");
log.debug("New User role selector: {}", editRoleInput);
log.debug("New User role value: {}", newUserRole); 

waitVisible(editRoleInput);
page.locator(editRoleInput).click();

String optionSelector = LocatorReader.getLocator("locatoradmin","userManagement", "optionselect", "selector");
page.locator(optionSelector + "'" + newUserRole + "']").first().click();

String selectedRole = page.locator(editRoleInput).first().innerText().trim();
log.info("Validating New User role value. Actual: {}", selectedRole);    
Assert.assertTrue(selectedRole.contains(newUserRole), "Selected user role does not match");
log.info("New User role value validated successfully");

}

public void editstatus(String newStatus)
{
log.info("Selecting New Status");
String editstatus = LocatorReader.getLocator("locatoradmin","userManagement","statusDropdown", "selector");
log.debug("New Status selector: {}", editstatus);
log.debug("New Status value: {}", newStatus); 

page.locator(editstatus).click();

String opt = LocatorReader.getLocator("locatoradmin","userManagement", "optionselect", "selector");
page.locator(opt + "'" + newStatus + "']").first().click();

String selectedStatus = page.locator(editstatus).first().innerText().trim();
log.info("Validating New status value. Actual: {}", selectedStatus);

Assert.assertTrue(selectedStatus.contains(newStatus), "Selected status does not match");
log.info("New status value validated successfully");
}

public void clickSAVE()
{
log.info("Clicking on Save button");
    String SAVE = LocatorReader.getLocator("locatoradmin","userManagement", "saveButton", "selector");
    safeClick(SAVE);

    String toastSelector = LocatorReader.getLocator("locatoradmin","userManagement", "toast", "selector");
   log.info("Waiting for toast after saving user details");

    page.waitForSelector(toastSelector,
            new Page.WaitForSelectorOptions()
                    .setState(WaitForSelectorState.VISIBLE)
                    .setTimeout(90000));

    Assert.assertTrue(page.locator(toastSelector).isVisible(),
            "User details not updated successfully");
            page.evaluate("window.scrollBy(0, 500)");
             log.info("User details saved successfully");
}

public void user1(String username)
{
log.info("Entering Username value");
String edituser = LocatorReader.getLocator("locatoradmin","admin", "searchUsername", "selector");
log.debug("Username selector: {}", edituser);
log.debug("Username value: {}", username); 

waitVisible(edituser);
page.locator(edituser).fill(username);

Assert.assertEquals(page.locator(edituser).inputValue(), username,
        "Username input value does not match expected.");
log.info("Username value validated successfully");

log.info("Clicking on search button");
String editsrch = LocatorReader.getLocator("locatoradmin","admin", "searchButton", "selector");
log.debug("Search button selector: {}", editsrch);

waitVisible(editsrch);
safeClick(editsrch);
    String currentUrl = page.url();
     Assert.assertTrue(
                currentUrl.contains("admin/viewSystemUsers"),
                "Not findout the record after searching. Current URL: " + currentUrl
     );
     page.evaluate("window.scrollBy(0, 500)");
     log.info("User details findout successfully");
}


















}