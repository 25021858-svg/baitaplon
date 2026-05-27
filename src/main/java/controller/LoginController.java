package controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import util.SceneManager;

import java.io.IOException;

public class LoginController {

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Label messageLabel;

    @FXML
    private void handleLogin() {
        String username = usernameField.getText();
        String password = passwordField.getText();

        if (username.isBlank() || password.isBlank()) {
            messageLabel.setText("Vui lòng nhập đầy đủ thông tin");
            return;
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