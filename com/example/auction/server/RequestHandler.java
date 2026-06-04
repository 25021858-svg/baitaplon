package com.example.auction.server;

import com.example.auction.model.Auction;
import com.example.auction.service.AuctionService;
import com.example.auction.service.BidService;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;

public class RequestHandler {
    private final AuctionService auctionService;
    private final BidService bidService;

    public RequestHandler() {
        this.auctionService = new AuctionService();
        this.bidService = new BidService();
    }

    public Response handle(Request request) {
        if (request == null) {
            return Response.fail("Request khong hop le");
        }

        String action = request.getAction();

        if (action == null || action.isBlank()) {
            return Response.fail("Action khong hop le");
        }

        try {
            switch (action) {
                case "GET_AUCTIONS":
                    return handleGetAuctions();

                case "GET_AUCTION_BY_ID":
                    return handleGetAuctionById(request);

                case "START_AUCTION":
                    return handleStartAuction(request);

                case "CANCEL_AUCTION":
                    return handleCancelAuction(request);

                case "CLOSE_EXPIRED_AUCTIONS":
                    return handleCloseExpiredAuctions();

                case "MARK_AS_PAID":
                    return handleMarkAsPaid(request);

                case "PLACE_BID":
                    return handlePlaceBid(request);

                default:
                    return Response.fail("Action khong duoc ho tro: " + action);
            }
        } catch (Exception e) {
            return Response.fail(e.getMessage());
        }
    }

    private Response handleGetAuctions() throws SQLException {
        List<Auction> auctions = auctionService.getAllAuctions();

        StringBuilder result = new StringBuilder();

        for (Auction auction : auctions) {
            result.append("Auction ID: ").append(auction.getId()).append("\n");
            result.append("Item ID: ").append(auction.getItemId()).append("\n");
            result.append("Current price: ").append(auction.getCurrentPrice()).append("\n");
            result.append("Current winner ID: ").append(auction.getCurrentWinnerId()).append("\n");
            result.append("Status: ").append(auction.getStatus()).append("\n");
            result.append("Start time: ").append(auction.getStartTime()).append("\n");
            result.append("End time: ").append(auction.getEndTime()).append("\n");
            result.append("--------------------\n");
        }

        return Response.success("Lay danh sach auction thanh cong", result.toString());
    }

    private Response handleGetAuctionById(Request request) throws SQLException {
        int auctionId = parseInt(request.getValue("auctionId"), "auctionId");

        Auction auction = auctionService.getAuctionById(auctionId);

        return Response.success("Lay auction thanh cong", auction);
    }

    private Response handleStartAuction(Request request) throws SQLException {
        int auctionId = parseInt(request.getValue("auctionId"), "auctionId");

        auctionService.startAuction(auctionId);

        return Response.success("Bat dau auction thanh cong", null);
    }

    private Response handleCancelAuction(Request request) throws SQLException {
        int auctionId = parseInt(request.getValue("auctionId"), "auctionId");

        auctionService.cancelAuction(auctionId);

        return Response.success("Huy auction thanh cong", null);
    }

    private Response handleCloseExpiredAuctions() throws SQLException {
        auctionService.closeExpireAuctions();

        return Response.success("Dong cac auction het han thanh cong", null);
    }

    private Response handleMarkAsPaid(Request request) throws SQLException {
        int auctionId = parseInt(request.getValue("auctionId"), "auctionId");

        auctionService.markAsPaid(auctionId);

        return Response.success("Chuyen auction sang PAID thanh cong", null);
    }

    private Response handlePlaceBid(Request request) throws SQLException {
        int auctionId = parseInt(request.getValue("auctionId"), "auctionId");
        int bidderId = parseInt(request.getValue("bidderId"), "bidderId");
        BigDecimal amount = parseBigDecimal(request.getValue("amount"), "amount");

        bidService.placeBid(auctionId, bidderId, amount);

        return Response.success("Dat gia thanh cong", null);
    }

    private int parseInt(String value, String fieldName) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("Thieu truong: " + fieldName);
        }

        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(fieldName + " phai la so nguyen");
        }
    }

    private BigDecimal parseBigDecimal(String value, String fieldName) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("Thieu truong: " + fieldName);
        }

        try {
            return new BigDecimal(value);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(fieldName + " phai la so");
        }
    }
}