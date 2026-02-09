package pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.WaitForSelectorState;
import utils.LocatorReader;
import utils.TestDataReader;
import utils.ScreenshotUtils;
import org.testng.Assert;

public class MyTimesheetsPage {

    private final Page page;
    private final TestDataReader testData;

    public MyTimesheetsPage(Page page) {
        this.page = page;
        this.testData = new TestDataReader("Testdata/timesheetData.json");
    }

    private void waitVisible(String selector) {
        page.waitForSelector(selector,
                new Page.WaitForSelectorOptions()
                        .setState(WaitForSelectorState.VISIBLE)
                        .setTimeout(30000));
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

    // ---------------- TIME MENU ----------------
    public void clickTimeMenu() {
        String timeMenu = LocatorReader.getLocator("timesheet", "menuTime", "selector");
        
        safeClick(timeMenu);
        ScreenshotUtils.captureScreenshot(page, "TimeMenu_Clicked");
    }

    public void clickTimesheetsOption() {
        String timesheetsOption = LocatorReader.getLocator("timesheet", "headerTimesheets", "selector");
        
        safeClick(timesheetsOption);
        ScreenshotUtils.captureScreenshot(page, "Timesheets_Option_Selected");
    }

    public void clickMyTimesheetsOption() {
        String myTimesheets = LocatorReader.getLocator("timesheet", "MyTimesheets", "selector");
       
        safeClick(myTimesheets);
        ScreenshotUtils.captureScreenshot(page, "MyTimesheets_Option_Selected");
    }

    // ---------------- EDIT TIMESHEET ----------------
    public void clickEditButton() {
        String editBtn = LocatorReader.getLocator("timesheet", "EditTimesheetButton", "selector");
        
        safeClick(editBtn);
        ScreenshotUtils.captureScreenshot(page, "Edit_Button_Clicked");
    }

    public void clickCancelButton(){
        String cancelBtn = LocatorReader.getLocator("timesheet", "Cancelbutton", "selector");
       
        safeClick(cancelBtn);
    
    }
    
    }
