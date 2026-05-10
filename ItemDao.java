package dao;
import model.Art;
import model.Electronics;
import model.Item;
import model.ItemType;
import model.Vehicle;
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
        ItemType itemType = ItemType.valueOf(resultSet.getString("item_type"));
        LocalDateTime createdAt = LocalDateTime.parse(resultSet.getString("created_at"));
        if (itemType == ItemType.ELECTRONICS) {
            return new Electronics(id, sellerId, name, description, startingPrice, createdAt);
        }
        if (itemType == ItemType.ART) {
            return new Art(id, sellerId, name, description, startingPrice, createdAt);
        }

        if (itemType == ItemType.VEHICLE) {
            return new Vehicle(id, sellerId, name, description, startingPrice, createdAt);
        }

        return null;
    }
}