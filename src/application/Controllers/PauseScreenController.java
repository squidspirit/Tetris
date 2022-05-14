package application.controllers;

import application.controllers.SceneController.Scenes;
import application.controllers.SoundController.Sounds;
import application.interfaces.Initializable;
import application.interfaces.KeyPressed;
import javafx.animation.AnimationTimer;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

public class PauseScreenController implements KeyPressed, Initializable {
    
    @FXML private Label titleLabel;
    @FXML private Label subtitleLabel;

    @Override
    public void initialiaze() {
        defaultTimer.start();
    }

    @Override
    public void keyPressed(KeyEvent keyEvent) {
        if (keyEvent.getCode() == KeyCode.ESCAPE) {
            defaultTimer.stop();
            SoundController.play(Sounds.PAUSE_OUT);
            SceneController.show(Scenes.PLAY_SCREEN, false);
        }
    }

    AnimationTimer defaultTimer = new AnimationTimer() {
        private int accumulatedFrames;
        
        public void handle(long now) {
            if (accumulatedFrames == 40) {
                subtitleLabel.setVisible(false);
            }
            else if (accumulatedFrames == 80) {
                subtitleLabel.setVisible(true);
                accumulatedFrames = 0;
            }
            accumulatedFrames ++;
        };

        public void start() {
            titleLabel.setVisible(true);
            subtitleLabel.setVisible(true);
            accumulatedFrames = 0;
            super.start();
        };
    };
}
