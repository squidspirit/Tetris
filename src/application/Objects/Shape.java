package application.Objects;

import java.util.ArrayList;

import application.Vector2D;
import javafx.scene.layout.Pane;

public class Shape extends Pane {

    private ShapeType shapeType;
    private Pane superParent;
    private int blockSize;
    
    private Vector2D position;
    private Vector2D superParentSize;
    private ArrayList<Vector2D> blocksPosList = new ArrayList<>();
    private Block[] blocks = new Block[4];

    public <T extends Pane> Shape(ShapeType shapeType, T superParent, int blockSize) {

        this.shapeType = shapeType;
        this.superParent = superParent;
        this.blockSize = blockSize;

        superParent.getChildren().add(this);
        this.position = new Vector2D(3, -3);
        this.setPosition(this.position);
        super.setWidth(blockSize * 4);
        super.setHeight(blockSize * 4);

        for (Object obj : superParent.getChildren())
            if (obj instanceof Block)
                blocksPosList.add(((Block)obj).getPosition());

        int blockCount = 0;
        for (int i = 0; i < 4; i ++) { // row
            for (int j = 0; j < 4; j ++) { // col
                if (shapeType.getBlockChart()[i][j]) {
                    blocks[blockCount] = new Block(blockSize);
                    blocks[blockCount].setFill(shapeType.getFill());
                    super.getChildren().add(blocks[blockCount]);
                    blocks[blockCount].setPosition(new Vector2D(j, i));
                    blockCount ++;
                }
            }
        }
    }

    public Vector2D getPosition() { return position; }

    public void setPosition(Vector2D position) {
        this.position = position;
        super.relocate(position.getX() * blockSize, position.getY() * blockSize);
    }

    public void decompose() {
        for (Block localBlock : blocks) {
            Block globalBlock = new Block(blockSize);
            superParent.getChildren().add(globalBlock);
            globalBlock.setPosition(position.add(localBlock.getPosition()));
        }
    }

    public boolean drop() {
        superParentSize = new Vector2D(superParent.getWidth(), superParent.getHeight()).devide(blockSize);
        Vector2D newPosition = position.add(new Vector2D(0, 1));
        for (Block localBlock : blocks)
            if (newPosition.add(localBlock.getPosition()).overBound(superParentSize))
                return false;
        for (Vector2D globalBlockPos : blocksPosList) {
            for (Block localBlock : blocks)
                if (newPosition.add(localBlock.getPosition()).equals(globalBlockPos))
                    return false;
        }
        setPosition(newPosition);
        return true;
    }

    public boolean rotate() {

        return true;
    }
}