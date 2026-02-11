Feature: Recruitment Management in OrangeHRM

Background:
  Given user is on OrangeHRM login page
  When user enter username
  When user enter password
  When user click on Login button
  Then the user is logged in to the application

@smoke1
Scenario: TC001 - Add Candidate details
Given user is on dashboard page
When user click on Recruitment menu
Then user navigate to viewCandidates page
When user click on Add button
Then user navigate to addCandidate page
When user enter First Name
When user enter Last Name
When user select Job Vacancy
When user enter Email
When user enter Contact Number
When user upload Resume
When user click on Savebutton
Then the candidate details is added successfully

@regression1
Scenario: TC002 - View Candidate details and downloaded attached resume
Given user is on home page
When user click on Recruit_menu 
Then user navigate to recruitment page
When user select vacancy from drop down
When user click on search button
Then the candidate details is displayed on the page
When user click on view icon 
Then user navigate to Candidate details page
When user verify the entered details 
Then all the entered details is displayed correctly
When user click on download button for resume
Then the resume is downloaded successfully
When user click on back button from browser 
Then user navigate to Candidates list page
When user click on Logout button from profile menu
Then user is logged out from the application



