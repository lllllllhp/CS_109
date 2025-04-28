package com.proj510.model.game;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class BoxModel extends Rectangle {
    private Color color;
    private int row;
    private int col;//坐标为物块左上角
    private boolean isSelected;
    private boolean isMoving;
    //表示box大小种类
    private int typeId;
    //唯一编号
    private int key;
    static int boxNumber = 0;

    //init box
    public BoxModel(int typeId, Color color, int row, int col, int width, int height) {
        super(width, height);
        setX(col * GameModel.GRID_SIZE);
        setY(row * GameModel.GRID_SIZE);
        setFill(color);
        setStroke(Color.BLACK);
        setStrokeWidth(1.5);

        this.typeId = typeId;
        this.color = color;
        this.row = row;
        this.col = col;
        isSelected = false;
        isMoving = false;

        boxNumber++;
        key = boxNumber;
    }

    public void setSize(int width, int height) {
        this.setWidth(width);
        this.setHeight(height);
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
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

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
        if (selected) {
            toFront();//移至图层顶部
            setStroke(Color.RED);
        } else setStroke(Color.BLACK);
    }

    public int getTypeId() {
        return typeId;
    }

    public void setTypeId(int typeId) {
        this.typeId = typeId;
    }

    public int getKey() {
        return key;
    }

    public void setKey(int key) {
        this.key = key;
    }

    public boolean isMoving() {
        return isMoving;
    }

    public void setMoving(boolean moving) {
        isMoving = moving;
    }

    @Override
    public String toString() {
        return String.format("%s\ntypeId=%d row=%d col=%d isSelected=%b", super.toString(), getTypeId(), getRow(), getCol(), isSelected());
    }
}
