package com.proj510.data;

import java.util.Arrays;

public class MapPre {
    public MapPre(int[][] matrix, int targetCol, int targetRow) {
        this.matrix = matrix;
        this.targetCol = targetCol;
        this.targetRow = targetRow;
    }

    private int[][] matrix;
    private int targetRow;
    private int targetCol;

    public int[][] getMatrixData() {
        int[][] copy = new int[getMatrix().length][];
        for (int i = 0; i < getMatrix().length; i++) {
            copy[i] = Arrays.copyOf(getMatrix()[i], getMatrix()[i].length);
        }
        return copy;
    }

    public int[][] getMatrix() {
        return matrix;
    }

    public int getTargetRow() {
        return targetRow;
    }

    public int getTargetCol() {
        return targetCol;
    }

    public void setMatrix(int[][] matrix) {
        this.matrix = matrix;
    }

    public void setTargetRow(int targetRow) {
        this.targetRow = targetRow;
    }

    public void setTargetCol(int targetCol) {
        this.targetCol = targetCol;
    }
}
