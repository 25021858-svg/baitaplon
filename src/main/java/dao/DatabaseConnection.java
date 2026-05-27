package dao;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
public class DatabaseConnection {
    private static final String URL = "jdbc:sqlite:database/auction.db";

    private DatabaseConnection() {
    }// không cho tạo object từ class này

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL); // DriverManager kết nối database theo URL
    }
}