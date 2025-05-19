package com.lllllllhp;

import com.lllllllhp.controller.userPage.CoverController;
import com.lllllllhp.utils.audioPlayer.AudioPlayer;
import com.lllllllhp.utils.dataUtils.DataUtils;
import com.lllllllhp.utils.socket.NetUtils;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;

public class MainApp extends Application {
    @Override
    public void init() {
        AudioPlayer.init();
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/userPage/cover.fxml"));
        Parent root = loader.load();

        CoverController coverController = loader.getController();
        coverController.setCurrentStage(primaryStage);

        Scene scene = new Scene(root);
        primaryStage.setTitle("Cover");
        primaryStage.setScene(scene);
        primaryStage.setFullScreen(false);
        primaryStage.show();

        AudioPlayer.playBGM();
    }

    @Override
    public void stop() {
        //退出自动保存
        if (DataUtils.isMember) {
            DataUtils.userData.save();
        }

        //删除临时账号
        Path path = Path.of("src/main/resources/User/Guest_");
        if (Files.exists(path) && Files.isDirectory(path)) {
            try (Stream<Path> walk = Files.walk(path)) {
                walk.forEach(path1 -> {
                    try {
                        Files.delete(path1);
                    } catch (IOException e) {
                        System.out.println(e.toString());
                    }
                });
                Files.delete(path);
                System.out.println("delete guest");
            } catch (IOException e) {
                System.out.println("delete guest error");
                System.out.println(e.toString());
            }
        }

        //安全关闭net
        if (NetUtils.hasServer()) {
            NetUtils.endServer();
        }
        if (NetUtils.hasClient()) {
            NetUtils.endClient();
        }

        System.out.println("ByeBye~~");
    }

    public static void main(String[] args) {
        launch(args);
    }
}