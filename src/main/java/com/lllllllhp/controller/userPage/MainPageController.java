package com.lllllllhp.controller.userPage;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.lllllllhp.controller.gamePage.GameRootPaneController;
import com.lllllllhp.data.MapPre;
import com.lllllllhp.data.MapRecord;
import com.lllllllhp.model.game.MapModel;
import com.lllllllhp.model.game.Time;
import com.lllllllhp.utils.dataUtils.DataUtils;
import com.lllllllhp.utils.Settings;
import com.lllllllhp.utils.socket.NetUtils;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.BindException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;

import static com.lllllllhp.utils.Settings.currentStage;
import static com.lllllllhp.utils.dataUtils.DataUtils.userData;

public class MainPageController {
    private Stage currentStage;

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
    @FXML
    Label airLabel;
    @FXML
    Pane socketPane;
    @FXML
    Label ip;
    @FXML
    Label port;
    //choosePane
    @FXML
    Label tips;
    @FXML
    Label mapName;
    @FXML
    Label stepCost;
    @FXML
    Label timeCost;
    @FXML
    Pane timeLimitPane;
    @FXML
    ToggleButton two;
    @FXML
    ToggleButton five;
    @FXML
    ToggleButton ten;
    @FXML
    ToggleButton twenty;


    private Label currentLabel;
    private MapPre currentMap;

    GameRootPaneController gameRootPaneController;

    @FXML
    public void handleNewGame() {
        chooseMap();
    }

    @FXML
    public void handleLoadGame() {
        //不让游客加载
        if ("Guest_".equals(userData.getId())) {
            warning.setText("No Game Saved.");
            return;
        }

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
            this.gameRootPaneController = loader.getController();

            currentStage.getScene().setRoot(root);
            currentStage.setTitle("Game");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void initMainPage() {
        //初始化地图选择界面
        mapName.setText("");
        timeCost.setText("");
        stepCost.setText("");
        //初始化mainPage
        if (NetUtils.hasServer()) {
            airLabel.setTextFill(Color.GREEN);
            socketPane.setVisible(true);
        } else {
            airLabel.setTextFill(Color.BLACK);
            socketPane.setVisible(false);
        }

        clientPane.setVisible(false);
        choosePane.setVisible(false);
        mainPane.setVisible(true);
        id.setText(" " + userData.getId());
        level.setText(String.format(" Lv %s", userData.getLevel()));
        rating.setText(String.format(" rating: %s", userData.getRating()));
        currentStage.setTitle(userData.getId());
    }

    public void chooseMap() {
        tips.setText("请选择你的地图");
        mainPane.setVisible(false);
        choosePane.setVisible(true);
        loadMapLabels();
    }

    @FXML
    public void handleOnAir() {
        if (!NetUtils.hasClient() && !NetUtils.hasServer()) {
            airLabel.setTextFill(Color.GREEN);
            NetUtils.startServer();
            ip.setText(" IP: " + NetUtils.IP);
            port.setText(" PORT: " + NetUtils.PORT);
            socketPane.setVisible(true);
        } else if (NetUtils.hasServer()) {
            airLabel.setTextFill(Color.BLACK);
            NetUtils.endServer();
            socketPane.setVisible(false);
        }
    }

    //客户端连接
    @FXML
    Pane clientPane;
    @FXML
    TextField ipField;
    @FXML
    TextField portField;
    @FXML
    Label socketWarning;

    @FXML
    public void handleSpectate() {
        if (NetUtils.hasServer()) {
            System.out.println("You're on air now!");
            warning.setText("You're on air now!");
        } else if (!NetUtils.hasClient()) {
            clientPane.setVisible(true);
            socketWarning.setText("");
        }
    }

    @FXML
    public void cancelConnect() {
        clientPane.setVisible(false);
    }

    @FXML
    public void handleConnect() {
        try {
            String ip = ipField.getText();
            int port = Integer.parseInt(portField.getText());
            NetUtils.startClient(ip, port);
        } catch (NumberFormatException e) {
            socketWarning.setText("Please check IP and PORT.");
            System.out.println(e.toString());
            return;
        } catch (RuntimeException e) {
            socketWarning.setText(e.getMessage());
            System.out.println(e.toString());
            return;
        }

        if (!NetUtils.hasClient() || !NetUtils.client.isConnected()) {
            socketWarning.setText("Please check IP and PORT.");
            return;
        }
        clientPane.setVisible(false);

        warning.setTextFill(Color.BLACK);
        warning.setText(String.format("Connect to %s !", ip));

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/gamePage/gameRootPane.fxml"));
            Parent root = loader.load();
            this.gameRootPaneController = loader.getController();

            gameRootPaneController.getTips().setTextFill(Color.BLACK);
            gameRootPaneController.getTips().setText("Waiting for Player...");

            currentStage.getScene().setRoot(root);
            currentStage.setTitle("Game");
        } catch (IOException e) {
            System.out.println(e.toString());
        }
    }

    @FXML
    public void logOut() {
        DataUtils.logOut();
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/userPage/cover.fxml"));
            Parent root = loader.load();
            CoverController coverController = loader.getController();
            coverController.setCurrentStage(currentStage);

            currentStage.getScene().setRoot(root);
            currentStage.setTitle("cover");
        } catch (IOException e) {
            System.out.println(e.toString());
        }
    }

    //------------------------------------------------------------------
    public void loadMapLabels() {
        mapContainer.getChildren().clear();
        Path mapFolder = Path.of("src/main/resources/maps");
        if (Files.exists(mapFolder) && Files.isDirectory(mapFolder)) {
            try (Stream<Path> pathStream = Files.list(mapFolder)) {
                var mapFiles = pathStream.toList();
                if (mapFiles.isEmpty()) {
                    System.out.println("暂无关卡文件");
                    tips.setText("暂无关卡文件");
                    Label noMapLabel = new Label("暂无关卡文件");
                    mapContainer.getChildren().addAll(noMapLabel);
                } else {
                    mapFiles.forEach(path -> {
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
        mapLabel.setPrefWidth(300);
        mapLabel.setPrefHeight(80);
        mapLabel.setStyle("-fx-font-size: 30px; -fx-cursor: hand");
        mapLabel.setFont(Settings.pixelFont);
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
            //展示游戏记录
            mapName.setText(mapPre.getName());
            if (userData.getPlayRecords().containsKey(mapPre.getName())) {
                MapRecord record = userData.getPlayRecords().get(mapPre.getName());
                stepCost.setText(String.format("Steps: %d", record.getRecordDeque().size()));
                timeCost.setText(String.format("Time: %s", record.getTime().toString()));
            } else {
                stepCost.setText("暂无游玩记录");
                timeCost.setText("");
            }
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

    @FXML
    public void returnOnMapChoose() {
        choosePane.setVisible(false);
        mainPane.setVisible(true);
    }

    @FXML
    ToggleButton timeLimit;

    public static Boolean isTimeLimit = false;

    @FXML
    public void handleToggleTimeLimit() {
        if (timeLimit.isSelected()) {
            System.out.println("Time limit ON");
            isTimeLimit = true;
            timeLimit.setVisible(true);
            timeLimitPane.setVisible(true);
            two.setVisible(true);
            five.setVisible(true);
            ten.setVisible(true);
            twenty.setVisible(true);

        } else {
            System.out.println("Time limit OFF");
            timeLimitPane.setVisible(false);
            two.setVisible(false);
            five.setVisible(false);
            ten.setVisible(false);
            twenty.setVisible(false);
        }
    }

    @FXML
    public void handleRandom() {
        MapModel randomMap = MapModel.getRandomMap();
        loadGamePage();
        gameRootPaneController.setChooseMap(randomMap);
        gameRootPaneController.initNewGamePage();
    }

    public static int limitTime;

    @FXML
    public void handleConfirm1() {
        limitTime = 120;
    }

    @FXML
    public void handleConfirm2() {
        limitTime = 300;
    }

    @FXML
    public void handleConfirm3() {
        limitTime = 600;
    }

    @FXML
    public void handleConfirm4() {
        limitTime = 1200;
    }


    //------------------------------------------------------------------------------------
    public void setCurrentStage(Stage currentStage) {
        this.currentStage = currentStage;
    }

    public void setCurrentLabel(Label currentLabel) {
        this.currentLabel = currentLabel;
    }

    public void setCurrentMap(MapPre currentMap) {
        this.currentMap = currentMap;
    }

    public static void toMainPage() {
        try {
            FXMLLoader loader = new FXMLLoader(MainPageController.class.getResource("/fxml/userPage/mainPage.fxml"));
            Parent root = loader.load();
            MainPageController mainPageController = loader.getController();
            mainPageController.setCurrentStage(Settings.currentStage);

            mainPageController.initMainPage();

            Settings.currentStage.getScene().setRoot(root);
            Settings.currentStage.setTitle(userData.getId());
        } catch (IOException e) {
            System.out.println("error\n" + e.toString());
        }
    }
}
