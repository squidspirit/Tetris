package application.main;

import application.controllers.SceneController;
import application.controllers.SceneController.Scenes;
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
        SceneController.show(Scenes.PLAY_SCREEN, true);

        // try (Client client = new Client(Arguments.SERVER_HOST, Arguments.SERVER_PORT)) {
        //     if (!client.send("SEND").equals("ACCEPT")) throw new Exception();
        //     if (!client.send("2000,SQUI").equals("ACCEPT")) throw new Exception();
        //     if (!client.send("REQUEST").equals("ACCEPT")) throw new Exception();
        //     while (!client.send("NEXT").equals("DONE"));
        //     client.send("BYE");
        // } catch (IOException exception) {
        //     System.out.println("Connection timeout.");
        // } catch (Exception exception) {
        //     System.out.println("Server runtime error.");
        // }

    }
}
