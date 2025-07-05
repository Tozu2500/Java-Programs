package com.library.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;

import com.library.exception.DatabaseException;
import com.library.util.Logger;

/*
 * Database connection pool implementation
 * Manages a pool of database connections for efficient resource utilization
 * 
 * @author Tozu
 * @version 1.0.0
 * */
public class ConnectionPool {

	private static final Logger logger = Logger.getInstance();
	
	// Pool config
	private final String jdbcUrl;
	private final Properties connectionProperties;
	private final int initialSize;
	private final int maxSize;
	private final long connectionTimeout;
	private final long maxIdleTime;
	private final long validationInterval;
	
	// Connections management
	private final BlockingQueue<PooledConnection> availableConnections;
	private final BlockingQueue<PooledConnection> usedConnections;
	private final AtomicInteger totalConnections;
	private final ReentrantLock lock;
	
	// Pool state
	private volatile boolean isShutdown = false;
	private volatile boolean isInitialized = false;
	
	// Statistics
	private final AtomicInteger connectionsCreated;
	private final AtomicInteger connectionsDestroyed;
	private final AtomicInteger connectionsRequested;
	private final AtomicInteger connectionsReturned;
	
	// Background thread for connection maintenance
	private Thread maintenanceThread;
	
	/* Constructor for ConnectionPool
	 * @param jdbcUrl database URL
	 * @param connectionProperties connection properties
	 * @param initialSize initial pool size
	 * @param maxSize maximum pool size
	 * */
	public ConnectionPool(String jdbcUrl, Properties connectionProperties, int initialSize, int maxSize) throws DatabaseException {
		this.jdbcUrl = jdbcUrl;
		this.connectionProperties = connectionProperties;
		this.initialSize = initialSize;
		this.maxSize = maxSize;
		this.connectionTimeout = 30000; // 30 seconds - 30,000 milliseconds
		this.maxIdleTime = 600000; // 10 minutes
		this.validationInterval = 300000; // 5 minutes
		
		this.availableConnections = new LinkedBlockingQueue<>();
		this.usedConnections = new LinkedBlockingQueue<>();
		this.totalConnections = new AtomicInteger(0);
		this.lock = new ReentrantLock();
		
		this.connectionsCreated = new AtomicInteger(0);
		this.connectionsDestroyed = new AtomicInteger(0);
		this.connectionsRequested = new AtomicInteger(0);
		this.connectionsReturned = new AtomicInteger(0);
		
		initialize();
	}
	
	/* Initialize the connection pool
	 * */
	private void initialize() throws DatabaseException {
		try {
			logger.info("Initializing connection pool..");
			
			// Create initial connections
			for (int i = 0; i < initialSize; i++) {
				PooledConnection connection = createConnection();
				if (connection != null) {
					availableConnections.offer(connection);
					totalConnections.incrementAndGet();
					connectionsCreated.incrementAndGet();
				}
			}
			
			// Start maintenance thread
			startMaintenanceThread();
			
			isInitialized = true;
			logger.info("Connection pool initialized with " + availableConnections.size() + " connections");
			
		} catch (Exception e) {
			logger.error("Failed to initialize connection pool: " + e.getMessage());
			throw new DatabaseException("Connection pool initialization failure", e);
		}
	}
	
	/* Get a connection from the pool
	 * @return database connection
	 * @throws SQLException if connection cannot be obtained
	 * */
	public Connection getConnection() throws SQLException {
		if (isShutdown) {
			throw new SQLException("Connection pool is shutting down");
		}
		
		if (!isInitialized) {
			throw new SQLException("Connection pool is not initialized");
		}
		
		connectionsRequested.incrementAndGet();
		
		PooledConnection connection = null;
		
		try {
			// Try to get an available connection
			connection = availableConnections.poll(connectionTimeout, TimeUnit.MILLISECONDS);
			
			if (connection == null) {
				// Try to create a new connection if pool is not at max capacity
				lock.lock();
				try {
					if (totalConnections.get() < maxSize) {
						connection = createConnection();
						if (connection != null) {
							totalConnections.incrementAndGet();
							connectionsCreated.incrementAndGet();
						}
					}
				} finally {
					lock.unlock();
				}
			}
			
			if (connection == null) {
				throw new SQLException("Unable to obtain connection from pool within timeout period");
			}
			
			// Validate connections beforee returning
			if (!isConnectionValid(connection)) {
				destroyConnection(connection);
				return getConnection(); // Recursive try-again
			}
			
			// Mark connection as used
			connection.setLastUsed(System.currentTimeMillis());
			usedConnections.offer(connection);
			
			return connection.getConnection();
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
			throw new SQLException("Interrupted while waiting for connection", e);
		}
	}
	
	/* Return a connection to the pool
	 * @param connection the connection to return
	 * */
	public void returnConnection(Connection connection) {
		if (connection == null || isShutdown) {
			return;
		}
		
		connectionsReturned.incrementAndGet();
		
		try {
			// Find the pooled connection wrapper
			PooledConnection pooledConnection = findPooledConnection(connection);
			
			if (pooledConnection != null) {
				// Remove rom used connections
				usedConnections.remove(pooledConnection);
				
				// Reset connection state
				resetConnection(connection);
				
				// Return to available pool
				pooledConnection.setLastUsed(System.currentTimeMillis());
				availableConnections.offer(pooledConnection);
				
				logger.debug("Connection returned to pool");
			} else {
				logger.warn("Attempted to return unknown connection to pool");
			}
		} catch (Exception e) {
			logger.error("Error returning connection to pool: " + e.getMessage());
			// If there's an error, destroy the connection
			destroyConnection(findPooledConnection(connection));
		}
	}
	
	/* Create a new database connection
	 * @return new pooled connection
	 * */
	private PooledConnection createConnection() {
		try {
			Connection connection = DriverManager.getConnection(jdbcUrl, connectionProperties);
			return new PooledConnection(connection, System.currentTimeMillis());
		} catch (SQLException e) {
			logger.error("Failed to create database connection: " + e.getMessage());
			return null;
		}
	}
	
	/* Validate if a connection is still valid
	 * @param connection the connection to validate
	 * @return true if valid, false otherwise
	 * */
	private boolean isConnectionValid(PooledConnection connection) {
		try {
			return connection.getConnection().isValid(5); // 5 second timeout
		} catch (SQLException e) {
			logger.debug("Connection validation failed: " + e.getMessage());
			return false;
		}
	}
	
	/* Reset connection stats (rollback transactions, reset autocommit, etc.)
	 * @param connection the connection to reset
	 * */
	private void resetConnection(Connection connection) {
		try {
			if (!connection.getAutoCommit()) {
				connection.rollback();
				connection.setAutoCommit(true);
			}
		} catch (SQLException e) {
			logger.warn("Failed to reset connection state: " + e.getMessage());
		}
	}
	
	/* Find the pooled connection wrapper for a given connection
	 * @param connection the connection to find
	 * @return the pooled connection wrapper
	 * */
	private PooledConnection findPooledConnection(Connection connection) {
		for (PooledConnection pooledConn : usedConnections) {
			if (pooledConn.getInstance().equals(connection)) {
				return pooledConn;
			}
		}
		return null;
	}
	
	/* Destroy a connection and remove it from the pool
	 * @param connection the connection to destroy
	 * */
	private void destroyConnection(PooledConnection connection) {
		if (connection == null) {
			return;
		}
		
		try {
			connection.getConnection().close();
			totalConnections.decrementAndGet();
			connectionsDestroyed.incrementAndGet();
			
			// Remove from both queues
			availableConnections.remove(connection);
			usedConnections.remove(connection);
		
			logger.debug("Connection destroyed");
			
		} catch (SQLException e) {
			logger.error("Error destroying connection: " + e.getMessage());
		}
	}
	
	/* Start the maintenance thread for connection cleanup
	 * */
	private void startMaintenanceThread() {
		maintenanceThread = new Thread(() -> {
			logger.info("Starting connection pool maintenance thread");
			
			while (!isShutdown) {
				try {
					Thread.sleep(validationInterval);
					performMaintenance();
				} catch (InterruptedException e) {
					Thread.currentThread().interrupt();
					break;
				} catch (Exception e) {
					logger.error("Error in maintenance thread: " + e.getMessage());
				}
			}
			
			logger.info("Connection pool maintenance thread stopped");
		});
		
		maintenanceThread.setDaemon(true);
		maintenanceThread.setName("ConnectionPool-Maintenance");
		maintenanceThread.start();
	}
	
	/* Perform maintenance tasks on the connection pool
	 * */
	private void performMaintenance() {
		logger.debug("Performing connection pool maintenance");
		
		long currentTime = System.currentTimeMillis();
		
		// Remove idle connections
		availableConnections.removeIf(connection -> {
			if (currentTime - connection.getLastUsed() > maxIdleTime) {
				destroyConnection(connection);
				return true;
			}
			return false;
		});
		
		
		// Validate available connections
		availableConnections.removeIf(connection -> {
			if (!isConnectionValid(connection)) {
				destroyConnection(connection);
				return true;
			}
			return false;
		});
		
		// Ensure minimum connections
		while (totalConnections.get() < initialSize) {
			PooledConnection connection = createConnection();
			if (connection != null) {
				availableConnections.offer(connection);
				totalConnections.incrementAndGet();
				connectionsCreated.incrementAndGet();
			} else {
				break;
			}
		}
		
		logger.debug("Maintenance completed. Available: " + availableConnections.size() +
				", Used: " + usedConnections.size() + ", Total: " + totalConnections.get());
	}
	
	/* Shutdown the connection pool
	 * */
	public void shutdown() {
		if (isShutdown) {
			return;
		}
		
		logger.info("Shutting down connection pool...");
		isShutdown = true;
		
		// Stop maintenance thread
		if (maintenanceThread != null) {
			maintenanceThread.interrupt();
		}
		
		// Close all connections
		closeAllConnections();
		
		logger.info("Connection pool shutdown complete");
	}
	
	/* Close all connections in the pool
	 * */
	private  void closeAllConnections() {
		// Close available connections
		while (!availableConnections.isEmpty()) {
			PooledConnection connection = availableConnections.poll();
			if (connection != null) {
				destroyConnection(connection);
			}
		}
		
		// Close used connections
		while (!usedConnections.isEmpty()) {
			PooledConnection connection = usedConnections.poll();
			if (connection != null) {
				destroyConnection(connection);
			}
		}
	}
	
	/* Get pool statistics
	 * @return pool statistics
	 * */
	public PoolStatistics getStatistics() {
		return new PoolStatistics(
				totalConnections.get(),
				availableConnections.size(),
				usedConnections.size(),
				connectionsCreated.get(),
				connectionsDestroyed.get(),
				connectionsRequested.get(),
				connectionsReturned.get()
		);
	}
	
	// Inner class representing a pooled connection
	private static class PooledConnection {
		private final Connection connection;
		private long lastUsed;
		private PooledConnection pooledConnection;
		
		public PooledConnection(Connection connection, long lastUsed) {
			this.connection = connection;
			this.lastUsed = lastUsed;
		}

		public PooledConnection getInstance() {
			if (pooledConnection != null) {
				return pooledConnection.getInstance();
			} else {
				return null;
			}
		}

		public Connection getConnection() {
			return connection;
		}
		
		public long getLastUsed() {
			return lastUsed;
		}
		
		public void setLastUsed(long lastUsed) {
			this.lastUsed = lastUsed;
		}
	}
	
	// Inner class representing pool statistics
	public static class PoolStatistics {
		private final int totalConnections;
		private final int availableConnections;
		private final int usedConnections;
		private final int connectionsCreated;
		private final int connectionsDestroyed;
		private final int connectionsRequested;
		private final int connectionsReturned;
		
		public PoolStatistics(int totalConnections, int availableConnections, int usedConnections,
							int connectionsCreated, int connectionsDestroyed,
							int connectionsRequested, int connectionsReturned) {
			this.totalConnections = totalConnections;
			this.availableConnections = availableConnections;
			this.usedConnections = usedConnections;
			this.connectionsCreated = connectionsCreated;
			this.connectionsDestroyed = connectionsDestroyed;
			this.connectionsRequested = connectionsRequested;
			this.connectionsReturned = connectionsReturned;
		}
		
		// Getters
		public int getTotalConnections() {
			return totalConnections;
		}
		
		public int getAvailableConnections() {
			return availableConnections;
		}
		
		public int getUsedConnections() {
			return usedConnections;
		}
		
		public int getConnectionsCreated() {
			return connectionsCreated;
		}
		
		public int getConnectionsDestroyed() {
			return connectionsDestroyed;
		}
		
		public int getConnectionsRequested() {
			return connectionsRequested;
		}
		
		public int getConnectionsReturned() {
			return connectionsReturned;
		}
		
		@Override
		public String toString() {
			return "PoolStatistics{" +
					"total=" + totalConnections +
					", available=" + availableConnections +
					", used=" + usedConnections +
					", created=" + connectionsCreated +
					", destroyed=" + connectionsDestroyed +
					", requested=" + connectionsRequested +
					", returned=" + connectionsReturned +
					'}';
		}
	}
}
