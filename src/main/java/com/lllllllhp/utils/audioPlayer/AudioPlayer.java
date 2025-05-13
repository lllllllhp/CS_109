package com.lllllllhp.utils.audioPlayer;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.util.Objects;

public class AudioPlayer {
    public static String bgmPath = Objects.requireNonNull(AudioPlayer.class.getResource("/audio/bgm1.mp3")).toExternalForm();

    public static void playBGM() {
        Media bgmMedia = new Media(bgmPath);
        MediaPlayer bgmPlayer = new MediaPlayer(bgmMedia);

        bgmPlayer.setCycleCount(MediaPlayer.INDEFINITE);
        bgmPlayer.play();
    }
}
