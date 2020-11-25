package com.upgrade.qa.test.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.upgrade.qa.test.util.SeleniumUtil;

public class LoginPage extends BasePage{
	
	/**
	 * Constructor
	 * 
	 * @param driver
	 * @param baseUrl
	 */
	public LoginPage(WebDriver driver, String baseUrl) {
		super(driver, baseUrl);
	}
	
	
	private final String uri = "/portal/login";
	
	
	@FindBy(xpath = "//button[@data-auto='login']")
	WebElement loginBtn;
	
	public void openPage() {
		driver.get(baseUrl+uri);
		menu.isDisplayed();
	}
	
	
	
	public void login(String username, String pwd) {
		this.username.sendKeys(username);
		this.password.sendKeys(pwd);
		loginBtn.click();
		waitForOfferPage(15);
	}

}
