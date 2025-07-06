package com.library.database;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.library.exception.DatabaseException;
import com.library.util.ConfigReader;
import com.library.util.Logger;
import com.mysql.cj.jdbc.DatabaseMetaData;

/*
 * Database initializer class
 * Handles database schema creation and initial data population
 * 
 * @author Tozu
 * @version 1.0.0
 * */
public class DatabaseInitializer {
	
	private static final Logger logger = Logger.getInstance();
	private static final String SCHEMA_FILE = "/sql/schema.sql";
	private static final String INITIAL_DATA_FILE = "/sql/initial_data.sql";
	private static final String SAMPLE_DATA_FILE = "/sql/sample_data.sql";
	
	/* Initialize the database schema and data
	 * @throws DatabaseException if initialization fails
	 * */
	public static void initializeDatabase() throws DatabaseException {
		try {
			logger.info("Starting database initialization...");
			
			// Check if database needs initialization
			if (!isDatabaseInitialized()) {
				logger.info("Database not initialized, creating schema...");
				createDatabaseSchema();
				insertInitialData();
				
				// Insert sample data if configured
				if (ConfigReader.getBoolean("db.insert.sample.data", false)) {
					insertSampleData();
				}
				
				logger.info("Database initialization completed successfully");
			} else {
				logger.info("Database already initialized");
			
				// Check for schema updates
				checkForSchemaUpdates();
			}
		} catch (Exception e) {
			logger.error("Database initialization failed: " + e.getMessage());
			throw new DatabaseException("Failed to initialize database", e);
		}
	}

	/*
	 * Check if the database is already initialized
	 * @return true if database is initialized, false otherwise
	 * */
	private static boolean isDatabaseInitialized() throws DatabaseException {
		try (Connection connection = DatabaseConnection.getConnection()) {
			DatabaseMetaData metaData = (DatabaseMetaData) connection.getMetaData();
			
			// Check if main tables exist
			String[] requiredTables = {"books", "members", "transactions", "categories", "authors"};
			
			for (String tableName : requiredTables) {
				ResultSet tables = metaData.getTables(null, null, tableName.toUpperCase(), null);
				if (!tables.next()) {
					tables.close();
					return false;
				}
				tables.close();
			}
			
			logger.info("All required tables found in database");
			return true;
		} catch (SQLException e) {
			logger.error("Error checking database initialization: " + e.getMessage());
			return false;
		}
	}
	
	/*
	 * Create the database schema from SQL file
	 * @throws DatabaseException if schema creation fails
	 * */
	private static void createDatabaseSchema() throws DatabaseException {
		try {
			logger.info("Creating database schema...");
			
			List<String> sqlStatements = readSQLFile(SCHEMA_FILE);
			insertInitialData(sqlStatements, "Schema creation");
			
			logger.info("Database schema created successfully");
			
		} catch (Exception e) {
			logger.error("Failed to create database schema: " + e.getMessage());
			throw new DatabaseException("Schema creation failed", e);
		}
	}
	
	/* 
	 * Insert initial data into the database
	 * */
	private static void insertInitialData(List<String> sqlStatements, String label) {
	    try {
	        logger.info("Inserting initial data for " + label + "...");

	        for (String sql : sqlStatements) {
	            try {
	                int affectedRows = DatabaseConnection.executeUpdate(sql);
	                logger.debug(label + " | Executed: " + sql + " | Rows affected: " + affectedRows);
	            } catch (SQLException e) {
	                logger.warn(label + " | Error executing SQL: " + sql + " | " + e.getMessage());
	            }
	        }

	        logger.info(label + " inserted successfully");
	    } catch (Exception e) {
	        logger.error("Failed to insert " + label + ": " + e.getMessage());
	        // Optional data, so don't throw
	    }
	}

	
	/*
	 * Insert sample data into the database
	 * */
	private static void insertSampleData() {
		try {
			logger.info("Inserting sample data...");
			
			// Read SQL statements from a file
			List<String> sqlStatements = readSQLFile(SAMPLE_DATA_FILE);
			
			// Execute each SQL statement
			for (String sql : sqlStatements) {
				try {
					int affectedRows = DatabaseConnection.executeUpdate(sql);
					logger.debug("Executed: " + sql + " | Rows affected: " + affectedRows);
				} catch (SQLException e) {
					logger.warn("Error executing sample SQL: " + sql + " | " + e.getMessage());
				}
			}
			
			logger.info("Sample data inserted successfully");
		} catch (Exception e) {
			logger.error("Failed to insert sample data: " + e.getMessage());
			// Sample data is optional, so don't throw
		}
	}
	
	/*
	 * Read SQL statements from a file
	 * @param fileName the name of the SQL file
	 * @return list of SQL statements
	 * @throws IOException if file reading fails
	 * */
	private static List<String> readSQLFile(String fileName) throws IOException {
		List<String> statements = new ArrayList<>();
		
		try (InputStream inputStream = DatabaseInitializer.class.getResourceAsStream(fileName);
			BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
				
				if (inputStream == null) {
					logger.warn("SQL file not found: " + fileName);
					return statements;
				}
				
				StringBuilder currentStatement = new StringBuilder();
				String line;
				
				while ((line = reader.readLine()) != null) {
					line = line.trim();
					
					// Skip empty lines and comments
					if (line.isEmpty() || line.startsWith("--") || line.startsWith("#")) {
						continue;
					}
					
					currentStatement.append(line).append(" ");
					
					// Check if statement is complete (ends with semicolon)
					if (line.endsWith(";")) {
						String statement = currentStatement.toString().trim();
						if (!statement.isEmpty()) {
							statements.add(statement);
						}
						currentStatement = new StringBuilder();
					}
				}
				
				// Add any remaining statement
				if (currentStatement.length() > 0) {
					String statement = currentStatement.toString().trim();
					if (!statement.isEmpty()) {
						statements.add(statement);
					}
				}
				
				logger.info("Read " + statements.size() + " SQL statements from " + fileName);
				
			} catch (IOException e) {
				logger.error("Error reading SQL file " + fileName + ": " + e.getMessage());
				throw e;
			}
			return statements;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
