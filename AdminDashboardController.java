package com.example.auction.controller;

import com.example.auction.util.SceneManager;
import javafx.fxml.FXML;

public class AdminDashboardController {

    @FXML
    private void handleLogout() {
        SceneManager.switchScene(
                "/com/example/auction/view/login.fxml",
                "Auction System - Login"
        );
    }
}