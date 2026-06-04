import com.example.auction.dao.DatabaseConnection;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class TestShowTables {
    public static void main(String[] args) {
        System.out.println("Working dir: " + System.getProperty("user.dir"));

        try (Connection connection = DatabaseConnection.getConnection();
             Statement statement = connection.createStatement()) {

            ResultSet resultSet = statement.executeQuery(
                    "SELECT name FROM sqlite_master WHERE type='table'"
            );

            System.out.println("Danh sach bang trong database:");

            while (resultSet.next()) {
                System.out.println(resultSet.getString("name"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}