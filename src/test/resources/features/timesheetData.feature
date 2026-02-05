Feature: My Timesheet Management in OrangeHRM
Background:
    Given user is on OrangeHRM login page
    When user enter username
    When user enter password
    When user click on Login button
    Then user should be logged in the application successfully

  @login
  Scenario: TC001 - Login to application
    # Login already done in Background
    Then user should be logged in the application successfully

  @addtimesheetdata
  Scenario: TC002 - Add Timesheet details
    Given user is on Dashboard page
    When  user navigates to Time -> Timesheets
    Then ViewEmployeeTimesheet page should be displayed
    When User click on Timesheet option
    Then Timesheet dropdown list should be displayed
    When user select My Timesheets option from the Timesheet dropdown
    Then ViewMyTimesheet page should be displayed
  
    When User click on Edit button
    Then EditTimesheet page should be displayed
    When User enter project name and select the project from the list
    Then Project should be selected successfully
    When User select activity from the activity dropdown list
    Then Activity should be selected successfully
    When User enter hours for each day of the week
    Then Hours should be entered successfully
    When User click on Save button
    Then Timesheet entry should be saved
    And Successfully saved message should be displayed
    And Created timesheet entry should be visible in the My Timesheets list







