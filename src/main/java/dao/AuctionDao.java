package dao;
import model.Auction;
import model.AuctionStatus;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class AuctionDao {
    public void save(Auction auction) throws SQLException {
        String sql = """
                INSERT INTO auctions(item_id,start_time,end_time,current_price,
                current_winner_id,status,created_at)
                VALUES(?,?,?,?,?,?,?)
                """;
        try (
                Connection connection = DatabaseConnection.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setInt(1, auction.getItemId());
            statement.setString(2, auction.getStartTime().toString());
            statement.setString(3, auction.getEndTime().toString());
            statement.setBigDecimal(4, auction.getCurrentPrice());
            if (auction.getCurrentWinnerId() == null) {
                statement.setObject(5, null);
            } else {
                statement.setInt(5, auction.getCurrentWinnerId());
            }
            statement.setString(6, auction.getStatus().name());
            statement.setString(7, auction.getCreatedAt().toString());
            statement.executeUpdate();
        }
    }

    public Auction findById(int id) throws SQLException {
        String sql = "SELECT *FROM auctions WHERE id=?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return mapResultSetToAuction(resultSet);
                }
            }
        }
        return null;

    }
    public List<Auction> findAll() throws SQLException{
        String sql= "SELECT * FROM auctions";
        List<Auction> auctions = new ArrayList<>();
        try(
                Connection connection= DatabaseConnection.getConnection();
                PreparedStatement statement=connection.prepareStatement(sql);
                ResultSet resultSet=statement.executeQuery()
                ){
            while(resultSet.next()){
                auctions.add(mapResultSetToAuction(resultSet));
            }
        }
        return auctions;

    }
    public List<Auction> findRunningAuctions() throws SQLException{
        String sql="SELECT * FROM auctions WHERE status=?";
        List<Auction> auctions = new ArrayList<>();
        try(
                Connection connection=DatabaseConnection.getConnection();
                PreparedStatement statement=connection.prepareStatement(sql)
                ){
            statement.setString(1, AuctionStatus.RUNNING.name());
            try(ResultSet resultSet=statement.executeQuery()){
                while(resultSet.next()){
                    auctions.add(mapResultSetToAuction(resultSet));
                }
            }
        }
        return auctions;
    }
    public void updateCurrentPrice(int auctionId,BigDecimal newPrice, int winnerId) throws SQLException{
        String sql= """
                UPDATE auctions
                SET current_price=?,current_winner_id=?
                WHERE id=?
                """;
        try(
                Connection connection= DatabaseConnection.getConnection();
                PreparedStatement statement=connection.prepareStatement(sql)
                ){
            statement.setBigDecimal(1,newPrice);
            statement.setInt(2,winnerId);
            statement.setInt(3,auctionId);
            statement.executeUpdate();
        }
    }
    public void updateStatus(int auctionId,AuctionStatus status) throws SQLException{
        String sql= """
                UPDATE auctions
                SET status=?
                WHERE id=?
                """;
        try(
                Connection connection=DatabaseConnection.getConnection();
                PreparedStatement statement=connection.prepareStatement(sql)
                ){
            statement.setString(1,status.name());
            statement.setInt(2,auctionId);
            statement.executeUpdate();
        }

    }
    private Auction mapResultSetToAuction ( ResultSet resultSet) throws SQLException{
        int id= resultSet.getInt("id");
        int itemId= resultSet.getInt("item_id");
        LocalDateTime startTime = parseDateTime(resultSet.getString("start_time"));
        LocalDateTime endTime = parseDateTime(resultSet.getString("end_time"));
        BigDecimal currentPrice = resultSet.getBigDecimal("current_price");
        Integer currentWinnerId=null;
        Object winnerObject=resultSet.getObject("current_winner_id");
        if(winnerObject!=null){
            currentWinnerId=resultSet.getInt("current_winner_id");

        }
        AuctionStatus status= AuctionStatus.valueOf(resultSet.getString("status"));
        LocalDateTime createdAt = parseDateTime(resultSet.getString("created_at"));
        return new Auction(id,itemId,startTime,endTime,currentPrice,currentWinnerId,status,createdAt);
    }
    private LocalDateTime parseDateTime(String value) {
        if (value == null) {
            return null;
        }

        if (value.length() == 10) {
            return LocalDateTime.parse(value + "T00:00:00");
        }

        return LocalDateTime.parse(value.replace(" ", "T"));
    }
}