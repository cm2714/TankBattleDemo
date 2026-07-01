

package com.course.tankbattle.resource.map.xmlparse.dto;

import org.apache.commons.digester3.annotations.rules.BeanPropertySetter;
import org.apache.commons.digester3.annotations.rules.ObjectCreate;


@ObjectCreate(pattern = "map/irons/iron")
public class XmlIron {
    @BeanPropertySetter(pattern = "map/irons/iron/x")
    private Integer x;
    @BeanPropertySetter(pattern = "map/irons/iron/y")
    private Integer y;

    public Integer getX() {
        return x;
    }

    public void setX(Integer x) {
        this.x = x;
    }

    public Integer getY() {
        return y;
    }

    public void setY(Integer y) {
        this.y = y;
    }
}

