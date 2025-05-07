package com.lllllllhp.controller.userPage;

import com.lllllllhp.data.UserData;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
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
    @FXML
    Button loginButton;
    @FXML
    Button guestButton;
    @FXML
    Label welcome;

    @FXML
    public void handleLogin() {
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
    public void handleGuest() {
        //临时账号，用于临时保存进度
        UserData guest = new UserData("Guest_", "0");
        guest.setMember(false);

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/userPage/mainPage.fxml"));
            Parent root = loader.load();
            MainPageController mainPageController = loader.getController();
            mainPageController.setCurrentStage(currentStage);
            mainPageController.setUserData(guest);

            mainPageController.initMainPage();

            currentStage.getScene().setRoot(root);
            currentStage.setTitle(guest.getId());
        } catch (IOException e) {
            System.out.println("error\n" + e.toString());
        }
    }//直接进入main page

    @FXML
    private void handleContinue(KeyEvent keyEvent) {
        loginButton.setVisible(true);
        guestButton.setVisible(true);
        welcome.setVisible(false);
    }

    public Stage getCurrentStage() {
        return currentStage;
    }

    public void setCurrentStage(Stage currentStage) {
        this.currentStage = currentStage;
    }
}