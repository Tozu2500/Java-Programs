package com.library.model;

import java.time.LocalDate;
import java.util.Objects;

/*
 * Book class for Library Management System
 * Represents a book with its properties and operations
 * 
 * @author Tozu
 * @version 1.0.0
 * */
public class Book {

	// Book attributes
	private String isbn;
	private String title;
	private String author;
	private String publisher;
	private int publicationYear;
	private String genre;
	private int totalCopies;
	private int availableCopies;
	private double price;
	private String description;
	private LocalDate dateAdded;
	private boolean isAvailable;
	
	// Constructors
	public Book() {
		this.dateAdded = LocalDate.now();
		this.isAvailable = true;
	}
	
	public Book(String isbn, String title, String author, String publisher,
			int publicationYear, String genre, int totalCopies, double price) {
		
		this.isbn = isbn;
		this.title = title;
		this.author = author;
		this.publisher = publisher;
		this.publicationYear = publicationYear;
		this.genre = genre;
		this.totalCopies = totalCopies;
		this.availableCopies = totalCopies;
		this.price = price;
		this.dateAdded = LocalDate.now();
		this.isAvailable = totalCopies > 0;
	}
	
	// Getters
	public String getIsbn() {
		return isbn;
	}
	
	public String getTitle() {
		return title;
	}
	
	public String getAuthor() {
		return author;
	}
	
	public String getPublisher() {
		return publisher;
	}
	
	public int getPublicationYear() {
		return publicationYear;
	}
	
	public String getGenre() {
		return genre;
	}
	
	public int getTotalCopies() {
		return totalCopies;
	}
	
	public int getAvailableCopies() {
		return availableCopies;
	}
	
	public double getPrice() {
		return price;
	}
	
	public String getDescription() {
		return description;
	}
	
	public LocalDate getDateAdded() {
		return dateAdded;
	}
	
	public boolean isAvailable() {
		return isAvailable && availableCopies > 0;
	}
	
	// Setters
	public void setIsbn(String isbn) {
		this.isbn = isbn;
	}
	
	public void setTitle(String title) {
		this.title = title;
	}
	
	public void setAuthor(String author) {
		this.author = author;
	}
	
	public void setPublisher(String publisher) {
		this.publisher = publisher;
	}
	
	public void setPublicationYear(int publicationYear) {
		this.publicationYear = publicationYear;
	}
	
	public void setGenre(String genre) {
		this.genre = genre;
	}
	
	public void setTotalCopies(int totalCopies) {
		this.totalCopies = totalCopies;
		updateAvailability();
	}
	
	public void setAvailableCopies(int availableCopies) {
		this.availableCopies = Math.max(0, Math.min(availableCopies, totalCopies));
		updateAvailability();
	}
	
	public void setPrice(double price) {
		this.price = price;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
	
	public void setDateAdded(LocalDate dateAdded) {
		this.dateAdded = dateAdded;
	}
	
	// Business methods
	public boolean borrowBook() {
		if (availableCopies > 0) {
			availableCopies--;
			updateAvailability();
			return true;
		}
		return false;
	}
	
	public boolean returnBook() {
		if (availableCopies < totalCopies) {
			availableCopies++;
			updateAvailability();
			return true;
		}
		return false;
	}
	
	public void addCopies(int copies) {
		if (copies > 0) {
			totalCopies += copies;
			availableCopies += copies;
			updateAvailability();
		}
	}
	
	public void removeCopies(int copies) {
		if (copies > 0 && copies <= availableCopies) {
			totalCopies -= copies;
			availableCopies -= copies;
			updateAvailability();
		}
	}
	
	private void updateAvailability() {
		this.isAvailable = availableCopies > 0;
	}
	
	public int getBorrowedCopies() {
		return totalCopies - availableCopies;
	}
	
	public double getBorrowRate() {
		if (totalCopies == 0) return 0.0;
		return (double) getBorrowedCopies() / totalCopies * 100;
	}
	
	// Utility methods
	@Override
	public String toString() {
		return String.format("Book{isbn='%s', title='%s', author='%s', publisher='%s', " +
							"year=%d, genre='%s', availablee=%d/%d, price=%.2f}",
							isbn, title, author, publisher, publicationYear,
							genre, availableCopies, totalCopies, price);
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null || getClass() != obj.getClass()) return false;
		Book book = (Book) obj;
		return Objects.equals(isbn, book.isbn);
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(isbn);
	}
	
	// Validation methods
	public boolean isValidIsbn() {
		return isbn != null && !isbn.trim().isEmpty();
	}
	
	public boolean isValidTitle() {
		return title != null && !title.trim().isEmpty();
	}
	
	public boolean isValidAuthor() {
		return author != null && !author.trim().isEmpty();
	}
	
	public boolean isCompleteBook() {
		return isValidIsbn() && isValidTitle() && isValidAuthor() && publisher != null && publicationYear > 0 && totalCopies >= 0;
	}
}
	
