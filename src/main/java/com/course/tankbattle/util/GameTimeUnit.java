package com.course.tankbattle.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class GameTimeUnit {
    private static final Logger LOGGER = LoggerFactory.getLogger(GameTimeUnit.class);

    private GameTimeUnit() {
    }

    public static void sleepMillis(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            LOGGER.warn("Game thread interrupted", e);
        }
    }
}
