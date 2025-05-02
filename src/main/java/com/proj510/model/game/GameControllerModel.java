package com.proj510.model.game;

import com.proj510.data.MapRecord;
import com.proj510.data.UserData;
import com.proj510.utils.aiSolver.AISolver;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
import javafx.concurrent.Task;
import javafx.util.Duration;

import java.util.*;

import static com.proj510.model.game.GameModel.GRID_SIZE;

public class GameControllerModel {
    private UserData userData;
    private GameModel gameModel;
    private MapModel mapModel;
    private Map<Integer, BoxModel> boxes = new HashMap<>();

    public GameControllerModel() {
    }

    static public boolean doMove(MapModel mapModel, BoxModel box, int row, int col, Direction direction) {
        int nextRow = row + direction.getRow();
        int nextCol = col + direction.getCol();
        if (!box.isMoving()) {
            if (mapModel.getId(row, col) == 1) {//小兵
                if (mapModel.checkInHeightSize(nextRow) && mapModel.checkInWidthSize(nextCol)) {
                    if (mapModel.getId(nextRow, nextCol) == 0) {
                        mapModel.getMatrix()[row][col] = 0;
                        mapModel.getMatrix()[nextRow][nextCol] = 1;
                        moveWithAnimation(box, nextCol, nextRow, direction);
                        return true;
                    }
                }
            } else if (mapModel.getId(row, col) == 2) {//横
                if (mapModel.checkInHeightSize(nextRow) && mapModel.checkInWidthSize(nextCol) && mapModel.checkInWidthSize(nextCol + 1)) {
                    mapModel.getMatrix()[row][col] = 0;
                    mapModel.getMatrix()[row][col + 1] = 0;//先将原本位置归零，便于判断
                    if (mapModel.getId(nextRow, nextCol) == 0 && mapModel.getId(nextRow, nextCol + 1) == 0) {
                        mapModel.getMatrix()[nextRow][nextCol] = 2;
                        mapModel.getMatrix()[nextRow][nextCol + 1] = 2;
                        moveWithAnimation(box, nextCol, nextRow, direction);
                        return true;
                    } else {    //若不可移动，撤销归零
                        mapModel.getMatrix()[row][col] = 2;
                        mapModel.getMatrix()[row][col + 1] = 2;
                    }
                }
            } else if (mapModel.getId(row, col) == 3) {//竖
                if (mapModel.checkInHeightSize(nextRow) && mapModel.checkInHeightSize(nextRow + 1) && mapModel.checkInWidthSize(nextCol)) {
                    mapModel.getMatrix()[row][col] = 0;
                    mapModel.getMatrix()[row + 1][col] = 0;
                    if (mapModel.getId(nextRow, nextCol) == 0 && mapModel.getId(nextRow + 1, nextCol) == 0) {
                        mapModel.getMatrix()[nextRow][nextCol] = 3;
                        mapModel.getMatrix()[nextRow + 1][nextCol] = 3;
                        moveWithAnimation(box, nextCol, nextRow, direction);
                        return true;
                    } else {
                        mapModel.getMatrix()[row][col] = 3;
                        mapModel.getMatrix()[row + 1][col] = 3;
                    }
                }
            } else if (mapModel.getId(row, col) == 4) {//曹操
                if (mapModel.checkInHeightSize(nextRow) && mapModel.checkInWidthSize(nextCol) && mapModel.checkInHeightSize(nextRow + 1) && mapModel.checkInWidthSize(nextCol + 1)) {
                    mapModel.getMatrix()[row][col] = 0;
                    mapModel.getMatrix()[row][col + 1] = 0;
                    mapModel.getMatrix()[row + 1][col] = 0;
                    mapModel.getMatrix()[row + 1][col + 1] = 0;
                    if (mapModel.getId(nextRow, nextCol) == 0
                            && mapModel.getId(nextRow + 1, nextCol + 1) == 0
                            && mapModel.getId(nextRow, nextCol + 1) == 0
                            && mapModel.getId(nextRow + 1, nextCol) == 0) {
                        mapModel.getMatrix()[nextRow][nextCol] = 4;
                        mapModel.getMatrix()[nextRow][nextCol + 1] = 4;
                        mapModel.getMatrix()[nextRow + 1][nextCol] = 4;
                        mapModel.getMatrix()[nextRow + 1][nextCol + 1] = 4;
                        moveWithAnimation(box, nextCol, nextRow, direction);
                        return true;
                    } else {
                        mapModel.getMatrix()[row][col] = 4;
                        mapModel.getMatrix()[row][col + 1] = 4;
                        mapModel.getMatrix()[row + 1][col] = 4;
                        mapModel.getMatrix()[row + 1][col + 1] = 4;
                    }
                }
            }
        }
        return false;
    }

    static public void moveWithAnimation(BoxModel boxModel, int nextCol, int nextRow, Direction direction) {
        boxModel.setCol(nextCol);
        boxModel.setRow(nextRow);
        //创建动画
        TranslateTransition transition = new TranslateTransition(Duration.seconds(0.1), boxModel);
        transition.setByX(direction.getCol() * GRID_SIZE);
        transition.setByY(direction.getRow() * GRID_SIZE);
        boxModel.setMoving(true);
        transition.play();

        transition.setOnFinished(e ->
                boxModel.setMoving(false)
        );
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
                if (doMove(mapModel, boxes.get(movementRecord.getBoxKey()), movementRecord.getRow() - undoDirection.getRow(), movementRecord.getCol() - undoDirection.getCol(), undoDirection)) {
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
            mapModel.getMatrix()[i] = Arrays.copyOf(mapModel.getCopyData()[i], mapModel.getCopyData()[i].length);
        }
        mapModel.setSteps(0);
        gameModel.initView();
        System.out.println("Game Restart");
    }

    public void saveGame() {
        userData.setMapRecord(new MapRecord(mapModel));
        userData.save();
    }

    public void aiSolve() throws InterruptedException {
        Task<Deque<MovementRecord>> task = new Task<>() {
            @Override
            protected Deque<MovementRecord> call() {
                MapModel map = MapModel.copy(mapModel);
                AISolver aiSolver = new AISolver(map);
                return aiSolver.bfsSolver();
            }
        };

        task.setOnSucceeded(event -> {
            Deque<MovementRecord> solution = task.getValue();
            //重置key
            gameModel.initView();
            setBoxes(gameModel.getBoxes());
            gameModel.setSelectedBox(null);

            if (solution == null || solution.isEmpty()) {
                System.out.println("无解");
                return;
            }
            //打印操作
            for (MovementRecord movementRecord : solution) {
                System.out.println(movementRecord);
            }

            Timeline timeline = new Timeline();

            KeyFrame keyFrame = new KeyFrame(Duration.seconds(0.3), e -> {
                if (!solution.isEmpty()) {
                    MovementRecord movementRecord = solution.pollFirst();
                    if (doMove(mapModel, boxes.get(movementRecord.getBoxKey()), movementRecord.getRow(), movementRecord.getCol(), movementRecord.getDirection())) {
                        gameModel.afterMove(movementRecord.getRow() + movementRecord.getDirection().getRow(), movementRecord.getCol() + movementRecord.getDirection().getCol(), movementRecord.getDirection());
                        System.out.println(movementRecord);
                        System.out.println("success");
                    } else System.out.println("fail");
                    if (gameModel.isSucceed()) {
                        gameModel.getRootPaneController().turnToWinPane();
                        gameModel.endGame();
                    }
                } else {
                    timeline.stop();
                    System.out.println("complete.");
                }
            });

            timeline.getKeyFrames().add(keyFrame);
            timeline.setCycleCount(Timeline.INDEFINITE);
            System.out.println("play");
            timeline.play();
        });

        task.setOnFailed(event -> {
            Throwable error = task.getException();
            System.out.println(error.toString());
        });

        new Thread(task).start();
    }//todo: 加载/演示时禁止操作

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
