package database;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Manages all database operations for the library management system.
 * This utility class handles database initialization, CRUD operations for books, 
 * and loan transaction management (borrowing and returning books).
 *
 * @author Ahmed
 * @version 1.0
 */
public class DatabaseManager {

    /**
     * Initializes the database by creating the required tables ('books' and 'loans') 
     * if they do not already exist.
     *
     * @throws SQLException if a database access error occurs or the SQL statements fail
     */
    public static void initializeDatabase() throws SQLException {
        String booksTable = "CREATE TABLE IF NOT EXISTS books (book_id INTEGER PRIMARY KEY AUTOINCREMENT, title TEXT, author TEXT);";
        String loansTable = "CREATE TABLE IF NOT EXISTS loans (loan_id INTEGER PRIMARY KEY AUTOINCREMENT, borrower_name TEXT, borrower_phone TEXT, book_title TEXT, return_date TEXT);";
        
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.execute(booksTable);
            stmt.execute(loansTable);
        }
    }

    /**
     * Adds a new book record to the database with the specified title and author.
     *
     * @param title the title of the book to be added
     * @param author the author of the book to be added
     * @throws SQLException if a database access error occurs or the insertion fails
     */
    public static void addBook(String title, String author) throws SQLException {
        String sql = "INSERT INTO books (title, author) VALUES (?, ?)";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, title);
            pstmt.setString(2, author);
            pstmt.executeUpdate();
        }
    }

    /**
     * Updates the details of an existing book in the database based on its ID.
     *
     * @param id the unique ID of the book to update
     * @param title the new title to be set
     * @param author the new author to be set
     * @throws SQLException if a database access error occurs or the update fails
     */
    public static void updateBook(int id, String title, String author) throws SQLException {
        String sql = "UPDATE books SET title = ?, author = ? WHERE book_id = ?";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, title);
            pstmt.setString(2, author);
            pstmt.setInt(3, id);
            pstmt.executeUpdate();
        }
    }

    /**
     * Deletes a book record from the database based on its ID.
     *
     * @param id the unique ID of the book to delete
     * @throws SQLException if a database access error occurs or the deletion fails
     */
    public static void deleteBook(int id) throws SQLException {
        String sql = "DELETE FROM books WHERE book_id = ?";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        }
    }

    /**
     * Searches for books whose title or author matches the specified keyword.
     *
     * @param keyword the search term used to match against book titles or authors
     * @return a list of Object arrays, where each array contains the book's ID, title, and author
     * @throws SQLException if a database access error occurs or the query fails
     */
    public static List<Object[]> searchBooks(String keyword) throws SQLException {
        List<Object[]> books = new ArrayList<>();
        String sql = "SELECT book_id, title, author FROM books WHERE title LIKE ? OR author LIKE ?";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, "%" + keyword + "%");
            pstmt.setString(2, "%" + keyword + "%");
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    books.add(new Object[]{rs.getInt("book_id"), rs.getString("title"), rs.getString("author")});
                }
            }
        }
        return books;
    }

    /**
     * Issues a book loan to a borrower after checking if the book is already borrowed.
     *
     * @param name the name of the borrower
     * @param phone the contact phone number of the borrower
     * @param title the title of the book being borrowed
     * @param date the due date or return date for the loan
     * @throws SQLException if a database access error occurs, or if the book is already borrowed 
     * (throws an exception with the message "ALREADY_BORROWED")
     */
    public static void issueLoan(String name, String phone, String title, String date) throws SQLException {
        String checkSql = "SELECT COUNT(*) FROM loans WHERE book_title = ?";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement checkPstmt = conn.prepareStatement(checkSql)) {
            checkPstmt.setString(1, title);
            try (ResultSet rs = checkPstmt.executeQuery()) {
                if (rs.next() && rs.getInt(1) > 0) {
                    // نلقي استثناء يحمل نصاً برمجياً مخصصاً ليتم التقاطه في الواجهة
                    throw new SQLException("ALREADY_BORROWED");
                }
            }
        }

        String sql = "INSERT INTO loans (borrower_name, borrower_phone, book_title, return_date) VALUES (?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, name);
            pstmt.setString(2, phone);
            pstmt.setString(3, title);
            pstmt.setString(4, date);
            pstmt.executeUpdate();
        }
    }

    /**
     * Returns a borrowed book by removing its corresponding loan record using the loan ID.
     *
     * @param loanId the unique ID of the loan record to delete
     * @throws SQLException if a database access error occurs or the deletion fails
     */
    public static void returnLoan(int loanId) throws SQLException {
        String sql = "DELETE FROM loans WHERE loan_id = ?";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, loanId);
            pstmt.executeUpdate();
        }
    }

    /**
     * Retrieves all current loan records from the database.
     *
     * @return a list of Object arrays, each representing a loan record with loan ID, 
     * book title, borrower name, borrower phone, and return date
     * @throws SQLException if a database access error occurs or the query fails
     */
    public static List<Object[]> getAllLoans() throws SQLException {
        List<Object[]> loans = new ArrayList<>();
        String sql = "SELECT loan_id, book_title, borrower_name, borrower_phone, return_date FROM loans";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                loans.add(new Object[]{
                    rs.getInt("loan_id"),
                    rs.getString("book_title"),
                    rs.getString("borrower_name"),
                    rs.getString("borrower_phone"),
                    rs.getString("return_date")
                });
            }
        }
        return loans;
    }
}