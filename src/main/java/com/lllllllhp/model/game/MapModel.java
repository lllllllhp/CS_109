package com.lllllllhp.model.game;

import com.lllllllhp.data.MapPre;
import com.lllllllhp.data.MapPreset;

import java.io.Serializable;
import java.util.Arrays;

public class MapModel implements Serializable {
    private String name;
    protected int[][] matrix;
    int[][] copyMatrix;  //备份，用于restart
    private int targetRow;
    private int targetCol;
    private int steps = 0;

    //直接导入map
    public MapModel(int[][] matrix, int targetCol, int targetRow) {
        this.matrix = matrix;
        this.copyMatrix = new int[matrix.length][];
        for (int i = 0; i < matrix.length; i++) {
            this.copyMatrix[i] = Arrays.copyOf(matrix[i], matrix[i].length);
        }
        this.targetCol = targetCol;
        this.targetRow = targetRow;
    }

    //加载map文件
    public MapModel(MapPre mapPre) {
        this.matrix = mapPre.getMatrixData();
        this.copyMatrix = new int[matrix.length][];
        for (int i = 0; i < this.matrix.length; i++) {
            this.copyMatrix[i] = Arrays.copyOf(this.matrix[i], this.matrix[i].length);
        }
        this.targetCol = mapPre.getTargetCol();
        this.targetRow = mapPre.getTargetRow();
        this.name = mapPre.getName();
    }
    //加载预设map
    public MapModel(MapPreset mapPreset) {
        this.matrix = mapPreset.getMatrixData();
        this.copyMatrix = new int[matrix.length][];
        for (int i = 0; i < this.matrix.length; i++) {
            this.copyMatrix[i] = Arrays.copyOf(this.matrix[i], this.matrix[i].length);
        }
        this.targetCol = mapPreset.getTargetCol();
        this.targetRow = mapPreset.getTargetRow();
    }

    //copy
    static public MapModel copy(MapModel mapModel) {
        MapModel newModel = new MapModel();
        newModel.steps = mapModel.getSteps();
        newModel.targetCol = mapModel.targetCol;
        newModel.targetRow = mapModel.targetRow;
        newModel.matrix = mapModel.getMatrixData();
        newModel.copyMatrix = mapModel.getCopyData();
        return newModel;
    }

    public int[][] getMatrixData() {
        int[][] copy = new int[matrix.length][];
        for (int i = 0; i < matrix.length; i++) {
            copy[i] = Arrays.copyOf(matrix[i], matrix[i].length);
        }
        return copy;
    }

    public int[][] getCopyData() {
        int[][] copy = new int[copyMatrix.length][];
        for (int i = 0; i < copyMatrix.length; i++) {
            copy[i] = Arrays.copyOf(copyMatrix[i], copyMatrix[i].length);
        }
        return copy;
    }
    //default map
    public MapModel() {
        this(MapPreset.MAP1);
    }

    public int getWidth() {
        return this.matrix[0].length;
    }

    public int getHeight() {
        return this.matrix.length;
    }

    public int[][] getCopyMatrix() {
        return copyMatrix;
    }

    public int getId(int row, int col) {
        return matrix[row][col];
    }

    public int getCopyId(int row, int col) {
        return copyMatrix[row][col];
    }

    public int[][] getMatrix() {
        return matrix;
    }

    public boolean checkInWidthSize(int col) {
        return col >= 0 && col < matrix[0].length;
    }

    public boolean checkInHeightSize(int row) {
        return row >= 0 && row < matrix.length;
    }

    public int getTargetRow() {
        return targetRow;
    }

    public void setTargetRow(int targetRow) {
        this.targetRow = targetRow;
    }

    public int getTargetCol() {
        return targetCol;
    }

    public void setTargetCol(int targetCol) {
        this.targetCol = targetCol;
    }

    public int getSteps() {
        return steps;
    }

    public void setSteps(int steps) {
        this.steps = steps;
    }

    public void addSteps() {
        steps++;
    }

    public void minusSteps() {
        steps--;
    }

    public void setMatrix(int[][] matrix) {
        this.matrix = matrix;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "MapModel{" +
                "matrix=" + Arrays.deepToString(matrix) +
                ", copyMatrix=" + Arrays.deepToString(copyMatrix) +
                ", targetRow=" + targetRow +
                ", targetCol=" + targetCol +
                '}';
    }
}
