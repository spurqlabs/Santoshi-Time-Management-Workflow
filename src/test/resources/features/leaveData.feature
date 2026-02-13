Feature: Leave Management in OrangeHRM
Background:
  Given user is on OrangeHRM login page
  When user enter username
  When user enter password
  When user click on Login button
  Then the user is logged in to the application
  Given user is on dashboard page
  
  

  @smoke2
  Scenario: TC006 - Add Entitlements for the apply leaves  
  When user click on Leave menu 
  Then user navigate to viewLeaveList page
  When user click on Entitlements tab and select Add Entitlements option 
  Then user navigate to addLeaveEntitlement page
  When user enter Employee Name
  When user select Leave Type
  When user enter Entitlement
  When user click on savebutton
  When user click on confirm button from the Updating Entitlement Pop up
  Then the leave entitlement is added successfully

  @smoke3
  Scenario: TC007 - Apply for Leave
  When user_click_Leave_left_side_menu 
  When user click on Apply tab 
  Then user navigate to applyLeave page
  When user_select_leave_type "<leaveType>"
  When user select from date "<fromDate>"
  When user select to date "<toDate>"
  When user select partial days option "<partialDay>"
  When user select duration "<duration>"
  When user enter comments in the textbox "<comment>"
  When user click on apply button
  Then entered details is saved and leave is applied successfully

  Examples:
  | leaveType      | fromDate   | toDate     | partialDay | duration             | comment        |
  | CAN - Vacation | 2026-02-02 | 2026-03-02 | All Days   | Half Day - Morning   | Travel Leave   |
  | CAN - Vacation | 2026-10-03 | 2026-11-03 | All Days   | Half Day - Afternoon | Friend Leave   |

  @regression2
Scenario: TC008 - Verify applied leave appears on My leave page
  When user click_Leave_leftmenu
  When user click on My Leave tab
  Then user navigate to viewMyLeaveList page
  When user select From Date
  When user select To Date
  When user click on Search Button
  Then the applied leave details is displayed with the “Pending Approval” status
  When user click on profile icon and select logout option
  Then From application user is getting logged out successfully
  