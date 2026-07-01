package com.course.tankbattle.view.menu;

import javax.swing.JMenu;
import javax.swing.JMenuItem;
import java.awt.event.ActionListener;

public class GameMenu extends JMenu {
    private static final long serialVersionUID = -3078540026626514620L;
    private JMenuItem langItem;

    public GameMenu(ActionListener listener) {
        super("Game");
        addItem("開始遊戲", "start", listener);
        addItem("重新開始", "again", listener);
        addItem("暫停/恢復", "stop", listener);
        addItem("排行榜", "ranking", listener);
        addSeparator();
        langItem = addItem("Language", "language", listener);
        addSeparator();
        addItem("返回首頁", "home", listener);
        addItem("退出遊戲", "exit", listener);
    }

    private JMenuItem addItem(String text, String command, ActionListener listener) {
        JMenuItem item = new JMenuItem(text);
        item.setActionCommand(command);
        item.addActionListener(listener);
        add(item);
        return item;
    }

    public void updateLanguageText(boolean isEnglish) {
        if (isEnglish) {
            setText("Game");
            getItem(0).setText("Start");
            getItem(1).setText("Restart");
            getItem(2).setText("Pause/Resume");
            getItem(3).setText("Ranking");
            getItem(7).setText("Home");
            getItem(8).setText("Exit");
            langItem.setText("Language: English");
        } else {
            setText("遊戲");
            getItem(0).setText("開始遊戲");
            getItem(1).setText("重新開始");
            getItem(2).setText("暫停/恢復");
            getItem(3).setText("排行榜");
            getItem(7).setText("返回首頁");
            getItem(8).setText("退出遊戲");
            langItem.setText("語言: 中文");
        }
    }
}

