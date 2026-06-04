import dao.DatabaseConnection;

import java.sql.Connection;

public class TestDatabaseConnection {
    public static void main(String[] args) {
        try (Connection connection = DatabaseConnection.getConnection()) {
            System.out.println("Ket noi database thanh cong!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}