package com.course.tankbattle.service;

import com.course.tankbattle.constant.GameConstants;
import com.course.tankbattle.context.GameContext;
import com.course.tankbattle.dto.RealTimeGameData;
import com.course.tankbattle.entity.Bomb;
import com.course.tankbattle.entity.Brick;
import com.course.tankbattle.entity.Bullet;
import com.course.tankbattle.entity.EnemyTank;
import com.course.tankbattle.entity.Iron;
import com.course.tankbattle.entity.MyTank;
import com.course.tankbattle.entity.Stuff;
import com.course.tankbattle.entity.Tank;
import com.course.tankbattle.entity.Water;
import com.course.tankbattle.enums.DirectionEnum;
import com.course.tankbattle.resource.map.Map;
import com.course.tankbattle.util.SoundUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Vector;

@Service
public class StateFlushService {
    @Autowired
    private GameContext context;
    @Autowired
    private TankControlService tankControlService;
    @Autowired
    private ComputingService computingService;

    public void refreshEnemyTankState() {
        RealTimeGameData data = context.getRealTimeGameData();
        if (data.getMyTanks().isEmpty()) {
            return;
        }
        MyTank myTank = data.getMyTanks().get(0);
        for (EnemyTank enemyTank : data.getEnemies()) {
            enemyTank.setMyTankDirect(myTank.getDirect());
            tankControlService.enemyFindAndKill(enemyTank, myTank, data.getMap());
        }
    }

    public void refreshBulletState() {
        RealTimeGameData data = context.getRealTimeGameData();
        Vector<MyTank> myTanks = data.getMyTanks();
        Vector<EnemyTank> enemies = data.getEnemies();
        Vector<Bomb> bombs = data.getBombs();
        Map map = data.getMap();

        if (myTanks.isEmpty()) {
            for (EnemyTank enemyTank : enemies) {
                enemyTank.setShot(false);
            }
            return;
        }
        MyTank myTank = myTanks.get(0);
        for (EnemyTank enemyTank : enemies) {
            for (Bullet enemyBullet : enemyTank.getBullets()) {
                processBulletShotEvent(enemyBullet, myTank, bombs, map);
            }
            for (Bullet myBullet : myTank.getBullets()) {
                processBulletShotEvent(myBullet, enemyTank, bombs, map);
                for (Bullet enemyBullet : enemyTank.getBullets()) {
                    if (computingService.isHitting(myBullet, enemyBullet)) {
                        myBullet.setLive(false);
                        enemyBullet.setLive(false);
                        Bomb bomb = new Bomb(myBullet.getX(), myBullet.getY());
                        bomb.setL(20);
                        bombs.add(bomb);
                    }
                }
            }
        }
    }

    public void refreshOverlapState() {
        RealTimeGameData data = context.getRealTimeGameData();
        Map map = data.getMap();
        for (MyTank myTank : data.getMyTanks()) {
            myTank.setOverlapAndCanNotShot(false);
            myTank.setOverlapCanShot(false);
            myTank.setOverlapCanShot(computingService.isMyTankOverlap(myTank, data.getEnemies()));
            for (Brick brick : map.getBricks()) {
                if (computingService.isTankOverlap(myTank, brick, 30)) {
                    myTank.setOverlapCanShot(true);
                }
            }
            for (Iron iron : map.getIrons()) {
                if (computingService.isTankOverlap(myTank, iron, 30)) {
                    myTank.setOverlapAndCanNotShot(true);
                }
            }
            for (Water water : map.getWaters()) {
                if (computingService.isTankOverlap(myTank, water, 30)) {
                    myTank.setOverlapAndCanNotShot(true);
                }
            }
        }

        for (EnemyTank enemyTank : data.getEnemies()) {
            enemyTank.setOverlapAndCanNotShot(false);
            enemyTank.setOverlapCanShot(false);
            enemyTank.setOverlapCanShot(computingService.isEnemyTankOverlap(enemyTank, data.getEnemies(), data.getMyTanks()));
            for (Brick brick : map.getBricks()) {
               if (computingService.isTankOverlap(enemyTank, brick, 30)) {
                       enemyTank.setOverlapCanShot(true);
                       enemyTank.setShot(true);
               }
            }
            for (Iron iron : map.getIrons()) {
               if (computingService.isTankOverlap(enemyTank, iron, 30)) {
                   enemyTank.setOverlapAndCanNotShot(true);
                }
            }
            for (Water water : map.getWaters()) {
                if (computingService.isTankOverlap(enemyTank, water, 30)) {
                    enemyTank.setOverlapAndCanNotShot(true);
                }
            }
        }
    }

    public void cleanAndCreate() {
        RealTimeGameData data = context.getRealTimeGameData();
        List<MyTank> diedMyTanks = new ArrayList<>();
        List<EnemyTank> diedEnemyTanks = new ArrayList<>();
        List<MyTank> addMyTanks = new ArrayList<>();
        List<EnemyTank> addEnemyTanks = new ArrayList<>();

        for (MyTank myTank : data.getMyTanks()) {
            myTank.getBullets().removeIf(b -> !b.isLive());
            if (!myTank.getLive()) {
                diedMyTanks.add(myTank);
                data.setMyTankNum(data.getMyTankNum() - 1);
                data.setBeKilled(data.getBeKilled() + 1);
                if (data.getMyTankNum() >= 1) {
                    addMyTanks.add(new MyTank(300, 620, DirectionEnum.NORTH));
                }
            }
        }

        for (EnemyTank enemy : data.getEnemies()) {
            enemy.getBullets().removeIf(b -> !b.isLive());
            if (!enemy.getLive()) {
                enemy.getTimer().cancel();
                diedEnemyTanks.add(enemy);
                data.setEnemyTankNum(data.getEnemyTankNum() - 1);
                if (data.getEnemyTankNum() >= GameConstants.INIT_ENEMY_TANK_IN_MAP_NUM) {
                    int location = new Random().nextInt(GameConstants.INIT_ENEMY_TANK_IN_MAP_NUM);
                    int fullSpan = GameConstants.GAME_PANEL_WIDTH - GameConstants.TANK_WIDTH;
                    int xStep = fullSpan / (GameConstants.INIT_ENEMY_TANK_IN_MAP_NUM - 1);
                    int x = location * xStep + GameConstants.TANK_WIDTH / 2 + new Random().nextInt(21) - 10;
                    if (x < 20) x = 20;
                    if (x > GameConstants.GAME_PANEL_WIDTH - 20) x = GameConstants.GAME_PANEL_WIDTH - 20;
                    EnemyTank enemyTank = new EnemyTank(x, -GameConstants.TANK_HEIGHT / 2, DirectionEnum.SOUTH);
                    enemyTank.setLocation(location);
                    enemyTank.setActivate(Boolean.TRUE);
                    tankControlService.enableEnemyTank(enemyTank);
                    addEnemyTanks.add(enemyTank);
                }
            }
        }

        data.getMyTanks().removeAll(diedMyTanks);
        data.getEnemies().removeAll(diedEnemyTanks);
        data.getMyTanks().addAll(addMyTanks);
        data.getEnemies().addAll(addEnemyTanks);
        data.getBombs().removeIf(bomb -> !bomb.isLive());
        data.getMap().getBricks().removeIf(brick -> !brick.getLive());
    }

    public void refreshMyTankState(RealTimeGameData gameData) {
       for (MyTank myTank : gameData.getMyTanks()) {
           if (myTank.isOverlapAndCanNotShot()) {
               return;
           }
            if (myTank.isOverlapCanShot()) {
                // Blocked by soft obstacle — allow direction change but not forward movement
                if (gameData.isUp())    { myTank.setDirect(DirectionEnum.NORTH); }
                else if (gameData.isDown())  { myTank.setDirect(DirectionEnum.SOUTH); }
                else if (gameData.isLeft())  { myTank.setDirect(DirectionEnum.WEST); }
                else if (gameData.isRight()) { myTank.setDirect(DirectionEnum.EAST); }
                return;
            }
           if (gameData.isUp()) {
               myTank.goNorth();
            } else if (gameData.isDown()) {
                myTank.goSouth();
            } else if (gameData.isLeft()) {
                myTank.goWest();
            } else if (gameData.isRight()) {
                myTank.goEast();
            }
        }
    }

    private void processBulletShotEvent(Bullet bullet, Tank tank, Vector<Bomb> bombs, Map map) {
        if (!bullet.isLive()) {
            return;
        }
        if (computingService.isHitting(bullet, tank)) {
            // 在家无敌：子弹打中玩家但玩家未离开出生点时，只销毁子弹不扣血
            if (tank instanceof MyTank && tank.getY() > GameConstants.HOME_Y_THRESHOLD) {
                bullet.setLive(false);
                Bomb bomb = new Bomb(bullet.getX(), bullet.getY());
                bomb.setL(20);
                bombs.add(bomb);
            } else {
                afterShotTank(bullet, tank, bombs);
            }
        }
        for (Brick brick : map.getBricks()) {
            if (computingService.isHitting(bullet, brick)) {
                afterShotStuff(bullet, brick, bombs);
            }
        }
        for (Iron iron : map.getIrons()) {
            if (computingService.isHitting(bullet, iron)) {
                afterShotStuff(bullet, iron, bombs);
            }
        }
    }

   public void afterShotStuff(Bullet bullet, Stuff stuff, Vector<Bomb> bombs) {
        Bomb bomb;
        switch (stuff.getType()) {
            case BRICK:
                bullet.setLive(false);
                stuff.setLive(false);
                bomb = new Bomb(stuff.getX(), stuff.getY());
                bomb.setL(40);
                bombs.add(bomb);
                break;
            case IRON:
                bullet.setLive(false);
                bomb = new Bomb(bullet.getX(), bullet.getY());
                bomb.setL(20);
                bombs.add(bomb);
                break;
            default:
                break;
        }
    }

    public void afterShotTank(Bullet bullet, Tank tank, Vector<Bomb> bombs) {
        bullet.setLive(false);
        Bomb bomb;
        if (tank.getBlood() <= 1) {
            tank.setLive(false);
            tank.setBlood(0);
            SoundUtils.playBomb();
            bomb = new Bomb(tank.getX(), tank.getY());
            bomb.setL(120);
        } else {
            tank.setBlood(tank.getBlood() - 1);
            bomb = new Bomb(bullet.getX(), bullet.getY());
            bomb.setL(40);
        }
        bombs.add(bomb);
    }
}
