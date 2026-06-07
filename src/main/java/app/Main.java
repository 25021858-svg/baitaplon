package app;

import javafx.application.Application;
import javafx.stage.Stage;
import util.SceneManager;

public class Main extends Application {
    @Override
    public void start(Stage stage) {
        SceneManager.setStage(stage);
        SceneManager.switchScene("/view/login.fxml", "Auction System - Login");
    }

    public static void main(String[] args) {
        launch();
    }
}