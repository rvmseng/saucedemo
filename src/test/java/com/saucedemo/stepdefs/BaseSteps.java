package com.saucedemo.stepdefs;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BaseSteps {

	protected final Logger logger;

	public BaseSteps() {
		logger = LoggerFactory.getLogger(this.getClass());
	}

}
