package com.tozu.models;

// Statistics tracking for file organizing operations, and most importantly, their history, because these are statistics.... :)
public class OrganizationStats {
	
	private int totalFiles = 0;
	private int processedFiles = 0;
	private int errors = 0;
	private long totalSize = 0;
	private long processedSize = 0;
	private long startTime;
	private long endTime;
	
	public OrganizationStats() {
		this.startTime = System.currentTimeMillis();
	}
	
	public void incrementProcessed() {
		processedFiles++;
	}
	
	public void incrementErrors() {
		errors++;
	}
	
	public void addProcessedSize(long size) {
		processedSize += size;
	}
	
	public void finish() {
		this.endTime = System.currentTimeMillis();
	}
	
	public double getDurationSeconds() {
		long duration = (endTime > 0 ? endTime : System.currentTimeMillis()) - startTime;
		return duration / 1000.0;
	}
	
	public double getSuccessRate() {
		return totalFiles > 0 ? (double) processedFiles / totalFiles * 100 : 0;
	}
	
	public int getTotalFiles() {
		return totalFiles;
	}
	
	public void setTotalFiles(int totalFiles) {
		this.totalFiles = totalFiles;
		this.totalSize = 0; // Reset when setting new total
	}
	
	public int getProcessedFiles() {
		return processedFiles;
	}
	
	public int getErrors() {
		return errors;
	}
	
	public long getTotalSize() {
		return totalSize;
	}
	
	public void setTotalSize(long totalSize) {
		this.totalSize = totalSize;
	}
	
	public long getProcessedSize() {
		return processedSize;
	}
	
	public int getRemainingFiles() {
		return totalFiles - processedFiles - errors;
	}
	
	public int getSkippedFiles() {
		return totalFiles - processedFiles - errors;
	}
	
	@Override
	public String toString() {
		return String.format("OrganizationStats{total=%d, processed=%d, errors=%d, duration=%.2fs, success=%.1f%%}",
				totalFiles, processedFiles, errors, getDurationSeconds(), getSuccessRate()
		);
	}
}
