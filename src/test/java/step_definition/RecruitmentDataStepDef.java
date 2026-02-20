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

@When ("user added a new candidate")
public void add_new_candidate()
{
addCandidateDetailsPage.clickadd();
addCandidateDetailsPage.enterfirstName();
addCandidateDetailsPage.enterlastName();
addCandidateDetailsPage.selectvacancy();
addCandidateDetailsPage.enteremail();
addCandidateDetailsPage.entercontact();
addCandidateDetailsPage.uploadresume();
addCandidateDetailsPage.clicksave();
}



@Then ("candidate is created sucessfully")
public void candidate_added()
{
System.out.println("New User is added successfully");

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

@When ("user click on Shortlist button")
public void shortlist()
{

addCandidateDetailsPage.clickshortlist();

}


@Then ("user navigate to changeCandidateVacancyStatus page")
public void candidatevcypage()
{
System.out.println("User navigate to changeCandidateVacancyStatus page for the shorlist the candidate");
}

@When ("user enter Notes and click save_BTN")
public void enternote()
{
   addCandidateDetailsPage.enternote();
   addCandidateDetailsPage.saveclick();
}

@Then ("entered details are saved and navigate to addCandidate page")
public void details()
{
    System.out.println("Entered details are saved and navigate to addCandidate page");
}

@When ("user click on Schedule Interview button")
public void scheduleinterview()
{
addCandidateDetailsPage.clickscheduleinterview();

}


@Then ("user navigate to Schedule Interview page for the entered interview details")
public void schedule_interview()
{
  System.out.println("User navigate to Schedule Interview page for the entered interview details");
}

@When ("user enter interview schedule details and click_SAVE")
public void interview_details()
{
addCandidateDetailsPage.entertitle();
addCandidateDetailsPage.enterineterviewer();    
addCandidateDetailsPage.selectdate();
addCandidateDetailsPage.selecttime();
addCandidateDetailsPage.note();
addCandidateDetailsPage.saveclick();
}

@Then ("entered interview details are saved and navigate to Applcation stage page with Interview Scheduled status")
public void interivew_details_save()
{
System.out.println("Entered interview details are saved and navigate to Applcation stage page with Interview Scheduled status");
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




