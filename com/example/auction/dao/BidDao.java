package com.example.auction.dao;
import com.example.auction.model.BidTransaction;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class BidDao{
    public void save(BidTransaction bid) throws SQLException{
        String sql= """
                INSERT INTO bids(auction_id, bidder_id,bid_amount,bid_time)
                VALUES(?,?,?,?)
                """;
        try(
                Connection connection=DatabaseConnection.getConnection();
                PreparedStatement statement=connection.prepareStatement(sql)
                ){
            statement.setInt(1,bid.getAuctionId());
            statement.setInt(2,bid.getBidderId());
            statement.setBigDecimal(3,bid.getAmount());
            statement.setString(4,bid.getBidTime().toString());
            statement.executeUpdate();
        }

    }
    public List<BidTransaction> findAuctionId(int auctionId) throws SQLException{
        String sql= """
                SELECT*FROM bids
                WHERE auctions_id=?
                ORDER BY bid_time ASC
                """;
        List<BidTransaction> bids= new ArrayList<>();
    try(
            Connection connection=DatabaseConnection.getConnection();
            PreparedStatement statement=connection.prepareStatement(sql)
            ){
        statement.setInt(1,auctionId);
        try(ResultSet resultSet=statement.executeQuery()){
            while(resultSet.next()){
                bids.add(mapResultSetToBid(resultSet));
            }
        }
    }
    return bids;

}
public BidTransaction findHighestBidByAuctionId(int auctionId) throws SQLException{
        String sql= """
                SELECT*FROM bids
                WHERE auction_id=?
                ORDER BY bid_amount DeSc
                LIMIT 1
                """;
        try(
                Connection connection=DatabaseConnection.getConnection();
                PreparedStatement statement=connection.prepareStatement(sql)
                ){
            try(ResultSet resultSet=statement.executeQuery()){
                if(resultSet.next()){
                    return mapResultSetToBid(resultSet);
                }
            }
        }
        return null;
}
private BidTransaction mapResultSetToBid(ResultSet resultSet) throws SQLException{
        int id=resultSet.getInt("id");
        int auctionId=resultSet.getInt("auctionId");
        int bidderId=resultSet.getInt("bidderId");
        BigDecimal bid_amount=resultSet.getBigDecimal("bid_amount");
        LocalDateTime bidTime=LocalDateTime.parse(resultSet.getString("bidTime"));
        return new BidTransaction(id,auctionId,bidderId,bid_amount,bidTime);
}
}
