package com.library.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/*
 * Utility class for reading configuration properties from configuration files.
 * This class provides methods to load and retrieve values
 * for the Library Management System.
 * 
 * @author Tozu
 * @version 1.0.0
 * */
public class ConfigReader {
	
	private static final Logger logger = Logger.getInstance();
	private static final String DEFAULT_CONFIG_FILE = "config/database.properties";
	private static Properties properties;
	private static ConfigReader instance;

	// Private constructor to implement singleton pattern
	private ConfigReader() {
		loadProperties(DEFAULT_CONFIG_FILE);
	}
	
	/*
	 * Get the singleton instance of ConfigReader
	 * @return ConfigReader instance
	 * */
	public static synchronized ConfigReader getInstance() {
		if (instance == null) {
			instance = new ConfigReader();
		}
		return instance;
	}
	
	/*
	 * Load properties from the specified configuration file
	 * @param configFile path to the configuration file
	 * */
	private void loadProperties(String configFile) {
		properties = new Properties();
		try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream(configFile)) {
			if (inputStream == null) {
				logger.warn("Configuration file not found: " + configFile);
				throw new RuntimeException("Configuration file not found: " + configFile);
			}
			properties.load(inputStream);
			logger.info("Configuration loaded successfully from: " + configFile);
		} catch (IOException e) {
			logger.fatal("Configuration loaded successfully from: " + configFile, e);
			throw new RuntimeException("Error loading configuration file: " + configFile, e);
		}
	}
	
	/*
	 * Get a property value by key
	 * @param key the property key
	 * @return the property value, or null if not found
	 * */
	public String getProperty(String key) {
		return properties.getProperty(key);
	}
	
	/*
	 * Get a property value by key with a default value
	 * @param key the property key
	 * @param defaultValue the default value if key is not found
	 * @return the property value, or default value if not found
	 * */
	public String getProperty(String key, String defaultValue) {
		return properties.getProperty(key, defaultValue);
	}
	
	/*
	 * Get a property value as integer
	 * @param key the property key
	 * @param defaultValue the default value if key is not found or invalid
	 * @return the property value as integer
	 * */
	public int getIntProperty(String key, int defaultValue) {
		String value = getProperty(key);
		if (value != null) {
			try {
				return Integer.parseInt(value.trim());
			} catch (NumberFormatException e) {
				logger.warn("Invalid integer value for property " + key + ": " + value);
			}
		}
		return defaultValue;
	}
	
	/*
	 * Get a property value as a boolean
	 * @param key the property key
	 * @param defaultValue the default value if key is not found
	 * @return the property value as boolean
	 * */
	public boolean getBooleanProperty(String key, boolean defaultValue) {
		String value = getProperty(key);
		
		if (value != null) {
			return Boolean.parseBoolean(value.trim());
		}
		return defaultValue;
	}
	
	/*
	 * Get a property value as long
	 * @param key the property key
	 * @param defaultValue the default value if key is not found or invalid
	 * @return the property value as long
	 * */
	public long getLongProperty(String key, long defaultValue) {
		String value = getProperty(key);
		if (value != null) {
			try {
				return Long.parseLong(value.trim());
			} catch (NumberFormatException e) {
				logger.warn("Invalid long value for property " + key + ": " + value);
			}
		}
		return defaultValue;
	}
	
	/*
	 * Get a property value as a double
	 * @param key the property key
	 * @param defaultValue the default value if key is not found or invalid
	 * @return the property value as double
	 * */
	public double getDoubleProperty(String key, double defaultValue) {
		String value = getProperty(key);
		if (value != null) {
			try {
				return Double.parseDouble(value.trim());
			} catch (NumberFormatException e) {
				logger.warn("Invalid double value for property " + key + ": " + value);
			}
		}
		return defaultValue;
	}
	
	/*
	 * Check if a property exists
	 * @param key the property key
	 * @return true if property exists, false otherwise
	 * */
	public boolean hasProperty(String key) {
		return properties.containsKey(key);
	}
	
	/*
	 * Get all property keys
	 * @return array of all property keys
	 * */
	public String[] getPropertyKeys() {
		return properties.keySet().toArray(new String[0]);
	}
	
	/*
	 * Reload configuration from file
	 * Useful for refreshing configuration without restarting the application
	 * */
	public void reloadConfiguration() {
		loadProperties(DEFAULT_CONFIG_FILE);
	}
	
	/* Load configuration from a custom file
	 * @param configFile path to the custom configuration file
	 * */
	public void loadCustomConfiguration(String configFile) {
		loadProperties(configFile);
	}
	
	// Convenience methods for common database configuration properties
	
	/*
	 * Get database URL
	 * @return database URL
	 * */
	public String getDatabaseUrl() {
		return getProperty("db.url");
	}

	/*
	 * Get database username
	 * @return database username
	 * */
	public String getDatabaseUsername() {
		return getProperty("db.username");
	}
	
	/*
	 * Get database password
	 * @return database password (In a real app, never return it like this.)
	 * */
	public String getDatabasePassword() {
		return getProperty("db.password");
	}
	
	/* 
	 * Get database driver class name
	 * @return database driver class name
	 * */
	public String getDatabaseDriver() {
		return getProperty("db.driver", "com.mysql.cj.jdbc.Driver");
	}
	
	/*
	 * Get maximum pool size for database connections
	 * @return maximum pool size
	 * */
	public int getMaxPoolSize() {
		return getIntProperty("db.pool.maxSize", 10);
	}
	
	/*
	 * Get minimum pool size for database connections
	 * @return minimum pool size
	 * */
	public int getMinPoolSize() {
		return getIntProperty("db.pool.minSize", 5);
	}
	
	/*
	 * Get connection timeout in seconds
	 * @return connection timeout in seconds
	 * */
	public int getConnectionTimeout() {
		return getIntProperty("db.connection.timeout", 30);
	}
	
	/*
	 * Get application name
	 * @return application name
	 * */
	public String getApplicationName() {
		return getProperty("app.name", "Library Management System");
	}
	
	/*
	 * Get application version
	 * @return application version
	 * */
	public String getApplicationVersion() {
		return getProperty("app.version", "1.0.0");
	}
	
	/*
	 * Get fine rate per day
	 * @return fine rate per day
	 * */
	public double getFineRatePerDay() {
		return getDoubleProperty("library.fine.ratePerDay", 0.50);
	}
	
	/*
	 * Get maximum days for book loan
	 * @return maximum loan days
	 * */
	public int getMaxLoanDays() {
		return getIntProperty("library.loan.maxDays", 14);
	}
	
	/*
	 * Get maximum number of books a member can borrow at once
	 * @return maximum books borrowed per member at once
	 * */
	public int getMaxBooksPerMember() {
		return getIntProperty("library.member.maxBooks", 5);
	}
	
}
