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


public class AddnewuserPage
{

private Page page;

public AddnewuserPage(Page page) {
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

public void clickadminmenu()
{
 String adminmenu = LocatorReader.getLocator("admin", "menuAdmin", "selector");
 // 1) Wait for page left menu / dashboard to be ready (OrangeHRM side panel)
    page.waitForSelector("//aside[contains(@class,'oxd-sidepanel')]",
            new Page.WaitForSelectorOptions().setState(WaitForSelectorState.VISIBLE).setTimeout(60000));

    // 2) Wait for Admin menu element to exist
    page.waitForSelector(adminmenu,
            new Page.WaitForSelectorOptions().setState(WaitForSelectorState.ATTACHED).setTimeout(60000));

    Locator menu = page.locator(adminmenu).first();

    // 3) Bring it into view (sometimes it exists but offscreen)
    menu.scrollIntoViewIfNeeded();

    // 4) Now wait until it's visible (if still not visible, we still try clicking)
    try {
        page.waitForSelector(adminmenu,
                new Page.WaitForSelectorOptions().setState(WaitForSelectorState.VISIBLE).setTimeout(15000));
    } catch (Exception ignored) {
        // don't fail here; we'll try click anyway
    }

    // 5) Click with fallback
    try {
        menu.click(new Locator.ClickOptions().setTimeout(30000));
    } catch (Exception e) {
        menu.click(new Locator.ClickOptions().setForce(true).setTimeout(30000));
    }

    // 6) Assert navigation happened (better assertion than "menu is visible")
    page.waitForLoadState(LoadState.NETWORKIDLE);

    String url = page.url();
    Assert.assertTrue(url.contains("admin"),
            "View Systemuser page not opened after clicking menu. Current URL: " + url);

}

public void clickaddBtn()
{
    String addBtn = LocatorReader.getLocator("admin", "addButton", "selector");
    safeClick(addBtn);

    page.waitForLoadState(LoadState.NETWORKIDLE);
    String url = page.url();
    Assert.assertTrue(url.contains("saveSystemUser"),
            "Save System User page not opened after clicking Add button. Current URL: " + url);
}

public void selectUserrole(String UserRole)
{
String userRole = LocatorReader.getLocator("userManagement", "userRoleDropdown", "selector");
waitVisible(userRole);
page.locator(userRole).click();

String optionSelector = LocatorReader.getLocator("userManagement", "optionselect", "selector");
page.locator(optionSelector + "'" + UserRole + "']").first().click();

String selectedRole = page.locator(userRole).first().innerText().trim();    
Assert.assertTrue(selectedRole.contains(UserRole), "Selected user role does not match");

}

public void enterempName(String EmployeeName)
{
    String nameemployee = LocatorReader.getLocator("userManagement", "employeeName", "selector");
    waitVisible(nameemployee);
    page.locator(nameemployee).fill(EmployeeName);


    String suggestion = LocatorReader.getLocator("userManagement", "dropdownsuggestion", "selector");
  String suggestionSelector = suggestion + "'" + EmployeeName + "']";

  Locator suggestion1 = page.locator(suggestionSelector).first();
  suggestion1.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE).setTimeout(20000));
  suggestion1.click();

  Assert.assertEquals(page.locator(nameemployee).inputValue(), EmployeeName,
      "Employee name input value does not match expected.");

}

public void selectstatus(String status)
{
String statusInput = LocatorReader.getLocator("userManagement", "statusDropdown", "selector");
waitVisible(statusInput);
page.locator(statusInput).click();

String opt = LocatorReader.getLocator("userManagement", "optionselect", "selector");
page.locator(opt + "'" + status + "']").first().click();

String selectedStatus = page.locator(statusInput).first().innerText().trim();    
Assert.assertTrue(selectedStatus.contains(status), "Selected status does not match");

}

public void enteruname(String username)
{
  String userName = LocatorReader.getLocator("userManagement", "username", "selector");
Locator userField = page.locator(userName).first();


    userField.waitFor(new Locator.WaitForOptions().setState(com.microsoft.playwright.options.WaitForSelectorState.VISIBLE));
    userField.scrollIntoViewIfNeeded();
    userField.fill(username);

    // Wait until UI really contains the value
    page.waitForCondition(() -> username.equals(userField.inputValue()));

    Assert.assertEquals(userField.inputValue(), username,
            "Username input value does not match expected.");
}

public void enterpwd(String password)
{
    String passWord = LocatorReader.getLocator("userManagement", "password", "selector");
    waitVisible(passWord);
    page.locator(passWord).fill(password);

    Assert.assertEquals(page.locator(passWord).inputValue(), password,
        "Password input value does not match expected.");

}

public void enterconfirmpwd(String confirmpassword)
{
    String confirmPassword = LocatorReader.getLocator("userManagement", "confirmPassword", "selector");
    waitVisible(confirmPassword);
    page.locator(confirmPassword).fill(confirmpassword);

    Assert.assertEquals(page.locator(confirmPassword).inputValue(), confirmpassword,
        "Confirm Password input value does not match expected.");
}

public void saveBTN()
{
    String saveBTN = LocatorReader.getLocator("userManagement", "saveButton", "selector");
    safeClick(saveBTN);

    String toastSelector = LocatorReader.getLocator("userManagement", "toast", "selector");

    page.waitForSelector(toastSelector,
            new Page.WaitForSelectorOptions()
                    .setState(WaitForSelectorState.VISIBLE)
                    .setTimeout(90000));

    Assert.assertTrue(page.locator(toastSelector).isVisible(),
            "User details not saved successfully");
            page.evaluate("window.scrollBy(0, 500)");

}

public void user1(String username)
{

String user1 = LocatorReader.getLocator("admin", "searchUsername", "selector");
waitVisible(user1);
page.locator(user1).fill(username);

Assert.assertEquals(page.locator(user1).inputValue(), username,
        "Username input value does not match expected.");

String srch1 = LocatorReader.getLocator("admin", "searchButton", "selector");
waitVisible(srch1);
safeClick(srch1);
    String currentUrl = page.url();
     Assert.assertTrue(
                currentUrl.contains("admin/viewSystemUsers"),
                "Not findout the record after searching. Current URL: " + currentUrl
     );
     page.evaluate("window.scrollBy(0, 500)");
}
  













}