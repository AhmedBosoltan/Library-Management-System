package core;

/**
 * Represents a library transaction within the library management system.
 * This class records the details of borrowing and returning activities, 
 * linking a specific member to a specific book along with the respective dates.
 *
 * @author Ahmed
 * @version 1.0
 */
public class Transaction {
    private String transactionId;
    private String memberId;
    private String bookId;
    private String borrowDate;
    private String returnDate;

    /**
     * Constructs a new Transaction instance with all the required record details.
     *
     * @param transactionId the unique identifier for the transaction
     * @param memberId the unique identifier of the member involved in the transaction
     * @param bookId the unique identifier of the book being borrowed or returned
     * @param borrowDate the date when the book was borrowed
     * @param returnDate the date when the book was returned (or expected to be returned)
     */
    public Transaction(String transactionId, String memberId, String bookId, String borrowDate, String returnDate) {
        this.transactionId = transactionId;
        this.memberId = memberId;
        this.bookId = bookId;
        this.borrowDate = borrowDate;
        this.returnDate = returnDate;
    }

    /**
     * Gets the unique identifier of the transaction.
     *
     * @return the transaction ID
     */
    public String getTransactionId() { return transactionId; }

    /**
     * Gets the unique identifier of the member associated with this transaction.
     *
     * @return the member ID
     */
    public String getMemberId() { return memberId; }

    /**
     * Gets the unique identifier of the book associated with this transaction.
     *
     * @return the book ID
     */
    public String getBookId() { return bookId; }

    /**
     * Gets the date when the book was borrowed.
     *
     * @return the borrow date
     */
    public String getBorrowDate() { return borrowDate; }

    /**
     * Gets the date when the book was returned.
     *
     * @return the return date
     */
    public String getReturnDate() { return returnDate; }
}