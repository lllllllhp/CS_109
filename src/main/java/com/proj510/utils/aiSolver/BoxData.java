package com.proj510.utils.aiSolver;

import com.proj510.model.game.BoxModel;

public class BoxData {
    private int row;
    private int col;//坐标为物块左上角
    //表示box大小种类
    private int typeId;
    //唯一编号
    private int key;

    public BoxData(BoxModel boxModel) {
        this.row = boxModel.getRow();
        this.col = boxModel.getCol();
        this.typeId = boxModel.getTypeId();
        this.key = boxModel.getKey();
    }
    //copy
    public BoxData(BoxData boxData) {
        this.row = boxData.getRow();
        this.col = boxData.getCol();
        this.typeId = boxData.getTypeId();
        this.key = boxData.getKey();
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getCol() {
        return col;
    }

    public void setCol(int col) {
        this.col = col;
    }

    public int getKey() {
        return key;
    }

    public void setKey(int key) {
        this.key = key;
    }

    public int getTypeId() {
        return typeId;
    }

    public void setTypeId(int typeId) {
        this.typeId = typeId;
    }

    @Override
    public String toString() {
        return "BoxData{" +
                "row=" + row +
                ", col=" + col +
                ", typeId=" + typeId +
                ", key=" + key +
                '}';
    }
}
