package com.course.tankbattle.dto;

import com.course.tankbattle.entity.Bomb;
import com.course.tankbattle.entity.EnemyTank;
import com.course.tankbattle.entity.MyTank;
import com.course.tankbattle.enums.Language;
import com.course.tankbattle.enums.LevelEnum;
import com.course.tankbattle.enums.StuffTypeEnum;
import com.course.tankbattle.resource.map.Map;

import java.util.Vector;

public class RealTimeGameData {
    private Vector<MyTank> myTanks = new Vector<>();
    private Vector<EnemyTank> enemies = new Vector<>();
    private Vector<Bomb> bombs = new Vector<>();
    private ScoreRecord lastScoreRecord;

    private Boolean mapMakingMode = Boolean.FALSE;
    private StuffTypeEnum currentSelectedStuff = StuffTypeEnum.BRICK;

    private Map map;

    private int enemyTankNum;
    private int myTankNum;
    private int beKilled;
    private int myBulletNum;
    private boolean isStart = false;
    private boolean startScreen = true;

    private boolean isStop = false;
    private boolean isExit = false;
    private boolean settlement = false;
    private boolean victory = false;
    private boolean showRanking = false;
    private int fontY = 700;
    private Language language = Language.ENGLISH;
    private int startMenuSelection = 0;
    private long levelStartAt = System.currentTimeMillis();
    private long settlementAt;

    private boolean up = false;
    private boolean down = false;
    private boolean left = false;
    private boolean right = false;

    private LevelEnum level = LevelEnum.FIRST_LEVEL;

    private boolean iconSmile;
    private int dy = 600;
    private int ky = 600;
    private int kx = 0;

    public void keyPressedDirect(Boolean up, Boolean down, Boolean left, Boolean right) {
        this.up = up; this.down = down; this.left = left; this.right = right;
    }

    public void clear() {
        enemies.forEach(e -> { e.setLive(Boolean.FALSE); e.setActivate(Boolean.FALSE); e.getTimer().cancel(); });
        myTanks.clear(); enemies.clear(); bombs.clear(); map = null;
    }

    public void clearRuntimeObjects() {
        enemies.forEach(e -> { e.setLive(Boolean.FALSE); e.setActivate(Boolean.FALSE); e.getTimer().cancel(); });
        enemies.clear(); bombs.clear(); myTanks.forEach(t -> t.getBullets().clear());
    }

    public void prepareLevel() {
        settlement = false; victory = false; showRanking = false;
        levelStartAt = System.currentTimeMillis(); settlementAt = 0L; dy = 600;
    }

    public int getElapsedSeconds() {
        long end = settlementAt > 0 ? settlementAt : System.currentTimeMillis();
        return (int) Math.max(0, (end - levelStartAt) / 1000);
    }

    // Getters and setters
    public int getEnemyTankNum() { return enemyTankNum; }
    public void setEnemyTankNum(int n) { enemyTankNum = n; }
    public int getMyTankNum() { return myTankNum; }
    public void setMyTankNum(int n) { myTankNum = n; }
    public int getBeKilled() { return beKilled; }
    public void setBeKilled(int n) { beKilled = n; }
    public int getMyBulletNum() { return myBulletNum; }
    public void setMyBulletNum(int n) { myBulletNum = n; }
    public boolean isStart() { return isStart; }
    public void setStart(boolean s) { isStart = s; }
    public boolean isStartScreen() { return startScreen; }
    public void setStartScreen(boolean s) { startScreen = s; }
    public boolean isStop() { return isStop; }
    public void setStop(boolean s) { isStop = s; }
    public boolean isUp() { return up; }
    public void setUp(boolean u) { up = u; }
    public boolean isDown() { return down; }
    public void setDown(boolean d) { down = d; }
    public boolean isLeft() { return left; }
    public void setLeft(boolean l) { left = l; }
    public boolean isRight() { return right; }
    public void setRight(boolean r) { right = r; }
    public LevelEnum getLevel() { return level; }
    public void setLevel(LevelEnum l) { level = l; }
    public boolean isIconSmile() { return iconSmile; }
    public void setIconSmile(boolean i) { iconSmile = i; }
    public int getDy() { return dy; }
    public void setDy(int d) { dy = d; }
    public int getKy() { return ky; }
    public void setKy(int k) { ky = k; }
    public int getKx() { return kx; }
    public void setKx(int k) { kx = k; }
    public Vector<MyTank> getMyTanks() { return myTanks; }
    public void setMyTanks(Vector<MyTank> v) { myTanks = v; }
    public Vector<EnemyTank> getEnemies() { return enemies; }
    public void setEnemies(Vector<EnemyTank> v) { enemies = v; }
    public Vector<Bomb> getBombs() { return bombs; }
    public void setBombs(Vector<Bomb> v) { bombs = v; }
    public Map getMap() { return map; }
    public void setMap(Map m) { map = m; }
    public boolean isExit() { return isExit; }
    public void setExit(boolean e) { isExit = e; }

    @Override
    public String toString() {
        return "RealTimeGameData{enemyTankNum=" + enemyTankNum + ", myTankNum=" + myTankNum + "}";
    }

    public StuffTypeEnum getCurrentSelectedStuff() { return currentSelectedStuff; }
    public void setCurrentSelectedStuff(StuffTypeEnum s) { currentSelectedStuff = s; }
    public Boolean getMapMakingMode() { return mapMakingMode; }
    public void setMapMakingMode(Boolean m) { mapMakingMode = m; }
    public boolean isSettlement() { return settlement; }
    public void setSettlement(boolean s) { settlement = s; }
    public boolean isVictory() { return victory; }
    public void setVictory(boolean v) { victory = v; }
    public long getSettlementAt() { return settlementAt; }
    public void setSettlementAt(long s) { settlementAt = s; }
    public long getLevelStartAt() { return levelStartAt; }
    public void setLevelStartAt(long l) { levelStartAt = l; }
    public ScoreRecord getLastScoreRecord() { return lastScoreRecord; }
    public void setLastScoreRecord(ScoreRecord s) { lastScoreRecord = s; }
    public boolean isShowRanking() { return showRanking; }
    public void setShowRanking(boolean s) { showRanking = s; }
    public int getFontY() { return fontY; }
    public void setFontY(int f) { fontY = f; }
    public void resetFontY() { fontY = 700; }
    public Language getLanguage() { return language; }
    public void setLanguage(Language lang) { this.language = lang; }
    public void toggleLanguage() { language = language.toggle(); }
    public int getStartMenuSelection() { return startMenuSelection; }
    public void setStartMenuSelection(int s) { startMenuSelection = s; }
}
