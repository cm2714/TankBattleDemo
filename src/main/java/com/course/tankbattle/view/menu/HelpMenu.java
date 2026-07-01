package com.course.tankbattle.view.menu;

import javax.swing.JMenu;
import javax.swing.JMenuItem;
import java.awt.event.ActionListener;

public class HelpMenu extends JMenu {
    public HelpMenu(ActionListener listener) {
        super("Help");
        addItem("How to Play", "help", listener);
        addItem("About", "about", listener);
    }

    private void addItem(String text, String command, ActionListener listener) {
        JMenuItem item = new JMenuItem(text);
        item.setActionCommand(command);
        item.addActionListener(listener);
        add(item);
    }
    public void updateTitle(boolean isEnglish) {
        setText(isEnglish ? "Help" : "幫助");
        getItem(0).setText(isEnglish ? "How to Play" : "操作說明");
        getItem(1).setText(isEnglish ? "About" : "關於");
    }
}


