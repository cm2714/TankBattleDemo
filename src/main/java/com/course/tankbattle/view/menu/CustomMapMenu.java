package com.course.tankbattle.view.menu;

import com.course.tankbattle.util.MapUtils;

import javax.swing.JMenu;
import javax.swing.JMenuItem;
import java.awt.event.ActionListener;
import java.io.File;

public class CustomMapMenu extends JMenu {
    public static final String CUSTOM_MAP_ACTION_COMMAND = "customMap";

    public CustomMapMenu(ActionListener listener) {
        super("Custom Maps");
        for (File file : MapUtils.listCustomMaps()) {
            String name = file.getName().replaceFirst("\\.xml$", "");
            JMenuItem item = new JMenuItem(name);
            item.setActionCommand(CUSTOM_MAP_ACTION_COMMAND);
            item.addActionListener(listener);
            add(item);
        }
    }
    public void updateTitle(boolean isEnglish) {
        setText(isEnglish ? "Custom Maps" : "自定義地圖");
    }
}

