package com.course.tankbattle.resource.map.xmlparse;

import com.course.tankbattle.entity.Stuff;
import com.course.tankbattle.exception.TankBattleGameException;
import com.course.tankbattle.resource.map.Map;
import com.course.tankbattle.resource.map.xmlparse.dto.XmlMap;
import org.apache.commons.digester3.Digester;
import org.apache.commons.digester3.annotations.FromAnnotationsRuleModule;
import org.apache.commons.digester3.binder.DigesterLoader;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Result;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

public final class MapParser {
    private static final Logger LOGGER = LoggerFactory.getLogger(MapParser.class);

    private MapParser() {
    }

    public static XmlMap getMapFromXml(String name) {
        DigesterLoader loader = DigesterLoader.newLoader(new FromAnnotationsRuleModule() {
            @Override
            protected void configureRules() {
                bindRulesFrom(XmlMap.class);
            }
        });

        String filePath = getCustomMapPath(name);
        try (InputStream is = new FileInputStream(new File(filePath))) {
            Digester digester = loader.newDigester();
            return digester.parse(is);
        } catch (Exception e) {
            LOGGER.error("Parse map failed: {}", filePath, e);
            throw new TankBattleGameException("地图解析失败");
        }
    }

    public static void generateXmlFromMap(Map map, String mapName) {
        try {
            Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
            Element rootElement = document.createElement("map");

            Element bricks = document.createElement("bricks");
            map.getBricks().forEach(b -> bricks.appendChild(generateElementByStuff(document, b)));
            rootElement.appendChild(bricks);

            Element irons = document.createElement("irons");
            map.getIrons().forEach(i -> irons.appendChild(generateElementByStuff(document, i)));
            rootElement.appendChild(irons);

            Element waters = document.createElement("waters");
            map.getWaters().forEach(w -> waters.appendChild(generateElementByStuff(document, w)));
            rootElement.appendChild(waters);

            document.appendChild(rootElement);
            File target = new File(getCustomMapPath(mapName));
            Result outputTarget = new StreamResult(FileUtils.openOutputStream(target));
            TransformerFactory.newInstance().newTransformer().transform(new DOMSource(document), outputTarget);
        } catch (Exception e) {
            LOGGER.error("Generate map xml failed", e);
            throw new TankBattleGameException("地图保存失败");
        }
    }

    private static String getCustomMapPath(String name) {
        return System.getProperty("user.home") + File.separator + ".tankBattle" + File.separator + "custom" + File.separator + name + ".xml";
    }

    private static Element generateElementByStuff(Document document, Stuff stuff) {
        Element element = document.createElement(stuff.getType().getXmlMark());
        Element x = document.createElement("x");
        x.setTextContent(stuff.getX().toString());
        Element y = document.createElement("y");
        y.setTextContent(stuff.getY().toString());
        element.appendChild(x);
        element.appendChild(y);
        return element;
    }
}
