package com.upgrade.qa.test.util;

import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class SeleniumUtil {
	private static long defaultTimeout = 2;

	
	public static void waitForElement(WebDriver driver, By by, long timeout) {
		timeout = timeout>0? timeout: SeleniumUtil.defaultTimeout;
		WebElement el = new WebDriverWait(driver, timeout).until(
				ExpectedConditions.presenceOfElementLocated(by));
	}
	
	public static WebElement findElement(WebDriver driver, By by, long timeout) {
		WebElement el = new WebDriverWait(driver, timeout).until(
				ExpectedConditions.presenceOfElementLocated(by));
		return el;
	}
	
	public static void waitAndClick(WebDriver driver, By by, long timeout) {
		WebElement el = new WebDriverWait(driver, timeout).until(
				ExpectedConditions.elementToBeClickable(by));
		el.click();
	}
	
	public static WebElement getInputField(WebDriver driver, String text, String searchString, String replacement) {
		By by = By.xpath(
				StringUtils.replace(text, searchString, replacement));
		return SeleniumUtil.findElement(driver, by, 0);
	}
}
