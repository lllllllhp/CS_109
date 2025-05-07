package com.lllllllhp.utils.aiSolver;

import com.lllllllhp.model.game.*;

import java.util.*;

public class GameState extends MapModel {
    private Map<Integer, BoxData> boxDataMap = new HashMap<>();
    //记录达到目前状态的移动
    Deque<MovementRecord> movementRecords = new ArrayDeque<>();

    //仅用于初始状态
    public GameState(AISolver aiSolver) {
        matrix = aiSolver.getMapModel().getMatrixData();
        this.boxDataMap = aiSolver.getOriginBoxDataMap();
    }

    public GameState deepCopy(GameState gameState) {
        GameState newState = new GameState();
        newState.setMatrix(gameState.getMatrixData());

        Map<Integer, BoxData> newDataMap = new HashMap<>();
        for (Map.Entry<Integer, BoxData> entry : gameState.getBoxDataMap().entrySet()) {
            newDataMap.put(entry.getKey(), new BoxData(entry.getValue()));
        }
        newState.setBoxDataMap(newDataMap);

        Deque<MovementRecord> newMovementRecords = new ArrayDeque<>();
        for (MovementRecord movementRecord : gameState.getMovementRecords()) {
            newMovementRecords.offer(MovementRecord.deepCopy(movementRecord));
        }
        newState.setMovementRecords(newMovementRecords);

        return newState;
    }

    public GameState() {
    }

    public List<GameState> generateNextState() {
        //上下左右可能
        Direction[] directions = new Direction[]{Direction.UP, Direction.DOWN, Direction.LEFT, Direction.RIGHT};
        List<GameState> list = new ArrayList<>();

        for (Map.Entry<Integer, BoxData> entry : boxDataMap.entrySet()) {
            int key = entry.getKey();
            BoxData boxData = entry.getValue();
            for (Direction direction : directions) {

                if (canMove(this, boxData, direction)) {
                    // 创建副本并获取新状态里的方块对象
                    GameState nextState = deepCopy(this);
                    BoxData boxCopy = nextState.getBoxDataMap().get(key);

                    // 在副本上执行移动
                    applyMove(nextState, boxCopy, direction);

                    // 构造新步骤记录
                    int step = nextState.getMovementRecords().isEmpty() ? 1 : nextState.getMovementRecords().peekLast().getStepNum() + 1;
                    MovementRecord record = new MovementRecord(step, key, boxCopy.getRow() - direction.getRow(), boxCopy.getCol() - direction.getCol(), direction);
                    nextState.getMovementRecords().offer(record);

                    list.add(nextState);
                    AISolver.tryNum++;
                }
            }
        }
        return list;
    }

    //谁写的shi
    public boolean canMove(MapModel mapModel, BoxData box, Direction direction) {
        int row = box.getRow();
        int col = box.getCol();
        int nextRow = row + direction.getRow();
        int nextCol = col + direction.getCol();
        if (box.getTypeId() == 1) {
            if (mapModel.checkInHeightSize(nextRow) && mapModel.checkInWidthSize(nextCol)) {
                return mapModel.getId(nextRow, nextCol) == 0;
            }
        } else if (box.getTypeId() == 2) {
            if (mapModel.checkInHeightSize(nextRow) && mapModel.checkInWidthSize(nextCol) && mapModel.checkInWidthSize(nextCol + 1)) {
                mapModel.getMatrix()[row][col] = 0;
                mapModel.getMatrix()[row][col + 1] = 0;//先将原本位置归零，便于判断
                if (mapModel.getId(nextRow, nextCol) == 0 && mapModel.getId(nextRow, nextCol + 1) == 0) {
                    mapModel.getMatrix()[row][col] = 2;
                    mapModel.getMatrix()[row][col + 1] = 2;
                    return true;
                } else {    //若不可移动，撤销归零
                    mapModel.getMatrix()[row][col] = 2;
                    mapModel.getMatrix()[row][col + 1] = 2;
                }
            }
        } else if (box.getTypeId() == 3) {
            if (mapModel.checkInHeightSize(nextRow) && mapModel.checkInHeightSize(nextRow + 1) && mapModel.checkInWidthSize(nextCol)) {
                mapModel.getMatrix()[row][col] = 0;
                mapModel.getMatrix()[row + 1][col] = 0;
                if (mapModel.getId(nextRow, nextCol) == 0 && mapModel.getId(nextRow + 1, nextCol) == 0) {
                    mapModel.getMatrix()[row][col] = 3;
                    mapModel.getMatrix()[row + 1][col] = 3;
                    return true;
                } else {
                    mapModel.getMatrix()[row][col] = 3;
                    mapModel.getMatrix()[row + 1][col] = 3;
                }
            }
        } else if (box.getTypeId() == 4) {
            if (mapModel.checkInHeightSize(nextRow) && mapModel.checkInWidthSize(nextCol) && mapModel.checkInHeightSize(nextRow + 1) && mapModel.checkInWidthSize(nextCol + 1)) {
                mapModel.getMatrix()[row][col] = 0;
                mapModel.getMatrix()[row][col + 1] = 0;
                mapModel.getMatrix()[row + 1][col] = 0;
                mapModel.getMatrix()[row + 1][col + 1] = 0;
                if (mapModel.getId(nextRow, nextCol) == 0
                        && mapModel.getId(nextRow + 1, nextCol + 1) == 0
                        && mapModel.getId(nextRow, nextCol + 1) == 0
                        && mapModel.getId(nextRow + 1, nextCol) == 0) {
                    mapModel.getMatrix()[row][col] = 4;
                    mapModel.getMatrix()[row][col + 1] = 4;
                    mapModel.getMatrix()[row + 1][col] = 4;
                    mapModel.getMatrix()[row + 1][col + 1] = 4;
                    return true;
                } else {
                    mapModel.getMatrix()[row][col] = 4;
                    mapModel.getMatrix()[row][col + 1] = 4;
                    mapModel.getMatrix()[row + 1][col] = 4;
                    mapModel.getMatrix()[row + 1][col + 1] = 4;
                }
            }
        }
        return false;
    }

    //又一tuo
    public void applyMove(MapModel mapModel, BoxData box, Direction direction) {
        int row = box.getRow();
        int col = box.getCol();
        int nextRow = row + direction.getRow();
        int nextCol = col + direction.getCol();
        if (box.getTypeId() == 1) {
            mapModel.getMatrix()[row][col] = 0;
            mapModel.getMatrix()[nextRow][nextCol] = 1;
            box.setRow(nextRow);
            box.setCol(nextCol);
        } else if (box.getTypeId() == 2) {
            mapModel.getMatrix()[row][col] = 0;
            mapModel.getMatrix()[row][col + 1] = 0;
            mapModel.getMatrix()[nextRow][nextCol] = 2;
            mapModel.getMatrix()[nextRow][nextCol + 1] = 2;
            box.setRow(nextRow);
            box.setCol(nextCol);
        } else if (box.getTypeId() == 3) {
            mapModel.getMatrix()[row][col] = 0;
            mapModel.getMatrix()[row + 1][col] = 0;
            mapModel.getMatrix()[nextRow][nextCol] = 3;
            mapModel.getMatrix()[nextRow + 1][nextCol] = 3;
            box.setRow(nextRow);
            box.setCol(nextCol);
        } else if (box.getTypeId() == 4) {
            mapModel.getMatrix()[row][col] = 0;
            mapModel.getMatrix()[row][col + 1] = 0;
            mapModel.getMatrix()[row + 1][col] = 0;
            mapModel.getMatrix()[row + 1][col + 1] = 0;
            mapModel.getMatrix()[nextRow][nextCol] = 4;
            mapModel.getMatrix()[nextRow][nextCol + 1] = 4;
            mapModel.getMatrix()[nextRow + 1][nextCol] = 4;
            mapModel.getMatrix()[nextRow + 1][nextCol + 1] = 4;
            box.setRow(nextRow);
            box.setCol(nextCol);
        }
    }

    @Override
    public String toString() {
        return String.format("mat:%s\nmap:%s", Arrays.deepToString(matrix), boxDataMap.toString());
    }

    //用于去重
    public String encode() {
        StringBuilder sb = new StringBuilder();
        for (int[] row : getMatrixData()) {
            for (int col : row) {
                sb.append(col);
            }
        }
        return sb.toString();
    }

    public Deque<MovementRecord> getMovementRecords() {
        return movementRecords;
    }

    public void setMovementRecords(Deque<MovementRecord> movementRecords) {
        this.movementRecords = movementRecords;
    }

    public Map<Integer, BoxData> getBoxDataMap() {
        return boxDataMap;
    }

    public void setBoxDataMap(Map<Integer, BoxData> boxDataMap) {
        this.boxDataMap = boxDataMap;
    }
}
