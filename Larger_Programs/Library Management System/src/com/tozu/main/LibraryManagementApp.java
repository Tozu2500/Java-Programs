package com.tozu.main;

import java.awt.Color;
import java.awt.Font;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.logging.Logger;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

/*
 * Main application class for the Library Management System.
 * This class serves as the entry point and handles the initialization of the application.
 * 
 * @author Tozu
 * @version 1.0
 * 
 * */
public class LibraryManagementApp {
	
	private static final String APP_NAME = "Library Management System";
	private static final String VERSION = "1.0.0";
	private static Logger logger = Logger.getLogger(null);
	
	/*
	 * Main method - entry point for the app
	 * @param args command line arguments
	 * */
	public static void main(String[] args) {
		// Set system properties for better GUI appearance
		setSystemProperties();
		
		// Initialize logger
		logger.info("Starting " + APP_NAME + " v" + VERSION);
		
		// Load config
		if (!loadConfiguration()) {
			showErrorAndExit("Failed to load configuration. Please check config files.");
			return;
		}
		
		// Initialize db connection
		if (!initializeDatabase()) {
			showErrorAndExit("Failed to initialize database connection. Please check database settings.");
			return;
		}

		// Set Look and Feel
		setLookAndFeel();
		
		// Start the application (GUI)
		SwingUtilities.invokeLater(() -> {
			try {
				startApplication();
			} catch (Exception e) {
				e.printStackTrace();
				Logger.error("");
			}
		});	
	}
	
	/*
	 * Set system properties for better GUI appearance
	 * */
	private static void setSystemProperties() {
		// Enable anti-aliasing for text
		System.setProperty("awt.useSystemAAFontSettings", "on");
		System.setProperty("swing.aatext", "true");
		
		// Set application name for macOS
		System.setProperty("apple.awt.application.name", APP_NAME);
		
		// Enable hardware acceleration if available
		System.setProperty("sun.java2d.opengl", "true");
		
		logger.info("System properties configured");
	}
	
	/*
	 * Load app config from properties files
	 * @return true if configuration loaded successfully, false otherwise
	 * */
	private static boolean loadConfiguration() {
		try {
			ConfigReader.loadConfiguration();
			logger.info("Configuration loaded successfully!");
			return true;
		} catch (Exception e) {
			logger.severe("Failed to load configuration: " + e.getMessage());
			return false;
		}
	}
	
	/*
	 * Initialize database connection and create tables if needeed
	 * @return true if database initialized successfully, false otherwise
	 * */
	private static boolean initializeDatabase() {
		try {
			// Test database connection
			Connection connection = DatabaseConnection.getConnection();
			if (connection == null) {
				logger.severe("Failed to establish database connection");
				return false;
			}
			
			// Initialize database schema if needed
			DatabaseInitializer.initializeDataabase();
			
			// Close test connection
			connection.close();
			
			logger.info("Database initialized successfully");
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			logger.severe("Database initialization failed: " + e.getMessage());
			return false;
		} catch (Exception e) {
			e.printStackTrace();
			logger.severe("Unexpected error during database initialization: " + e.getMessage());
			return false;
		}
	}
	
	/*
	 * Set the look and feel for the app
	 * */
	private static void setLookAndFeel() {
		try {
			// System look and feel
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			logger.info("System Look and Feel applied");
		} catch (Exception e) {
			logger.warning("Failed to set system look and feel, using default: " + e.getMessage());
			try {
				// Fallback to nimbus look and feel
				for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
					if ("Nimbus".equals(info.getName())) {
						UIManager.setLookAndFeel(info.getClassName());
						logger.info("Nimbus look and feel applied");
						break;
					}
				}
			} catch (Exception ex) {
				e.printStackTrace();
				logger.warning("Failed to set Nimbus look and feel: " + e.getMessage());
			}
		}
		
		// Customize UI defaults
		customizeUIDefaults();
	}
	
	/*
	 * Customize UI defaults for better appearance
	 * */
	private static void customizeUIDefaults() {
		// Set default font
		Font defaultFont = new Font(Font.SANS_SERIF, Font.PLAIN, 12);
		UIManager.put("Button.font", defaultFont);
		UIManager.put("Label.font", defaultFont);
		UIManager.put("TextField.font", defaultFont);
		UIManager.put("TextArea.font", defaultFont);
		UIManager.put("Table.font", defaultFont);
		UIManager.put("Menu.font", defaultFont);
		UIManager.put("MenuItem.font", defaultFont);
		
		// Set default colors
		UIManager.put("Panel.background", new Color(240, 240, 240));
		UIManager.put("Button.background", new Color(225, 225, 225));
		
		// Set table defaults
		UIManager.put("Table.selectionBackgound", new Color(184, 207, 229));
		UIManager.put("Table.selectionForeground", Color.BLACK);
		UIManager.put("Table.gridColor", new Color(200, 200, 200));
		
		logger.info("UI defaults customized");
	}
	
	/*
	 * Start the main app
	 * */
	private static void startApplication() {
		// Check if login is required (based on configuration)
		boolean loginRequired = ConfigReader.getBoolean("login.required", true);
		
		if (loginRequired) {
			// Show login frame first
			showLoginFrame();
		} else {
			// Directly show the main app
			showMainFrame();
		}
	}
	
	/*
	 * Show the login frame
	 * */
	private static void showLoginFrame() {
		SwingUtilities.invokeLater(() -> {
			LoginFrame loginFrame = new LoginFrame();
			loginFrame.setVisible(true);
			
			// Set up login success callback
			loginFrame.setOnLoginSuccess(() -> {
				loginFrame.dispose();
				showMainFrame();
			});
			
			logger.info("Login frame displayed");
		});
	}
	
	/*
	 * Show the main app frame
	 * */
	private static void showMainFrame() {
		SwingUtilities.invokeLater(() -> {
			MainFrame mainFrame = new MainFram();
			mainFrame.setVisible(true);
			
			// Center the frame on screen
			mainFrame.setLocationRelativeTo(null);
			
			// Set up shutdown hook
			setupShutdownHook();
			
			logger.info("Main application frame displayed");
		});
	}
	
	/*
	 * Set up shutdown hook for graceful app termination
	 * */
	private static void setupShutdownHook() {
		Runtime.getRuntime().addShutdownHook(new Thread(() -> {
			logger.info("Application shutting down...");
			
			try {
				// Close database connections
				DatabaseConnection.closeAllConnections();
				logger.info("Database connections closed");
			} catch (Exception e) {
				logger.warning("Error closing database connections: " + e.getMessage());
			}
			
			logger.info("Application shutdown complete");
		}));
	}
	
	/*
	 * Show error message and exit application
	 * @param message error message to display
	 * */
	private static void showErrorAndExit(String message) {
		SwingUtilities.invokeLater(() -> {
			JOptionPane.showMessageDialog(
					null,
					message,
					APP_NAME + " - Error",
					JOptionPane.ERROR_MESSAGE
			);
			System.exit(1);
		});
	}
	
	/*
	 * Get application name
	 * @return app name
	 * */
	private static String getAppName() {
		return APP_NAME;
	}
	
	/*
	 * Get app version
	 * @return app version
	 * */
	private static String getAppVersion() {
		return VERSION;
	}
	
}
