package com.example.auction;

import com.example.auction.dao.UserDao;
import com.example.auction.model.Auction;
import com.example.auction.model.AuctionStatus;
import com.example.auction.model.Item;
import com.example.auction.model.User;
import com.example.auction.service.AuctionService;
import com.example.auction.service.BidService;
import com.example.auction.service.ItemService;
import com.example.auction.util.SceneManager;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class AuctionListController {

    @FXML private TableView<AuctionRowData> auctionTableView;
    @FXML private TableColumn<AuctionRowData, Integer> idColumn;
    @FXML private TableColumn<AuctionRowData, String> productNameColumn;
    @FXML private TableColumn<AuctionRowData, String> priceColumn;
    @FXML private TableColumn<AuctionRowData, String> statusColumn;

    @FXML private Label userInfoLabel;
    @FXML private Button makeBidButton;
    @FXML private Button depositButton;
    @FXML private Button newSaleButton;
    @FXML private Button removeItemButton;

    private final AuctionService auctionService = new AuctionService();
    private final BidService bidService = new BidService();
    private final ItemService itemService = new ItemService();
    private final UserDao userDao = new UserDao();

    private int currentUserId;
    private String currentRole;

    @FXML
    public void initialize() {
        if (UserSession.currentUser != null) {
            this.currentUserId = UserSession.currentUser.getId();
            this.currentRole = UserSession.currentUser.getRole().name();
        } else {
            // Backup test nhanh dữ liệu
            this.currentUserId = 2;
            this.currentRole = "BIDDER";
        }

        // Cấu hình bảng hiển thị
        auctionTableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        idColumn.setReorderable(false);
        productNameColumn.setReorderable(false);
        priceColumn.setReorderable(false);
        statusColumn.setReorderable(false);

        idColumn.setMaxWidth(60);
        idColumn.setMinWidth(50);
        statusColumn.setMaxWidth(130);
        statusColumn.setMinWidth(100);
        priceColumn.setMaxWidth(160);
        priceColumn.setMinWidth(120);

        idColumn.setCellValueFactory(cell -> cell.getValue().idProperty().asObject());
        productNameColumn.setCellValueFactory(cell -> cell.getValue().nameProperty());
        priceColumn.setCellValueFactory(cell -> cell.getValue().priceProperty());
        statusColumn.setCellValueFactory(cell -> cell.getValue().statusProperty());

        // Tải thông tin và phân quyền chức năng giao diện dựa vào Role
        loadCurrentUserInfo();
        configureButtonVisibility();

        // Nạp danh sách các phiên đấu giá
        loadAuctionData();
    }

    /**
     * VẤN ĐỀ 1: Điều khiển việc Ẩn/Hiện nút bấm theo Vai trò (Role-based UI Control)
     */
    private void configureButtonVisibility() {
        if ("SELLER".equalsIgnoreCase(currentRole)) {
            // Seller bán hàng: Không được đấu giá, không cần nạp tiền vào ví ở đây
            makeBidButton.setVisible(false);
            makeBidButton.setManaged(false);
            depositButton.setVisible(false);
            depositButton.setManaged(false);

            newSaleButton.setVisible(true);
            newSaleButton.setManaged(true);
            removeItemButton.setVisible(true);
            removeItemButton.setManaged(true);
        } else if ("BIDDER".equalsIgnoreCase(currentRole)) {
            // Bidder đi mua: Không được tự tạo phiên hay hủy phiên của người khác
            makeBidButton.setVisible(true);
            makeBidButton.setManaged(true);
            depositButton.setVisible(true);
            depositButton.setManaged(true);

            newSaleButton.setVisible(false);
            newSaleButton.setManaged(false);
            removeItemButton.setVisible(false);
            removeItemButton.setManaged(false);
        } else if ("ADMIN".equalsIgnoreCase(currentRole)) {
            // Quyền tối cao Admin: Thấy và quản lý được toàn bộ hệ thống
            makeBidButton.setVisible(true);
            makeBidButton.setManaged(true);
            depositButton.setVisible(true);
            depositButton.setManaged(true);
            newSaleButton.setVisible(true);
            newSaleButton.setManaged(true);
            removeItemButton.setVisible(true);
            removeItemButton.setManaged(true);
        }
    }

    private void loadCurrentUserInfo() {
        try {
            User user = userDao.findById(currentUserId);
            if (user != null) {
                String username = user.getUsername();
                currentRole = user.getRole().name();
                configureButtonVisibility(); // Cập nhật lại UI nếu role thay đổi động từ DB

                if (!"SELLER".equalsIgnoreCase(currentRole)) {
                    BigDecimal balance = userDao.getBalance(currentUserId);
                    userInfoLabel.setText(String.format("Tài khoản: %s  |  Vai trò: [%s]  |  Số dư ví: %,.0f VNĐ",
                            username, currentRole, balance.doubleValue()));
                } else {
                    userInfoLabel.setText(String.format("Tài khoản: %s  |  Vai trò: [%s]", username, currentRole));
                }
            } else {
                userInfoLabel.setText("Không tìm thấy thông tin tài khoản trên hệ thống!");
            }
        } catch (Exception e) {
            userInfoLabel.setText("Lỗi khi tải thông tin người dùng: " + e.getMessage());
        }
    }

    private void loadAuctionData() {
        try {
            List<Auction> auctions = auctionService.getAllAuctions();
            ObservableList<AuctionRowData> rowDataList = FXCollections.observableArrayList();

            for (Auction auction : auctions) {
                try {
                    Item item = itemService.getItemByid(auction.getItemId());
                    rowDataList.add(new AuctionRowData(auction, item));
                } catch (Exception e) {
                    System.err.println("Không tìm thấy thông tin sản phẩm cho phiên đấu giá ID: " + auction.getId());
                }
            }
            auctionTableView.setItems(rowDataList);
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Lỗi tải dữ liệu", "Không thể nạp danh sách đấu giá: " + e.getMessage());
        }
    }

    @FXML
    private void handleMakeBid() {
        AuctionRowData selectedRow = auctionTableView.getSelectionModel().getSelectedItem();
        if (selectedRow == null) {
            showAlert(Alert.AlertType.WARNING, "Cảnh báo", "Vui lòng chọn một phiên đấu giá trong danh sách!");
            return;
        }

        Auction auction = selectedRow.getAuction();

        if (auction.getStatus() != AuctionStatus.RUNNING) {
            showAlert(Alert.AlertType.ERROR, "Lỗi trạng thái", "Phiên đấu giá này không ở trạng thái đang diễn ra (RUNNING)!");
            return;
        }

        if (auction.getCurrentWinnerId() != null && auction.getCurrentWinnerId().equals(currentUserId)) {
            showAlert(Alert.AlertType.INFORMATION, "Thông báo đặt giá", "Bạn đang là người trả giá cao nhất rồi!");
            return;
        }

        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Đặt giá đấu");
        dialog.setHeaderText("Sản phẩm: " + selectedRow.getItem().getName());
        dialog.setContentText("Nhập số tiền muốn trả (Giá hiện tại: " + auction.getCurrentPrice() + " VNĐ):");

        Optional<String> result = dialog.showAndWait();
        result.ifPresent(amountStr -> {
            try {
                BigDecimal bidAmount = new BigDecimal(amountStr.trim());
                bidService.placeBid(auction.getId(), currentUserId, bidAmount);
                showAlert(Alert.AlertType.INFORMATION, "Thành công", "Bạn đã đặt giá đấu thành công!");

                loadAuctionData();
                loadCurrentUserInfo();
            } catch (NumberFormatException e) {
                showAlert(Alert.AlertType.ERROR, "Lỗi nhập liệu", "Vui lòng nhập đúng định dạng số tiền hợp lệ!");
            } catch (Exception e) {
                showAlert(Alert.AlertType.ERROR, "Lỗi đặt giá", e.getMessage());
            }
        });
    }

    @FXML
    private void handleViewDetail() {
        AuctionRowData selectedRow = auctionTableView.getSelectionModel().getSelectedItem();
        if (selectedRow == null) {
            showAlert(Alert.AlertType.WARNING, "Cảnh báo", "Vui lòng chọn một hàng để xem chi tiết!");
            return;
        }

        Auction auction = selectedRow.getAuction();
        Item item = selectedRow.getItem();

        StringBuilder detailText = new StringBuilder();
        detailText.append("=== THÔNG TIN CHI TIẾT PHIÊN ĐẤU GIÁ ===\n\n");
        detailText.append("Mã đấu giá (ID): ").append(auction.getId()).append("\n");
        detailText.append("Tên sản phẩm: ").append(item.getName()).append("\n");
        detailText.append("Loại sản phẩm: ").append(item.getItemType()).append("\n");
        detailText.append("Mô tả: ").append(item.getDescription()).append("\n");
        detailText.append("Giá khởi điểm: ").append(item.getStartingPrice()).append(" VNĐ\n");
        detailText.append("Giá hiện tại: ").append(auction.getCurrentPrice()).append(" VNĐ\n");
        detailText.append("Trạng thái: ").append(auction.getStatus()).append("\n");

        if (auction.getCurrentWinnerId() != null && auction.getCurrentWinnerId().equals(currentUserId)) {
            detailText.append("\nChú thích: Bạn đang là người tạm thời dẫn đầu phiên đấu giá này.");
        }

        showAlert(Alert.AlertType.INFORMATION, "Chi tiết sản phẩm", detailText.toString());
    }

    /**
     * VẤN ĐỀ 2: Bảo vệ dữ liệu (Encapsulation) & Phân quyền nghiệp vụ hủy phiên đấu giá
     */
    @FXML
    private void handleRemoveItem() {
        // 1. Kiểm tra lựa chọn trên bảng
        AuctionRowData selected = auctionTableView.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert(Alert.AlertType.WARNING, "Cảnh báo", "Vui lòng chọn một phiên đấu giá trong bảng để hủy!");
            return;
        }

        Auction auction = selected.getAuction();
        Item item = selected.getItem();

        // 2. Chặn việc hủy vô hạn hoặc sai trạng thái (State pattern concept)
        AuctionStatus status = auction.getStatus();
        if (status == AuctionStatus.CANCELED) {
            showAlert(Alert.AlertType.ERROR, "Lỗi trạng thái", "Phiên đấu giá này ĐÃ BỊ HỦY trước đó rồi. Không thể hủy lại!");
            return;
        }

        // Chặn nếu phiên đấu giá đã hoàn tất thành công (FINISHED / PAID)
        if (status == AuctionStatus.FINISHED || status == AuctionStatus.PAID) {
            showAlert(Alert.AlertType.ERROR, "Lỗi nghiệp vụ", "Không thể hủy một phiên đấu giá đã kết thúc thành công hoặc đã thanh toán!");
            return;
        }

        // 3. Kiểm tra Data Ownership (Chỉ Admin hoặc Chính chủ sở hữu mặt hàng mới được hủy)
        boolean isAdmin = "ADMIN".equalsIgnoreCase(currentRole);
        boolean isOwner = false;

        try {
            // CHÚ Ý KIẾN TRÚC: Gọi getter lấy mã người bán từ Item để thực thi tính đóng gói
            // Đoạn này giả định class Item của bạn tuân thủ đúng OOP và có thuộc tính `getSellerId()` hoặc tương đương.
            isOwner = (item.getSellerId() == currentUserId);
        } catch (Exception e) {
            // Fallback an toàn phòng khi Class Item của bạn chưa bổ sung trường dữ liệu `sellerId`
            // Hãy kiểm tra lại class Item.java xem phương thức chuẩn lấy ID người bán là gì nhé!
            System.err.println("Kiến trúc lưu ý: Vui lòng kiểm tra lại phương thức getSellerId() trong class Item.");
            isOwner = false;
        }

        if (!isAdmin && !isOwner) {
            showAlert(Alert.AlertType.ERROR, "Lỗi phân quyền", "Bạn không có quyền hủy phiên đấu giá này! Chỉ Admin hoặc Người đăng bán sản phẩm mới thực hiện được.");
            return;
        }

        // 4. Xác nhận hành động
        Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmAlert.setTitle("Xác nhận hủy phiên");
        confirmAlert.setHeaderText(null);
        confirmAlert.setContentText("Bạn có chắc chắn muốn HỦY phiên đấu giá của sản phẩm: [" + item.getName() + "] không?");

        Optional<ButtonType> result = confirmAlert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                // 5. Gọi Service chuyển đổi trạng thái dưới DB
                auctionService.cancelAuction(auction.getId());
                showAlert(Alert.AlertType.INFORMATION, "Thành công", "Đã hủy phiên đấu giá thành công!");

                // 6. Refresh dữ liệu mới lên UI
                loadAuctionData();
            } catch (IllegalArgumentException e) {
                showAlert(Alert.AlertType.ERROR, "Lỗi nghiệp vụ", e.getMessage());
            } catch (SQLException e) {
                showAlert(Alert.AlertType.ERROR, "Lỗi hệ thống", "Không thể kết nối CSDL để hủy phiên!");
                e.printStackTrace();
            }
        }
    }

    @FXML
    private void handleDepositMoney() {
        TextInputDialog dialog = new TextInputDialog("");
        dialog.setTitle("Nạp tiền vào ví");
        dialog.setHeaderText("Nạp tiền tài khoản");
        dialog.setContentText("Nhập số tiền muốn nạp (VNĐ):");

        Optional<String> result = dialog.showAndWait();
        if (result.isPresent()) {
            String amountStr = result.get().trim();
            if (amountStr.isEmpty()) return;

            try {
                BigDecimal amount = new BigDecimal(amountStr);

                if (amount.compareTo(BigDecimal.ZERO) <= 0) {
                    showAlert(Alert.AlertType.ERROR, "Lỗi nhập liệu", "Số tiền nạp vào phải lớn hơn 0 VNĐ!");
                    return;
                }

                userDao.depositMoney(currentUserId, amount);
                showAlert(Alert.AlertType.INFORMATION, "Thành công",
                        "Đã nạp thành công " + String.format("%,.0f VNĐ", amount) + " vào ví tài khoản!");

                loadCurrentUserInfo();

            } catch (NumberFormatException e) {
                showAlert(Alert.AlertType.ERROR, "Lỗi nhập liệu", "Vui lòng chỉ nhập ký tự số hợp lệ!");
            } catch (SQLException e) {
                showAlert(Alert.AlertType.ERROR, "Lỗi hệ thống", "Lỗi kết nối cơ sở dữ liệu khi nạp tiền!");
                e.printStackTrace();
            }
        }
    }

    @FXML
    private void handleNewSale() {
        showAlert(Alert.AlertType.INFORMATION, "Tạo phiên", "Tính năng đăng bán sản phẩm đấu giá mới đang được xây dựng!");
    }

    @FXML
    private void handleLogout() {
        try {
            SceneManager.switchScene("view/login.fxml", "Auction System - Login");
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Lỗi chuyển màn hình", "Không tìm thấy file giao diện đăng nhập login.fxml!");
        }
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static class AuctionRowData {
        private final SimpleIntegerProperty id;
        private final SimpleStringProperty name;
        private final SimpleStringProperty price;
        private final SimpleStringProperty status;

        private final Auction auction;
        private final Item item;

        public AuctionRowData(Auction auction, Item item) {
            this.auction = auction;
            this.item = item;

            this.id = new SimpleIntegerProperty(auction.getId());
            this.name = new SimpleStringProperty(item.getName());

            double currentPriceVal = auction.getCurrentPrice() != null ? auction.getCurrentPrice().doubleValue() : 0.0;
            this.price = new SimpleStringProperty(String.format("%,.0f VNĐ", currentPriceVal));
            this.status = new SimpleStringProperty(auction.getStatus() != null ? auction.getStatus().name() : "OPEN");
        }

        public Auction getAuction() { return auction; }
        public Item getItem() { return item; }

        public SimpleIntegerProperty idProperty() { return id; }
        public SimpleStringProperty nameProperty() { return name; }
        public SimpleStringProperty priceProperty() { return price; }
        public SimpleStringProperty statusProperty() { return status; }
    }
}