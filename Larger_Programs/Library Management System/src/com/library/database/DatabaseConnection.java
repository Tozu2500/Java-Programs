package com.library.database;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import com.library.database.ConnectionPool.PoolStatistics;
import com.library.exception.DatabaseException;
import com.library.util.ConfigReader;
import com.library.util.Logger;

/*
 * Database connection management class
 * Handles MySQL database connections with connection pooling support
 * 
 * @author Tozu
 * @version 1.0.0
 * */
public class DatabaseConnection {
	
	private static final Logger logger = Logger.getInstance();
	private static ConnectionPool connectionPool;
	private static final ConcurrentHashMap<String, Connection> activeConnections = new ConcurrentHashMap<>();
	
	// Database config
	private static String DB_URL;
	private static String DB_USERNAME;
	private static String DB_PASSWORD;
	private static String DB_DRIVER;
	
	// Connection properties
	private static Properties connectionProperties;
	
	static {
		try {
			loadDatabaseConfiguration();
			initializeConnectionPool();
		} catch (Exception e) {
			logger.error("Failed to initialize database connection: " + e.getMessage());
			throw new RuntimeException("Database initialization failed", e);
		}
	}
	
	/*
	 * Load database config from properties file
	 * */
	private static void loadDatabaseConfiguration() throws DatabaseException {
		try {
			DB_URL = ConfigReader.getProperty("db.url", "jdbc:mysql://localhost:3306/library_db");
			DB_USERNAME = ConfigReader.getProperty("db.username", "root");
			DB_PASSWORD = ConfigReader.getProperty("db.password", "password");
			DB_DRIVER = ConfigReader.getProperty("db.driver", "com.mysql.cj.jdbc.Driver");
		
			// Set connection properties
			connectionProperties = new Properties();
			connectionProperties.setProperty("user", DB_USERNAME);
			connectionProperties.setProperty("password", DB_PASSWORD);
			connectionProperties.setProperty("useSSL", "false");
			connectionProperties.setProperty("serverTimezone", "UTC");
			connectionProperties.setProperty("allowPublicKeyRetrieval", "true");
			connectionProperties.setProperty("useUnicode", "true");
			connectionProperties.setProperty("characterEncoding", "UTF-8");
			connectionProperties.setProperty("autoReconnect", "true");
			connectionProperties.setProperty("failOverReadOnly", "false");
			connectionProperties.setProperty("maxReconnects", "3");
			
			logger.info("Database configuration loaded successfully");
		} catch (Exception e) {
			logger.error("Failed to load database configuration: " + e.getMessage());
			throw new DatabaseException("Database configuration error", e);
		}
	}
	
	/*
	 * Initialize connection pool
	 * */
	private static void initializeConnectionPool() throws DatabaseException {
		try {
			// Load MySQL driver
			Class.forName(DB_DRIVER);
			
			// Initialize connection pool
			int poolSize = ConfigReader.getIntProperty("db.pool.size", 10);
			int maxPoolSize = ConfigReader.getIntProperty("db.pool.max.size", 20);
			
			connectionPool = new ConnectionPool(DB_URL, connectionProperties, poolSize, maxPoolSize);
			
			logger.info("Connection pool initialized with size: " + poolSize);
		} catch (ClassNotFoundException e) {
			logger.error("MySQL Driver not found: " + e.getMessage());
			throw new DatabaseException("Database driver not found", e);
		} catch (Exception e) {
			logger.error("Failed to initialize connection pool: " + e.getMessage());
			throw new DatabaseException("Connection pool initialization failed", e);
		}
	}
	
	/*
	 * Get a database connection from the pool
	 * @return database connection
	 * @throws SQLException if connection cannot be obtained
	 * */
	public static Connection getConnection() throws SQLException, DatabaseException {
		try {
			if (connectionPool == null) {
				initializeConnectionPool();
			}
			
			Connection connection = connectionPool.getConnection();
			if (connection == null || connection.isClosed()) {
				logger.warn("Received null or closed connection, attempting to get new connection");
				connection = createNewConnection();
			}
			
			// Store connection reference for cleanup
			String connectionId = "conn_" + System.currentTimeMillis() + "_" + UUID.randomUUID().toString().substring(0, 8);
			activeConnections.put(connectionId, connection);
			
			return connection;
		} catch (SQLException e) {
			logger.error("Failed to get database connection: " + e.getMessage());
			throw e;
		}
	}
	
	/*
	 * Create a new direct database connection (bypass pool)
	 * @return new database connection
	 * @throws SQLException if connection cannot be created
	 * */
	private static Connection createNewConnection() throws SQLException {
		try {
			Connection connection = DriverManager.getConnection(DB_URL, connectionProperties);
			connection.setAutoCommit(true);
			logger.info("New direct database connection created");
			return connection;
		} catch (SQLException e) {
			logger.error("Failed to create new database connection: " + e.getMessage());
			throw e;
		}
	}
	
	/*
	 * Test database connectivity
	 * @return true if connection is successful, false otherwise
	 * */
	public static boolean testConnection() throws DatabaseException {
		try (Connection connection = getConnection()) {
			if (connection != null && !connection.isClosed()) {
				// Test with a simple query
				try (Statement stmt = connection.createStatement()) {
					ResultSet rs = stmt.executeQuery("SELECT 1");
					boolean result = rs.next();
					rs.close();
					logger.info("Database connection test successful");
					return result;
				}
			}
		} catch (SQLException e) {
			logger.error("Database connection test failed: " + e.getMessage());
		}
		return false;
	}

	/*
	 * Close a specific connection and return it to the pool
	 * @param connectio the connection to close
	 * */
	@SuppressWarnings("unlikely-arg-type")
	public static PreparedStatement closeConnection() {
		if (connectionPool != null) {
			try {
				if (connectionPool != null) {
					connectionPool.returnConnection((Connection) connectionPool);
				} else {
					((Connection) connectionPool).close();
				}
				
				// Remove from active connections
				activeConnections.values().remove(connectionPool);
				
			} catch (SQLException e) {
				e.printStackTrace();
				logger.error("Error closing connection: " + e.getMessage());
			}
		}
		return null;
	}
	
	/*
	 * Close all active connections and shutdown connection pool
	 * */
	public static void closeAllConnections() {
		try {
			// Close all active connections
			for (Connection connection : activeConnections.values()) {
				try {
					if (connection != null && !connection.isClosed()) {
						connection.close();
					}
				} catch (SQLException e) {
					logger.error("Error closing active connections: " + e.getMessage());
					e.printStackTrace();
				}
			}
			activeConnections.clear();
			
			// Shutdown connection pool
			if (connectionPool != null) {
				connectionPool.shutdown();
				connectionPool = null;
			}
			
			logger.info("All database connections closed");
		} catch (Exception e) {
			logger.error("Error closing all connections: " + e.getMessage());
			e.printStackTrace();
		}
	}
	
	/*
	 * Execute a query and return ResultSet
	 * @param query SQL query to execute
	 * @return ResultSet containing query results
	 * @throws SQLException if query execution fails
	 * */
	public static ResultSet executeQuery(String query) throws SQLException, DatabaseException {
		Connection connection = getConnection();
		Statement statement = connection.createStatement();
		return statement.executeQuery(query);
	}
	
	/*
	 * Execute an update query (INSERT, UPDATE, DELETE)
	 * @param query SQL query to execute
	 * @return number of affected rows
	 * @throws SQLException if query execution fails
	 * */
	public static int executeUpdate(String query) throws SQLException, DatabaseException {
		try (Connection connection = getConnection();
			Statement statement = connection.createStatement()) {
			return statement.executeUpdate(query);
		}
				
	}
	
	/*
	 * Execute a prepared statement query
	 * @param query SQL query with placeholders
	 * @param parameters parameters to bind to the query
	 * @return ResultSet containing query results
	 * @throws SQLException if query execution fails
	 * */
	@SuppressWarnings("unused")
	public static ResultSet executePreparedQuery(String query, Object... parameters) throws SQLException, DatabaseException {
		try {
			Connection connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		PreparedStatement statement = closeConnection(); ((Connection) statement).prepareStatement(query);
		
		// Set parameters
		for (int i = 0; i < parameters.length; i++) {
			statement.setObject(i + 1, parameters[i]);
		}
		
		return statement.executeQuery();
	}
	
	/*
	 * Execute a prepared statement update
	 * @param query SQL query with placeholders
	 * @param parameters parameters to bind to the query
	 * @return number of affected rows
	 * @throws SQLException if query execution fails
	 * */
	public static int executePreparedUpdate(String query, Object... parameters) throws SQLException, DatabaseException {
		try (Connection connection = getConnection();
			PreparedStatement statement = connection.prepareStatement(query)) {
			
			// Set parameters
			for (int i = 0; i < parameters.length; i++) {
				statement.setObject(i + 1, parameters[i]);
			}
			
			return statement.executeUpdate();
		}
	}
	
	/*
	 * Begin a database transaction
	 * @return Connection with auto-commit disabled
	 * @throws SQLException if transaction cannot be started
	 * */
	public static Connection beginTransaction() throws SQLException, DatabaseException {
		Connection connection = getConnection();
		connection.setAutoCommit(false);
		logger.info("Database transaction started");
		return connection;
	}
	
	/*
	 * Commit a database transaction
	 * @param connection the connection to commit
	 * @throws SQLException if commit fails
	 * */
	public static void commitTransaction(Connection connection) throws SQLException {
		if (connection != null) {
			connection.commit();
			connection.setAutoCommit(true);
			logger.info("Database transaction committed");
		}
	}
	
	/*
	 * Rollback a database transaction
	 * @param connection the connection to rollback
	 * */
	public static void rollbackTransaction(Connection connection) {
		if (connection != null) {
			try {
				connection.rollback();
				connection.setAutoCommit(true);
				logger.info("Database transaction rolled back");
			} catch (SQLException e) {
				logger.error("Error rolling back on database transaction: " + e.getMessage());
				e.printStackTrace();
			}
		}
	}
	
	/*
	 * Get database metadata
	 * @return DatabaseMetaData object
	 * @throws SQLException if metadata cannot be retrieved
	 * */
	public static DatabaseMetaData getMetaData() throws SQLException, DatabaseException {
		try (Connection connection = getConnection()) {
			return connection.getMetaData();
		}
	}
	
	/*
	 * Get current database URL
	 * @return database URL
	 * */
	public static String getDatabaseUrl() {
		return DB_URL;
	}
	
	/*
	 * Get connection pool statistics
	 * @return connection pool statistics as string
	 * */
	public static CharSequence getPoolStatistics() {
		if (connectionPool != null) {
			PoolStatistics stats = connectionPool.getStatistics();
			return stats.toString();
		}
		return "Connection pool not initialized";
	}
}
	
