package com.course.tankbattle.service;

import com.course.tankbattle.context.GameContext;
import com.course.tankbattle.entity.Bullet;
import com.course.tankbattle.entity.EnemyTank;
import com.course.tankbattle.entity.Iron;
import com.course.tankbattle.entity.MyTank;
import com.course.tankbattle.entity.Tank;
import com.course.tankbattle.enums.DirectionEnum;
import com.course.tankbattle.resource.map.Map;
import com.course.tankbattle.task.BulletMoveTask;
import com.course.tankbattle.task.EnemyTankAutoShotTask;
import com.course.tankbattle.task.EnemyTankMoveTask;
import com.course.tankbattle.util.GameTimeUnit;
import com.course.tankbattle.util.SoundUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import java.util.Vector;

/**
 * 坦克控制服务
 */
@Service
public class TankControlService {
    @Autowired
    private GameContext gameContext;
    @Autowired
    private ThreadPoolTaskExecutor taskExecutor;

    /**
     * 激活敌方坦克线程
     */
    public void enableEnemyTanks() {
        Vector<EnemyTank> enemyTanks = gameContext.getRealTimeGameData().getEnemies();
        enemyTanks.forEach(enemyTank -> {
            taskExecutor.execute(new EnemyTankMoveTask(enemyTank, gameContext));
            enemyTank.getTimer().schedule(new EnemyTankAutoShotTask(enemyTank, this), 0, 500);
        });
    }

    /**
     * 激活敌方坦克线程
     */
    public void enableEnemyTank(EnemyTank tank) {
        taskExecutor.execute(new EnemyTankMoveTask(tank, gameContext));
        tank.getTimer().schedule(new EnemyTankAutoShotTask(tank, this), 0, 500);
    }

    /**
     * 每隔36毫秒 一直向西走
     */
    public void enemyGoWest(EnemyTank enemy) {
        for (; ; ) {
            GameTimeUnit.sleepMillis(36);
            if (!enemy.isOverlapAndCanNotShot() && !enemy.isOverlapCanShot()) {
                enemy.goWest();
            }
            if (enemy.getMyTankLocation() != DirectionEnum.WEST) {
                enemy.setDirect(enemy.getMyTankDirect());
                break;
            }
        }
    }

    /**
     * 每隔36毫秒 一直向东走
     */
    public void enemyGoEast(EnemyTank enemy) {
        for (; ; ) {
            GameTimeUnit.sleepMillis(36);
            if (!enemy.isOverlapAndCanNotShot() && !enemy.isOverlapCanShot()) {
                enemy.goEast();
            }
            if (enemy.getMyTankLocation() != DirectionEnum.EAST) {
                enemy.setDirect(enemy.getMyTankDirect());
                break;
            }
        }
    }

    /**
     * 每隔36毫秒 一直向北走
     */
    public void enemyGoNorth(EnemyTank enemy) {
        for (; ; ) {
            GameTimeUnit.sleepMillis(36);
            if (!enemy.isOverlapAndCanNotShot() && !enemy.isOverlapCanShot()) {
                enemy.goNorth();
            }
            if (enemy.getMyTankLocation() != DirectionEnum.NORTH) {
                enemy.setDirect(enemy.getMyTankDirect());
                break;
            }
        }
    }

    /**
     * 每隔36毫秒 一直向南走
     */
    public void enemyGoSouth(EnemyTank enemy) {
        for (; ; ) {
            GameTimeUnit.sleepMillis(36);
            if (!enemy.isOverlapAndCanNotShot() && !enemy.isOverlapCanShot()) {
                enemy.goSouth();
            }
            if (enemy.getMyTankLocation() != DirectionEnum.SOUTH) {
                enemy.setDirect(enemy.getMyTankDirect());
                break;
            }
        }
    }

    /**
     * 让敌人坦克能够发现我的坦克并开炮
     *
     * @param enemy  敌方坦克
     * @param myTank 我的坦克
     * @param map    地图对象
     */
    public void enemyFindAndKill(EnemyTank enemy, MyTank myTank, Map map) {
        int myX = myTank.getX();
        int myY = myTank.getY();
        int enX = enemy.getX();
        int enY = enemy.getY();
        if (Math.abs(myX - enX) < 20 && myY <= 580) {
            int s = 0;
            if (enY < myY) {
                for (int t = 0; t < map.getIrons().size(); t++) {
                    Iron iron = map.getIrons().get(t);
                    if (Math.abs(enX - iron.getX()) <= 10 && iron.getY() > enY && iron.getY() < myY) {
                        s = 1;
                        break;
                    }
                }
                if (s == 0) {
                    enemy.setShot(true);
                    enemy.setMyTankLocation(DirectionEnum.SOUTH);
                }
            } else {
                for (int t = 0; t < map.getIrons().size(); t++) {
                    Iron iron = map.getIrons().get(t);
                    if (Math.abs(enX - iron.getX()) <= 10 && iron.getY() < enY && iron.getY() > myY) {
                        s = 1;
                        break;
                    }
                }
                if (s == 0) {
                    enemy.setShot(true);
                    enemy.setMyTankLocation(DirectionEnum.NORTH);
                }
            }
        } else if (Math.abs(myY - enY) < 20 && myY <= 580) {
            int s = 0;
            if (enX > myX) {
                for (int t = 0; t < map.getIrons().size(); t++) {
                    Iron iron = map.getIrons().get(t);
                    if (Math.abs(enY - iron.getY()) <= 10 && iron.getX() < enX && iron.getX() > myX) {
                        s = 1;
                        break;
                    }
                }
                if (s == 0) {
                    enemy.setShot(true);
                    enemy.setMyTankLocation(DirectionEnum.WEST);
                }
            } else {
                for (int t = 0; t < map.getIrons().size(); t++) {
                    Iron iron = map.getIrons().get(t);
                    if (Math.abs(enY - iron.getY()) <= 10 && iron.getX() > enX && iron.getX() < myX) {
                        s = 1;
                        break;
                    }
                }
                if (s == 0) {
                    enemy.setShot(true);
                    enemy.setMyTankLocation(DirectionEnum.EAST);
                }
            }
        } else {
            enemy.setShot(false);
            enemy.setMyTankLocation(DirectionEnum.INVALID);
        }
    }

    /**
     * 射击，发射一颗子弹
     *
     * @param tank 坦克对象
     */
    public void shot(Tank tank) {
        if (!tank.getLive()) {
            return;
        }
        if (tank instanceof EnemyTank) {
            SoundUtils.playEnemyFire();
        } else {
            SoundUtils.playShoot();
        }
        Bullet bullet = createBullet(tank);
        tank.getBullets().add(bullet);
        taskExecutor.execute(new BulletMoveTask(bullet));
    }

    private Bullet createBullet(Tank tank) {
        int x = tank.getX();
        int y = tank.getY();
        switch (tank.getDirect()) {
            case NORTH:
                y -= 24;
                break;
            case SOUTH:
                y += 24;
                break;
            case WEST:
                x -= 24;
                break;
            case EAST:
                x += 24;
                break;
            default:
                break;
        }
        return new Bullet(x, y, tank.getDirect(), tank.getBulletSpeed());
    }
}
