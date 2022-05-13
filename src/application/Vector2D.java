package application;

public class Vector2D {

    private int x;
    private int y;

    public <T extends Number> Vector2D(T x, T y) {
        this.x = Integer.parseInt((String.valueOf(x.intValue())));
        this.y = Integer.parseInt((String.valueOf(y.intValue())));
    }

    public int getX() { return x; }
    public int getY() { return y; }
    public void setX(int x) { this.x = x; }
    public void setY(int y) { this.y = y; }

    public Vector2D add(Vector2D v) {
        return new Vector2D(x + v.x, y + v.y);
    }

    public Vector2D minus(Vector2D v) {
        return new Vector2D(x - v.x, y - v.y);
    }

    public Vector2D multiply(int m) {
        return new Vector2D(x * m, y * m);
    }

    public Vector2D devide(int d) {
        return new Vector2D(x / d, y / d);
    }

    public boolean overBound(Vector2D v) {
        return x > v.x || y > v.y;
    }

    public boolean overBound(Vector2D v1, Vector2D v2) {
        return x < v1.x || x > v2.x || y < v1.y || y > v2.y;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Vector2D)
            return x == ((Vector2D)obj).getX() && y == ((Vector2D)obj).getY();
        return super.equals(obj);
    }
}
