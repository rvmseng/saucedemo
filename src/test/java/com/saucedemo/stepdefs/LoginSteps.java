package com.saucedemo.stepdefs;

import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

import java.util.Map;
import org.testng.Assert;
import com.saucedemo.factory.PlaywrightFactory;
import com.saucedemo.pages.LoginPage;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class LoginSteps extends BaseSteps{
	private final LoginPage loginPage;

	public LoginSteps() {
		this.loginPage = new LoginPage(PlaywrightFactory.getPage());
	}

	@Given("the user is on the login page")
	public void the_user_is_on_the_login_page() {
		loginPage.navigate();
	}

	@When("the user logs in with username {string} and password {string}")
	public void the_user_logs_in_with_credentials(String username, String password) {
		loginPage.performlogin(username, password);
	}

	@When("the user fills the form with:")
	public void the_user_fills_the_form_with(DataTable table) {
		Map<String, String> data = table.asMaps().get(0);

		loginPage.fillUserName(data.get("username"));
		loginPage.fillPassword(data.get("password"));
	}

	@When("click on login button")
	public void click_on_login_button() {
		loginPage.clickOnLoginButton();
	}

	@Then("the user should see the inventory page")
	public void the_user_should_see_the_inventory_page() {
		Assert.assertTrue(loginPage.isLoggedIn(), "User should be logged in and inventory visible");
	}

	@Then("the user should see an error message containing {string}")
	public void the_user_should_see_an_error_message_containing(String expectedText) {
		String actual = loginPage.getErrorText();

		assertNotNull(actual, "Error message should be displayed");
		assertTrue(actual.toLowerCase().contains(expectedText.toLowerCase()),"Error message should match");
	}
}
