package com.example.auction;

import com.example.auction.service.AuthService;
import com.example.auction.util.SceneManager;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import com.example.auction.service.AuthService;
import com.example.auction.model.User;

import java.io.IOException;
import java.sql.SQLException;

public class LoginController {

    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private Label messageLabel;
    private final AuthService authService = new AuthService();

    @FXML
    private void handleLogin() {
        String username = usernameField.getText().trim();
        String password = passwordField.getText();

        if (username.isBlank() || password.isBlank()) {
            messageLabel.setText("Vui lòng nhập đầy đủ thông tin");
            return;
        }

        try {
            User loggedInUser = authService.login(username, password);

            // --- ĐÂY LÀ ĐOẠN MÓC NỐI SANG USERSESSION MỚI ---
            UserSession.currentUser = loggedInUser; // Tự động nhận diện dù là Bidder, Seller hay Admin
            UserSession.loggedInUsername = loggedInUser.getUsername();
            UserSession.loggedInRole = loggedInUser.getRole().name(); // Thêm .name() để biến Enum thành String cho khớp kiểu dữ liệu
            // -----------------------------------------------

            messageLabel.setText("Đăng nhập thành công!");
            SceneManager.switchScene("view/auction-list.fxml", "Auction System - Main List");

        } catch (IllegalArgumentException e) {
            // Hứng lỗi sai mật khẩu / tài khoản từ AuthService đưa lên
            messageLabel.setText(e.getMessage());
        } catch (SQLException e) {
            // Hứng lỗi nếu SQLite sập hoặc chưa tạo bảng
            messageLabel.setText("Lỗi kết nối cơ sở dữ liệu!");
            e.printStackTrace();
        } catch (IOException e) {
            // Hứng lỗi nếu không tìm thấy file fxml màn hình sau
            messageLabel.setText("Lỗi tải giao diện tiếp theo!");
            e.printStackTrace();
        }
    }

    @FXML
    private void gotoRegister() {
        try {
            SceneManager.switchScene("view/register.fxml", "Auction System - Register");
        } catch (IOException e) {
            messageLabel.setText("Không thể chuyển sang màn hình đăng ký!");
            e.printStackTrace();
        }
    }
}