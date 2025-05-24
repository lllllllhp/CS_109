package com.lllllllhp.model.game;

import com.lllllllhp.data.MapRecord;
import com.lllllllhp.utils.aiSolver.AISolver;
import com.lllllllhp.utils.audioPlayer.AudioPlayer;
import com.lllllllhp.utils.socket.NetUtils;
import com.lllllllhp.utils.socket.messageModel.Command;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
import javafx.concurrent.Task;
import javafx.util.Duration;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;

import static com.lllllllhp.model.game.GameModel.GRID_SIZE;
import static com.lllllllhp.utils.dataUtils.DataUtils.userData;

public class GameControllerModel {
    private GameModel gameModel;
    private MapModel mapModel;

    public GameControllerModel() {
    }

    static public boolean doMove(MapModel mapModel, BoxModel box, int row, int col, Direction direction) {
        int nextRow = row + direction.getRow();
        int nextCol = col + direction.getCol();
        if (!box.isMoving()) {
            switch (mapModel.getId(row, col)) {
                case 1 -> {//小兵
                    if (mapModel.checkInHeightSize(nextRow) && mapModel.checkInWidthSize(nextCol)
                            && mapModel.getId(nextRow, nextCol) == 0) {
                        mapModel.getMatrix()[row][col] = 0;
                        mapModel.getMatrix()[nextRow][nextCol] = 1;
                        moveWithAnimation(box, nextCol, nextRow, direction);
                        return true;
                    }
                }
                case 2 -> {//横
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
                }
                case 3 -> {//竖
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
                }
                case 4 -> {//曹操
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
        }
        return false;
    }

    static public void moveWithAnimation(BoxModel boxModel, int nextCol, int nextRow, Direction direction) {
        boxModel.setCol(nextCol);
        boxModel.setRow(nextRow);
        AudioPlayer.playEffect("/audio/transition.wav");
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
        if (gameModel.getMovementStack().isEmpty()) {
            System.out.println("You can't undo now.");
            return;
        }

        MovementRecord movementRecord = gameModel.getMovementStack().peek();
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
            if (doMove(mapModel, gameModel.getBoxes().get(movementRecord.getBoxKey()), movementRecord.getRow() - undoDirection.getRow(), movementRecord.getCol() - undoDirection.getCol(), undoDirection)) {
                System.out.println(gameModel.getMovementStack().pop());
                mapModel.minusSteps();
                gameModel.getRootPaneController().updateSteps();
                System.out.println("Undo succeed.");
            } else System.out.println("Undo error.");
        }

        if (NetUtils.hasServer() && NetUtils.server.isRunning()) {
            try {
                NetUtils.server.broadcast(new Command(Command.CommandType.UNDO, userData.getId(), LocalDateTime.now()));
            } catch (IOException e) {
                System.out.println(e.toString());
            }
        }
    }

    public void restart() {
        for (int i = 0; i < mapModel.getMatrix().length; i++) {
            mapModel.getMatrix()[i] = Arrays.copyOf(mapModel.getCopyData()[i], mapModel.getCopyData()[i].length);
        }

        gameModel.getTimeline().stop();
        gameModel.setTime(new Time(0));
        mapModel.setSteps(0);

        gameModel.initView();
        System.out.println("Game Restart");

        if (NetUtils.hasServer() && NetUtils.server.isRunning()) {
            try {
                NetUtils.server.broadcast(new Command(Command.CommandType.RESTART, userData.getId(), LocalDateTime.now()));
            } catch (IOException e) {
                System.out.println(e.toString());
            }
        }
    }

    public void saveGame() {
        userData.setMapRecord(new MapRecord(mapModel));
        //记录移动
        userData.getMapRecord().setRecordDeque(gameModel.getMovementStack());
        //记录当前box的key
        userData.getMapRecord().setKeyMap(gameModel.getKeyMap());
        //记录用时
        userData.getMapRecord().setTime(gameModel.getTime());
        System.out.println(userData.getMapRecord().getTime());

        userData.save();
    }

    public void aiSolve() throws InterruptedException {
        //取消选中box
        if (gameModel.getSelectedBox()!=null) {
            gameModel.getSelectedBox().setSelected(false);
            gameModel.setSelectedBox(null);
        }
        //传给观众
        if (NetUtils.hasServer() && NetUtils.server.isRunning()) {
            try {
                NetUtils.server.broadcast(new Command(Command.CommandType.AI_SOLVE, userData.getId(), LocalDateTime.now()));
            } catch (IOException e) {
                System.out.println(e.toString());
            }
        }

        Task<Deque<MovementRecord>> task = getTask();

        new Thread(task).start();
    }//todo: 加载/演示时禁止操作

    private Task<Deque<MovementRecord>> getTask() {
        Task<Deque<MovementRecord>> task = new Task<>() {
            @Override
            protected Deque<MovementRecord> call() {
                MapModel map = MapModel.copy(mapModel);
                AISolver aiSolver = new AISolver(map, gameModel.getKeyMap());
                return aiSolver.aStarSolver();
            }
        };

        task.setOnSucceeded(event -> {
            Deque<MovementRecord> solution = task.getValue();

            if (solution == null || solution.isEmpty()) {
                System.out.println("无解");
                return;
            }

            playSolution(solution);

        });

        task.setOnFailed(event -> {
            Throwable error = task.getException();
            System.out.println(error.toString());
        });
        return task;
    }

    public void review(String mapName) {
        //通过地图名得到解决方法
        //todo：确保地图名字各不同
        System.out.println("try review");
        System.out.println("Map name: " + mapName);
        System.out.println("Play records: " + userData.getPlayRecords());
        System.out.println("Record for this map: " + userData.getPlayRecords().get(mapName));
        
        // 创建一个新的Deque来存储解决方案
        Deque<MovementRecord> originalSolution = userData.getPlayRecords().get(mapName).getRecordDeque();
        Deque<MovementRecord> solution = new ArrayDeque<>(originalSolution);
        
        //类似restart
        for (int i = 0; i < mapModel.getMatrix().length; i++) {
            mapModel.getMatrix()[i] = Arrays.copyOf(mapModel.getCopyData()[i], mapModel.getCopyData()[i].length);
        }
        gameModel.getTimeline().stop();
        mapModel.setSteps(0);
        gameModel.initView();

        System.out.println("Solution: " + solution);
        playSolution(solution);
        System.out.println("review done");
    }

    public void reviewSavedGame() {
        Deque<MovementRecord> solution = userData.getPlayRecords().get(gameModel.getMapModel().getName()).getRecordDeque();

        playSolution(solution);
    }

    //用于review和ai解决
    public void playSolution(Deque<MovementRecord> solution) {
        Timeline timeline = new Timeline();

        KeyFrame keyFrame = new KeyFrame(Duration.seconds(0.3), e -> {
            if (!solution.isEmpty()) {
                MovementRecord movementRecord = solution.pollFirst();
                if (doMove(mapModel, gameModel.getBoxes().get(movementRecord.getBoxKey()), movementRecord.getRow(), movementRecord.getCol(), movementRecord.getDirection())) {
                    //设置选中的box，防止异常
                    gameModel.setSelectedBox(getGameModel().getBoxes().get(movementRecord.getBoxKey()));
                    gameModel.afterMove(movementRecord.getRow() + movementRecord.getDirection().getRow(), movementRecord.getCol() + movementRecord.getDirection().getCol(), movementRecord.getDirection());
                } else {
                    System.out.println("fail at: " + movementRecord);
                    timeline.stop();
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
}
