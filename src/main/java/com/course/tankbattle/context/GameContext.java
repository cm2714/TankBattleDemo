

package com.course.tankbattle.context;

import com.course.tankbattle.constant.GameConstants;
import com.course.tankbattle.dto.RealTimeGameData;
import com.course.tankbattle.entity.EnemyTank;
import com.course.tankbattle.entity.MyTank;
import com.course.tankbattle.enums.DirectionEnum;
import com.course.tankbattle.enums.LevelEnum;
import com.course.tankbattle.listener.KeyEventListener;
import com.course.tankbattle.listener.MenuActionEventListener;
import com.course.tankbattle.listener.MouseEventListener;
import com.course.tankbattle.service.*;
import com.course.tankbattle.task.GameDataUpdateTask;
import com.course.tankbattle.view.frame.GameFrame;
import com.course.tankbattle.view.menubar.TankBattleMenuBar;
import com.course.tankbattle.view.panel.GamePanel;
import com.course.tankbattle.util.GameFont;
import javax.swing.plaf.FontUIResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Random;


@Component
public class GameContext {
    private Logger logger = LoggerFactory.getLogger(GameContext.class);
    /**
     * 游戏Frame
     */
    private GameFrame gameFrame;
    /**
     * 游戏画板
     */
    private GamePanel gamePanel;
    /**
     * 窗口菜单
     */
    private TankBattleMenuBar tankBattleMenuBar;
    /**
     * 游戏实时数据
     */
    private RealTimeGameData realTimeGameData;

    @Autowired
    private KeyEventListener keyEventListener;
    @Autowired
    private MenuActionEventListener menuActionEventListener;
    @Autowired
    private MouseEventListener mouseEventListener;


    @Autowired
    private GameEventService gameEventService;
    @Autowired
    private PaintService paintService;
    @Autowired
    private ThreadPoolTaskExecutor taskExecutor;
    @Autowired
    private TankControlService tankControlService;
    @Autowired
    private StateFlushService stateFlushService;
    @Autowired
    private ComputingService computingService;

    @EventListener
    public void init(ApplicationReadyEvent applicationReadyEvent) {
        logger.info("Application Started..., applicationReadyEvent = {}", applicationReadyEvent);
        String lookAndFeel = "javax.swing.plaf.nimbus.NimbusLookAndFeel";
        try {
            UIManager.setLookAndFeel(lookAndFeel);
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {
            logger.warn("Set look and feel failed", e);
        }

        // Override UI fonts to use pixel font (prevent 宋体 on Chinese Windows)
        FontUIResource pixelUI = new FontUIResource(GameFont.getFont(14f));
        UIManager.put("Menu.font", pixelUI);
        UIManager.put("MenuItem.font", pixelUI);
        UIManager.put("OptionPane.messageFont", pixelUI);
        UIManager.put("OptionPane.buttonFont", pixelUI);
        UIManager.put("Label.font", pixelUI);
        UIManager.put("Panel.font", pixelUI);

        //初始化第一关
        initLevelData(LevelEnum.FIRST_LEVEL);

        this.gamePanel = new GamePanel(paintService, mouseEventListener);
        tankBattleMenuBar = new TankBattleMenuBar(menuActionEventListener);
        tankBattleMenuBar.updateLanguage(true);

        this.gameFrame = new GameFrame();
        this.gameFrame.setJMenuBar(tankBattleMenuBar);
        this.gameFrame.add(this.gamePanel);
        this.gameFrame.addKeyListener(keyEventListener);
        this.gameFrame.setVisible(true);
        this.gameFrame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent windowEvent) {
                gameEventService.exitGame();
            }
        });

        logger.info("execute UpdateTask...");
        taskExecutor.execute(new GameDataUpdateTask(this));
        logger.info("game start success...");

        // Play startup sound
        com.course.tankbattle.util.SoundUtils.playStartup();

        // Show start screen initially
        RealTimeGameData initialData = this.realTimeGameData;
        if (initialData != null) {
            initialData.setStartScreen(true);
            initialData.setStart(false);
        }
    }

    public void showStartScreen() {
        backHome();
        realTimeGameData.setStartScreen(true);
        realTimeGameData.setStart(false);
        realTimeGameData.setShowRanking(false);
        realTimeGameData.setStartMenuSelection(0);
    }

    /**
     * 初始化指定关卡游戏数据
     *
     * @param level 关卡
     */
    private void initLevelData(LevelEnum level) {
        logger.info("init Game Data start...");
        //初始化实时数据
        realTimeGameData = new RealTimeGameData();

                                //初始化敌方坦克 — 全幅散开从底线入场
        int fullSpan = GameConstants.GAME_PANEL_WIDTH - GameConstants.TANK_WIDTH;
        int xStep = fullSpan / (GameConstants.INIT_ENEMY_TANK_IN_MAP_NUM - 1);
        Random rand = new Random();
        for (int i = 0; i < GameConstants.INIT_ENEMY_TANK_IN_MAP_NUM; i++) {
            int x = i * xStep + GameConstants.TANK_WIDTH / 2 + rand.nextInt(21) - 10;
            if (x < 20) x = 20;
            if (x > GameConstants.GAME_PANEL_WIDTH - 20) x = GameConstants.GAME_PANEL_WIDTH - 20;
            EnemyTank enemy = new EnemyTank(x, -GameConstants.TANK_HEIGHT / 2, DirectionEnum.SOUTH);
            enemy.setLocation(i);
            realTimeGameData.getEnemies().add(enemy);
        }

        //初始化我方坦克
        for (int i = 0; i < GameConstants.INIT_MY_TANK_IN_MAP_NUM; i++) {
            MyTank myTank = new MyTank(GameConstants.GAME_PANEL_WIDTH / 2, GameConstants.GAME_PANEL_HEIGHT + GameConstants.TANK_HEIGHT / 2, DirectionEnum.NORTH);
            realTimeGameData.getMyTanks().add(myTank);
        }

        realTimeGameData.setMap(level.getMap());
        int lv = level.getValue();
        realTimeGameData.setEnemyTankNum(lv <= 1 ? 8 : lv == 2 ? 10 : lv == 3 ? 12 : lv == 4 ? 15 : 20);
        realTimeGameData.setMyTankNum(GameConstants.INIT_MY_TANK_NUM);
        realTimeGameData.setMyBulletNum(GameConstants.MY_TANK_INIT_BULLET_NUM);
        realTimeGameData.setBeKilled(0);
        realTimeGameData.setLevel(level);
        realTimeGameData.prepareLevel();
        logger.info("init Game Data end...");
    }

    /**
     * 开始游戏
     */
    public void startGame() {
        realTimeGameData.setStart(Boolean.TRUE);
        realTimeGameData.setStartScreen(false);
        realTimeGameData.setShowRanking(false);
        realTimeGameData.setStartMenuSelection(0);
        realTimeGameData.prepareLevel();
        realTimeGameData.getEnemies().forEach(t -> t.setActivate(Boolean.TRUE));
        realTimeGameData.getMyTanks().forEach(t -> t.setActivate(Boolean.TRUE));
        tankControlService.enableEnemyTanks();

        // Play level start sound
        com.course.tankbattle.util.SoundUtils.playStart();
    }

    public void backHome() {
        realTimeGameData.clear();
        initLevelData(LevelEnum.FIRST_LEVEL);
    }

    /**
     * 从某个关卡开始游戏
     *
     * @param level 关卡
     */
    public void startLevel(LevelEnum level) {
        realTimeGameData.clear();
        initLevelData(level);
        startGame();
    }

    public void clean() {
        realTimeGameData.setExit(true);
        realTimeGameData.clear();
    }

    public TankBattleMenuBar getTankBattleMenuBar() {
        return tankBattleMenuBar;
    }

    public MenuActionEventListener getMenuActionEventListener() {
        return menuActionEventListener;
    }

    public GamePanel getGamePanel() {
        return gamePanel;
    }

    public RealTimeGameData getRealTimeGameData() {
        return realTimeGameData;
    }

    public GameEventService getGameEventService() {
        return gameEventService;
    }

    public StateFlushService getStateFlushService() {
        return stateFlushService;
    }

    public TankControlService getTankControlService() {
        return tankControlService;
    }

    public ComputingService getComputingService() {
        return computingService;
    }
}

