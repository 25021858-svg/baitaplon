package controller;

import client.AuctionClient;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import server.Response;
import util.SceneManager;

import java.math.BigDecimal;

public class SellerProductManagementController {
    @FXML
    private Label welcomeLabel;

    @FXML
    private Label messageLabel;

    @FXML
    private TextArea productTextArea;

    @FXML
    private TextField itemIdField;

    @FXML
    private TextField nameField;

    @FXML
    private TextField descriptionField;

    @FXML
    private TextField startingPriceField;

    @FXML
    private ComboBox<String> itemTypeComboBox;

    private final AuctionClient auctionClient = new AuctionClient();

    @FXML
    public void initialize() {
        welcomeLabel.setText("Xin chao, " + UserSession.getCurrentUsername()
                + " - Role: " + UserSession.getCurrentRole());

        itemTypeComboBox.getItems().addAll("ELECTRONICS", "ART", "VEHICLE");
        itemTypeComboBox.setValue("ELECTRONICS");

        loadSellerProducts();
    }

    @FXML
    private void handleCreateItem() {
        try {
            int sellerId = UserSession.getCurrentUserId();
            String name = nameField.getText();
            String description = descriptionField.getText();
            BigDecimal startingPrice = new BigDecimal(startingPriceField.getText());
            String itemType = itemTypeComboBox.getValue();

            Response response = auctionClient.createItem(
                    sellerId,
                    name,
                    description,
                    startingPrice,
                    itemType
            );

            messageLabel.setText(response.getMessage());

            if (response.isSuccess()) {
                clearForm();
                loadSellerProducts();
            }

        } catch (NumberFormatException e) {
            messageLabel.setText("Gia khoi diem phai la so");
        }
    }

    @FXML
    private void handleUpdateItem() {
        try {
            int itemId = Integer.parseInt(itemIdField.getText());
            String name = nameField.getText();
            String description = descriptionField.getText();
            BigDecimal startingPrice = new BigDecimal(startingPriceField.getText());
            String itemType = itemTypeComboBox.getValue();

            Response response = auctionClient.updateItem(
                    itemId,
                    name,
                    description,
                    startingPrice,
                    itemType
            );

            messageLabel.setText(response.getMessage());

            if (response.isSuccess()) {
                clearForm();
                loadSellerProducts();
            }

        } catch (NumberFormatException e) {
            messageLabel.setText("Item ID va gia khoi diem phai la so");
        }
    }

    @FXML
    private void handleDeleteItem() {
        try {
            int itemId = Integer.parseInt(itemIdField.getText());

            Response response = auctionClient.deleteItem(itemId);
            messageLabel.setText(response.getMessage());

            if (response.isSuccess()) {
                clearForm();
                loadSellerProducts();
            }

        } catch (NumberFormatException e) {
            messageLabel.setText("Item ID phai la so nguyen");
        }
    }

    @FXML
    private void handleRefresh() {
        loadSellerProducts();
    }

    @FXML
    private void handleClearForm() {
        clearForm();
    }

    @FXML
    private void handleLogout() {
        UserSession.logout();
        SceneManager.switchScene("/view/login.fxml", "Login");
    }

    private void loadSellerProducts() {
        int sellerId = UserSession.getCurrentUserId();

        Response response = auctionClient.getItemsBySeller(sellerId);

        if (response.isSuccess()) {
            productTextArea.setText(String.valueOf(response.getData()));
            messageLabel.setText(response.getMessage());
        } else {
            productTextArea.clear();
            messageLabel.setText(response.getMessage());
        }
    }

    private void clearForm() {
        itemIdField.clear();
        nameField.clear();
        descriptionField.clear();
        startingPriceField.clear();
        itemTypeComboBox.setValue("ELECTRONICS");
    }
}