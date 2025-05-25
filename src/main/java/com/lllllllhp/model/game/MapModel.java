package com.lllllllhp.model.game;

import com.lllllllhp.data.MapPre;
import com.lllllllhp.data.MapPreset;
import com.lllllllhp.utils.aiSolver.AISolver;

import java.io.Serializable;
import java.util.*;

public class MapModel implements Serializable {
    private String name;
    protected int[][] matrix;
    int[][] copyMatrix;  //备份，用于restart
    private int targetRow;
    private int targetCol;
    private int steps = 0;
    private Time time;

    //直接导入map
    public MapModel(int[][] matrix, int targetCol, int targetRow) {
        this.matrix = matrix;
        this.copyMatrix = new int[matrix.length][];
        for (int i = 0; i < matrix.length; i++) {
            this.copyMatrix[i] = Arrays.copyOf(matrix[i], matrix[i].length);
        }
        this.targetCol = targetCol;
        this.targetRow = targetRow;
        name = "";
        time = new Time(0);
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

    //生成随机地图,5_4
    public static MapModel getRandomMap() {
        Random random = new Random();

        int col = random.nextInt(3);//0-2
        int row = random.nextInt(4);//0-3
        MapModel mapModel = new MapModel(new int[5][4], col, row);
        mapModel.setName("random_");

        //2_2
        int col2_2;
        int row2_2;
        do {
            col2_2 = random.nextInt(3);
            row2_2 = random.nextInt(4);
        } while (row2_2 == row && col2_2 == col);
        mapModel.matrix[row2_2][col2_2] = 4;
        mapModel.matrix[row2_2 + 1][col2_2] = 4;
        mapModel.matrix[row2_2][col2_2 + 1] = 4;
        mapModel.matrix[row2_2 + 1][col2_2 + 1] = 4;

        //2_1
        List<String> listCanPlace2_1 = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                if (mapModel.matrix[i][j] == 0 && mapModel.matrix[i + 1][j] == 0) {
                    listCanPlace2_1.add(String.format("%d,%d", i, j));
                }
            }
        }
        Collections.shuffle(listCanPlace2_1);//打乱取前几个
        for (int i = 0; i < 4; i++) {
            String[] location = listCanPlace2_1.get(i).split(",");
            int row2 = Integer.parseInt(location[0]);
            int col2 = Integer.parseInt(location[1]);
            mapModel.matrix[row2][col2] = 3;
            mapModel.matrix[row2 + 1][col2] = 3;
            if (row2 > 0)
                listCanPlace2_1.remove(String.format("%d,%d", row2 - 1, col2));
            if (row2 < 4) listCanPlace2_1.remove(String.format("%d,%d", row2 + 1, col2));
        }

        //1_2,共一个
        List<int[]> listCanPlace1_2 = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 3; j++) {
                if (mapModel.matrix[i][j] == 0 && mapModel.matrix[i][j + 1] == 0) {
                    listCanPlace1_2.add(new int[]{i, j});
                }
            }
        }
        Collections.shuffle(listCanPlace1_2);//打乱取前几个
        int[] location1_2 = listCanPlace1_2.get(0);
        mapModel.matrix[location1_2[0]][location1_2[1]] = 2;
        mapModel.matrix[location1_2[0]][location1_2[1] + 1] = 2;

        //1_1
        List<int[]> listCanPlace1_1 = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 4; j++) {
                if (mapModel.matrix[i][j] == 0) {
                    listCanPlace1_1.add(new int[]{i, j});
                }
            }
        }
        Collections.shuffle(listCanPlace1_1);
        for (int i = 0; i < 4; i++) {
            int[] location1_1 = listCanPlace1_1.get(i);
            mapModel.matrix[location1_1[0]][location1_1[1]] = 1;
        }

        mapModel.copyMatrix = mapModel.getMatrixData();

        //判断有无解
        AISolver aiSolver = new AISolver(mapModel);
        Deque<MovementRecord> records = aiSolver.aStarSolver();
        if (records != null && !records.isEmpty())
            return mapModel;
        else {
            System.out.println("generate again");
            return getRandomMap();
        }
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

    public void showMap() {
        for (int i = 0; i < matrix.length; i++) {
            System.out.println(Arrays.toString(matrix[i]));
        }
    }
}
