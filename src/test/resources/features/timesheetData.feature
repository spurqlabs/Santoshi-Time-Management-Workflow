Feature: My Timesheet Management in OrangeHRM

Background:
  Given user is on OrangeHRM login page
  When user enter username
  When user enter password
  When user click on Login button
  Then the user is logged in to the application

@smoke
Scenario: TC001 - Login to application
  Then the user is logged in to the application

@regression
Scenario: TC002 - Add Timesheet details
  Given user is on Dashboard page
  When user navigates to Time -> Timesheets
  Then ViewEmployeeTimesheet page is displayed
  When User click on Timesheet option
  Then Timesheet dropdown list is displayed
  When user select My Timesheets option from the Timesheet dropdown
  Then ViewMyTimesheet page is displayed
  When User click on Edit button
  Then EditTimesheet page is displayed
  When User click on Cancel button
  Then My Timesheet page is displayed

@sanity
Scenario: TC003 - Add Contact details
  Given user is on dashbrd page
  When user click on My Info tab
  Then viewPersonalDetails page is displayed with all personal details of the user
  When user click on Contact Details subtab
  Then ContactDetails page is displayed
  When user enter steet1
  When user enter city
  When user enter state
  When user enter zip code
  When user enter country
  When user enter mobile
  When user enter work phone
  When user enter work email
  When user click on Save button
  Then Contact Details is saved successfully
