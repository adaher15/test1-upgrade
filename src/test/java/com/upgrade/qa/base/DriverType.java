package com.upgrade.qa.base;

import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

public class DriverType {
	
	
	public static WebDriver getDriver() {
		//Should select driver type between Chrome, FF
		return getChrome();
	}
	
	public static WebDriver getChrome() {
		if (StringUtils.isEmpty(System.getProperty("webdriver.chrome.driver"))) {
			System.setProperty("webdriver.chrome.driver",
					System.getProperty("user.dir") + "/drivers/chromedriver");
		}
		ChromeOptions options = new ChromeOptions();
		options.addArguments("no-default-browser-check");
		WebDriver d = new ChromeDriver(options);
		return d;
	}
	
	public static void getFireFox() {
		//TODO: to be implemented
	}
}
