

package com.course.tankbattle.view.menubar;

import com.course.tankbattle.view.menu.*;

import javax.swing.*;
import java.awt.event.ActionListener;


public class TankBattleMenuBar extends JMenuBar {

    private static final long serialVersionUID = -4010376438320829163L;
    private GameMenu gameMenu;
    private SysMapMenu sysMapMenu;
    private HelpMenu helpMenu;
    private MapEditMenu mapEditMenu;
    private CustomMapMenu customMapMenu;

    public TankBattleMenuBar(ActionListener listener) {
        super();

        gameMenu = new GameMenu(listener);
        sysMapMenu = new SysMapMenu(listener);
        helpMenu = new HelpMenu(listener);
        mapEditMenu = new MapEditMenu(listener);
        customMapMenu=new CustomMapMenu(listener);

        this.add(gameMenu);
        this.add(sysMapMenu);
        this.add(mapEditMenu);        
        this.add(customMapMenu);
        this.add(helpMenu);
    }

    public GameMenu getGameMenu() {
        return gameMenu;
    }

    public SysMapMenu getSysMapMenu() {
        return sysMapMenu;
    }

    public MapEditMenu getMapEditMenu() {
        return mapEditMenu;
    }

    public CustomMapMenu getCustomMapMenu() {
        return customMapMenu;
    }

    public void updateLanguage(boolean isEnglish) {
        gameMenu.updateLanguageText(isEnglish);
        sysMapMenu.updateTitle(isEnglish);
        mapEditMenu.updateTitle(isEnglish);
        customMapMenu.updateTitle(isEnglish);
        helpMenu.updateTitle(isEnglish);
    }

    /**
     * Refresh custom maps menu items
     */
    public void refreshCustomMaps(CustomMapMenu menu) {
        if (menu == null) return;
        menu.removeAll();
        java.io.File[] files = com.course.tankbattle.util.MapUtils.listCustomMaps().toArray(new java.io.File[0]);
        java.awt.event.ActionListener listener = menu.getItem(0) != null ?
                menu.getItem(0).getActionListeners()[0] : null;
        if (listener == null) return;
        for (java.io.File file : files) {
            String name = file.getName().replaceFirst("\\.xml$", "");
            javax.swing.JMenuItem item = new javax.swing.JMenuItem(name);
            item.setActionCommand(com.course.tankbattle.view.menu.CustomMapMenu.CUSTOM_MAP_ACTION_COMMAND);
            item.addActionListener(listener);
            menu.add(item);
        }
    }

    @Override
    public HelpMenu getHelpMenu() {
        return helpMenu;
    }
}

