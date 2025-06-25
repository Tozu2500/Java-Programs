package com.tozu.core;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Logger;

// Core file organization, handles file organization operations with various strats
public class FileOrganizer {
	
	private final ExecutorService executorService;
	private final List<OrganizationRule> customRules;
	private OrganizationStats stats;
	private volatile boolean cancelled = false;
	
	// Progress callback interface
	public interface ProgressCallback {
		void onProgress(int current, int total, String currentFile);
		void onComplete(OrganizationStats stats);
		void onError(String error);
	}
	
	public FileOrganizer() {
		this.executorService = Executors.newFixedThreadPool(
				Runtime.getRuntime().availableProcessors()
		);
		this.customRules = new ArrayList<>();
		this.stats = new OrganizationStats();
	}
	
	// Organize files by .extension
	public CompletableFuture<OrganizationStats> organizeByExtension(File sourceDir) {
		return organizeByExtension(sourceDir, null);
	}
	
	public CompletableFuture<OrganizationStats> organizeByExtension(File sourceDir, ProgressCallback callback) {
		return CompletableFuture.supplyAsync(() -> {
			try {
				reset();
				Logger.info("Starting organization by extension in: " + sourceDir.getAbsolutePath());
				
				List<File> files = FileUtils.getAllFiles(sourceDir, ConfigManager.shouldProcessSubDirectories());
				stats.setTotalFiles(files.size());
				
				if (callback != null) {
					callback.onProgress(0, files.size(), "Initializing...");
				}
				
				Map<String, List<File>> extensionGroups = groupFilesByExtension(files);
				
				for (Map.Entry<String, List<File>> entry : extensionGroups.entrySet()) {
					if (cancelled) {
						break;
					}
					
					String extension = entry.getKey();
					List<File> fileGroup = entry.getValue();
					
					String folderName = extension.isEmpty() ? "No Extension" : extension.toUpperCase() + " Files";
					File targetDir = new File(sourceDir, folderName);
					
					createDirectoryIfNotExists(targetDir);
					
					for (File file : fileGroup) {
						if (cancelled) break;
						
						if (callback != null) {
							callback.onProgress(stats.getProcessedFiles(), stats.getTotalFiles(), file.getName());
						}
						
						moveFile(file, targetDir);
					}
				}
				
				Logger.info("Extension organization completed. Processed: " + stats.getProcessedFiles() + " files");
				return stats;
			} catch (IOException e) {
				Logger.error("Error during extension organizing. " + e.getMessage());
				if (callback != null) {
					callback.onError(e.getMessage());
					e.printStackTrace();
				}
				throw new RuntimeException(e);
			}
		}, executorService);
	}
			
	// Organize files by date
	public CompletableFuture<OrganizationStats> organizeByDate(File sourceDir) {
		return organizeByDate(sourceDir, null);
	}
	
	public CompletableFuture<OrganizationStats> organizeByDate(File sourceDir, ProgressCallback callback) {
		return CompletableFuture.supplyAsync(() -> {
			try {
				reset();
				Logger.info("Starting organizing the files by date: " + sourceDir.getAbsolutePath());
				
				List<File> files = FileUtils.getAllFiles(sourceDir, ConfigManager.shouldProcessSubdirectories());
				stats.setTotalFiles(Files.size());
			
				DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
				
				for (File file : files) {
					if (cancelled) break;
					
					if (callback != null) {
						callback.onProgress(stats.getProcessedFiles(), stats.getTotalFiles(), file.getName());
					}
					
					try {
						LocalDate fileDate = LocalDate.ofInstant(java.time.Instant.ofEpochMilli(file.lastModified()),
								ZoneId.systemDefault()
						);
						
						String dateFolder = fileDate.format(formatter);
						File targetDir = new File(sourceDir, dateFolder);
						
						createDirectoryIfNotExists(targetDir);
						moveFile(file, targetDir);
						
					} catch (Exception e) {
						Logger.error("Error processing file by date: " + file.getName(), e);
						stats.incrementError();
						e.printStackTrace();
					}
				}
				
				if (callback != null && !cancelled) {
					callback.onComplete(stats);
				}
				
				Logger.info("File organization by date completed. " + stats.getProcessedFiles() +" files");
				return stats;
			} catch (Exception e) {
				Logger.error("Error during date organization", e);
				if (callback != null) {
					callback.onError(e.getMessage());
				}
				throw new RuntimeException(e);
			}
		}, executorService);
	}
	
	// Organize files by size range
	public CompletableFuture<OrganizationStats> organizeBySize(File sourceDir) {
		return organizeBySize(sourceDir, null);
	}
	
	public CompletableFuture<OrganizationStats> organizeBySize(File sourceDir, ProgressCallback callback) {
		return CompletableFuture.supplyAsync(() -> {
			try {
				reset();
				Logger.info("Starting file organizing by size in: " + sourceDir.getAbsolutePath());
				
				List<File> files = FileUtils.getAllFiles(sourceDir, ConfigManager.shouldProcessSubdirectories());
				stats.setTotalFiles(Files.size());
				
				for (File file : files) {
					if (cancelled) break;
					
					if (callback != null) {
						callback.onProgress(stats.getProcessedFiles(), stats.getTotalFiles(), file.getName());
					}
					
					String sizeCategory = FileUtils.getSizeCategory(File.length());
					File targetDir = new File(sourceDir, sizeCategory);
					
					createDirectoryIfNotExists(targetDir);
					moveFile(file, targetDir);
				}
				
				if (callback != null && !cancelled) {
					callback.onComplete(stats);
				}
				
				Logger.info("File sorting by size completed. " + stats.getProcessedFiles() + " files");
				return stats;
			} catch (Exception e) {
				Logger.error("Error during file organizing", e);
				if (callback != null) {
					callback.onError(e.getMessage());
				}
				throw new RuntimeException(e);
			}
		}, executorService);
	}
	
	// Organize files by their type (documents, images, videos, etc...)
	public CompletableFuture<OrganizationStats> organizeByFileType(File sourceDir) {
		return organizeByFileType(sourceDir, null);
	}
	
	public CompletableFuture<OrganizationStats> organizeByFileType(File sourceDir, ProgressCallback callback) {
		return CompletableFuture.supplyAsync(() -> {
			try {
				reset();
				Logger.info("Starting file organizing by file type " + sourceDir.getAbsolutePath());
				
				List<File> files = FileUtils.getAllFiles(sourceDir, ConfigManager.shouldProcessSubdirectories());
				stats.setTotalFiles(Files.size());
				
				for (File file : files) {
					if (cancellled) break;
					
					if (callback != null) {
						callback.onProgress(stats.getProcessedFiles(), stats.getTotalFiles(), file.getName());
					}
					
					String fileType = FileUtils.getFileTypeCategory(file);
					File targetDir = new File(sourceDir, fileType);
					
					createDirectoryIfNotExists(targetDir);
					moveFile(file, targetDir);
				}
				
				if (callback != null && !cancelled) {
					callback.onComplete(stats);
				}
				
				Logger.info("File type organizing completed. " + stats.getProcessedFiles() + " files");
				return stats;
			} catch (Exception e) {
				Logger.error("Error during file type organization.", e);
				if (callback != null) {
					callback.onError(e.getMessage());
				}
				throw new RuntimeException(e);
			}
		}, executorService);
	}
	
	// Apply custom organizing rules
	public CompletableFuture<OrganizationStats> organizeByCustomRules(File sourceDir, ProgressCallback callback) {
		return CompletableFuture.supplyAsync(() -> {
			try {
				reset();
				Logger.info("Starting file organizing with custom rules in: " + sourceDir.getAbsolutePath());
				
				List<File> files = FileUtils.getAllFiles(sourceDir, ConfigManager.shouldProcessSubdirectories());
				stats.setTotalFiles(Files.size());
				
				for (File file : files) {
					if (cancelled) break;
					
					if (callback != null) {
						callback.onProgress(stats.getProcessedFiles(), stats.getTotalFiles(), file.getName());
					}
					
					FileInfo fileInfo = new FileInfo(file);
					String targetFolder = null;
					
					// Apply custom rules in order of priority
					for (OrganizationRule rule : customRules) {
						if (rule.matches(fileInfo)) {
							targetFolder = rule.getTargetFolder();
							break;
						}
					}
					
					if (targetFolder != null) {
						File targetDir = new File(sourceDir, targetFolder);
						createDirectoryIfNotExists(targetDir);
						moveFile(file, targetDir);
					} else {
						// Default behavior if no rule matches
						stats.incrementErrors();
					}
				}
				
				if (callback != null && !cancelled) {
					callback.onComplete(stats);
				}
				
				Logger.info("File organizing with custom rules completed. " + stats.getProcessedFiles() + " files");
				return stats;
			} catch (Exception e) {
				Logger.error("Error during file organization with custom rules.", e);
				if (callback != null) {
					callback.onError(e.getMessage());
				}
				throw new RuntimeException(e);
			}
		}, executorService);
	}
	
	// Utility methods
	private Map<String, List<File>> groupFilesByExtension(List<File> files) {
		Map<String, List<File>> groups = new HashMap<>();
		
		for (File file : files) {
			if (cancelled) break;
			
			String extension = FileUtils.getFileExtension(file);
			groups.computeIfAbsent(extension, k -> new ArrayList<>()).add(file);
		}
		
		return groups;
	}
	
	private void moveFile(File source, File targetDir) throws IOException {
		if (ConfigManager.isDryRun()) {
			Logger.info("DRY RUN: Would move " + source.getName() + " to " + targetDir.getName());
			stats.incrementProcessed();
			return;
		}
		
		File targetFile = new File(targetDir, source.getName());
		
		// Handle file name conflicts
		targetFile = resolveNameConflict(targetFile);
		
		try {
			Files.move(source.toPath(), targetFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
			stats.incrementProcessed();
			Logger.debug("Moved: " + source.getName() + " -> " + targetFile.getAbsolutePath());
			
		} catch (IOException e) {
			stats.incrementError();
			Logger.error("Failed to move the file(s) " + source.getName(), e);
			throw e;
		}
	}
	
	private File resolveNameConflict(File targetFile) {
		if (!targetFile.exists()) {
			return targetFile;
		}
		
		String name = FileUtils.getFileNameWithoutExtension(targetFile);
		String extension = FileUtils.getFileExtension(targetFile);
		File parentDir = targetFile.getParentFile();
		
		int counter = 1;
		while (targetFile.exists()) {
			String newName = name + "_" + counter;
			if (!extension.isEmpty()) {
				newName += "." + extension;
			}
			targetFile = new File(parentDir, newName);
			counter++;
		}
		
		return targetFile;
	}
	
	private void createDirectoryIfNotExists(File dir) throws IOException {
		if (!dir.exists()) {
			if (!dir.mkdirs()) {
				throw new IOException("Directory creation failed (IOException) " + dir.getAbsolutePath());
			}
			Logger.debug("Created directory: " + dir.getAbsolutePath());
		}
	}
	
	private void reset() {
		this.cancelled = true;
		Logger.info("File organization cancelled by user");
	}
	
	public void addCustomRule(OrganizationRule rule) {
		customRules.add(rule);
	}
	
	public void clearCustomRules() {
		customRules.clear();
	}
	
	public List<OrganizationRule> getCustomRules() {
		return new ArrayList<>(customRules);
	}
	
	public OrganizationStats getStats() {
		return stats;
	}
	
	public void shutdown() {
		executorService.shutdown();
	}
}
	

