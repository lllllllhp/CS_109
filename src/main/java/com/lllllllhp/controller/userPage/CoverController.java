package com.lllllllhp.controller.userPage;

import com.lllllllhp.data.UserData;
import com.lllllllhp.utils.dataUtils.DataUtils;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
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
            //直接生成验证码
            loginController.refresh();

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

        DataUtils.setUserData(guest);
        DataUtils.isMember = false;

        MainPageController.toMainPage();
    }//直接进入main page

    @FXML
    private void handleContinue(MouseEvent mouseEvent) {
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