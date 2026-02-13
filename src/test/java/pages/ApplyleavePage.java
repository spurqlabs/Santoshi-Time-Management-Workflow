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

public class ApplyleavePage 
{
private Page page;

public ApplyleavePage(Page page) {
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

public void clickapplytab()
{
String applytab = LocatorReader.getLocator("leave", "applyTab", "selector");
page.waitForSelector(applytab,
            new Page.WaitForSelectorOptions().setState(WaitForSelectorState.VISIBLE).setTimeout(60000));
    page.locator(applytab).first().click();

    String leaveactual = LocatorReader.getLocator("leave", "leaveType", "selector");
page.waitForSelector(leaveactual,
            new Page.WaitForSelectorOptions().setState(WaitForSelectorState.VISIBLE).setTimeout(60000));

    Assert.assertTrue(page.locator(leaveactual).first().isVisible(),
            "Add leave Entitlements page did not open (Leave Type input not visible).");

}

public void leavetype(String leaveType)
{
String leavetypeInput = LocatorReader.getLocator("leave", "leaveType", "selector");
// Wait for the dropdown to be visible
        waitVisible(leavetypeInput);
        
        // Wait a moment for the page to settle
        page.waitForTimeout(500);
        
        // Click the dropdown to open it
        page.locator(leavetypeInput).click();

        // Wait for the dropdown options to appear
        page.waitForTimeout(2000);

        
        System.out.println("Looking for Leave Type: " + leaveType);
        
        // Try multiple selection strategies
        boolean selected = false;
        
        // Strategy 1: Try exact text match with normalize-space
        try {
            String option = LocatorReader.getLocator("leave", "optionselect", "selector");
             String option1 = option + "'" + leaveType + "']";
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
                String option2 = "//div[@role='option' and contains(normalize-space(), '" + leaveType + "')]";
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
                String option3 = "//*[contains(text(), '" + leaveType + "') and @role='option']";
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
                    if (optionText.contains(leaveType)) {
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
Assert.assertTrue(selectedVacancy.contains(leaveType), "Selected leave type does not match");
}



public void selectfromdate(String fromDate)
{
String fromdateInput = LocatorReader.getLocator("leave", "applyFromDate", "selector");
waitVisible(fromdateInput);
page.locator(fromdateInput).fill(fromDate);
String actualfromdate = page.locator(fromdateInput).inputValue();       
Assert.assertEquals(actualfromdate, fromDate, "From date input value does not match expected.");
}

public void selecttodate(String toDate)
{

String todateInput = LocatorReader.getLocator("leave", "applyToDate", "selector");
waitVisible(todateInput);
  Locator to = page.locator(todateInput);
to.click();
  to.press("Control+A");
  to.press("Backspace");
    to.type(toDate, new Locator.TypeOptions().setDelay(50));

  to.press("Tab");

String actualtodate = to.inputValue().trim();       
Assert.assertEquals(actualtodate, toDate, "To date input value does not match expected.");

}

public void selectpartial(String partialDay)
{
String partialInput = LocatorReader.getLocator("leave", "partialDays", "selector");
waitVisible(partialInput);
page.locator(partialInput).click(); 
String option = LocatorReader.getLocator("leave", "optionselect", "selector");
 String option1 = option + "'" + partialDay + "']";
page.locator(option1).first().click();
String selectedpartial = page.locator(partialInput).first().innerText().trim();
Assert.assertTrue(selectedpartial.contains(partialDay), "Selected partial day does not match");

}

public void selectduration(String duration)
{
String durationInput = LocatorReader.getLocator("leave", "duration", "selector");
waitVisible(durationInput);
page.locator(durationInput).click();
String option = LocatorReader.getLocator("leave", "optionselect", "selector");
 String option1 = option + "'" + duration + "']";
page.locator(option1).first().click();
String selectedduration = page.locator(durationInput).first().innerText().trim();   
Assert.assertTrue(selectedduration.contains(duration), "Selected duration does not match");
}

public void entercmt(String comment)
{
String cmtInput = LocatorReader.getLocator("leave", "comment", "selector");
waitVisible(cmtInput);
page.locator(cmtInput).fill(comment);
String actualcmt = page.locator(cmtInput).inputValue();
Assert.assertEquals(actualcmt, comment, "Comment input value does not match expected.");
}

public void clickapply()
{

    String applybtn = LocatorReader.getLocator("leave", "applyButton", "selector");
    waitVisible(applybtn);
    page.locator(applybtn).click();
  String toastMessage = "//div[contains(@class,'oxd-toast')]//p[contains(@class,'oxd-text--toast-message')]";

    Locator toast = page.locator(toastMessage).first();
  toast.waitFor(new Locator.WaitForOptions()
      .setState(WaitForSelectorState.VISIBLE)
      .setTimeout(15000));

  String msg = toast.innerText().trim();
  Assert.assertTrue(msg.toLowerCase().contains("success"),
      "Leave apply did not succeed. Toast message was: " + msg);
}





}