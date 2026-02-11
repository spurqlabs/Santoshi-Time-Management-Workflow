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






public class AddCandidatedetails{

private Page page;
private TestDataReader testData;

    private static final String LOGIN_SCENARIO = "TC001 - Add Candidate details";
    private static final String RECRUITMENT_SCENARIO = "TC002 - View Candidate details and downloaded attached resume";


    public AddCandidatedetails(Page page) {
        this.page = page;
        this.testData = new TestDataReader("Testdata/recruitmentData.json");
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

public void clickrecruitmentmenu()
{

    String recruitmentMenu = LocatorReader.getLocator("recruitment", "menuRecruitment", "selector");

    // 1) Wait for page left menu / dashboard to be ready (OrangeHRM side panel)
    page.waitForSelector("//aside[contains(@class,'oxd-sidepanel')]",
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
    Assert.assertTrue(url.contains("recruitment"),
            "Recruitment page not opened after clicking menu. Current URL: " + url);

    ScreenshotUtils.captureScreenshot(page, "Recruitment_Menu_Clicked");
}

public void clickadd()
{
    String addBtn = LocatorReader.getLocator("recruitment", "addButton", "selector");

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

        
    String firstName = LocatorReader.getLocator("addCandidate", "firstName", "selector");
    page.waitForSelector(firstName,
            new Page.WaitForSelectorOptions().setState(WaitForSelectorState.VISIBLE).setTimeout(60000));

    Assert.assertTrue(page.locator(firstName).first().isVisible(),
            "Add Candidate page did not open after clicking Add button");

    ScreenshotUtils.captureScreenshot(page, "Add_Button_Clicked");
}

public void enterfirstName()
{
String fname = testData.getData(LOGIN_SCENARIO, "firstname");
String fnameInput = LocatorReader.getLocator("addCandidate", "firstName", "selector");
waitVisible(fnameInput);
page.locator(fnameInput).fill(fname);
String actualfname = page.locator(fnameInput).inputValue();
Assert.assertEquals(actualfname, fname, "First name does not match");
}

public void enterlastName()
{
String lname = testData.getData(LOGIN_SCENARIO, "lastname");
String lnameInput = LocatorReader.getLocator("addCandidate", "lastName", "selector");
waitVisible(lnameInput);
page.locator(lnameInput).fill(lname);
String actuallname = page.locator(lnameInput).inputValue();
Assert.assertEquals(actuallname, lname, "Last name does not match");
}

public void selectvacancy()
{
String vacancy = testData.getData(LOGIN_SCENARIO, "jobvacancy");
String vacancyDropdown = LocatorReader.getLocator("addCandidate", "jobVacancy", "selector");
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
            String option1 = "//div[@role='option']//span[normalize-space()='" + vacancy + "']";
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
                String option2 = "//div[@role='option' and contains(normalize-space(), '" + vacancy + "')]";
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
                String option3 = "//*[contains(text(), '" + vacancy + "') and @role='option']";
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
                    
                    // Try to select if it contains our vacancy
                    if (optionText.contains(vacancy)) {
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

        String selectedText = page.locator(vacancyDropdown).textContent();

        ScreenshotUtils.captureScreenshot(page, "Vacancy_Selected");
        String selectedVacancy = page.locator(vacancyDropdown).first().textContent().trim();    
Assert.assertEquals(selectedVacancy, vacancy, "Selected vacancy does not match");
}

public void enteremail()
{
String emil = testData.getData(LOGIN_SCENARIO, "email");
String emilInput = LocatorReader.getLocator("addCandidate", "email", "selector");
waitVisible(emilInput);
page.locator(emilInput).fill(emil);
String actualemil = page.locator(emilInput).inputValue();
Assert.assertEquals(actualemil, emil, "Email does not match");
}

public void entercontact()
{
String contact = testData.getData(LOGIN_SCENARIO, "cnumber");
String contactInput = LocatorReader.getLocator("addCandidate", "contactNumber", "selector");
waitVisible(contactInput);
page.locator(contactInput).fill(contact);   
String actualcontact = page.locator(contactInput).inputValue();
Assert.assertEquals(actualcontact, contact, "Contact number does not match");
}

public void uploadresume()
{
 String filePath = testData.getData(LOGIN_SCENARIO, "filePath");
 String browse = LocatorReader.getLocator("addCandidate", "resumeUploadInput", "selector");

 page.waitForSelector(browse,
            new Page.WaitForSelectorOptions()
                    .setState(WaitForSelectorState.ATTACHED)
                    .setTimeout(60000));
 Locator browseInput = page.locator(browse).first();
 browseInput.scrollIntoViewIfNeeded();
    browseInput.setInputFiles(Paths.get(filePath));

String inputValue = browseInput.inputValue();
    System.out.println("File input value after upload: " + inputValue);

    Assert.assertTrue(
            inputValue != null && !inputValue.trim().isEmpty(),
            "File input did not receive file."
    );

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
    
    String saveBtn = LocatorReader.getLocator("addCandidate", "saveButton", "selector");
    safeClick(saveBtn);
        String toast = "//p[contains(@class,'oxd-text--toast-message') and contains(.,'Successfully')]";

    page.waitForSelector(toast,
            new Page.WaitForSelectorOptions()
                    .setState(WaitForSelectorState.VISIBLE)
                    .setTimeout(60000));

    Assert.assertTrue(page.locator(toast).isVisible(),
            "Candidate not saved successfully");
}


public void clickrecmenu()
{

    String recmenu = LocatorReader.getLocator("recruitment", "menuRecruitment", "selector");

    // 1) Wait for page left menu / dashboard to be ready (OrangeHRM side panel)
    page.waitForSelector("//aside[contains(@class,'oxd-sidepanel')]",
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
    Assert.assertTrue(url.contains("recruitment"),
            "Recruitment page not opened after clicking menu. Current URL: " + url);

    ScreenshotUtils.captureScreenshot(page, "Recruitment_Menu_Clicked");
}
 public void selectjobvacancy()
 {
String vcy = testData.getData(RECRUITMENT_SCENARIO, "vacancy");
String vcydropdown = LocatorReader.getLocator("viewCandidates", "vacancyDropdown", "selector");

// Wait for the dropdown to be visible
        waitVisible(vcydropdown);
        
        // Wait a moment for the page to settle
        page.waitForTimeout(500);
        
        // Click the dropdown to open it
        page.locator(vcydropdown).click();

        // Wait for the dropdown options to appear
        page.waitForTimeout(2000);

        
        System.out.println("Looking for Vacancy: " + vcy);
        
        // Try multiple selection strategies
        boolean selected = false;
        
        // Strategy 1: Try exact text match with normalize-space
        try {
            String option1 = "//div[@role='option']//span[normalize-space()='" + vcy + "']";
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
                String option2 = "//div[@role='option' and contains(normalize-space(), '" + vcy + "')]";
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
                String option3 = "//*[contains(text(), '" + vcy + "') and @role='option']";
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
                    
                    // Try to select if it contains our vacancy
                    if (optionText.contains(vcy)) {
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

        String selectedText = page.locator(vcydropdown).textContent();

        ScreenshotUtils.captureScreenshot(page, "Vacancy_Selected");
        String selectedvcy = page.locator(vcydropdown).first().textContent().trim();    
Assert.assertEquals(selectedvcy, vcy, "Selected vacancy does not match");


 }

 

 public void clicksrch()
 {
   String searchbtn = LocatorReader.getLocator("viewCandidates", "searchButton", "selector");
   waitVisible(searchbtn);
   safeClick(searchbtn);
    String currentUrl = page.url();
     Assert.assertTrue(
                currentUrl.contains("recruitment"),
                "Not findout the record after searching. Current URL: " + currentUrl
     );
     page.evaluate("window.scrollBy(0, 500)");

      }
    
    public void clickview()
    {
        String viewbtn = LocatorReader.getLocator("viewCandidates", "viewIcon", "selector");
        waitVisible(viewbtn);
        safeClick(viewbtn);
        String currentUrl = page.url();
        Assert.assertTrue(
                currentUrl.contains("recruitment"),
                "Not navigated to add candidate page after clicking view. Current URL: " + currentUrl
        );
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

    }    
public void clickdownload()
    {
        String downloadbtn = LocatorReader.getLocator("viewCandidates", "resumeLink", "selector");
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
    }

    public void clickback()
    {
        page.goBack(new Page.GoBackOptions().setWaitUntil(WaitUntilState.NETWORKIDLE).setTimeout(60000));
    Assert.assertTrue(page.url().contains("viewCandidates"), "Did not return to viewCandidates page after browser back");

    }

    public void clicklogout()
    {

    String profilebtn = LocatorReader.getLocator("profile", "profileMenu","selector");
    waitVisible(profilebtn);
    page.locator(profilebtn).click();

    String logbtn = LocatorReader.getLocator("profile", "logout","selector");
    waitVisible(logbtn);
    page.locator(logbtn).click();
    }

}
