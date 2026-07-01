package com.course.tankbattle.service;

import com.course.tankbattle.constant.GameConstants;
import com.course.tankbattle.context.GameContext;
import com.course.tankbattle.dto.RealTimeGameData;
import com.course.tankbattle.dto.ScoreRecord;
import com.course.tankbattle.entity.EnemyTank;
import com.course.tankbattle.entity.MyTank;
import java.util.Random;
import com.course.tankbattle.enums.DirectionEnum;
import com.course.tankbattle.enums.LevelEnum;
import com.course.tankbattle.util.GameTimeUnit;
import com.course.tankbattle.view.panel.GamePanel;
import com.course.tankbattle.util.SoundUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GameEventService {
    @Autowired
    private GameContext context;
    @Autowired
    private TankControlService tankControlService;
    @Autowired
    private ScoreService scoreService;

    public void nextGame(RealTimeGameData data) {
        data.clearRuntimeObjects();
        data.setMap(data.getLevel().getMap());
        initEnemies(data, true);
        for (int i = 0; i < data.getMyTanks().size(); i++) {
            MyTank myTank = data.getMyTanks().get(i);
            myTank.setActivate(Boolean.TRUE);
            myTank.setX(GameConstants.GAME_PANEL_WIDTH / 2);
            myTank.setY(GameConstants.GAME_PANEL_HEIGHT + GameConstants.TANK_HEIGHT / 2);
            myTank.setLive(Boolean.TRUE);
            myTank.setBlood(10);
        }
        int ec = data.getLevel().getValue();
        data.setEnemyTankNum(ec <= 1 ? 8 : ec == 2 ? 10 : ec == 3 ? 12 : ec == 4 ? 15 : 20);
        data.prepareLevel();
        tankControlService.enableEnemyTanks();
    }

    public void finishLevel(boolean victory) {
        RealTimeGameData data = context.getRealTimeGameData();
        if (data.isSettlement()) {
            return;
        }
        data.setSettlement(true);
        if (victory) {
            SoundUtils.playWin();
        } else {
            SoundUtils.playGameOver();
        }
        data.setVictory(victory);
        data.setSettlementAt(System.currentTimeMillis());
        ScoreRecord record = scoreService.buildRecord(data, victory);
        data.setLastScoreRecord(record);
        scoreService.save(record);
    }

    public void goNextAfterSettlement() {
        RealTimeGameData data = context.getRealTimeGameData();
        if (!data.isVictory()) {
            return;
        }
        LevelEnum nextLevel = LevelEnum.nextLevel(data.getLevel());
        data.setLevel(nextLevel);
        nextGame(data);
    }

    public void exitGame() {
        context.clean();
        System.exit(0);
    }

    public void pauseOrResume(RealTimeGameData gameData) {
        if (!gameData.isStart() || gameData.isSettlement()) {
            return;
        }
        boolean pause = !gameData.isStop();
        gameData.setStop(pause);
        gameData.getMyTanks().forEach(myTank -> setTankSpeedPaused(myTank, pause));
        gameData.getEnemies().forEach(enemyTank -> setTankSpeedPaused(enemyTank, pause));
    }

    public void fontDynamicMove(GamePanel panel) {
        RealTimeGameData data = context.getRealTimeGameData();
        int ky = data.getKy();
        if (ky > 0 && ky != 21) {
            ky = ky - 8;
        }
        if (ky == 0) {
            for (int i = 0; i < 5; i++) {
                ky = ky + 7;
                panel.repaint();
                GameTimeUnit.sleepMillis(40);
            }
            ky = 21;
        }
        data.setKy(ky);
    }

    private void setTankSpeedPaused(com.course.tankbattle.entity.Tank tank, boolean pause) {
        if (pause) {
            tank.setSpeedVector(tank.getSpeed());
            tank.setSpeed(0);
            tank.getBullets().forEach(b -> {
                b.setSpeedVector(b.getSpeed());
                b.setSpeed(0);
            });
        } else {
            tank.setSpeed(tank.getSpeedVector() == 0 ? 4 : tank.getSpeedVector());
            tank.setSpeedVector(0);
            tank.getBullets().forEach(b -> b.setSpeed(b.getSpeedVector() == 0 ? 6 : b.getSpeedVector()));
        }
    }

            private void initEnemies(RealTimeGameData data, boolean active) {
        int fullSpan = GameConstants.GAME_PANEL_WIDTH - GameConstants.TANK_WIDTH;
        int xStep = fullSpan / (GameConstants.INIT_ENEMY_TANK_IN_MAP_NUM - 1);
        Random rand = new Random();
        for (int i = 0; i < GameConstants.INIT_ENEMY_TANK_IN_MAP_NUM; i++) {
            int x = i * xStep + GameConstants.TANK_WIDTH / 2 + rand.nextInt(21) - 10;
            if (x < 20) x = 20;
            if (x > GameConstants.GAME_PANEL_WIDTH - 20) x = GameConstants.GAME_PANEL_WIDTH - 20;
            EnemyTank enemy = new EnemyTank(x, -GameConstants.TANK_HEIGHT / 2, DirectionEnum.SOUTH);
            enemy.setLocation(i);
            enemy.setActivate(active);
            data.getEnemies().add(enemy);
        }
    }
}

