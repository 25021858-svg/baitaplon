package controller;

import client.AuctionClient;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import server.Response;
import util.SceneManager;

import java.math.BigDecimal;

public class AdminDashboardController {
    @FXML
    private Label messageLabel;

    @FXML
    private TextArea itemTextArea;

    @FXML
    private TextArea auctionTextArea;

    @FXML
    private TextField itemIdField;

    @FXML
    private TextField startTimeField;

    @FXML
    private TextField endTimeField;

    @FXML
    private TextField currentPriceField;

    @FXML
    private TextField auctionIdField;

    private final AuctionClient auctionClient = new AuctionClient();

    @FXML
    public void initialize() {
        loadItems();
        loadAuctions();
    }

    @FXML
    private void handleRefresh() {
        loadItems();
        loadAuctions();
    }

    @FXML
    private void handleCreateAuction() {
        try {
            int itemId = Integer.parseInt(itemIdField.getText());
            String startTime = startTimeField.getText();
            String endTime = endTimeField.getText();
            BigDecimal currentPrice = new BigDecimal(currentPriceField.getText());

            Response response = auctionClient.createAuction(
                    itemId,
                    startTime,
                    endTime,
                    currentPrice
            );

            messageLabel.setText(response.getMessage());

            if (response.isSuccess()) {
                clearCreateAuctionForm();
                loadAuctions();
            }

        } catch (NumberFormatException e) {
            messageLabel.setText("Item ID va current price phai la so");
        }
    }

    @FXML
    private void handleStartAuction() {
        try {
            int auctionId = Integer.parseInt(auctionIdField.getText());

            Response response = auctionClient.startAuction(auctionId);
            messageLabel.setText(response.getMessage());

            if (response.isSuccess()) {
                loadAuctions();
            }

        } catch (NumberFormatException e) {
            messageLabel.setText("Auction ID phai la so nguyen");
        }
    }

    @FXML
    private void handleCancelAuction() {
        try {
            int auctionId = Integer.parseInt(auctionIdField.getText());

            Response response = auctionClient.cancelAuction(auctionId);
            messageLabel.setText(response.getMessage());

            if (response.isSuccess()) {
                loadAuctions();
            }

        } catch (NumberFormatException e) {
            messageLabel.setText("Auction ID phai la so nguyen");
        }
    }

    @FXML
    private void handleMarkAsPaid() {
        try {
            int auctionId = Integer.parseInt(auctionIdField.getText());

            Response response = auctionClient.markAsPaid(auctionId);
            messageLabel.setText(response.getMessage());

            if (response.isSuccess()) {
                loadAuctions();
            }

        } catch (NumberFormatException e) {
            messageLabel.setText("Auction ID phai la so nguyen");
        }
    }

    @FXML
    private void handleCloseExpiredAuctions() {
        Response response = auctionClient.closeExpiredAuctions();
        messageLabel.setText(response.getMessage());

        if (response.isSuccess()) {
            loadAuctions();
        }
    }

    @FXML
    private void handleLogout() {
        UserSession.logout();
        SceneManager.switchScene("/view/login.fxml", "Login");
    }

    private void loadItems() {
        Response response = auctionClient.getItems();

        if (response.isSuccess()) {
            itemTextArea.setText(String.valueOf(response.getData()));
        } else {
            itemTextArea.clear();
            messageLabel.setText(response.getMessage());
        }
    }

    private void loadAuctions() {
        Response response = auctionClient.getAuctions();

        if (response.isSuccess()) {
            auctionTextArea.setText(String.valueOf(response.getData()));
            messageLabel.setText(response.getMessage());
        } else {
            auctionTextArea.clear();
            messageLabel.setText(response.getMessage());
        }
    }

    private void clearCreateAuctionForm() {
        itemIdField.clear();
        startTimeField.clear();
        endTimeField.clear();
        currentPriceField.clear();
    }
}