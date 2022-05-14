package application;

import application.Controllers.SceneController;
import application.Controllers.SceneController.Loader;
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
        SceneController.stage = stage;
        SceneController.show(Loader.PLAY_SCREEN, true);
    }
}
