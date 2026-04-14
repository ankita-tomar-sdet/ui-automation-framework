package utils;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;
import java.util.NoSuchElementException;
import java.util.function.Function;

public class BrowserWaits {

	/**
	 * Standard Explicit Wait for Visibility
	 * @param element The WebElement to wait for
	 * @param seconds Timeout duration
	 */
	public static WebElement waitForVisibility(WebElement element, int seconds) {
		WebDriverWait wait = new WebDriverWait(DriverFactory.getDriver(), Duration.ofSeconds(seconds));
		return wait.until(ExpectedConditions.visibilityOf(element));
	}

	/**
	 * Standard Explicit Wait for Clickability
	 */
	public static WebElement waitForClickability(WebElement element, int seconds) {
		WebDriverWait wait = new WebDriverWait(DriverFactory.getDriver(), Duration.ofSeconds(seconds));
		return wait.until(ExpectedConditions.elementToBeClickable(element));
	}

	/**
	 * Static Wait (The "Last Resort")
	 * Only use this if Explicit waits fail (e.g., waiting for an animation)
	 */
	public static void sleep(int seconds) {
		try {
			Thread.sleep(seconds * 1000L);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Retries a click if the element becomes stale.
	 */
	public static void clickWithRetry(WebElement element) {
		int attempts = 0;
		while (attempts < 3) {
			try {
				element.click();
				break; // Success! Exit the loop.
			} catch (StaleElementReferenceException e) {
				// Element is stale, refinding logic happens here
				System.out.println("Attempt " + (attempts + 1) + ": Element is stale, retrying...");
			}
			attempts++;
		}
	}


	/**
	 *	When you use @FindBy, Selenium creates a "Proxy" for the element. It doesn't actually look for the element until you call a method like .click(). 
	 *	If you encounter frequent stale elements, you can wrap your interaction in a logic that re-initializes the Page Object.
		Better Solution in BrowserUtils:
		Instead of passing the WebElement, pass a "Wait" condition that forces Selenium to look for the element fresh.
		Note: ExpectedConditions.refreshed is specifically designed to handle Stale Elements by waiting for them to be redrawn. 
	 *
	 */
	public static void clickWithWait(By locator) {
		new WebDriverWait(DriverFactory.getDriver(), Duration.ofSeconds(10))
		.until(ExpectedConditions.refreshed(
				ExpectedConditions.elementToBeClickable(locator)
				)).click();
	}

	// In BrowserUtils.java
	public static void verifyPageLoaded(WebElement element, String expectedTitle) {
		WebDriverWait wait = new WebDriverWait(DriverFactory.getDriver(), Duration.ofSeconds(10));
		try {
			wait.until(ExpectedConditions.visibilityOf(element));
			if(!DriverFactory.getDriver().getTitle().equals(expectedTitle)) {
				throw new RuntimeException("Title mismatch!");
			}
		} catch (Exception e) {
			throw new RuntimeException("Page failed to load correctly: " + e.getMessage());
		}
	}
	
	/*
	 * Implementing Fluent Waits is the primary way to transition from a "flaky" framework to a "resilient" one. 
	 * As a QE, you want to move away from static Thread.sleep() (which wastes time) or simple WebDriverWait (which often crashes on minor DOM shifts).

		Fluent Wait is a "Self-Healing" mechanism because it doesn't just wait for time; 
		it actively ignores specific exceptions (like NoSuchElementException or StaleElementReferenceException) while polling the DOM at a high frequency.
	 */
	public static WebElement waitForElementSmartly(WebDriver driver, WebElement loginBtn) {
        // Configure the Fluent Wait
        Wait<WebDriver> wait = new FluentWait<>(driver)
                .withTimeout(Duration.ofSeconds(30))          // Total wait time
                .pollingEvery(Duration.ofMillis(500))         // Check every 0.5 seconds
                .ignoring(NoSuchElementException.class)      // Self-healing part 1
                .ignoring(StaleElementReferenceException.class) // Self-healing part 2
                .withMessage("Timed out: Element not found or stable after 30 seconds");

        // The "Healing" logic: Keep trying until the element is actually interactable
        return wait.until(new Function<WebDriver, WebElement>() {
            public WebElement apply(WebDriver driver) {
                WebElement element = driver.findElement((By) loginBtn);
                if (element.isDisplayed() && element.isEnabled()) {
                    return element;
                }
                return null;
            }
        });
    }
}
