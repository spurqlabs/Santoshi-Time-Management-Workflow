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

public class AddnewuserPage
{
private static final Logger log = LoggerUtil.getLogger(AddnewuserPage.class);
private Page page;
public AddnewuserPage(Page page) {
        this.page = page;
        log.info("Addnewuser object initialized");
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

public void clickadminmenu()
{
    log.info("Clicking on Admin menu");
 String adminmenu = LocatorReader.getLocator("locatoradmin","admin", "menuAdmin", "selector");
 // 1) Wait for page left menu / dashboard to be ready (OrangeHRM side panel)


 String sidepanel3 = LocatorReader.getLocator("locatoradmin","admin", "sidepanel3", "selector"); 
    page.waitForSelector(sidepanel3,
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
         log.info("Current URL after clicking Admin menu: {}", url);

    Assert.assertTrue(url.contains("admin"),
            "View Systemuser page not opened after clicking menu. Current URL: " + url);
    log.info("Admin menu navigation successful");

}

public void clickaddBtn()
{
    log.info("Clicking on Add button");
    String addBtn = LocatorReader.getLocator("locatoradmin","admin", "addButton", "selector");
    log.debug("Add button selector: {}", addBtn);
    safeClick(addBtn);

    page.waitForLoadState(LoadState.NETWORKIDLE);
    String url = page.url();
    log.info("Current URL after clicking Admin menu: {}", url);
    Assert.assertTrue(url.contains("saveSystemUser"),
            "Save System User page not opened after clicking Add button. Current URL: " + url);
    log.info("Save System User page navigation successful");
 
}

public void selectUserrole(String UserRole)
{
    log.info("Selecting User Role");
String userRole = LocatorReader.getLocator("locatoradmin","userManagement", "userRoleDropdown", "selector");
log.debug("User role selector: {}", userRole);
log.debug("User role value: {}", UserRole); 

waitVisible(userRole);
page.locator(userRole).click();

String optionSelector = LocatorReader.getLocator("locatoradmin","userManagement", "optionselect", "selector");
page.locator(optionSelector + "'" + UserRole + "']").first().click();

String selectedRole = page.locator(userRole).first().innerText().trim(); 
log.info("Validating User role value. Actual: {}", selectedRole);   
Assert.assertTrue(selectedRole.contains(UserRole), "Selected user role does not match");
log.info("User role value validated successfully");
}

public void enterempName(String EmployeeName)
{
    log.info("Entering Employee Name value");
    String nameemployee = LocatorReader.getLocator("locatoradmin","userManagement", "employeeName", "selector");
    log.debug("Employee Name selector: {}", nameemployee);
    log.debug("Employee Name value: {}", EmployeeName); 
    waitVisible(nameemployee);
    page.locator(nameemployee).fill(EmployeeName);


    String suggestion = LocatorReader.getLocator("locatoradmin","userManagement", "dropdownsuggestion", "selector");
  String suggestionSelector = suggestion + "'" + EmployeeName + "']";

  Locator suggestion1 = page.locator(suggestionSelector).first();
  suggestion1.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE).setTimeout(20000));
  suggestion1.click();


  Assert.assertEquals(page.locator(nameemployee).inputValue(), EmployeeName,
      "Employee name input value does not match expected.");
    log.info("Employee Name value validated successfully");
}

public void selectstatus(String status)
{
    log.info("Entering Status");

String statusInput = LocatorReader.getLocator("locatoradmin","userManagement", "statusDropdown", "selector");
log.debug("Status selector: {}", statusInput);
log.debug("Status  value: {}", status); 

waitVisible(statusInput);
page.locator(statusInput).click();

String opt = LocatorReader.getLocator("locatoradmin","userManagement", "optionselect", "selector");
page.locator(opt + "'" + status + "']").first().click();

String selectedStatus = page.locator(statusInput).first().innerText().trim();  
log.info("Validating Status value. Actual: {}", selectedStatus);

Assert.assertTrue(selectedStatus.contains(status), "Selected status does not match");
log.info("Status value validated successfully");

}

public void enteruname(String username)
{
    log.info("Entering Username value");

  String userName = LocatorReader.getLocator("locatoradmin","userManagement", "username", "selector");
Locator userField = page.locator(userName).first();

 log.debug("Username selector: {}", userField);
 log.debug("Username value: {}", username); 



    userField.waitFor(new Locator.WaitForOptions().setState(com.microsoft.playwright.options.WaitForSelectorState.VISIBLE));
    userField.scrollIntoViewIfNeeded();
    userField.fill(username);

    // Wait until UI really contains the value
    page.waitForCondition(() -> username.equals(userField.inputValue()));

    Assert.assertEquals(userField.inputValue(), username,
            "Username input value does not match expected.");
            log.info("Username value validated successfully");
}

public void enterpwd(String password)
{
    log.info("Entering Password value");

    String passWord = LocatorReader.getLocator("locatoradmin","userManagement", "password", "selector");
    log.debug("Password selector: {}", passWord);
    log.debug("Password value: {}", password); 

    waitVisible(passWord);
    page.locator(passWord).fill(password);

    Assert.assertEquals(page.locator(passWord).inputValue(), password,
        "Password input value does not match expected.");
        log.info("Password value validated successfully");

}

public void enterconfirmpwd(String confirmpassword)
{
        log.info("Entering Confirm Password value");

    String confirmPassword = LocatorReader.getLocator("locatoradmin","userManagement", "confirmPassword", "selector");
    log.debug("Confirm Password selector: {}", confirmPassword);
    log.debug("Confirm Password value: {}", confirmpassword); 

    waitVisible(confirmPassword);
    page.locator(confirmPassword).fill(confirmpassword);

    Assert.assertEquals(page.locator(confirmPassword).inputValue(), confirmpassword,
        "Confirm Password input value does not match expected.");
        log.info("Confirm Password value validated successfully");
}

public void saveBTN()
{
    log.info("Clicking on Save Button");
    String saveBTN = LocatorReader.getLocator("locatoradmin","userManagement", "saveButton", "selector");
    safeClick(saveBTN);

    String toastSelector = LocatorReader.getLocator("locatoradmin","userManagement", "toast", "selector");
    log.info("Waiting for toast after saving candidate details");

    page.waitForSelector(toastSelector,
            new Page.WaitForSelectorOptions()
                    .setState(WaitForSelectorState.VISIBLE)
                    .setTimeout(90000));

    Assert.assertTrue(page.locator(toastSelector).isVisible(),
            "User details not saved successfully");
            page.evaluate("window.scrollBy(0, 500)");
            log.info("User details saved successfully");

}

public void user1(String username)
{
log.info("Entering Username value");
String user1 = LocatorReader.getLocator("locatoradmin","admin", "searchUsername", "selector");
log.debug("Username selector: {}", user1);
log.debug("Username value: {}", username); 

waitVisible(user1);
page.locator(user1).fill(username);

Assert.assertEquals(page.locator(user1).inputValue(), username,
        "Username input value does not match expected.");
        log.info("Username value validated successfully");

log.info("Clicking on search button");
String srch1 = LocatorReader.getLocator("locatoradmin","admin", "searchButton", "selector");
log.debug("Search button selector: {}", srch1);
waitVisible(srch1);
safeClick(srch1);
    String currentUrl = page.url();
     Assert.assertTrue(
                currentUrl.contains("admin/viewSystemUsers"),
                "Not findout the record after searching. Current URL: " + currentUrl
     );
     page.evaluate("window.scrollBy(0, 500)");
     log.info("User details findout successfully");
}
  













}