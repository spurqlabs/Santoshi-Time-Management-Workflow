package Test_Runner;
import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;
import org.testng.annotations.DataProvider;





@CucumberOptions(
        features = "src/test/resources/features/userData.feature",
        glue = {"step_definition"}, tags = "@smoke4 or @regression3 or @regression4 or @negative",
        plugin = {
                "pretty", 
                "html:target/cucumber-reports.html",
                "io.qameta.allure.cucumber7jvm.AllureCucumber7Jvm"},
        monochrome = true
       
)
public class User_runner extends AbstractTestNGCucumberTests
{
        @Override
   @DataProvider(parallel = true)
   public Object[][] scenarios() {
       return super.scenarios();
   }

   }


        
   




    
