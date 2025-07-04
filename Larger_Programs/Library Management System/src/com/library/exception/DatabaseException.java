package com.library.exception;

/*
 * Custom exception class for database-related errors.
 * This is a checked exception that should be used to wrap database operations that may fail.
 * */
@SuppressWarnings("serial")
public class DatabaseException extends Exception {

	@SuppressWarnings("unused")
	private static final long SerialVersionUID = 1L;
	
	/*
	 * Construct a new DatabaseException with no detail message.
	 * */
	public DatabaseException() {
		super();
	}
	
	/*
	 * Constructs a new DatabaseException with the specified detail message.
	 * 
	 * @param message the detail message
	 * */
	public DatabaseException(String message) {
		super(message);
	}
	
	/*
	 * Constructs a new DatabaseException with the specified detail message and cause.
	 * 
	 * @param message the detail message
	 * @param cause the cause of the exception
	 * */
	public DatabaseException(String message, Throwable cause) {
		super(message, cause);
	}
	
	/*
	 * Constructs a new DatabaseException with the specified detail message and cause.
	 * 
	 * @param cause the cause of the exception
	 * */
	public DatabaseException(Throwable cause) {
		super(cause);
	}
	
	/*
	 * Constructs a new DatabaseException with the specified detail message, cause, suppression enabled or disabled, and writable
	 * stack tree
	 * enabled or disabled
	 * 
	 * @param message the detail message
	 * @param cause the cause of the exception
	 * @param enableSuppression whether or not suppression is enabled
	 * @param writableStackTrace whether or not the stack trace should be writable
	 * */
	protected DatabaseException(String message, Throwable cause,
						boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}
	
}
