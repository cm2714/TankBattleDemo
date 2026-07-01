package com.course.tankbattle.task;

import com.course.tankbattle.entity.EnemyTank;
import com.course.tankbattle.service.TankControlService;

import java.util.TimerTask;

/**
 * 敌方坦克自动射击任务
 */
public class EnemyTankAutoShotTask extends TimerTask {
    private EnemyTank enemyTank;
    private TankControlService tankControlService;

    public EnemyTankAutoShotTask(EnemyTank enemyTank, TankControlService tankControlService) {
        this.enemyTank = enemyTank;
        this.tankControlService = tankControlService;
    }

    @Override
    public void run() {
        if (enemyTank.getSpeedVector() == 0 && enemyTank.isShot() && enemyTank.activate()) {
            tankControlService.shot(enemyTank);
        }
    }
}
