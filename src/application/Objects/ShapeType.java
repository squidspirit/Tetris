package application.Objects;

import java.util.Random;

import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

public enum ShapeType {

    // I, J, L, O, S, T, Z;
    I(new Boolean[][]{
        {false, false, false, false},
        {false, false, false, false},
        {true , true , true , true },
        {false, false, false, false}
    }, Color.AQUA),
    J(new Boolean[][]{
        {false, false, false, false},
        {true , false, false, false},
        {true , true , true , false},
        {false, false, false, false}
    }, Color.BLUE),
    L(new Boolean[][]{
        {false, false, false, false},
        {false, false, true , false},
        {true , true , true , false},
        {false, false, false, false}
    }, Color.ORANGE),
    O(new Boolean[][]{
        {false, false, false, false},
        {false, true , true , false},
        {false, true , true , false},
        {false, false, false, false}
    }, Color.YELLOW),
    S(new Boolean[][]{
        {false, false, false, false},
        {false, true , true , false},
        {true , true , false, false},
        {false, false, false, false}
    }, Color.GREEN),
    T(new Boolean[][]{
        {false, false, false, false},
        {false, true , false, false},
        {true , true , true , false},
        {false, false, false, false}
    }, Color.PURPLE),
    Z(new Boolean[][]{
        {false, false, false, false},
        {true , true , false, false},
        {false, true , true , false},
        {false, false, false, false}
    }, Color.RED);


    private Boolean[][] blockChart;
    private Paint fill;

    ShapeType(Boolean[][] blockChart, Paint fill) {
        this.blockChart = blockChart;
        this.fill = fill;
    }

    public Boolean[][] getBlockChart() { return blockChart; }
    public Paint getFill() { return fill; }

    public static ShapeType getRandom() {
        ShapeType[] values = ShapeType.values();
        int length = values.length;
        int randIndex = new Random().nextInt(length);
        return values[randIndex];
    }
}
