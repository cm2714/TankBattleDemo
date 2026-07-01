package com.course.tankbattle.view.menu;

import javax.swing.JMenu;
import javax.swing.JMenuItem;
import java.awt.event.ActionListener;

public class MapEditMenu extends JMenu {
    public MapEditMenu(ActionListener listener) {
        super("Map Editor");
        addItem("創建地圖", "createMap", listener);
        addItem("保存地圖", "saveMap", listener);
    }

    private void addItem(String text, String command, ActionListener listener) {
        JMenuItem item = new JMenuItem(text);
        item.setActionCommand(command);
        item.addActionListener(listener);
        add(item);
    }
    public void updateTitle(boolean isEnglish) {
        setText(isEnglish ? "Map Editor" : "地圖編輯");
        getItem(0).setText(isEnglish ? "Create Map" : "創建地圖");
        getItem(1).setText(isEnglish ? "Save Map" : "保存地圖");
    }
}


