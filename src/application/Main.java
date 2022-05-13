package application;

import application.Constants.FXMLFiles;
import application.Controllers.SceneController;
import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        stage.setTitle("TETRIS");
        stage.setResizable(false);

        try (SceneController sceneController = new SceneController(FXMLFiles.PLAY_SCREEN)) {
            sceneController.show(stage);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }
}
