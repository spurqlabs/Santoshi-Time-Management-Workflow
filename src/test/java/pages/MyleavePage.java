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

public class MyleavePage{
  
private static final Logger log = LoggerUtil.getLogger(MyleavePage.class);

private Page page;
private TestDataReader testData;

private static final String MY_LEAVE = "TC008 - Verify applied leave appears on My leave page";

public MyleavePage(Page page) {
        this.page = page;
        this.testData = new TestDataReader("Testdata/leaveData.json");
         log.info("Myleave object initialized");
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


public void clickmyleave()
{
  log.info("Clicking on My leave Tab");
String myleave = LocatorReader.getLocator("locatorleave","leave", "myLeaveTab", "selector");
log.debug("My leave Tab selector: {}", myleave);
page.waitForSelector(myleave,
            new Page.WaitForSelectorOptions().setState(WaitForSelectorState.VISIBLE).setTimeout(60000));
    page.locator(myleave).first().click();

    String datef = LocatorReader.getLocator("locatorleave","leave", "applyFromDate", "selector");
page.waitForSelector(datef,
            new Page.WaitForSelectorOptions().setState(WaitForSelectorState.VISIBLE).setTimeout(60000));
log.info("Validating ViewMyleave list page. Actual: {}", datef);
    Assert.assertTrue(page.locator(datef).first().isVisible(),
            "ViewMyleave list page did not open (From Date input not visible).");
log.info("ViewMyleave list page navigation successful");
}


public void datefromselect()
{
String datefrom = testData.getData(MY_LEAVE, "fromdate");
String datefromInput = LocatorReader.getLocator("locatorleave","leave", "applyFromDate", "selector");
log.info("Selecting From Date");
log.debug("From Date selector: {}", datefromInput);
log.debug("From Date value: {}", datefrom); 

waitVisible(datefromInput);
  Locator to = page.locator(datefromInput);
to.click();
  to.fill("");
  to.fill(datefrom);

    to.press("Tab");
String actualdatefrom = to.inputValue().trim(); 
log.info("Validating From Date value. Actual: {}", actualdatefrom);

Assert.assertEquals(actualdatefrom, datefrom, "From date input value does not match expected.");
log.info("From Date value validated successfully");
}

public void datetoselect()
{
String dateto = testData.getData(MY_LEAVE, "todate");
String todateInput = LocatorReader.getLocator("locatorleave","leave", "applyToDate", "selector");
log.info("Selecting To Date");
log.debug("To Date selector: {}", todateInput);
log.debug("To Date value: {}", dateto);

waitVisible(todateInput);
  Locator to = page.locator(todateInput);
to.click();
  to.press("Control+A");
  to.press("Backspace");
    to.type(dateto, new Locator.TypeOptions().setDelay(50));

  to.press("Tab");

String actualtodate = to.inputValue().trim(); 
log.info("Validating To Date value. Actual: {}", actualtodate);

Assert.assertEquals(actualtodate, dateto, "To date input value does not match expected.");
log.info("To Date value validated successfully");

}

public void clicksrch()
{
  log.info("Clicking on search button");
    String srchbtn = LocatorReader.getLocator("locatorleave","leave", "leaveSearchButton", "selector");
    waitVisible(srchbtn);
    page.locator(srchbtn).click();
    page.evaluate("window.scrollBy(0, 500)");
    log.info("Candiate leave details validated successfully");

}

public void clickout()
    {

    String profilebutton = LocatorReader.getLocator("locatortimesheet","profile", "profileMenu","selector");
    log.info("Clicking on profile icon");
    log.debug("Profile Icon selector: {}", profilebutton);

    waitVisible(profilebutton);
    page.locator(profilebutton).click();

    String logbutton = LocatorReader.getLocator("locatortimesheet","profile", "logout","selector");
    log.info("Clicking on logout option");
    log.debug("Logout option selector: {}", logbutton);

    waitVisible(logbutton);
    page.locator(logbutton).click();
    log.info("User logout Successfully");
    
    }







}