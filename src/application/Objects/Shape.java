package application.Objects;

import java.util.ArrayList;

import application.SquareMatrix;
import application.Vector2D;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

public class Shape extends Pane{

    private ShapeType shapeType;
    private Pane superParent;
    private int blockSize;
    private int size;
    private SquareMatrix<Boolean> blockChart;
    private Vector2D position;
    private Vector2D superParentSize;
    private ArrayList<Vector2D> blocksPosList = new ArrayList<>();



    public <T extends Pane> Shape(ShapeType shapeType, T superParent, int blockSize) {

        this.shapeType = shapeType;
        this.superParent = superParent;
        this.blockSize = blockSize;
        this.size = shapeType.getBlockChart().length;
        this.blockChart = new SquareMatrix<>(
            Boolean.class, size, shapeType.getBlockChart()
        );

        superParent.getChildren().add(this);
        superParentSize = new Vector2D(
            superParent.getWidth(),
            superParent.getHeight()
        ).devide(blockSize);
        this.position = new Vector2D(3, -size + 1);
        this.setPosition(this.position);
        super.setWidth(blockSize * size);
        super.setHeight(blockSize * size);

        for (Object block : superParent.getChildren())
            if (block instanceof Block)
                blocksPosList.add(((Block)block).getPosition());

        for (int i = 0; i < 4; i ++) {
            Block block = new Block(blockSize);
            super.getChildren().add(block);
            block.setFill(shapeType.getFill());
            block.setStroke(Color.GRAY);
            block.setStrokeWidth(3);
        }
        this.setBlockChart(blockChart);
    }



    private boolean checkFeasible() {
        for (Object localBlock : this.getChildren())
            if (position.add(((Block)localBlock).getPosition()).overBound(
                    new Vector2D(0, -3),
                    superParentSize.minus(new Vector2D(1, 1))))
                return false;
        for (Vector2D globalBlockPos : blocksPosList) {
            for (Object localBlock : this.getChildren())
                if (position.add(((Block)localBlock).getPosition()).equals(globalBlockPos))
                    return false;
        }
        return true;
    }

    public ShapeType getShapeType() { return shapeType; }

    public Vector2D getPosition() { return position; }

    public void setPosition(Vector2D position) {
        this.position = position;
        super.relocate(position.getX() * blockSize, position.getY() * blockSize);
    }

    public SquareMatrix<Boolean> getBlockChart() { return blockChart; }

    public void setBlockChart(SquareMatrix<Boolean> blockChart) {
        this.blockChart = blockChart;
        Vector2D[] posList = new Vector2D[4];
        int posIndex = 0;
        for (int i = 0; i < size; i ++)
            for (int j = 0; j < size; j ++)
                if (blockChart.getMatrix()[i][j])
                    posList[posIndex ++] = new Vector2D(j, i);
        for (Object block : this.getChildren()) {
            ((Block)block).setPosition(posList[-- posIndex]);
        }
    }

    public void decompose() {
        for (Object localBlock : this.getChildren()) {
            Block globalBlock = new Block(blockSize);
            superParent.getChildren().add(globalBlock);
            globalBlock.setFill(shapeType.getFill());
            globalBlock.setStroke(Color.GRAY);
            globalBlock.setStrokeWidth(3);
            globalBlock.setPosition(position.add(((Block)localBlock).getPosition()));
        }
        superParent.getChildren().remove(this);
        System.gc();
    }

    public boolean drop() {
        Vector2D tmp = position;
        setPosition(position.add(new Vector2D(0, 1)));
        if (checkFeasible()) return true;
        setPosition(tmp);
        return false;
    }

    public boolean rotate() {
        SquareMatrix<Boolean> tmp = blockChart;
        setBlockChart(blockChart.rotate());
        if (checkFeasible()) return true;
        setBlockChart(tmp);
        return false;
    }

    public boolean left() {
        Vector2D tmp = position;
        setPosition(position.add(new Vector2D(-1, 0)));
        if (checkFeasible()) return true;
        setPosition(tmp);
        return false;
    }

    public boolean right() {
        Vector2D tmp = position;
        setPosition(position.add(new Vector2D(1, 0)));
        if (checkFeasible()) return true;
        setPosition(tmp);
        return false;
    }
}