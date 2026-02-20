package step_definition;

import utils.ConfigReader;
import utils.LocatorReader;
import utils.TestDataReader;
import pages.AddnewuserPage;
import pages.EditdetailPage;
import pages.ModifyUserPage;
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

public class UserManagementStepDef
{

private AddnewuserPage addnewuserPage;
private EditdetailPage editdetailPage;
private ModifyUserPage modifyUserPage;

@Given("User_on_Dashboard_Page")
    public void Dashboardpage() {

        addnewuserPage = new AddnewuserPage(Hooks.getPage());
        editdetailPage = new EditdetailPage(Hooks.getPage());
        modifyUserPage = new ModifyUserPage(Hooks.getPage());
        
        // kept for semantic step mapping; page already initialized in @Before
        try {
            Thread.sleep(5000); // Wait 5 seconds so you can see the browser
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

@When ("user click on Admin menu")
public void adminmenu()
{
 addnewuserPage.clickadminmenu();

}

@Then ("user navigate to viewSystemUsers page")
public void viewSystemUserspage()
{
    System.out.println("User navigate to viewSystemUsers page");
}

@When ("user click on addbutton")
public void addBTN()
{
    addnewuserPage.clickaddBtn();
}

@Then ("user navigate to saveSystemUser page")
public void savesystemUserpage()
{
    System.out.println("User navigate to saveSystemUser page");
}

@When ("user select User Role {string}")
public void selectrole(String UserRole)
{
    addnewuserPage.selectUserrole(UserRole);
}

@When ("user enter EmployeeName {string}")
public void Empname(String EmployeeName)
{
    addnewuserPage.enterempName(EmployeeName);
}

@When ("user select Status {string}")
public void status(String status)
{
    addnewuserPage.selectstatus(status);
}

@When ("user enter Username {string}")
public void username(String username)
{

    addnewuserPage.enteruname(username);
}

@When ("user enter Password {string}")
public void password(String password)
{

    addnewuserPage.enterpwd(password);
}

@When ("user enter Confirm Password {string}")
public void confirmpassword(String confirmpassword)
{
    addnewuserPage.enterconfirmpwd(confirmpassword);
}

@When ("user click SAVE_Button")
public void saveBTN()
{
    addnewuserPage.saveBTN();
}

@Then ("the new user is added successfully")
public void user_added()
{
    System.out.println("New user is added successfully");
}


@When("user enter created first Username and click search button {string}")
public void srchuser1(String username)
{
  addnewuserPage.user1(username);
}


 @Then ("Username is displayed in the System user list")
public void user1_displayed()
{
    System.out.println("First created Username is displayed in the System user list");
}

@When ("user enter created second User_name and click search button {string}")
public void srchuser2(String username)
{
addnewuserPage.user1(username);

}

@Then ("User_name is displayed in the System_user list")
public void user2_displayed()
{
    System.out.println("Second created User_name is displayed in the System_user list");
}

@When("user click on Admin left menu")
public void clickadminmenu()
{
    addnewuserPage.clickadminmenu();
}

@When ("user enter username in search box {string} and click on Search button")
public void srchusername(String username)
{
    addnewuserPage.user1(username);
}

@Then ("the entered user is displayed on the screen")
public void displayed_user()
{
    System.out.println("Entered user is displayed on the screen");
}


@When ("user click on Edit button")
public void edit()
{
    editdetailPage.clickedit();

}

@Then ("user navigate to save_SystemUser page")
public void SystemUserPage()
{
    System.out.println("User navigate to saveSystemUser page");
}


@When ("user change the User role {string}")
public void edituserRole(String newUserRole)
{
    editdetailPage.editUserRole(newUserRole);
}


@When ("user changed the status {string}")
public void editstatus(String newStatus)
{
    editdetailPage.editstatus(newStatus);
}


@When ("user click on SAVE Button")
public void SAVE()
{
    editdetailPage.clickSAVE();
}


@Then ("user details is updated sucessfully")
public void updated()
{
    System.out.println("User details is updated successfully");
}


@When ("user enter user_Name and click search button {string}")
public void srchUser(String username)
{
    editdetailPage.user1(username);
}

@Then ("Updated data is displayed")
public void updated_data()
{
    System.out.println("Updated user details is displayed in the System user list");
}

@When ("user click on Admin left tab")
public void click_admin_left_tab()
{
    addnewuserPage.clickadminmenu();
}

@When ("user enter User_Name in search box {string} and click on Search button")
public void username_srch(String username)
{
    addnewuserPage.user1(username);
}

@Then ("the Entered User_Name is displayed on the screen")
public void disaplyeduser()
{
    System.out.println("Entered User_Name is displayed on the screen");
}

@When ("user click on delete icon")
public void delete()
{
    modifyUserPage.clickdelete();
}

@When ("user click on Yes, Delete button from the pop up")
public void delete_confirm()
{
    modifyUserPage.confirmdelete();
}


@Then ("the user is deleted successfully and should not be displayed in the list")
public void deleted()
{
    System.out.println("User is deleted successfully and should not be displayed in the list");
}

@When ("user click Admin left menu")
public void adminleftmenu()
{
    addnewuserPage.clickadminmenu();
}

@When ("user click on Add")
public void clickadd()
{
    addnewuserPage.clickaddBtn();
}

@Then ("user_navigate_to_saveSystemUser_page")
public void userpage()
{
    System.out.println("User navigate to saveSystemUser page");
}

@When ("user select UserRole {string}")
public void roleuser(String UserRole)
{
    addnewuserPage.selectUserrole(UserRole);
}

@When ("user enter Employee_Name {string}")
public void nameEmp(String EmployeeName)
{
    addnewuserPage.enterempName(EmployeeName);
}

@When ("user select_Status {string}")
public void status_user(String status)
{
    addnewuserPage.selectstatus(status);
}


@When ("user enter Duplicate Username {string}")
public void duplicate_username(String username)
{
    addnewuserPage.enteruname(username);
}

@Then ("Already exists error message is displayed")
public void error_message()
{
    System.out.println("Already exists error message is displayed");
}


@When ("user click_profile icon and select logout option")
public void user_logout()
{
    modifyUserPage.click_logout();
}


@Then ("user is getting logged out successfully")
public void user_loggedout()
{
    System.out.println("User is getting logged out successfully");
}



























}