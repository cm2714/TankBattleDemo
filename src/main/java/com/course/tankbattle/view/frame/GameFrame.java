package com.course.tankbattle.view.frame;

import com.course.tankbattle.resource.image.Images;

import javax.swing.JFrame;
import javax.swing.WindowConstants;
import java.awt.Dimension;
import java.awt.Toolkit;

public class GameFrame extends JFrame {
    private static final long serialVersionUID = -1176914786963603304L;

    public GameFrame() {
        setSize(800, 700);
        setTitle("Tank Battle Course Edition");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);
        setIconImage(Images.myTankImg[0]);

        Dimension screenSizeInfo = Toolkit.getDefaultToolkit().getScreenSize();
        int leftTopX = ((int) screenSizeInfo.getWidth() - getWidth()) / 2;
        int leftTopY = ((int) screenSizeInfo.getHeight() - getHeight()) / 2;
        setLocation(leftTopX, leftTopY);
    }
}
