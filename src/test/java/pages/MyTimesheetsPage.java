package pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.WaitForSelectorState;
import utils.LocatorReader;
import utils.TestDataReader;
import utils.ScreenshotUtils;

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
        } catch (Exception e) {
            loc.click(new Locator.ClickOptions().setForce(true).setTimeout(30000));
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

    // ---------------- PROJECT ----------------
    public void enterProject() {
        String project = testData.getData("register.timesheet.project");
        String projectInput = LocatorReader.getLocator("timesheet", "ProjectDropdown", "selector");

        waitVisible(projectInput);
        page.locator(projectInput).fill(project);
        ScreenshotUtils.captureScreenshot(page, "Project_Entered");
    }

    // ---------------- ACTIVITY ----------------
    public void selectActivity() {

    String activity = testData.getData("register.timesheet.activity");

   
    String activityDropdown =
            LocatorReader.getLocator("timesheet", "ActivityDropdown", "selector");

    safeClick(activityDropdown);

    // Wait for the dropdown options to appear
    page.waitForTimeout(500);

    
    String activityOption =
            "//div[@role='option']//span[normalize-space()='" + activity + "']"
            + " | //div[@role='option' and normalize-space()='" + activity + "']";

    // Use locator with increased timeout for waiting
    page.locator(activityOption).waitFor(
            new Locator.WaitForOptions()
                    .setTimeout(60000));

    safeClick(activityOption);
    ScreenshotUtils.captureScreenshot(page, "Activity_Selected");
}


    // ---------------- HOURS ----------------
    public void enterHours() {
        String day2 = testData.getData("register.timesheet.hours.2nd Feb");
        String day3 = testData.getData("register.timesheet.hours.3rd Feb");
        String day4 = testData.getData("register.timesheet.hours.4th Feb");

        String hourInput = LocatorReader.getLocator("timesheet", "2ndDayInput", "selector");

        waitVisible(hourInput);

        // Because your day locators are generic, use nth() to fill different cells
        page.locator(hourInput).nth(0).fill(day2);
        page.locator(hourInput).nth(1).fill(day3);
        page.locator(hourInput).nth(2).fill(day4);
        ScreenshotUtils.captureScreenshot(page, "Hours_Entered");
    }

    // ---------------- SAVE ----------------
    public void clickSave() {
        String saveBtn = LocatorReader.getLocator("timesheet", "SaveButton", "selector");
        waitVisible(saveBtn);

        page.locator(saveBtn).click(
                new Locator.ClickOptions()
                        .setNoWaitAfter(true)
                        .setTimeout(30000)
        );
        ScreenshotUtils.captureScreenshot(page, "Save_Button_Clicked");
    }
}
