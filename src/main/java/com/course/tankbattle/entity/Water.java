

package com.course.tankbattle.entity;

import com.course.tankbattle.enums.StuffTypeEnum;


public class Water extends Stuff {
    /**
     * 构造方法
     *
     * @param x x坐标
     * @param y y坐标
     */

    public Water(int x, int y) {
        super(x, y);
        this.setType(StuffTypeEnum.WATER);
        this.setWidth(20);
        this.setHeight(20);
    }

}

