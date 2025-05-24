package com.lllllllhp.utils;

import javafx.scene.image.Image;

import java.util.Objects;

public class ImageLoader {
    public static Image game2_2_left;
    public static Image game1_1;
    public static Image game1_2;
    public static Image game2_1;
    public static Image exit;
    public static Image obstacle;

    public static void initGameImage() {
        game2_2_left = new Image(Objects.requireNonNull(ImageLoader.class.getResourceAsStream("/image/boxImage/2_2_left.png")));
        game1_1 = new Image(Objects.requireNonNull(ImageLoader.class.getResourceAsStream("/image/boxImage/1_1.png")));
        game2_1 = new Image(Objects.requireNonNull(ImageLoader.class.getResourceAsStream("/image/boxImage/2_1.png")));
        game1_2 = new Image(Objects.requireNonNull(ImageLoader.class.getResourceAsStream("/image/boxImage/1_2.png")));
        exit = new Image(Objects.requireNonNull(ImageLoader.class.getResourceAsStream("/image/boxImage/exit.png")));
        obstacle = new Image(Objects.requireNonNull(ImageLoader.class.getResourceAsStream("/image/boxImage/obstacle.png")));
    }
}
