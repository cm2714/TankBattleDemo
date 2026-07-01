package com.course.tankbattle.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sound.sampled.*;
import java.io.BufferedInputStream;
import java.io.InputStream;

public final class SoundUtils {
    private static final Logger LOGGER = LoggerFactory.getLogger(SoundUtils.class);

    private SoundUtils() {
    }

    public static void playStartup() {
        playSound("/static/sound/mainMenuLoad.wav");
    }

    public static void playStart() {
        playSound("/static/sound/start.wav");
    }

    public static void playSelect() {
        playSound("/static/sound/select.wav");
    }

    public static void playShoot() {
        playSound("/static/sound/normalFire.wav");
    }

    public static void playEnemyFire() {
        playSound("/static/sound/e_fire.mp3");
    }

    public static void playBomb() {
        playSound("/static/sound/normalBomb.wav");
    }

    public static void playRocketBomb() {
        playSound("/static/sound/rocketBomb.wav");
    }

    public static void playRocketFire() {
        playSound("/static/sound/rocketFire.wav");
    }

    public static void playGameOver() {
        playSound("/static/sound/GameOver.wav");
    }

    public static void playWin() {
        playSound("/static/sound/Win.wav");
    }

    public static void playItem() {
        playSound("/static/sound/item.wav");
    }

    public static void playTankMove() {
        playSound("/static/sound/tank_move.wav");
    }

    private static void playSound(String path) {
        new Thread(() -> {
            try {
                InputStream is = SoundUtils.class.getResourceAsStream(path);
                if (is == null) {
                    LOGGER.warn("Sound not found: {}", path);
                    return;
                }
                try (BufferedInputStream bis = new BufferedInputStream(is);
                     AudioInputStream ais = AudioSystem.getAudioInputStream(bis)) {
                    Clip clip = AudioSystem.getClip();
                    clip.open(ais);
                    clip.start();
                }
            } catch (Exception e) {
                LOGGER.debug("Sound play failed (non-critical): {}", e.getMessage());
            }
        }, "sound-thread").start();
    }
}
