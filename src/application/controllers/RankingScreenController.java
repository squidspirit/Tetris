package application.controllers;

import java.util.ArrayList;

import application.client.Client;
import application.controllers.SceneController.Scenes;
import application.controllers.SoundController.Sounds;
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

    final private String ENTER_HINT = "ENTER YOUR NAME TO UPLOAD\nYOUR SCORE TO THE SERVER";
    final private String ESCAPE_HINT = "PRESS ESC TO BACK TO GAME\n ";

    @FXML private Label scoreLabel;
    @FXML private Label nameLabel;
    @FXML private Label hintLabel;
    @FXML private Label cursorLabel;
    @FXML private Label chartTitleLabel;
    @FXML private VBox boardVBox;
    
    private boolean enterLock;
    private int score;
    private String name;
    private LeaderBoard leaderBoard;
    private ArrayList<String> dataList = new ArrayList<>();

    @Override
    public void init() {
        name = "";
        nameLabel.setText("");
        enterLock = false;
        score = ((PlayScreenController)SceneController.getController(Scenes.PLAY_SCREEN)).getScore();
        scoreLabel.setText(String.format("%06d", score));
        chartTitleLabel.setText(String.format("%3s %6s %10s", "ORD", "SCORE", "NAME"));
        hintLabel.setText(ENTER_HINT);
        dataList = requestLeaderBoard();
        boardVBox.getChildren().clear();
        leaderBoard = new LeaderBoard(dataList);
        boardVBox.getChildren().add(leaderBoard);
        cursorTimer.start();
    }

    private void updateLeaderBoard() {
        dataList = requestLeaderBoard();
        leaderBoard.setList(dataList);
        SoundController.play(Sounds.PAUSE_IN);
    }


    @Override
    public void keyPressed(KeyEvent keyEvent) {
        if (dataList.isEmpty() && keyEvent.getCode() == KeyCode.SPACE)
            updateLeaderBoard();
        
        if (keyEvent.getCode() == KeyCode.ESCAPE) {
            SceneController.show(Scenes.PLAY_SCREEN, false);
            SoundController.play(Sounds.PAUSE_OUT);
        }
        else if (keyEvent.getCode() == KeyCode.UP) {
            if (leaderBoard.scrollUp()) SoundController.play(Sounds.MOVE);
        }
        else if (keyEvent.getCode() == KeyCode.DOWN) {
            if (leaderBoard.scrollDown()) SoundController.play(Sounds.MOVE);
        }
        
        String keyText = keyEvent.getText().toUpperCase();
        if (enterLock || dataList.isEmpty()) return;
        if (keyText.length() > 0 && name.length() < 10 &&
                keyText.charAt(0) >= 'A' && keyText.charAt(0) <= 'Z') {
            name += keyText;
            nameLabel.setText(name);
            SoundController.play(Sounds.MOVE);
        }
        else if (name.length() > 0 && keyEvent.getCode() == KeyCode.BACK_SPACE) {
            name = name.substring(0, name.length() - 1);
            nameLabel.setText(name);
            SoundController.play(Sounds.MOVE);
        }
        else if (keyEvent.getCode() == KeyCode.ENTER) {
            if (name.length() > 0) {
                enterLock = true;
                hintLabel.setText(ESCAPE_HINT);
                hintTimer.start();
                sendData(String.format("%06d,%s", score, name));
                updateLeaderBoard();
                SoundController.play(Sounds.SCORE_BIG);
            }
        }
    }

    AnimationTimer cursorTimer = new AnimationTimer() {
        private int accumulatedFrames;

        public void handle(long now) {
            if (enterLock)
                cursorTimer.stop();

            if (accumulatedFrames == 40)
                cursorLabel.setVisible(false);
            else if (accumulatedFrames == 80) {
                cursorLabel.setVisible(true);
                accumulatedFrames = 0;
            }
            accumulatedFrames ++;
        };

        public void start() {
            accumulatedFrames = 0;
            super.start();
        };

        public void stop() {
            cursorLabel.setVisible(false);
            super.stop();
        };
    };

    AnimationTimer hintTimer = new AnimationTimer() {
        private int accumulatedFrames;
        
        public void handle(long now) {
            if (accumulatedFrames == 40)
                hintLabel.setVisible(false);
            else if (accumulatedFrames == 80) {
                hintLabel.setVisible(true);
                accumulatedFrames = 0;
            }
            accumulatedFrames ++;
        };

        public void start() {
            accumulatedFrames = 0;
            super.start();
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
            System.out.println(exception.getMessage());
        }
    }
}
