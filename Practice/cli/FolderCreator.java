package com.tozu.cli;

import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FolderCreator {

	public static void main(String[] args) {
		if (args.length == 0) {
			printUsage();
			System.exit(1);
		}
		
		boolean createParents = false;
		boolean verbose = false;
		String targetPath = null;
		
		// Parse arguments
		for (String arg : args) {
			switch (arg) {
				case "-p":
				case "--parents":
					createParents = true;
					break;
				case "-v":
				case "--verbose":
					verbose = true;
					break;
				default:
					targetPath = arg;
			}
		}
		
		if (targetPath == null) {
			System.err.println("Error, no folder path provided.");
			printUsage();
			System.exit(1);
		}
		
		Path path = Paths.get(targetPath);
		
		try {
			if (createParents) {
				Files.createDirectories(path);
				if (verbose) {
					System.out.println("Created directories (including parents): " + path.toAbsolutePath());
				}
			} else {
				Files.createDirectory(path);
				if (verbose) {
					System.out.println("Created directory: " + path.toAbsolutePath());
				}
			}
			System.exit(0);
		} catch (FileAlreadyExistsException e) {
			System.err.println("Error, directory already exists -> " + path.toAbsolutePath());
			System.exit(2);
		} catch (IOException e) {
			System.err.println("Error creating directory: " + e.getMessage());
			System.exit(3);
		}
	}
	
	private static void printUsage() {
		System.out.println("Usage: foldercli [options] <path>");
		System.out.println("Options:");
		System.out.println("  -p, --parents    Create parent directories as needed");
		System.out.println("  -v, --verbose    Print detailed output");
	}
	
}
