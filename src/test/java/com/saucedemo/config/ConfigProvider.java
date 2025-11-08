package com.saucedemo.config;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ConfigProvider {

	private static final Logger log = LoggerFactory.getLogger(ConfigProvider.class);
	private final Properties prop;
	private final String loadedFileName;
	private static ConfigProvider instance;
	
	private ConfigProvider(String fileName) {
		this.prop = new Properties();
		this.loadedFileName = fileName;

		try (FileInputStream fis = new FileInputStream("configs/" + fileName)) {
			prop.load(fis);
			log.info("Config loaded from: configs/{}", fileName);
		} catch (IOException e) {
			log.error("Failed to load config file '{}' : {}", fileName, e.getMessage());
			throw new RuntimeException("Failed to load config file: " + fileName, e);
		}
	}

	public static ConfigProvider config() {
		if (instance == null) {
			String file = System.getProperty("config"); // -Dconfig=...

			if (file == null || file.isBlank()) {
				file = System.getenv("CONFIG_FILE"); // env var CONFIG_FILE
			}

			if (file == null || file.isBlank()) {
				file = "uat.properties"; // fallback
				log.warn("No config specified; using fallback: {}", file);
			} else {
				log.info("Using config file: {}", file);
			}

			instance = new ConfigProvider(file);
		}

		return instance;
	}

	public static ConfigProvider config(String explicitFileName) {
		if (instance == null) {
			instance = new ConfigProvider(explicitFileName);
		} else {
			log.info("ConfigProvider already initialized with '{}'; explicit call ignored.",
					instance.getLoadedFileName());
		}
		return instance;
	}

	public String getLoadedFileName() {
		return loadedFileName;
	}

	public String getProperty(String key) {
		return prop.getProperty(key);
	}

	public String getProperty(String key, String defaultValue) {
		return prop.getProperty(key, defaultValue);
	}

	public String getBaseURL() {
		return prop.getProperty("baseURL");
	}

	public String getUsername() {
		return prop.getProperty("username");
	}

	public String getPassword() {
		return prop.getProperty("password");
	}

	public int getMaxRetry() {
		return Integer.parseInt(prop.getProperty("maxRetry", "1"));
	}
	
	public int getDefaultTimeout() {
		return Integer.parseInt(prop.getProperty("defaultTimeout", "5000"));
	}
}