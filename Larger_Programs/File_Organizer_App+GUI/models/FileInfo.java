package com.tozu.models;

import java.io.File;
import java.time.LocalDateTime;
import java.time.ZoneId;

// Represents file information for organizing rules
public class FileInfo {
	
	private final File file;
	private final String name;
	private final String extension;
	private final long size;
	private final LocalDateTime lastModified;
	private final LocalDateTime created;
	
	public FileInfo(File file) {
		this.file = file;
		this.name = file.getName();
		this.extension = getExtensionFromName(name);
		this.size = file.length();
		this.lastModified = LocalDateTime.ofInstant(
				java.time.Instant.ofEpochMilli(file.lastModified()),
				ZoneId.systemDefault()
		);
		
		// Note, Java does not have direct access to creation time on all systems.
		this.created = lastModified; // Fallback to last modified on this line
	}
	
	private String getExtensionFromName(String fileName) {
		int lastDot = fileName.lastIndexOf('.');
		return lastDot > 0 ? fileName.substring(lastDot + 1).toLowerCase() : "";
	}
	
	public File getFile() {
		return file;
	}
	
	public String getName() {
		return name;
	}

	public String getExtension() {
		return extension;
	}
	
	public long getSize() {
		return size;
	}
	
	public LocalDateTime getLastModified() {
		return lastModified;
	}
	
	public LocalDateTime getCreated() {
		return created;
	}
	
	public String getPath() {
		return file.getAbsolutePath();
	}
	
	public String getParentPath() {
		return file.getParent();
	}
	
	public boolean isDirectory() {
		return file.isDirectory();
	}
	
	public boolean isHidden() {
		return file.isHidden();
	}
	
	@Override
	public String toString() {
		return String.format("FileInfo(name='%s', size=%d, extension='%s'}",
				name, size, extension);
	}
}

