package com.saucedemo.hooks;

import java.util.ArrayList;

import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import io.qameta.allure.Allure;
import io.qameta.allure.SeverityLevel;

public class AllureSeverityHooks {

	@Before
	public void setSeverityTag(Scenario scenario) {
		ArrayList<String> tags = (ArrayList<String>) scenario.getSourceTagNames();
		for (String tag : tags) {
			if (tag.equalsIgnoreCase("@critical")) {
				Allure.label("severity", SeverityLevel.CRITICAL.value());
			} else if (tag.equalsIgnoreCase("@blocker")) {
				Allure.label("severity", SeverityLevel.BLOCKER.value());
			} else if (tag.equalsIgnoreCase("@minor")) {
				Allure.label("severity", SeverityLevel.MINOR.value());
			} else if (tag.equalsIgnoreCase("@normal")) {
				Allure.label("severity", SeverityLevel.NORMAL.value());
			} else if (tag.equalsIgnoreCase("@trivial")) {
				Allure.label("severity", SeverityLevel.TRIVIAL.value());
			}
		}
	}
}
