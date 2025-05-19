package com.lllllllhp.controller.userPage;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.lllllllhp.data.UserData;
import com.lllllllhp.utils.dataUtils.DataChecker;
import com.lllllllhp.utils.dataUtils.DataUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static com.lllllllhp.utils.dataUtils.DataUtils.userData;

public class LoginController {
    private Stage currentStage;

    @FXML
    TextField idField;
    @FXML
    TextField passWordField;
    @FXML
    Label warning;

    @FXML
    private void handleConfirm(ActionEvent actionEvent) {
        if (idField.getText().isEmpty() || passWordField.getText().isEmpty()) {
            warning.setText("Please enter your id and password.");
            System.out.println("Please enter your id and password.");
            return;
        }

        if (!check()) return;

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/userPage/mainPage.fxml"));
            Parent root = loader.load();
            MainPageController mainPageController = loader.getController();
            mainPageController.setCurrentStage(currentStage);

            mainPageController.initMainPage();

            currentStage.getScene().setRoot(root);
            currentStage.setTitle(userData.getId());
        } catch (IOException e) {
            System.out.println("error\n" + e.toString());
        }
    }

    public boolean check() {
        String id = idField.getText();
        String passWord = passWordField.getText();
        Path path = Paths.get("src/main/resources/User", id);
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        if (Files.exists(path) && Files.isDirectory(path)) {
            //已注册用户，检查密码
            path = path.resolve("userData.json");
            try {
                String inputData = Files.readString(path);
                UserData temp = gson.fromJson(inputData, UserData.class);

                //检验hash
                if (!DataChecker.checkData(temp, path)) {
                    warning.setText("Data is invalid!");
                    System.out.println("Data is invalid!");
                    return false;
                }
                //检验密码
                if (!temp.getPassWord().equals(passWord)) {
                    warning.setText("Please check your password.");
                    System.out.println("Wrong Password.");
                    return false;
                }
                //传入数据
                DataUtils.setUserData(temp);

                System.out.println("data is valid");
                return true;
            } catch (JsonSyntaxException e) {
                //文件损坏
                System.out.println("data corrupted.");
                System.out.println(e.toString());
                warning.setText("data corrupted.");
                return false;
            } catch (IOException e) {
                System.out.println("Reading error.");
                System.out.println(e.toString());
                warning.setText("Reading error.");
                return false;
            }
        } else {
            //未注册用户，注册新账户
            try {
                path = path.resolve("userData.json");
                Files.createDirectories(path.getParent());
                UserData temp = new UserData(id, passWord);
                String newData = gson.toJson(temp);
                Files.writeString(path, newData);
                //传入数据
                DataUtils.setUserData(temp);

                System.out.println("Welcome newcomer.");
                return true;
            } catch (IOException e) {
                warning.setText("Creating new account fails.");
                System.out.println("Creating new account fails.");
                System.out.println(e.toString());
                return false;
            }
        }
    }

    @FXML
    public void returnToCover() {
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

    //--------------------------------------------------------------------------------------
    public Stage getCurrentStage() {
        return currentStage;
    }

    public void setCurrentStage(Stage currentStage) {
        this.currentStage = currentStage;
    }
}
