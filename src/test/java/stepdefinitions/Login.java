package stepdefinitions;

import org.junit.Assert;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import pages.LoginPage;
import utils.ConfigReader;
import utils.DriverFactory;

public class Login {
	// Cucumber will use the default hidden constructor
    public Login() {
        // Must be empty or handle its own initialization
    }
	
	LoginPage loginPage = new LoginPage();
	
	@Given("the user is on the login page")
	public void the_user_is_on_the_login_page() {
	    DriverFactory.getDriver().get(ConfigReader.getProperty("url"));
	    loginPage.verifyOnLoginPage();
	    System.out.println("User in on the Login Page");
	}

	@When("the user enters a valid username and password and Clicks on Submit")
	public void the_user_enters_a_valid_username_and_password() {
		String username = ConfigReader.getProperty("valid_user");
		String password = ConfigReader.getProperty("valid_password");
	    loginPage.login(username, password);
	}


	@Then("the user should see the dashboard page title {string}")
	public void the_user_should_see_the_dashboard_page_title(String expectedTitle) {
	    String actualTitle = DriverFactory.getDriver().getTitle();
	    Assert.assertEquals(expectedTitle, actualTitle);
	    System.out.println("Login with valid crendential test case passed successfully");
	}

	@When("the user enters {string} and {string}")
	public void the_user_enters_and(String username, String password) {
	    loginPage.login(username, password);
	}

	@Then("the user should see an error message {string}")
	public void the_user_should_see_an_error_message(String expectedError) {
	    String actualError = loginPage.getErrorMessage();
	    Assert.assertEquals(expectedError, actualError);
	    System.out.println("Failed Login with invalid crendential test case passed successfully");
	}


}
