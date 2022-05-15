package application.controllers;

import application.controllers.SceneController.Scenes;
import application.controllers.SoundController.Sounds;
import application.functions.Vector2D;
import application.interfaces.Initializable;
import application.interfaces.KeyPressed;
import application.interfaces.Pausable;
import application.main.Arguments;
import application.objects.Block;
import application.objects.Shape;
import application.objects.ShapeType;
import javafx.animation.AnimationTimer;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

public class PlayScreenController implements KeyPressed, Initializable, Pausable {
    
    @FXML private Label levelLabel;
    @FXML private Label scoreLabel;
    @FXML private Label lineScoreLabel;
    @FXML private Label startLabel;
    @FXML private Pane boarderPane;
    @FXML private Pane gamePane;
    @FXML private Pane nextPane;
    @FXML private GridPane root;

    private int level;
    private int score;
    private int lineScore;
    private int interruptTimer;
    private Shape shape;
    private Shape nextShape;
    private boolean isPlaying;
    private boolean isPlayable;
    private boolean isDead;
    private boolean dropLock;
    private Vector2D gamePaneSize;

    @Override
    public void initialiaze() {
        init();
        defaultTimer.start();
    }

    @Override
    public void resume() {
        isPlayable = true;
    }

    private void init() {
        gamePaneSize = new Vector2D(
            gamePane.getWidth(),
            gamePane.getHeight()
        ).devide(Arguments.BLOCK_SIZE);
        level = 0;
        levelLabel.setText(String.format("%03d", level));
        score = 0;
        scoreLabel.setText(String.format("%06d", score));
        lineScore = 0;
        lineScoreLabel.setText(String.format("%06d", lineScore));
        interruptTimer = -1;
        isPlaying = false;
        isPlayable = false;
        isDead = false;
        dropLock = false;
        gamePane.getChildren().clear();
        nextPane.getChildren().clear();
        System.gc();
    }

    private <T> boolean noneNullArray(T[] array) {
        for (T elm : array)
            if (elm == null) return false;
        return true;
    }

    private void gameOver() {
        isPlaying = false;
        isPlayable = false;
        isDead = true;
        SoundController.stoploop(Sounds.BGM);
        gameTimer.stop();
        defaultTimer.start();
    }

    private void removeLinesAndDead() {
        if (dropLock) return;
        dropLock = true;
        shape.decompose();
        Block[][] blocks = new Block[gamePaneSize.getY()][gamePaneSize.getX()];
        int[] lines = new int[gamePaneSize.getY()];
        int hasRemoved = 0;
        for (Object block : gamePane.getChildren()) {
            if (block instanceof Block) {
                Vector2D pos = ((Block)block).getPosition();
                if (pos.getY() < 0) {
                    gameOver();
                    shape = null;
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
                hasRemoved ++;
                System.gc();
            }
        }
        lineScore += hasRemoved;
        switch (hasRemoved) {
            case 1: score +=   40; SoundController.play(Sounds.SCORE_SMALL); break;
            case 2: score +=  100; SoundController.play(Sounds.SCORE_SMALL); break;
            case 3: score +=  300; SoundController.play(Sounds.SCORE_SMALL); break;
            case 4:
                score += 1200;
                SoundController.play(Sounds.SCORE_BIG);
                flashTimer.start();
                break;
            default: break;
        }
        if (lineScore >= Arguments.LEVELUP_REQUIRE[level]) {
            SoundController.play(Sounds.LEVELUP);
            levelLabel.setText(String.format("%03d", ++ level));
        }
        scoreLabel.setText(String.format("%06d", score));
        lineScoreLabel.setText(String.format("%06d", lineScore));
        shape = null;
        dropLock = false;
        isPlayable = true;
    }

    @Override
    public void keyPressed(KeyEvent keyEvent) {
        if (!isPlayable) return;
        if (!isPlaying) {
            if (keyEvent.getCode() == KeyCode.ENTER) {
                if (isDead) init();
                SoundController.play(Sounds.START);
                nextShape = new Shape(ShapeType.getRandom(), nextPane, Arguments.BLOCK_SIZE);
                nextShape.setPosition(new Vector2D(1, 1));
                SoundController.playloop(Sounds.BGM);
                gameTimer.start();
                defaultTimer.stop();
                isPlaying = true;
            }
            return;
        }
        if (shape == null) return;
        switch (keyEvent.getCode()) {
            case LEFT:
                SoundController.play(Sounds.MOVE);
                shape.left();
                break;
            case RIGHT:
                SoundController.play(Sounds.MOVE);
                shape.right();
                break;
            case UP:
                SoundController.play(Sounds.MOVE);
                shape.rotate();
                break;
            case DOWN:
                SoundController.play(Sounds.MOVE);
                if (shape.drop()) interruptTimer = 0;
                else interruptTimer = Arguments.FALL_SPEED[level];
                break;
            case SPACE:
                interruptTimer = Arguments.FALL_SPEED[level];
                while (shape.drop());
                break;
            case ESCAPE:
                isPlayable = false;
                SoundController.play(Sounds.PAUSE_IN);
                SceneController.show(Scenes.PAUSE_SCREEN, true);
                break;
            default: return;
        }
    }

    AnimationTimer gameTimer = new AnimationTimer() {
        private int accumulatedFrames;
        // private long last = System.nanoTime();

        public void handle(long now) {
            // System.out.printf("%d %d\n", accumulatedFrames, now - last);
            if (!isPlayable) return;
            if (interruptTimer >= 0) {
                accumulatedFrames = interruptTimer;
                interruptTimer = -1;
            }
            if (accumulatedFrames >= Arguments.FALL_SPEED[level]) {
                if (shape == null) {
                    shape = new Shape(nextShape.getShapeType(), gamePane, Arguments.BLOCK_SIZE);
                    nextPane.getChildren().clear();
                    nextShape = new Shape(ShapeType.getRandom(), nextPane, Arguments.BLOCK_SIZE);
                    nextShape.setPosition(new Vector2D(1, 1));
                }
                else if (!shape.drop()) {
                    SoundController.play(Sounds.LAND);
                    removeLinesAndDead();
                }
                if (shape != null) accumulatedFrames = 0;
            }
            else accumulatedFrames ++;
            // last = now;
        };

        public void start() {
            accumulatedFrames = Arguments.LEVELUP_REQUIRE[0];
            super.start();
            nextPane.getChildren().clear();
        };
    };

    AnimationTimer defaultTimer = new AnimationTimer() {
        private int accumulatedFrames;
        private Vector2D boardBlockIndex;
        private Vector2D nextBlockDirection;
        private boolean boardDone;

        public void handle(long now) {
            if (accumulatedFrames % 40 == 0) {
                if (accumulatedFrames % 80 == 0)
                    startLabel.setVisible(true);
                else startLabel.setVisible(false);
            }

            if (!boardDone) {
                Block block = new Block(Arguments.BLOCK_SIZE);
                boarderPane.getChildren().add(block);
                block.getRectangle().setFill(Color.web("#404040"));
                block.getRectangle().setStroke(Color.BLACK);
                block.getRectangle().setStrokeWidth(4);
                block.setPosition(boardBlockIndex);
                
                if (boardBlockIndex.add(nextBlockDirection).overBound(
                        new Vector2D(0, 0), gamePaneSize.add(new Vector2D(1, 1)))) {
                    if (nextBlockDirection.equals(new Vector2D(1, 0)))
                        nextBlockDirection = new Vector2D(0, 1);
                    else if (nextBlockDirection.equals(new Vector2D(0, 1)))
                        nextBlockDirection = new Vector2D(-1, 0);
                    else if (nextBlockDirection.equals(new Vector2D(-1, 0)))
                        nextBlockDirection = new Vector2D(0, -1);
                    else {
                        isPlayable = true;
                        boardDone = true;
                    }
                }
                boardBlockIndex = boardBlockIndex.add(nextBlockDirection);
            }
            accumulatedFrames ++;
        };

        public void start() {
            SoundController.play(Sounds.BORDER);
            isPlayable = false;
            boardDone = false;        
            boarderPane.getChildren().clear();
            boardBlockIndex = new Vector2D(0, 0);
            nextBlockDirection = new Vector2D(1, 0);
            accumulatedFrames = 0;
            flashTimer.start();
            super.start();
        };

        public void stop() {
            isPlayable = true;
            startLabel.setVisible(false);
            super.stop();
        };
    };

    AnimationTimer flashTimer = new AnimationTimer() {
        private int accumulatedFrames;
        private int flashed;

        public void handle(long now) {
            if (flashed == 2) this.stop();
            if (accumulatedFrames == 4)
                root.setBackground(new Background(new BackgroundFill(Color.BLACK, CornerRadii.EMPTY, Insets.EMPTY)));
            else if (accumulatedFrames == 8) {
                root.setBackground(new Background(new BackgroundFill(Color.LIGHTGRAY, CornerRadii.EMPTY, Insets.EMPTY)));
                accumulatedFrames = 0;
                flashed ++;
            }
            accumulatedFrames ++;
        };

        public void start() {
            accumulatedFrames = 0;
            flashed = 0;
            root.setBackground(new Background(new BackgroundFill(Color.LIGHTGRAY, CornerRadii.EMPTY, Insets.EMPTY)));
            super.start();
        };

        public void stop() {
            root.setBackground(new Background(new BackgroundFill(Color.BLACK, CornerRadii.EMPTY, Insets.EMPTY)));
            super.stop();
        };
    };
}
