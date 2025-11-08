package com.saucedemo.dataprovider;

import org.testng.annotations.DataProvider;
import static com.saucedemo.config.ConfigProvider.config;

public class LoginDataProvider {

	@DataProvider(name = "invalidLoginData")
	public static Object[][] invalidLoginData() {
		String correctUsername = config().getUsername();
		String correctPassword = config().getPassword();

		return new Object[][] {
				// {username, password, expected error message}
				{ "admin", correctPassword, "INVALID_CREDENTIALS"},
				{ correctUsername, "WrongPassword", "INVALID_CREDENTIALS"}};
	}
}
