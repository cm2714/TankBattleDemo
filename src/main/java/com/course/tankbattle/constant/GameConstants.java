

package com.course.tankbattle.constant;


public class GameConstants {
    /**
     * 坦克宽度
     */
    public final static int TANK_WIDTH = 40;
    /**
     * 坦克高度
     */
    public final static int TANK_HEIGHT = 40;
    /**
     * 游戏面板宽度
     */
    public final static int GAME_PANEL_WIDTH = 600;
    /**
     * 游戏面板高度
     */
    public final static int GAME_PANEL_HEIGHT = 600;
    /**
     * 我方坦克子弹初始数量
     */
    public static final int MY_TANK_INIT_BULLET_NUM = 500;
    /**
     * 地图中初始敌方坦克数量
     */
    public final static int INIT_ENEMY_TANK_IN_MAP_NUM = 5;
    /**
     * 地图中初始我方坦克数量
     */
    public final static int INIT_MY_TANK_IN_MAP_NUM = 1;
    /**
     * 敌方坦克总数
     */
    public final static int INIT_ENEMY_TANK_NUM = 8;
    /**
     * 我方坦克总数
     */
    public final static int INIT_MY_TANK_NUM = 4;
    /**
     * 我方坦克子弹同时存在地图中的数目
     */
    
    public final static int MY_TANK_BULLET_LIVE_NUM = 3;

    /**
     * 我方坦克出生点无敌区域Y坐标阈值，高于此值为在家无敌状态
     */
    public final static int HOME_Y_THRESHOLD = GAME_PANEL_HEIGHT - TANK_HEIGHT / 2;
}

