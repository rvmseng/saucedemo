package com.saucedemo.pages;

import java.util.List;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.saucedemo.data.Endpoints;

public class InventoryPage extends BasePage {
	private final Locator inventoryList;
	private final Locator title;
	private final Locator burgerMenu;
	private final Locator items; // cards

	public InventoryPage(Page page) {
		super(page);
		this.inventoryList = page.locator(".inventory_list");
		this.title = page.locator(".title");
		this.burgerMenu = page.locator("#react-burger-menu-btn");
		this.items = page.locator(".inventory_item");
	}

	public void navigate() {
		open(Endpoints.inventory.getUrl());
	}
	
	public void shouldBeLoaded() {
		shouldBeVisible(inventoryList);
		shouldContainText(title, "Products");
	}

	public int itemCount() {
		return items.count();
	}

	public List<String> allTitles() {
		return items.locator(".inventory_item_name").allTextContents();
	}

	public void openFirstItem() {
		items.first().locator(".inventory_item_name").click();
	}

	public void openMenu() {
		click(burgerMenu);
	}
}
