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

public class AddEntitlementsPage 
{

 private static final Logger log = LoggerUtil.getLogger(AddEntitlementsPage.class);

private Page page;
private TestDataReader testData;

private static final String ENTITLEMENT_SCENARIO = "TC006 - Add Entitlements for the apply leaves";

public AddEntitlementsPage(Page page) {
        this.page = page;
        this.testData = new TestDataReader("Testdata/leaveData.json");
         log.info("AddEntitlements object initialized");
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

public void clickleavemenu()
{
    log.info("Clicking on Leave menu");

 String leaveMenu = LocatorReader.getLocator("locatorleave","leave", "menuLeave", "selector");

    // 1) Wait for page left menu / dashboard to be ready (OrangeHRM side panel)
    String sidepane2 = LocatorReader.getLocator("locatorleave","leave", "sidepanel2", "selector");
    page.waitForSelector(sidepane2,
            new Page.WaitForSelectorOptions().setState(WaitForSelectorState.VISIBLE).setTimeout(60000));

    // 2) Wait for Recruitment menu element to exist
    page.waitForSelector(leaveMenu,
            new Page.WaitForSelectorOptions().setState(WaitForSelectorState.ATTACHED).setTimeout(60000));

    Locator menu = page.locator(leaveMenu).first();

    // 3) Bring it into view (sometimes it exists but offscreen)
    menu.scrollIntoViewIfNeeded();

    // 4) Now wait until it's visible (if still not visible, we still try clicking)
    try {
        page.waitForSelector(leaveMenu,
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
     log.info("Current URL after clicking Recruitment menu: {}", url);

    Assert.assertTrue(url.contains("leave"), "View Leave page not opened after clicking menu. Current URL: " + url);
    log.info("Leave menu navigation successful");

}

public void clickentitlementstab()
{
log.info("Clicking on Entitlements tab");

String entitlementtab = LocatorReader.getLocator("locatorleave","leave", "entitlementsTab", "selector");
log.debug("Entitlements tab selector: {}", entitlementtab);

page.waitForSelector(entitlementtab,
            new Page.WaitForSelectorOptions().setState(WaitForSelectorState.VISIBLE).setTimeout(60000));
    page.locator(entitlementtab).first().click();

String addentitlementsOption = LocatorReader.getLocator("locatorleave","leave", "addEntitlementsButton", "selector");
log.debug("Add Entitlements tab selector: {}", addentitlementsOption);

page.waitForSelector(addentitlementsOption,
            new Page.WaitForSelectorOptions().setState(WaitForSelectorState.VISIBLE).setTimeout(60000));
    page.locator(addentitlementsOption).first().click();

String actualempname = LocatorReader.getLocator("locatorleave","leave", "employeeName", "selector");
log.info("Validating Employee Name value. Actual: {}", actualempname);

page.waitForSelector(actualempname,
            new Page.WaitForSelectorOptions().setState(WaitForSelectorState.VISIBLE).setTimeout(60000));

    Assert.assertTrue(page.locator(actualempname).first().isVisible(),
            "Add leave Entitlements page did not open (Employee Name input not visible).");
log.info("Employee Name value validated successfully");
}

public void enteremployeeName()
{
String empName = testData.getData(ENTITLEMENT_SCENARIO, "employeeName");
String empNameInput = LocatorReader.getLocator("locatorleave","leave", "employeeName", "selector");

log.info("Entering Employee Name value");
log.debug("Employee Name selector: {}", empNameInput);
log.debug("Employee Name value: {}", empName); 


waitVisible(empNameInput);
page.locator(empNameInput).fill(empName);

String suggestionTemplate = LocatorReader.getLocator("locatorleave","leave", "dropdownsuggestion", "selector");
  String suggestionSelector = suggestionTemplate + "'" + empName + "']";

  Locator suggestion = page.locator(suggestionSelector).first();
  log.info("Validating Employee Name value. Actual: {}", empNameInput);

  suggestion.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE).setTimeout(15000));
  suggestion.click();

  Assert.assertEquals(page.locator(empNameInput).inputValue(), empName,
      "Employee name input value does not match expected.");
      log.info("Employee Name value validated successfully");

}

public void selectleaveType()
{
String leavetype = testData.getData(ENTITLEMENT_SCENARIO, "leavetype");
String leavetypeInput = LocatorReader.getLocator("locatorleave","leave", "leaveType", "selector");

log.info("Selecting Leave Type");
log.debug("Leave Type selector: {}", leavetypeInput);
log.debug("Leave Type value: {}", leavetype); 

// Wait for the dropdown to be visible
        waitVisible(leavetypeInput);
        
        // Wait a moment for the page to settle
        page.waitForTimeout(500);
        
        // Click the dropdown to open it
        page.locator(leavetypeInput).click();

        // Wait for the dropdown options to appear
        page.waitForTimeout(2000);

        
        System.out.println("Looking for Leave Type: " + leavetype);
        
        // Try multiple selection strategies
        boolean selected = false;
        
        // Strategy 1: Try exact text match with normalize-space
        try {
            String option = LocatorReader.getLocator("locatorleave","leave", "optionselect", "selector");
             String option1 = option + "'" + leavetype + "']";
            if (page.locator(option1).count() > 0) {
                log.info("Leave type found using strategy 1 (exact match)");
               
                page.locator(option1).first().click();
                selected = true;
            }
        } catch (Exception e) {
            log.warn("Strategy 1 failed: {}", e.getMessage());
            
        }
        
        // Strategy 2: Try contains text
        if (!selected) {
            try {
                  
                  String option2 = LocatorReader.getLocator("locatorleave","leave", "optionselect", "selector");
                  String option2WithVacancy = option2 + "'" + leavetype + "']";

                if (page.locator(option2WithVacancy).count() > 0) {
                    log.info("Leave type found using strategy 2 (exact match)");
                    
                    page.locator(option2WithVacancy).first().click();
                    selected = true;
                }
            } catch (Exception e) {
                 log.warn("Strategy 2 failed: {}", e.getMessage());
               
            }
        }
        
        // Strategy 3: Try clicking any div with role=option that contains the country text
        if (!selected) {
            try {

                String optionTemplate1  = LocatorReader.getLocator("locatorleave","leave", "dropdownOptionContains", "selector");
                String option5 = optionTemplate1.replace("{value}", leavetype);
                Locator option6 = page.locator(option5);


                if (option6.count() > 0) {

                 log.info("Leave type found using strategy 3 (exact match)");
                    
                    option6.first().click();
                    selected = true;
                }
            } 
            
            catch (Exception e) {
                log.warn("Strategy 3 failed: {}", e.getMessage());
               
            }
        
        }
        
        // Strategy 4: Get all options and print them for debugging
        if (!selected) {
            try {
          log.warn("Specific leaves option not found. Available options:(debug).");

               String optionsLocator1 = LocatorReader.getLocator("locatorleave","leave", "rolecount1", "selector");
            Locator opt1 = page.locator(optionsLocator1);

                int count = opt1.count();
                 log.info("Total leave type options found: {}", count);
                
                for (int i = 0; i < Math.min(count, 10); i++) {
                    String optionText = opt1.nth(i).textContent();
                 log.debug("Option {}: {}", i, optionText);
                    
                    // Try to select if it contains our leave type
                    if (optionText.contains(leavetype)) {
                   log.info("Found match at index {}", i);
                        
                        opt1.nth(i).click();
                        selected = true;
                        break;
                    }
                }
            } catch (Exception e) {
                log.warn("Strategy 4 failed: {}", e.getMessage());
                
            }
        }
        
        // Fallback: Click first option
        if (!selected) {
            try {

                System.out.println("Selecting first available option as fallback");
                String optionsLocator2 = LocatorReader.getLocator("locatorleave","leave", "rolecount1", "selector");
            Locator opt2 = page.locator(optionsLocator2);

                opt2.first().click();
                selected = true;
            } catch (Exception e) {
                System.out.println("Fallback selection failed: " + e.getMessage());
                ScreenshotUtils.captureScreenshot(page, "vacancy_Selection_Failed");
                throw new RuntimeException("Failed to select vacancy from dropdown", e);
            }
        }
        
        // Wait for the dropdown to close
        page.waitForTimeout(500);

        String selectedText = page.locator(leavetypeInput).textContent();
       log.info("Validating Leave type. Actual: {}", selectedText);

        ScreenshotUtils.captureScreenshot(page, "Leave type_Selected");
        String selectedVacancy = page.locator(leavetypeInput).first().innerText().trim(); 

Assert.assertTrue(selectedVacancy.contains(leavetype), "Selected leave type does not match");
log.info("Leave type value validated successfully");
}


public void enterentitlement()
{
String entitlement = testData.getData(ENTITLEMENT_SCENARIO, "entitlement");
String entitlementInput = LocatorReader.getLocator("locatorleave","leave", "entitlement", "selector");
log.info("Entering Entitlement value");
log.debug("Entitlement selector: {}", entitlementInput);
log.debug("Entitlement value: {}", entitlement); 


waitVisible(entitlementInput);
page.locator(entitlementInput).fill(entitlement);   
String actualentitlement = page.locator(entitlementInput).inputValue();
log.info("Validating Entitlement value. Actual: {}", actualentitlement);
Assert.assertEquals(actualentitlement, entitlement, "Entitlement input value does not match expected.");
log.info("Entitlement value validated successfully");

}

public void clickonsavebtn()
{
    log.info("Clicking on Save_Button");
String btn = LocatorReader.getLocator("locatorleave","leave", "save", "selector");
waitVisible(btn);
page.locator(btn).click();
log.info("Details saved successfully");
}

public void clickonconfirm()
{
    log.info("Clicking on Confirm button");
String confirmbtn = LocatorReader.getLocator("locatorleave","leave", "confirm", "selector");
waitVisible(confirmbtn);
page.locator(confirmbtn).click();
log.info("Confirmation validated successfully");
}

}

