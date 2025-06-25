package com.tozu.models;

import java.util.function.Predicate;
import java.util.regex.Pattern;

public class OrganizationRule {
	
	public enum RuleType {
		EXTENSION,
		NAME_PATTERN,
		SIZE,
		DATE,
		CUSTOM
	};
	
	private final String name;
	private final String description;
	private final RuleType type;
	private final String targetFolder;
	private final Predicate<FileInfo> condition;
	private final int priority;
	private boolean enabled;
	
	public OrganizationRule(String name, String description, RuleType type,
			String targetFolder, Predicate<FileInfo> condition, int priority) {
		this.name = name;
		this.description = description;
		this.type = type;
		this.targetFolder = targetFolder;
		this.condition = condition;
		this.priority = priority;
		this.enabled = true;
	}
	
	public boolean matches(FileInfo fileInfo) {
		return enabled && conditions.test(fileInfo); // Testing with enum to make it work
	}
	
	// Factory methods for common rules
	public static OrganizationRule byExtension(String name, String extension, String targetFolder, int priority) {
		return new OrganizationRule(
				name,
				"Files with extension: " + extension,
				RuleType.EXTENSION,
				targetFolder,
				fileInfo -> fileInfo.getExtension().equalsIgnoreCase(extension),
				priority
		);
	}
	
	public static OrganizationRule byNamePattern(String name, String pattern, String targetFolder, int priority) {
		Pattern regex = Pattern.compile(pattern, Pattern.CASE_INSENSITIVE);
		return new OrganizationRule(
				name,
				"Files matching pattern: " + pattern,
				RuleType.NAME_PATTERN,
				targetFolder,
				fileInfo -> regex.matcher(fileInfo.getName()).matches(),
				priority
		);
	}
	
	public static OrganizationRule bySize(String name, long minSize, long maxSize, String targetFolder, int priority) {
		return new OrganizationRule(
				name,
				String.format("Files between %d and %d bytes", minSize, maxSize),
				RuleType.SIZE,
				targetFolder,
				fileInfo -> fileInfo.getSize() >= minSize && fileInfo.getSize() <= maxSize,
				priority
		);
	}
	
	public static OrganizationRule byDateRange(String name, LocalDateTime after, LocalDateTime before,
			String targetFolder, int priority) {
		return new OrganizationRule(
				name,
				"Files modified between " + after + " and " + before,
				RuleType.DATE,
				targetFolder,
				fileInfo -> {
					LocalDateTime modified = fileInfo.getLastModified();
					return modified.isAfter(after) && modified.isBefore(before);
				},
				priority
		);
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	

}
