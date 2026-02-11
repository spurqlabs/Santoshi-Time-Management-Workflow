package step_definition;

import utils.ConfigReader;
import utils.LocatorReader;
import utils.TestDataReader;
import pages.AddCandidatedetails;
import step_definition.Hooks;
import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.And;

public class RecruitmentDataStepDef {

    AddCandidatedetails addCandidateDetailsPage;
     
    
    
    @Given("user is on dashboard page")
    public void user_is_on_dashboard_page() {
        addCandidateDetailsPage = new AddCandidatedetails(Hooks.getPage());
        
        // Prevent browser from closing immediately (for debug/demo)
        try {
            Thread.sleep(5000); // Wait 5 seconds so you can see the browser
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @When("user click on Recruitment menu")
        public void user_click_on_recruitment_menu ()
         {
           
            addCandidateDetailsPage.clickrecruitmentmenu();
                     }
    @Then("user navigate to viewCandidates page")
    public void user_navigate_to_view_candidates_page ()
    {

        System.out.println("User navigated to viewCandidates page");
    }

        @When("user click on Add button")
    public void user_click_on_add_button () 
    {
               addCandidateDetailsPage.clickadd();
               
        }
        @Then ("user navigate to addCandidate page")
        public void user_navigate_to_add_candidate_page()
        {
            System.out.println("User navigated to addCandidate page");
        }
        @When("user enter First Name")
        public void user_enter_first_name() {
            addCandidateDetailsPage.enterfirstName();
        }
        @When("user enter Last Name")
        public void user_enter_last_name() {
            addCandidateDetailsPage.enterlastName();
        }
        @When("user select Job Vacancy")
        public void user_select_job_vacancy() {
            addCandidateDetailsPage.selectvacancy();
        }
       @When("user enter Email")
    public void user_enter_email() {
        addCandidateDetailsPage.enteremail();
    }
      @When("user enter Contact Number")
    public void user_enter_contact_number() {
        addCandidateDetailsPage.entercontact();
    }

    @When("user upload Resume")
    public void user_upload_resume() 
    {
        addCandidateDetailsPage.uploadresume();
    }

@When("user click on Savebutton")
public void user_click_on_save_button() 
{
    addCandidateDetailsPage.clicksave();
}

@Then("the candidate details is added successfully")
public void the_candidate_details_is_added_successfully() {
    System.out.println("Candidate details added successfully.");
}

@Given("user is on home page")
public void user_is_on_home_page() {
    addCandidateDetailsPage = new AddCandidatedetails(Hooks.getPage());
    System.out.println("User is on Home page");
}

@When("user click on Recruit_menu")
public void user_click_on_recruit_menu() 
{
    addCandidateDetailsPage.clickrecmenu();
    
}

@Then("user navigate to recruitment page")
public void user_navigate_to_recruitment_page() {
    System.out.println("User navigated to recruitment page");
}

@When("user select vacancy from drop down")
public void user_select_vacancy_from_drop_down() {
    addCandidateDetailsPage.selectjobvacancy();
}


@When("user click on search button")
public void user_click_on_search_button() {
    addCandidateDetailsPage.clicksrch();
}

@Then("the candidate details is displayed on the page")
public void the_candidate_details_is_displayed_on_the_page() {
    System.out.println("Candidate details displayed on the page.");
}

@When("user click on view icon")
public void user_click_on_view_icon() {   
    addCandidateDetailsPage.clickview();
}

@Then("user navigate to Candidate details page")
public void user_navigate_to_candidate_details_page() {
    System.out.println("User navigated to Candidate details page");
}

@When("user verify the entered details")
public void user_verify_the_entered_details() 
{
   addCandidateDetailsPage.verifydetails();
}
@Then ("all the entered details is displayed correctly")
public void all_the_entered_details_is_displayed_correctly() {
    System.out.println("All the entered details is displayed correctly.");
}

@When("user click on download button for resume")
public void user_click_on_download_button_for_resume() 
{
    addCandidateDetailsPage.clickdownload();
}

@Then("the resume is downloaded successfully")
public void the_resume_is_downloaded_successfully() {   
    System.out.println("Resume downloaded successfully.");
}

@When("user click on back button from browser")
public void user_click_on_back_button_from_browser() {
    addCandidateDetailsPage.clickback();
}

@Then ("user navigate to Candidates list page")
public void user_navigate_to_candidates_list_page() {
    System.out.println("User navigated to Candidates list page");
}

@When("user click on Logout button from profile menu")
public void user_click_on_logout_button_from_profile_menu() {
    addCandidateDetailsPage.clicklogout();
}

@Then("user is logged out from the application")
public void user_is_logged_out_from_the_application() {
    System.out.println("User logged out successfully.");
}

}




