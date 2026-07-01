package com.course.tankbattle.listener;

import com.course.tankbattle.context.GameContext;
import com.course.tankbattle.dto.RealTimeGameData;
import com.course.tankbattle.entity.Brick;
import com.course.tankbattle.entity.Iron;
import com.course.tankbattle.entity.Stuff;
import com.course.tankbattle.entity.Water;
import com.course.tankbattle.util.MapUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Iterator;

@Component
public class MouseEventListener implements MouseListener {
    private static final Logger LOGGER = LoggerFactory.getLogger(MouseEventListener.class);

    @Autowired
    private GameContext context;

    @Override
    public void mouseClicked(MouseEvent e) {
        RealTimeGameData data = context.getRealTimeGameData();
        if (!data.getMapMakingMode()) {
            return;
        }
        Stuff gridStuff = MapUtils.getNearestStuff(e.getX(), e.getY());
        int x = gridStuff.getX();
        int y = gridStuff.getY();

        if (e.getButton() == MouseEvent.BUTTON1) {
            switch (data.getCurrentSelectedStuff()) {
                case BRICK:
                    data.getMap().getBricks().add(new Brick(x, y));
                    break;
                case IRON:
                    data.getMap().getIrons().add(new Iron(x, y));
                    break;
                case WATER:
                    data.getMap().getWaters().add(new Water(x, y));
                    break;
                default:
                    break;
            }
        } else if (e.getButton() == MouseEvent.BUTTON3) {
            eraseStuff(data, x, y);
        }
    }

    private void eraseStuff(RealTimeGameData data, int x, int y) {
        data.getMap().getBricks().removeIf(b -> b.getX().equals(x) && b.getY().equals(y));
        data.getMap().getIrons().removeIf(i -> i.getX().equals(x) && i.getY().equals(y));
        data.getMap().getWaters().removeIf(w -> w.getX().equals(x) && w.getY().equals(y));
    }

    @Override
    public void mousePressed(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }
}
