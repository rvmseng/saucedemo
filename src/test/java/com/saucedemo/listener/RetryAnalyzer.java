package com.saucedemo.listener;

import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;
import static com.saucedemo.config.ConfigProvider.config;

public class RetryAnalyzer implements IRetryAnalyzer {

	private int retryCount = 0;
	private final int maxRetryCount;

	public RetryAnalyzer() {
		this.maxRetryCount = config().getMaxRetry();
	}

	@Override
	public boolean retry(ITestResult result) {
		if (retryCount < maxRetryCount) {
			retryCount++;
			return true;
		}
		return false;
	}
}