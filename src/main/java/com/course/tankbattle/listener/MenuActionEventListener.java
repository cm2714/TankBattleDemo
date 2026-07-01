

package com.course.tankbattle.listener;

import com.course.tankbattle.service.MenuActionService;
import com.course.tankbattle.view.menu.CustomMapMenu;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import com.course.tankbattle.util.SoundUtils;
import org.springframework.stereotype.Component;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

@Component
public class MenuActionEventListener implements ActionListener {
    private static final Logger LOGGER = LoggerFactory.getLogger(MenuActionEventListener.class);

    @Autowired
    private MenuActionService menuActionService;

    @Override
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();
        if (!(source instanceof JMenuItem)) {
            LOGGER.info("Nothing can do with command: {}", e.getActionCommand());
            return;
        }

        String command = e.getActionCommand();
        if (CustomMapMenu.CUSTOM_MAP_ACTION_COMMAND.equals(command)) {
            menuActionService.loadCustomMap(((JMenuItem) source).getText());
            return;
        }

        // Explicit dispatch instead of reflection
        dispatchCommand(command);
    }

    private void dispatchCommand(String command) {
        SoundUtils.playSelect();
        switch (command) {
            case "start":
                menuActionService.start();
                break;
            case "again":
                menuActionService.again();
                break;
            case "stop":
                menuActionService.stop();
                break;
            case "ranking":
                menuActionService.ranking();
                break;
            case "home":
                menuActionService.home();
                break;
            case "exit":
                menuActionService.exit();
                break;
            case "first":
                menuActionService.first();
                break;
            case "second":
                menuActionService.second();
                break;
            case "third":
                menuActionService.third();
                break;
            case "fourth":
                menuActionService.fourth();
                break;
            case "fifth":
                menuActionService.fifth();
                break;
            case "createMap":
                menuActionService.createMap();
                break;
            case "saveMap":
                menuActionService.saveMap();
                break;
            case "help":
                menuActionService.help();
                break;
            case "about":
                menuActionService.about();
                break;
            case "language":
                menuActionService.language();
                break;
            default:
                LOGGER.warn("Unknown menu command: {}", command);
                break;
        }
    }
}

