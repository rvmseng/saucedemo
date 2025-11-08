package com.saucedemo.factory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.BrowserType.LaunchOptions;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import com.microsoft.playwright.options.Cookie;
import com.saucedemo.config.ConfigProvider;

public class PlaywrightFactory {
	private static final Logger log = LoggerFactory.getLogger(PlaywrightFactory.class);

	private static final ThreadLocal<Playwright> playwrightTl = new ThreadLocal<>();
	private static final ThreadLocal<Browser> browserTl = new ThreadLocal<>();
	private static final ThreadLocal<BrowserContext> browserContextTl = new ThreadLocal<>();
	private static final ThreadLocal<Page> pageTl = new ThreadLocal<>();
	private static String browserName = "";

	public static void initPlaywright() {
		Playwright playwright = playwrightTl.get();
		if (playwright == null) {
			playwright = Playwright.create();
			playwrightTl.set(playwright);
		}

		Browser browser = browserTl.get();
		if (browser == null) {
			browserName = System.getProperty("browser", "chrome").toLowerCase();

			BrowserType browserType;
			switch (BrowserName.valueOf(browserName)) {
			case firefox:
				browserType = playwright.firefox();
				break;
			case webkit:
				browserType = playwright.webkit();
				break;
			case chrome:
			case chromium:
			default:
				browserType = playwright.chromium();
				break;
			}
			browser = browserType.launch(new LaunchOptions().setHeadless(false));
			browserTl.set(browser);
		}

		log.info("✅ Playwright initialized.");
	}

	public static void initBrowser() {
		Browser browser = browserTl.get();
		if (browser == null) {
			initPlaywright();
		}

		BrowserContext context = browser.newContext();
		browserContextTl.set(context);

		Page page = context.newPage();
		page.setDefaultTimeout(ConfigProvider.config().getDefaultTimeout());

		pageTl.set(page);
		log.info("✅ Browser initialized: " + browserName);
	}

	public static Page getPage() {
		return pageTl.get();
	}

	public static void cleanup() {
		try {
			Page page = pageTl.get();
			if (page != null)
				page.close();
		} catch (Exception ignored) {
		}
		try {
			BrowserContext context = browserContextTl.get();
			if (context != null)
				context.close();
		} catch (Exception ignored) {
		}

		pageTl.remove();
		browserContextTl.remove();

		log.info("▶ Playwright cleanup done");
	}

	public static void shutdown() {
		try {
			Page page = pageTl.get();
			if (page != null)
				page.close();
		} catch (Exception ignored) {
		}
		try {
			BrowserContext context = browserContextTl.get();
			if (context != null)
				context.close();
		} catch (Exception ignored) {
		}
		try {
			Browser browser = browserTl.get();
			if (browser != null)
				browser.close();
		} catch (Exception ignored) {
		}
		try {
			Playwright playwright = playwrightTl.get();
			if (playwright != null)
				playwright.close();
		} catch (Exception ignored) {
		}

		pageTl.remove();
		browserContextTl.remove();
		browserTl.remove();
		playwrightTl.remove();

		log.info("▶ Playwright shutdown done");
	}

	public static String getBrowserName() {
		return browserName;
	}

	public static Optional<Cookie> getCookie(String name) {
		List<Cookie> cookies = getCookies();
		Optional<Cookie> cookie = cookies.stream().filter(c -> c.name.equals(name)).findFirst();

		return cookie;
	}

	public static List<Cookie> getCookies() {
		BrowserContext context = browserContextTl.get();
		if (context != null) {
			return context.cookies();
		}

		log.warn("❌ Can not find any cookies!");
		return new ArrayList<Cookie>();
	}

	public static void addCookies(String name, String value, String domain, String path, Double expiresUnixSeconds,
			boolean httpOnly, boolean secure) {
		BrowserContext context = browserContextTl.get();
		if (context == null) {
			log.warn("❌ Can not set cookies, BrowserContext is null!");
			throw new RuntimeException("❌ Can not set cookies, BrowserContext is null");
		}

		Cookie cookie = new Cookie(name, value);
		if (domain != null) {
			cookie.setDomain(domain);
		}

		if (path != null) {
			cookie.setPath(path);
		}

		double expires = (System.currentTimeMillis() / 1000.0) + (24 * 60 * 60); // next day
		if (expiresUnixSeconds != null) {
			expires = expiresUnixSeconds;
		}

		cookie.setExpires(expires);
		cookie.setHttpOnly(httpOnly);
		cookie.setSecure(secure);

		List<Cookie> list = Arrays.asList(cookie);
		context.addCookies(list);
	}

	enum BrowserName {
		chrome, chromium, firefox, webkit,
	}
}
