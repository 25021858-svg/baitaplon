package com.example.auction.service;

import com.example.auction.dao.AuctionDao;
import com.example.auction.exception.AuctionNotFoundException;
import com.example.auction.model.Auction;
import com.example.auction.model.AuctionStatus;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

public class AuctionService{
    private final AuctionDao auctionDao;
    public AuctionService(){
        this.auctionDao=new AuctionDao();
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



