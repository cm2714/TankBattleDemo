package com.course.tankbattle.entity;

import com.course.tankbattle.constant.GameConstants;
import com.course.tankbattle.enums.DirectionEnum;
import com.course.tankbattle.enums.StuffTypeEnum;
import com.course.tankbattle.enums.TankTypeEnum;

import java.awt.*;
import java.util.Vector;

public class Tank extends Stuff {
    private Color color = Color.green;
    private int speed = 4;
    private int bulletSpeed = 6;
    private Vector<Bullet> bullets;
    private boolean isOverlapAndCanNotShot = false;
    private boolean isOverlapCanShot = false;
    private int speedVector;
    private Boolean isActivate = Boolean.FALSE;
    private TankTypeEnum tankType;

    public Tank(int x, int y, DirectionEnum direct) {
        super(x, y);
        this.setDirect(direct);
        this.bullets = new Vector<>();
        this.setType(StuffTypeEnum.TANK);
        this.setWidth(40);
        this.setHeight(40);
    }

    public void goNorth() {
        this.setDirect(DirectionEnum.NORTH);
        if (this.getY() > 20) {
            this.setY(this.getY() - this.speed);
        }
    }

    public void goSouth() {
        this.setDirect(DirectionEnum.SOUTH);
        if (this.getY() < GameConstants.GAME_PANEL_HEIGHT - 20) {
            this.setY(this.getY() + this.speed);
        }
    }

    public void goWest() {
        this.setDirect(DirectionEnum.WEST);
        if (this.getX() > 20 && this.getY() <= GameConstants.GAME_PANEL_HEIGHT - 20) {
            this.setX(this.getX() - this.speed);
        }
    }

    public void goEast() {
        this.setDirect(DirectionEnum.EAST);
        if (this.getX() < GameConstants.GAME_PANEL_WIDTH - 20
                && this.getY() <= GameConstants.GAME_PANEL_HEIGHT - 20) {
            this.setX(this.getX() + this.speed);
        }
    }

    public void go(DirectionEnum where) {
        switch (where) {
            case NORTH: this.goNorth(); break;
            case SOUTH: this.goSouth(); break;
            case WEST:  this.goWest();  break;
            case EAST:  this.goEast();  break;
            default: break;
        }
    }

    public int getBulletSpeed() { return bulletSpeed; }
    public void setBulletSpeed(int bulletSpeed) { this.bulletSpeed = bulletSpeed; }

    public int getSpeed() { return speed; }
    public void setSpeed(int speed) { this.speed = speed; }

    public Color getColor() { return color; }
    public void setColor(Color color) { this.color = color; }

    public Vector<Bullet> getBullets() { return bullets; }
    public void setBullets(Vector<Bullet> bullets) { this.bullets = bullets; }

    public void setSpeedVector(int speedVector) { this.speedVector = speedVector; }
    public int getSpeedVector() { return speedVector; }

    public boolean isOverlapAndCanNotShot() { return isOverlapAndCanNotShot; }
    public void setOverlapAndCanNotShot(boolean v) { this.isOverlapAndCanNotShot = v; }

    public boolean isOverlapCanShot() { return isOverlapCanShot; }
    public void setOverlapCanShot(boolean v) { this.isOverlapCanShot = v; }

    public Boolean activate() { return isActivate; }
    public void setActivate(Boolean activate) { isActivate = activate; }

    public TankTypeEnum getTankType() { return tankType; }
    public void setTankType(TankTypeEnum tankType) { this.tankType = tankType; }
}
