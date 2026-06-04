package com.example.auction;

import com.example.auction.util.SceneManager;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URL;

public class Main extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        // Cung cấp stage cho SceneManager để chuyển màn hình không bị NullPointerException
        SceneManager.setStage(stage);

        // Tìm kiếm file fxml linh hoạt để triệt tiêu lỗi sập app khi chạy trên các máy khác nhau
        URL fxmlLocation = getClass().getResource("view/login.fxml");

        if (fxmlLocation == null) {
            throw new IOException("Không tìm thấy file login.fxml. Vui lòng kiểm tra lại vị trí đặt file!");
        }

        FXMLLoader fxmlLoader = new FXMLLoader(fxmlLocation);
        Scene scene = new Scene(fxmlLoader.load(), 700, 450);
        stage.setTitle("Auction System - Login");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}