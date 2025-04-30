package com.proj510;

import com.proj510.controller.userPage.CoverController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

public class MainApp extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/userPage/cover.fxml"));
        Parent root = loader.load();

        CoverController coverController = loader.getController();
        coverController.setCurrentStage(primaryStage);

        Scene scene = new Scene(root);
        primaryStage.setTitle("Cover");
        primaryStage.setScene(scene);
        primaryStage.setFullScreen(true);
        primaryStage.show();
    }

    @Override
    public void stop() {
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
    }//删除临时账号

    public static void main(String[] args) {
        launch(args);
    }
}