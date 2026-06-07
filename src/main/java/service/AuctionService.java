package service;

import dao.AuctionDao;
import exception.AuctionNotFoundException;
import model.Auction;
import model.AuctionStatus;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
import dao.ItemDao;
import model.Item;
import model.AuctionStatus;
import model.Auction;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import dao.ItemDao;
import model.Auction;
import model.AuctionStatus;
import model.Item;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalDateTime;

import java.time.LocalDateTime;
public class AuctionService{
    private final ItemDao itemDao;
    private final AuctionDao auctionDao;
    public AuctionService(){
        this.auctionDao=new AuctionDao();
        this.itemDao = new ItemDao();
    }
    public void createAuction(int itemId, LocalDateTime startTime,
                              LocalDateTime endTime, BigDecimal currentPrice) throws SQLException {
        if (itemId <= 0) {
            throw new IllegalArgumentException("itemId khong hop le");
        }

        if (startTime == null) {
            throw new IllegalArgumentException("startTime khong duoc de trong");
        }

        if (endTime == null) {
            throw new IllegalArgumentException("endTime khong duoc de trong");
        }

        if (!endTime.isAfter(startTime)) {
            throw new IllegalArgumentException("endTime phai sau startTime");
        }

        if (currentPrice == null || currentPrice.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("gia khoi diem phai lon hon 0");
        }

        Item item = itemDao.findById(itemId);

        if (item == null) {
            throw new IllegalArgumentException("khong tim thay san pham de tao auction");
        }

        Auction auction = new Auction(
                0,
                itemId,
                startTime,
                endTime,
                currentPrice,
                null,
                AuctionStatus.OPEN,
                LocalDateTime.now()
        );

        auctionDao.save(auction);
    }
    public Auction getAuctionById(int auctionId) throws SQLException{
        if(auctionId <=0){
            throw new IllegalArgumentException(("Auction id khong hop le"));
        }
        Auction auction=auctionDao.findById(auctionId);
        if (auction==null){
            throw new AuctionNotFoundException(("khong tim thay auction id="+auctionId));
        }
        return auction;
    }
    public List<Auction> getAllAuctions() throws SQLException{
        return auctionDao.findAll();
    }
    public void startAuction(int auctionId) throws SQLException{
        Auction auction=getAuctionById(auctionId); // tìm kiếm id phiên đấu giá
        if (auction.getStatus()!=AuctionStatus.OPEN){ // kiểm tra trạng thái phiên đấu giá  là OPEN hay không
            throw new IllegalArgumentException("chi auction OPEN moi duoc bat dau");
        }
        LocalDateTime now = LocalDateTime.now();
        if(now.isAfter(auction.getEndTime())){ //
            auction.setStatus(AuctionStatus.FINISHED); // kiểm tra xem phiên đấu giá đã hết hạn chưa
            auctionDao.updateStatus(auction.getId(), AuctionStatus.FINISHED);// neeus hết hạn rồi thì chuyển sang finish
            throw new IllegalArgumentException("Auction da het han, khong the bat dau");

        }
        auctionDao.updateStatus(auction.getId(), AuctionStatus.RUNNING);// nếu chưa hết hạn thì chuyển sang running

    }
    public void cancelAuction(int auctionId) throws SQLException{
        Auction auction=getAuctionById(auctionId);
        if(auction.getStatus()==AuctionStatus.FINISHED|| auction.getStatus()==AuctionStatus.PAID){  // kiểm tra xem nó đã kết thúc hoặc được trả chưa
            throw new IllegalArgumentException("khong the huy auction da ket thuc hoac thanh toan");
        }
        auctionDao.updateStatus(auction.getId(), AuctionStatus.CANCELED); // nếu auction chưa kết thúc hoặc thanh toán thì chuyển sang trạng thái CANCEL

    }
    public void closeExpireAuctions() throws SQLException{
        List<Auction> auctions=auctionDao.findAll();
        LocalDateTime now=LocalDateTime.now();
        for(Auction auction:auctions){
            if(auction.getStatus()==AuctionStatus.RUNNING&& now.isAfter(auction.getEndTime())){ // nếu đang running và đã hết hạn thì chuyển sang finished
                auctionDao.updateStatus(auction.getId(), AuctionStatus.FINISHED);

            }
        }
    }
    public void markAsPaid(int auctionId) throws SQLException{
        Auction auction=getAuctionById(auctionId);
        if(auction.getStatus()!=AuctionStatus.FINISHED){  // kiểm tra xem nó có phải là trạng tháu finish không
            throw new IllegalArgumentException("chi auction finished moi chuyen qua paid");

        }
        auctionDao.updateStatus(auction.getId(), AuctionStatus.PAID);// nếu phải thì chuyển qua paid

    }
}



