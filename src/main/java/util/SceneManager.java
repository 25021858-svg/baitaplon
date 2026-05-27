package util;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

public class SceneManager {
    private static Stage stage;

    private SceneManager() {
    }

    public static void setStage(Stage primaryStage) {
        stage = primaryStage;
    }

    public static void switchScene(String fxmlPath, String title) throws IOException {
        FXMLLoader loader = new FXMLLoader(SceneManager.class.getResource(fxmlPath));
        Scene scene = new Scene(loader.load());

        stage.setTitle(title);
        stage.setScene(scene);
        stage.show();
    }
}