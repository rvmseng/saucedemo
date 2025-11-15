package com.saucedemo.pages;

import java.util.ArrayList;
import java.util.List;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import com.microsoft.playwright.options.SelectOption;
import com.saucedemo.data.Endpoints;
import com.saucedemo.dto.Product;
import com.saucedemo.util.Utility;

public class InventoryPage extends BasePage {
	private final Locator inventoryList;
	private final Locator title;
	private final Locator burgerMenu;
	private final Locator items; // cards
	private final Locator productSortSelect;
	private final Locator shoppingCartBadge;
	private final Locator addToCartButtons;

	public InventoryPage(Page page) {
		super(page);
		this.inventoryList = page.locator(".inventory_list");
		this.title = page.locator(".title");
		this.burgerMenu = page.locator("#react-burger-menu-btn");
		this.items = page.locator(".inventory_item");
		this.productSortSelect = page.locator(".product_sort_container");
		this.shoppingCartBadge = page.locator(".shopping_cart_badge");
		this.addToCartButtons = page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Add to cart"));
	}

	public InventoryPage navigate() {
		open(Endpoints.inventory.getUrl());
		return this;
	}

	public InventoryPage clickOnAddtoCartButton(int index) {
		addToCartButtons.nth(index).click();
		return this;
	}

	public InventoryPage clickOnFirstAddtoCartButton() {
		addToCartButtons.first().click();
		return this;
	}

	public InventoryPage clickOnLastAddtoCartButton() {
		addToCartButtons.last().click();
		return this;
	}

	public InventoryPage reload() {
		page.reload();
		return this;
	}

	public boolean shoppingBasketIsEmpty() {
		return !isPresent(shoppingCartBadge);
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

	public InventoryPage openFirstItem() {
		items.first().locator(".inventory_item_name").click();
		return this;
	}

	public InventoryPage openMenu() {
		click(burgerMenu);
		return this;
	}

	public InventoryPage selectSortOptionByText(String text) {
		productSortSelect.selectOption(new SelectOption().setLabel(text));
		return this;
	}

	public String getSortOptionDefaultValue() {
		return productSortSelect.evaluate("el => el.options[el.selectedIndex].text").toString();
	}

	public List<Product> getAllProducts() {
		List<Product> products = new ArrayList<>();
		int count = itemCount();

		for (int i = 0; i < count; i++) {
			Locator item = items.nth(i);
			try {
				Product product = extractProductFromItem(item);
				products.add(product);
			} catch (Exception e) {
				logger.error(String.format("Failed to extract product at index %d: %s%n", i, e.getMessage()));
			}
		}
		return products;
	}

	private Product extractProductFromItem(Locator item) {
		String name = item.locator(".inventory_item_name").innerText().trim();
		String price = Utility.extractPrice(item.locator(".inventory_item_price").innerText());
		String description = item.locator(".inventory_item_desc").innerText().trim();
		String imageLink = item.locator("img.inventory_item_img").getAttribute("src");

		Product product = new Product();
		product.setName(name);
		product.setPrice(price);
		product.setDescription(description);
		product.setImageLink(imageLink);

		return product;
	}
}
