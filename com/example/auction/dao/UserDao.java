package com.example.auction.dao;

import com.example.auction.model.Admin;
import com.example.auction.model.Bidder;
import com.example.auction.model.Role;
import com.example.auction.model.Seller;
import com.example.auction.model.User;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDao {

    public void save(User user) throws SQLException {
        String sql = "INSERT INTO users(username, password, role) VALUES (?, ?, ?)";

        try (
                Connection connection = DatabaseConnection.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setString(1, user.getUsername());
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

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return mapResultSetToUser(resultSet);
                }
            }
        }
        return null;
    }

    public User findById(int id) throws SQLException {
        String sql = "SELECT * FROM users WHERE id = ?";

        try (
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

    public boolean existsByUsername(String username) throws SQLException {
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

    /**
     * Lay so du balance cua nguoi dung tu database
     */
    public BigDecimal getBalance(int userId) throws SQLException {
        String sql = "SELECT balance FROM users WHERE id = ?";
        try (
                Connection connection = DatabaseConnection.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setInt(1, userId);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    BigDecimal balance = resultSet.getBigDecimal("balance");
                    return balance != null ? balance : BigDecimal.ZERO;
                }
            }
        }
        return BigDecimal.ZERO;
    }

    /**
     * Cap nhat thay doi so du balance cua nguoi dung vao database
     */
    public void depositMoney(int userId, BigDecimal amount) throws SQLException {
        String sql = "UPDATE users SET balance = balance + ? WHERE id = ?";
        try (
                Connection connection = DatabaseConnection.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setBigDecimal(1, amount);
            statement.setInt(2, userId);
            statement.executeUpdate();
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