

package com.course.tankbattle.view.menu;

import javax.swing.*;
import java.awt.event.ActionListener;


public class SysMapMenu extends JMenu {

    public SysMapMenu(ActionListener listener) {
        super("System Maps");
        JMenuItem firstLevel = new JMenuItem("第一关");
        JMenuItem secondLevel = new JMenuItem("第二关");
        JMenuItem thirdLevel = new JMenuItem("第三关");
        JMenuItem fourthLevel = new JMenuItem("第四关");
        JMenuItem fifthLevel = new JMenuItem("第五关");

        firstLevel.setActionCommand("first");
        secondLevel.setActionCommand("second");
        thirdLevel.setActionCommand("third");
        fourthLevel.setActionCommand("fourth");
        fifthLevel.setActionCommand("fifth");

        firstLevel.addActionListener(listener);
        secondLevel.addActionListener(listener);
        thirdLevel.addActionListener(listener);
        fourthLevel.addActionListener(listener);
        fifthLevel.addActionListener(listener);

        this.add(firstLevel);
        this.add(secondLevel);
        this.add(thirdLevel);
        this.add(fourthLevel);
        this.add(fifthLevel);
    }

    public void updateTitle(boolean isEnglish) {
        setText(isEnglish ? "System Maps" : "系統地圖");
        String[] en = {"Stage 1", "Stage 2", "Stage 3", "Stage 4", "Stage 5"};
        String[] zh = {"第一關", "第二關", "第三關", "第四關", "第五關"};
        for (int i = 0; i < 5; i++) {
            getItem(i).setText(isEnglish ? en[i] : zh[i]);
        }
    }
}


