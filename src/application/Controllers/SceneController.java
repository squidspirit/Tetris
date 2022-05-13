package application.Controllers;

import java.io.IOException;

import application.KeyPressed;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

public class SceneController implements AutoCloseable {
    
    private FXMLLoader loader;
    private String fxmlPath;
    private Scene scene;

    @Override
    public void close() throws Exception {
        System.gc();
    }

    public SceneController(String fxmlPath) {
        this.fxmlPath = fxmlPath;
    }

    public void show(Stage stage) throws IOException {
        loader = new FXMLLoader(getClass().getResource(fxmlPath));
        scene = new Scene((Parent)loader.load());
        stage.setScene(scene);
        stage.show();

        scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                Object controller = loader.getController();
                if (controller instanceof KeyPressed)
                    ((KeyPressed)controller).keyPressed(event);
            }
        });
    }

    public void show(ActionEvent event) throws IOException {
        show((Stage)((Node)event.getSource()).getScene().getWindow());
    }
}
