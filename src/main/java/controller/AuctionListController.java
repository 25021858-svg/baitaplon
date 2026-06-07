package controller;

import client.AuctionClient;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import server.Response;
import util.SceneManager;

public class AuctionListController {
    @FXML
    private Label welcomeLabel;

    @FXML
    private Label messageLabel;

    @FXML
    private TextArea auctionTextArea;

    @FXML
    private TextField auctionIdField;

    private final AuctionClient auctionClient = new AuctionClient();

    @FXML
    public void initialize() {
        welcomeLabel.setText("Xin chao, " + UserSession.getCurrentUsername()
                + " - Role: " + UserSession.getCurrentRole());
        loadAuctions();
    }

    @FXML
    private void loadAuctions() {
        Response response = auctionClient.getAuctions();

        if (response.isSuccess()) {
            auctionTextArea.setText(String.valueOf(response.getData()));
            messageLabel.setText(response.getMessage());
        } else {
            messageLabel.setText(response.getMessage());
        }
    }

    @FXML
    private void handleOpenAuctionDetail() {
        try {
            int auctionId = Integer.parseInt(auctionIdField.getText());

            UserSession.setSelectedAuctionId(auctionId);
            SceneManager.switchScene("/view/auction-detail.fxml", "Auction Detail");

        } catch (NumberFormatException e) {
            messageLabel.setText("Auction ID khong hop le");
        }
    }

    @FXML
    private void handleRefresh() {
        loadAuctions();
    }

    @FXML
    private void handleLogout() {
        UserSession.logout();
        SceneManager.switchScene("/view/login.fxml", "Login");
    }
}