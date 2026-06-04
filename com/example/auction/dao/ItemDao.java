package com.example.auction.dao;
import com.example.auction.model.Art;
import com.example.auction.model.Electronics;
import com.example.auction.model.Item;
import com.example.auction.model.ItemType;
import com.example.auction.model.Vehicle;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ItemDao {
    public void save(Item item) throws SQLException {
        String sql = """
                INSERT INTO items(seller_id, name, description, starting_price, item_type, created_at)
                VALUES (?, ?, ?, ?, ?, ?)
                """;
        try (
                Connection connection = DatabaseConnection.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setInt(1, item.getSellerId());
            statement.setString(2, item.getName());
            statement.setString(3, item.getDescription());
            statement.setBigDecimal(4, item.getStartingPrice());
            statement.setString(5, item.getItemType().name());
            statement.setString(6, item.getCreatedAt().toString());

            statement.executeUpdate();


        }
    }

    public Item findById(int id) throws SQLException {
        String sql = "SELECT*FROM items WHERE id=?";
        try (
                Connection connection = DatabaseConnection.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setInt(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return mapResultSetToItem(resultSet);
                }
            }
        }
        return null;
    }

    public List<Item> findAll() throws SQLException {
        String sql = "SELECT*FROM items";
        List<Item> items = new ArrayList<>();
        try (
                Connection connection = DatabaseConnection.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql);
                ResultSet resultSet = statement.executeQuery()
        ) {
            while (resultSet.next()) {
                items.add(mapResultSetToItem(resultSet));
            }
        }
        return items;
    }

    public List<Item> findBySellerId(int sellerId) throws SQLException {
        String sql = "SELECT*FROM items WHERE seller_id=?";
        List<Item> items = new ArrayList<>();
        try (
                Connection connection = DatabaseConnection.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setInt(1, sellerId);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    items.add(mapResultSetToItem(resultSet));
                }
            }
        }
        return items;
    }

    public void update(Item item) throws SQLException {
        String sql = """
                UPDATE items
                SET name=?, description=?, starting_price=?, item_type = ?
                WHERE id=?
                """;
        try (
                Connection connection = DatabaseConnection.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setString(1, item.getName());
            statement.setString(2, item.getDescription());
            statement.setBigDecimal(3, item.getStartingPrice());
            statement.setString(4, item.getItemType().name());
            statement.setInt(5, item.getId());

            statement.executeUpdate();
        }
    }

    public void deleteById(int id) throws SQLException {
        String sql = "DELETE FROM items WHERE id=?";
        try (
                Connection connection = DatabaseConnection.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setInt(1, id);
            statement.executeUpdate();
        }
    }

    private Item mapResultSetToItem(ResultSet resultSet) throws SQLException {
        int id = resultSet.getInt("id");
        int sellerId = resultSet.getInt("seller_id");
        String name = resultSet.getString("name");
        String description = resultSet.getString("description");
        BigDecimal startingPrice = resultSet.getBigDecimal("starting_price");

        // 🛡️ BẮT LỖI PHÒNG VỆ: Nếu DB chưa kịp thêm cột item_type, mặc định coi là ELECTRONICS
        ItemType itemType = ItemType.ELECTRONICS;
        try {
            String typeStr = resultSet.getString("item_type");
            if (typeStr != null) {
                itemType = ItemType.valueOf(typeStr);
            }
        } catch (SQLException e) {
            System.out.println("[⚠️ CẢNH BÁO] Không tìm thấy cột item_type trong DB, tự động dùng mặc định ELECTRONICS");
        }

        // 🛡️ XỬ LÝ DATE AN TOÀN: SQLite lưu '2025-12-13' thì chuyển thành dạng LocalDateTime hợp lệ
        String createdAtStr = resultSet.getString("created_at");
        LocalDateTime createdAt;
        try {
            if (createdAtStr != null && createdAtStr.length() == 10) { // Định dạng yyyy-MM-dd
                createdAt = java.time.LocalDate.parse(createdAtStr).atStartOfDay();
            } else {
                createdAt = LocalDateTime.parse(createdAtStr.replace(" ", "T"));
            }
        } catch (Exception e) {
            createdAt = LocalDateTime.now(); // Tránh sập app
        }

        if (itemType == ItemType.ELECTRONICS) {
            return new Electronics(id, sellerId, name, description, startingPrice, itemType, createdAt);
        }
        if (itemType == ItemType.ART) {
            return new Art(id, sellerId, name, description, startingPrice, itemType, createdAt);
        }
        if (itemType == ItemType.VEHICLE) {
            return new Vehicle(id, sellerId, name, description, startingPrice, itemType, createdAt);
        }

        return null;
    }
}