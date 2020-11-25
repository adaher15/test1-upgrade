package com.upgrade.qa.base;



import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.BeforeTest;

public class BaseTest {

	public static WebDriver driver;
	public final String BASE_URL = "https://www.credify.tech";

	// For the demo purpose we'll use only chrome driver
	@BeforeTest
	public void setupDriver() {
		driver = DriverType.getChrome();
	}

	
	@AfterTest
	public void closeBrowser() {
		this.driver.close();
		this.driver.quit();
	}
	

	
}
