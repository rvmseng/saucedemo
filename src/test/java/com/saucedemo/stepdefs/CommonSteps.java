package com.saucedemo.stepdefs;

import com.saucedemo.factory.PlaywrightFactory;
import com.saucedemo.pages.InventoryPage;
import com.saucedemo.pages.LoginPage;
import com.saucedemo.util.Utility;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;

public class CommonSteps {

	public CommonSteps() {
		// TODO Auto-generated constructor stub
	}

	@Given("the user is authenticated")
	public void user_is_authenticated() {
		new LoginPage(PlaywrightFactory.getPage()).navigate();
		Utility.addStandardUserCookie();
	}
	
	@When("open inventory page")
	public void open_inventory_page() {
		new InventoryPage(PlaywrightFactory.getPage()).navigate();
	}

}
