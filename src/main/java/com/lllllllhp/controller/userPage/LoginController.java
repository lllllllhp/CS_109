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
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static com.lllllllhp.utils.Settings.currentStage;
import static com.lllllllhp.utils.dataUtils.DataUtils.userData;

public class LoginController {
    String currentCaptcha;

    @FXML
    TextField idField;
    @FXML
    TextField passWordField;
    @FXML
    Label warning;
    @FXML
    Button Captcha;
    @FXML
    TextField captchaField;

    @FXML
    public void refresh() {
        StringBuilder captcha = new StringBuilder();
        Random random = new Random();
        // 生成指定长度的随机数字和字母组合
        for (int i = 0; i < 5; i++) {
            int charType = random.nextInt(3);
            switch (charType) {
                case 0: // 数字
                    captcha.append(random.nextInt(10));
                    break;
                case 1: // 大写字母
                    captcha.append((char) (random.nextInt(26) + 65));
                    break;
                case 2: // 小写字母
                    captcha.append((char) (random.nextInt(26) + 97));
                    break;
            }
        }

        currentCaptcha = captcha.toString();

        // 设置按钮文本为验证码
        Captcha.setText(currentCaptcha);

        // 添加视觉干扰 - 随机颜色
        Color randomColor = Color.rgb(
                random.nextInt(128),
                random.nextInt(128),
                random.nextInt(128)
        );

        // 添加视觉干扰 - 随机旋转
        Captcha.setRotate(random.nextInt(11) - 5); // -5到5度之间的随机旋转

        // 更新按钮样式
        Captcha.setStyle(String.format(
                "-fx-background-color: #4a86e8; -fx-text-fill: rgb(%d,%d,%d); -fx-font-size: 20px;",
                (int) (randomColor.getRed() * 255),
                (int) (randomColor.getGreen() * 255),
                (int) (randomColor.getBlue() * 255)
        ));
    }

    public boolean checkCaptcha() {
        String captchaCode = captchaField.getText().toUpperCase();
        return captchaCode.equals(currentCaptcha.toUpperCase());
    }

    //监测账号是否注册
    @FXML
    public void accountListener() {
        List<String> subdirectoryNames = new ArrayList<>();
        Path dirPath = Paths.get("src/main/resources/User");
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(dirPath)) {
            for (Path entry : stream) {
                if (Files.isDirectory(entry)) {
                    subdirectoryNames.add(entry.getFileName().toString());
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        idField.textProperty().addListener((observableValue, oldvalue, newvalue) -> {
            if (subdirectoryNames.contains(newvalue)) {
                System.out.println("Account is registered.");
                warning.setTextFill(Color.BLACK);
                warning.setText("Account is registered.");
            } else {
                System.out.println("Account is not registered.");
                warning.setTextFill(Color.RED);
                warning.setText("Account is not registered");
            }
        });
    }


    @FXML
    private void handleConfirm(ActionEvent actionEvent) {
        if (idField.getText().isEmpty() || passWordField.getText().isEmpty()) {
            warning.setTextFill(Color.RED);
            warning.setText("Please enter your id and password.");
            System.out.println("Please enter your id and password.");
            return;
        }
        if (!checkCaptcha()) {
            System.out.println("captcha wrong");
            warning.setTextFill(Color.RED);
            warning.setText("captcha wrong");
            return;
        }
        if (!check()) return;

        MainPageController.toMainPage();
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
                    warning.setTextFill(Color.RED);
                    warning.setText("Data is invalid!");
                    System.out.println("Data is invalid!");
                    return false;
                }
                //检验密码
                if (!temp.getPassWord().equals(passWord)) {
                    warning.setTextFill(Color.RED);
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
                warning.setTextFill(Color.RED);
                warning.setText("data corrupted.");
                return false;
            } catch (IOException e) {
                System.out.println("Reading error.");
                System.out.println(e.toString());
                warning.setTextFill(Color.RED);
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

    public void initialize() {
        accountListener();
    }
}
