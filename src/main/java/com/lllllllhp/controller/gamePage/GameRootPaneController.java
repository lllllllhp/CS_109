package com.lllllllhp.controller.gamePage;

import com.lllllllhp.controller.userPage.MainPageController;
import com.lllllllhp.model.game.GameControllerModel;
import com.lllllllhp.model.game.GameModel;
import com.lllllllhp.model.game.MapModel;

import com.lllllllhp.model.game.Time;
import com.lllllllhp.utils.socket.NetUtils;
import com.lllllllhp.utils.socket.messageModel.Command;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDateTime;

import static com.lllllllhp.controller.userPage.MainPageController.isTimeLimit;
import static com.lllllllhp.controller.userPage.MainPageController.limitTime;
import static com.lllllllhp.utils.Settings.currentStage;
import static com.lllllllhp.utils.dataUtils.DataUtils.userData;

public class GameRootPaneController {
    //gamePage
    @FXML
    private Pane gamePane;
    @FXML
    private Pane pagePane;
    @FXML
    private Pane operatePane;
    @FXML
    private Label stepCounter;
    @FXML
    private Label timerLabel;
    @FXML
    private Label tips;
    @FXML
    Label timeLeft;
    //victory pane
    @FXML
    private Label totalSteps;
    @FXML
    private Label totalTime;
    @FXML
    private Label userName;
    @FXML
    private Button returnToMain;
    @FXML
    private Button reviewGame;
    @FXML
    private Pane winPane;

    private GameControllerModel gameControllerModel;
    private GameModel gameModel;
    private MapModel chooseMap;

    public void initNewGamePage() {
        initPane();
        gameModel.setMapModel(chooseMap);
        gameControllerModel.setMapModel(gameModel.getMapModel());
        gameModel.initView();
    }

    public void initLoadGamePage() {
        initPane();
        gameModel.setMapModel(userData.getMapRecord().getMapModel());
        gameControllerModel.setMapModel(gameModel.getMapModel());
        gameModel.loadGame();
    }

    public void initSpectatingPage() {
        if (!NetUtils.hasClient() || !NetUtils.client.isConnected()) return;
        //-------------------------------
        tips.setTextFill(Color.BLACK);
        tips.setText(String.format("You are watching: %s", NetUtils.IP));
        pagePane.setDisable(false);
        winPane.setDisable(true);
        winPane.setVisible(false);
        operatePane.setVisible(false);
        if (gameModel == null) gameModel = new GameModel() {
            //无法选择box
            @Override
            public void selectBox(MouseEvent mouseEvent) {
            }

            //无法成功
            @Override
            public void checkIsSucceed() {
                if (getMainBox().getCol() == getMapModel().getTargetCol() && getMainBox().getRow() == getMapModel().getTargetRow()) {
                    getTimeline().stop();
                    //
                    tips.setTextFill(Color.BLACK);
                    tips.setText("Player win the game.");
                    gamePane.getChildren().clear();
                }
            }
        };
        gameModel.setTime(new Time(0));
        updateTimer();
        if (gameControllerModel == null) gameControllerModel = new GameControllerModel();

        gameModel.setGameController(gameControllerModel);
        gameControllerModel.setGameModel(gameModel);

        gameModel.setRootPaneController(this);
        gameModel.setGamePane(gamePane);
        //---------------------------------------------
        gameModel.setMapModel(NetUtils.ClientData.spectatingMap.getMapModel());
        gameControllerModel.setMapModel(NetUtils.ClientData.spectatingMap.getMapModel());

        NetUtils.ClientData.clientGame = gameModel;
        NetUtils.ClientData.clientGameCon = gameControllerModel;

        gameModel.loadGame();
    }

    public void initPane() {
        //键盘监听
        currentStage.getScene().addEventFilter(KeyEvent.KEY_PRESSED, keyEvent -> {
            KeyCode code = keyEvent.getCode();
            switch (code) {
                case UP, W -> gameModel.doMoveUp();
                case DOWN, S -> gameModel.doMoveDown();
                case LEFT, A -> gameModel.doMoveLeft();
                case RIGHT, D -> gameModel.doMoveRight();
            }
        });


        pagePane.setDisable(false);
        winPane.setDisable(true);
        winPane.setVisible(false);
        operatePane.setVisible(true);
        tips.setText("");

        gameModel = new GameModel();
        gameControllerModel = new GameControllerModel();

        gameModel.setGameController(gameControllerModel);
        gameControllerModel.setGameModel(gameModel);

        gameModel.setRootPaneController(this);
        gameModel.setGamePane(gamePane);
    }

    @FXML
    public void handleKeyPress(KeyEvent keyEvent) {
        //init中实现，防止上下左右被吞
    }

    @FXML
    public void handleMouseClick(MouseEvent mouseEvent) {
        gameModel.selectBox(mouseEvent);
    }

    @FXML
    public void handleUndo() {
        gameControllerModel.undo();
    }

    @FXML
    public void handleRestart() {
        gameControllerModel.restart();
    }

    @FXML
    public void handleSave() {
        if ("Guest_".equals(userData.getId())) {
            tips.setText("You can't save game.");
            return;
        }
        gameControllerModel.saveGame();
    }

    @FXML
    public void handleSolve() {
        try {
            gameControllerModel.aiSolve();
        } catch (InterruptedException e) {
            System.out.println("ai解决失败");
            System.out.println(e.toString());
        }
    }

    @FXML
    public void upButton() {
        gameModel.doMoveUp();
    }

    @FXML
    public void downButton() {
        gameModel.doMoveDown();
    }

    @FXML
    public void leftButton() {
        gameModel.doMoveLeft();
    }

    @FXML
    public void rightButton() {
        gameModel.doMoveRight();
    }

    public void updateSteps() {
        stepCounter.setText(String.format("Steps: %d", gameModel.getMapModel().getSteps()));
    }

    public void updateTimer() {
        if (!isTimeLimit) {
            timerLabel.setText(String.format("Time: %s", gameModel.getTime().toString()));
            timerLabel.setVisible(true);
            timeLeft.setVisible(false);
        }
        else {
            timerLabel.setVisible(false);
            timeLeft.setVisible(true);
            timeLeft.setText(String.format("         Time Left：%s", getRemainingTimeString(limitTime)));
        }
    }

    public String getRemainingTimeString(int totalSeconds) {
        int elapsed = gameModel.getTime().getTotal();
        int remaining = totalSeconds - elapsed;
        if (remaining < 0) remaining = 0;
        int min = remaining / 60;
        int sec = remaining % 60;
        return String.format("%02d:%02d", min, sec);
    }


    public void turnToWinPane() {
        gameModel.getTimeline().stop();

        totalSteps.setText(String.format("Total Steps: %d", gameModel.getMapModel().getSteps()));
        totalTime.setText(String.format("Time cost: %s", gameModel.getTime().toString()));
        userName.setText(userData.getId());

        //保存胜利记录
        gameControllerModel.saveGame();
        userData.getMapRecord().setHadSuccess(true);
        userData.saveRecord(userData.getMapRecord());

        //清除上次游戏记录
        userData.setMapRecord(null);
        winPane.toFront();
        winPane.setDisable(false);
        pagePane.setDisable(true);
        winPane.setVisible(true);
    }

    @FXML
    public void handleReturn() {
        gameModel.getTimeline().stop();
        //退出时保存
        if (gameControllerModel != null) gameControllerModel.saveGame();
        returnToMainPage();

        if (NetUtils.hasServer() && NetUtils.server.isRunning()) {
            try {
                NetUtils.server.broadcast(new Command(Command.CommandType.RETURN, userName.getId(), LocalDateTime.now()));
            } catch (IOException e) {
                System.out.println(e.toString());
            }
        }
    }

    @FXML
    public void reviewSavedGame() {
        if ("random_".equals(gameModel.getMapModel().getName()) || !userData.getPlayRecords().containsKey(gameModel.getMapModel().getName())) {
            tips.setText("No success record!");
            return;
        }
        gameControllerModel.reviewSavedGame();
    }

    //-----------------------------------------------------------------------------------------------------------------------
    //win pane
    @FXML
    public void returnToMainPage() {
        gameModel.getTimeline().stop();

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/userPage/mainPage.fxml"));
            Parent root = loader.load();
            MainPageController mainPageController = loader.getController();
            mainPageController.setCurrentStage(currentStage);
            //更新主界面
            mainPageController.initMainPage();

            currentStage.getScene().setRoot(root);
            currentStage.setTitle("MainPage");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    public void handleReview() {
        gameControllerModel.review(gameModel.getMapModel().getName());
    }

    //----------------------------------------------------------------------------------------------------------------

    public Label getStepCounter() {
        return stepCounter;
    }

    public void setStepCounter(Label stepCounter) {
        this.stepCounter = stepCounter;
    }

    public GameControllerModel getGameController() {
        return gameControllerModel;
    }

    public void setGameController(GameControllerModel gameControllerModel) {
        this.gameControllerModel = gameControllerModel;
    }

    public Label getTotalSteps() {
        return totalSteps;
    }

    public void setTotalSteps(Label totalSteps) {
        this.totalSteps = totalSteps;
    }

    public MapModel getChooseMap() {
        return chooseMap;
    }

    public void setChooseMap(MapModel chooseMap) {
        this.chooseMap = chooseMap;
    }

    public Pane getPagePane() {
        return pagePane;
    }

    public Pane getWinPane() {
        return winPane;
    }

    public Pane getGamePane() {
        return gamePane;
    }

    public Label getTips() {
        return tips;
    }
}
