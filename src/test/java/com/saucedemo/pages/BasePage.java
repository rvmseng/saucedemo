package com.saucedemo.pages;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.LoadState;

public abstract class BasePage {
	protected final Logger logger;

	protected final Page page;

	protected BasePage(Page page) {
		logger = LoggerFactory.getLogger(this.getClass());
		this.page = page;
	}

	protected Locator $(String selector) {
		return page.locator(selector);
	}

	public void open(String url) {
		page.navigate(url);
		page.waitForLoadState(LoadState.NETWORKIDLE);
	}

	protected void click(Locator locator) {
		locator.click(); 
	}

	protected boolean isPresent(Locator locator) {
		return locator.count() > 0;
	}

	protected void fill(Locator locator, String value) {
		if (value != null && !value.isEmpty()) {
			locator.fill(value); // auto-wait
		}
	}

	protected boolean isVisible(Locator locator) {
		return locator.isVisible();
	}

	protected void shouldBeVisible(Locator locator) {
		assertThat(locator).isVisible();
	}

	public void shouldHaveTitle(String regex) {
		assertThat(page).hasTitle(java.util.regex.Pattern.compile(regex));
	}

	protected void shouldContainText(Locator locator, String text) {
		assertThat(locator).containsText(text);
	}
}
