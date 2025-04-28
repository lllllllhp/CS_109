package com.proj510.controller.userPage;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.io.IOException;

public class CoverController {
    private Stage currentStage;

    @FXML
    AnchorPane coverPane;
    @FXML
    StackPane rootPane;

    public Stage getCurrentStage() {
        return currentStage;
    }

    public void setCurrentStage(Stage currentStage) {
        this.currentStage = currentStage;
    }

    @FXML
    private void handleKeyPress(KeyEvent keyEvent){

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/userPage/login.fxml"));
            Parent root = loader.load();
            LoginController loginController = loader.getController();
            loginController.setCurrentStage(currentStage);

            currentStage.getScene().setRoot(root);
            currentStage.setTitle("Login");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
