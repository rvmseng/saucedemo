package com.saucedemo.stepdefs;

import org.testng.SkipException;

import com.saucedemo.factory.PlaywrightFactory;
import com.saucedemo.pages.InventoryPage;
import com.saucedemo.pages.LoginPage;
import com.saucedemo.util.Utility;

import io.cucumber.java.en.Given;

public class CommonSteps {

	InventoryPage InventoryPage;
	
	public CommonSteps() {
		InventoryPage=new InventoryPage(PlaywrightFactory.getPage());
	}

	@Given("the user is authenticated")
	public void user_is_authenticated() {
		new LoginPage(PlaywrightFactory.getPage()).navigate();
		Utility.addStandardUserCookie();
	}

	@Given("the user is on the Inventory page")
	public void open_inventory_page() {
		InventoryPage.navigate();
	}

	@Given("the user is logged in and on the Inventory page")
	public void user_logged_in_and_on_Inventory_page() {
		user_is_authenticated();
		open_inventory_page();
	}

	@Given("the shopping basket be empty")
	public void shopping_basket_be_empty() {
		if(!InventoryPage.shoppingBasketIsEmpty()) {
			throw new SkipException("Expected shopping basket be empty but it has item.");
		}
	}

}
