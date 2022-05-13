// 未知錯誤 未啟用 待研究

package application;

import java.lang.reflect.Array;

public class Matrix<T> {
    
    private int size;
    private T[][] matrix;

    private void createMatrix(Class<T[][]> clazz, int size) {
        @SuppressWarnings("unchecked")
        final T[][] matrix = (T[][])Array.newInstance(clazz.getComponentType(), size);
        this.matrix = matrix;
    }

    public Matrix(Class<T[][]> clazz, int size) {
        createMatrix(clazz, size);
    }

    public int getSize() { return size; }
    public T[][] getMatrix() { return matrix; }
    
    public void setMatrix(T[][] matrix) {
        if (matrix.length != size) return;
        this.matrix = matrix;
    }
}
