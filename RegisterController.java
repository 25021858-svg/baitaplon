package controller;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
package controller;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import util.SceneManager;

import java.io.IOException;

public class RegisterController {

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private PasswordField confirmPasswordField;

    @FXML
    private ComboBox<String> roleComboBox;

    @FXML
    private Label messageLabel;

    @FXML
    public void handleRegister() {
        String username = usernameField.getText();
        String password = passwordField.getText();
        String confirmPassword = confirmPasswordField.getText();
        String role = roleComboBox.getValue();

        if (username.isBlank() || password.isBlank() || confirmPassword.isBlank() || role == null) {
            messageLabel.setText("Vui lòng nhập đầy đủ thông tin");
            return;
        }

        if (!password.equals(confirmPassword)) {
            messageLabel.setText("Mật khẩu nhập lại không khớp");
            return;
        }

        messageLabel.setText("Đăng ký tạm thời thành công");
    }

    @FXML
    private void gotoLogin() {
        try {
            SceneManager.switchScene("/view/login.fxml", "Auction System - Login");
        } catch (IOException e) {
            messageLabel.setText("Không thể quay lại màn hình đăng nhập");
            e.printStackTrace();
        }
    }
}