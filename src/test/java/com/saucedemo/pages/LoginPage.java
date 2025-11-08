package com.saucedemo.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.saucedemo.data.Endpoints;

public class LoginPage extends BasePage {
	private final Locator username;
	private final Locator password;
	private final Locator loginButton;
	private final Locator errorMessage;

	public LoginPage(Page page) {
		super(page);
		this.username = page.locator("#user-name");
		this.password = page.locator("#password");
		this.loginButton = page.locator("#login-button");
		this.errorMessage = page.locator("div.error-message-container");
	}

	public void navigate() {
		open(Endpoints.login.getUrl());
	}

	public void performlogin(String user, String pass) {
		fill(username, user);
		fill(password, pass);
		click(loginButton);
	}

	public boolean isLoggedIn() {
		return isVisible($(".inventory_list"));
	}

	public void clickOnLoginButton() {
		click(loginButton);
	}

	public String getErrorText() {
		return errorMessage.textContent();
	}

	public void fillUserName(String string) {
		fill(username, string);
	}

	public void fillPassword(String string) {
		fill(password, string);
	}
}
