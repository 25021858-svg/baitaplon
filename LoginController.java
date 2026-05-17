package controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import model.User;
import service.AuthService;
import util.SceneManager;

import java.io.IOException;

public class LoginController {

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Label messageLabel;

    private final AuthService authService = new AuthService();

    @FXML
    private void handleLogin() {
        String username = usernameField.getText();
        String password = passwordField.getText();

        if (username.isBlank() || password.isBlank()) {
            messageLabel.setText("Vui lòng nhập đầy đủ thông tin");
            return;
        }

        try {
            //Pass login sang cho ham login trong Authentication Service
            User loggedInUser = authService.login(username, password);

            //Neu chay ham login khong bi loi va tra ve user thanh cong, thong bao ra man hinh
            if (loggedInUser != null) {
                messageLabel.setText("Đăng nhập thành công!");
                //Sau khi thong bao dang nhap thanh cong, switch sang scene danh sach dau gia
                SceneManager.switchScene("/view/auction-list.fxml", "Auction System - Main List");
            }

        } catch (IllegalArgumentException e) {
           //Neu co loi thi thong bao ra man hinh
            messageLabel.setText(e.getMessage());

        } catch (SQLException e) {
            // Bắt lỗi kết nối Database (ví dụ: chưa bật MySQL, lỗi driver...)
            messageLabel.setText("Lỗi kết nối cơ sở dữ liệu!");
            e.printStackTrace();

        } catch (IOException e) {
            // Bắt lỗi khi không tìm thấy hoặc không mở được file auction-list.fxml
            messageLabel.setText("Lỗi hệ thống: Không thể chuyển màn hình!");
            e.printStackTrace();
        }
        messageLabel.setText("Login tạm thời thành công");
    }

    @FXML
    private void gotoRegister() {
        try {
            SceneManager.switchScene("/view/register.fxml", "Auction System - Register");
        } catch (IOException e) {
            messageLabel.setText("Không thể chuyển sang màn hình đăng ký");
            e.printStackTrace();
        }
    }
}