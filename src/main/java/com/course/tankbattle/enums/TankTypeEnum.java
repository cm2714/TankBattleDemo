

package com.course.tankbattle.enums;


public enum TankTypeEnum {
    INVALID(-1, "无效"),
    MY(0, "我的坦克"),
    ENEMY(1, "敌人坦克");

    private Integer key;
    private String name;

    TankTypeEnum(Integer key, String name) {
        this.key = key;
        this.name = name;
    }

    public static TankTypeEnum getByKey(Integer key) {
        for (TankTypeEnum tmp : TankTypeEnum.values()) {
            if (tmp.getKey().equals(key)) {
                return tmp;
            }
        }
        return INVALID;
    }

    public Integer getKey() {
        return key;
    }

    public String getName() {
        return name;
    }
}

