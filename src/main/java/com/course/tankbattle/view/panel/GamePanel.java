

package com.course.tankbattle.view.panel;

import com.course.tankbattle.listener.MouseEventListener;
import com.course.tankbattle.service.PaintService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;


public class GamePanel extends JPanel {
    private Logger logger = LoggerFactory.getLogger(GamePanel.class);

    private static final long serialVersionUID = 2933760710140135907L;
    private PaintService paintService;

    public GamePanel(PaintService paintService, MouseEventListener mouseEventListener) {
        super();
        this.paintService = paintService;
        this.addMouseListener(mouseEventListener);
    }

    /*
     * 重新paint
     */
    @Override
    public void paint(Graphics g) {
        super.paint(g);
        this.paintService.rePaintPanel(this, g);
        logger.debug("paint...");
    }
}

