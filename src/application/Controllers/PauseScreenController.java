package application.Controllers;

import application.Initializable;
import application.KeyPressed;
import application.Controllers.SceneController.Loader;
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
            SceneController.show(Loader.PLAY_SCREEN, false);
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
