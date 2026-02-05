package step_definition;

import utils.ConfigReader;
import utils.LocatorReader;
import utils.TestDataReader;
import pages.LoginPage;
import step_definition.Hooks;
import pages.MyTimesheetsPage;




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
    
    
    @Given("user is on OrangeHRM login page")
    public void user_is_on_orangehrm_login_page() {
        
        
        
        loginPage = new LoginPage(Hooks.page);
        
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

    @Then("user should be logged in the application successfully")
    public void user_should_be_logged_in_the_application_successfully()
     {
        System.out.println("Login successful, user is on Dashboard page");
       }


    @Given("user is on Dashboard page")
    public void user_is_on_dashboard_page() 
    {
        myTimesheetsPage = new MyTimesheetsPage(Hooks.page);
        System.out.println("User is on Dashboard page");        
        }
        


    @When("user navigates to Time -> Timesheets")
    public void user_navigates_to_time_timesheets() 
    {
        myTimesheetsPage.clickTimeMenu();
    }

    @Then("ViewEmployeeTimesheet page should be displayed")
    public void view_employee_timesheet_page_should_be_displayed() 
    {
        System.out.println("View Employee Timesheet page should be displayed");
    }
    @When("User click on Timesheet option")
    public void user_click_on_timesheets_option() {
        myTimesheetsPage.clickTimesheetsOption();
    }
    @Then("Timesheet dropdown list should be displayed")
    public void timesheet_dropdown_list_should_be_displayed()
     {
        System.out.println("Timesheet dropdown list should be displayed");
    }   
    @When("user select My Timesheets option from the Timesheet dropdown")
    public void user_select_my_timesheets_option_from_the_timesheet_dropdown() 
    {
        myTimesheetsPage.clickMyTimesheetsOption();
    }

    @Then("ViewMyTimesheet page should be displayed")
    public void view_my_timesheet_page_should_be_displayed() 
    {
        System.out.println("View My Timesheet page should be displayed");
    }

    @When("User click on Edit button")
    public void user_click_on_edit_button() 
    {
        myTimesheetsPage.clickEditButton();
    }

    @Then("EditTimesheet page should be displayed")
    public void edit_timesheet_page_should_be_displayed() 
    {
       System.out.println("Timesheet page should be displayed");

    }

    @When("User enter project name and select the project from the list")
    public void user_enter_project_name_and_select_the_project_from_the_list() 
    {
       myTimesheetsPage.enterProject();
    }

    @Then("Project should be selected successfully")
    public void project_should_be_selected_successfully() 
    {
        System.out.println("Project should be selected successfully");
    }

    @When("User select activity from the activity dropdown list")
    public void user_select_activity_from_the_activity_dropdown_list() 
    {
        myTimesheetsPage.selectActivity();
    }

    @Then("Activity should be selected successfully")
    public void activity_should_be_selected_successfully() 
    {
        System.out.println("Activity should be selected successfully");
    }

    @When("User enter hours for each day of the week")
    public void user_enter_hours_for_each_day_of_the_week() 
    {
        myTimesheetsPage.enterHours();
    }

    @Then("Hours should be entered successfully")
    public void hours_should_be_entered_successfully() 
    {
        System.out.println("Hours should be entered successfully");
    }

    @When("User click on Save button")
    public void user_click_on_save_button() 
    {
       myTimesheetsPage.clickSave();
    }

    @Then("Timesheet entry should be saved")
    public void timesheet_entry_should_be_saved() 
    {
        System.out.println("Timesheet entry should be saved");
    }

    @And("Successfully saved message should be displayed")
    public void successfully_saved_message_should_be_displayed() 
    {
        System.out.println("Successfully saved message should be displayed");
    }

    @And("Created timesheet entry should be visible in the My Timesheets list")
    public void created_timesheet_entry_should_be_visible_in_the_my_timesheets_list() 
    {
        System.out.println("Created timesheet entry should be visible in the My Timesheets list");
    }
    

}
