package com.lllllllhp.utils.audioPlayer;

import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class AudioPlayer {
    private static final String bgmPath = Objects.requireNonNull(AudioPlayer.class.getResource("/audio/bgm1.mp3")).toExternalForm();
    private static final String boxSoundFXPath = Objects.requireNonNull(AudioPlayer.class.getResource("/")).toExternalForm();

    private static Media bgmMedia;
    private static MediaPlayer bgmPlayer;

    private static final Map<SFXType, AudioClip> simpleSFXMap = new HashMap<>();
    private static final Map<SFXType, AudioClip> gameSFXMap = new HashMap<>();

    private static double bgmVolume = 1.0;
    private static double sfxVolume = 1.0;

    public enum SFXType {
        CLICK,
    }

    public static void init() {
        bgmMedia = new Media(bgmPath);
        bgmPlayer = new MediaPlayer(bgmMedia);
        bgmPlayer.setCycleCount(MediaPlayer.INDEFINITE);


    }

    public static void playBGM() {
        if (bgmPlayer != null) {
            bgmPlayer.play();
        }
    }

    public void stopBGM() {
        if (bgmPlayer != null) {
            bgmPlayer.stop();
            bgmPlayer.dispose();
            bgmPlayer = null;
        }
    }

    public void pauseBGM() {
        if (bgmPlayer != null) {
            bgmPlayer.pause();
        }
    }

    public void resumeBGM() {
        if (bgmPlayer != null) {
            bgmPlayer.play();
        }
    }

    public static void playSFX() {

    }

    public static double getSfxVolume() {
        return sfxVolume;
    }

    public static void setSfxVolume(double sfxVolume) {
        AudioPlayer.sfxVolume = sfxVolume;
    }

    public static double getBgmVolume() {
        return bgmVolume;
    }

    public static void setBgmVolume(double bgmVolume) {
        AudioPlayer.bgmVolume = bgmVolume;
    }
}
