package controller;

import client.AuctionClient;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import server.Response;
import util.SceneManager;

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

    private final AuctionClient auctionClient = new AuctionClient();

    @FXML
    public void initialize() {
        roleComboBox.getItems().addAll("BIDDER", "SELLER");
        roleComboBox.setValue("BIDDER");
    }

    @FXML
    private void handleRegister() {
        String username = usernameField.getText();
        String password = passwordField.getText();
        String confirmPassword = confirmPasswordField.getText();
        String role = roleComboBox.getValue();

        if (username.isBlank() || password.isBlank() || confirmPassword.isBlank()) {
            messageLabel.setText("Vui long nhap day du thong tin");
            return;
        }

        if (!password.equals(confirmPassword)) {
            messageLabel.setText("Mat khau xac nhan khong khop");
            return;
        }

        Response response = auctionClient.register(username, password, role);

        if (response.isSuccess()) {
            messageLabel.setText("Dang ky thanh cong");
        } else {
            messageLabel.setText(response.getMessage());
        }
    }

    @FXML
    private void goToLogin() {
        SceneManager.switchScene("/view/login.fxml", "Login");
    }
}