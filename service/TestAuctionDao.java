import dao.AuctionDao;
import model.Auction;

import java.util.List;

public class TestAuctionDao {
    public static void main(String[] args) {
        try {
            AuctionDao auctionDao = new AuctionDao();

            List<Auction> auctions = auctionDao.findAll();

            if (auctions.isEmpty()) {
                System.out.println("Khong co auction nao trong database");
                return;
            }

            for (Auction auction : auctions) {
                System.out.println("ID: " + auction.getId());
                System.out.println("Item ID: " + auction.getItemId());
                System.out.println("Start time: " + auction.getStartTime());
                System.out.println("End time: " + auction.getEndTime());
                System.out.println("Current price: " + auction.getCurrentPrice());
                System.out.println("Current winner ID: " + auction.getCurrentWinnerId());
                System.out.println("Status: " + auction.getStatus());
                System.out.println("Created at: " + auction.getCreatedAt());
                System.out.println("--------------------");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}