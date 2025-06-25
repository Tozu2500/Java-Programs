package com.tozu;

import java.io.File;
import java.util.logging.Logger;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public class FileOrganizerApp {
	
	private static final String APP_NAME = "Advanced file organizer";
	
	private static final String VERSION = "1.0";

	public static void main(String[] args) {
		// Logger initialization
		Logger.initialize();
		Logger.info("Starting " + APP_NAME + " v" + VERSION);
		
		// Load configurations
		ConfigManager.loadConfig();
		
		if (args.length > 0 && args[0].equals("--cli")) {
			runCLI(args);
		} else {
			runGUI();
		}
	}
	
	private static void runGUI() {
		try {
			// Set system look and feel
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			Logger.error("Could not set system look and feel: " + e.getMessage());
			e.printStackTrace();
		}
		
		SwingUtilities.invokeLater(() -> {
			new FileOrganizerGUI().setVisible(true);
		});
	}
	
	private static void runCLI(String[] args) {
		if (args.length < 2) {
			printUsage();
			return;
		}
		
		String sourcePath = args[1];
		String mode = args.length > 2 ? args[2] : "extension";
		
		File sourceDir = new File(sourcePath);
		if (!sourceDir.exists() || !sourceDir.isDirectory()) {
			System.err.println("Error: Source directory doesn't exist or is not a directory on this PC.");
			return;
		}
		
		FileOrganizer organizer = new FileOrganizer();
		
		try {
			switch (mode.toLowerCase()) {
				case "extension":
					organizer.organizeByExtension(sourceDir);
					break;
				case "date":
					organizer.organizeByDate(sourceDir);
					break;
				case "size":
					organizer.organizeBySize(sourceDir);
					break;
				case "type":
					organizer.organizeByType(sourceDir);
					break;
				default:
					System.err.println("Unknown organization mode: " + mode);
					printUsage();
					return;
			}
			
			System.out.println("File organization completed successfully!");
			
		} catch (Exception e) {
			System.err.println("Error during file organization: " + e.getMessage());
			e.printStackTrace();
			Logger.error("CLI organization failed");
		}
	}
	
	private static void printUsage() {
		System.out.println("Usage: ");
		System.out.println("  GUI Mode: java FileOrganizerApp");
		System.out.println("  CLI Mode: java FileOrganizerApp --cli <source_path> [mode]");
		System.out.println("  Modes: extension, date, size, type");
	}
}
	

