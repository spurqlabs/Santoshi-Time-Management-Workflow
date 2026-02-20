package pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.WaitForSelectorState;
import utils.LocatorReader;
import utils.TestDataReader;
import utils.ScreenshotUtils;
import org.testng.Assert;
import org.slf4j.Logger;
import utils.LoggerUtil;


public class MyTimesheetsPage {

private static final Logger log = LoggerUtil.getLogger(MyTimesheetsPage.class);

    private final Page page;
    private final TestDataReader testData;

    public MyTimesheetsPage(Page page) {
        this.page = page;
        this.testData = new TestDataReader("Testdata/timesheetData.json");
        log.info("MyTimesheetsPage initialized");
    }

    private void waitVisible(String selector) 
    {
    log.debug("Waiting for element to be visible: {}", selector);

        page.waitForSelector(selector,
                new Page.WaitForSelectorOptions()
                        .setState(WaitForSelectorState.VISIBLE)
                        .setTimeout(30000));
    }

    private void safeClick(String selector) {

    waitVisible(selector);
    Locator loc = page.locator(selector);
    log.info("Clicking element: {}", selector);


    try {
        loc.click(new Locator.ClickOptions().setTimeout(30000));
    } catch (Exception ignored) {
        log.warn("Normal click failed, trying force click on: {}", selector);
        loc.click(
            new Locator.ClickOptions()
                .setForce(true)
                .setTimeout(30000)
        );
    }
}

    // ---------------- TIME MENU ----------------
    public void clickTimeMenu() {

        log.info("Clicking on Time menu");

        String timeMenu = LocatorReader.getLocator("locatortimesheet", "timesheet", "menuTime", "selector");
        
        safeClick(timeMenu);        
        String url = page.url();
        log.info("Current URL after clicking Time menu: {}", url);
        
       Assert.assertTrue(url.contains("viewEmployeeTimesheet"),
            "viewEmployeeTimesheet page not opened after clicking menu. Current URL: " + url);

       log.info("Time menu navigation successful");

        ScreenshotUtils.captureScreenshot(page, "TimeMenu_Clicked");
    }

    public void clickTimesheetsOption() {
        log.info("Clicking on Timesheets option");

        String timesheetsOption = LocatorReader.getLocator("locatortimesheet", "timesheet", "headerTimesheets", "selector");
        
        safeClick(timesheetsOption);
         
        String mytimesheetopt = LocatorReader.getLocator("locatortimesheet","timesheet", "MyTimesheets", "selector");
        
         log.info("Validating My Timesheets option visibility");


         Assert.assertTrue(page.locator(mytimesheetopt).first().isVisible(),
                        "My Timesheets option not visible after clicking Timesheets.");
          log.info("Timesheets option validated successfully");
        ScreenshotUtils.captureScreenshot(page, "Timesheets_Option_Selected");
    }

    public void clickMyTimesheetsOption() {
        log.info("Clicking on My Timesheets option");

        String myTimesheets = LocatorReader.getLocator("locatortimesheet","timesheet", "MyTimesheets", "selector");
       
        safeClick(myTimesheets);
        String url = page.url();
        log.info("Current URL after clicking My Timesheets: {}", url);
        Assert.assertTrue(url.contains("viewMyTimesheet"),
            "viewMyTimesheet page not opened after clicking option. Current URL: " + url);
         log.info("My Timesheets navigation successful");
        ScreenshotUtils.captureScreenshot(page, "MyTimesheets_Option_Selected");
    }

    // ---------------- EDIT TIMESHEET ----------------
    public void clickEditButton() {
        log.info("Clicking Edit button");
        String editBtn = LocatorReader.getLocator("locatortimesheet","timesheet", "EditTimesheetButton", "selector");
        
        safeClick(editBtn);


        page.waitForSelector(editBtn, 
                new Page.WaitForSelectorOptions()
                        .setState(WaitForSelectorState.VISIBLE)
                        .setTimeout(80000));

       String url = page.url();
       log.info("Current URL after clicking Edit: {}", url);

       Assert.assertTrue(url.contains("editTimesheet"),
            "EditTimesheet page not opened after clicking edit button. Current URL: " + url);
         
         log.info("Edit page opened successfully");
        ScreenshotUtils.captureScreenshot(page, "Edit_Button_Clicked");
    }

    public void clickCancelButton()
    {
        log.info("Clicking Cancel button");
        String cancelBtn = LocatorReader.getLocator("locatortimesheet","timesheet", "Cancelbutton", "selector");
        safeClick(cancelBtn);

        page.waitForSelector(cancelBtn, 
                new Page.WaitForSelectorOptions()
                        .setState(WaitForSelectorState.VISIBLE)
                        .setTimeout(80000));

        
         String url = page.url();
          log.info("Current URL after clicking Cancel: {}", url);
      Assert.assertTrue(url.contains("viewMyTimesheet"),
            "ViewMyTimesheet page not opened after clicking cancel button. Current URL: " + url);
      log.info("Cancel action successful");

    
    }
    
    }
