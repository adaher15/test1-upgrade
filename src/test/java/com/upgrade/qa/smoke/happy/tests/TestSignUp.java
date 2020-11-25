package com.upgrade.qa.smoke.happy.tests;

import org.testng.annotations.Test;
import org.openqa.selenium.support.PageFactory;

import com.upgrade.qa.base.BaseTest;
import com.upgrade.qa.test.actors.application.Applicant;
import com.upgrade.qa.test.pages.LoginPage;
import com.upgrade.qa.test.pages.OfferPage;
import com.upgrade.qa.test.pages.PersonalLoansPage;
import com.upgrade.qa.test.pages.SignUpPage;
import com.upgrade.qa.test.util.HttpClient;

import java.io.IOException;
import java.util.HashMap;
import java.util.UUID;

import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.ParseException;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.util.EntityUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.testng.Assert;
import org.testng.ITestContext;

public class TestSignUp extends BaseTest {

	PersonalLoansPage loan;
	SignUpPage signup;
	OfferPage offer;
	LoginPage login;
	private static final String API_URL = "https://credapi.credify.tech/api/brportorch/v2";
	JSONParser parser = new JSONParser();
	
	@Test(priority = 1)
	public void setup() {
		Assert.assertNotNull(this.driver);
		Assert.assertTrue(StringUtils.isNotEmpty(this.BASE_URL));
		loan = new PersonalLoansPage(super.driver, this.BASE_URL);
		signup = new SignUpPage(this.driver, this.BASE_URL);
		offer = new OfferPage(this.driver, this.BASE_URL);
		login = new LoginPage(this.driver, this.BASE_URL);

		PageFactory.initElements(this.driver, loan);
		PageFactory.initElements(this.driver, signup);
		PageFactory.initElements(this.driver, offer);
		PageFactory.initElements(this.driver, login);
		loan.loadPage();
	}

	@Test(dependsOnMethods = { "setup" })
	public void testStartLoan() {
		loan.loadPage();
		Assert.assertTrue(loan.checkYourRate.isDisplayed());
		loan.fillPersonalLoan("2000", PersonalLoansPage.LOAN_PURPOSE.HOME_IMPROVEMENT);
		Assert.assertTrue(StringUtils.contains(driver.getCurrentUrl(), "funnel/personal-information-1/"));
		Assert.assertTrue(StringUtils.contains(driver.getCurrentUrl(),
				PersonalLoansPage.LOAN_PURPOSE.HOME_IMPROVEMENT.toString()));

	}

	@Test(dependsOnMethods = { "testStartLoan" })
	public void testIndividualLoan() {
		Assert.assertFalse(signup.isCoApplicantDisplayed());
		int rand = RandomUtils.nextInt(0, 20);
		Applicant applicant = new Applicant("TesterFN"+rand, "Tester1LN"+rand, "123 Melrose Street", "Brooklyn", "NY", "11206",
				"12/14/1990");
		signup.fillPersonalInfo(applicant);
		signup.clickContinue();
		Assert.assertTrue(StringUtils.contains(driver.getCurrentUrl(), "step=income"));
	}

	@Test(dependsOnMethods = { "testIndividualLoan" })
	public void testIncomeForm() {
		signup.fillBorowerIncome(Integer.toString(RandomUtils.nextInt(120000, 250000))
				, Integer.toString(RandomUtils.nextInt(5000, 50000)));
		signup.clickContinue();
		Assert.assertTrue(StringUtils.contains(driver.getCurrentUrl(), "step=login"));

	}
	
	
	@Test(dependsOnMethods = {"testIncomeForm"})
	public void testOffer(ITestContext context){
		String email = "karim"+RandomUtils.nextInt(0, 200)+"@upgrade-challenge.com";
		String password = "UpgradeChalg12#";
		context.setAttribute("username", email);
		context.setAttribute("password", password);
		signup.fillSignupForm(email, password);
		signup.clickSubmit();
		Assert.assertTrue(StringUtils.contains(driver.getCurrentUrl(), "funnel/offer-page"));
		
		String amount = offer.getElementText(OfferPage.ELEMENTS_KEYS.AMOUNT);
		String monthly = offer.getElementText(OfferPage.ELEMENTS_KEYS.MONTHLY);
		String term = offer.getElementText(OfferPage.ELEMENTS_KEYS.TERM);
		String rate = offer.getElementText(OfferPage.ELEMENTS_KEYS.RATE);
		String apr = offer.getElementText(OfferPage.ELEMENTS_KEYS.APR);
		Assert.assertTrue(
				StringUtils.isNotEmpty(amount) && 
				StringUtils.isNotEmpty(monthly) && 
				StringUtils.isNotEmpty(term) && 
				StringUtils.isNotEmpty(rate) && 
				StringUtils.isNotEmpty(apr));
		
		context.setAttribute(OfferPage.ELEMENTS_KEYS.AMOUNT.toString(), amount);
		context.setAttribute(OfferPage.ELEMENTS_KEYS.MONTHLY.toString(), monthly);
		context.setAttribute(OfferPage.ELEMENTS_KEYS.TERM.toString(), term);
		context.setAttribute(OfferPage.ELEMENTS_KEYS.RATE.toString(), rate);
		context.setAttribute(OfferPage.ELEMENTS_KEYS.APR.toString(), apr);
		
		offer.singOut();
	}
	
	
	@Test(dependsOnMethods = {"testOffer"})
	public void testLogin(ITestContext context){
		login.openPage();
		login.login((String) context.getAttribute("username"), (String) context.getAttribute("password"));
		String amount = offer.getElementText(OfferPage.ELEMENTS_KEYS.AMOUNT);
		String monthly = offer.getElementText(OfferPage.ELEMENTS_KEYS.MONTHLY);
		String term = offer.getElementText(OfferPage.ELEMENTS_KEYS.TERM);
		String rate = offer.getElementText(OfferPage.ELEMENTS_KEYS.RATE);
		String apr = offer.getElementText(OfferPage.ELEMENTS_KEYS.APR);
		
		Assert.assertEquals(amount, context.getAttribute(OfferPage.ELEMENTS_KEYS.AMOUNT.toString()));
		Assert.assertEquals(monthly, context.getAttribute(OfferPage.ELEMENTS_KEYS.MONTHLY.toString()));
		Assert.assertEquals(term, context.getAttribute(OfferPage.ELEMENTS_KEYS.TERM.toString()));
		Assert.assertEquals(rate, context.getAttribute(OfferPage.ELEMENTS_KEYS.RATE.toString()));
		Assert.assertEquals(apr, context.getAttribute(OfferPage.ELEMENTS_KEYS.APR.toString()));
	}
	
	
	@SuppressWarnings("unchecked")
	@Test(dependsOnMethods = {"testOffer"})
	public void testPost(ITestContext context) {
		HttpClient client = new HttpClient(API_URL);

		@SuppressWarnings("serial")
		HashMap<String, String> headers = new HashMap<String, String>() {
			{
				put("Content-Type", "application/json");
				put("x-cf-source-id", "coding-challenge");
				put("x-cf-corr-id", UUID.randomUUID().toString());
			}
		};
		JSONObject jo = new JSONObject();
		jo.put("username", (String) context.getAttribute("username"));
		jo.put("password", (String) context.getAttribute("password"));
		String payload = jo.toJSONString();
		
		try {
			CloseableHttpResponse response = client.postRequest("/login", headers, payload);
			StatusLine code = response.getStatusLine();
			Assert.assertEquals(code.getStatusCode(), 200);
			JSONObject result = (JSONObject) parser.parse(EntityUtils.toString(response.getEntity()));
			JSONArray array  = (JSONArray) result.get("loanApplications");
			String type = ((JSONObject) array.get(0)).get("productType").toString();
			Assert.assertEquals(type, "PERSONAL_LOAN");
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail(e.getMessage());
		} 
	}

}
