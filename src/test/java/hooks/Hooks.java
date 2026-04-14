package hooks;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.BeforeAll;
import io.cucumber.java.Scenario;
import utils.ConfigReader;
import utils.DBUtils;
import utils.DriverFactory;
import utils.ExcelUtils;
import utils.ScenarioContext;

import java.io.FileOutputStream;
import java.util.Properties;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;

public class Hooks {
	// This is a simplified counter for sequential runs. 
    // In a complex framework, you'd pass the row index from the Feature file or a DataProvider.
    private static int globalCounter = 1;
	
	@BeforeAll
	public static void setAllureEnvironment() {
	    try {
	        Properties props = new Properties();
	        props.setProperty("Environment", "QA-Server-1");
	        props.setProperty("Browser", ConfigReader.getProperty("browser"));
	        props.setProperty("OS", System.getProperty("os.name"));
	        props.setProperty("Java.Version", System.getProperty("java.version"));

	        FileOutputStream fos = new FileOutputStream("target/allure-results/environment.properties");
	        props.store(fos, "Allure Environment Properties");
	        fos.close();
	    } catch (Exception e) {
	        System.out.println("Could not write environment.properties");
	    }
	}
	@Before
    public void setUpRowTracker(Scenario scenario) {
        // Assign the current global count to this specific thread
        ExcelUtils.setCurrentRow(globalCounter);
        
        // Increment for the next scenario
        globalCounter++;
    }

	@Before
	public void setUp() {
		// Ensuring we start fresh with a maximized window
		DriverFactory.getDriver().manage().window().maximize();

		// 2. The magic line: Clear all browser cookies
		DriverFactory.getDriver().manage().deleteAllCookies();
	}

	@After
	public void tearDown(Scenario scenario) {
		// Logic to capture a screenshot only if the scenario fails
		if (scenario.isFailed()) {
			final byte[] screenshot = ((TakesScreenshot) DriverFactory.getDriver()).getScreenshotAs(OutputType.BYTES);
			// This attaches the screenshot directly to your Cucumber report
			scenario.attach(screenshot, "image/png", "screenshot_on_failure");
		}

		// Always close the driver to avoid memory leaks
		DriverFactory.closeDriver();
	}
	
	@Before("@db") // Only connects for scenarios tagged with @db
    public void setUpDatabase() {
        DBUtils.createConnection();
    }

    @After("@db")
    public void tearDownDatabase() {
//        // CLEAN DATA: Remove the test record created during the scenario
//        // You can use a variable saved during the test run Query is dummy, replace with actual logic to get the test record ID
//        String testOrderId = (String) ScenarioContext.Context.ORDER_ID; 
//        
//        if (testOrderId != null) {
//            DBUtils.executeUpdate("DELETE FROM orders WHERE order_id = ?", testOrderId);
//            System.out.println("Database cleaned for Order ID: " + testOrderId);
//        }
//        
        DBUtils.destroy();
    }
}