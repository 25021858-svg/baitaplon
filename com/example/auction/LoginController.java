package com.example.auction;

import com.example.auction.util.SceneManager;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import java.io.IOException;

public class LoginController {

    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private Label messageLabel;

    @FXML
    private void handleLogin() {
        String username = usernameField.getText().trim();
        String password = passwordField.getText();

        if (username.isBlank() || password.isBlank()) {
            messageLabel.setText("Vui lòng nhập đầy đủ thông tin");
            return;
        }

        // Test logic dựa trên bộ nhớ RAM tạm thời
        if (UserSession.userDatabase.containsKey(username)) {
            UserSession.UserInfo user = UserSession.userDatabase.get(username);
            if (user.password.equals(password)) {
                // Đồng bộ hóa trạng thái phiên chạy tạm
                UserSession.currentUser = user;
                UserSession.loggedInUsername = username;
                UserSession.loggedInRole = user.role;

                messageLabel.setText("Đăng nhập thành công!");

                try {
                    SceneManager.switchScene("auction-list.fxml", "Auction System - Main List");
                } catch (IOException e) {
                    messageLabel.setText("Lỗi: Không tìm thấy giao diện danh sách!");
                    e.printStackTrace();
                }
                return;
            }
        }
        messageLabel.setText("Sai tài khoản hoặc mật khẩu!");
    }

    @FXML
    private void gotoRegister() {
        try {
            SceneManager.switchScene("register.fxml", "Auction System - Register");
        } catch (IOException e) {
            messageLabel.setText("Không thể chuyển sang màn hình đăng ký!");
            e.printStackTrace();
        }
    }
}