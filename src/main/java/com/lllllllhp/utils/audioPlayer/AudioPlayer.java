package com.lllllllhp.utils.audioPlayer;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.util.Objects;

public class AudioPlayer {
    public static final String bgmPath = Objects.requireNonNull(AudioPlayer.class.getResource("/audio/bgm1.mp3")).toExternalForm();
    public static final String boxSoundFXPath = Objects.requireNonNull(AudioPlayer.class.getResource("/")).toExternalForm();
    public static Media bgmMedia;
    public static MediaPlayer bgmPlayer;

    public static void init() {
        bgmMedia = new Media(bgmPath);
        bgmPlayer = new MediaPlayer(bgmMedia);
        bgmPlayer.setCycleCount(MediaPlayer.INDEFINITE);
    }

    public static void playBGM() {
        bgmPlayer.play();
    }
}
