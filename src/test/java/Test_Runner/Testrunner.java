package Test_Runner;
import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;


@CucumberOptions(
        features = "src/test/resources/features",
        glue = {"step_definition"},plugin = {
                "pretty", 
                "html:target/cucumber-reports.html",
                "io.qameta.allure.cucumber7jvm.AllureCucumber7Jvm"},
        monochrome = true,
        tags = "@login or @addtimesheetdata"
)
public class Testrunner extends AbstractTestNGCucumberTests
{

    }
