package server;

import model.Auction;
import model.BidTransaction;
import model.Item;
import model.Role;
import model.User;
import service.AuctionService;
import service.AuthService;
import service.BidService;
import service.ItemService;
import dao.BidDao;
import java.time.LocalDateTime;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;

public class RequestHandler {
    private final AuctionService auctionService;
    private final BidService bidService;
    private final AuthService authService;
    private final ItemService itemService;
    private final BidDao bidDao;

    public RequestHandler() {
        this.auctionService = new AuctionService();
        this.bidService = new BidService();
        this.authService = new AuthService();
        this.itemService = new ItemService();
        this.bidDao = new BidDao();
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
                case "LOGIN":
                    return handleLogin(request);

                case "REGISTER":
                    return handleRegister(request);

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
                case "CREATE_AUCTION":
                    return handleCreateAuction(request);
                case "MARK_AS_PAID":
                    return handleMarkAsPaid(request);

                case "PLACE_BID":
                    return handlePlaceBid(request);

                case "GET_ITEMS":
                    return handleGetItems();

                case "GET_ITEM_BY_ID":
                    return handleGetItemById(request);

                case "GET_ITEMS_BY_SELLER":
                    return handleGetItemsBySeller(request);

                case "DELETE_ITEM":
                    return handleDeleteItem(request);

                case "GET_BIDS_BY_AUCTION":
                    return handleGetBidsByAuction(request);
                case "CREATE_ITEM":
                    return handleCreateItem(request);

                case "UPDATE_ITEM":
                    return handleUpdateItem(request);
                default:
                    return Response.fail("Action khong duoc ho tro: " + action);
            }
        } catch (Exception e) {
            return Response.fail(e.getMessage());
        }
    }
    private Response handleCreateAuction(Request request) throws SQLException {
        int itemId = parseInt(request.getValue("itemId"), "itemId");
        LocalDateTime startTime = parseLocalDateTime(request.getValue("startTime"), "startTime");
        LocalDateTime endTime = parseLocalDateTime(request.getValue("endTime"), "endTime");
        BigDecimal currentPrice = parseBigDecimal(request.getValue("currentPrice"), "currentPrice");

        auctionService.createAuction(itemId, startTime, endTime, currentPrice);

        return Response.success("Tao auction thanh cong", null);
    }

    private LocalDateTime parseLocalDateTime(String value, String fieldName) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("Thieu truong: " + fieldName);
        }

        try {
            return LocalDateTime.parse(value);
        } catch (Exception e) {
            throw new IllegalArgumentException(fieldName + " phai co dang yyyy-MM-ddTHH:mm:ss");
        }
    }

    private Response handleLogin(Request request) throws SQLException {
        String username = request.getValue("username");
        String password = request.getValue("password");

        User user = authService.login(username, password);

        if (user == null) {
            return Response.fail("Sai username hoac password");
        }

        String data = user.getId() + "," + user.getUsername() + "," + user.getRole();

        return Response.success("Dang nhap thanh cong", data);
    }

    private Response handleRegister(Request request) throws SQLException {
        String username = request.getValue("username");
        String password = request.getValue("password");
        String roleText = request.getValue("role");

        if (username == null || username.isBlank()) {
            return Response.fail("Username khong duoc de trong");
        }

        if (password == null || password.isBlank()) {
            return Response.fail("Password khong duoc de trong");
        }

        if (roleText == null || roleText.isBlank()) {
            return Response.fail("Role khong duoc de trong");
        }

        Role role = Role.valueOf(roleText);

        authService.register(username, password, role);

        return Response.success("Dang ky thanh cong", null);
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

        StringBuilder result = new StringBuilder();

        result.append("Auction ID: ").append(auction.getId()).append("\n");
        result.append("Item ID: ").append(auction.getItemId()).append("\n");
        result.append("Current price: ").append(auction.getCurrentPrice()).append("\n");
        result.append("Current winner ID: ").append(auction.getCurrentWinnerId()).append("\n");
        result.append("Status: ").append(auction.getStatus()).append("\n");
        result.append("Start time: ").append(auction.getStartTime()).append("\n");
        result.append("End time: ").append(auction.getEndTime()).append("\n");

        return Response.success("Lay auction thanh cong", result.toString());
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

    private Response handleGetItems() throws SQLException {
        List<Item> items = itemService.getAllItems();

        StringBuilder result = new StringBuilder();

        for (Item item : items) {
            result.append("Item ID: ").append(item.getId()).append("\n");
            result.append("Seller ID: ").append(item.getSellerId()).append("\n");
            result.append("Name: ").append(item.getName()).append("\n");
            result.append("Description: ").append(item.getDescription()).append("\n");
            result.append("Starting price: ").append(item.getStartingPrice()).append("\n");
            result.append("Type: ").append(item.getItemType()).append("\n");
            result.append("Created at: ").append(item.getCreatedAt()).append("\n");
            result.append("--------------------\n");
        }

        return Response.success("Lay danh sach item thanh cong", result.toString());
    }

    private Response handleGetItemById(Request request) throws SQLException {
        int itemId = parseInt(request.getValue("itemId"), "itemId");

        Item item = itemService.getItemById(itemId);

        StringBuilder result = new StringBuilder();

        result.append("Item ID: ").append(item.getId()).append("\n");
        result.append("Seller ID: ").append(item.getSellerId()).append("\n");
        result.append("Name: ").append(item.getName()).append("\n");
        result.append("Description: ").append(item.getDescription()).append("\n");
        result.append("Starting price: ").append(item.getStartingPrice()).append("\n");
        result.append("Type: ").append(item.getItemType()).append("\n");
        result.append("Created at: ").append(item.getCreatedAt()).append("\n");

        return Response.success("Lay item thanh cong", result.toString());
    }

    private Response handleGetItemsBySeller(Request request) throws SQLException {
        int sellerId = parseInt(request.getValue("sellerId"), "sellerId");

        List<Item> items = itemService.getItemsBySellerId(sellerId);

        StringBuilder result = new StringBuilder();

        for (Item item : items) {
            result.append("Item ID: ").append(item.getId()).append("\n");
            result.append("Seller ID: ").append(item.getSellerId()).append("\n");
            result.append("Name: ").append(item.getName()).append("\n");
            result.append("Description: ").append(item.getDescription()).append("\n");
            result.append("Starting price: ").append(item.getStartingPrice()).append("\n");
            result.append("Type: ").append(item.getItemType()).append("\n");
            result.append("Created at: ").append(item.getCreatedAt()).append("\n");
            result.append("--------------------\n");
        }

        return Response.success("Lay danh sach item cua seller thanh cong", result.toString());
    }

    private Response handleDeleteItem(Request request) throws SQLException {
        int itemId = parseInt(request.getValue("itemId"), "itemId");

        itemService.deleteItem(itemId);

        return Response.success("Xoa item thanh cong", null);
    }
    private Response handleCreateItem(Request request) throws SQLException {
        int sellerId = parseInt(request.getValue("sellerId"), "sellerId");
        String name = request.getValue("name");
        String description = request.getValue("description");
        BigDecimal startingPrice = parseBigDecimal(request.getValue("startingPrice"), "startingPrice");
        String itemType = request.getValue("itemType");

        itemService.createItem(sellerId, name, description, startingPrice, itemType);

        return Response.success("Tao san pham thanh cong", null);
    }

    private Response handleUpdateItem(Request request) throws SQLException {
        int itemId = parseInt(request.getValue("itemId"), "itemId");
        String name = request.getValue("name");
        String description = request.getValue("description");
        BigDecimal startingPrice = parseBigDecimal(request.getValue("startingPrice"), "startingPrice");
        String itemType = request.getValue("itemType");

        itemService.updateItem(itemId, name, description, startingPrice, itemType);

        return Response.success("Cap nhat san pham thanh cong", null);
    }

    private Response handleGetBidsByAuction(Request request) throws SQLException {
        int auctionId = parseInt(request.getValue("auctionId"), "auctionId");

        List<BidTransaction> bids = bidDao.findAuctionId(auctionId);

        StringBuilder result = new StringBuilder();

        for (BidTransaction bid : bids) {
            result.append("Bid ID: ").append(bid.getId()).append("\n");
            result.append("Auction ID: ").append(bid.getAuctionId()).append("\n");
            result.append("Bidder ID: ").append(bid.getBidderId()).append("\n");
            result.append("Amount: ").append(bid.getAmount()).append("\n");
            result.append("Bid time: ").append(bid.getBidTime()).append("\n");
            result.append("--------------------\n");
        }

        return Response.success("Lay lich su bid thanh cong", result.toString());
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