package com.saucedemo.hooks;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.saucedemo.factory.PlaywrightFactory;
import com.saucedemo.util.Utility;

import io.cucumber.java.After;
import io.cucumber.java.AfterAll;
import io.cucumber.java.Before;
import io.cucumber.java.BeforeAll;
import io.cucumber.java.Scenario;

public class TestHooks {

	private static final Logger log = LoggerFactory.getLogger(TestHooks.class);
	
	public TestHooks() {
		
	}
	
	@BeforeAll
	public static void beforeAll() {
		log.info("🚀 Test Execution Started");
		PlaywrightFactory.initPlaywright();
		Utility.addEnvironmentFileToAllure();
	}
	
	@Before(order = 0)
	public void Setup() {
		log.info("🛠️ Setup method called");
		PlaywrightFactory.initBrowser();
	}

	@After
	public void teardown(Scenario scenario) {
		log.info("🧹 teardown method called");
		Utility.attachAndSaveScreenshot(scenario);
		
		PlaywrightFactory.cleanup();
	}
	
	@AfterAll
	public static void afterAll() {
		log.info("🏁 Test Execution Finished 🏁");
		PlaywrightFactory.shutdown();
	}
}
