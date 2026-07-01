package com.course.tankbattle.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.io.InputStream;

public final class GameFont {
    private static final Logger LOGGER = LoggerFactory.getLogger(GameFont.class);
    private static Font pixelFont;

    private GameFont() {
    }

    public static Font getFont(float size) {
        if (pixelFont == null) {
            loadFont();
        }
        return pixelFont.deriveFont(size);
    }

    private static void loadFont() {
        // Try to load fusion-pixel font from resources
        try (InputStream is = GameFont.class.getResourceAsStream("/static/font/fusion-pixel.ttf")) {
            if (is != null) {
                Font font = Font.createFont(Font.TRUETYPE_FONT, is);
                GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
                ge.registerFont(font);
                pixelFont = font;
                LOGGER.info("Loaded fusion-pixel font");
                return;
            }
        } catch (Exception e) {
            LOGGER.debug("Fusion-pixel font not found, using fallback");
        }
        // Fallback to Monospaced
        // Fallback to Courier New (available everywhere, pixel-like)
        pixelFont = new Font("Courier New", Font.PLAIN, 1);
        LOGGER.info("Using Courier New fallback font");
    }
}

