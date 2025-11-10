package com.saucedemo.stepdefs;

import java.util.List;

import com.saucedemo.assertion.CustomeAssert;
import com.saucedemo.dto.Product;
import com.saucedemo.factory.PlaywrightFactory;
import com.saucedemo.pages.InventoryPage;

import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class InventoryPageProductSortingSteps extends BaseSteps {

	private InventoryPage inventoryPage;

	public InventoryPageProductSortingSteps() {
		super();
		inventoryPage = new InventoryPage(PlaywrightFactory.getPage());
	}

	@When("the user selects the sort option {string}")
	public void whenUserSelectsSortOption(String label) {
		logger.info("the user selects the sort option {}", label);
		inventoryPage.selectSortOptionByText(label);
	}
	
	@Then("the product list should be ordered by {string}")
	public void thenProductsShouldBeOrderedBy(String orderType) {
	    List<Product> products = inventoryPage.getAllProducts(); 
	    
	    switch(orderType) {
	        case "name-ascending":
	            CustomeAssert.assertListSortedByName(products, true);
	            break;
	        case "name-descending":
	        	CustomeAssert.assertListSortedByName(products, false);
	            break;
	        case "price-ascending":
	        	CustomeAssert.assertListSortedByPrice(products, true);
	            break;
	        case "price-descending":
	        	CustomeAssert.assertListSortedByPrice(products, false);
	            break;
	        default:
	            throw new IllegalArgumentException("Unknown orderType: " + orderType);
	    }
	}
}
