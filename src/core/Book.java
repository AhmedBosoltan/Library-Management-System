package core;

/**
 * Represents a book in the library management system.
 * This class holds essential information about a book, including its ID, 
 * title, author, and current availability status.
 *
 * @author Ahmed
 * @version 1.0
 */
public class Book {
    private String bookId;
    private String title;
    private String author;
    private String status;

    /**
     * Constructs a new Book instance with the specified details.
     *
     * @param bookId the unique identifier for the book
     * @param title the title of the book
     * @param author the author of the book
     * @param status the current status of the book (e.g., Available, Borrowed)
     */
    public Book(String bookId, String title, String author, String status) {
        this.bookId = bookId;
        this.title = title;
        this.author = author;
        this.status = status;
    }

    /**
     * Gets the unique identifier of the book.
     *
     * @return the book ID
     */
    public String getBookId() { return bookId; }

    /**
     * Sets the unique identifier of the book.
     *
     * @param bookId the new book ID to set
     */
    public void setBookId(String bookId) { this.bookId = bookId; }

    /**
     * Gets the title of the book.
     *
     * @return the book title
     */
    public String getTitle() { return title; }

    /**
     * Sets the title of the book.
     *
     * @param title the new title to set
     */
    public void setTitle(String title) { this.title = title; }

    /**
     * Gets the author of the book.
     *
     * @return the book author
     */
    public String getAuthor() { return author; }

    /**
     * Sets the author of the book.
     *
     * @param author the new author to set
     */
    public void setAuthor(String author) { this.author = author; }

    /**
     * Gets the current status of the book.
     *
     * @return the book status
     */
    public String getStatus() { return status; }

    /**
     * Sets the current status of the book.
     *
     * @param status the new status to set
     */
    public void setStatus(String status) { this.status = status; }
}