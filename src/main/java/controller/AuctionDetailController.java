package controller;

import client.AuctionClient;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import server.Response;
import util.SceneManager;

import java.math.BigDecimal;

public class AuctionDetailController {
    @FXML
    private Label messageLabel;

    @FXML
    private TextArea auctionDetailTextArea;

    @FXML
    private TextField bidAmountField;

    private final AuctionClient auctionClient = new AuctionClient();

    @FXML
    public void initialize() {
        loadAuctionDetail();
    }

    private void loadAuctionDetail() {
        int auctionId = UserSession.getSelectedAuctionId();

        if (auctionId <= 0) {
            messageLabel.setText("Auction ID khong hop le");
            return;
        }

        Response response = auctionClient.getAuctionById(auctionId);

        if (response.isSuccess()) {
            auctionDetailTextArea.setText(String.valueOf(response.getData()));
            messageLabel.setText(response.getMessage());
        } else {
            auctionDetailTextArea.clear();
            messageLabel.setText(response.getMessage());
        }
    }

    @FXML
    private void handlePlaceBid() {
        if (!UserSession.isLoggedIn()) {
            messageLabel.setText("Ban can dang nhap de dat gia");
            return;
        }

        if (!UserSession.isBidder()) {
            messageLabel.setText("Chi BIDDER moi duoc dat gia");
            return;
        }

        String amountText = bidAmountField.getText();

        if (amountText == null || amountText.isBlank()) {
            messageLabel.setText("Vui long nhap gia muon dat");
            return;
        }

        try {
            int auctionId = UserSession.getSelectedAuctionId();
            int bidderId = UserSession.getCurrentUserId();
            BigDecimal amount = new BigDecimal(amountText);

            Response response = auctionClient.placeBid(auctionId, bidderId, amount);

            messageLabel.setText(response.getMessage());

            if (response.isSuccess()) {
                bidAmountField.clear();
                loadAuctionDetail();
            }

        } catch (NumberFormatException e) {
            messageLabel.setText("Gia dat khong hop le");
        }
    }

    @FXML
    private void handleRefresh() {
        loadAuctionDetail();
    }

    @FXML
    private void goBack() {
        SceneManager.switchScene("/view/auction-list.fxml", "Auction List");
    }
}