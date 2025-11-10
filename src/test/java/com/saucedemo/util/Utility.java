package com.saucedemo.util;

import static com.saucedemo.config.ConfigProvider.config;
import static com.saucedemo.util.AllureUtils.attachScreenshot;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.automatedowl.tools.AllureEnvironmentWriter;
import com.google.common.collect.ImmutableMap;
import com.microsoft.playwright.Page;
import com.saucedemo.factory.PlaywrightFactory;
import com.saucedemo.pages.LoginPage;

import io.cucumber.java.Scenario;

public class Utility {
	private static final Logger logger = LoggerFactory.getLogger(Utility.class);

	private static final Path SCREENSHOT_FOLDER = Paths.get("target", "screenshots");

	public static String getEnvName(String configName) {

		if (configName != null && !configName.isEmpty()) {
			String[] arr = configName.split("\\.");
			if (arr.length > 0) {
				return arr[0];
			}
		}
		return null;
	}

	public static void saveScreenshotToFile(byte[] bytes, String filename) {
		try {
			if (!Files.exists(SCREENSHOT_FOLDER)) {
				Files.createDirectories(SCREENSHOT_FOLDER);
			}
			Path target = SCREENSHOT_FOLDER.resolve(filename);
			Files.write(target, bytes);
		} catch (IOException e) {
			logger.error("Failed to save screenshot to file: " + e.getMessage());
			e.printStackTrace();
		}
	}

	public static void attachAndSaveScreenshot(Scenario scenario) {
		try {
			Page page = PlaywrightFactory.getPage();
			if (page == null) {
				logger.warn("Playwright page is null — cannot capture screenshot");
				return;
			}

			if (scenario.isFailed()) {
				// screen as binary
				byte[] bytes = page.screenshot(new Page.ScreenshotOptions().setFullPage(true));

				String testName = scenario.getName();
				String safeName = testName + "_" + System.currentTimeMillis() + ".png";

				// save to file
				saveScreenshotToFile(bytes, safeName);

				// add to allure report
				attachScreenshot(bytes, "Screenshot - " + testName);

				logger.warn("❌ Test [{}] FAILED! Attaching logs and API response to Allure...", testName);
			}

		} catch (Exception e) {
			logger.error("Failed to capture screenshot in listener: " + e.getMessage());
			e.printStackTrace();
		}
	}

	public static void addStandardUserCookie() {
		PlaywrightFactory.addCookies("session-username", "standard_user", "www.saucedemo.com", "/", null, false, false);
	}

	public static void doLoginWithDefaultCredentials() {
		Page page = PlaywrightFactory.getPage();
		LoginPage login = new LoginPage(page);

		login.performlogin(config().getUsername(), config().getPassword());
	}

	public static void addEnvironmentFileToAllure() {
		try {
			String configName = config().getLoadedFileName();
			AllureEnvironmentWriter.allureEnvironmentWriter(ImmutableMap.of(
					"Environment", getEnvName(configName),
					"ConfigFile", configName, "Base_URL", config().getBaseURL()),
					System.getProperty("user.dir") + "/allure-results/");

		} catch (Exception e) {
			String errorMessage = "❌ Initialization failed: " + e.getMessage();
			logger.error(errorMessage, e);
			throw new RuntimeException(errorMessage, e);
		}
	}
	
	public static String extractPrice(String priceText) {
	    // Remove currency symbols and extra spaces, keep only the numeric value
	    return priceText.replaceAll("[^\\d.,]", "").trim();
	}
	
	public static BigDecimal parsePriceToBigDecimal(String priceRaw) {
	    // مثال: "$9.99" -> 9.99
	    String cleaned = priceRaw.replaceAll("[^0-9.,-]", ""); // حذف نماد ارز
	    cleaned = cleaned.replace(",", "."); // اگر کاما جداکننده اعشار است
	    return new BigDecimal(cleaned);
	}
}
