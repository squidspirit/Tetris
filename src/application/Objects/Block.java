package application.Objects;

import application.Vector2D;
import javafx.scene.shape.Rectangle;

public class Block extends Rectangle {
    
    private Vector2D position;
    private int size;

    public Block(int size) {
        this.size = size;
        super.setWidth(size);
        super.setHeight(size);
    }

    public void setPosition(Vector2D position) {
        this.position = position;
        super.relocate(position.getX() * size, position.getY() * size);
    }

    public Vector2D getPosition() {
        return position;
    }

}
