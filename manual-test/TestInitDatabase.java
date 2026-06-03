import dao.DatabaseConnection;

import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.Statement;

public class TestInitDatabase {
    public static void main(String[] args) {
        runSqlFile("database/schema.sql");
        runSqlFile("database/seed.sql");

        System.out.println("Da tao lai database thanh cong!");
    }

    private static void runSqlFile(String filePath) {
        try (Connection connection = DatabaseConnection.getConnection();
             Statement statement = connection.createStatement()) {

            String sql = Files.readString(Path.of(filePath));
            String[] commands = sql.split(";");

            for (String command : commands) {
                String trimmed = command.trim();

                if (!trimmed.isEmpty()) {
                    statement.execute(trimmed);
                }
            }

        } catch (Exception e) {
            System.out.println("Loi khi chay file: " + filePath);
            e.printStackTrace();
        }
    }
}