package com.lllllllhp.controller.gamePage;

import com.lllllllhp.controller.userPage.MainPageController;
import com.lllllllhp.model.game.GameControllerModel;
import com.lllllllhp.model.game.GameModel;
import com.lllllllhp.model.game.MapModel;

import com.lllllllhp.utils.socket.NetUtils;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;

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
    //victory pane
    @FXML
    private Label totalSteps;
    @FXML
    private Button returnToMain;
    @FXML
    private Pane winPane;

    private GameControllerModel gameControllerModel;
    private Stage currentStage;
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
        pagePane.setDisable(false);
        winPane.setDisable(true);
        winPane.setVisible(false);
        operatePane.setVisible(false);
        gameModel = new GameModel(){
            //无法选择box
            @Override
            public void selectBox(MouseEvent mouseEvent) {
            }
        };
        gameControllerModel = new GameControllerModel();

        gameModel.setGameController(gameControllerModel);
        gameControllerModel.setGameModel(gameModel);

        gameModel.setRootPaneController(this);
        gameModel.setGamePane(gamePane);
        gameModel.setCurrentStage(currentStage);
        //---------------------------------------------
        gameModel.setMapModel(NetUtils.ClientData.spectatingMap.getMapModel());
        gameControllerModel.setMapModel(NetUtils.ClientData.spectatingMap.getMapModel());

        NetUtils.ClientData.clientGame = gameModel;
        NetUtils.ClientData.clientGameCon = gameControllerModel;

        gameModel.loadGame();
    }

    public void initPane() {
        pagePane.setDisable(false);
        winPane.setDisable(true);
        winPane.setVisible(false);
        operatePane.setVisible(true);

        gameModel = new GameModel();
        gameControllerModel = new GameControllerModel();

        gameModel.setGameController(gameControllerModel);
        gameControllerModel.setGameModel(gameModel);

        gameModel.setRootPaneController(this);
        gameModel.setGamePane(gamePane);
        gameModel.setCurrentStage(currentStage);
    }

    @FXML
    public void handleKeyPress(KeyEvent keyEvent) {
        KeyCode code = keyEvent.getCode();
        switch (code) {
            case UP, W -> gameModel.doMoveUp();
            case DOWN, S -> gameModel.doMoveDown();
            case LEFT, A -> gameModel.doMoveLeft();
            case RIGHT, D -> gameModel.doMoveRight();
        }
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
        timerLabel.setText(String.format("Time: %s", gameModel.getTime().toString()));
    }

    public void turnToWinPane() {
        totalSteps.setText(String.format("Total Steps: %d", gameModel.getMapModel().getSteps()));

        //保存胜利记录
        gameControllerModel.saveGame();
        userData.getMapRecord().setHadSuccess(true);
        userData.saveRecord(userData.getMapRecord());
        //清除上次游戏记录
        userData.setMapRecord(null);
        userData.addRating(0.5);

        winPane.toFront();
        winPane.setDisable(false);
        pagePane.setDisable(true);
        winPane.setVisible(true);
    }

    @FXML
    public void handleReturn() {
        //退出时保存
        if (gameControllerModel != null) gameControllerModel.saveGame();
        returnToMainPage();
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

    //----------------------------------------------------------------------------------------------------------------
    public Stage getCurrentStage() {
        return currentStage;
    }

    public void setCurrentStage(Stage currentStage) {
        this.currentStage = currentStage;
    }

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
}
