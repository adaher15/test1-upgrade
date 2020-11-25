package com.upgrade.qa.test.pages;

import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.upgrade.qa.test.actors.application.Applicant;
import com.upgrade.qa.test.util.SeleniumUtil;

public class SignUpPage extends BasePage{
	
	private final String uri = "/funnel/personal-information-1/SMALL_BUSINESS/__ID__?step=__STEP__";
	
	/**
	 * Constructor
	 * @param driver
	 * @param baseUrl
	 */
	public SignUpPage(WebDriver driver, String baseUrl) {
		super(driver, baseUrl);
	}
	
	
	// List of repeated input fileds
	HashMap<String, String> inputKeys = new HashMap<String, String>() {{
		   put("firstName", "borrowerFirstName");
		   put("lastName", "borrowerLastName");
		   put("street", "borrowerStreet");
		   put("city", "borrowerCity");
		   put("state", "borrowerState");
		   put("zipCode", "borrowerZipCode");
		   put("birth", "borrowerDateOfBirth");
		   
		   put("coFirstName", "coBorrowerFirstName");
		   put("coLastName", "coBorrowerLastName");
		   put("coStreet", "coBorrowerStreet");
		   put("coCity", "coBorrowerCity");
		   put("coState", "coBorrowerState");
		   put("coZipCode", "coBorrowerZipCode");
		   put("coBirth", "coBorrowerDateOfBirth");
		   // Income fileds
		   put("income", "borrowerIncome");
		   put("additionalIncome", "borrowerAdditionalIncome");
		   put("coIncome", "coBorrowerIncome");

		}};
	
	// generic path for prsonalInfo forrms
	private String inputPath = "//input[@data-auto='__NAME__']";
	
	public enum STEPS{
		CONTACT,
		INCOME,
		LOGIN
	}
	
	// Webelements
	String headerPath = "//h1[contains(text(), 'Let\'s get started with some basic information')]";
	String continueBtnPath = "//button[@data-auto='continuePersonalInfo']";
	String submitBtnPath = "//button[@data-auto='submitPersonalInfo']";

	@FindBy(xpath="//h2[contains(text(), 'Co-applicant')]")
	List<WebElement>  coAppllicant;
	
	@FindBy(xpath="//input[@name='jointApplicationToggle']")
	List<WebElement> jointApplication;
	
	
	@FindBy(xpath="//label[./input[@type='checkbox']]")
	WebElement sameAddress;
	
	
	@FindBy(xpath="//input[@name='agreements']")
	WebElement agreements;
	
	
	
	
	public String getUri() {
		return this.uri;
	}
	
	/**
	 * Click on button continue
	 */
	public void clickContinue() {
		String currentUrl = driver.getCurrentUrl();
		SeleniumUtil.waitAndClick(driver, By.xpath(continueBtnPath), 5);
		if(StringUtils.contains(currentUrl, "step=contact")) {
			SeleniumUtil.waitForElement(driver, By.xpath("//h1[contains(text(), 'How much money do you make in a year?')]"), 2);
		}
	}
	
	
	public void clickSubmit() {
		SeleniumUtil.waitAndClick(driver, By.xpath(submitBtnPath), 5);
		// Wait for page to load, it's sometimes slow
		By by = By.xpath("//div[contains(text(), 'Your Loan Amount')]");
		SeleniumUtil.waitForElement(driver, by, 12);
		waitForMenu(20);
		waitForOfferPage(25);
	}
	
	// The following section is about "step=contact"
	/** 
	 * Position is zero based, 0=Individual and 1=Joint Application
	 * @param position
	 */
	public void toggleJointApplication(int position) {
		jointApplication.get(position).click();
	}
	
	/**
	 * 
	 * @param applicant
	 * @param selectCoApplicant
	 * @param coApplicant
	 */
	public void fillPersonalInfo(Applicant applicant) {
		// Fill information about applicant
		SeleniumUtil.getInputField(driver, inputPath, "__NAME__", inputKeys.get("firstName")).sendKeys(applicant.getFirstName());
		SeleniumUtil.getInputField(driver, inputPath, "__NAME__", inputKeys.get("lastName")).sendKeys(applicant.getLastName());
		WebElement streetInput = SeleniumUtil.getInputField(driver, inputPath, "__NAME__", inputKeys.get("street"));
		streetInput.sendKeys(applicant.getStreet());
		SeleniumUtil.getInputField(driver, inputPath, "__NAME__", inputKeys.get("city")).sendKeys(applicant.getCity());
		SeleniumUtil.getInputField(driver, inputPath, "__NAME__", inputKeys.get("state")).sendKeys(applicant.getState());
		SeleniumUtil.getInputField(driver, inputPath, "__NAME__", inputKeys.get("zipCode")).sendKeys(applicant.getZipCode());
		SeleniumUtil.getInputField(driver, inputPath, "__NAME__", inputKeys.get("birth")).sendKeys(applicant.getDateOfBirth());
		
		// Make sure address is not expanded
//		if(BooleanUtils.toBoolean(streetInput.getAttribute("aria-expanded"))) {
		try {
			TimeUnit.SECONDS.sleep(1);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
			clickSection();
//		}
	}
	
	
	public void clickSection() {
		driver.findElement(By.xpath("//div[contains(@class,'row-lg')]")).click();
	}
	
	
	/**
	 * 
	 * @param coApplicant
	 * @param useSameAddress
	 */
	public void fillCoApplicant(Applicant coApplicant, boolean useSameAddress) {
		toggleJointApplication(1);
		SeleniumUtil.getInputField(driver, inputPath, "__NAME__", inputKeys.get("coFirstName")).sendKeys(coApplicant.getFirstName());
		SeleniumUtil.getInputField(driver, inputPath, "__NAME__", inputKeys.get("coLastName")).sendKeys(coApplicant.getLastName());
		SeleniumUtil.getInputField(driver, inputPath, "__NAME__", inputKeys.get("coBirth")).sendKeys(coApplicant.getDateOfBirth());
		
		if(useSameAddress) {
			// Make sure same address is checked
			if (! Boolean.getBoolean(sameAddress.getAttribute("data-checked"))){
				sameAddress.click();
			}
		}else {
			SeleniumUtil.getInputField(driver, inputPath, "__NAME__", inputKeys.get("coStreet")).sendKeys(coApplicant.getStreet());
			SeleniumUtil.getInputField(driver, inputPath, "__NAME__", inputKeys.get("coCity")).sendKeys(coApplicant.getCity());
			SeleniumUtil.getInputField(driver, inputPath, "__NAME__", inputKeys.get("coState")).sendKeys(coApplicant.getState());
			SeleniumUtil.getInputField(driver, inputPath, "__NAME__", inputKeys.get("coZipCode")).sendKeys(coApplicant.getZipCode());
		}
	}
	
	
	/**
	 * Verify if header is displayed
	 * @return
	 */
	public boolean isCoApplicantDisplayed() {
		return coAppllicant != null && coAppllicant.size() == 2;
	}
	
	
	// The following section is about step=income
	
	public void fillBorowerIncome(String incomeVal, String additionalIncomeVal) {
		SeleniumUtil.getInputField(driver, inputPath, "__NAME__", inputKeys.get("income")).sendKeys(incomeVal);
		SeleniumUtil.getInputField(driver, inputPath, "__NAME__", inputKeys.get("additionalIncome")).sendKeys(additionalIncomeVal);
		clickSection();
	}
	
	public void fillSignupForm(String email, String passwordVal) {
		username.sendKeys(email);
		password.sendKeys(passwordVal);
		// Get sibling div
		agreements.findElement(By.xpath("following-sibling::div")).click();
	}
	

}
