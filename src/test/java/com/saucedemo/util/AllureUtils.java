package com.saucedemo.util;

import java.io.ByteArrayInputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.microsoft.playwright.Response;

import io.qameta.allure.Allure;

public class AllureUtils {

	private static final Logger log = LoggerFactory.getLogger(AllureUtils.class);

	@FunctionalInterface
	public interface StepSupplier<T> {
		T get();
	}

	public static <T> T step(String name, StepSupplier<T> stepSupplier) {
		return Allure.step(name, stepSupplier::get);
	}

	public void step(String name) {
		Allure.step(name);
	}

	public static void step(String name, Runnable stepAction) {
		Allure.step(name, stepAction::run);
	}

	/**
	 * @param username
	 * @param password
	 */
	public static void addLoginParameters(String username, String password) {
		Allure.step("Username: " + username);
		Allure.step("Password: " + "********");
	}

	public static void attachResponse(String title, Response response) {
		if (response != null) {
			String responseBody = response.text();
			Allure.addAttachment(title, "application/json", responseBody, ".json");
		}
	}

	public static void attachText(String title, String content) {
		Allure.addAttachment(title, content);
	}

	public static void attachScreenshot(byte[] bytes, String name) {
		try (ByteArrayInputStream bais = new ByteArrayInputStream(bytes)) {
			Allure.addAttachment(name, bais);
		} catch (Exception e) {
			log.error("Failed to attach screenshot to Allure report: " + e.getMessage());
			e.printStackTrace();
		}
	}
}
