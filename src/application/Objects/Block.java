package application.objects;

import application.funtions.Vector2D;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Block extends Pane {
    
    private Vector2D position;
    private int size;

    private Rectangle block;
    private Rectangle lightSpot;
    private Rectangle lightLeft;
    private Rectangle lightTop;

    public Block(int size) {
        this.size = size;
        super.setWidth(size);
        super.setHeight(size);

        block = new Rectangle(size, size);
        lightSpot = new Rectangle((double)size / 8, (double)size / 6);
        lightLeft = new Rectangle((double)size / 8, (double)size / 3);
        lightTop = new Rectangle((double)size / 1.5, (double)size / 8);
        super.getChildren().addAll(block, lightSpot, lightLeft, lightTop);
        lightSpot.relocate((double)size / 6, (double)size / 1.6);
        lightLeft.relocate((double)size / 6, (double)size / 6);
        lightTop.relocate((double)size / 6, (double)size / 6);
        lightSpot.setFill(Color.WHITE);
        lightLeft.setFill(Color.WHITE);
        lightTop.setFill(Color.WHITE);
    }

    public void setPosition(Vector2D position) {
        this.position = position;
        super.relocate(position.getX() * size, position.getY() * size);
    }

    public Vector2D getPosition() {
        return position;
    }

    public Rectangle getRectangle() {
        return block;
    }
}
