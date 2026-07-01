package com.course.tankbattle.resource.image;

import java.awt.Image;
import java.awt.Toolkit;


public class Images {
    /**
     * 我方坦克图片
     */
    public static final Image[] myTankImg = {
            getImage("/static/img/UTank_.gif"),
            getImage("/static/img/DTank_.gif"),
            getImage("/static/img/LTank_.gif"),
            getImage("/static/img/RTank_.gif")
    };

    /**
     * 敌方坦克图片
     */
    public static final Image[] enemyTankImg = {
            getImage("/static/img/UTank.gif"),
            getImage("/static/img/DTank.gif"),
            getImage("/static/img/LTank.gif"),
            getImage("/static/img/RTank.gif")
    };

    /**
     * 爆炸图片
     */
    public static final Image[] bomb = {
            getImage("/static/img/bomb_1.png"),
            getImage("/static/img/bomb_2.png"),
            getImage("/static/img/bomb_3.png"),
            getImage("/static/img/bomb_4.png"),
            getImage("/static/img/bomb_5.png")
    };

    /**
     * 子弹图片
     */
    public static final Image bullet = getImage("/static/img/bullet.gif");
    /**
     * 游戏开始背景图
     */
    public static final Image startImage = getImage("/static/img/gameStart.png");
    /**
     * 游戏失败
     */
    public static final Image gameOver = getImage("/static/img/gameOver.gif");
    /**
     * 游戏成功
     */
    public static final Image gameWin = getImage("/static/img/gameWin.gif");
    /**
     * 游戏开始动态文字
     */
    public static final Image font = getImage("/static/img/font.png");

    public static final Image brickBC = getImage("/static/img/brick_bc.gif");
    public static final Image ironBC = getImage("/static/img/iron_bc.gif");
    public static final Image waterBC = getImage("/static/img/water_bc.png");

    private static Image getImage(String name) {
        return Toolkit.getDefaultToolkit().getImage(Images.class.getResource(name));
    }
}
