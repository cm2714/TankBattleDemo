

package com.course.tankbattle.entity;

import com.course.tankbattle.enums.DirectionEnum;
import com.course.tankbattle.enums.TankTypeEnum;

import java.awt.Color;


public class MyTank extends Tank {
    /**
     * 构造方法
     *
     * @param x      x坐标
     * @param y      y坐标
     * @param direct 方向
     */
    public MyTank(int x, int y, DirectionEnum direct) {
        super(x, y, direct);
        this.setColor(Color.yellow);
        this.setTankType(TankTypeEnum.MY);
        this.setBlood(10);
    }


}

