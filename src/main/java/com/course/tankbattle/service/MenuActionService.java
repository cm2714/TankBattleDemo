package com.course.tankbattle.service;

import com.course.tankbattle.context.GameContext;
import com.course.tankbattle.dto.RealTimeGameData;
import com.course.tankbattle.enums.LevelEnum;
import com.course.tankbattle.resource.map.Map;
import com.course.tankbattle.resource.map.level.Map1;
import com.course.tankbattle.resource.map.xmlparse.MapParser;
import com.course.tankbattle.resource.map.xmlparse.dto.XmlMap;
import com.course.tankbattle.view.menubar.TankBattleMenuBar;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.swing.*;

@Service
public class MenuActionService {
    private static final Logger LOGGER = LoggerFactory.getLogger(MenuActionService.class);

    @Autowired
    private GameContext context;

    public void start() {
        LOGGER.info("Menu action: start game");
        context.startGame();
    }

    public void again() {
        LOGGER.info("Menu action: restart game");
        context.backHome();
        context.startGame();
    }

    public void stop() {
        LOGGER.info("Menu action: pause/resume");
        context.getGameEventService().pauseOrResume(context.getRealTimeGameData());
    }

    public void ranking() {
        LOGGER.info("Menu action: show ranking");
        RealTimeGameData data = context.getRealTimeGameData();
        data.setShowRanking(!data.isShowRanking());
    }

    public void home() {
        LOGGER.info("Menu action: back to home");
        context.backHome();
        RealTimeGameData data = context.getRealTimeGameData();
        data.setStartScreen(true);
        data.setShowRanking(false);
    }

    public void exit() {
        LOGGER.info("Menu action: exit game");
        context.getGameEventService().exitGame();
    }

    public void first() {
        startLevel(LevelEnum.FIRST_LEVEL);
    }

    public void second() {
        startLevel(LevelEnum.SECOND_LEVEL);
    }

    public void third() {
        startLevel(LevelEnum.THIRD_LEVEL);
    }

    public void fourth() {
        startLevel(LevelEnum.FOUR_LEVEL);
    }

    public void fifth() {
        startLevel(LevelEnum.FIVE_LEVEL);
    }

    public void createMap() {
        LOGGER.info("Menu action: create map");
        RealTimeGameData data = context.getRealTimeGameData();
        data.setMapMakingMode(true);
        data.setMap(new Map1());
        data.setStartScreen(false);
        data.setStart(false);
    }

    public void saveMap() {
        LOGGER.info("Menu action: save map");
        RealTimeGameData data = context.getRealTimeGameData();
        boolean en = data.getLanguage() == com.course.tankbattle.enums.Language.ENGLISH;
        if (!data.getMapMakingMode() || data.getMap() == null) {
            JOptionPane.showMessageDialog(context.getGamePanel(),
                    en ? "No map to save." : "沒有可保存的地圖",
                    en ? "Info" : "提示",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }
        String name = JOptionPane.showInputDialog(context.getGamePanel(),
                en ? "Enter map name:" : "輸入地圖名稱:");
        if (name != null && !name.trim().isEmpty()) {
            MapParser.generateXmlFromMap(data.getMap(), name.trim());
            JOptionPane.showMessageDialog(context.getGamePanel(),
                    en ? "Map saved successfully." : "地圖保存成功",
                    en ? "Info" : "提示",
                    JOptionPane.INFORMATION_MESSAGE);
            TankBattleMenuBar menuBar = context.getTankBattleMenuBar();
            menuBar.refreshCustomMaps(menuBar.getCustomMapMenu());
        }
    }

    public void help() {
        LOGGER.info("Menu action: show help");
        RealTimeGameData data = context.getRealTimeGameData();
        boolean en = data.getLanguage() == com.course.tankbattle.enums.Language.ENGLISH;
        JPanel panel = context.getGamePanel();
        String msg, title;
        if (en) {
            msg = "Controls:\n" +
                  "  Arrow Keys  Move Tank\n" +
                  "  X           Fire\n" +
                  "  P           Pause / Resume\n" +
                  "  Enter       Start / Confirm\n" +
                  "  R           Ranking\n\n" +
                  "Tips:\n" +
                  "  Destroy all enemy tanks to clear the stage.\n" +
                  "  Protect your base.\n" +
                  "  Use bricks and iron as cover.";
            title = "How to Play";
        } else {
            msg = "操作說明：\n" +
                  "  ↑ ↓ ← →  移動坦克\n" +
                  "  X          開火射擊\n" +
                  "  P          暫停 / 繼續\n" +
                  "  Enter      開始 / 確認\n" +
                  "  R          排行榜\n\n" +
                  "提示：\n" +
                  "  消滅所有敵方坦克即可過關。\n" +
                  "  保護我方基地。\n" +
                  "  利用磚塊和鐵塊作為掩體。";
            title = "操作說明";
        }
        JOptionPane.showMessageDialog(panel, msg, title, JOptionPane.INFORMATION_MESSAGE);
    }

    public void about() {
        LOGGER.info("Menu action: show about");
        RealTimeGameData data = context.getRealTimeGameData();
        boolean en = data.getLanguage() == com.course.tankbattle.enums.Language.ENGLISH;
        JPanel panel = context.getGamePanel();
        String msg, title;
        if (en) {
            msg = "Battle City\n" +
                  "Version 1.0\n\n" +
                  "Java + Swing + Spring Boot\n\n" +
                  "© Chen Ming";
            title = "About";
        } else {
            msg = "坦克大戰\n" +
                  "版本 1.0\n\n" +
                  "Java + Swing + Spring Boot\n\n" +
                  "© 陳銘";
            title = "關於";
        }
        JOptionPane.showMessageDialog(panel, msg, title, JOptionPane.INFORMATION_MESSAGE);
    }

    public void loadCustomMap(String mapName) {
        LOGGER.info("Menu action: load custom map: {}", mapName);
        RealTimeGameData data = context.getRealTimeGameData();
        boolean en = data.getLanguage() == com.course.tankbattle.enums.Language.ENGLISH;
        try {
            XmlMap xmlMap = MapParser.getMapFromXml(mapName);
            if (xmlMap == null) {
                JOptionPane.showMessageDialog(context.getGamePanel(),
                        en ? "Failed to load map." : "地圖加載失敗",
                        en ? "Error" : "錯誤",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }
            data.clear();
            data.setMap(new Map(xmlMap));
            data.setLevel(LevelEnum.FIRST_LEVEL);
            context.startGame();
        } catch (Exception e) {
            LOGGER.error("Load custom map failed: {}", mapName, e);
            JOptionPane.showMessageDialog(context.getGamePanel(),
                    (en ? "Failed to load map: " : "地圖加載失敗: ") + e.getMessage(),
                    en ? "Error" : "錯誤",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    public void language() {
        LOGGER.info("Menu action: toggle language");
        RealTimeGameData data = context.getRealTimeGameData();
        data.toggleLanguage();
        boolean isEnglish = data.getLanguage() == com.course.tankbattle.enums.Language.ENGLISH;
        TankBattleMenuBar bar = context.getTankBattleMenuBar();
        if (bar != null) {
            bar.updateLanguage(isEnglish);
        }
    }

    private void startLevel(LevelEnum level) {
        LOGGER.info("Menu action: start level: {}", level.getName());
        context.startLevel(level);
    }
}
