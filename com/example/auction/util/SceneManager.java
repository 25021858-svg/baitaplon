package com.example.auction.util;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URL;

public class SceneManager {
    private static Stage primaryStage;

    public static void setStage(Stage stage) {
        primaryStage = stage;
    }

    public static void switchScene(String fxmlFileName, String title) throws IOException {
        if (primaryStage == null) {
            throw new IllegalStateException("Primary Stage chưa được cấu hình.");
        }

        // Thử tìm file fxml theo các đường dẫn tương đối và tuyệt đối phục vụ test nhanh
        URL fxmlLocation = SceneManager.class.getResource("/com/example/auction/" + fxmlFileName);
        if (fxmlLocation == null) {
            fxmlLocation = SceneManager.class.getResource(fxmlFileName);
        }
        if (fxmlLocation == null) {
            fxmlLocation = SceneManager.class.getResource("../" + fxmlFileName);
        }

        if (fxmlLocation == null) {
            throw new IOException("Không thể định vị file FXML: " + fxmlFileName);
        }

        FXMLLoader loader = new FXMLLoader(fxmlLocation);
        Scene scene = new Scene(loader.load(), 700, 450);
        primaryStage.setTitle(title);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}