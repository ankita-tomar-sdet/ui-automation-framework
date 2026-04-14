package utils;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;

public abstract class BasePage {

	public BasePage() {
		/* * This is the magic line. It initializes the web elements 
		 * defined with @FindBy annotations in the child classes.
		 */
		PageFactory.initElements(DriverFactory.getDriver(), this);
	}

	// Generic method to verify the page title
	public boolean verifyPageTitle(String expectedTitle) {
		return DriverFactory.getDriver().getTitle().equals(expectedTitle);
	}

	// Generic method to verify if a specific header element is displayed
	public boolean isPageLoaded(WebElement element) {
		return BrowserWaits.waitForVisibility(element, 10).isDisplayed();
	}
	
}
