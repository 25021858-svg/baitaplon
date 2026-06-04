import com.example.auction.dao.DatabaseConnection;
import com.example.auction.model.Auction;
import com.example.auction.service.AuctionService;
import com.example.auction.service.BidService;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class TestBidService {
    public static void main(String[] args) {
        try {
            prepareDataForBidTest();

            int auctionId = 1;
            int bidderId = getBidderId();

            BidService bidService = new BidService();

            bidService.placeBid(auctionId, bidderId, new BigDecimal("400.00"));

            AuctionService auctionService = new AuctionService();
            Auction auction = auctionService.getAuctionById(auctionId);

            System.out.println("Dat gia thanh cong!");
            System.out.println("Current price: " + auction.getCurrentPrice());
            System.out.println("Current winner ID: " + auction.getCurrentWinnerId());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void prepareDataForBidTest() throws Exception {
        try (Connection connection = DatabaseConnection.getConnection()) {
            String insertUserSql = """
                    INSERT OR IGNORE INTO users(username, password, role)
                    VALUES ('bidder1', '123456', 'BIDDER')
                    """;

            try (PreparedStatement statement = connection.prepareStatement(insertUserSql)) {
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

    private static int getBidderId() throws Exception {
        String sql = "SELECT id FROM users WHERE username = 'bidder1'";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            if (resultSet.next()) {
                return resultSet.getInt("id");
            }
        }

        throw new RuntimeException("Khong tim thay bidder1");
    }
}