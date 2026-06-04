package com.example.auction;

import com.example.auction.UserSession;
import com.example.auction.util.SceneManager;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import java.io.IOException;

public class RegisterController {

    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private PasswordField confirmPasswordField; // Ánh xạ đúng trường nhập lại mật khẩu từ FXML
    @FXML private ComboBox<String> roleComboBox;       // FIX: Khai báo để kết nối với fx:id="roleComboBox"
    @FXML private Label messageLabel;

    @FXML
    public void initialize() {
        // FIX: Đổ dữ liệu giả lập các vai trò vào ComboBox khi màn hình đăng ký khởi chạy
        roleComboBox.setItems(FXCollections.observableArrayList("Buyer", "Seller"));
        roleComboBox.setValue("Buyer"); // Đặt vai trò mặc định ban đầu là Người mua
    }

    @FXML
    private void handleRegister() {
        String username = usernameField.getText().trim();
        String password = passwordField.getText();
        String confirmPassword = confirmPasswordField != null ? confirmPasswordField.getText() : "";
        String assignedRole = roleComboBox.getValue();

        // 1. Kiểm tra tính hợp lệ của dữ liệu đầu vào
        if (username.isBlank() || password.isBlank()) {
            messageLabel.setText("Vui lòng điền đầy đủ thông tin!");
            return;
        }

        // 2. Kiểm tra khớp mật khẩu
        if (confirmPasswordField != null && !password.equals(confirmPassword)) {
            messageLabel.setText("Mật khẩu nhập lại không trùng khớp!");
            return;
        }

        // 3. Kiểm tra tài khoản trùng lặp
        UserSession session = UserSession.getInstance();
        if (session.getUserDatabase().containsKey(username)) {
            messageLabel.setText("Tài khoản này đã tồn tại trên hệ thống!");
            return;
        }

        // 4. Lưu tài khoản mới vào database RAM giả lập
        UserSession.UserInfo newUser = new UserSession.UserInfo(username, password, assignedRole);
        session.getUserDatabase().put(username, newUser);

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Đăng ký thành công");
        alert.setHeaderText(null);
        alert.setContentText("Tài khoản '" + username + "' với vai trò [" + assignedRole + "] đã được tạo thành công!");
        alert.showAndWait();

        gotoLogin();
    }

    @FXML
    private void gotoLogin() {
        try {
            SceneManager.switchScene("login.fxml", "Auction System - Login");
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