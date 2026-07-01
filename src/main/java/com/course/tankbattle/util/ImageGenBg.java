package com.course.tankbattle.util;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;

public final class ImageGenBg {
    private ImageGenBg() {}

    public static void generateStartBg(String outputPath) throws Exception {
        int w = 800, h = 700;
        BufferedImage img = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = img.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        GradientPaint gp = new GradientPaint(0, 0, new Color(25, 25, 38), 0, h, new Color(8, 8, 16));
        g.setPaint(gp);
        g.fillRect(0, 0, w, h);

        for (int i = 0; i < 60; i++) {
            int sx = (int)(Math.random() * w);
            int sy = (int)(Math.random() * 120);
            int b = 150 + (int)(Math.random() * 105);
            g.setColor(new Color(b, b, b, 150));
            g.fillRect(sx, sy, 2, 2);
        }

        drawTank(g, 120, 90, 2.0, new Color(80, 90, 110));
        drawTank(g, 680, 120, 2.0, new Color(80, 90, 110));
        drawTank(g, 90, 380, 1.5, new Color(60, 70, 90));
        drawTank(g, 720, 460, 1.5, new Color(60, 70, 90));
        drawTank(g, 400, 60, 1.8, new Color(65, 80, 100));
        drawTank(g, 140, 550, 1.5, new Color(50, 60, 80));
        drawTank(g, 660, 570, 1.5, new Color(50, 60, 80));

        for (int row = 0; row < 6; row++) {
            for (int col = 0; col < 25; col++) {
                int bx = 20 + col * 16;
                int by = 650 + row * 6;
                g.setColor(new Color(110, 45, 25));
                int shift = (row % 2) * 8;
                g.fillRect(bx + shift, by, 14, 4);
            }
        }

        g.dispose();
        ImageIO.write(img, "png", new File(outputPath));
    }

    static void drawTank(Graphics2D g, int cx, int cy, double scale, Color color) {
        int s = (int)scale;
        g.setColor(color);
        g.fillRect(cx - 10*s, cy - 8*s, 20*s, 14*s);
        g.setColor(new Color(Math.min(255, color.getRed()+50), Math.min(255, color.getGreen()+50), Math.min(255, color.getBlue()+50)));
        g.fillRect(cx - 4*s, cy - 12*s, 8*s, 10*s);
        g.setColor(new Color(Math.min(255, color.getRed()+80), Math.min(255, color.getGreen()+80), Math.min(255, color.getBlue()+80)));
        g.fillRect(cx - 1*s, cy - 22*s, 3*s, 12*s);
    }
}
