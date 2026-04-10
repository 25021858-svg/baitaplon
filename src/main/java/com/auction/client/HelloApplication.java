package com.auction.client;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        //Load controller of auction table
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("auction-list.fxml"));
        //Create the window
        Scene scene = new Scene(fxmlLoader.load(), 960, 720);

        stage.setTitle("Hệ thống Đấu giá - Client");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}