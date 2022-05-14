package application.objects;

import java.util.Random;

import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

public enum ShapeType {

    // I, J, L, O, S, T, Z;
    I(new Boolean[][]{
        {false, false, false, false},
        {true , true , true , true },
        {false, false, false, false},
        {false, false, false, false}
    }, Color.DARKTURQUOISE),
    J(new Boolean[][]{
        {true , false, false},
        {true , true , true },
        {false, false, false}
    }, Color.MEDIUMBLUE),
    L(new Boolean[][]{
        {false, false, true },
        {true , true , true },
        {false, false, false}
    }, Color.CHOCOLATE),
    O(new Boolean[][]{
        {false, false, false, false},
        {false, true , true , false},
        {false, true , true , false},
        {false, false, false, false}
    }, Color.GOLD),
    S(new Boolean[][]{
        {false, true , true },
        {true , true , false},
        {false, false, false}
    }, Color.GREEN),
    T(new Boolean[][]{
        {false, true , false},
        {true , true , true },
        {false, false, false}
    }, Color.DARKMAGENTA),
    Z(new Boolean[][]{
        {true , true , false},
        {false, true , true },
        {false, false, false}
    }, Color.DARKRED);


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
