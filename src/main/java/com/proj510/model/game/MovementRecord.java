package com.proj510.model.game;

public class MovementRecord {
    private int boxKey;
    private int stepNum;
    private int col;
    private int row;
    private Direction direction;

    public MovementRecord(int stepNum, int boxKey, int row, int col, Direction direction) {
        this.stepNum = stepNum;
        this.boxKey = boxKey;
        this.row = row;
        this.col = col;
        this.direction = direction;
    }

    public int getCol() {
        return col;
    }

    public void setCol(int col) {
        this.col = col;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public Direction getDirection() {
        return direction;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    public int getStepNum() {
        return stepNum;
    }

    public void setStepNum(int stepNum) {
        this.stepNum = stepNum;
    }

    public int getBoxKey() {
        return boxKey;
    }

    public void setBoxKey(int boxKey) {
        this.boxKey = boxKey;
    }

    @Override
    public String toString() {
        return String.format("Step%d: row: %d col: %d Direction: %s", getStepNum(), getRow(), getCol(), getDirection().name());
    }
}
