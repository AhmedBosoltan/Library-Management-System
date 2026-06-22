package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Manages the database connectivity for the application.
 * This class provides a centralized way to establish and retrieve 
 * connections to the SQLite database.
 * * @author Ahmed
 * @version 1.0
 */
public class DBConnection {
    // سيتم إنشاء قاعدة البيانات تلقائياً باسم library.db في مجلد المشروع
    private static final String URL = "jdbc:sqlite:library.db";

    /**
     * Establishes and returns a connection to the SQLite database.
     * * @return a {@link Connection} object to the database specified by the URL.
     * @throws SQLException if a database access error occurs or the url is null.
     */
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL);
    }
}