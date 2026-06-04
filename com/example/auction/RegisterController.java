package com.example.auction;

import com.example.auction.service.AuthService;
import com.example.auction.model.Role;
import com.example.auction.util.SceneManager;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import java.io.IOException;
import java.sql.SQLException;

public class RegisterController {

    // Ánh xạ chính xác tới các Textbox, ComboBox, Label trên file FXML giao diện
    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private PasswordField confirmPasswordField;
    @FXML private ComboBox<String> roleComboBox;
    @FXML private Label messageLabel;

    // Khởi tạo AuthService để tương tác với SQLite thông qua UserDao
    private final AuthService authService = new AuthService();

    @FXML
    public void initialize() {
        // Đổ dữ liệu hiển thị trực quan ra ComboBox cho người dùng chọn khi màn hình hiện lên
        roleComboBox.setItems(FXCollections.observableArrayList("Bidder", "Seller"));
        roleComboBox.setValue("Bidder"); // Mặc định ban đầu chọn sẵn Buyer
    }

    @FXML
    private void handleRegister() {
        // Lấy dữ liệu người dùng gõ vào các ô Textbox
        String username = usernameField.getText().trim();
        String password = passwordField.getText();
        String confirmPassword = confirmPasswordField != null ? confirmPasswordField.getText() : "";
        String assignedRoleStr = roleComboBox.getValue();

        // 1. Kiểm tra xem người dùng có bỏ trống ô nào không
        if (username.isBlank() || password.isBlank() || confirmPassword.isBlank()) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("LỖI");
            alert.setHeaderText(null);
            alert.setContentText("Vui lòng nhập đầy đủ thông tin");
            alert.showAndWait();
            return;
        }

        // 2. Kiểm tra mật khẩu nhập lại có khớp không
        if (!password.equals(confirmPassword)) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("LỖI");
            alert.setHeaderText(null);
            alert.setContentText("Mật khẩu nhập lại không trùng khớp");
            alert.showAndWait();
            return;
        }

        // 3. ĐỒNG BỘ VAI TRÒ: Chuyển đổi từ chữ hiển thị giao diện sang Enum hệ thống
        Role assignedRole = Role.BIDDER; // "Buyer" trên giao diện tương ứng với role BIDDER trong DB
        if ("Seller".equals(assignedRoleStr)) {
            assignedRole = Role.SELLER;  // "Seller" tương ứng với role SELLER trong DB
        }

        try {
            // 4. Gọi AuthService để thực hiện ghi trực tiếp tài khoản mới này xuống SQLite
            authService.register(username, password, assignedRole);

            // 5. Nếu ghi thành công và không văng lỗi -> Hiện Pop-up chúc mừng thông báo
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Đăng ký thành công");
            alert.setHeaderText(null);
            alert.setContentText("Tài khoản '" + username + "' với vai trò [" + assignedRoleStr + "] đã được tạo thành công!");
            alert.showAndWait();

            // 6. Tự động chuyển hướng người dùng quay lại màn hình Login để test đăng nhập
            gotoLogin();

        } catch (IllegalArgumentException e) {
            // Hứng các lỗi bắt trùng lặp (Ví dụ: "Username da ton tai") từ tầng Service rồi in lên giao diện
            messageLabel.setText(e.getMessage());
        } catch (SQLException e) {
            // Hứng lỗi trong trường hợp kết nối cơ sở dữ liệu gặp sự cố
            messageLabel.setText("Lỗi kết nối cơ sở dữ liệu khi đăng ký!");
            e.printStackTrace();
        }
    }

    @FXML
    private void gotoLogin() {
        // Hàm xử lý chuyển màn hình mượt mà, hỗ trợ cả 2 kiểu cấu trúc thư mục FXML phổ biến
        try {
            SceneManager.switchScene("view/login.fxml", "Auction System - Login");
        } catch (IOException e) {
            try {
                SceneManager.switchScene("/com/example/auction/login.fxml", "Auction System - Login");
            } catch (IOException ex) {
                messageLabel.setText("Không thể chuyển hướng về màn hình đăng nhập!");
                ex.printStackTrace();
            }
        }
    }
}