import dao.DatabaseConnection;
import  model.Auction;
import model.AuctionStatus;
import org.junit.jupiter.api.Test;
import service.AuctionService;

import java.sql.Connection;
import java.sql.PreparedStatement;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class AuctionServiceTest{
    @Test
    void startAuction_success () throws Exception{// kiểm tra trườn hợp start auction thành công
        prepareAuction("OPEN","+1 day");
        AuctionService auctionService = new AuctionService();
        auctionService.startAuction(1);
        Auction auction= auctionService.getAuctionById(1);
        assertEquals(AuctionStatus.RUNNING,auction.getStatus());

    }
    @Test
    void startAuction_notOpen_shouldFail() throws Exception{// kiểm tra trường hợp không phải OPEN thì không được start
        prepareAuction("RUNNING","+1 day");
        AuctionService auctionService= new AuctionService();
        assertThrows(IllegalArgumentException.class,()-> {
            auctionService.startAuction(1);
        });
    }
    @Test
    void closeExpireAuctions_success() throws Exception{ // kiểm tra chức năng tự đóng auction đã hết hạn
        prepareAuction("RUNNING","-1 day");
        AuctionService auctionService = new AuctionService();
        auctionService.closeExpireAuctions();
        Auction auction= auctionService.getAuctionById(1);
        assertEquals(AuctionStatus.FINISHED,auction.getStatus());

    }
    private void prepareAuction(String status,String timeModifier) throws Exception{
        String sql= """
                UPDATE auctions
                SET status=?,
                    end_time=datetime('now',?)
                WHERE id =1
                   
                """;
        try(Connection connection = DatabaseConnection.getConnection();
        PreparedStatement statement= connection.prepareStatement(sql)){
            statement.setString(1,status);
            statement.setString(2,timeModifier);
            statement.executeUpdate();
        }
    }

}




