

package com.course.tankbattle.listener;

import com.course.tankbattle.constant.GameConstants;
import com.course.tankbattle.context.GameContext;
import com.course.tankbattle.dto.RealTimeGameData;
import com.course.tankbattle.entity.MyTank;
import com.course.tankbattle.enums.DirectionEnum;
import com.course.tankbattle.enums.StuffTypeEnum;
import com.course.tankbattle.service.GameEventService;
import com.course.tankbattle.service.TankControlService;
import com.course.tankbattle.service.MenuActionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.course.tankbattle.util.SoundUtils;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;


@Component
public class KeyEventListener implements KeyListener {
    @Autowired
    private TankControlService tankControlService;
    @Autowired
    private GameEventService gameEventService;
    @Autowired
    private GameContext gameContext;
    @Autowired
    private MenuActionService menuActionService;

    /**
     * 按键按下操作
     *
     * @param e 事件
     */
    @Override
    public void keyPressed(KeyEvent e) {
        RealTimeGameData gameData = gameContext.getRealTimeGameData();

        // Global shortcuts for start screen / settlement / ranking
        if (!gameData.isStart()) {
            // Start screen menu navigation
            if (gameData.isStartScreen()) {
                int sel = gameData.getStartMenuSelection();
                if (e.getKeyCode() == KeyEvent.VK_UP) {
                    gameData.setStartMenuSelection(Math.max(0, sel - 1));
                    SoundUtils.playSelect();
                    return;
                }
                if (e.getKeyCode() == KeyEvent.VK_DOWN) {
                    gameData.setStartMenuSelection(Math.min(1, sel + 1));
                    SoundUtils.playSelect();
                    return;
                }
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    SoundUtils.playSelect();
                    if (sel == 0) {
                        gameContext.startGame();
                    } else {
                        menuActionService.about();
                    }
                    return;
                }
            }

            if (e.getKeyCode() == KeyEvent.VK_R) {
                gameData.setShowRanking(!gameData.isShowRanking());
                return;
            }
            if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                if (gameData.isShowRanking()) {
                    gameData.setShowRanking(false);
                }
                return;
            }
            return;
        }

        // Settlement screen shortcuts
        if (gameData.isSettlement()) {
            if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                if (gameData.isVictory()) {
                    gameEventService.goNextAfterSettlement();
                } else {
                    gameContext.showStartScreen();
                }
                return;
            }
            if (e.getKeyCode() == KeyEvent.VK_R && !gameData.isVictory()) {
                SoundUtils.playSelect();
                gameContext.startGame();
                return;
            }
            return;
        }

        // Show ranking during game
        if (e.getKeyCode() == KeyEvent.VK_R) {
            gameData.setShowRanking(!gameData.isShowRanking());
            return;
        }

        //地图编辑模式，按键C操作
        if (gameData.getMapMakingMode() && e.getKeyCode() == KeyEvent.VK_C) {
            switch (gameData.getCurrentSelectedStuff()) {
                case BRICK:
                    gameData.setCurrentSelectedStuff(StuffTypeEnum.IRON);
                    break;
                case IRON:
                    gameData.setCurrentSelectedStuff(StuffTypeEnum.WATER);
                    break;
                case WATER:
                    gameData.setCurrentSelectedStuff(StuffTypeEnum.INVALID);
                    break;
                default:
                    gameData.setCurrentSelectedStuff(StuffTypeEnum.BRICK);
                    break;
            }
            return;
        }


        // 暂停游戏
        if (e.getKeyCode() == KeyEvent.VK_P) {
            gameEventService.pauseOrResume(gameData);
            return;
        }

        //我方坦克按键处理
        gameData.getMyTanks().forEach(myTank -> processMyTank(myTank, gameData, e));
    }

    /**
     * 处理我方坦克按键按下操作
     *
     * @param myTank 我方坦克
     * @param data   实时数据
     * @param e      事件
     */
    private void processMyTank(MyTank myTank, RealTimeGameData data, KeyEvent e) {
        //坦克已死亡
        if (!myTank.getLive()) {
            data.keyPressedDirect(false, false, false, false);
            return;
        }

        //向上按键
        if ((e.getKeyCode() == KeyEvent.VK_UP)) {
            myTank.setDirect(DirectionEnum.NORTH);
            data.keyPressedDirect(true, false, false, false);
            return;
        }

        //坦克处于地图外面(default, 600*600)
        if (myTank.getY() > GameConstants.HOME_Y_THRESHOLD) {
            return;
        }

        switch (e.getKeyCode()) {
            case KeyEvent.VK_DOWN:
                myTank.setDirect(DirectionEnum.SOUTH);
                data.keyPressedDirect(false, true, false, false);
                break;
            case KeyEvent.VK_LEFT:
                myTank.setDirect(DirectionEnum.WEST);
                data.keyPressedDirect(false, false, true, false);
                break;
            case KeyEvent.VK_RIGHT:
                myTank.setDirect(DirectionEnum.EAST);
                data.keyPressedDirect(false, false, false, true);
                break;
            case KeyEvent.VK_X:
                //开火射击
                if (myTank.getBullets().size() < GameConstants.MY_TANK_BULLET_LIVE_NUM && data.getMyBulletNum() > 0) {
                    data.setMyBulletNum(data.getMyBulletNum() - 1);
                    tankControlService.shot(myTank);
                }
                break;
            default:
                break;
        }
    }

    /**
     * 释放按键操作
     *
     * @param e 按键事件
     */
    @Override
    public void keyReleased(KeyEvent e) {
        RealTimeGameData data = gameContext.getRealTimeGameData();
        switch (e.getKeyCode()) {
            case KeyEvent.VK_UP:
                data.setUp(false);
                break;
            case KeyEvent.VK_DOWN:
                data.setDown(false);
                break;
            case KeyEvent.VK_LEFT:
                data.setLeft(false);
                break;
            case KeyEvent.VK_RIGHT:
                data.setRight(false);
                break;
            default:
                break;
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }
}

