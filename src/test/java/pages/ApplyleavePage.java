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

public class ApplyleavePage 
{
    private static final Logger log = LoggerUtil.getLogger(ApplyleavePage.class);
private Page page;

public ApplyleavePage(Page page) {
        this.page = page;
         log.info("Applyleave object initialized");
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

public void clickapplytab()
{
    log.info("Clicking on Apply Tab");
String applytab = LocatorReader.getLocator("locatorleave","leave", "applyTab", "selector");
log.debug("Apply Tab selector: {}", applytab);
page.waitForSelector(applytab,
            new Page.WaitForSelectorOptions().setState(WaitForSelectorState.VISIBLE).setTimeout(60000));
    page.locator(applytab).first().click();

    String leaveactual = LocatorReader.getLocator("locatorleave","leave", "leaveType", "selector");
log.info("Validating Add leave Entitlements page. Actual: {}", leaveactual);
page.waitForSelector(leaveactual,
            new Page.WaitForSelectorOptions().setState(WaitForSelectorState.VISIBLE).setTimeout(60000));

    Assert.assertTrue(page.locator(leaveactual).first().isVisible(),
            "Add leave Entitlements page did not open (Leave Type input not visible).");
   log.info("Add leave Entitlements page navigation successful");
}

public void leavetype(String leaveType)
{
String leavetypeInput = LocatorReader.getLocator("locatorleave","leave", "leaveType", "selector");
log.info("Selecting Leave Type");
log.debug("Leave Type selector: {}", leavetypeInput);
log.debug("Leave Type value: {}", leaveType); 


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
            String option = LocatorReader.getLocator("locatorleave","leave", "optionselect", "selector");
             String option1 = option + "'" + leaveType + "']";
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

                String option3 = LocatorReader.getLocator("locatorleave","leave", "optionselect1", "selector");
                  String option3WithVacancy = option3 + "'" + leaveType + "']";
                
                if (page.locator(option3WithVacancy).count() > 0) {
                    log.info("Leave type found using strategy 2 (exact match)");
                    page.locator(option3WithVacancy).first().click();
                    selected = true;
                }
            } catch (Exception e) {
                log.warn("Strategy 2 failed: {}", e.getMessage());
            }
        }
        
        // Strategy 3: Try clicking any div with role=option that contains the country text
        if (!selected) {
            try {
                
                String optionTemplate2  = LocatorReader.getLocator("locatorleave","leave", "dropdownOptionContains", "selector");
                String option7 = optionTemplate2.replace("{value}", leaveType);
                Locator option8 = page.locator(option7);


                if (option8.count() > 0) {
                    log.info("Leave type found using strategy 3 (exact match)");
                    option8.first().click();
                    selected = true;
                }
            } catch (Exception e) {
                log.warn("Strategy 3 failed: {}", e.getMessage());
            }
        }
        
        // Strategy 4: Get all options and print them for debugging
        if (!selected) {
            try {
                log.warn("Specific leaves option not found. Available options:(debug).");


                String optionsLocator2 = LocatorReader.getLocator("locatorleave","leave", "rolecount1", "selector");
            Locator opt2 = page.locator(optionsLocator2);

                int count = opt2.count();

                log.info("Total leave type options found: {}", count);
                
                for (int i = 0; i < Math.min(count, 10); i++) {
                    String optionText = opt2.nth(i).textContent();
                   log.debug("Option {}: {}", i, optionText);
                    
                    // Try to select if it contains our leave type
                    if (optionText.contains(leaveType)) {
                       log.info("Found match at index {}", i);
                        opt2.nth(i).click();
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
              String optionsLocator4 = LocatorReader.getLocator("locatorleave","leave", "rolecount1", "selector");
            Locator opt4 = page.locator(optionsLocator4);


                opt4.first().click();
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
        String selectedVacancy = page.locator(leavetypeInput).first().innerText().trim();    
      Assert.assertTrue(selectedVacancy.contains(leaveType), "Selected leave type does not match");
      log.info("Leave type value validated successfully");
        ScreenshotUtils.captureScreenshot(page, "Leave type_Selected");
        
}



public void selectfromdate(String fromDate)
{
String fromdateInput = LocatorReader.getLocator("locatorleave","leave", "applyFromDate", "selector");
log.info("Selecting From Date");
log.debug("From Date selector: {}", fromdateInput);
log.debug("From Date value: {}", fromDate); 

waitVisible(fromdateInput);
page.locator(fromdateInput).fill(fromDate);
String actualfromdate = page.locator(fromdateInput).inputValue(); 
log.info("Validating From Date value. Actual: {}", actualfromdate);

Assert.assertEquals(actualfromdate, fromDate, "From date input value does not match expected.");
log.info("From Date value validated successfully");

}

public void selecttodate(String toDate)
{

String todateInput = LocatorReader.getLocator("locatorleave","leave", "applyToDate", "selector");
log.info("Selecting To Date");
log.debug("To Date selector: {}", todateInput);
log.debug("To Date value: {}", toDate);

waitVisible(todateInput);
  Locator to = page.locator(todateInput);
to.click();
  to.press("Control+A");
  to.press("Backspace");
    to.type(toDate, new Locator.TypeOptions().setDelay(50));

  to.press("Tab");

String actualtodate = to.inputValue().trim();  
log.info("Validating To Date value. Actual: {}", actualtodate);

Assert.assertEquals(actualtodate, toDate, "To date input value does not match expected.");
log.info("To Date value validated successfully");

}

public void selectpartial(String partialDay)
{
String partialInput = LocatorReader.getLocator("locatorleave","leave", "partialDays", "selector");
log.info("Selecting Partial Day");
log.debug("Partial Day selector: {}", partialInput);
log.debug("Partial Day value: {}", partialDay);

waitVisible(partialInput);
page.locator(partialInput).click(); 
String option = LocatorReader.getLocator("locatorleave","leave", "optionselect", "selector");
 String option1 = option + "'" + partialDay + "']";
page.locator(option1).first().click();
String selectedpartial = page.locator(partialInput).first().innerText().trim();
log.info("Validating Partial Day value. Actual: {}", selectedpartial);

Assert.assertTrue(selectedpartial.contains(partialDay), "Selected partial day does not match");
log.info("Partial Day value validated successfully");

}

public void selectduration(String duration)
{
String durationInput = LocatorReader.getLocator("locatorleave","leave", "duration", "selector");
log.info("Selecting Duration");
log.debug("Duration selector: {}", durationInput);
log.debug("Duration value: {}", duration);

waitVisible(durationInput);
page.locator(durationInput).click();
String option = LocatorReader.getLocator("locatorleave","leave", "optionselect", "selector");
 String option1 = option + "'" + duration + "']";
page.locator(option1).first().click();
String selectedduration = page.locator(durationInput).first().innerText().trim(); 
log.info("Validating Duration value. Actual: {}", selectedduration);  
Assert.assertTrue(selectedduration.contains(duration), "Selected duration does not match");
log.info("Duration value validated successfully");

}

public void entercmt(String comment)
{
String cmtInput = LocatorReader.getLocator("locatorleave","leave", "comment", "selector");
log.info("Entering Comment value");
log.debug("Comment selector: {}", cmtInput);
log.debug("Comment value: {}", comment); 

waitVisible(cmtInput);
page.locator(cmtInput).fill(comment);
String actualcmt = page.locator(cmtInput).inputValue();
log.info("Validating Comment value. Actual: {}", actualcmt);
Assert.assertEquals(actualcmt, comment, "Comment input value does not match expected.");
log.info("Comment value validated successfully");
page.evaluate("window.scrollBy(0, 500)");

}

public void clickapply()
{
    log.info("Clicking on apply button");

    String applybtn = LocatorReader.getLocator("locatorleave","leave", "applyButton", "selector");
    waitVisible(applybtn);
    page.locator(applybtn).click();
  String toastMessage = LocatorReader.getLocator("locatorleave","leave", "toastm1", "selector");

    Locator toast = page.locator(toastMessage).first();
    log.info("Waiting for toast after saving candidate details");
  toast.waitFor(new Locator.WaitForOptions()
      .setState(WaitForSelectorState.VISIBLE)
      .setTimeout(25000));


  String msg = toast.innerText().trim();
  Assert.assertTrue(msg.toLowerCase().contains("successfully"),
      "Leave apply did not succeed. Toast message was: " + msg);
      log.info("Leave applied successfully");
}



}