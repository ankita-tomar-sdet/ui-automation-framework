package pages;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import io.qameta.allure.Step;
import utils.BasePage;
import utils.BrowserWaits;
import utils.DriverFactory;

public class LoginPage extends BasePage {

	@FindBy(xpath = "//input[@data-qa = \"signup-name\"]")
	private WebElement newUserName;

	@FindBy(xpath = "//input[@data-qa = \"signup-email\"]")
	private WebElement newUserEmail;

	@FindBy(xpath = "//button[@data-qa = \"signup-button\"]")
	private WebElement signupBtn;

	@FindBy(xpath = "//input[@data-qa = \"login-email\"]")
	private WebElement existingUserEmail;

	@FindBy(xpath = "//input[@data-qa = \"login-password\"]")
	private WebElement existingUserPassword;

	@FindBy(xpath = "//button[@data-qa = \"login-button\"]")
	private WebElement loginBtn;

	@FindBy(xpath = "//p[text() = 'Your email or password is incorrect!']")
	private WebElement errorMsg;

	@FindBy(xpath = "//img[contains(@src,\"logo.png\")]")
	private WebElement loginLogo;
	
	@Step("Verifying that we are on the Login Page by checking the unique logo and page title")
	public void verifyOnLoginPage() {

		String expectedTitle = "Automation Exercise - Signup / Login"; 
		try {
			// 1. Wait logic: Give the page 10 seconds to show the unique 'loginLogo'
			BrowserWaits.waitForVisibility(loginLogo, 10);

			// 2. Logic to check if the Title is also correct
			if (!DriverFactory.getDriver().getTitle().equals(expectedTitle)) {
				throw new Exception("Title Mismatch");
			}

		} catch (Exception e) {
			// 3. Handle the failure with a detailed error message
			throw new RuntimeException(
					"CRITICAL ERROR: Not on the Login Page!" +
							"\nExpected Title: " + expectedTitle +
							"\nActual Title: " + DriverFactory.getDriver().getTitle() +
							"\nActual URL: " + DriverFactory.getDriver().getCurrentUrl() +
							"\nReason: " + e.getMessage()
					);
		}
	}
	
	@Step("Verifying that we are on the Sign Up Page by checking the unique logo and page title")
	public void verifyOnSignUpPage() {

		String expectedTitle = "Automation Exercise - Signup"; 
		try {
			// 1. Wait logic: Give the page 10 seconds to show the unique 'loginLogo'
			BrowserWaits.waitForVisibility(loginLogo, 10);

			// 2. Logic to check if the Title is also correct
			if (!DriverFactory.getDriver().getTitle().equals(expectedTitle)) {
				throw new Exception("Title Mismatch");
			}

		} catch (Exception e) {
			// 3. Handle the failure with a detailed error message
			throw new RuntimeException(
					"CRITICAL ERROR: Not on the Login Page!" +
							"\nExpected Title: " + expectedTitle +
							"\nActual Title: " + DriverFactory.getDriver().getTitle() +
							"\nActual URL: " + DriverFactory.getDriver().getCurrentUrl() +
							"\nReason: " + e.getMessage()
					);
		}
	}
	
	@Step("Logging in with valid username and password")
	public void login(String username, String password) {
		verifyOnLoginPage();
		existingUserEmail.sendKeys(username);
		existingUserPassword.sendKeys(password);
		BrowserWaits.waitForElementSmartly(DriverFactory.getDriver(), loginBtn).click();
	}

	@Step("Verifying that the error message is displayed for invalid login attempt")
	public String getErrorMessage() {
		return BrowserWaits.waitForVisibility(errorMsg, 20).getText();
	}
	
	@Step("Signing up with new username and email")
	public void signUp(String username, String userEmail) {
		verifyOnLoginPage();
		newUserName.sendKeys(username);
		newUserEmail.sendKeys(userEmail);
		signupBtn.submit();
		verifyOnSignUpPage();

	}







}
