import dao.DatabaseConnection;
import exception.InvalidBidException;
import model.Auction;
import org.junit.jupiter.api.Test;
import service.AuctionService;
import service.BidService;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class BidServiceTest{
    @Test
    void placeBid_success() throws Exception{
        prepareData();
        int auctionId=1;
        int bidderId = getBidderId();
        BidService bidService= new BidService();
        bidService.placeBid(auctionId,bidderId,new BigDecimal("700.00"));

        AuctionService auctionService= new AuctionService();
        Auction auction= auctionService.getAuctionById(auctionId);
        assertEquals(0,auction.getCurrentPrice().compareTo(new BigDecimal("700.00")));
        assertEquals(Integer.valueOf(bidderId),auction.getCurrentWinnerId());
    }
    @Test
    void placeBid_lowerPrice_shouldFail() throws Exception {
        prepareData();

        int auctionId = 1;
        int bidderId = getBidderId();

        BidService bidService = new BidService();

        assertThrows(InvalidBidException.class, () -> {
            bidService.placeBid(auctionId, bidderId, new BigDecimal("400.00"));
        });
    }

    private void prepareData() throws Exception {
        try (Connection connection = DatabaseConnection.getConnection()) {

            String insertBidderSql = """
                    INSERT OR IGNORE INTO users(username, password, role)
                    VALUES ('bidder_test', '123456', 'BIDDER')
                    """;

            try (PreparedStatement statement = connection.prepareStatement(insertBidderSql)) {
                statement.executeUpdate();
            }

            String clearBidSql = """
                    DELETE FROM bids
                    WHERE auction_id = 1
                    """;

            try (PreparedStatement statement = connection.prepareStatement(clearBidSql)) {
                statement.executeUpdate();
            }

            String updateAuctionSql = """
                    UPDATE auctions
                    SET status = 'RUNNING',
                        current_price = 500.22,
                        current_winner_id = NULL,
                        end_time = datetime('now', '+1 day')
                    WHERE id = 1
                    """;

            try (PreparedStatement statement = connection.prepareStatement(updateAuctionSql)) {
                statement.executeUpdate();
            }
        }
    }

    private int getBidderId() throws Exception {
        String sql = "SELECT id FROM users WHERE username = 'bidder_test'";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            if (resultSet.next()) {
                return resultSet.getInt("id");
            }
        }

        throw new RuntimeException("Khong tim thay bidder_test");
    }
}
