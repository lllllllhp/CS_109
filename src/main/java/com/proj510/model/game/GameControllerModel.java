package com.proj510.model.game;

import com.proj510.data.MapRecord;
import com.proj510.data.UserData;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class GameControllerModel {
    private UserData userData;
    private GameModel gameModel;
    private MapModel mapModel;
    private Map<Integer, BoxModel> boxes = new HashMap<>();

    public GameControllerModel(GameModel gameModel) {
        this.gameModel = gameModel;
        this.mapModel = gameModel.getMapModel();
        this.boxes = gameModel.getBoxes();
    }

    public GameControllerModel() {}

    public boolean doMove(BoxModel box, int row, int col, Direction direction) {
        if (mapModel.getId(row, col) == 1) {//小兵
            int nextRow = row + direction.getRow();
            int nextCol = col + direction.getCol();
            if (mapModel.checkInHeightSize(nextRow) && mapModel.checkInWidthSize(nextCol)) {
                if (mapModel.getId(nextRow, nextCol) == 0) {
                    mapModel.getMatrix()[row][col] = 0;
                    mapModel.getMatrix()[nextRow][nextCol] = 1;
                    box.setRow(nextRow);
                    box.setCol(nextCol);
                    box.setX(box.getCol() * GameModel.GRID_SIZE);
                    box.setY(box.getRow() * GameModel.GRID_SIZE);
                    return true;
                }
            }
        } else if (mapModel.getId(row, col) == 2) {//横
            int nextRow = row + direction.getRow();
            int nextCol = col + direction.getCol();
            if (mapModel.checkInHeightSize(nextRow) && mapModel.checkInWidthSize(nextCol) && mapModel.checkInWidthSize(nextCol + 1)) {
                mapModel.getMatrix()[row][col] = 0;
                mapModel.getMatrix()[row][col + 1] = 0;//先将原本位置归零，便于判断
                if (mapModel.getId(nextRow, nextCol) == 0 && mapModel.getId(nextRow, nextCol + 1) == 0) {

                    mapModel.getMatrix()[nextRow][nextCol] = 2;
                    mapModel.getMatrix()[nextRow][nextCol + 1] = 2;
                    box.setRow(nextRow);
                    box.setCol(nextCol);
                    box.setX(box.getCol() * GameModel.GRID_SIZE);
                    box.setY(box.getRow() * GameModel.GRID_SIZE);
                    return true;
                } else {    //若不可移动，撤销归零
                    mapModel.getMatrix()[row][col] = 2;
                    mapModel.getMatrix()[row][col + 1] = 2;
                }
            }
        } else if (mapModel.getId(row, col) == 3) {//竖
            int nextRow = row + direction.getRow();
            int nextCol = col + direction.getCol();
            if (mapModel.checkInHeightSize(nextRow) && mapModel.checkInHeightSize(nextRow + 1) && mapModel.checkInWidthSize(nextCol)) {
                mapModel.getMatrix()[row][col] = 0;
                mapModel.getMatrix()[row + 1][col] = 0;
                if (mapModel.getId(nextRow, nextCol) == 0 && mapModel.getId(nextRow + 1, nextCol) == 0) {
                    mapModel.getMatrix()[nextRow][nextCol] = 3;
                    mapModel.getMatrix()[nextRow + 1][nextCol] = 3;
                    box.setRow(nextRow);
                    box.setCol(nextCol);
                    box.setX(box.getCol() * GameModel.GRID_SIZE);
                    box.setY(box.getRow() * GameModel.GRID_SIZE);
                    return true;
                } else {
                    mapModel.getMatrix()[row][col] = 3;
                    mapModel.getMatrix()[row + 1][col] = 3;
                }
            }
        } else if (mapModel.getId(row, col) == 4) {//曹操
            int nextRow = row + direction.getRow();
            int nextCol = col + direction.getCol();
            if (mapModel.checkInHeightSize(nextRow) && mapModel.checkInWidthSize(nextCol) && mapModel.checkInHeightSize(nextRow + 1) && mapModel.checkInWidthSize(nextCol + 1)) {
                mapModel.getMatrix()[row][col] = 0;
                mapModel.getMatrix()[row][col + 1] = 0;
                mapModel.getMatrix()[row + 1][col] = 0;
                mapModel.getMatrix()[row + 1][col + 1] = 0;
                if (mapModel.getId(nextRow, nextCol) == 0 && mapModel.getId(nextRow + 1, nextCol + 1) == 0) {
                    mapModel.getMatrix()[nextRow][nextCol] = 4;
                    mapModel.getMatrix()[nextRow][nextCol + 1] = 4;
                    mapModel.getMatrix()[nextRow + 1][nextCol] = 4;
                    mapModel.getMatrix()[nextRow + 1][nextCol + 1] = 4;
                    box.setRow(nextRow);
                    box.setCol(nextCol);
                    box.setX(box.getCol() * GameModel.GRID_SIZE);
                    box.setY(box.getRow() * GameModel.GRID_SIZE);
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

    public void undo() {
        MovementRecord movementRecord = null;
        if (gameModel.getMovementStack().isEmpty()) {
            System.out.println("You can't undo now.");
        } else {
            movementRecord = gameModel.getMovementStack().peek();
            Direction undoDirection;
            if (movementRecord != null) {
                //判断undo方向
                if (movementRecord.getDirection().equals(Direction.UP)) {
                    undoDirection = Direction.DOWN;
                } else if (movementRecord.getDirection().equals(Direction.DOWN)) {
                    undoDirection = Direction.UP;
                } else if (movementRecord.getDirection().equals(Direction.LEFT)) {
                    undoDirection = Direction.RIGHT;
                } else {
                    undoDirection = Direction.LEFT;
                }
                //移动
                if (doMove(boxes.get(movementRecord.getBoxKey()), movementRecord.getRow() - undoDirection.getRow(), movementRecord.getCol() - undoDirection.getCol(), undoDirection)) {
                    gameModel.getMovementStack().pop();
                    mapModel.minusSteps();
                    gameModel.getRootPaneController().updateSteps();
                    System.out.println("Undo succeed.");
                } else System.out.println("Undo error.");
            }
        }
        //todo
    }

    public void restart() {
        for (int i = 0; i < mapModel.getMatrix().length; i++) {
            mapModel.getMatrix()[i] = Arrays.copyOf(mapModel.getCopyMatrix()[i], mapModel.getCopyMatrix()[i].length);
        }
        mapModel.setSteps(0);
        gameModel.initGame();
        System.out.println("Game Restart");
    }

    public void saveGame() {
        userData.setMapRecord(new MapRecord(mapModel));
        userData.save();
    }

    //--------------------------------------------------------------------------------
    public GameModel getGameModel() {
        return gameModel;
    }

    public void setGameModel(GameModel gameModel) {
        this.gameModel = gameModel;
    }

    public MapModel getMapModel() {
        return mapModel;
    }

    public void setMapModel(MapModel mapModel) {
        this.mapModel = mapModel;
    }

    public UserData getUserData() {
        return userData;
    }

    public void setUserData(UserData userData) {
        this.userData = userData;
    }

    public Map<Integer, BoxModel> getBoxes() {
        return boxes;
    }

    public void setBoxes(Map<Integer, BoxModel> boxes) {
        this.boxes = boxes;
    }
}
