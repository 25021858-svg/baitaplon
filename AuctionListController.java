package com.example.auction;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.io.IOException;
import java.util.Optional;

import com.example.auction.util.SceneManager;
public class AuctionListController {

    @FXML private TableView<FakeData> auctionTableView;
    @FXML private TableColumn<FakeData, Integer> idColumn;
    @FXML private TableColumn<FakeData, String> productNameColumn;
    @FXML private TableColumn<FakeData, String> priceColumn;
    @FXML private TableColumn<FakeData, String> statusColumn;

    @FXML private Label userInfoLabel;
    @FXML private Button makeBidButton;
    @FXML private Button depositButton;
    @FXML private Button newSaleButton;

    // Quản lý số dư tài khoản ví ảo tập trung để check điều kiện đặt giá
    private double userBalance = 50000000.0;
    private String currentRole = "Buyer";

    @FXML
    public void initialize() {
        // Kết nối các cột với thuộc tính dữ liệu công khai
        idColumn.setCellValueFactory(cell -> cell.getValue().idProperty().asObject());
        productNameColumn.setCellValueFactory(cell -> cell.getValue().nameProperty());
        priceColumn.setCellValueFactory(cell -> cell.getValue().priceProperty());
        statusColumn.setCellValueFactory(cell -> cell.getValue().statusProperty());

        idColumn.prefWidthProperty().bind(auctionTableView.widthProperty().multiply(0.10));
        productNameColumn.prefWidthProperty().bind(auctionTableView.widthProperty().multiply(0.50));
        priceColumn.prefWidthProperty().bind(auctionTableView.widthProperty().multiply(0.20));
        statusColumn.prefWidthProperty().bind(auctionTableView.widthProperty().multiply(0.20));

        // Tooltip hiển thị text khi rê chuột vào tên sản phẩm quá dài
        productNameColumn.setCellFactory(column -> new javafx.scene.control.TableCell<FakeData, String>() {
            private final javafx.scene.control.Tooltip tooltip = new javafx.scene.control.Tooltip();

            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setTooltip(null);
                } else {
                    setText(item);
                    tooltip.setText(item);
                    tooltip.setWrapText(true);
                    tooltip.setPrefWidth(300);
                    setTooltip(tooltip);
                }
            }
        });

        // Đổ dữ liệu mẫu (FakeData) chứa cả thuộc tính ẩn: Khởi điểm, Hiện tại, Bước giá tối thiểu
        ObservableList<FakeData> list = FXCollections.observableArrayList(
                new FakeData(1, "Laptop Dell XPS 13 Pro cực mượt", 12000000.0, 15000000.0, 500000.0, "Đang mở"),
                new FakeData(2, "iPhone 13 Pro Max xịn", 15000000.0, 18500000.0, 200000.0, "Đang mở"),
                new FakeData(3, "Bàn phím cơ AKKO", 800000.0, 1200000.0, 50000.0, "Đã đóng")
        );
        auctionTableView.setItems(list);

        // FIX: Sửa model.UserSession thành UserSession do nằm cùng một thư mục package
        this.currentRole = UserSession.loggedInRole;
        runMockRoleTesting(currentRole);
    }

    private void updateUI() {
        userInfoLabel.setText("Tài khoản: " + UserSession.loggedInUsername + " | Vai trò: " + currentRole + " | Số dư ví ảo: " + String.format("%,.0f VNĐ", userBalance));

        if ("Buyer".equalsIgnoreCase(currentRole)) {
            makeBidButton.setVisible(true);   makeBidButton.setManaged(true);
            depositButton.setVisible(true);   depositButton.setManaged(true);
            newSaleButton.setVisible(false);  newSaleButton.setManaged(false);
        } else if ("Seller".equalsIgnoreCase(currentRole)) {
            makeBidButton.setVisible(false);  makeBidButton.setManaged(false);
            depositButton.setVisible(false);  depositButton.setManaged(false);
            newSaleButton.setVisible(true);   newSaleButton.setManaged(true);
        } else {
            makeBidButton.setVisible(true);   makeBidButton.setManaged(true);
            depositButton.setVisible(true);   depositButton.setManaged(true);
            newSaleButton.setVisible(true);   newSaleButton.setManaged(true);
        }
    }
    /**
     * Hàm hiển thị giao diện và số dư đồng bộ theo thời gian thực
     */
    private void runMockRoleTesting(String role) {
        userInfoLabel.setText("Tài khoản: " + UserSession.loggedInUsername + " | Vai trò: " + role + " | Số dư ví ảo: " + String.format("%,.0f VNĐ", userBalance));

        if ("Buyer".equalsIgnoreCase(role)) {
            makeBidButton.setVisible(true);   makeBidButton.setManaged(true);
            depositButton.setVisible(true);   depositButton.setManaged(true);
            newSaleButton.setVisible(false);  newSaleButton.setManaged(false);
        } else if ("Seller".equalsIgnoreCase(role)) {
            makeBidButton.setVisible(false);  makeBidButton.setManaged(false);
            depositButton.setVisible(false);  depositButton.setManaged(false);
            newSaleButton.setVisible(true);   newSaleButton.setManaged(true);
        } else {
            makeBidButton.setVisible(true);   makeBidButton.setManaged(true);
            depositButton.setVisible(true);   depositButton.setManaged(true);
            newSaleButton.setVisible(true);   newSaleButton.setManaged(true);
        }
    }

    @FXML
    private void handleViewDetail() {
        FakeData selectedProduct = auctionTableView.getSelectionModel().getSelectedItem();
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Chi tiết sản phẩm");
        alert.setHeaderText(null);

        if (selectedProduct != null) {
            // Hiển thị cả các thuộc tính ẩn (Giá khởi điểm và Bước giá tối thiểu)
            alert.setContentText("=== THÔNG TIN CHI TIẾT ===" +
                    "\nTên sản phẩm: " + selectedProduct.nameProperty().get() +
                    "\n----------------------------------------" +
                    "\n-> Giá khởi điểm: " + String.format("%,.0f VNĐ", selectedProduct.getStartingPrice()) +
                    "\n-> Giá hiện tại công khai: " + selectedProduct.priceProperty().get() +
                    "\n-> Bước giá tối thiểu yêu cầu: " + String.format("%,.0f VNĐ", selectedProduct.getMinBidStep()) +
                    "\n----------------------------------------" +
                    "\nTrạng thái phiên: " + selectedProduct.statusProperty().get());
        } else {
            alert.setContentText("Vui lòng chọn một sản phẩm trong bảng trước khi bấm xem chi tiết!");
        }
        alert.showAndWait();
    }

    @FXML
    private void handleMakeBid() {
        FakeData selectedProduct = auctionTableView.getSelectionModel().getSelectedItem();

        // Điều kiện 1: Kiểm tra xem đã chọn sản phẩm trên bảng chưa
        if (selectedProduct == null) {
            showMockAlert("Yêu cầu chọn sản phẩm", "Vui lòng chọn một sản phẩm từ bảng danh sách để thực hiện đấu giá!");
            return;
        }

        // Kiểm tra xem sản phẩm có đang mở hay đã đóng phiên đấu giá
        if ("Đã đóng".equalsIgnoreCase(selectedProduct.statusProperty().get())) {
            showMockAlert("Phiên đã đóng", "Sản phẩm này đã kết thúc đấu giá, bạn không thể đặt giá thêm.");
            return;
        }

        // Tính toán mức giá hợp lệ tối thiểu tiếp theo
        double minValidBid = selectedProduct.getCurrentPriceNum() + selectedProduct.getMinBidStep();

        // Mở hộp thoại nhập số tiền đấu giá
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Tham Gia Đặt Giá Đấu Giá");
        dialog.setHeaderText("Sản phẩm: " + selectedProduct.nameProperty().get());
        dialog.setContentText("Giá hiện tại: " + selectedProduct.priceProperty().get() +
                "\nBước giá tối thiểu: " + String.format("%,.0f VNĐ", selectedProduct.getMinBidStep()) +
                "\n=> Mức giá đặt tối thiểu tiếp theo: " + String.format("%,.0f VNĐ", minValidBid) +
                "\n\nNhập số tiền bạn muốn đặt:");

        Optional<String> result = dialog.showAndWait();
        if (result.isPresent()) {
            try {
                double inputBidAmount = Double.parseDouble(result.get().trim());

                // Điều kiện 2: Kiểm tra giá đưa ra không được ít hơn (giá hiện tại + bước giá tối thiểu)
                if (inputBidAmount < minValidBid) {
                    showMockAlert("Lỗi Đặt Giá", "Giá bạn đặt không hợp lệ!" +
                            "\nSố tiền nhập vào phải lớn hơn hoặc bằng mức tối thiểu\nquy định: " + String.format("%,.0f VNĐ", minValidBid));
                    return;
                }

                // Điều kiện 3: Kiểm tra giá đưa ra không được vượt quá số tiền hiện tại có trong tài khoản
                if (inputBidAmount > userBalance) {
                    showMockAlert("Lỗi Số Dư", "Tài khoản của bạn không đủ tiền để thực hiện lượt đặt giá này!" +
                            "\nSố dư hiện tại: " + String.format("%,.0f VNĐ", userBalance) +
                            "\nSố tiền bạn muốn đặt: " + String.format("%,.0f VNĐ", inputBidAmount));
                    return;
                }

                // --- ĐỦ ĐIỀU KIỆN: TIẾN HÀNH CẬP NHẬT DỮ LIỆU ---
                selectedProduct.setCurrentPriceNum(inputBidAmount); // Cập nhật lại giá mới của sản phẩm
                auctionTableView.refresh(); // Ép bảng cập nhật lại giao diện ngay lập tức

                showMockAlert("Đặt giá thành công", "Chúc mừng! Bạn đã trở thành người dẫn đầu phiên với mức giá đặt: " + String.format("%,.0f VNĐ", inputBidAmount));

            } catch (NumberFormatException e) {
                showMockAlert("Lỗi Định Dạng", "Vui lòng nhập một số tiền hợp lệ (chỉ bao gồm các ký tự số)!");
            }
        }
    }

    @FXML
    private void handleDepositMoney() {
        // NÂNG CẤP CHỨC NĂNG NẠP TIỀN (DEPOSIT)
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Nạp Tiền Vào Tài Khoản");
        dialog.setHeaderText("Cổng nạp tiền ví điện tử hệ thống");
        dialog.setContentText("Số dư hiện tại: " + String.format("%,.0f VNĐ", userBalance) + "\n\nNhập số tiền bạn muốn nạp (VNĐ):");

        Optional<String> result = dialog.showAndWait();
        if (result.isPresent()) {
            try {
                double depositAmount = Double.parseDouble(result.get().trim());

                // Kiểm tra số tiền nạp phải lớn hơn 0
                if (depositAmount <= 0) {
                    showMockAlert("Lỗi Nạp Tiền", "Số tiền nạp vào hệ thống phải lớn hơn 0 VNĐ!");
                    return;
                }

                // Cộng dồn vào ví hiện có và làm mới thông tin giao diện hiển thị
                this.userBalance += depositAmount;
                updateUI();

                showMockAlert("Nạp Tiền Thành Công", "Đã nạp thành công " + String.format("%,.0f VNĐ", depositAmount) + " vào ví tài khoản của bạn.");

            } catch (NumberFormatException e) {
                showMockAlert("Lỗi Định Dạng", "Vui lòng nhập số tiền hợp lệ không chứa ký tự chữ!");
            }
        }
    }

    @FXML
    private void handleNewSale() {
        showMockAlert("Tạo tài sản mới (New Sale)", "Mở form đăng sản phẩm đấu giá mới!");
    }

    @FXML
    private void handleLogout() {
        // SỬA LỖI LOGOUT: Thêm cảnh báo xác nhận và chuyển hướng về màn hình đăng nhập
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Xác nhận đăng xuất");
        alert.setHeaderText(null);
        alert.setContentText("Bạn có chắc chắn muốn đăng xuất khỏi hệ thống không?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            // Xóa thông tin phiên làm việc cũ về trạng thái mặc định
            UserSession.currentUser = null;
            UserSession.loggedInUsername = "Guest";
            UserSession.loggedInRole = "Buyer";

            try {
                // Tự động chuyển scene quay về giao diện Login ban đầu
                SceneManager.switchScene("login.fxml", "Auction System - Login");
            } catch (IOException e) {
                showMockAlert("Lỗi Hệ Thống", "Không thể quay lại màn hình đăng nhập!");
                e.printStackTrace();
            }
        }
    }

    private void showMockAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    // =========================================================================
    // LỚP DỮ LIỆU MẪU ĐÃ ĐƯỢC THÊM CÁC THUỘC TÍNH ẨN THEO YÊU CẦU
    // =========================================================================
    public static class FakeData {
        private final SimpleIntegerProperty id;
        private final SimpleStringProperty name;
        private final SimpleStringProperty price;
        private final SimpleStringProperty status;

        // Các thuộc tính ẩn phục vụ nghiệp vụ chi tiết và kiểm tra
        private final double startingPrice;
        private final double minBidStep;
        private double currentPriceNum;

        public FakeData(int id, String name, double startingPrice, double currentPriceNum, double minBidStep, String status) {
            this.id = new SimpleIntegerProperty(id);
            this.name = new SimpleStringProperty(name);
            this.startingPrice = startingPrice;
            this.currentPriceNum = currentPriceNum;
            this.minBidStep = minBidStep;
            this.price = new SimpleStringProperty(String.format("%,.0f VNĐ", currentPriceNum));
            this.status = new SimpleStringProperty(status);
        }

        public double getStartingPrice() {
            return startingPrice;
        }

        public double getMinBidStep() {
            return minBidStep;
        }

        public double getCurrentPriceNum() {
            return currentPriceNum;
        }

        public void setCurrentPriceNum(double newPrice) {
            this.currentPriceNum = newPrice;
            this.price.set(String.format("%,.0f VNĐ", newPrice)); // Đồng bộ text hiển thị lên TableView
        }

        public SimpleIntegerProperty idProperty() { return id; }
        public SimpleStringProperty nameProperty() { return name; }
        public SimpleStringProperty priceProperty() { return price; }
        public SimpleStringProperty statusProperty() { return status; }
    }
}