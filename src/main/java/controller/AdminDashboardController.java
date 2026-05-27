package controller;

import javafx.fxml.FXML;
import util.SceneManager;

import java.io.IOException;

public class AdminDashboardController {

    @FXML
    private void handleLogout() throws IOException {
        SceneManager.switchScene(
                "/view/Login.fxml",
                "Auction System - Login"
        );
    }
}