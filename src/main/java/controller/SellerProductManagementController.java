package com.example.auction.controller;

import com.example.auction.util.SceneManager;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

public class SellerProductManagementController {

    @FXML
    private TextField productNameField;

    @FXML
    private TextField startingPriceField;

    @FXML
    private ListView<String> productListView;

    @FXML
    public void initialize() {
        productListView.getItems().addAll(
                "Laptop Dell - Starting price: 10000000",
                "iPhone 13 - Starting price: 9000000"
        );
    }

    @FXML
    private void handleAddProduct() {
        String productName = productNameField.getText().trim();
        String price = startingPriceField.getText().trim();

        if (productName.isEmpty() || price.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Invalid input", "Please fill in all fields!");
            return;
        }

        try {
            double startingPrice = Double.parseDouble(price);

            if (startingPrice <= 0) {
                showAlert(Alert.AlertType.ERROR, "Invalid price", "Price must be greater than 0!");
                return;
            }

            productListView.getItems().add(
                    productName + " - Starting price: " + startingPrice
            );

            productNameField.clear();
            startingPriceField.clear();

        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Invalid price", "Please enter a valid number!");
        }
    }

    @FXML
    private void handleDeleteProduct() {
        String selectedProduct = productListView.getSelectionModel().getSelectedItem();

        if (selectedProduct == null) {
            showAlert(Alert.AlertType.WARNING, "No product selected", "Please select a product first!");
            return;
        }

        productListView.getItems().remove(selectedProduct);
    }

    @FXML
    private void handleLogout() {
        SceneManager.switchScene(
                "/com/example/auction/view/login.fxml",
                "Auction System - Login"
        );
    }

    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}