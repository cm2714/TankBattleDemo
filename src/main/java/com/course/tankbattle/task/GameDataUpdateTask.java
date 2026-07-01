package com.course.tankbattle.task;

import com.course.tankbattle.context.GameContext;
import com.course.tankbattle.dto.RealTimeGameData;
import com.course.tankbattle.service.GameEventService;
import com.course.tankbattle.service.StateFlushService;
import com.course.tankbattle.util.GameTimeUnit;
import com.course.tankbattle.view.panel.GamePanel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GameDataUpdateTask implements Runnable {
    private static final Logger LOGGER = LoggerFactory.getLogger(GameDataUpdateTask.class);
    private final GameContext gameContext;

    public GameDataUpdateTask(GameContext gameContext) {
        this.gameContext = gameContext;
    }

    @Override
    public void run() {
        GamePanel panel = gameContext.getGamePanel();
        GameEventService gameEventService = gameContext.getGameEventService();
        StateFlushService stateFlushService = gameContext.getStateFlushService();
        while (!gameContext.getRealTimeGameData().isExit()) {
            GameTimeUnit.sleepMillis(30);
            RealTimeGameData gameData = gameContext.getRealTimeGameData();
            if (gameData.isStart()) {
                if (!gameData.isSettlement() && (gameData.getMyTankNum() == 0 || gameData.getEnemyTankNum() == 0)) {
                    gameEventService.finishLevel(gameData.getMyTankNum() > 0);
                }
                if (gameData.isSettlement()) {
                    if (gameData.isVictory() && System.currentTimeMillis() - gameData.getSettlementAt() > 3000) {
                        gameEventService.goNextAfterSettlement();
                    }
                } else if (!gameData.isStop()) {
                    stateFlushService.cleanAndCreate();
                    stateFlushService.refreshEnemyTankState();
                    stateFlushService.refreshBulletState();
                    stateFlushService.refreshOverlapState();
                    stateFlushService.refreshMyTankState(gameData);
                }
            } else {
                // Start screen animation - just repaint
                if (gameData.isStartScreen()) {
                    // Animate font.png sliding up
                    int fontY = gameData.getFontY();
                    if (fontY > 30) {
                        fontY -= 10;
                        if (fontY < 30) fontY = 30;
                        gameData.setFontY(fontY);
                    }
                    GameTimeUnit.sleepMillis(50);
                    panel.repaint();
                    continue;
                }
                // Legacy font animation for pre-game screen
                if (gameData.getKy() == 21 && gameData.getKx() <= 654) {
                    gameData.setKx(gameData.getKx() + 2);
                }
                gameEventService.fontDynamicMove(panel);
                GameTimeUnit.sleepMillis(60);
            }
            panel.repaint();
            LOGGER.debug("data : {}", gameData);
        }
    }
}
