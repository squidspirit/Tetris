package application.controllers;

import java.util.ArrayList;

import application.client.Client;
import application.controllers.SceneController.Scenes;
import application.interfaces.Initializable;
import application.interfaces.KeyPressed;
import application.main.Arguments;
import application.objects.LeaderBoard;
import javafx.animation.AnimationTimer;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;

public class RankingScreenController implements KeyPressed, Initializable {
    
    @FXML private Label scoreLabel;
    @FXML private Label nameLabel;
    @FXML private Label hintLabel;
    @FXML private VBox boardVBox;
    
    private boolean enterLock;
    private int score;
    private String name;
    private LeaderBoard leaderBoard;
    private ArrayList<String> dataList = new ArrayList<>();

    @Override
    public void initialiaze() {
        name = "";
        enterLock = false;
        score = ((PlayScreenController)SceneController.getController(Scenes.PLAY_SCREEN)).getScore();
        scoreLabel.setText(String.format("%06d", score));
        dataList = requestLeaderBoard();
        boardVBox.getChildren().clear();
        leaderBoard = new LeaderBoard(dataList);
        boardVBox.getChildren().add(leaderBoard);
    }

    private void updateLeaderBoard() {
        dataList = requestLeaderBoard();
        leaderBoard.setList(dataList);
    }


    @Override
    public void keyPressed(KeyEvent keyEvent) {
        if (keyEvent.getCode() == KeyCode.ESCAPE) {
            SceneController.show(Scenes.PLAY_SCREEN, false);
        }
        
        String keyText = keyEvent.getText().toUpperCase();
        if (enterLock) return;
        if (keyText.length() > 0 && keyText.charAt(0) >= 'A' && keyText.charAt(0) <= 'Z') {
            name += keyText;
            nameLabel.setText(name);
        }
        else if (name.length() > 0 && keyEvent.getCode() == KeyCode.BACK_SPACE) {
            name = name.substring(0, name.length() - 1);
            nameLabel.setText(name);
        }
        else if (keyEvent.getCode() == KeyCode.ENTER) {
            if (name.length() > 0) {
                sendData(String.format("%06d,%s", score, name));
                updateLeaderBoard();
                enterLock = true;
            }
        }
    }

    AnimationTimer textTimer = new AnimationTimer() {
        public void handle(long now) {

        };
    };

    AnimationTimer hintTimer = new AnimationTimer() {
        public void handle(long now) {

        };
    };




    private static ArrayList<String> requestLeaderBoard() {
        ArrayList<String> list = new ArrayList<>();
        String response;
        try (Client client = new Client(Arguments.SERVER_HOST, Arguments.SERVER_PORT)) {
            if (!client.send("REQUEST").equals("ACCEPT")) throw new Exception();
            while (true) {
                response = client.send("NEXT");
                if (response.equals("DONE")) break;
                list.add(response);
            }
        } catch (Exception exception) {
            System.out.println(exception.getMessage());
            return new ArrayList<>();
        }
        return list;
    }

    private static void sendData(String data) {
        try (Client client = new Client(Arguments.SERVER_HOST, Arguments.SERVER_PORT)) {
            if (!client.send("SEND").equals("ACCEPT")) throw new Exception();
            if (!client.send(data).equals("ACCEPT")) throw new Exception();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }
}
