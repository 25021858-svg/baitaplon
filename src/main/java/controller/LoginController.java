package controller;

import client.AuctionClient;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import server.Response;
import util.SceneManager;

public class LoginController {
    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Label messageLabel;

    private final AuctionClient auctionClient = new AuctionClient();

    @FXML
    private void handleLogin() {
        String username = usernameField.getText();
        String password = passwordField.getText();

        if (username.isBlank() || password.isBlank()) {
            messageLabel.setText("Vui long nhap username va password");
            return;
        }

        Response response = auctionClient.login(username, password);

        if (!response.isSuccess()) {
            messageLabel.setText(response.getMessage());
            return;
        }

        saveSession(response);

        if (UserSession.isAdmin()) {
            SceneManager.switchScene("/view/admin-dashboard.fxml", "Admin Dashboard");
        } else if (UserSession.isSeller()) {
            SceneManager.switchScene("/view/seller-product-management.fxml", "Seller Product Management");
        } else {
            SceneManager.switchScene("/view/auction-list.fxml", "Auction List");
        }
    }

    @FXML
    private void goToRegister() {
        SceneManager.switchScene("/view/register.fxml", "Register");
    }

    private void saveSession(Response response) {
        Object data = response.getData();

        if (data == null) {
            UserSession.login(0, usernameField.getText(), "BIDDER");
            return;
        }

        String text = String.valueOf(data);
        String[] parts = text.split(",");

        if (parts.length >= 3) {
            int userId = Integer.parseInt(parts[0].trim());
            String username = parts[1].trim();
            String role = parts[2].trim();

            UserSession.login(userId, username, role);
        } else {
            UserSession.login(0, usernameField.getText(), "BIDDER");
        }
    }
}