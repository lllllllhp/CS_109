package com.proj510.data;

import java.util.Arrays;

public enum MapPreset {
    MAP1(new int[][]{
            {3, 4, 4, 3},
            {3, 4, 4, 3},
            {3, 2, 2, 3},
            {3, 1, 1, 3},
            {1, 0, 0, 1},//横刀立马

    }, 1, 3),//default map
    MAP2(new int[][]{
            {1, 0, 0, 0},
            {1, 4, 4, 0},
            {1, 4, 4, 0},
            {0, 0, 0, 0},
            {0, 0, 0, 0},
    }, 1, 3),

    MAP3(new int[][]{
            {0, 0, 0, 0},
            {0, 4, 4, 0},
            {0, 4, 4, 0},
            {0, 3, 1, 0},
            {0, 3, 0, 0},
    }, 1, 3),
    MAP4(new int[][]{
            {1, 4, 4, 1},
            {3, 4, 4, 3},
            {3, 2, 2, 3},
            {3, 1, 1, 3},
            {3, 0, 0, 3},
    }, 1, 3);//兵分三路


    MapPreset(int[][] matrix, int targetCol, int targetRow) {
        this.matrix = matrix;
        this.targetCol = targetCol;
        this.targetRow = targetRow;
    }

    private final int[][] matrix;
    private final int targetRow;
    private final int targetCol;

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
}
