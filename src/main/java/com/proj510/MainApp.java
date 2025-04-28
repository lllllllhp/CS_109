package com.proj510;

import com.proj510.controller.userPage.CoverController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

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

    public static void main(String[] args) {
        launch(args);
    }
}
