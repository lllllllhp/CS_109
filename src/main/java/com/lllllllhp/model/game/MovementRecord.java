package com.lllllllhp.model.game;

public class MovementRecord {
    private int stepNum;
    private int boxKey;
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

    static public MovementRecord deepCopy(MovementRecord movementRecord) {
        return new MovementRecord(
                movementRecord.getStepNum(),
                movementRecord.getBoxKey(),
                movementRecord.getRow(),
                movementRecord.getCol(),
                movementRecord.getDirection());
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

    public int getBoxKey() {
        return boxKey;
    }

    public void setBoxKey(int boxKey) {
        this.boxKey = boxKey;
    }

    public int getStepNum() {
        return stepNum;
    }

    public void setStepNum(int stepNum) {
        this.stepNum = stepNum;
    }

    @Override
    public String toString() {
        return String.format("Step:%d key:%d row: %d col: %d Direction: %s", getStepNum(), getBoxKey(), getRow(), getCol(), getDirection().name());
    }
}
