package Test_Runner;
import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;
import org.testng.annotations.DataProvider;





@CucumberOptions(
        features = "src/test/resources/features",
        glue = {"step_definition"}, tags = "@smoke or @regression or @sanity",
        plugin = {
                "pretty", 
                "html:target/cucumber-reports.html",
                "io.qameta.allure.cucumber7jvm.AllureCucumber7Jvm"},
        monochrome = true
       
)
public class Testrunner extends AbstractTestNGCucumberTests
{
        // This enables TestNG to run scenarios in parallel. This is the key switch that runs scenarios in parallel

   @Override
   @DataProvider(parallel = true)
   public Object[][] scenarios() {
       return super.scenarios();
   }




    }
