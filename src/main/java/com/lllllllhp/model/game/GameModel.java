package com.lllllllhp.model.game;

import com.lllllllhp.controller.gamePage.GameRootPaneController;
import com.lllllllhp.data.MapPreset;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.*;

import static com.lllllllhp.utils.dataUtils.DataUtils.userData;

public class GameModel {
    private GameRootPaneController rootPaneController;
    private GameControllerModel gameControllerModel;
    private Stage currentStage;

    private MapModel mapModel;
    private BoxModel selectedBox;
    private Rectangle broader;
    private Rectangle target;
    private Pane gamePane;
    private Map<Integer, BoxModel> boxes = new HashMap<>();
    private final Timer timer = new Timer(0);

    public static int GRID_SIZE = 75;
    //记录所有移动
    private Deque<MovementRecord> movementStack = new ArrayDeque<>();
    //目标box,随map init
    private BoxModel mainBox;

    //----------------------------------------------------------------------

    public GameModel() {
        this(new MapModel(MapPreset.MAP1));
    }//import default map

    public GameModel(MapModel mapModel) {
        boxes = new HashMap<>();
        this.mapModel = mapModel;
    }

    public void selectBox(MouseEvent mouseEvent) {
        BoxModel clickedBox = null;//点击的box
        Object obj = mouseEvent.getPickResult().getIntersectedNode();//获取点击的节点
        if (obj instanceof BoxModel) {
            clickedBox = (BoxModel) obj;
            if (selectedBox == null) {
                selectedBox = clickedBox;
                selectedBox.setSelected(true);
            } else if (selectedBox != clickedBox) {
                selectedBox.setSelected(false);
                clickedBox.setSelected(true);
                selectedBox = clickedBox;
            } else {
                clickedBox.setSelected(false);
                selectedBox = null;
            }
            System.out.println(clickedBox);
        } else {
            System.out.println("is not BoxModel!");
        }
    }//handle mouse click

    public void initView() {
        //clear all
        gamePane.getChildren().clear();
        //加载步数
        rootPaneController.updateSteps();
        //size
        gamePane.setPrefWidth(GRID_SIZE * mapModel.getWidth());
        gamePane.setPrefHeight(GRID_SIZE * mapModel.getHeight());
        //边框
        broader = new Rectangle(0, 0, GRID_SIZE * mapModel.getWidth(), GRID_SIZE * mapModel.getHeight());
        broader.setFill(Color.gray(0.85));
        broader.setStroke(Color.BLACK);
        broader.setStrokeWidth(2);
        gamePane.getChildren().addAll(broader);
        //目的地提示
        target = new Rectangle(mapModel.getTargetCol() * GRID_SIZE, mapModel.getTargetRow() * GRID_SIZE, 2 * GRID_SIZE, 2 * GRID_SIZE);
        target.setFill(Color.GREEN);
        target.setOpacity(0.2);
        target.setDisable(true);
        gamePane.getChildren().addAll(target);

        //加载障碍物
        for (int i = 0; i < mapModel.getHeight(); i++) {
            for (int j = 0; j < mapModel.getWidth(); j++) {
                if (mapModel.getId(i, j) == 9) {
                    Rectangle obstacle = new Rectangle(j * GRID_SIZE, i * GRID_SIZE, GRID_SIZE, GRID_SIZE);
                    obstacle.setFill(Color.gray(0.5));
                    obstacle.setStroke(Color.BLACK);
                    obstacle.setStrokeWidth(1.5);
                    gamePane.getChildren().add(obstacle);
                }
            }
        }

        initGameData();

        for (Map.Entry<Integer, BoxModel> entry : boxes.entrySet()) {
            BoxModel box = entry.getValue();
            gamePane.getChildren().addAll(box);
        }
        //开始计时
        startTimer();
    }

    public void initGameData() {
        //clear all
        BoxModel.boxNumber = 0;
        boxes.clear();
        movementStack.clear();
        setMainBox(null);
        //copy a map
        int[][] map = new int[mapModel.getHeight()][mapModel.getWidth()];
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[0].length; j++) {
                map[i][j] = mapModel.getId(i, j);
            }
        }
        //Test: show map
        for (int[] array : map) {
            System.out.println(Arrays.toString(array));
        }
        //build Component

        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[0].length; j++) {
                BoxModel box = null;
                switch (map[i][j]) {
                    case 1 -> {
                        box = new BoxModel(1, Color.ORANGE, i, j, GRID_SIZE, GRID_SIZE);//左上角为坐标
                        map[i][j] = 0;
                    }
                    case 2 -> {
                        box = new BoxModel(2, Color.PINK, i, j, GRID_SIZE * 2, GRID_SIZE);
                        map[i][j] = 0;
                        map[i][j + 1] = 0;
                    }
                    case 3 -> {
                        box = new BoxModel(3, Color.BLUE, i, j, GRID_SIZE, GRID_SIZE * 2);
                        map[i][j] = 0;
                        map[i + 1][j] = 0;
                    }
                    case 4 -> {
                        box = new BoxModel(4, Color.GREEN, i, j, GRID_SIZE * 2, GRID_SIZE * 2);
                        map[i][j] = 0;
                        map[i + 1][j] = 0;
                        map[i][j + 1] = 0;
                        map[i + 1][j + 1] = 0;
                        //设置目标box
                        if (mainBox == null) {
                            setMainBox(box);
                            System.out.println("set MainBox succeed.");
                        } else {
                            System.out.println("Warning: multiple MainBox.");
                        }
                    }
                }
                if (box != null) {
                    boxes.put(box.getKey(), box);//加到boxMap
                }
            }
        }
    }

    public void loadGame() {
        initView();
        //导入保存的操作
        setMovementStack(userData.getMapRecord().getRecordDeque());
        //重新加载key
        loadKeyMap(userData.getMapRecord().getKeyMap());
    }

    public Map<String, Integer> getKeyMap() {
        if (boxes == null) return null;
        //储存key对应方块的位置，重新加载位置方块对应key不变
        Map<String, Integer> keyMap = new HashMap<>();
        for (Map.Entry<Integer, BoxModel> entry : boxes.entrySet()) {
            String rowCol = String.format("%d%d", entry.getValue().getRow(), entry.getValue().getCol());
            keyMap.put(rowCol, entry.getKey());
        }

        return keyMap;
    }

    public void loadKeyMap(Map<String, Integer> map) {
        Map<Integer, BoxModel> newBoxes = new HashMap<>();
        for (Map.Entry<Integer, BoxModel> entry : boxes.entrySet()) {
            String current = String.format("%d%d", entry.getValue().getRow(), entry.getValue().getCol());
            int key = map.get(current);
            entry.getValue().setKey(key);
            newBoxes.put(key, entry.getValue());
        }

        setBoxes(newBoxes);
        System.out.println(newBoxes);
    }

    public void doMoveUp() {
        if (selectedBox != null) {
            if (GameControllerModel.doMove(mapModel, selectedBox, selectedBox.getRow(), selectedBox.getCol(), Direction.UP)) {
                afterMove(selectedBox.getRow(), selectedBox.getCol(), Direction.UP);
            }
        }
    }

    public void doMoveDown() {
        if (selectedBox != null) {
            if (GameControllerModel.doMove(mapModel, selectedBox, selectedBox.getRow(), selectedBox.getCol(), Direction.DOWN)) {
                afterMove(selectedBox.getRow(), selectedBox.getCol(), Direction.DOWN);
            }
        }
    }

    public void doMoveLeft() {
        if (selectedBox != null) {
            if (GameControllerModel.doMove(mapModel, selectedBox, selectedBox.getRow(), selectedBox.getCol(), Direction.LEFT)) {
                afterMove(selectedBox.getRow(), selectedBox.getCol(), Direction.LEFT);
            }
        }
    }

    public void doMoveRight() {
        if (selectedBox != null) {
            if (GameControllerModel.doMove(mapModel, selectedBox, selectedBox.getRow(), selectedBox.getCol(), Direction.RIGHT)) {
                afterMove(selectedBox.getRow(), selectedBox.getCol(), Direction.RIGHT);
            }
        }
    }

    public void afterMove(int row, int col, Direction direction) {
        mapModel.addSteps();
        rootPaneController.updateSteps();
        if (selectedBox != null) {
            movementStack.push(new MovementRecord(mapModel.getSteps(), selectedBox.getKey(), row - direction.getRow(), col - direction.getCol(), direction));
            System.out.println(movementStack.peek());
        }
        if (isSucceed()) {
            getRootPaneController().turnToWinPane();
            endGame();
        }
    }

    public boolean isSucceed() {
        return mainBox.getCol() == mapModel.getTargetCol() && mainBox.getRow() == mapModel.getTargetRow();
    }

    public void endGame() {

    }//todo

    public void startTimer() {
        timer.getTimeline().getKeyFrames().add(new KeyFrame(Duration.seconds(1), actionEvent -> {
            timer.addSeconds(1);
            rootPaneController.updateTimer();
        }));
        timer.getTimeline().setCycleCount(Animation.INDEFINITE);
        timer.getTimeline().play();
    }//todo

    //-----------------------------------------------------------
    public MapModel getMapModel() {
        return mapModel;
    }

    public void setMapModel(MapModel mapModel) {
        this.mapModel = mapModel;
    }

    public Stage getCurrentStage() {
        return currentStage;
    }

    public void setCurrentStage(Stage currentStage) {
        this.currentStage = currentStage;
    }

    public Pane getGamePane() {
        return gamePane;
    }

    public void setGamePane(Pane gamePane) {
        this.gamePane = gamePane;
    }

    public BoxModel getSelectedBox() {
        return selectedBox;
    }

    public void setSelectedBox(BoxModel selectedBox) {
        this.selectedBox = selectedBox;
    }

    public GameControllerModel getGameController() {
        return gameControllerModel;
    }

    public void setGameController(GameControllerModel gameControllerModel) {
        this.gameControllerModel = gameControllerModel;
    }

    public void setRootPaneController(GameRootPaneController rootPaneController) {
        this.rootPaneController = rootPaneController;
    }

    public Deque<MovementRecord> getMovementStack() {
        return movementStack;
    }

    public void setMovementStack(Deque<MovementRecord> movementStack) {
        this.movementStack = movementStack;
    }

    public GameRootPaneController getRootPaneController() {
        return rootPaneController;
    }

    public BoxModel getMainBox() {
        return mainBox;
    }

    public void setMainBox(BoxModel mainBox) {
        this.mainBox = mainBox;
    }

    public Map<Integer, BoxModel> getBoxes() {
        return boxes;
    }

    public void setBoxes(Map<Integer, BoxModel> boxes) {
        this.boxes = boxes;
    }

    public Timer getTimer() {
        return timer;
    }
}
