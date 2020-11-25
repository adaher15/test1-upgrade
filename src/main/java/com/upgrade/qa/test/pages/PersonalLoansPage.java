package com.upgrade.qa.test.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.Select;

import com.upgrade.qa.test.util.SeleniumUtil;



public class PersonalLoansPage {
	
	private final String uri = "/funnel/nonDMFunnel";
	private String baseUrl;
	private WebDriver driver;
	
	public enum LOAN_PURPOSE {
		CREDIT_CARD,
		DEBT_CONSOLIDATION,
		SMALL_BUSINESS,
		HOME_IMPROVEMENT,
		LARGE_PURCHASE,
		OTHER
	}
	
	String formFooter = "//div[contains(text(), 'Checking your rate won’t impact your credit score.')]";
	
	@FindBy(xpath="//button[@data-auto='CheckYourRate']")
	public WebElement checkYourRate;
	
	@FindBy(xpath="//input[@name='desiredAmount']")
	public WebElement desiredAmount;
	
	@FindBy(xpath="//select[@data-auto='dropLoanPurpose']")
	public WebElement loanPurpose;
	
	public PersonalLoansPage(WebDriver driver, String baseUrl) {
		this.driver = driver;
		this.baseUrl = baseUrl;
	}
	
	public String getUri() {
		return uri;
	}
	
	
	public void loadPage() {
		driver.get(this.baseUrl+this.uri);
		By by = By.xpath(formFooter);
		SeleniumUtil.waitForElement(driver, by , 5);
	}
	
	/**
	 * 
	 * @param amount
	 * @param purpose
	 */
	public void fillPersonalLoan(String amount, LOAN_PURPOSE purpose) {
		Select selectPurpose = new Select(loanPurpose); 
		desiredAmount.sendKeys(amount);
		selectPurpose.selectByValue(purpose.toString());
		checkYourRate.click();
		
	}
	
	
}
