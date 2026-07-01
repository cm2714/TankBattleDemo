

package com.course.tankbattle.resource.map.xmlparse.dto;

import org.apache.commons.digester3.annotations.rules.ObjectCreate;
import org.apache.commons.digester3.annotations.rules.SetNext;

import java.util.Vector;


@ObjectCreate(pattern = "map/waters")
public class XmlWaters {

    private Vector<XmlWater> waters = new Vector<>();


    public Vector<XmlWater> getWaters() {
        return waters;
    }

    public void setWaters(Vector<XmlWater> waters) {
        this.waters = waters;
    }

    @SetNext
    public void addWater(XmlWater water) {
        this.waters.add(water);
    }
}

