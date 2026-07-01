package com.course.tankbattle.task;

import com.course.tankbattle.constant.GameConstants;
import com.course.tankbattle.entity.Bullet;
import com.course.tankbattle.enums.DirectionEnum;
import com.course.tankbattle.util.GameTimeUnit;

public class BulletMoveTask implements Runnable {
    private final Bullet bullet;

    public BulletMoveTask(Bullet bullet) {
        this.bullet = bullet;
    }

    @Override
    public void run() {
        while (bullet.isLive()) {
            GameTimeUnit.sleepMillis(25);
            move();
            if (bullet.getX() < 0 || bullet.getX() > GameConstants.GAME_PANEL_WIDTH ||
                    bullet.getY() < 0 || bullet.getY() > GameConstants.GAME_PANEL_HEIGHT) {
                bullet.setLive(false);
            }
        }
    }

    private void move() {
        DirectionEnum direct = bullet.getDirect();
        if (direct == DirectionEnum.NORTH) {
            bullet.setY(bullet.getY() - bullet.getSpeed());
        } else if (direct == DirectionEnum.SOUTH) {
            bullet.setY(bullet.getY() + bullet.getSpeed());
        } else if (direct == DirectionEnum.WEST) {
            bullet.setX(bullet.getX() - bullet.getSpeed());
        } else if (direct == DirectionEnum.EAST) {
            bullet.setX(bullet.getX() + bullet.getSpeed());
        }
    }
}
