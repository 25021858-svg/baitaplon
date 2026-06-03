import dao.DatabaseConnection;
import model.Auction;
import service.AuctionService;

import java.sql.Connection;
import java.sql.PreparedStatement;

public class TestAuctionService {
    public static void main(String[] args) {
        try {
            prepareAuctionForTest();

            AuctionService auctionService = new AuctionService();

            System.out.println("=== Truoc khi start ===");
            Auction before = auctionService.getAuctionById(1);
            System.out.println("Status: " + before.getStatus());
            System.out.println("End time: " + before.getEndTime());

            auctionService.startAuction(1);

            System.out.println("=== Sau khi start ===");
            Auction after = auctionService.getAuctionById(1);
            System.out.println("Status: " + after.getStatus());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void prepareAuctionForTest() throws Exception {
        String sql = """
                UPDATE auctions
                SET status = 'OPEN',
                    end_time = datetime('now', '+1 day')
                WHERE id = 1
                """;

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.executeUpdate();
        }
    }
}