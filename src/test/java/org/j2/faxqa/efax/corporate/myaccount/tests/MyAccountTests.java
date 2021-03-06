package org.j2.faxqa.efax.corporate.myaccount.tests;

import org.testng.Assert;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.annotations.Test;

import com.github.javafaker.Faker;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.j2.faxqa.efax.common.BaseTest;
import org.j2.faxqa.efax.common.Config;
import org.j2.faxqa.efax.common.TLDriverFactory;
import org.j2.faxqa.efax.common.TestRail;
import org.j2.faxqa.efax.corporate.admin.CommonMethods;
import org.j2.faxqa.efax.corporate.admin.CoreFaxFunctions;
import org.j2.faxqa.efax.corporate.admin.pageobjects.*;
import org.j2.faxqa.efax.corporate.myaccount.pageobjects.*;
import org.j2.faxqa.efax.efax_us.myaccount.pageobjects.AccountDetailsPage;
import org.j2.faxqa.efax.efax_us.myaccount.pageobjects.HomePage;

public class MyAccountTests  extends BaseTest {

	@TestRail(id = "C8149")
	@Test(enabled = true, priority = 1, description = "Verify that admin user should be able to create new MyAccount user")
	public void shouldBeAbleToCreateNewMyAccountUser(ITestResult result) throws Exception {
		System.out.print(1/0);
		new CoreFaxFunctions().createNewMyAccountUser();
		
	}

	@TestRail(id = "C8150")
	@Test(enabled = true, priority = 1, description = "Verify that Send outbound fax and Receive inbound fax functionality is working as expected")
	public void verifySendOutboundReceiveInboundFaxFunctionality(ITestContext context) throws Exception {

		String[] credentials = new CoreFaxFunctions().createNewMyAccountUser().split(";");
		LoginPageMyAccount loginPageMyAccount = new LoginPageMyAccount();
		loginPageMyAccount.open();
		loginPageMyAccount.login(credentials[0], credentials[1]);

		boolean response = new CoreFaxFunctions().composeSendFaxTo(credentials[0]);
		Assert.assertEquals(response, true);

	}

	@TestRail(id = "C8151")
	@Test(enabled = true, priority = 1, expectedExceptions = {Exception.class}, description = "Verify that user should be able to send faxes, receive and view them in 'View faxes' page")
	public void ableToReceiveAndViewFaxWhenStorageEnabledONFromMGMT(ITestContext context) throws Exception {

		String[] credentials = new CoreFaxFunctions().createNewMyAccountUser().split(";");
		LoginPageMyAccount loginPageMyAccount = new LoginPageMyAccount();
		loginPageMyAccount.open();
		loginPageMyAccount.login(credentials[0], credentials[1]);
		String faxNumber = credentials[0];

		String uniqueid = UUID.randomUUID().toString().replace("-", "").substring(0, 15);
		HomePageMyAccount homepage = new HomePageMyAccount();
		homepage.clickAccountDetailsTab();

		AccountDetailsPageMyAccount acctdetailspage = new AccountDetailsPageMyAccount();
		acctdetailspage.clickPreferencesTab();
		acctdetailspage.updatesendCSID(uniqueid);

		boolean response = new CoreFaxFunctions().composeSendFaxTo(credentials[0]);
		Assert.assertTrue(response);

		NavigationBarMyAccount navigationBarMyAccount = new NavigationBarMyAccount();
		navigationBarMyAccount.clickReportsTab();

		AccountDetailsPageMyAccount aacdetailspage = new AccountDetailsPageMyAccount();
		aacdetailspage.clickReportsTab();
		aacdetailspage.clickSendTab();
		aacdetailspage.clickSendGo();
		boolean flag = aacdetailspage.isSendActivityLogFound(uniqueid, 60);
		// Assert.assertTrue(response);

		aacdetailspage.clickReceiveTab();
		aacdetailspage.clickReceiveGo();
		flag = aacdetailspage.isReceiveActivityLogFound(uniqueid, 60);
		// Assert.assertTrue(response);

		aacdetailspage.clickViewFaxesTab();
		ViewFaxesModalMyAccount viewfaxespage = new ViewFaxesModalMyAccount();
		flag = viewfaxespage.isFaxReceived(uniqueid, 60);
		Assert.assertTrue(flag);

	}
}