package utils;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

import io.github.bonigarcia.wdm.WebDriverManager;


public class DriverFactory {

	// Private constructor to prevent instantiation
	private DriverFactory() {}

	private static ThreadLocal<WebDriver> driverPool = new ThreadLocal<>();

	public static WebDriver getDriver() {
		// FIX: Check the thread-local's contained value, not the ThreadLocal object itself.
		if (driverPool.get() == null) {
			// Read browser type from config.properties
			String browser = ConfigReader.getProperty("browser").toLowerCase();
			System.out.println("Browser type being read: " + browser);
			
			
			// Factory Pattern: Switch between browsers
			switch (browser) {
			case "chrome":
				WebDriverManager.chromedriver().setup();
				driverPool.set(new ChromeDriver());
				break;
			case "firefox":
				WebDriverManager.firefoxdriver().setup();
				driverPool.set(new FirefoxDriver());
				break;
			case "edge":
				WebDriverManager.edgedriver().setup();
				driverPool.set(new EdgeDriver());
				break;
			default:
				throw new RuntimeException("Browser not supported: " + browser);
			}
		}
		return driverPool.get();
	}

	public static void closeDriver() {
		if (driverPool.get() != null) {
			driverPool.get().quit();
			driverPool.remove();
		}
	}
}