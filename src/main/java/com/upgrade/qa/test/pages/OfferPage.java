package com.upgrade.qa.test.pages;

import java.util.HashMap;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;


public class OfferPage extends BasePage {

	/**
	 * Constructor
	 * 
	 * @param driver
	 * @param baseUrl
	 */
	public OfferPage(WebDriver driver, String baseUrl) {
		super(driver,baseUrl);
	}


	private final String uri = "funnel/offer-page";
	public enum ELEMENTS_KEYS  {
			AMOUNT,
			MONTHLY,
			TERM,
			RATE,
			APR,
			MINPAY
	}
	// List of repeated input fileds
	HashMap<ELEMENTS_KEYS, String> divKeys = new HashMap<ELEMENTS_KEYS, String>() {
		{
			put(ELEMENTS_KEYS.AMOUNT, "//*[@data-auto='userLoanAmount']");
			put(ELEMENTS_KEYS.MONTHLY, "//*[@data-auto='defaultMonthlyPayment']");
			put(ELEMENTS_KEYS.TERM, "//*[@data-auto='defaultLoanTerm']");
			put(ELEMENTS_KEYS.RATE, "//*[@data-auto='defaultLoanInterestRate']");
			put(ELEMENTS_KEYS.APR, "//*[@data-auto='defaultMoreInfoAPR']");
			put(ELEMENTS_KEYS.MINPAY, "//*[@data-auto='directPayMinimumAmount']");

		}
	};

	

	
	public String getElementText(ELEMENTS_KEYS key) {
		WebElement elm = driver.findElement(By.xpath(divKeys.get(key)));
		return elm.getText();
	}
	
	
}
