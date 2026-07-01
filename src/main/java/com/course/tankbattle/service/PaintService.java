package com.course.tankbattle.service;

import com.course.tankbattle.constant.GameConstants;
import com.course.tankbattle.context.GameContext;
import com.course.tankbattle.dto.RealTimeGameData;
import com.course.tankbattle.dto.ScoreRecord;
import com.course.tankbattle.entity.*;
import com.course.tankbattle.enums.Language;
import com.course.tankbattle.enums.LevelEnum;
import com.course.tankbattle.resource.image.Images;
import com.course.tankbattle.resource.map.Map;
import com.course.tankbattle.util.GameFont;
import com.course.tankbattle.util.Texts;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.swing.*;
import java.awt.*;
import java.util.List;

@Service
public class PaintService {
    private static final Logger LOGGER = LoggerFactory.getLogger(PaintService.class);

    @Autowired
    private GameContext context;
    @Autowired
    private ScoreService scoreService;

    public void rePaintPanel(JPanel panel, Graphics g) {
        RealTimeGameData data = context.getRealTimeGameData();
        if (data == null) return;
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        if (data.isStartScreen()) { drawStartScreen(g2d, panel, data); return; }
        if (data.isShowRanking()) { drawRanking(g2d, panel, data); return; }
        drawGameArea(g2d, data);
        drawPanelObjects(g2d, data);
        if (data.isSettlement()) drawSettlement(g2d, panel, data);
    }

    private void drawStartScreen(Graphics2D g, JPanel panel, RealTimeGameData data) {
        int w = panel.getWidth(), h = panel.getHeight();
        g.setColor(new Color(8, 8, 16));
        g.fillRect(0, 0, w, h);
        Image bg = Images.startImage;
        if (bg != null) {
            g.drawImage(bg, 0, 0, w, h, panel);
                    // Brighten as title rises: fontY=700->alpha 160, fontY=30->alpha 60
        int fY = data.getFontY();
        int alpha = 60 + Math.max(0, Math.min(100, (fY - 30) * 100 / (700 - 30)));
        g.setColor(new Color(0, 0, 0, alpha));
            g.fillRect(0, 0, w, h);
        }
        Image fontImg = Images.font;
        int fontY = data.getFontY();
        if (fontImg != null) {
            int imgW = fontImg.getWidth(panel);
            int imgH = fontImg.getHeight(panel);
            int drawX = (w - imgW) / 2;
            if (imgW > w) {
                int dw = w, dh = (dw * imgH) / imgW;
                g.drawImage(fontImg, 0, fontY, dw, dh, panel);
            } else {
                g.drawImage(fontImg, drawX, fontY, panel);
            }
        }
        if (fontY <= 30) {
            int sel = data.getStartMenuSelection();
            boolean en = data.getLanguage() == Language.ENGLISH;
            String startText = en ? "START GAME" : "开始游戏";
            String helpText = en ? "ABOUT" : "关于";

            g.setFont(GameFont.getFont(26f));
            FontMetrics fm = g.getFontMetrics();

            // START GAME
            if (sel == 0) {
                int alpha = (int) (Math.sin(System.currentTimeMillis() / 300.0) * 80 + 175);
                alpha = Math.max(0, Math.min(255, alpha));
                g.setColor(new Color(255, 215, 0, alpha));
            } else {
                g.setColor(new Color(180, 180, 180));
            }
            int startY = h / 2 - 30;
            g.drawString(startText, (w - fm.stringWidth(startText)) / 2, startY);

            // HELP
            if (sel == 1) {
                int alpha = (int) (Math.sin(System.currentTimeMillis() / 300.0) * 80 + 175);
                alpha = Math.max(0, Math.min(255, alpha));
                g.setColor(new Color(255, 215, 0, alpha));
            } else {
                g.setColor(new Color(180, 180, 180));
            }
            int helpY = h / 2 + 20;
            g.drawString(helpText, (w - fm.stringWidth(helpText)) / 2, helpY);
        }
    }

    private void drawRanking(Graphics2D g, JPanel panel, RealTimeGameData data) {
        Language lang = data.getLanguage();
        int w = panel.getWidth(), h = panel.getHeight();
        g.setColor(new Color(0, 0, 0, 200));
        g.fillRect(0, 0, w, h);
        g.setFont(GameFont.getFont(28f));
        g.setColor(new Color(255, 200, 50));
        String title = Texts.ranking(lang);
        FontMetrics fm = g.getFontMetrics();
        g.drawString(title, (w - fm.stringWidth(title)) / 2, 80);

        List<ScoreRecord> top = scoreService.loadTop(10);
        if (top.isEmpty()) {
            g.setFont(GameFont.getFont(16f));
            g.setColor(new Color(180, 180, 180));
            String empty = Texts.get(lang, "NO RECORDS", "暫無記錄");
            g.drawString(empty, (w - fm.stringWidth(empty)) / 2, h / 2);
        } else {
            g.setFont(GameFont.getFont(11f));
            g.setColor(new Color(200, 200, 220));
            int[] colX = {w/2-220, w/2-130, w/2-30, w/2+40, w/2+110, w/2+190};
            String[] headers = {
                Texts.get(lang,"RANK","排名"), Texts.get(lang,"PLAYER","玩家"),
                Texts.score(lang), Texts.kills(lang), Texts.level(lang), "TIME"
            };
            for (int i = 0; i < headers.length; i++) g.drawString(headers[i], colX[i], 140);
            g.setColor(new Color(100, 100, 120));
            g.drawLine(w/2-230, 150, w/2+230, 150);
            g.setFont(GameFont.getFont(10f));
            int rank = 1;
            for (ScoreRecord r : top) {
                int y = 180 + rank * 32;
                g.setColor(rank <= 3 ? new Color(255,200,50) : new Color(180,180,200));
                g.drawString("#"+rank+"  ", colX[0], y);
                g.drawString(r.getPlayerName(), colX[1], y);
                g.drawString(String.valueOf(r.getScore()), colX[2], y);
                g.drawString(String.valueOf(r.getKills()), colX[3], y);
                g.drawString(r.getLevelName(), colX[4], y);
                g.drawString(r.getDisplayTime(), colX[5], y);
                rank++;
            }
        }
        g.setFont(GameFont.getFont(11f));
        g.setColor(new Color(140, 140, 160));
        String hint = Texts.get(lang, "R to return | ENTER to start", "R 返回 | Enter 開始");
        fm = g.getFontMetrics();
        g.drawString(hint, (w - fm.stringWidth(hint)) / 2, h - 40);
    }
    private void drawGameArea(Graphics2D g, RealTimeGameData data) {
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, GameConstants.GAME_PANEL_WIDTH, GameConstants.GAME_PANEL_HEIGHT);
        drawMap(g, data.getMap());
        drawBombs(g, data.getBombs());
        drawTanks(g, data.getMyTanks(), data.getEnemies());
        drawBullets(g, data.getMyTanks(), data.getEnemies());
        drawHUD(g, data);
    }

    private void drawMap(Graphics2D g, Map map) {
        if (map == null) return;
        for (Brick b : map.getBricks()) if (b.getLive())
            g.drawImage(Images.brickBC, b.getX()-b.getWidth()/2, b.getY()-b.getHeight()/2, b.getWidth(), b.getHeight(), null);
        for (Iron i : map.getIrons()) if (i.getLive())
            g.drawImage(Images.ironBC, i.getX()-i.getWidth()/2, i.getY()-i.getHeight()/2, i.getWidth(), i.getHeight(), null);
        for (Water w : map.getWaters()) if (w.getLive())
            g.drawImage(Images.waterBC, w.getX()-w.getWidth()/2, w.getY()-w.getHeight()/2, w.getWidth(), w.getHeight(), null);
    }

    private void drawBombs(Graphics2D g, java.util.Vector<Bomb> bombs) {
        for (Bomb b : bombs) {
            if (!b.isLive()) continue;
            b.lifeDown();
            if (b.getLifeTime() <= 0) { b.setLive(false); continue; }
            int idx = Math.min(b.getLifeTime()/6, Images.bomb.length-1);
            Image img = Images.bomb[idx];
            if (img != null) g.drawImage(img, b.getX()-b.getL()/2, b.getY()-b.getL()/2, b.getL(), b.getL(), null);
        }
    }

    private void drawTanks(Graphics2D g, java.util.Vector<MyTank> myTanks, java.util.Vector<EnemyTank> enemies) {
        for (EnemyTank e : enemies) if (e.getLive()) drawTank(g, e, Images.enemyTankImg);
        for (MyTank m : myTanks) if (m.getLive()) drawTank(g, m, Images.myTankImg);
    }

    private void drawTank(Graphics2D g, Tank tank, Image[] images) {
        int idx = tank.getDirect().getKey();
        if (idx < 0 || idx >= images.length || images[idx] == null) return;
        g.drawImage(images[idx], tank.getX()-tank.getWidth()/2, tank.getY()-tank.getHeight()/2, tank.getWidth(), tank.getHeight(), null);
        boolean isEnemy = tank.getTankType() == com.course.tankbattle.enums.TankTypeEnum.ENEMY;
        drawHealthBar(g, tank, isEnemy);
    }

    private void drawHealthBar(Graphics2D g, Tank tank, boolean isEnemy) {
        int bw = tank.getWidth(), bh = 4;
        int x = tank.getX() - bw/2, y = tank.getY() - tank.getHeight()/2 - 8;
        int max = 10, blood = Math.max(0, Math.min(tank.getBlood()==null?0:tank.getBlood(), max));
        float ratio = max > 0 ? (float)blood/max : 0;
        g.setColor(new Color(40,40,40,200));
        g.fillRect(x, y, bw, bh);
        if (ratio > 0) {
            Color c;
            if (isEnemy) {
                c = ratio>0.6f ? new Color(240,100,100) : ratio>0.3f ? new Color(220,60,60) : new Color(180,30,30);
            } else {
                c = ratio>0.6f ? new Color(80,220,80) : ratio>0.3f ? new Color(240,220,40) : new Color(240,60,60);
            }
            g.setColor(c);
            g.fillRect(x, y, (int)(bw*ratio), bh);
        }
        g.setColor(new Color(180,180,180,150));
        g.drawRect(x, y, bw, bh);
    }

    private void drawBullets(Graphics2D g, java.util.Vector<MyTank> myTanks, java.util.Vector<EnemyTank> enemies) {
        for (MyTank t : myTanks) for (Bullet b : t.getBullets()) if (b.isLive())
            g.drawImage(Images.bullet, b.getX()-b.getWidth()/2, b.getY()-b.getHeight()/2, b.getWidth(), b.getHeight(), null);
        for (EnemyTank t : enemies) for (Bullet b : t.getBullets()) if (b.isLive())
            g.drawImage(Images.bullet, b.getX()-b.getWidth()/2, b.getY()-b.getHeight()/2, b.getWidth(), b.getHeight(), null);
    }

        private void drawHUD(Graphics2D g, RealTimeGameData data) {
        Language lang = data.getLanguage();
        g.setColor(new Color(30,30,35));
        g.fillRect(GameConstants.GAME_PANEL_WIDTH, 0, 200, GameConstants.GAME_PANEL_HEIGHT);
        g.setColor(new Color(50,50,60));
        g.fillRect(GameConstants.GAME_PANEL_WIDTH, 0, 200, 3);
        int cx = GameConstants.GAME_PANEL_WIDTH + 100, y = 40;
        g.setFont(GameFont.getFont(20f));
        g.setColor(Color.WHITE);
        String st = Texts.stage(lang, data.getLevel().getValue());
        FontMetrics fm = g.getFontMetrics();
        g.drawString(st, cx - fm.stringWidth(st)/2, y); y += 35;
        g.setColor(new Color(60,60,70));
        g.fillRect(GameConstants.GAME_PANEL_WIDTH+20, y-10, 160, 1);
        y += 5;
        int colX = GameConstants.GAME_PANEL_WIDTH + 20;
        int valX = GameConstants.GAME_PANEL_WIDTH + 140;
        int lh = 27;
        drawInfo(g, colX, valX, y, Texts.enemy(lang), String.valueOf(data.getEnemyTankNum()), new Color(200,180,180)); y += lh;
        drawInfo(g, colX, valX, y, Texts.kills(lang), String.valueOf(GameConstants.INIT_ENEMY_TANK_NUM - data.getEnemyTankNum()), new Color(200,200,180)); y += lh;
        drawInfo(g, colX, valX, y, Texts.player(lang), String.valueOf(data.getMyTankNum()), new Color(180,200,180)); y += lh;
        drawInfo(g, colX, valX, y, Texts.bullets(lang), String.valueOf(data.getMyBulletNum()), new Color(180,180,200)); y += lh;
        drawInfo(g, colX, valX, y, Texts.time(lang), data.getElapsedSeconds() + "s", new Color(200,200,200)); y += lh + 8;
        g.setFont(GameFont.getFont(11f));
        g.setColor(new Color(180,180,200));
        String sl = Texts.score(lang);
        fm = g.getFontMetrics();
        g.drawString(sl, cx - fm.stringWidth(sl)/2, y); y += 20;
        g.setFont(GameFont.getFont(22f));
        g.setColor(new Color(255,215,0));
        int sc = Math.max(0, (GameConstants.INIT_ENEMY_TANK_NUM - data.getEnemyTankNum()) * 100);
        String ss = String.valueOf(sc);
        fm = g.getFontMetrics();
        g.drawString(ss, cx - fm.stringWidth(ss)/2, y);
        if (data.isStop()) {
            y += 40;
            g.setFont(GameFont.getFont(16f));
            g.setColor(new Color(255,200,50));
            String pt = Texts.get(lang,"PAUSED","暫停");
            fm = g.getFontMetrics();
            g.drawString(pt, cx - fm.stringWidth(pt)/2, y);
        }
    }

    private void drawInfo(Graphics2D g, int colX, int valX, int y, String label, String value, Color labelColor) {
        g.setFont(GameFont.getFont(10f));
        g.setColor(labelColor);
        g.drawString(label, colX, y);
        g.setFont(GameFont.getFont(13f));
        g.setColor(Color.WHITE);
        FontMetrics fm = g.getFontMetrics();
        g.drawString(value, valX - fm.stringWidth(value), y);
    }

    private void drawSettlement(Graphics2D g, JPanel panel, RealTimeGameData data) {
        Language lang = data.getLanguage();
        int w = GameConstants.GAME_PANEL_WIDTH, h = GameConstants.GAME_PANEL_HEIGHT;
        g.setColor(new Color(0,0,0,200));
        g.fillRect(0, 0, w, h);
        boolean victory = data.isVictory();
        g.setFont(GameFont.getFont(32f));
        g.setColor(victory ? new Color(255,215,0) : new Color(255,80,80));
        String title = victory ? Texts.stageClear(lang) : Texts.gameOver(lang);
        FontMetrics fm = g.getFontMetrics();
        g.drawString(title, (w - fm.stringWidth(title))/2, 100);
        Image ri = victory ? Images.gameWin : Images.gameOver;
        if (ri != null) {
            int dy = data.getDy(); if (dy > 80) { dy -= 8; data.setDy(dy); }
            g.drawImage(ri, w/2-30, dy, 60, 60, panel);
        }
        ScoreRecord r = data.getLastScoreRecord();
        if (r == null) return;
        g.setFont(GameFont.getFont(13f));
        g.setColor(new Color(200,200,200));
        int y = 160, ih = 28, lx = w/2-90;
        g.drawString(Texts.score(lang)+":  "+r.getScore(), lx, y); y+=ih;
        g.drawString(Texts.kills(lang)+":  "+r.getKills()+"/"+GameConstants.INIT_ENEMY_TANK_NUM, lx, y); y+=ih;
        g.drawString(Texts.bullets(lang)+":  "+r.getUsedBullets(), lx, y); y+=ih;
        g.drawString(Texts.lives(lang)+":  "+r.getRemainingLives(), lx, y); y+=ih;
        g.drawString(Texts.time(lang)+":  "+r.getElapsedSeconds()+"s", lx, y); y+=ih;
        g.drawString(Texts.level(lang)+":  "+r.getLevelName(), lx, y); y+=ih+10;
        if (victory) {
            LevelEnum nx = LevelEnum.nextLevel(data.getLevel());
            if (nx != LevelEnum.FIRST_LEVEL) {
                g.setFont(GameFont.getFont(13f)); g.setColor(new Color(180,220,180));
                g.drawString(Texts.nextStage(lang)+"  "+nx.getName(), lx, y); y+=ih;
            }
        }
        g.setFont(GameFont.getFont(11f));
        g.setColor(new Color(140,140,160));
        String bottom = victory ? Texts.get(lang,"ENTER to continue","Enter 下一關") : Texts.get(lang,"PRESS ENTER","按 Enter 繼續");
        fm = g.getFontMetrics();
        g.drawString(bottom, (w-fm.stringWidth(bottom))/2, h-40);
    }

    private void drawPanelObjects(Graphics2D g, RealTimeGameData data) {
        if (data.getMapMakingMode()) drawMapEditOverlay(g, data);
    }

    private void drawMapEditOverlay(Graphics2D g, RealTimeGameData data) {
        Language lang = data.getLanguage();
        g.setColor(new Color(255,255,255,100));
        g.setFont(GameFont.getFont(12f));
        int hudX = GameConstants.GAME_PANEL_WIDTH + 10, y = 320;
        g.drawString(Texts.get(lang,"MAP EDITOR","地圖編輯"), hudX+10, y);
        g.setFont(GameFont.getFont(9f));
        g.setColor(new Color(200,200,220));
        g.drawString(Texts.get(lang,"Current: ","當前: ")+data.getCurrentSelectedStuff().getName(), hudX+10, y+20);
        g.drawString(Texts.get(lang,"C toggle | Right-click delete","C 切換 | 右鍵刪除"), hudX+10, y+40);
        g.drawString(Texts.get(lang,"Save: Menu > Save","保存: 選單-保存地圖"), hudX+10, y+60);
    }
}






