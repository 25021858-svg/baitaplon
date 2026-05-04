package com.example.auction.controller;

import com.example.auction.util.SceneManager;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ListView;

public class AuctionListController {

    @FXML
    private ListView<String> auctionListView;

    @FXML
    public void initialize() {
        auctionListView.getItems().addAll(
                "Laptop Dell - Current price: 10000000",
                "iPhone 13 - Current price: 9000000",
                "Mechanical Keyboard - Current price: 1000000"
        );
    }

    @FXML
    private void handleViewDetail() {
        String selectedAuction = auctionListView.getSelectionModel().getSelectedItem();

        if (selectedAuction == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("No auction selected");
            alert.setHeaderText(null);
            alert.setContentText("Please select an auction first!");
            alert.showAndWait();
            return;
        }

        SceneManager.switchScene(
                "/com/example/auction/view/auction-detail.fxml",
                "Auction Detail"
        );
    }

    @FXML
    private void handleLogout() {
        SceneManager.switchScene(
                "/com/example/auction/view/login.fxml",
                "Auction System - Login"
        );
    }
}