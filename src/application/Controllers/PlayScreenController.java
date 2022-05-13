package application.Controllers;

import application.KeyPressed;
import application.Constants.Arguments;
import application.Objects.Shape;
import application.Objects.ShapeType;
import javafx.animation.AnimationTimer;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;

public class PlayScreenController implements KeyPressed {
    
    @FXML private Label scoreLabel;
    @FXML private Label lineLabel;
    @FXML private Pane gamePane;
    @FXML private Pane nextPane;

    private int score;
    private int line;
    private Shape shape;

    @FXML
    private void initialize() {
        init();
    }

    private void init() {
        score = 0;
        scoreLabel.setText(String.valueOf(score));
        line = 0;
        lineLabel.setText(String.valueOf(line));
    }

    @Override
    public void keyPressed(KeyEvent keyEvent) {
        if (shape == null)
            shape = new Shape(ShapeType.getRandom(), gamePane, Arguments.BLOCK_SIZE);
        else if (!shape.drop()) {
            shape.decompose();
            shape = new Shape(ShapeType.getRandom(), gamePane, Arguments.BLOCK_SIZE);
        }
        
        
    }

    AnimationTimer timer = new AnimationTimer() {
        public void handle(long now) {

        };
    };
}
