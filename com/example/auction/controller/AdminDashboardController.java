package com.example.auction.controller;

import com.example.auction.util.SceneManager;
import javafx.fxml.FXML;

public class AdminDashboardController {

    @FXML
    private void handleLogout() {
        try {
            SceneManager.switchScene(
                    "/com/example/auction/view/login.fxml",
                    "Auction System - Login"
            );
        } catch (java.io.IOException e) {
            e.printStackTrace(); // In lỗi ra màn hình nếu không tìm thấy file fxml
        }
    }
}