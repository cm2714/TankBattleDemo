package com.course.tankbattle.resource.map.xmlparse.dto;

import org.apache.commons.digester3.annotations.rules.ObjectCreate;
import org.apache.commons.digester3.annotations.rules.SetNext;

@ObjectCreate(pattern = "map")
public class XmlMap {
    private XmlBricks bricks = new XmlBricks();
    private XmlIrons irons = new XmlIrons();
    private XmlWaters waters = new XmlWaters();

    public XmlBricks getBricks() {
        return bricks;
    }

    @SetNext
    public void setBricks(XmlBricks bricks) {
        this.bricks = bricks;
    }

    public XmlIrons getIrons() {
        return irons;
    }

    @SetNext
    public void setIrons(XmlIrons irons) {
        this.irons = irons;
    }

    public XmlWaters getWaters() {
        return waters;
    }

    @SetNext
    public void setWaters(XmlWaters waters) {
        this.waters = waters;
    }
}
