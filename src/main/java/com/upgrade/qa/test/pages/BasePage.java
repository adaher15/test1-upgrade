package com.upgrade.qa.test.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.upgrade.qa.test.util.SeleniumUtil;

public class BasePage {
	

	protected String baseUrl;
	protected WebDriver driver;
	
	public BasePage(WebDriver driver, String baseUrl) {
		this.driver = driver;
		this.baseUrl = baseUrl;
	}

	@FindBy(xpath="//div[@class='header-nav']")
	WebElement menu;
	
	@FindBy(xpath="//input[@name='username']")
	WebElement username;
	
	@FindBy(xpath="//input[@name='password']")
	WebElement password;
	
	String signOutPath = "//a[contains(text(), 'Sign Out')]";
	
	public void singOut() {
		waitForMenu(16);
		menu.click();
		By by = By.xpath(signOutPath);
		SeleniumUtil.waitAndClick(driver, by , 2);
	}
	
	public void waitForOfferPage(long time) {
		SeleniumUtil.waitForElement(driver, By.xpath("//*[@data-auto='userLoanAmount']") , time);
		
	}
	
	public void waitForMenu(long time) {
		SeleniumUtil.waitForElement(driver, By.xpath("//div[@class='header-nav']") , time);
	}
}
