import dao.BidDao;
import model.BidTransaction;

import java.util.List;

public class TestBidDao {
    public static void main(String[] args) {
        try {
            BidDao bidDao = new BidDao();

            int auctionId = 1;

            List<BidTransaction> bids = bidDao.findAuctionId(auctionId);

            if (bids.isEmpty()) {
                System.out.println("Khong co bid nao trong auction id = " + auctionId);
            } else {
                System.out.println("Danh sach bid cua auction id = " + auctionId);

                for (BidTransaction bid : bids) {
                    System.out.println("ID: " + bid.getId());
                    System.out.println("Auction ID: " + bid.getAuctionId());
                    System.out.println("Bidder ID: " + bid.getBidderId());
                    System.out.println("Amount: " + bid.getAmount());
                    System.out.println("Bid time: " + bid.getBidTime());
                    System.out.println("--------------------");
                }
            }

            BidTransaction highestBid = bidDao.findHighestBidByAuctionId(auctionId);

            if (highestBid == null) {
                System.out.println("Chua co highest bid");
            } else {
                System.out.println("Highest bid:");
                System.out.println("ID: " + highestBid.getId());
                System.out.println("Auction ID: " + highestBid.getAuctionId());
                System.out.println("Bidder ID: " + highestBid.getBidderId());
                System.out.println("Amount: " + highestBid.getAmount());
                System.out.println("Bid time: " + highestBid.getBidTime());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}