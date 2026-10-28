package org.group5.service;

import java.util.ArrayList;
import java.util.List;

import org.group5.model.Book;

public class BookService {

    private List<Book> books;

    public BookService() {
        this.books = new ArrayList<Book>();
    }

    public BookService(List<Book> books) {
        this.books = (books != null) ? books : new ArrayList<Book>();
    }

    public List<Book> getBooks() {
        return books;
    }

    public void addBook(Book book) {
        if (book == null) {
            throw new IllegalArgumentException("Book details cannot be null.");
        }
        if (book.getBookId() == null || book.getBookId().trim().isEmpty()) {
            throw new IllegalArgumentException("Book ID cannot be empty.");
        }
        if (book.getTitle() == null || book.getTitle().trim().isEmpty()) {
            throw new IllegalArgumentException("Book title cannot be empty.");
        }
        if (findBookById(book.getBookId().trim()) != null) {
            throw new IllegalArgumentException("A book with ID '" + book.getBookId().trim() + "' already exists.");
        }
        
        book.setBookId(book.getBookId().trim());
        book.setTitle(book.getTitle().trim());
        book.setAuthor(book.getAuthor() != null ? book.getAuthor().trim() : "Unknown");
        book.setCategory(book.getCategory() != null ? book.getCategory().trim() : "General");
        
        books.add(book);
    }

    
    public boolean removeBook(String bookId) {
        if (bookId == null || bookId.trim().isEmpty()) {
            throw new IllegalArgumentException("Please enter a valid Book ID to remove.");
        }
        Book book = findBookById(bookId.trim());
        if (book == null) {
            throw new IllegalArgumentException("Book with ID '" + bookId.trim() + "' not found.");
        }
        if (!book.isAvailable()) {
            throw new IllegalArgumentException("Cannot remove Book '" + book.getTitle() + "' because it is currently borrowed.");
        }
        return books.remove(book);
    }

   

    public List<Book> searchBooks(String keyword) {
        List<Book> results = new ArrayList<Book>();
        if (keyword == null || keyword.trim().isEmpty()) {
            return new ArrayList<Book>(books);
        }
        String lowerKey = keyword.trim().toLowerCase();
        for (Book b : books) {
            if (b.getBookId().toLowerCase().contains(lowerKey) ||
                b.getTitle().toLowerCase().contains(lowerKey) ||
                b.getAuthor().toLowerCase().contains(lowerKey) ||
                b.getCategory().toLowerCase().contains(lowerKey)) {
                results.add(b);
            }
        }
        return results;
    }

    

    public Book findBookById(String bookId) {
        if (bookId == null) return null;
        for (Book b : books) {
            if (b.getBookId().equalsIgnoreCase(bookId.trim())) {
                return b;
            }
        }
        return null;
    }

    public List<Book> getAllBooks() {
        return new ArrayList<Book>(books);
    }

    public List<Book> getAvailableBooks() {
        List<Book> available = new ArrayList<Book>();
        for (Book b : books) {
            if (b.isAvailable()) {
                available.add(b);
            }
        }
        return available;
    }

    public List<Book> getBorrowedBooks() {
        List<Book> borrowed = new ArrayList<Book>();
        for (Book b : books) {
            if (!b.isAvailable()) {
                borrowed.add(b);
            }
        }
        return borrowed;
    }
}
