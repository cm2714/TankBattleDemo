package com.course.tankbattle.service;

import com.course.tankbattle.constant.GameConstants;
import com.course.tankbattle.entity.Bullet;
import com.course.tankbattle.entity.EnemyTank;
import com.course.tankbattle.entity.MyTank;
import com.course.tankbattle.entity.Stuff;
import com.course.tankbattle.entity.Tank;
import com.course.tankbattle.enums.DirectionEnum;
import org.springframework.stereotype.Service;

import java.util.Random;
import java.util.Vector;

@Service
public class ComputingService {
    public Boolean isHitting(Bullet bullet, Stuff stuff) {
        return bullet != null && stuff != null && stuff.getLive() &&
                Math.abs(bullet.getX() - stuff.getX()) <= (stuff.getWidth() + bullet.getWidth()) / 2 &&
                Math.abs(bullet.getY() - stuff.getY()) <= (stuff.getHeight() + bullet.getHeight()) / 2;
    }

    public Boolean isHitting(Bullet bullet1, Bullet bullet2) {
        return bullet1 != null && bullet2 != null &&
                Math.abs(bullet1.getX() - bullet2.getX()) <= bullet1.getWidth() &&
                Math.abs(bullet1.getY() - bullet2.getY()) <= bullet1.getHeight();
    }

    public boolean equals(Stuff s1, Stuff s2) {
        return s1 != null && s2 != null && s2.getX().equals(s1.getX()) && s2.getY().equals(s1.getY());
    }

    public boolean isMyTankOverlap(MyTank tank, Vector<EnemyTank> enemies) {
        for (EnemyTank enemy : enemies) {
            if (isTankOverlap(tank, enemy, GameConstants.TANK_WIDTH)) {
                return true;
            }
        }
        return false;
    }

    public boolean isEnemyTankOverlap(EnemyTank enemy, Vector<EnemyTank> enemies, Vector<MyTank> myTanks) {
        for (EnemyTank enemyTank : enemies) {
            if (enemy != enemyTank && isTankOverlap(enemy, enemyTank, GameConstants.TANK_WIDTH)) {
                enemy.setOverlapAndCanNotShot(true);
                return true;
            }
        }
        for (MyTank myTank : myTanks) {
            if (isTankOverlap(enemy, myTank, GameConstants.TANK_WIDTH)) {
                enemy.setOverlapCanShot(true);
                return true;
            }
        }
        enemy.setOverlapAndCanNotShot(false);
        enemy.setOverlapCanShot(false);
        return false;
    }

    public boolean isTankOverlap(Tank tank, Stuff stuff, int length) {
        int nextX = tank.getX();
        int nextY = tank.getY();
        switch (tank.getDirect()) {
            case NORTH:
                nextY -= tank.getSpeed();
                break;
            case SOUTH:
                nextY += tank.getSpeed();
                break;
            case EAST:
                nextX += tank.getSpeed();
                break;
            case WEST:
                nextX -= tank.getSpeed();
                break;
            default:
                break;
        }
        return Math.abs(nextY - stuff.getY()) < length && Math.abs(nextX - stuff.getX()) < length;
    }

    public DirectionEnum enemyGetRandomDirect(DirectionEnum direct1, DirectionEnum direct2, DirectionEnum direct3) {
        switch (new Random().nextInt(3)) {
            case 0:
                return direct1;
            case 1:
                return direct2;
            case 2:
                return direct3;
            default:
                return DirectionEnum.INVALID;
        }
    }
}
