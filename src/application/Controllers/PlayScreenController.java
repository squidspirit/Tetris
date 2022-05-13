package application.Controllers;

import application.KeyPressed;
import application.Vector2D;
import application.Constants.Arguments;
import application.Objects.Block;
import application.Objects.Shape;
import application.Objects.ShapeType;
import javafx.animation.AnimationTimer;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;

public class PlayScreenController implements KeyPressed {
    
    @FXML private Label scoreLabel; // 40 100 300 1200
    @FXML private Label lineLabel;
    @FXML private Pane gamePane;
    @FXML private Pane nextPane;

    private int score;
    private int line;
    private Shape shape;
    private Shape nextShape;
    private boolean isPlaying;

    @FXML
    private void initialize() {
        init();
    }

    private void init() {
        score = 0;
        scoreLabel.setText(String.valueOf(score));
        line = 0;
        lineLabel.setText(String.valueOf(line));
        isPlaying = false;
        gamePane.getChildren().clear();
    }

    private <T> boolean noneNullArray(T[] array) {
        for (T elm : array)
            if (elm == null) return false;
        return true;
    }

    private void gameOver() {
        timer.stop();
    }

    private void removeLines() {
        Vector2D gamePaneSize = new Vector2D(
            gamePane.getWidth(),
            gamePane.getHeight()
        ).devide(Arguments.BLOCK_SIZE);
        Block[][] blocks = new Block[gamePaneSize.getY()][gamePaneSize.getX()];
        int[] lines = new int[gamePaneSize.getY()];
        for (Object block : gamePane.getChildren()) {
            if (block instanceof Block) {
                Vector2D pos = ((Block)block).getPosition();
                if (pos.getY() < 0) {
                    gameOver();
                    return;
                }
                lines[pos.getY()] ++;
                blocks[pos.getY()][pos.getX()] = (Block)block;
            }
        }
        for (int i = 0; i < gamePaneSize.getY(); i ++) {
            if (noneNullArray(blocks[i])) {
                for (int j = 0; j < i; j ++)
                    for (Block block : blocks[j])
                        if (block != null)
                            block.setPosition(block.getPosition().add(new Vector2D(0, 1)));
                gamePane.getChildren().removeAll(blocks[i]);
            }
        }
    }

    @Override
    public void keyPressed(KeyEvent keyEvent) {
        if (!isPlaying) {
            nextShape = new Shape(ShapeType.getRandom(), nextPane, Arguments.BLOCK_SIZE);
            nextShape.setPosition(new Vector2D(0, 0));
            timer.start();
            isPlaying = true;
            return;
        }
        if (shape == null) {
            shape = new Shape(nextShape.getShapeType(), gamePane, Arguments.BLOCK_SIZE);
            nextPane.getChildren().clear();
            nextShape = new Shape(ShapeType.getRandom(), nextPane, Arguments.BLOCK_SIZE);
            nextShape.setPosition(new Vector2D(0, 0));
            return;
        }
        switch (keyEvent.getCode()) {
            case UP:
                shape.rotate();
                break;
            case DOWN:
                if (!shape.drop()) {
                    shape.decompose();
                    removeLines();
                    shape = null;
                }
                break;
            case LEFT:
                shape.left();
                break;
            case RIGHT:
                shape.right();
                break;
            default: return;
        }
    }

    AnimationTimer timer = new AnimationTimer() {
        long last = 0;

        public void handle(long now) {
            if (now - last >= Arguments.COOL_TIME) {
                if (shape == null) {
                    shape = new Shape(nextShape.getShapeType(), gamePane, Arguments.BLOCK_SIZE);
                    nextPane.getChildren().clear();
                    nextShape = new Shape(ShapeType.getRandom(), nextPane, Arguments.BLOCK_SIZE);
                    nextShape.setPosition(new Vector2D(0, 0));
                }
                else if (!shape.drop()) {
                    shape.decompose();
                    removeLines();
                    shape = null;
                }
                last = now;
            }
        };
    };
}
