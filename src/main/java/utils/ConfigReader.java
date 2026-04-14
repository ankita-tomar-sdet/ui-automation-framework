package utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigReader {
	
	private static Properties properties;
	
	static {
		properties = new Properties();
		boolean loaded = false;
		// First attempt: load from classpath (recommended when running from IDE, JAR, or Maven)
		try (InputStream input = ConfigReader.class.getClassLoader().getResourceAsStream("config.properties")) {
			if (input != null) {
				properties.load(input);
				loaded = true; // successfully loaded from classpath
			}
		} catch (IOException e) {
			e.printStackTrace();
			// continue to fallback attempts
		}
		
		// Fallback: attempt to load from the working directory (project root) - useful when running tests from IDE
		if (!loaded) {
			String path = System.getProperty("user.dir") + "/config.properties";
			try (FileInputStream input = new FileInputStream(path)) {
				properties.load(input);
				loaded = true; // successfully loaded from file system
			} catch (IOException e) {
				// continue to final failure case below
				e.printStackTrace();
			}
			
			if (!loaded) {
				throw new RuntimeException("Failed to load the config properties file. Tried:\n" +
						" - classpath resource 'config.properties'\n" +
						" - file at: " + path + "\nMake sure 'config.properties' exists in one of these locations.");
			}
		}
	}
	
	public static String getProperty(String key) {
		return properties.getProperty(key);
	}

}