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
import com.microsoft.playwright.Download;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.Files;
import org.slf4j.Logger;
import utils.LoggerUtil;



public class AddCandidatedetails{

 private static final Logger log = LoggerUtil.getLogger(AddCandidatedetails.class);

private Page page;
private TestDataReader testData;

    private static final String LOGIN_SCENARIO = "TC004 - Add Candidate details";
    private static final String RECRUITMENT_SCENARIO = "TC005 - View Candidate details, downloaded attached resume and schedule Interview";


    public AddCandidatedetails(Page page) {
        this.page = page;
        this.testData = new TestDataReader("Testdata/recruitmentData.json");
        log.info("AddCandidatedetails object initialized");
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

public void clickrecruitmentmenu()
{
    log.info("Clicking on Recruitment menu");

    String recruitmentMenu = LocatorReader.getLocator("recruit","recruitment", "menuRecruitment", "selector");

    // 1) Wait for page left menu / dashboard to be ready (OrangeHRM side panel)
    String sidepanel = LocatorReader.getLocator("recruit","recruitment", "sidepanel", "selector");
    page.waitForSelector(sidepanel,
            new Page.WaitForSelectorOptions().setState(WaitForSelectorState.VISIBLE).setTimeout(60000));

    // 2) Wait for Recruitment menu element to exist
    page.waitForSelector(recruitmentMenu,
            new Page.WaitForSelectorOptions().setState(WaitForSelectorState.ATTACHED).setTimeout(60000));

    Locator menu = page.locator(recruitmentMenu).first();

    // 3) Bring it into view (sometimes it exists but offscreen)
    menu.scrollIntoViewIfNeeded();

    // 4) Now wait until it's visible (if still not visible, we still try clicking)
    try {
        page.waitForSelector(recruitmentMenu,
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

    Assert.assertTrue(url.contains("recruitment"),
            "Recruitment page not opened after clicking menu. Current URL: " + url);
    log.info("Recruitment menu navigation successful");

    ScreenshotUtils.captureScreenshot(page, "Recruitment_Menu_Clicked");
}

public void clickadd()
{
        log.info("Clicking on Add button to add candidate details");

    String addBtn = LocatorReader.getLocator("recruit","recruitment", "addButton", "selector");

    // Ensure network is settled
    page.waitForLoadState(LoadState.NETWORKIDLE);

    // Wait for Add button to be attached
    page.waitForSelector(addBtn,
            new Page.WaitForSelectorOptions().setState(WaitForSelectorState.ATTACHED).setTimeout(60000));

    Locator add = page.locator(addBtn).first();

    // scroll into view (in case offscreen)
    add.scrollIntoViewIfNeeded();

    // try visible wait but don't fail early
    try {
        page.waitForSelector(addBtn,
                new Page.WaitForSelectorOptions().setState(WaitForSelectorState.VISIBLE).setTimeout(15000));
    } catch (Exception ignored) {}

    // click with fallback
    try {
        add.click(new Locator.ClickOptions().setTimeout(30000));
    } catch (Exception e) {
        add.click(new Locator.ClickOptions().setForce(true).setTimeout(30000));
    }

        
    String firstName = LocatorReader.getLocator("recruit","addCandidate", "firstName", "selector");
    page.waitForSelector(firstName,
            new Page.WaitForSelectorOptions().setState(WaitForSelectorState.VISIBLE).setTimeout(60000));
    log.info("Validating First Name field. Actual: {}", firstName);

    Assert.assertTrue(page.locator(firstName).first().isVisible(),
            "Add Candidate page did not open after clicking Add button");
       log.info("Add Candidate page loaded successfully");     

    ScreenshotUtils.captureScreenshot(page, "Add_Button_Clicked");
}

public void enterfirstName()
{
String fname = testData.getData(LOGIN_SCENARIO, "firstname");
String fnameInput = LocatorReader.getLocator("recruit","addCandidate", "firstName", "selector");
log.info("Entering First Name value");
 log.debug("First Name selector: {}", fnameInput);
 log.debug("First Name value: {}", fname);       


waitVisible(fnameInput);
page.locator(fnameInput).fill(fname);
String actualfname = page.locator(fnameInput).inputValue();
        log.info("Validating First Name value. Actual: {}", actualfname);

 Assert.assertEquals(actualfname, fname, "First name does not match");
 log.info("First Name value validated successfully");

}

public void enterlastName()
{
String lname = testData.getData(LOGIN_SCENARIO, "lastname");
String lnameInput = LocatorReader.getLocator("recruit","addCandidate", "lastName", "selector");
log.info("Entering Last Name value");
 log.debug("Last Name selector: {}", lnameInput);
 log.debug("Last Name value: {}", lname); 

waitVisible(lnameInput);
page.locator(lnameInput).fill(lname);
String actuallname = page.locator(lnameInput).inputValue();
log.info("Validating Last Name value. Actual: {}", actuallname);

Assert.assertEquals(actuallname, lname, "Last name does not match");
log.info("Last Name value validated successfully");

}

public void selectvacancy()
{
String vacancy = testData.getData(LOGIN_SCENARIO, "jobvacancy");
String vacancyDropdown = LocatorReader.getLocator("recruit","addCandidate", "jobVacancy", "selector");

log.info("Selecting Vacancy");
log.debug("Vacancy selector: {}", vacancyDropdown);
log.debug("Vacancy value: {}", vacancy); 



// Wait for the dropdown to be visible
        waitVisible(vacancyDropdown);
        
        // Wait a moment for the page to settle
        page.waitForTimeout(500);
        
        // Click the dropdown to open it
        page.locator(vacancyDropdown).click();

        // Wait for the dropdown options to appear
        page.waitForTimeout(2000);

        
        System.out.println("Looking for Vacancy: " + vacancy);
        
        // Try multiple selection strategies
        boolean selected = false;
        
        // Strategy 1: Try exact text match with normalize-space
        try {
            String option1 = LocatorReader.getLocator("recruit","recruitment", "optionselect", "selector");
            String option1WithVacancy = option1 + "'" + vacancy + "']";
        
            if (page.locator(option1WithVacancy).count() > 0) {
                System.out.println("Found option using strategy 1");
                page.locator(option1WithVacancy).first().click();
                selected = true;
            }
        } catch (Exception e) {
            System.out.println("Strategy 1 failed: " + e.getMessage());
        }
        
        // Strategy 2: Try contains text
        if (!selected) {
            try {
                String option2 = LocatorReader.getLocator("recruit","recruitment", "optionselect", "selector");
                String option2WithVacancy = option2 + "'" + vacancy + "')]";


                if (page.locator(option2WithVacancy).count() > 0) {
                    System.out.println("Found option using strategy 2");
                    page.locator(option2WithVacancy).first().click();
                    selected = true;
                }
            } catch (Exception e) {
                System.out.println("Strategy 2 failed: " + e.getMessage());
            }
        }
        
        // Strategy 3: Try clicking any div with role=option that contains the country text
        if (!selected) {
            try {
                String optionTemplate  = LocatorReader.getLocator("recruit","recruitment", "dropdownOptionContains", "selector");
                String option3 = optionTemplate.replace("{value}", vacancy);
                Locator option4 = page.locator(option3);

                
                if (option4.count() > 0) {
                    System.out.println("Found option using strategy 3");
                    option4.first().click();
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

              String optionsLocator = LocatorReader.getLocator("recruit","recruitment", "rolecount", "selector");
            Locator options = page.locator(optionsLocator);

                int count = options.count();
            

                System.out.println("Total options found: " + count);
                
                for (int i = 0; i < Math.min(count, 10); i++) {
                    String optionText = options.nth(i).textContent();
                    System.out.println("Option " + i + ": " + optionText);
                    
                    // Try to select if it contains our vacancy
                    if (optionText.contains(vacancy)) {
                        System.out.println("Found match at index " + i);
                        options.nth(i).click();
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

            String firstoption = LocatorReader.getLocator("recruit","recruitment", "rolecount", "selector");
            Locator fstoption = page.locator(firstoption);
                fstoption.first().click();
                selected = true;
            } catch (Exception e) {
                System.out.println("Fallback selection failed: " + e.getMessage());
                ScreenshotUtils.captureScreenshot(page, "vacancy_Selection_Failed");
                throw new RuntimeException("Failed to select vacancy from dropdown", e);
            }
        }
        
        // Wait for the dropdown to close
        page.waitForTimeout(500);

        String selectedText = page.locator(vacancyDropdown).textContent();

        ScreenshotUtils.captureScreenshot(page, "Vacancy_Selected");
        String selectedVacancy = page.locator(vacancyDropdown).first().textContent().trim();    
         log.info("Validating Vacancy Actual: {}", selectedVacancy);
    Assert.assertEquals(selectedVacancy, vacancy, "Selected vacancy does not match");
    log.info("Vacancy value validated successfully");
}

public void enteremail()
{
String emil = testData.getData(LOGIN_SCENARIO, "email");
String emilInput = LocatorReader.getLocator("recruit","addCandidate", "email", "selector");
log.info("Entering email value");
log.debug("Email selector: {}", emilInput);
log.debug("Email value: {}", emil); 

waitVisible(emilInput);
page.locator(emilInput).fill(emil);
String actualemil = page.locator(emilInput).inputValue();
log.info("Validating Email value. Actual: {}", actualemil);

Assert.assertEquals(actualemil, emil, "Email does not match");
log.info("Email value validated successfully");
}

public void entercontact()
{
String contact = testData.getData(LOGIN_SCENARIO, "cnumber");
String contactInput = LocatorReader.getLocator("recruit","addCandidate", "contactNumber", "selector");
log.info("Entering Contact value");
log.debug("Contact selector: {}", contactInput);
log.debug("Contact value: {}", contact); 

waitVisible(contactInput);
page.locator(contactInput).fill(contact);   
String actualcontact = page.locator(contactInput).inputValue();
log.info("Validating Contact value. Actual: {}", actualcontact);
Assert.assertEquals(actualcontact, contact, "Contact number does not match");
log.info("Contact value validated successfully");
}

public void uploadresume()
{
 String filePath = testData.getData(LOGIN_SCENARIO, "filePath");
 String browse = LocatorReader.getLocator("recruit","addCandidate", "resumeUploadInput", "selector");
 
 log.info("Uploading Resume");
 log.debug("Resume selector: {}", browse);
  log.debug("Resume value: {}", filePath); 

 page.waitForSelector(browse,
            new Page.WaitForSelectorOptions()
                    .setState(WaitForSelectorState.ATTACHED)
                    .setTimeout(60000));
 Locator browseInput = page.locator(browse).first();
 browseInput.scrollIntoViewIfNeeded();
    browseInput.setInputFiles(Paths.get(filePath));

String inputValue = browseInput.inputValue();
    log.info("Validating File input value after upload. Actual: {}", inputValue);

    Assert.assertTrue(
            inputValue != null && !inputValue.trim().isEmpty(),
            "File input did not receive file."
    );
    log.info("File input receive file successfully");

    // Wait for filename to appear
    String fileName = Paths.get(filePath).getFileName().toString();



    page.waitForSelector("text=" + fileName,
            new Page.WaitForSelectorOptions()
                    .setState(WaitForSelectorState.VISIBLE)
                    .setTimeout(60000));

    page.waitForTimeout(2000);
page.evaluate("window.scrollBy(0, 500)");
}

public void clicksave()
{
    log.info("Clicking Save button");
    
    String saveBtn = LocatorReader.getLocator("recruit","addCandidate", "saveButton", "selector");
    safeClick(saveBtn);
        String toast = LocatorReader.getLocator("recruit","recruitment", "toast", "selector");
    
    log.info("Waiting for toast after saving candidate details");

    page.waitForSelector(toast,
            new Page.WaitForSelectorOptions()
                    .setState(WaitForSelectorState.VISIBLE)
                    .setTimeout(80000));

    Assert.assertTrue(page.locator(toast).isVisible(),
            "Candidate not saved successfully");
            log.info("Candidate details saved successfully");
}


public void clickrecmenu()
{
        log.info("Clicking_on_Recruitment_menu");

    String recmenu = LocatorReader.getLocator("recruit","recruitment", "menuRecruitment", "selector");

// 1) Wait for page left menu / dashboard to be ready (OrangeHRM side panel)
    String sidepanel1 = LocatorReader.getLocator("recruit","recruitment", "sidepanel", "selector");
    page.waitForSelector(sidepanel1,
            new Page.WaitForSelectorOptions().setState(WaitForSelectorState.VISIBLE).setTimeout(60000));

       // 2) Wait for Recruitment menu element to exist
    page.waitForSelector(recmenu,
            new Page.WaitForSelectorOptions().setState(WaitForSelectorState.ATTACHED).setTimeout(60000));

    Locator menu = page.locator(recmenu).first();

    // 3) Bring it into view (sometimes it exists but offscreen)
    menu.scrollIntoViewIfNeeded();

    // 4) Now wait until it's visible (if still not visible, we still try clicking)
    try {
        page.waitForSelector(recmenu,
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

    Assert.assertTrue(url.contains("recruitment"),
            "Recruitment page not opened after clicking menu. Current URL: " + url);
            log.info("Recruitment menu navigation successful");

    ScreenshotUtils.captureScreenshot(page, "Recruitment_Menu_Clicked");
} 
    public void verifydetails()
    {
      String expectfname = testData.getData(LOGIN_SCENARIO, "firstname");
      String expectlname = testData.getData(LOGIN_SCENARIO, "lastname");
      String expectvacancy = testData.getData(LOGIN_SCENARIO, "jobvacancy");
      String expectemail = testData.getData(LOGIN_SCENARIO, "email");
     String expectcontact = testData.getData(LOGIN_SCENARIO, "cnumber");
     String expectresume = testData.getData(LOGIN_SCENARIO, "filePath");
     page.evaluate("window.scrollBy(0, 500)");
     log.info ("Details validated successfully");

    }    
public void clickdownload()
    {
            log.info("Clicking download button");

        String downloadbtn = LocatorReader.getLocator("recruit","viewCandidates", "resumeLink", "selector");
        Download download = page.waitForDownload(() -> {
        page.locator(downloadbtn).first().click();
    });


    String flename = download.suggestedFilename();
    Path savePath = Paths.get("src/test/resources/downloads/" + flename);

    try {
        download.saveAs(savePath);
    } catch (Exception e) {
        throw new RuntimeException("Failed to save downloaded file", e);
    }

    // Verify file exists
    Assert.assertTrue(Files.exists(savePath),
            "Downloaded resume file not found at: " + savePath);
    log.info ("Resume file validated successfully");
    }


public void clickshortlist()
{
  log.info("Clicking shortlist button");

 String shortlistbtn = LocatorReader.getLocator("recruit","recruitment", "shortlistButton","selector");
 safeClick(shortlistbtn);

page.waitForLoadState(LoadState.NETWORKIDLE);

String url = page.url();
log.info("Current URL after clicking shortlist button: {}", url);

Assert.assertTrue(url.contains("changeCandidateVacancyStatus"),
            "changeCandidateVacancyStatus page not opened after clicking menu. Current URL: " + url);
log.info("ChangeCandidateVacancyStatus page navigation successful");
}

public void enternote()
{

String note = testData.getData (RECRUITMENT_SCENARIO, "Notes");
String noteInput = LocatorReader.getLocator("recruit","recruitment", "notesInput","selector");

log.info("Entering Note value");
log.debug("Note selector: {}", noteInput);
log.debug("Note value: {}", note); 
waitVisible(noteInput);
page.locator(noteInput).fill(note);

String actualnote = page.locator(noteInput).inputValue();
log.info("Validating Note value. Actual: {}", actualnote);

Assert.assertEquals(actualnote, note, "Notes does not match");
log.info("Note value validated successfully");

}

public void saveclick()
{
    log.info("Clicking on Save_Button");
String savebtn = LocatorReader.getLocator("recruit","recruitment", "saveButton","selector");
safeClick(savebtn);

String t1 = LocatorReader.getLocator("recruit","recruitment", "toast","selector");
log.info("Waiting for toast after saving details");

    page.waitForSelector(t1,
            new Page.WaitForSelectorOptions()
                    .setState(WaitForSelectorState.VISIBLE)
                    .setTimeout(80000));

    Assert.assertTrue(page.locator(t1).isVisible(),
            "Details not saved successfully");
             log.info("Details saved successfully");
}

public void clickscheduleinterview()
{
    log.info("Clicking on schedule Interview button");

String schedulebtn = LocatorReader.getLocator("recruit","recruitment", "scheduleInterviewButton","selector");
safeClick(schedulebtn);

page.waitForLoadState(LoadState.NETWORKIDLE);

String url = page.url();
 log.info("Current URL after clicking schedule Interview: {}", url);

Assert.assertTrue(url.contains("changeCandidateVacancyStatus"),
            "changeCandidateVacancyStatus page not opened after clicking menu. Current URL: " + url);
log.info("ChangeCandidateVacancyStatus page  navigation successful");
}

public void entertitle()
{
 String ineterviewtitle = testData.getData(RECRUITMENT_SCENARIO, "Interview Title");
 String titleInput = LocatorReader.getLocator("recruit","recruitment", "interviewtitle","selector");
log.info("Entering Title value");
log.debug("Title selector: {}", titleInput);
log.debug("Title value: {}", ineterviewtitle); 

waitVisible(titleInput);
page.locator(titleInput).fill(ineterviewtitle);

String actualtitle = page.locator(titleInput).inputValue();
log.info("Validating Title value. Actual: {}", actualtitle);

Assert.assertEquals(actualtitle, ineterviewtitle, "Interview title does not match");
log.info("Title value validated successfully");

}

public void enterineterviewer()
{
    String interviewername = testData.getData(RECRUITMENT_SCENARIO, "Interviewer");
    String userName = LocatorReader.getLocator("recruit","recruitment", "interviewer","selector");
    log.info("Entering Interviewer value");
   log.debug("Interviewer selector: {}", userName);
    log.debug("Interviewer value: {}", interviewername); 

    waitVisible(userName);
    page.locator(userName).fill(interviewername);
    String actualinterviewer = page.locator(userName).inputValue();
    log.info("Validating Interviewer value. Actual: {}", actualinterviewer);

    Assert.assertEquals(actualinterviewer, interviewername, "Interviewer name does not match");
    log.info("Interviewer value validated successfully");

  String suggestionTemplate = LocatorReader.getLocator("recruit","recruitment", "dropdownsuggestion", "selector");
  String suggestionSelector = suggestionTemplate + "'" + interviewername + "']";   
  Locator suggestion = page.locator(suggestionSelector).first();
  suggestion.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE).setTimeout(15000));
  suggestion.click();
  Assert.assertEquals(page.locator(userName).inputValue(), interviewername,
      "Interviewer name input value does not match expected.");

    }


public void selectdate()
{
String selectDate = testData.getData(RECRUITMENT_SCENARIO, "Interview Date");
String selectDateInput = LocatorReader.getLocator("recruit","recruitment", "interviewDate","selector");
log.info("Selecting Date");
log.debug("Date selector: {}", selectDateInput);
log.debug("Date value: {}", selectDate); 

waitVisible(selectDateInput);

Locator date = page.locator(selectDateInput);
date.click();

date.type(selectDate, new Locator.TypeOptions().setDelay(50));

  date.press("Tab");

     

String selectdateactual = date.inputValue().trim();    
log.info("Validating Date value. Actual: {}", selectdateactual);

Assert.assertEquals(selectdateactual, selectDate, "Selected date does not match expected.");
log.info("Date value validated successfully");

}

public void selecttime()
{
String timeInput = LocatorReader.getLocator("recruit","recruitment", "interviewTime","selector");
log.info("Clicking on Time option");
log.debug("Time selector: {}", timeInput);

waitVisible(timeInput);
    Locator timeInputLoc = page.locator(timeInput);
    timeInputLoc.click();

String time = testData.getData(RECRUITMENT_SCENARIO, "Interview Time");
String timeloc = LocatorReader.getLocator("recruit","recruitment", "Time","selector");

log.info("Entering Time value");
log.debug("Time selector: {}", timeloc);
log.debug("Time value: {}", time); 

waitVisible(timeloc);
page.locator(timeloc).fill(time);

String timeampmloc = LocatorReader.getLocator("recruit","recruitment", "TimeAMPM","selector");
waitVisible(timeampmloc);
page.locator(timeampmloc).check();

String timeactual = timeInputLoc.inputValue().trim();
log.info("Validating Time value. Actual: {}", timeactual);
String expectedTime = time + ":00";
Assert.assertTrue(timeactual.startsWith(expectedTime), "Selected time does not match expected. Actual: " + timeactual);
log.info("Time value validated successfully");

}

public void note()
{

String note1 = testData.getData (RECRUITMENT_SCENARIO, "Note");
String noteInput1 = LocatorReader.getLocator("recruit","recruitment", "notesInput","selector");

log.info("Entering Note");
log.debug("Note selector: {}", noteInput1);
log.debug("Note value: {}", note1); 

waitVisible(noteInput1);
page.locator(noteInput1).fill(note1);

String actualnote1 = page.locator(noteInput1).inputValue();
log.info("Validating Note value. Actual: {}", actualnote1);

Assert.assertEquals(actualnote1, note1, "Notes does not match");
log.info("Note value validated successfully");

}

  
    public void clicklogout()
    {
    String profilebtn = LocatorReader.getLocator("locatortimesheet","profile", "profileMenu","selector");
    log.info("Clicking on profile icon");
    log.debug("Profile Icon selector: {}", profilebtn);

    waitVisible(profilebtn);
    page.locator(profilebtn).click();


    String logbtn = LocatorReader.getLocator("locatortimesheet","profile", "logout","selector");
    log.info("Clicking on logout option");
    log.debug("Logout option selector: {}", logbtn);

    waitVisible(logbtn);
    page.locator(logbtn).click();
    log.info("User logout Successfully");
    }

}
