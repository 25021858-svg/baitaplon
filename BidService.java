package service;

import dao.AuctionDao;
import dao.BidDao;
import dao.UserDao;
import exception.AuctionClosedException;
import exception.AuctionNotFoundException;
import exception.InvalidBidException;
import exception.UserNotFoundException;
import model.Auction;
import model.AuctionStatus;
import model.BidTransaction;
import model.Role;
import model.User;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalDateTime;

public class BidService {
    private final AuctionDao auctionDao;
    private final BidDao bidDao;
    private final UserDao userDao;

    public BidService() {
        this.auctionDao = new AuctionDao();
        this.bidDao = new BidDao();
        this.userDao = new UserDao();
    }

    public void placeBid(int auctionId, int bidderId, BigDecimal amount) throws SQLException {
        if (auctionId <= 0) { // kiểm tra id của auction
            throw new IllegalArgumentException("Auction id khong hop le");
        }

        if (bidderId <= 0) { // kiểm tra id của bidder
            throw new IllegalArgumentException("Bidder id khong hop le");
        }

        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) { // kiểm tra xem giá đặt có hợp lệ hay không
            throw new InvalidBidException("Gia dat phai lon hon 0");
        }

        Auction auction = auctionDao.findById(auctionId); // tìm thông tin của auction

        if (auction == null) {
            throw new AuctionNotFoundException("Khong tim thay auction id = " + auctionId);
        }

        User bidder = userDao.findById(bidderId); // tìm thông tin của người đặt

        if (bidder == null) {
            throw new UserNotFoundException("Khong tim thay bidder id = " + bidderId);
        }

        if (bidder.getRole() != Role.BIDDER) {
            throw new InvalidBidException("Chi BIDDER moi duoc dat gia");
        }

        if (auction.getStatus() != AuctionStatus.RUNNING) { // kiểm tra trạng thái phải là running
            throw new AuctionClosedException("Auction khong trong trang thai RUNNING");
        }

        LocalDateTime now = LocalDateTime.now();// lấy thời gian hiện tại

        if (now.isAfter(auction.getEndTime())) {
            auctionDao.updateStatus(auction.getId(), AuctionStatus.FINISHED);
            throw new AuctionClosedException("Auction da het han"); // kiểm tra xem auction đã hết hạn hay chưa
        }

        if (amount.compareTo(auction.getCurrentPrice()) <= 0) {
            throw new InvalidBidException("Gia dat phai cao hon gia hien tai"); // kiểm tra giá đặt
        }

        BidTransaction bid = new BidTransaction( //
                0,
                auctionId,
                bidderId,
                amount,
                now
        );

        bidDao.save(bid);// lưu thông tin lịch  sử đặt giá

        auctionDao.updateCurrentPrice(auctionId, amount, bidderId); // cập nhật thông tin mới nhất từ bidder
    }
}