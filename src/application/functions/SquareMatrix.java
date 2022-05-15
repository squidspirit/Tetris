package application.functions;

import java.lang.reflect.Array;

public class SquareMatrix<T> {

    final private Class<? extends T> clazz;
    final private int size;
    private T[][] matrix;

    protected T[][] createArray() {
        @SuppressWarnings("unchecked")
        T[][] matrix = (T[][])Array.newInstance(clazz, size, size);
        return matrix;
    }

    public SquareMatrix(Class<? extends T> clazz, int size) {
        this.size = size;
        this.clazz = clazz;
        this.matrix = createArray();
    }

    public SquareMatrix(Class<? extends T> clazz, int size, T[][] matrix) {
        this.clazz = clazz;
        this.size = size;
        this.matrix = matrix;
    }

    public int getSize() { return size; }
    public T[][] getMatrix() { return matrix; }
    
    public void setMatrix(T[][] matrix) { this.matrix = matrix; }
    public void setElement(int x, int y, T elm) { matrix[x][y] = elm; }

    public SquareMatrix<T> rotate() {
        T[][] newMatrix = createArray();
        for (int i = 0; i < size; i ++)
            for (int j = 0; j < size; j ++)
            newMatrix[i][j] = matrix[size - j - 1][i];
        return new SquareMatrix<>(clazz, size, newMatrix);
    }
}
