package com.auction.client;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class HelloController {
    @FXML private TableView<AuctionItem> auctionTable;
    @FXML private TableColumn<AuctionItem, String> nameColumn;
    @FXML private TableColumn<AuctionItem, Double> priceColumn;

    @FXML
    public void initialize() {
        // Cài đặt cách lấy dữ liệu cho từng cột
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));

        // Nạp dữ liệu giả vào bảng
        auctionTable.setItems(FXCollections.observableArrayList(
                new AuctionItem("iPhone 15 Pro", 1200.0),
                new AuctionItem("Macbook M3", 2500.0),
                new AuctionItem("Đồng hồ Rolex", 15000.0),
                new AuctionItem("Little St.James", 67676767.42)
        ));
    }
}