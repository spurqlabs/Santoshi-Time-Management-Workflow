# Assignment One - Playwright Java Automation Framework

A comprehensive **BDD (Behavior-Driven Development)** test automation framework built with **Playwright**, **Cucumber**, **TestNG**, and **Java**. This framework is designed for testing the OrangeHRM application with detailed reporting and screenshot capture capabilities.

## 🎯 Project Overview

This framework provides:
- **BDD Approach**: Cucumber feature files for readable test scenarios
- **Cross-browser Support**: Chromium browser automation with Playwright
- **Comprehensive Reporting**: Allure and Cucumber HTML reports with embedded screenshots
- **Flexible Configuration**: JSON-based configuration and test data management
- **Modular Architecture**: Page Object Model (POM) for maintainable test code
- **Dynamic Locators**: Externalized locators in JSON files for easy maintenance

---

## 📋 Prerequisites

Before setting up the framework, ensure you have:

- **Java**: JDK 11 or higher
- **Maven**: 3.6.0 or higher
- **Git**: For version control
- **Playwright**: Automatically installed via Maven dependencies

### Verify Installation

```bash
# Check Java version
java -version

# Check Maven version
mvn -version

# Check Git version
git --version
```

---

## 📁 Project Structure

```
Assignment_One_Playwright_Java/
├── src/
│   └── test/
│       ├── java/
│       │   ├── pages/                    # Page Object Model classes
│       │   │   ├── LoginPage.java        # Login page interactions
│       │   │   └── MyTimesheetsPage.java # Timesheet page interactions
│       │   ├── step_definition/          # Cucumber step definitions
│       │   │   ├── Hooks.java            # Setup and teardown logic
│       │   │   └── TimesheetDataStepDef.java  # Step implementations
│       │   ├── Test_Runner/              # Test execution configuration
│       │   │   └── Testrunner.java       # Cucumber runner with CucumberOptions
│       │   └── utils/                    # Utility classes
│       │       ├── ConfigReader.java     # Configuration management
│       │       ├── LocatorReader.java    # Locator management
│       │       ├── TestDataReader.java   # Test data management
│       │       ├── ScreenshotUtils.java  # Screenshot capture
│       │       ├── JsonUtils.java        # JSON parsing utilities
│       │       └── ScreenshotUtils.java  # Screenshot utilities
│       └── resources/
│           ├── config/
│           │   └── config.json           # Application configuration
│           ├── features/                 # Cucumber feature files
│           │   └── timesheetData.feature # Timesheet test scenarios
│           ├── locators/                 # Locator repositories
│           │   └── testdata_locator.json # Element locators
│           └── Testdata/                 # Test data
│               └── timesheetData.json    # Test data in JSON format
├── target/
│   ├── cucumber-reports.html             # Cucumber HTML report
│   ├── allure-results/                   # Allure report data
│   ├── surefire-reports/                 # TestNG reports
│   └── test-classes/                     # Compiled test classes
├── pom.xml                               # Maven configuration
└── README.md                             # This file
```

---

## 🚀 Getting Started

### Step 1: Clone the Repository

```bash
git clone <repository-url>
cd Assignment_One_Playwright_Java
```

### Step 2: Install Dependencies

```bash
mvn clean install
```

This command will:
- Download all Maven dependencies
- Install Playwright browsers
- Compile the project

### Step 3: Configure the Application

Edit `src/test/resources/config/config.json`:

```json
{
  "browser": "chromium",
  "headless": false,
  "slowMo": 500,
  "appUrl": "https://opensource-demo.orangehrm.com/web/index.php/auth/login"
}
```

**Configuration Parameters:**
- `browser`: Browser type (chromium, firefox, webkit)
- `headless`: Run in headless mode (true/false)
- `slowMo`: Slow down actions by milliseconds
- `appUrl`: Base URL of the application

### Step 4: Add Test Data

Edit `src/test/resources/Testdata/timesheetData.json`:

```json
{
  "register": {
    "timesheet": {
      "project": "Project Name",
      "activity": "Administration",
      "hours": {
        "2nd Feb": "8",
        "3rd Feb": "8",
        "4th Feb": "8"
      }
    }
  }
}
```

---

## ▶️ Running Tests

### Run All Tests

```bash
mvn test
```

### Run Specific Test Tags

```bash
mvn test -Dgroups=@login,@addtimesheetdata
```

### Run in Headless Mode

```bash
mvn test -DskipTests=false
```

---

## 📊 Generating Reports

### Generate Cucumber HTML Report

The Cucumber HTML report is automatically generated at:
```
target/cucumber-reports.html
```

### Generate Allure Report

After test execution, generate the Allure report:

```bash
# Generate and open Allure report
allure serve target/allure-results

# Or generate HTML report
allure generate target/allure-results -o target/allure-report
allure open target/allure-report
```

---

## 📝 Writing Test Scenarios

### Create a Feature File

Create `src/test/resources/features/myTest.feature`:

```gherkin
@mytest
Feature: Test Feature Description

  Scenario: Test Scenario Description
    Given user is on OrangeHRM login page
    When user enter username
    And user enter password
    And user click on Login button
    Then user should be logged in the application successfully
```

### Add Step Definitions

Add methods in `src/test/java/step_definition/TimesheetDataStepDef.java`:

```java
@When("user enter username")
public void user_enter_username() {
    loginPage.enterUsername();
}
```

---

## 🔧 Key Framework Features

### 1. Page Object Model (POM)

Page classes encapsulate element locators and interactions:

```java
public class LoginPage {
    private final Page page;
    
    public LoginPage(Page page) {
        this.page = page;
    }
    
    public void enterUsername() {
        // Implementation
    }
}
```

### 2. Externalized Locators

Locators are stored in JSON files and loaded dynamically:

```json
{
  "timesheet": {
    "menuTime": {
      "selector": "//a[contains(text(), 'Time')]"
    }
  }
}
```

Load locators in code:
```java
String locator = LocatorReader.getLocator("timesheet", "menuTime", "selector");
```

### 3. Screenshot Capture

Automatic screenshots are captured:
- After each step (via `@AfterStep`)
- On test failure (via `@After`)
- Manually using `ScreenshotUtils.captureScreenshot(page, scenario, stepName)`

### 4. Timeout Management

Default timeout set to 60 seconds in `Hooks.java`:

```java
page.setDefaultTimeout(60000);
```

### 5. Test Data Management

Load test data from JSON files:

```java
TestDataReader testData = new TestDataReader("Testdata/timesheetData.json");
String project = testData.getData("register.timesheet.project");
```

---

## 🐛 Troubleshooting

### Issue: Timeout Error (Timeout 30000ms exceeded)

**Solution**: The timeout was increased to 60 seconds in recent updates. If still facing issues:

1. Check if the element selector is correct in JSON
2. Add explicit wait in step definition:
   ```java
   page.waitForTimeout(500);
   ```
3. Verify element is visible before interaction

### Issue: Screenshots Not Appearing in Report

**Solution**: Ensure `@AfterStep` is triggered:

1. Verify `Hooks.java` has the `@AfterStep` method
2. Check that Scenario object is passed to `captureScreenshot()`
3. Rebuild and run tests: `mvn clean test`

### Issue: Locators Not Found

**Solution**:

1. Verify JSON locator file path is correct
2. Check JSON syntax is valid (use JSON validator)
3. Ensure key path matches the getter call
4. Update locators in `testdata_locator.json`

### Issue: Maven Build Fails

**Solution**:

```bash
# Clean and rebuild
mvn clean install -U

# Skip tests and build
mvn clean install -DskipTests

# Enable debug logging
mvn test -X
```

---

## 📚 Dependencies

### Core Dependencies

| Dependency | Version | Purpose |
|-----------|---------|---------|
| Playwright | 1.40.0 | Browser automation |
| Cucumber Java | 7.14.0 | BDD framework |
| Cucumber TestNG | 7.14.0 | Integration with TestNG |
| TestNG | 7.7.1 | Test execution framework |
| Allure Cucumber7 | 2.25.0 | Allure reporting |
| JSON Simple | 1.1.1 | JSON parsing |

---

## 🎓 Best Practices

1. **Use meaningful step descriptions** - Make scenarios readable to non-technical users
2. **Keep page objects focused** - One page object per application page
3. **Externalize all locators** - Use JSON files for all selectors
4. **Use explicit waits** - Avoid hardcoded delays
5. **Log important actions** - Help with debugging and report clarity
6. **Capture screenshots strategically** - After each step, especially on failures
7. **Maintain test data separately** - Use JSON files for test data
8. **Follow naming conventions** - Use clear names for methods and variables

---

## 📖 Common Commands

```bash
# Clean build
mvn clean

# Compile tests
mvn test-compile

# Run tests
mvn test

# Skip tests and build
mvn install -DskipTests

# Run specific test class
mvn test -Dtest=Testrunner

# View Allure report
allure serve target/allure-results

# Check for compilation errors
mvn verify

# Generate site documentation
mvn site
```

---

## 🤝 Contributing

1. Create a new branch for your feature
2. Write tests following BDD principles
3. Update locators in JSON files
4. Ensure all tests pass
5. Generate reports and verify
6. Submit a pull request

---

## 📞 Support & Issues

For issues or questions:

1. Check the **Troubleshooting** section above
2. Review existing test scenarios for examples
3. Check Cucumber and Playwright documentation
4. Enable debug logging: `mvn test -X`

---

## 📄 License

This project is part of an assignment and follows the specified license.

---

## 🎯 Next Steps

1. ✅ Set up the project following the Getting Started section
2. ✅ Update configuration in `config.json`
3. ✅ Run a sample test: `mvn test`
4. ✅ View reports in `target/cucumber-reports.html`
5. ✅ Write your own test scenarios

---

## 📝 Changelog

### Version 1.0
- Initial project setup
- Cucumber BDD integration
- Playwright browser automation
- Allure and Cucumber HTML reporting
- Screenshot capture functionality
- JSON-based configuration and test data

---

**Happy Testing! 🚀**
