

package com.course.tankbattle.enums;



public enum StuffTypeEnum {
    INVALID(-1, "无效", "unknown"),
    BRICK(0, "砖块", "brick"),
    IRON(1, "铁块", "iron"),
    WATER(2, "水池", "water"),
    TANK(3, "坦克", "tank"),
    MAP(4, "地图", "map");

    private Integer key;
    private String xmlMark;
    private String name;

    StuffTypeEnum(Integer key, String name, String xmlMark) {
        this.key = key;
        this.name = name;
        this.xmlMark = xmlMark;
    }

    public static StuffTypeEnum getByKey(Integer key) {
        for (StuffTypeEnum tmp : StuffTypeEnum.values()) {
            if (tmp.getKey().equals(key)) {
                return tmp;
            }
        }
        return INVALID;
    }

    public Integer getKey() {
        return key;
    }

    public String getXmlMark() {
        return xmlMark;
    }

    public String getName() {
        return name;
    }
}

