package com.lllllllhp.utils.settings;

import javafx.scene.text.Font;
import javafx.stage.Stage;

public class Settings {
    public static Stage currentStage;
    //init中初始化字体
    public static Font pixelFont;

    public static class Mode {
        public static boolean timeAttack;

    }

    public static void initFont() {
        pixelFont = Font.loadFont(Settings.class.getResourceAsStream("/styles/fonts/zpix/zpix.ttf"), 18);
    }

    public static Stage getCurrentStage() {
        return currentStage;
    }

    public static void setCurrentStage(Stage currentStage) {
        Settings.currentStage = currentStage;
    }
}
