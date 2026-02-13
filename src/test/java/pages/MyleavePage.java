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

public class MyleavePage{

private Page page;
private TestDataReader testData;

private static final String MY_LEAVE = "TC008 - Verify applied leave appears on My leave page";

public MyleavePage(Page page) {
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


public void clickmyleave()
{
String myleave = LocatorReader.getLocator("leave", "myLeaveTab", "selector");

page.waitForSelector(myleave,
            new Page.WaitForSelectorOptions().setState(WaitForSelectorState.VISIBLE).setTimeout(60000));
    page.locator(myleave).first().click();

    String datef = LocatorReader.getLocator("leave", "applyFromDate", "selector");
page.waitForSelector(datef,
            new Page.WaitForSelectorOptions().setState(WaitForSelectorState.VISIBLE).setTimeout(60000));

    Assert.assertTrue(page.locator(datef).first().isVisible(),
            "ViewMyleave list page did not open (From Date input not visible).");
}


public void datefromselect()
{
String datefrom = testData.getData(MY_LEAVE, "fromdate");
String datefromInput = LocatorReader.getLocator("leave", "applyFromDate", "selector");

waitVisible(datefromInput);
  Locator to = page.locator(datefromInput);
to.click();
  to.fill("");
  to.fill(datefrom);

    to.press("Tab");
String actualdatefrom = to.inputValue().trim();       
Assert.assertEquals(actualdatefrom, datefrom, "From date input value does not match expected.");
}

public void datetoselect()
{
String dateto = testData.getData(MY_LEAVE, "todate");
String todateInput = LocatorReader.getLocator("leave", "applyToDate", "selector");
waitVisible(todateInput);
  Locator to = page.locator(todateInput);
to.click();
  to.press("Control+A");
  to.press("Backspace");
    to.type(dateto, new Locator.TypeOptions().setDelay(50));

  to.press("Tab");

String actualtodate = to.inputValue().trim();       
Assert.assertEquals(actualtodate, dateto, "To date input value does not match expected.");

}

public void clicksrch()
{
    String srchbtn = LocatorReader.getLocator("leave", "leaveSearchButton", "selector");
    waitVisible(srchbtn);
    page.locator(srchbtn).click();
    page.evaluate("window.scrollBy(0, 500)");

}

public void clickout()
    {

    String profilebutton = LocatorReader.getLocator("profile", "profileMenu","selector");
    waitVisible(profilebutton);
    page.locator(profilebutton).click();

    String logbutton = LocatorReader.getLocator("profile", "logout","selector");
    waitVisible(logbutton);
    page.locator(logbutton).click();
    }







}