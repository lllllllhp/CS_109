package com.lllllllhp.utils.settings;

import javafx.stage.Stage;

public class Settings {
    public static Stage currentStage;

    public static class Mode {
        public static boolean timeAttack;

    }




    public static Stage getCurrentStage() {
        return currentStage;
    }

    public static void setCurrentStage(Stage currentStage) {
        Settings.currentStage = currentStage;
    }
}
