package com.saucedemo.data;

import com.saucedemo.config.ConfigProvider;

public enum Endpoints {

	inventory("/inventory.html"),
	login("/");

	private final String url;

	public String getUrl() {
		return url;
	}

	private Endpoints(String url) {
		this.url = ConfigProvider.config().getBaseURL() + url;
	}

}
