package com.library.util;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/*
 * Custom logger for the Library Management System.
 * Provides thread-safe logging with different log levels, file rotation and formatted output.
 * 
 * @author Tozu
 * @version 1.0
 * */
public class Logger {
	
	public enum LogLevel {
		DEBUG(0, "DEBUG"),
		INFO(1, "INFO"),
		WARN(2, "WARN"),
		ERROR(3, "ERROR"),
		FATAL(4, "FATAL");
		
		private final int level;
		private final String name;
		
		LogLevel(int level, String name) {
			this.level = level;
			this.name = name;
		}
		
		public int getLevel() {
			return level;
		}
		
		public String getName() {
			return name;
		}
	}
	
	private static Logger instance;
	private static final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
	
	private LogLevel currentLogLevel = LogLevel.INFO;
	private String logFilePath = "logs/library_app_log.txt";
	private String logDirectory = "logs";
	private boolean consoleOutput = true;
	private boolean fileOutput = true;
	private long maxFileSize = 10 * 1024 * 1024; // Equals 10mb
	private int maxBackupFiles = 5;
	
	private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
	private PrintWriter fileWriter;
	private File currentLogFile;
	
	// Private constructor for singleton pattern
	private Logger() {
		initializeLogger();
	}
	
	/*
	 * Get the singleton instance of Logger
	 * */
	public static Logger getInstance() {
		if (instance == null) {
			lock.writeLock().lock();
			try {
				if (instance == null) {
					instance = new Logger();
				}
			} finally {
				lock.writeLock().unlock();
			}
		}
		return instance;
	}
	
	/*
	 * Initialize the logger - create directories and text files
	 * */
	private void initializeLogger() {
		try {
			// Create log directory if it does not yet exist
			File logDir = new File(logDirectory);
			if (!logDir.exists()) {
				logDir.mkdirs();
			}
			
			// Initialize log file
			currentLogFile = new File(logFilePath);
			if (fileOutput) {
				fileWriter = new PrintWriter(new FileWriter(currentLogFile, true));
			}
			
			// Log initialization
			info("Logger initialized successfully");
		
		} catch (IOException e) {
			System.err.println("Failed to initialize logger: " + e.getMessage());
			fileOutput = false; // Disabling file output, if the initialization fails.
		}
	}
	
	/*
	 * Set minimum log level
	 * */
	public void setLogLevel(LogLevel level) {
		this.currentLogLevel = level;
		info("Log level changed to: " + level.getName());
	}
	
	/*
	 * Set the log file path
	 * */
	public void setLogFilePath(String path) {
		lock.writeLock().lock();
		try {
			closeFileWriter();
			this.logFilePath = path;
			this.currentLogFile = new File(path);
			if (fileOutput) {
				fileWriter = new PrintWriter(new FileWriter(currentLogFile, true));
			}
		} catch (IOException e) {
			System.err.println("Failed to change log file path: " + e.getMessage());
		} finally {
			lock.writeLock().unlock();
		}
	}
	
	/*
	 * Enable or disable console output
	 * */
	public void setConsoleOutput(boolean enabled) {
		this.consoleOutput = enabled;
	}
	
	/*
	 * Enable or disable file output
	 * */
	public void setFileOutput(boolean enabled) {
		this.fileOutput = enabled;
		if (!enabled) {
			closeFileWriter();
		} else if (fileWriter == null) {
			try {
				fileWriter = new PrintWriter(new FileWriter(currentLogFile, true));
			} catch (IOException e) {
				System.err.println("Failed to enable file output: " + e.getMessage());
			}
		}
	}
	
	/*
	 * Set maximum file size for rotation
	 * */
	public void setMaxFileSize(long maxSize) {
		this.maxFileSize = maxSize;
	}
	
	/*
	 * Set maximum number of backup files
	 * */
	public void setMaxBackupFiles(int maxFiles) {
		this.maxBackupFiles = maxFiles;
	}
	
	// Logging methods
	public void debug(String message) {
		log(LogLevel.DEBUG, message, null);
	}
	
	public void debug(String message, Throwable throwable) {
		log(LogLevel.DEBUG, message, throwable);
	}
	
	public void info(String message) {
		log(LogLevel.INFO, message, null);
	}
	
	public void info(String message, Throwable throwable) {
		log(LogLevel.INFO, message, throwable);
	}
	
	public void warn(String message) {
		log(LogLevel.INFO, message, null);
	}
	
	public void warn(String message, Throwable throwable) {
		log(LogLevel.DEBUG, message, throwable);
	}
	
	public void error(String message) {
		log(LogLevel.ERROR, message, null);
	}
	
	public void error(String message, Throwable throwable) {
		log(LogLevel.ERROR, message, throwable);
	}
	
	public void fatal(String message) {
		log(LogLevel.FATAL, message, null);
	}
	
	public void fatal(String message, Throwable throwable) {
		log(LogLevel.FATAL, message, throwable);
	}
	
	/*
	 * Core logging method
	 * */
	private void log(LogLevel level, String message, Throwable throwable) {
		if (level.getLevel() < currentLogLevel.getLevel()) {
			return; // Skip if below current log level
		}
		
		lock.readLock().lock();
		try {
			String timestamp = dateFormat.format(new Date());
			String threadName = Thread.currentThread().getName();
			String logEntry = formatLogEntry(timestamp, level, threadName, message, throwable);
			
			// Output to console
			if (consoleOutput) {
				if (level.getLevel() >= LogLevel.WARN.getLevel()) {
					System.err.println(logEntry);
				} else {
					System.out.println(logEntry);
				}
			}
			
			// Output to file
			if (fileOutput && fileWriter != null) {
				// Check if file rotation needed
				if (currentLogFile.length() > maxFileSize) {
					rotateLogFile();
				}
				
				fileWriter.println(logEntry);
				fileWriter.flush();
			}
		} finally {
			lock.readLock().unlock();
		}
	}
	
	/*
	 * Format log entry with consistent structure
	 * */
	private String formatLogEntry(String timestamp, LogLevel level, String threadName,
						String message, Throwable throwable) {
		StringBuilder sb = new StringBuilder();
		sb.append("[").append(timestamp).append("] ");
		sb.append("[").append(level.getName()).append("] ");
		sb.append("[").append(threadName).append("] ");
		sb.append(message);
		
		if (throwable != null) {
			sb.append("\n").append(getStackTrace(throwable));
		}
		
		return sb.toString();
	}
	
	/*
	 * Get stack trace as string
	 * */
	private String getStackTrace(Throwable throwable) {
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		throwable.printStackTrace(pw);
		return sw.toString();
	}
	
	/*
	 * Rotate log files when size limit is reached
	 * */
	private void rotateLogFile() {
		lock.writeLock().lock();
		try {
			closeFileWriter();
			
			// Rotate existing backup files
			for (int i = maxBackupFiles - 1; i >= 1; i--) {
				File oldFile = new File(logFilePath + "." + i);
				File newFile = new File(logFilePath + "." + (i + 1));
				if (oldFile.exists()) {
					if (newFile.exists()) {
						newFile.delete();
					}
					oldFile.renameTo(newFile);
				}
			}
			
			// Move current log file to backup
			File backupFile = new File(logFilePath + ".1");
			if (backupFile.exists()) {
				backupFile.delete();
			}
			currentLogFile.renameTo(backupFile);
			
			// Create new log file
			currentLogFile = new File(logFilePath);
			fileWriter = new PrintWriter(new FileWriter(currentLogFile, true));
		
			info("Log file rotated successfully");
		} catch (IOException e) {
			System.err.println("Failed to rotate log file: " + e.getMessage());
		} finally {
			lock.writeLock().unlock();
		}
	}
	
	/*
	 * Close file writer safely
	 * */
	private void closeFileWriter() {
		if (fileWriter != null) {
			fileWriter.close();
			fileWriter = null;
		}
	}
	
	/*
	 * Shutdown logger - close all resources
	 * */
	public void shutdown() {
		lock.writeLock().lock();
		try {
			info("Logger shutting down...");
			closeFileWriter();
		} finally {
			lock.writeLock().unlock();
		}
	}
	
	/*
	 * Log method entry for debugging
	 * */
	public void entering(String className, String methodName) {
		debug("ENTERING: " + className + "." + methodName + "()");
	}
	
	/*
	 * Log method entry with parameters
	 * */
	public void entering(String className, String methodName, Object... params) {
		StringBuilder sb = new StringBuilder();
		sb.append("ENTERING: ").append(className).append(".").append(methodName).append("(");
		for (int i = 0; i < params.length; i++) {
			if (i > 0) sb.append(", ");
			sb.append(params[i]);
		}
		sb.append(")");
		debug(sb.toString());
	}
	
	/*
	 * Log method exit
	 * */
	public void exiting(String className, String methodName) {
		debug("EXITING: " + className + "." + methodName + "()");
	}
	
	/*
	 * Log method exit with return value
	 * */
	public void exiting(String className, String methodName, Object result) {
		debug("EXITING: " + className + "." + methodName + "() -> " + result);
	}
	
	/*
	 * Log config info
	 * */
	public void logConfig() {
		info("Logger Configuration");
		info("Log Level: " + currentLogLevel.getName());
		info("Log File: " + logFilePath);
		info("Console Output " + consoleOutput);
		info("File Output: " + fileOutput);
		info("Max File Size: " + (maxFileSize / 1024 / 1024) + " MB");
		info("Max Backup Files: " + maxBackupFiles);
		info("=====================================");
	}
}
