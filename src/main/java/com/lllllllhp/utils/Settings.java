package com.lllllllhp.utils;

import javafx.scene.text.Font;
import javafx.stage.Stage;

public class Settings {
    public static Stage currentStage;
    //init中初始化字体
    public static Font pixelFont;


    public static void initFont() {
        pixelFont = Font.loadFont(Settings.class.getResourceAsStream("/styles/fonts/zpix/zpix.ttf"), 18);
    }


    public static void setCurrentStage(Stage currentStage) {
        Settings.currentStage = currentStage;
    }
}
