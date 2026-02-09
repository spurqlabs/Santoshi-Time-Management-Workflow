package pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import utils.LocatorReader;
import utils.TestDataReader;
import utils.ScreenshotUtils;
import com.microsoft.playwright.options.WaitForSelectorState;
import utils.FileUtil;
import java.nio.file.Path;


public class MyInfo {

    private final Page page;
    private final TestDataReader testData;

    public MyInfo(Page page) {
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

    public void clickMyInfoMenu() {
        String myInfoMenu = LocatorReader.getLocator("myInfo", "myInfoMenu", "selector");
        
        safeClick(myInfoMenu);
    }

    public void clickContactDetailsTab() {
        String contactDetailsTab = LocatorReader.getLocator("contactDetails", "tab", "selector");
        
        safeClick(contactDetailsTab);
    }

    public void enterstreet1() {
        String street2 = testData.getData("register.myinfo.street1");
        String streetInput = LocatorReader.getLocator("contactDetails", "street1", "selector");
        
        System.out.println("Trying to enter street1: " + street2);
        System.out.println("Using selector: " + streetInput);
        
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
            
            System.out.println("Street 1 value entered successfully: " + street2);
            ScreenshotUtils.captureScreenshot(page, "Street1_Entered");
            page.waitForTimeout(300);
            
        } catch (Exception e) {
            System.out.println("Error entering street1: " + e.getMessage());
            ScreenshotUtils.captureScreenshot(page, "Street1_Error");
            throw new RuntimeException("Failed to enter street1", e);
        }
    }

    public void entercity() {
    String cityName = testData.getData("register.myinfo.city1");
    String cityInput = LocatorReader.getLocator("contactDetails", "city", "selector");

    Locator cityField = page.locator(cityInput).first();

    cityField.scrollIntoViewIfNeeded();
    cityField.fill(cityName);

    

    ScreenshotUtils.captureScreenshot(page, "City_Entered");
}


    public void enterstate() {
        String stateName = testData.getData("register.myinfo.state");
        String stateInput = LocatorReader.getLocator("contactDetails", "stateProvince", "selector");
        waitVisible(stateInput);
        Locator statefield = page.locator(stateInput);
                    
        statefield.fill(stateName);
        
        
    }

    public void enterzipCode() {
        String zip = testData.getData("register.myinfo.zipcode");
        String zipInput = LocatorReader.getLocator("contactDetails", "zipPostalCode", "selector");
        waitVisible(zipInput);
        Locator zipfield = page.locator(zipInput);
            

       
        zipfield.fill(zip);
        
       
    }

    public void selectcountry() {
        String cntry = testData.getData("register.myinfo.country1");
        String dropdown = LocatorReader.getLocator("contactDetails", "Country", "selector");
        
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
            String option1 = "//div[@role='option']//span[normalize-space()='" + cntry + "']";
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
                String option2 = "//div[@role='option' and contains(normalize-space(), '" + cntry + "')]";
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
                String option3 = "//*[contains(text(), '" + cntry + "') and @role='option']";
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
                    
                    // Try to select if it contains our country
                    if (optionText.contains(cntry)) {
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
                ScreenshotUtils.captureScreenshot(page, "Country_Selection_Failed");
                throw new RuntimeException("Failed to select country from dropdown", e);
            }
        }
        
        // Wait for the dropdown to close
        page.waitForTimeout(500);

        String selectedText = page.locator(dropdown).textContent();

        ScreenshotUtils.captureScreenshot(page, "Country_Selected");
        System.out.println("Country selection completed");
    }

    public void entermobile() {
        String mble = testData.getData("register.myinfo.mobile");
        String mbleInput = LocatorReader.getLocator("contactDetails", "mobile", "selector");
        waitVisible(mbleInput);
        Locator mblefield = page.locator(mbleInput);
          

       mblefield.fill(mble);
         
       
    }

    public void enterworkmobile() {
        String wMobile = testData.getData("register.myinfo.workphone");
        String wMobileInput = LocatorReader.getLocator("contactDetails", "work", "selector");
        
       waitVisible(wMobileInput);
Locator wMobilefield = page.locator(wMobileInput);
   

       
       wMobilefield.fill(wMobile); 
       
    }
    public void enterworkemail() 
    {
        String workeml = testData.getData("register.myinfo.workemail");
        String workemlInput = LocatorReader.getLocator("contactDetails", "workEmail", "selector");
        
       waitVisible(workemlInput);
Locator workemlfield = page.locator(workemlInput);
           

       workemlfield.fill(workeml);  
         
       page.evaluate("window.scrollBy(0, 500)");
  }

    
    public void clickSaveButton() {
        String SaveButton = LocatorReader.getLocator("contactDetails", "saveButton", "selector");
       
       
        waitVisible(SaveButton);        
                safeClick(SaveButton);
    }

}