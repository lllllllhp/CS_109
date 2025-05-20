package com.lllllllhp.utils.socket.messageModel;

import com.lllllllhp.controller.gamePage.GameRootPaneController;
import com.lllllllhp.data.MapRecord;
import com.lllllllhp.utils.socket.NetUtils;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

import java.io.IOException;
import java.time.LocalDateTime;

import static com.lllllllhp.utils.settings.Settings.currentStage;

public class MapInfo extends Message {
    MapRecord mapRecord;

    public MapInfo(MapRecord mapRecord, String sender, LocalDateTime dateTime) {
        super(sender, dateTime);
        this.mapRecord = mapRecord;
    }

    @Override
    public void show() {
        Platform.runLater(() -> {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/gamePage/gameRootPane.fxml"));
                Parent root = loader.load();
                GameRootPaneController gameRootPaneController = loader.getController();

                NetUtils.ClientData.spectatingMap = mapRecord;
                gameRootPaneController.initSpectatingPage();

                currentStage.getScene().setRoot(root);
                currentStage.setTitle(String.format("%s的游戏", sender));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }
}
