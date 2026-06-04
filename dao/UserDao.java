package dao;

import model.Admin;
import model.Bidder;
import model.Role;
import model.Seller;
import model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDao {

    public void save(User user) throws SQLException { // throw ném lỗi ra ngoài cho service xử lí
        String sql = "INSERT INTO users(username, password, role) VALUES (?, ?, ?)";

        try (
                Connection connection = DatabaseConnection.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setString(1, user.getUsername());// gán user.getUsername vào dấu ? thứ nhất
            statement.setString(2, user.getPassword());
            statement.setString(3, user.getRole().name());

            statement.executeUpdate();
        }
    }

    public User findByUsername(String username) throws SQLException {
        String sql = "SELECT * FROM users WHERE username = ?";

        try (
                Connection connection = DatabaseConnection.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setString(1, username);

            try (ResultSet resultSet = statement.executeQuery()) { // câu để SELECT hỏi db để lấy dữ liệu
                if (resultSet.next()) { //có dòng dữ liệu tiếp theo thì tạo object
                    return mapResultSetToUser(resultSet);
                }
            }
        }

        return null;
    }

    public User findById(int id) throws SQLException {
        String sql = "SELECT * FROM users WHERE id = ?";

        try ( //lấy db xong tự đóng giúp ko bị rò rỉ tài nguyên
                Connection connection = DatabaseConnection.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setInt(1, id);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return mapResultSetToUser(resultSet);
                }
            }
        }

        return null;
    }

    public boolean existsByUsername(String username) throws SQLException {// kiểm tra username có tồn tại chưa
        String sql = "SELECT id FROM users WHERE username = ?";

        try (
                Connection connection = DatabaseConnection.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setString(1, username);

            try (ResultSet resultSet = statement.executeQuery()) {
                return resultSet.next();
            }
        }
    }

    private User mapResultSetToUser(ResultSet resultSet) throws SQLException {
        int id = resultSet.getInt("id");
        String username = resultSet.getString("username");
        String password = resultSet.getString("password");
        Role role = Role.valueOf(resultSet.getString("role"));

        if (role == Role.BIDDER) {
            return new Bidder(id, username, password);
        }

        if (role == Role.SELLER) {
            return new Seller(id, username, password);
        }

        if (role == Role.ADMIN) {
            return new Admin(id, username, password);
        }

        return null;
    }
}
// luồng chạy 1. UserDao nhận username = "loc"
//2. Viết SQL: SELECT * FROM users WHERE username = ?
//3. Mở kết nối bằng DatabaseConnection
//4. Gán "loc" vào dấu ?
//5. Chạy executeQuery()
//6. Database trả kết quả
//7. Nếu có user → mapResultSetToUser()
//8. Trả về object Bidder/Seller/Admin
//9. Nếu không có → trả về null