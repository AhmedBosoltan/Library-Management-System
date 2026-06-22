package gui;

import database.DatabaseManager;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

/**
 * Main user interface frame for the Library Management System.
 * This class provides a tabbed graphical interface built using Java Swing,
 * featuring panels for librarian inventory control, borrower registration, 
 * and active loans tracking.
 * * @author Ahmed
 * @version 1.0
 */
public class MainFrame extends JFrame {
    private DefaultTableModel bookInventoryModel, borrowSearchModel, activeLoansModel;
    private JTable inventoryTable, borrowSearchTable, loansTable;
    
    private JTextField txtTitle, txtAuthor;
    private int selectedBookId = -1;
    private boolean isUpdatingSelection = false;

    /**
     * Constructs a new MainFrame window.
     * Initializes the UI layout, titles, dimensions, creates tabs,
     * and performs the initial data sync from the database.
     */
    public MainFrame() {
        setTitle("Library Management System");
        setSize(950, 650);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        this.applyComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);

        JTabbedPane mainTabs = new JTabbedPane();

        mainTabs.addTab("Librarian Inventory Control", createPageOne());
        mainTabs.addTab("Borrower Registration Desk", createPageTwo());
        mainTabs.addTab("Active Book Loans Tracker", createPageThree());

        add(mainTabs);

        // تحميل البيانات الأولية وتحديث الجداول فوراً عند الإقلاع
        refreshAllTables();
    }

    /**
     * Creates and configures the Librarian Inventory Control panel (Tab 1).
     * This panel allows the librarian to add, update, and delete books.
     * * @return a {@link JPanel} representing the inventory control interface.
     */
    private JPanel createPageOne() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel topInputs = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
        txtTitle = new JTextField(18);
        txtAuthor = new JTextField(18);
        
        topInputs.add(new JLabel("Book Title:"));
        topInputs.add(txtTitle);
        topInputs.add(new JLabel("Book Author:"));
        topInputs.add(txtAuthor);
        panel.add(topInputs, BorderLayout.NORTH);

        String[] columns = {"Book ID", "Title", "Author"};
        bookInventoryModel = new DefaultTableModel(columns, 0);
        inventoryTable = new JTable(bookInventoryModel);
        panel.add(new JScrollPane(inventoryTable), BorderLayout.CENTER);

        JPanel bottomButtons = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        JButton btnSave = new JButton("Save Book");
        JButton btnUpdate = new JButton("Update Selected");
        JButton btnDelete = new JButton("Delete Book");
        bottomButtons.add(btnSave);
        bottomButtons.add(btnUpdate);
        bottomButtons.add(btnDelete);
        panel.add(bottomButtons, BorderLayout.SOUTH);

        // معالج اختيار الصفوف لتجنب تعليق الواجهة
        inventoryTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && !isUpdatingSelection) {
                int row = inventoryTable.getSelectedRow();
                if (row != -1) {
                    selectedBookId = Integer.parseInt(inventoryTable.getValueAt(row, 0).toString());
                    txtTitle.setText(inventoryTable.getValueAt(row, 1).toString());
                    txtAuthor.setText(inventoryTable.getValueAt(row, 2).toString());
                }
            }
        });

        // زر الحفظ
        btnSave.addActionListener(e -> {
            try {
                String title = txtTitle.getText().trim();
                String author = txtAuthor.getText().trim();
                if(title.isEmpty() || author.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Please fill all book fields!");
                    return;
                }
                DatabaseManager.addBook(title, author);
                clearInputs();
                refreshAllTables(); 
                JOptionPane.showMessageDialog(this, "Book added successfully!");
            } catch (SQLException ex) { JOptionPane.showMessageDialog(this, "Error saving book."); }
        });

        // زر التعديل والتحديث اللحظي المباشر
        btnUpdate.addActionListener(e -> {
            try {
                if (selectedBookId == -1) {
                    JOptionPane.showMessageDialog(this, "Please select a book from the table first!");
                    return;
                }
                String title = txtTitle.getText().trim();
                String author = txtAuthor.getText().trim();
                if(title.isEmpty() || author.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Please fill all book fields!");
                    return;
                }
                
                DatabaseManager.updateBook(selectedBookId, title, author);
                
                isUpdatingSelection = true;
                inventoryTable.clearSelection();
                refreshAllTables(); 
                clearInputs();
                isUpdatingSelection = false;
                
                JOptionPane.showMessageDialog(this, "Book updated successfully!");
            } catch (SQLException ex) { 
                isUpdatingSelection = false;
                JOptionPane.showMessageDialog(this, "Error updating book."); 
            }
        });

        // زر الحذف والتحديث اللحظي المباشر
        btnDelete.addActionListener(e -> {
            try {
                if (selectedBookId == -1) {
                    JOptionPane.showMessageDialog(this, "Please select a book from the table first!");
                    return;
                }
                
                DatabaseManager.deleteBook(selectedBookId);
                
                isUpdatingSelection = true;
                inventoryTable.clearSelection();
                refreshAllTables(); 
                clearInputs();
                isUpdatingSelection = false;
                
                JOptionPane.showMessageDialog(this, "Book deleted successfully!");
            } catch (SQLException ex) { 
                isUpdatingSelection = false;
                JOptionPane.showMessageDialog(this, "Error deleting book."); 
            }
        });

        return panel;
    }

    /**
     * Creates and configures the Borrower Registration Desk panel (Tab 2).
     * This panel handles searching for books and issuing new loans with duplication checks.
     * * @return a {@link JPanel} representing the borrowing management interface.
     */
    private JPanel createPageTwo() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel topForm = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        JTextField txtRecipient = new JTextField(12);
        JTextField txtPhone = new JTextField(12);
        JTextField txtReturnDate = new JTextField("2026-07-15", 10);

        topForm.add(new JLabel("Recipient Name:")); topForm.add(txtRecipient);
        topForm.add(new JLabel("Phone:")); topForm.add(txtPhone);
        topForm.add(new JLabel("Return Date (YYYY-MM-DD):")); topForm.add(txtReturnDate);

        JPanel searchBar = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        JTextField txtSearch = new JTextField(20);
        JButton btnSearch = new JButton("Search Book");
        searchBar.add(new JLabel("Search Inventory:"));
        searchBar.add(txtSearch); searchBar.add(btnSearch);

        JPanel combinedTop = new JPanel(new GridLayout(2, 1));
        combinedTop.add(topForm);
        combinedTop.add(searchBar);
        panel.add(combinedTop, BorderLayout.NORTH);

        String[] columns = {"Book ID", "Title", "Author"};
        borrowSearchModel = new DefaultTableModel(columns, 0);
        borrowSearchTable = new JTable(borrowSearchModel);
        panel.add(new JScrollPane(borrowSearchTable), BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton btnBorrow = new JButton("Issue Borrow Loan");
        bottomPanel.add(btnBorrow);
        panel.add(bottomPanel, BorderLayout.SOUTH);

        btnSearch.addActionListener(e -> {
            try {
                List<Object[]> results = DatabaseManager.searchBooks(txtSearch.getText().trim());
                borrowSearchModel.setRowCount(0);
                for (Object[] r : results) borrowSearchModel.addRow(r);
            } catch (SQLException ex) { System.out.println("Search failed."); }
        });

        btnBorrow.addActionListener(e -> {
            int row = borrowSearchTable.getSelectedRow();
            if (row == -1) { 
                JOptionPane.showMessageDialog(this, "Please select a book from the table first!"); 
                return; 
            }
            String name = txtRecipient.getText().trim();
            String phone = txtPhone.getText().trim();
            String date = txtReturnDate.getText().trim();
            String bookTitle = borrowSearchModel.getValueAt(row, 1).toString();

            if (name.isEmpty() || phone.isEmpty() || date.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please fill all member fields!");
                return;
            }

            try {
                DatabaseManager.issueLoan(name, phone, bookTitle, date);
                JOptionPane.showMessageDialog(this, "Book borrowed successfully!");
                txtRecipient.setText(""); txtPhone.setText("");
                refreshAllTables(); 
            } catch (SQLException ex) { 
                // التقاط نص الاستعارة المكررة وإظهار الرسالة المطلوبة بالعربية
                if ("ALREADY_BORROWED".equals(ex.getMessage())) {
                    JOptionPane.showMessageDialog(this, "This book is currently borrowed", "System Warning", JOptionPane.WARNING_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(this, "Error establishing loan.");
                }
            }
        });

        return panel;
    }

    /**
     * Creates and configures the Active Book Loans Tracker panel (Tab 3).
     * This panel monitors currently active loans and allows confirming returns.
     * * @return a {@link JPanel} representing the active loans monitoring interface.
     */
    private JPanel createPageThree() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        String[] columns = {"Loan ID", "Book Title", "Borrower Name", "Phone Number", "Return Due Date"};
        activeLoansModel = new DefaultTableModel(columns, 0);
        loansTable = new JTable(activeLoansModel);
        panel.add(new JScrollPane(loansTable), BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton btnReturn = new JButton("Confirm Book Return & Clear Tracker Record");
        bottomPanel.add(btnReturn);
        panel.add(bottomPanel, BorderLayout.SOUTH);

        btnReturn.addActionListener(e -> {
            int row = loansTable.getSelectedRow();
            if (row == -1) { JOptionPane.showMessageDialog(this, "Please select a book from the table first!"); return; }
            int id = Integer.parseInt(activeLoansModel.getValueAt(row, 0).toString());
            try {
                DatabaseManager.returnLoan(id);
                JOptionPane.showMessageDialog(this, "Book returned successfully!");
                refreshAllTables(); 
            } catch (SQLException ex) { System.out.println("Return handling failed."); }
        });

        return panel;
    }

    /**
     * Clears the entry text fields for book title and author,
     * and resets the tracking ID for the selected book.
     */
    private void clearInputs() {
        txtTitle.setText("");
        txtAuthor.setText("");
        selectedBookId = -1; 
    }

    /**
     * Synchronizes and updates all UI tables simultaneously and safely
     * on the Event Dispatch Thread (EDT). Fetches latest state from database.
     */
    private void refreshAllTables() {
        SwingUtilities.invokeLater(() -> {
            try {
                List<Object[]> books = DatabaseManager.searchBooks("");
                bookInventoryModel.setRowCount(0); 
                borrowSearchModel.setRowCount(0); 
                for (Object[] b : books) {
                    bookInventoryModel.addRow(b); 
                    borrowSearchModel.addRow(b);
                }
                
                List<Object[]> loans = DatabaseManager.getAllLoans();
                activeLoansModel.setRowCount(0); 
                for (Object[] l : loans) {
                    activeLoansModel.addRow(l); 
                }
            } catch (Exception ex) { 
                System.out.println("Error updating UI components dynamically."); 
            }
        });
    }

    /**
     * Main application entry point.
     * Initializes the database architecture first, then launches the Graphical UI.
     * * @param args command-line arguments (not used).
     */
    public static void main(String[] args) {
        try { DatabaseManager.initializeDatabase(); } catch (Exception e) { return; }
        SwingUtilities.invokeLater(() -> new MainFrame().setVisible(true));
    }
}