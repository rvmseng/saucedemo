package com.saucedemo.listener;

import static com.saucedemo.config.ConfigProvider.config;
import static com.saucedemo.util.Utility.getEnvName;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.automatedowl.tools.AllureEnvironmentWriter;
import com.google.common.collect.ImmutableMap;

import io.qameta.allure.Allure;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.listener.TestLifecycleListener;
import io.qameta.allure.model.Label;
import io.qameta.allure.model.TestResult;

public class AllureListener implements TestLifecycleListener {
	private static final Logger logger = LoggerFactory.getLogger(AllureListener.class);

	@Override
	public void beforeTestStop(TestResult result) {
		TestLifecycleListener.super.beforeTestStop(result);
	}

	@Override
	public void beforeTestStart(TestResult result) {
		TestLifecycleListener.super.beforeTestStart(result);
	}

	@Override
	public void beforeTestUpdate(TestResult result) {
		TestLifecycleListener.super.beforeTestUpdate(result);
	}

	@Override
	public void afterTestStart(TestResult result) {
		TestLifecycleListener.super.afterTestStart(result);
		setSeverityLabel(result.getLabels());
		addAllureEnvFile();
	}

	@Override
	public void afterTestStop(TestResult result) {
		TestLifecycleListener.super.afterTestStop(result);
	}

	@Override
	public void afterTestUpdate(TestResult result) {
		TestLifecycleListener.super.afterTestUpdate(result);
	}

	private void addAllureEnvFile() {
		String configName = config().getLoadedFileName();
		try {
			AllureEnvironmentWriter.allureEnvironmentWriter(ImmutableMap.of("Environment", getEnvName(configName),
					"ConfigFile", configName, "Base_URL", config().getBaseURL()),
					System.getProperty("user.dir") + "/allure-results/");

			logger.debug("Allure environment file added to allure folder");
		} catch (Exception e) {
			logger.warn("Failed to write Allure environment file: " + e.getMessage());
		}
	}

	private void setSeverityLabel(List<Label> list) {
		List<String> severity = list.stream().filter(label -> label.getName().equalsIgnoreCase("severity"))
				.map(Label::getValue).toList();
		for (String tag : severity) {
			if (tag.equalsIgnoreCase("critical")) {
				Allure.label("severity", SeverityLevel.CRITICAL.value());
			} else if (tag.equalsIgnoreCase("blocker")) {
				Allure.label("severity", SeverityLevel.BLOCKER.value());
			} else if (tag.equalsIgnoreCase("minor")) {
				Allure.label("severity", SeverityLevel.MINOR.value());
			} else if (tag.equalsIgnoreCase("normal")) {
				Allure.label("severity", SeverityLevel.NORMAL.value());
			} else if (tag.equalsIgnoreCase("trivial")) {
				Allure.label("severity", SeverityLevel.TRIVIAL.value());
			}
		}
	}

}