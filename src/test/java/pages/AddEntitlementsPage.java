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

public class AddEntitlementsPage 
{

private Page page;
private TestDataReader testData;

private static final String ENTITLEMENT_SCENARIO = "TC006 - Add Entitlements for the apply leaves";

public AddEntitlementsPage(Page page) {
        this.page = page;
        this.testData = new TestDataReader("Testdata/leaveData.json");
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

public void clickleavemenu()
{
 String leaveMenu = LocatorReader.getLocator("leave", "menuLeave", "selector");

    // 1) Wait for page left menu / dashboard to be ready (OrangeHRM side panel)
    page.waitForSelector("//aside[contains(@class,'oxd-sidepanel')]",
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
    Assert.assertTrue(url.contains("leave"),
            "View Leave page not opened after clicking menu. Current URL: " + url);
}

public void clickentitlementstab()
{
String entitlementtab = LocatorReader.getLocator("leave", "entitlementsTab", "selector");
page.waitForSelector(entitlementtab,
            new Page.WaitForSelectorOptions().setState(WaitForSelectorState.VISIBLE).setTimeout(60000));
    page.locator(entitlementtab).first().click();

String addentitlementsOption = LocatorReader.getLocator("leave", "addEntitlementsButton", "selector");
page.waitForSelector(addentitlementsOption,
            new Page.WaitForSelectorOptions().setState(WaitForSelectorState.VISIBLE).setTimeout(60000));
    page.locator(addentitlementsOption).first().click();

String actualempname = LocatorReader.getLocator("leave", "employeeName", "selector");
page.waitForSelector(actualempname,
            new Page.WaitForSelectorOptions().setState(WaitForSelectorState.VISIBLE).setTimeout(60000));

    Assert.assertTrue(page.locator(actualempname).first().isVisible(),
            "Add leave Entitlements page did not open (Employee Name input not visible).");

}

public void enteremployeeName()
{
String empName = testData.getData(ENTITLEMENT_SCENARIO, "employeeName");
String empNameInput = LocatorReader.getLocator("leave", "employeeName", "selector");
waitVisible(empNameInput);
page.locator(empNameInput).fill(empName);

String suggestionTemplate = LocatorReader.getLocator("leave", "dropdownsuggestion", "selector");
  String suggestionSelector = suggestionTemplate + "'" + empName + "']";

  Locator suggestion = page.locator(suggestionSelector).first();
  suggestion.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE).setTimeout(15000));
  suggestion.click();

  Assert.assertEquals(page.locator(empNameInput).inputValue(), empName,
      "Employee name input value does not match expected.");

}

public void selectleaveType()
{
String leavetype = testData.getData(ENTITLEMENT_SCENARIO, "leavetype");
String leavetypeInput = LocatorReader.getLocator("leave", "leaveType", "selector");
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
            String option = LocatorReader.getLocator("leave", "optionselect", "selector");
             String option1 = option + "'" + leavetype + "']";
            if (page.locator(option1).count() > 0) {
                System.out.println("Found option using strategy 1");
                page.locator(option1).first().click();
                selected = true;
            }
        } catch (Exception e) {
            System.out.println("Strategy 1 failed: " + e.getMessage());
        }
        
        // Strategy 2: Try contains text
        if (!selected) {
            try {
                String option2 = "//div[@role='option' and contains(normalize-space(), '" + leavetype + "')]";
                if (page.locator(option2).count() > 0) {
                    System.out.println("Found option using strategy 2");
                    page.locator(option2).first().click();
                    selected = true;
                }
            } catch (Exception e) {
                System.out.println("Strategy 2 failed: " + e.getMessage());
            }
        }
        
        // Strategy 3: Try clicking any div with role=option that contains the country text
        if (!selected) {
            try {
                String option3 = "//*[contains(text(), '" + leavetype + "') and @role='option']";
                if (page.locator(option3).count() > 0) {
                    System.out.println("Found option using strategy 3");
                    page.locator(option3).first().click();
                    selected = true;
                }
            } catch (Exception e) {
                System.out.println("Strategy 3 failed: " + e.getMessage());
            }
        }
        
        // Strategy 4: Get all options and print them for debugging
        if (!selected) {
            try {
                System.out.println("Specific country option not found. Available options:");
                int count = page.locator("//div[@role='option']").count();
                System.out.println("Total options found: " + count);
                
                for (int i = 0; i < Math.min(count, 10); i++) {
                    String optionText = page.locator("//div[@role='option']").nth(i).textContent();
                    System.out.println("Option " + i + ": " + optionText);
                    
                    // Try to select if it contains our leave type
                    if (optionText.contains(leavetype)) {
                        System.out.println("Found match at index " + i);
                        page.locator("//div[@role='option']").nth(i).click();
                        selected = true;
                        break;
                    }
                }
            } catch (Exception e) {
                System.out.println("Strategy 4 failed: " + e.getMessage());
            }
        }
        
        // Fallback: Click first option
        if (!selected) {
            try {
                System.out.println("Selecting first available option as fallback");
                page.locator("//div[@role='option']").first().click();
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

        ScreenshotUtils.captureScreenshot(page, "Leave type_Selected");
        String selectedVacancy = page.locator(leavetypeInput).first().innerText().trim();    
Assert.assertTrue(selectedVacancy.contains(leavetype), "Selected leave type does not match");
}



public void enterentitlement()
{
String entitlement = testData.getData(ENTITLEMENT_SCENARIO, "entitlement");
String entitlementInput = LocatorReader.getLocator("leave", "entitlement", "selector");
waitVisible(entitlementInput);
page.locator(entitlementInput).fill(entitlement);   
String actualentitlement = page.locator(entitlementInput).inputValue();
Assert.assertEquals(actualentitlement, entitlement, "Entitlement input value does not match expected.");
}

public void clickonsavebtn()
{
String btn = LocatorReader.getLocator("leave", "save", "selector");
waitVisible(btn);
page.locator(btn).click();
}

public void clickonconfirm()
{
String confirmbtn = LocatorReader.getLocator("leave", "confirm", "selector");
waitVisible(confirmbtn);
page.locator(confirmbtn).click();
}

}

