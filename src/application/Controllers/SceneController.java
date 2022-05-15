package application.controllers;

import java.io.IOException;

import application.interfaces.Initializable;
import application.interfaces.KeyPressed;
import application.interfaces.Pausable;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.fxml.LoadException;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

public class SceneController {

    public enum Scenes {

        PLAY_SCREEN(new FXMLLoader(SceneController.class.getResource("/resources/PlayScreen.fxml"))),
        PAUSE_SCREEN(new FXMLLoader(SceneController.class.getResource("/resources/PauseScreen.fxml")));

        private FXMLLoader loader;
        private Scene scene;
        private Object controller;

        Scenes(FXMLLoader loader) {
            this.loader = loader;
            try {
                this.scene = new Scene((Parent)loader.load());
                this.controller = loader.getController();
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        }

        private Scene getScene() { return scene; }
        private Object getController() { return controller; }

        private void reload() {
            try {
                this.scene = new Scene((Parent)loader.load());
            } catch (LoadException exception) {
                // Do nothing
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        }
    }
    


    public static Stage stage;

    public static void show(Scenes loader) {
        show(loader, true);
    }

    public static void show(Scenes loader, boolean newLoad) {
        if (newLoad) reload(loader);

        stage.setScene(loader.getScene());
        stage.show();

        if (newLoad) {
            loader.getScene().setOnKeyPressed(new EventHandler<KeyEvent>() {
                @Override
                public void handle(KeyEvent event) {
                    if (loader.getController() instanceof KeyPressed)
                        ((KeyPressed)loader.getController()).keyPressed(event);
                }
            });

            if (loader.getController() instanceof Initializable)
                ((Initializable)loader.getController()).initialiaze();
        }
        else {
            if (loader.getController() instanceof Pausable)
                ((Pausable)loader.getController()).resume();
        }
    }

    public static void reload(Scenes loader) {
        loader.reload();
    }
}
