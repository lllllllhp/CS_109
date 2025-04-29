package com.proj510.controller.userPage;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.proj510.controller.gamePage.GameRootPaneController;
import com.proj510.data.MapPre;
import com.proj510.data.UserData;
import com.proj510.model.game.MapModel;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class MainPageController {
    private Stage currentStage;
    private UserData userData;
    @FXML
    Pane mainPane;
    @FXML
    Pane choosePane;
    @FXML
    VBox mapContainer;

    @FXML
    Label id;
    @FXML
    Label level;
    @FXML
    Label rating;
    @FXML
    Label warning;
    //choosePane
    @FXML
    Label tips;
    private Label currentLabel;
    private MapPre currentMap;

    GameRootPaneController gameRootPaneController;

    @FXML
    public void handleNewGame() {
        chooseMap();
    }

    @FXML
    public void handleLoadGame() {
        if (userData.getMapRecord() != null) {
            loadGamePage();
            gameRootPaneController.initLoadGamePage();
        } else {
            warning.setText("No Game Saved.");
            System.out.println("No Game Saved.");
        }
    }

    public void loadGamePage() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/gamePage/gameRootPane.fxml"));
            Parent root = loader.load();
            GameRootPaneController gameRootPaneController = loader.getController();
            this.gameRootPaneController = gameRootPaneController;
            gameRootPaneController.setCurrentStage(currentStage);
            gameRootPaneController.setUserData(userData);

            currentStage.getScene().setRoot(root);
            currentStage.setTitle("Game");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void initMainPage() {
        choosePane.setVisible(false);
        mainPane.setVisible(true);
        id.setText(userData.getId());
        level.setText(String.valueOf(userData.getLevel()));
        rating.setText(String.valueOf(userData.getRating()));
    }

    public void chooseMap() {
        tips.setText("请选择你的地图");
        mainPane.setVisible(false);
        choosePane.setVisible(true);
        loadMapLabels();
    }

    public void loadMapLabels() {
        Path mapFolder = Paths.get("src/main/resources/maps");
        if (Files.exists(mapFolder) && Files.isDirectory(mapFolder)) {
            try (Stream<Path> pathStream = Files.list(mapFolder)){
                var mapFiles = pathStream.toList();
                if (mapFiles.isEmpty()) {
                    System.out.println("暂无关卡文件");
                    tips.setText("暂无关卡文件");
                    Label noMapLabel = new Label("暂无关卡文件");
                    mapContainer.getChildren().addAll(noMapLabel);
                } else {
                    mapFiles.forEach(path -> {
                        //预加载地图
                        String map;
                        Gson gson = new GsonBuilder().create();
                        try {
                            map = Files.readString(path);
                            MapPre mapPre = gson.fromJson(map, MapPre.class);
                            Label mapLabel = getMapLabel(mapPre);
                            mapContainer.getChildren().addAll(mapLabel);
                        } catch (IOException e) {
                            System.out.println("地图读取失败");
                            warning.setText("地图读取失败");
                        }
                        //创建地图选择按钮 todo 美化
                    });
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        } else {
            System.out.println("Can't find map folder. ");
            try {
                Files.createDirectories(mapFolder.getParent());
                System.out.println("Create map folder.");
                loadMapLabels();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private Label getMapLabel(MapPre mapPre) {
        Label mapLabel = new Label(mapPre.getName());
        mapLabel.setPrefWidth(200);
        //设置监听
        mapLabel.setOnMouseClicked(event -> {
            if (currentLabel == null) {
                currentLabel = mapLabel;
                mapLabel.setTextFill(Color.RED);
            } else if (currentLabel != mapLabel) {
                currentLabel.setTextFill(Color.BLACK);
                mapLabel.setTextFill(Color.RED);
            }
            setCurrentLabel(mapLabel);
            setCurrentMap(mapPre);
        });
        return mapLabel;
    }

    @FXML
    public void handleConfirm() {
        if (currentMap != null) {
            loadGamePage();
            gameRootPaneController.setChooseMap(new MapModel(currentMap));
            gameRootPaneController.initNewGamePage();
        } else {
            warning.setText("请选择地图");
        }
    }

    //------------------------------------------------------------------------------------
    public void setCurrentStage(Stage currentStage) {
        this.currentStage = currentStage;
    }

    public void setUserData(UserData userData) {
        this.userData = userData;
    }

    public void setCurrentLabel(Label currentLabel) {
        this.currentLabel = currentLabel;
    }

    public void setCurrentMap(MapPre currentMap) {
        this.currentMap = currentMap;
    }
}
