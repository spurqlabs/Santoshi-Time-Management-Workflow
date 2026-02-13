package step_definition;

import utils.ConfigReader;
import utils.LocatorReader;
import utils.TestDataReader;
import pages.AddEntitlementsPage;
import pages.ApplyleavePage;
import pages.MyleavePage;
import step_definition.Hooks;
import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import io.cucumber.java.en.Given;
import io.cucumber.java.Before;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.And;

public class LeaveDataStepDef 
{
   private AddEntitlementsPage addEntitlementsPage;
   private ApplyleavePage applyleavePage;
   private MyleavePage myleavePage;

@Before
   public void initializePage() {
        addEntitlementsPage = new AddEntitlementsPage(Hooks.getPage());
        applyleavePage = new ApplyleavePage(Hooks.getPage());
        myleavePage = new MyleavePage(Hooks.getPage());
    }    

    @Given("user_is_on_dashboard_page")
    public void user_is_on_dashboard_page() {
        
        // kept for semantic step mapping; page already initialized in @Before
        try {
            Thread.sleep(5000); // Wait 5 seconds so you can see the browser
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @When("user click on Leave menu")
    public void user_click_on_leave_menu()
    {
      addEntitlementsPage.clickleavemenu();

    }

    @Then("user navigate to viewLeaveList page")
    public void user_navigate_to_view_leave_list_page()
    {
       System.out.println("User is on viewLeaveList page"); 
        }

 @When("user click on Entitlements tab and select Add Entitlements option")
    public void user_click_on_entitlements_tab_and_select_add_entitlements_option() {
        addEntitlementsPage.clickentitlementstab();
    }

    @Then("user navigate to addLeaveEntitlement page")
    public void user_navigate_to_add_leave_entitlement_page() {
        System.out.println("User navigate to addLeaveEntitlement page");
    }

    @When("user enter Employee Name")
    public void user_enter_employee_name() {
        addEntitlementsPage.enteremployeeName();
    }
    @When("user select Leave Type")
    public void user_select_leave_type() {
        addEntitlementsPage.selectleaveType();
    }



@When("user enter Entitlement")
public void user_enter_entitlement()
{  
addEntitlementsPage.enterentitlement();

}
@When("user click on savebutton")
public void user_click_on_savebutton()
{   
    addEntitlementsPage.clickonsavebtn();
}
@When("user click on confirm button from the Updating Entitlement Pop up")
public void clickonconfirmbtn()
{
addEntitlementsPage.clickonconfirm();
}
    
@Then("the leave entitlement is added successfully")
public void leavedata_is_added()
{
    System.out.println("Leave entitlement added successfully");
}

@When("user_click_Leave_left_side_menu")
    public void leftmenu()
    {
      addEntitlementsPage.clickleavemenu();

    }
@When ("user click on Apply tab")
public void clickapply()
{
    applyleavePage.clickapplytab();
}
@Then ("user navigate to applyLeave page")
public void applyleavepage()
{
    System.out.println("User navigate to applyLeave page");
}
@When("user_select_leave_type {string}")
public void selectleavetype(String leaveType)
{

applyleavePage.leavetype(leaveType);
}

@When ("user select from date {string}")
public void fromdate(String fromDate)
{
    applyleavePage.selectfromdate(fromDate);
}
@When ("user select to date {string}")
public void todate(String toDate)
{
    applyleavePage.selecttodate(toDate);
}

@When("user select partial days option {string}")
public void partialday(String partialDay)
{
    applyleavePage.selectpartial(partialDay);
}

@When ("user select duration {string}")
public void duration(String duration)
{
    applyleavePage.selectduration(duration);
}

@When ("user enter comments in the textbox {string}")
public void comment(String comment)
{
    applyleavePage.entercmt(comment);
}

@When("user click on apply button")
public void applybtn()
{

    applyleavePage.clickapply();
}

@Then("entered details is saved and leave is applied successfully")
public void leave_applied()
{
    System.out.println("Leave applied successfully");
}

@When("user click_Leave_leftmenu")
public void click_leave_leftmenu()
{
    addEntitlementsPage.clickleavemenu();
    }

@When("user click on My Leave tab")
    public void clickmyleavetab()
    {
      myleavePage.clickmyleave();
    }
    
@Then("user navigate to viewMyLeaveList page")
public void viewmyleave()
{
    System.out.println("User navigate to viewMyLeaveList page");
}
@When ("user select From Date")
public void selectfromdate1()
{
myleavePage.datefromselect();
}

@When ("user select To Date")
public void selecttodate1()
{
   myleavePage.datetoselect();
}

@When ("user click on Search Button")
public void clicksrch()
{
myleavePage.clicksrch();
}

@Then ("the applied leave details is displayed with the “Pending Approval” status")
public void leavedetails()
{
    System.out.println("Applied leave details is displayed with the “Pending Approval” status");
}

@When ("user click on profile icon and select logout option")
public void out()
{
  myleavePage.clickout();
}

@Then ("From application user is getting logged out successfully")
public void out1()
{
    System.out.println("User is logged out successfully from the application");
}
}