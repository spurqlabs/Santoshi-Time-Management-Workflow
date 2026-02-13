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

public class EditdetailPage
{


private Page page;

public EditdetailPage(Page page) {
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

public void clickedit()
{

String editBtn = LocatorReader.getLocator("userManagement","editButton", "selector");
waitVisible(editBtn);
safeClick(editBtn);

String currentUrl = page.url();
        Assert.assertTrue(
                currentUrl.contains("saveSystemUser"),
                "Not navigated to saveSystemUser page after clicking edit button. Current URL: " + currentUrl
        );
}

public void editUserRole(String newUserRole)
{
String editRoleInput = LocatorReader.getLocator("userManagement","userRoleDropdown", "selector");
waitVisible(editRoleInput);
page.locator(editRoleInput).click();

String optionSelector = LocatorReader.getLocator("userManagement", "optionselect", "selector");
page.locator(optionSelector + "'" + newUserRole + "']").first().click();

String selectedRole = page.locator(editRoleInput).first().innerText().trim();    
Assert.assertTrue(selectedRole.contains(newUserRole), "Selected user role does not match");
}

public void editstatus(String newStatus)
{

String editstatus = LocatorReader.getLocator("userManagement","statusDropdown", "selector");
page.locator(editstatus).click();

String opt = LocatorReader.getLocator("userManagement", "optionselect", "selector");
page.locator(opt + "'" + newStatus + "']").first().click();

String selectedStatus = page.locator(editstatus).first().innerText().trim();    
Assert.assertTrue(selectedStatus.contains(newStatus), "Selected status does not match");
}

public void clickSAVE()
{

    String SAVE = LocatorReader.getLocator("userManagement", "saveButton", "selector");
    safeClick(SAVE);

    String toastSelector = LocatorReader.getLocator("userManagement", "toast", "selector");

    page.waitForSelector(toastSelector,
            new Page.WaitForSelectorOptions()
                    .setState(WaitForSelectorState.VISIBLE)
                    .setTimeout(90000));

    Assert.assertTrue(page.locator(toastSelector).isVisible(),
            "User details not updated successfully");
            page.evaluate("window.scrollBy(0, 500)");
}

public void user1(String username)
{

String edituser = LocatorReader.getLocator("admin", "searchUsername", "selector");
waitVisible(edituser);
page.locator(edituser).fill(username);

Assert.assertEquals(page.locator(edituser).inputValue(), username,
        "Username input value does not match expected.");

String editsrch = LocatorReader.getLocator("admin", "searchButton", "selector");
waitVisible(editsrch);
safeClick(editsrch);
    String currentUrl = page.url();
     Assert.assertTrue(
                currentUrl.contains("admin/viewSystemUsers"),
                "Not findout the record after searching. Current URL: " + currentUrl
     );
     page.evaluate("window.scrollBy(0, 500)");
}


















}