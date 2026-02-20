package pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import utils.LocatorReader;
import utils.TestDataReader;
import utils.ScreenshotUtils;
import com.microsoft.playwright.options.WaitForSelectorState;
import org.testng.Assert;
import utils.FileUtil;
import java.nio.file.Path;
import org.slf4j.Logger;
import utils.LoggerUtil;


public class MyInfo {

    private static final Logger log = LoggerUtil.getLogger(MyInfo.class);

    private final Page page;
    private final TestDataReader testData;
        private static final String LOGIN_SCENARIO = "TC003 - Add Contact details";


    public MyInfo(Page page) {
        this.page = page;
        this.testData = new TestDataReader("Testdata/timesheetData.json");
        log.info("MyInfo page object initialized");
    }

    private void waitVisible(String selector) {
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
             log.warn("Normal click failed, trying force click for selector: {}", selector);
            loc.click(
                new Locator.ClickOptions()
                    .setForce(true)
                    .setTimeout(30000)
            );
        }
    }

    public void clickMyInfoMenu() {
        log.info("Clicking My Info menu");
        String myInfoMenu = LocatorReader.getLocator("locatortimesheet","myInfo", "myInfoMenu", "selector");
        
        safeClick(myInfoMenu);

        String url = page.url();
         log.info("URL after clicking My Info menu: {}", url);
    Assert.assertTrue(url.contains("viewPersonalDetails"),
            "viewPersonalDetails page not opened after clicking MyInfo menu. Current URL: " + url);
      log.info("My Info menu navigation successful");
    }

    public void clickContactDetailsTab() {
         log.info("Clicking Contact Details tab");
        String contactDetailsTab = LocatorReader.getLocator("locatortimesheet","contactDetails", "tab", "selector");
        
        safeClick(contactDetailsTab);

        String url = page.url();
        log.info("URL after clicking Contact Details tab: {}", url);
    Assert.assertTrue(url.contains("contactDetails"),
            "ContactDetails page not opened after clicking Contact Details tab. Current URL: " + url);
       log.info("Contact Details tab navigation successful");
    }

    public void enterstreet1() {
        String street2 = testData.getData(LOGIN_SCENARIO, "street1");
        String streetInput = LocatorReader.getLocator("locatortimesheet","contactDetails", "street1", "selector");
        
         log.info("Entering Street 1 value");
        log.debug("Street 1 selector: {}", streetInput);
        log.debug("Street 1 value: {}", street2);

        
        try {
            // Wait for the field to be visible
            waitVisible(streetInput);
            
            // Extra wait to ensure field is fully loaded
            page.waitForTimeout(500);
            
            // Get the locator
            Locator inputField = page.locator(streetInput);
            
            // Click to focus the field
            inputField.click();
            page.waitForTimeout(500);
            
            // Triple-click to select all
            inputField.click(new Locator.ClickOptions().setClickCount(3));
            page.waitForTimeout(200);
            
            // Type the value
            inputField.type(street2, new Locator.TypeOptions().setDelay(50));

            log.info("Street 1 entered successfully");
            
            ScreenshotUtils.captureScreenshot(page, "Street1_Entered");
            page.waitForTimeout(300);
            
        } catch (Exception e) {
            log.error("Failed to enter street1. Error: {}", e.getMessage(), e);
            
            ScreenshotUtils.captureScreenshot(page, "Street1_Error");
            throw new RuntimeException("Failed to enter street1", e);
        }

        String actualstreet = page.locator(streetInput).inputValue();
        log.info("Validating Street 1 value. Actual: {}", actualstreet);
        Assert.assertEquals(actualstreet, street2, "Street 1 does not match");
        log.info("Street 1 value validated successfully");
    }

    public void entercity() {
    String cityName = testData.getData(LOGIN_SCENARIO, "city1");
    String cityInput = LocatorReader.getLocator("locatortimesheet","contactDetails", "city", "selector");
        
        log.info("Entering City");
        log.debug("City selector: {}", cityInput);
        log.debug("City value: {}", cityName);

    Locator cityField = page.locator(cityInput).first();

    cityField.scrollIntoViewIfNeeded();
    cityField.fill(cityName);

    String actualcity = page.locator(cityInput).inputValue();
     log.info("Validating City. Actual: {}", actualcity);

        Assert.assertEquals(actualcity, cityName, "City does not match");
        log.info("City value validated successfully");

    ScreenshotUtils.captureScreenshot(page, "City_Entered");
}


    public void enterstate() {
        String stateName = testData.getData(LOGIN_SCENARIO, "state");
        String stateInput = LocatorReader.getLocator("locatortimesheet","contactDetails", "stateProvince", "selector");
        
          log.info("Entering State/Province");
        log.debug("State selector: {}", stateInput);
        log.debug("State value: {}", stateName);

        waitVisible(stateInput);
        Locator statefield = page.locator(stateInput);
                    
        statefield.fill(stateName);

        String actualstate = page.locator(stateInput).inputValue();
         log.info("Validating State. Actual: {}", actualstate);

        Assert.assertEquals(actualstate, stateName, "State does not match");
        log.info("State value validated successfully");
        
    }

    public void enterzipCode() {
        String zip = testData.getData(LOGIN_SCENARIO,"zipcode");
        String zipInput = LocatorReader.getLocator("locatortimesheet","contactDetails", "zipPostalCode", "selector");
        
        log.info("Entering Zip/Postal Code");
        log.debug("Zip selector: {}", zipInput);
        log.debug("Zip value: {}", zip);

        waitVisible(zipInput);
        Locator zipfield = page.locator(zipInput);
                   
        zipfield.fill(zip);
        String actualzip = page.locator(zipInput).inputValue();
        log.info("Validating Zip. Actual: {}", actualzip);

        Assert.assertEquals(actualzip, zip, "Zip code does not match");
        log.info("Zip code value validated successfully");
        
       
    }

    public void selectcountry() {
        String cntry = testData.getData(LOGIN_SCENARIO,"country1");
        String dropdown = LocatorReader.getLocator("locatortimesheet","contactDetails", "Country", "selector");
        
        log.info("Selecting Country");
        log.debug("Country dropdown selector: {}", dropdown);
        log.debug("Expected country: {}", cntry);


        // Wait for the dropdown to be visible
        waitVisible(dropdown);
        
        // Wait a moment for the page to settle
        page.waitForTimeout(500);
        
        // Click the dropdown to open it
        page.locator(dropdown).click();

        // Wait for the dropdown options to appear
        page.waitForTimeout(2000);

        // Take a screenshot for debugging
        ScreenshotUtils.captureScreenshot(page, "Country_Dropdown_Opened");
        
        System.out.println("Looking for country: " + cntry);
        
        // Try multiple selection strategies
        boolean selected = false;
        
        // Strategy 1: Try exact text match with normalize-space
        try {
            String option1 = LocatorReader.getLocator("locatortimesheet","contactDetails", "optionselect", "selector");
                   
             String cntryoption = option1 + "'" + cntry + "']";            
            
            if (page.locator(cntryoption).count() > 0) {
                log.info("Country found using strategy 1 (exact match)");
                page.locator(cntryoption).first().click();
                selected = true;
            }
        } catch (Exception e) {
            log.warn("Country strategy 1 failed: {}", e.getMessage());
            
        }
        
        // Strategy 2: Try contains text
        if (!selected) {
            try {

                String option2 = LocatorReader.getLocator("locatortimesheet","contactDetails", "optionselect", "selector");
                   
             String cntryoption1 = option2 + "'" + cntry + "']";  
                if (page.locator(cntryoption1).count() > 0) {
                    log.info("Country found using strategy 2 exact match)");
                    
                    page.locator(cntryoption1).first().click();
                    selected = true;
                }


            } catch (Exception e) {
                log.warn("Country strategy 2 failed: {}", e.getMessage());
                
            }
        }
        
        // Strategy 3: Try clicking any div with role=option that contains the country text
        if (!selected) {
            try {
                 
                 String optionTemplate2  = LocatorReader.getLocator("locatortimesheet","contactDetails", "dropdownOptionContains2", "selector");
                String option9 = optionTemplate2.replace("{value}", cntry);
                Locator opt5 = page.locator(option9);

                 if (opt5.count() > 0) {
                    log.info("Country found using strategy 3 (contains)");
                    
                    opt5.first().click();
                    selected = true;
                }
            } catch (Exception e) {
                log.warn("Country strategy 3 failed: {}", e.getMessage());
            }
        }
        
        // Strategy 4: Get all options and print them for debugging
        if (!selected) {
            try {
                log.warn("Country not found directly. Printing available options (debug).");
                
                String optionsLoc1 = LocatorReader.getLocator("locatortimesheet","contactDetails", "rolecount2", "selector");
            Locator optloc1 = page.locator(optionsLoc1);

                int count = optloc1.count();
                log.info("Total country options found: {}", count);
                
                
                for (int i = 0; i < Math.min(count, 10); i++) {
                    String optionText = optloc1.nth(i).textContent();
                    log.debug("Option {}: {}", i, optionText);
                   
                    
                    // Try to select if it contains our country
                    if (optionText.contains(cntry)) {
                        log.info("Country matched at index {}", i);
                        
                        optloc1.nth(i).click();
                        selected = true;
                        break;
                    }
                }
            } catch (Exception e) {
                log.warn("Country strategy 4 failed: {}", e.getMessage());
                
            }
        }
        
        // Fallback: Click first option
        if (!selected) {
            try {

                System.out.println("Selecting first available option as fallback");

                 String optionsLoc2 = LocatorReader.getLocator("locatortimesheet","contactDetails", "rolecount2", "selector");
            Locator optloc2 = page.locator(optionsLoc2);

                optloc2.first().click();
                selected = true;
            } catch (Exception e) {
                System.out.println("Fallback selection failed: " + e.getMessage());
                ScreenshotUtils.captureScreenshot(page, "Country_Selection_Failed");
                throw new RuntimeException("Failed to select country from dropdown", e);
            }
        }
        
        // Wait for the dropdown to close
        page.waitForTimeout(500);

        String selectedText = page.locator(dropdown).textContent();

        ScreenshotUtils.captureScreenshot(page, "Country_Selected");
        System.out.println("Country selection completed");

        // Verify selection - use dropdown text content as the displayed value
        String actualcountry = page.locator(dropdown).textContent();
         log.info("Validating Country. Actual: {}", actualcountry);

        Assert.assertTrue(actualcountry != null && actualcountry.contains(cntry), "Country does not match. Actual: " + actualcountry);
         log.info("Country value validated successfully");
    }

    public void entermobile() {
        String mble = testData.getData(LOGIN_SCENARIO,"mobile");
        String mbleInput = LocatorReader.getLocator("locatortimesheet","contactDetails", "mobile", "selector");
        
        log.info("Entering Mobile number");
        log.debug("Mobile selector: {}", mbleInput);
        log.debug("Mobile value: {}", mble);
        
        waitVisible(mbleInput);
        Locator mblefield = page.locator(mbleInput);
          

       mblefield.fill(mble);

       String actualmobile = page.locator(mbleInput).inputValue();
         log.info("Validating Mobile. Actual: {}", actualmobile);
        Assert.assertEquals(actualmobile, mble, "Mobile number does not match");
        log.info("Mobile number value validated successfully");
         ScreenshotUtils.captureScreenshot(page, "Mobile_Entered");
    }

    public void enterworkmobile() {
        String wMobile = testData.getData(LOGIN_SCENARIO,"workphone");
        String wMobileInput = LocatorReader.getLocator("locatortimesheet","contactDetails", "work", "selector");
        
          log.info("Entering Work phone");
        log.debug("Work phone selector: {}", wMobileInput);
        log.debug("Work phone value: {}", wMobile);

       waitVisible(wMobileInput);
  Locator wMobilefield = page.locator(wMobileInput);
         
       wMobilefield.fill(wMobile); 

       String actualwmobile = page.locator(wMobileInput).inputValue();
       log.info("Validating Work phone. Actual: {}", actualwmobile);

        Assert.assertEquals(actualwmobile, wMobile, "Work mobile number does not match");
       log.info("Work phone value validated successfully");
        ScreenshotUtils.captureScreenshot(page, "WorkPhone_Entered");
    }
    public void enterworkemail() 
    {
        String workeml = testData.getData(LOGIN_SCENARIO,"workemail");
        String workemlInput = LocatorReader.getLocator("locatortimesheet","contactDetails", "workEmail", "selector");
        
       log.info("Entering Work Email");
        log.debug("Work email selector: {}", workemlInput);
        log.debug("Work email value: {}", workeml);
        

       waitVisible(workemlInput);
      Locator workemlfield = page.locator(workemlInput);
           
       workemlfield.fill(workeml);  
         
       page.evaluate("window.scrollBy(0, 500)");

       String actualworkemail = page.locator(workemlInput).inputValue();
       log.info("Validating Work Email. Actual: {}", actualworkemail);

        Assert.assertEquals(actualworkemail, workeml, "Work email does not match");
        log.info("Work email value validated successfully");
        ScreenshotUtils.captureScreenshot(page, "WorkEmail_Entered");
  }

    
    public void clickSaveButton() {
        log.info("Clicking Save button for Contact Details");

        String SaveButton = LocatorReader.getLocator("locatortimesheet","contactDetails", "saveButton", "selector");
        safeClick(SaveButton);

         String toast6 = LocatorReader.getLocator("locatortimesheet","contactDetails", "toast", "selector");
          
           log.info("Waiting for toast after saving contact details");

    page.waitForSelector(toast6,
            new Page.WaitForSelectorOptions()
                    .setState(WaitForSelectorState.VISIBLE)
                    .setTimeout(80000));

       Assert.assertTrue(page.locator(toast6).isVisible(),
            "Contact details are not saved successfully");
            
            log.info("Contact details saved successfully");
        ScreenshotUtils.captureScreenshot(page, "ContactDetails_Saved");




    }

}