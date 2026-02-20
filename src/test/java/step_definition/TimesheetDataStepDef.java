package step_definition;

import utils.ConfigReader;
import utils.LocatorReader;
import utils.TestDataReader;
import pages.LoginPage;
import step_definition.Hooks;
import pages.MyTimesheetsPage;
import pages.MyInfo;




import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.And;


public class TimesheetDataStepDef {

    LoginPage loginPage;
    MyTimesheetsPage myTimesheetsPage;
    MyInfo myInfoPage;

     
    
    
    @Given("user is on OrangeHRM login page")
    public void user_is_on_orangehrm_login_page() {
        
        
        
        loginPage = new LoginPage(Hooks.getPage());
        
        // Prevent browser from closing immediately (for debug/demo)
        try {
            Thread.sleep(5000); // Wait 5 seconds so you can see the browser
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @When("user enter username")
        public void user_enter_username ()
         {
           
            loginPage.enterUsername();
                     }

        @When("user enter password")
    public void user_enter_password () 
    {
               loginPage.enterPassword();
               
        }

        @When("user click on Login button")
    public void user_click_on_login_button() 
    {
        
        loginPage.clickLogin();
        }

    @Then("the user is logged in to the application")
    public void user_is_logged_in_the_application_successfully()
     {
       
        System.out.println("Login successful, user is on Dashboard page");
        assert loginPage.isDashboardDisplayed() : "Login failed: Dashboard is not displayed after clicking Login";
       }


    @Given("user is on Dashboard page")
    public void user_is_on_dashboard_page() 
    {
        myTimesheetsPage = new MyTimesheetsPage(Hooks.getPage());
        System.out.println("User is on Dashboard page");        
        }
        


    @When("user navigates to Time -> Timesheets")
    public void user_navigates_to_time_timesheets() 
    {
        myTimesheetsPage.clickTimeMenu();
    }

    @Then("ViewEmployeeTimesheet page is displayed")
    public void view_employee_timesheet_page_is_displayed() 
    {
        System.out.println("View Employee Timesheet page should be displayed");
    }
    @When("User click on Timesheet option")
    public void user_click_on_timesheets_option() {
        myTimesheetsPage.clickTimesheetsOption();
    }
    @Then("Timesheet dropdown list is displayed")
    public void timesheet_dropdown_list_is_displayed()
     {
        System.out.println("Timesheet dropdown list should be displayed");
    }   
    @When("user select My Timesheets option from the Timesheet dropdown")
    public void user_select_my_timesheets_option_from_the_timesheet_dropdown() 
    {
        myTimesheetsPage.clickMyTimesheetsOption();
    }

    @Then("ViewMyTimesheet page is displayed")
    public void view_my_timesheet_page_is_displayed() 
    {
        System.out.println("View My Timesheet page should be displayed");
    }

    @When("User click on Edit button")
    public void user_click_on_edit_button() 
    {
        myTimesheetsPage.clickEditButton();
    }

    @Then("EditTimesheet page is displayed")
    public void edit_timesheet_page_is_displayed() 
    {
       System.out.println("Timesheet page should be displayed");

    }
    @When("User click on Cancel button")
  public void user_click_on_cancel_button() 
  {
    myTimesheetsPage.clickCancelButton();
  }

  @Then("My Timesheet page is displayed")
  public void my_timesheet_page_is_displayed() 
  {
    System.out.println("My Timesheet page should be displayed");
  }

  @Given("user is on dashbrd page")
  public void user_is_on_dashbrd_page() 
  {
    myInfoPage = new MyInfo(Hooks.getPage());
    System.out.println("User is on dashboard page");
  }

  @When("user click on My Info tab")
  public void user_click_on_my_info_tab()
   {
         myInfoPage.clickMyInfoMenu();
  }

  @Then("viewPersonalDetails page is displayed with all personal details of the user")
  public void view_personal_details_page_is_displayed()
   {
    System.out.println("viewPersonalDetails page is displayed with all personal details of the user");
  }

  @When("user click on Contact Details subtab")
  public void user_click_on_contact_details_subtab() 
  {
    myInfoPage.clickContactDetailsTab();
  }

  @Then("ContactDetails page is displayed")
  public void contact_details_page_is_displayed() 
  {
    System.out.println("ContactDetails page is displayed");
    }

  // ---------- Enter contact fields (test data separated) ----------
  @When("user enter steet1")
  public void user_enter_street1() 
  {
    myInfoPage.enterstreet1();

  }

  @When("user enter city")
  public void user_enter_city() {
    myInfoPage.entercity();
  }

  @When("user enter state")
  public void user_enter_state() {
    myInfoPage.enterstate();

  }

  @When("user enter zip code")
  public void user_enter_zip_code() {
    myInfoPage.enterzipCode();
  }

  @When("user enter country")
  public void user_enter_country() {
    myInfoPage.selectcountry();
  }

  @When("user enter mobile")
  public void user_enter_mobile() {
    myInfoPage.entermobile();
  }

  @When("user enter work phone")
  public void user_enter_work_phone() {
    myInfoPage.enterworkmobile();    
  }

  @When("user enter work email")
  public void user_enter_work_email() {
    myInfoPage.enterworkemail();
  }

  @When("user click on Save button")
  public void user_click_on_save_button() {
    myInfoPage.clickSaveButton();
  }

  @Then("Contact Details is saved successfully")
  public void contact_details_is_saved_successfully() 
  {
   System.out.println("Contact Details is saved successfully");
  }
  
  
   
}
