package com.proj510.controller.gamePage;

import com.proj510.controller.userPage.MainPageController;
import com.proj510.data.UserData;
import com.proj510.model.game.GameControllerModel;
import com.proj510.model.game.GameModel;
import com.proj510.model.game.MapModel;
import com.proj510.data.MapPreset;
import javafx.event.ActionEvent;
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

public class GameRootPaneController {
    //gamePage
    @FXML
    private Pane gamePane;
    @FXML
    private Pane pagePane;
    @FXML
    private Label stepCounter;
    //victory pane
    @FXML
    private Label totalSteps;
    @FXML
    private Button returnToMain;
    @FXML
    private Pane winPane;

    private UserData userData;
    private GameControllerModel gameControllerModel;
    private Stage currentStage;
    private GameModel gameModel;
    private MapModel chooseMap;

    public void initNewGamePage() {
        initPane();
        gameModel.setMapModel(chooseMap);
        gameControllerModel.setMapModel(gameModel.getMapModel());
        gameControllerModel.setBoxes(gameModel.getBoxes());
        gameModel.initGame();
    }

    public void initLoadGamePage() {
        initPane();
        gameModel.setMapModel(userData.getMapRecord().getMapModel());
        gameControllerModel.setMapModel(gameModel.getMapModel());
        gameControllerModel.setBoxes(gameModel.getBoxes());
        gameModel.initGame();
    }

    public void initPane() {
        winPane.setVisible(false);
        gameModel = new GameModel();
        gameControllerModel = new GameControllerModel();

        gameModel.setGameController(gameControllerModel);
        gameControllerModel.setGameModel(gameModel);

        gameModel.setRootPaneController(this);
        gameModel.setGamePane(gamePane);
        gameModel.setCurrentStage(currentStage);

        gameControllerModel.setUserData(userData);
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
    public void handleUndo(ActionEvent actionEvent) {
        gameControllerModel.undo();
    }

    @FXML
    public void handleRestart(ActionEvent actionEvent) {
        gameControllerModel.restart();
    }

    @FXML
    public void handleSave(ActionEvent actionEvent) {
        gameControllerModel.saveGame();
    }

    @FXML
    public void upButton(ActionEvent actionEvent) {
        gameModel.doMoveUp();
    }
    @FXML
    public void downButton(ActionEvent actionEvent) {
        gameModel.doMoveDown();
    }
    @FXML
    public void leftButton(ActionEvent actionEvent) {
        gameModel.doMoveLeft();
    }
    @FXML
    public void rightButton(ActionEvent actionEvent) {
        gameModel.doMoveRight();
    }

    public void updateSteps() {
        stepCounter.setText(String.format("Steps: %d", gameModel.getMapModel().getSteps()));
    }

    public void turnToWinPane() {
        totalSteps.setText(String.format("Total Steps: %d", gameModel.getMapModel().getSteps()));
        //清除上次游戏记录
        userData.setMapRecord(null);
        winPane.setVisible(true);
    }

    //-----------------------------------------------------------------------------------------------------------------------
    //win pane
    public void returnToMainPage(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/userPage/mainPage.fxml"));
            Parent root = loader.load();
            MainPageController mainPageController = loader.getController();
            mainPageController.setCurrentStage(currentStage);
            mainPageController.setUserData(userData);
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

    public UserData getUserData() {
        return userData;
    }

    public void setUserData(UserData userData) {
        this.userData = userData;
    }

    public MapModel getChooseMap() {
        return chooseMap;
    }

    public void setChooseMap(MapModel chooseMap) {
        this.chooseMap = chooseMap;
    }
}
