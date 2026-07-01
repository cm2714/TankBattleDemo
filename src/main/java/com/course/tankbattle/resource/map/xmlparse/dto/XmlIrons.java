package com.course.tankbattle.resource.map.xmlparse.dto;

import org.apache.commons.digester3.annotations.rules.ObjectCreate;
import org.apache.commons.digester3.annotations.rules.SetNext;

import java.util.Vector;

@ObjectCreate(pattern = "map/irons")
public class XmlIrons {
    private Vector<XmlIron> irons = new Vector<>();

    @SetNext
    public void addIron(XmlIron iron) {
        this.irons.add(iron);
    }

    public Vector<XmlIron> getIrons() {
        return irons;
    }

    public void setIrons(Vector<XmlIron> irons) {
        this.irons = irons;
    }
}
