package com.course.tankbattle.service;

import com.course.tankbattle.constant.GameConstants;
import com.course.tankbattle.dto.RealTimeGameData;
import com.course.tankbattle.dto.ScoreRecord;
import com.course.tankbattle.enums.LevelEnum;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

public class ScoreServiceTest {

    @Test
    public void testCalculateScoreMinimum() {
        ScoreService service = new ScoreService();
        RealTimeGameData data = new RealTimeGameData();
        // Worst case: 0 kills, 0 bullets, all lives gone, max elapsed time
        data.setEnemyTankNum(GameConstants.INIT_ENEMY_TANK_NUM); // 0 kills
        data.setMyBulletNum(0);
        data.setMyTankNum(0);
        int elapsedSeconds = 10000;
        int score = service.calculateScore(data, elapsedSeconds);
        // Score should never be less than 0
        Assert.assertTrue("Score should be >= 0, but got " + score, score >= 0);
    }

    @Test
    public void testCalculateScoreHighScore() {
        ScoreService service = new ScoreService();
        RealTimeGameData data = new RealTimeGameData();
        // Best case: all enemies killed, many bullets remaining
        data.setEnemyTankNum(0); // all 8 killed
        data.setMyBulletNum(GameConstants.MY_TANK_INIT_BULLET_NUM); // all bullets remaining (unused)
        data.setMyTankNum(GameConstants.INIT_MY_TANK_NUM); // all lives remaining
        int elapsedSeconds = 30; // fast victory
        int score = service.calculateScore(data, elapsedSeconds);
        // Should have a high positive score
        Assert.assertTrue("Score should be positive for optimal play, but got " + score, score > 500);
    }

    @Test
    public void testBuildRecord() {
        ScoreService service = new ScoreService();
        RealTimeGameData data = new RealTimeGameData();
        data.setLevel(LevelEnum.FIRST_LEVEL);
        data.setEnemyTankNum(GameConstants.INIT_ENEMY_TANK_NUM - 5); // 5 kills
        data.setMyBulletNum(450); // used 50 bullets
        data.setMyTankNum(3); // lost 1 life

        long startAt = System.currentTimeMillis() - 60000; // 1 min ago
        data.setLevelStartAt(startAt);

        ScoreRecord record = service.buildRecord(data, true);
        Assert.assertNotNull(record);
        Assert.assertEquals("胜利", record.getResult());
        Assert.assertEquals("第一关", record.getLevelName());
        Assert.assertEquals(5, record.getKills());
        Assert.assertEquals(50, record.getUsedBullets());
        Assert.assertEquals(3, record.getRemainingLives());
        Assert.assertTrue("Elapsed should be ~60 seconds", record.getElapsedSeconds() >= 55 && record.getElapsedSeconds() <= 65);
    }

    @Test
    public void testScoreRecordJson() {
        ScoreRecord record = new ScoreRecord("胜利", LevelEnum.FIRST_LEVEL, 1500, 8, 50, 4, 45);
        String json = record.toJson();
        Assert.assertTrue(json.contains("\"score\":1500"));
        Assert.assertTrue(json.contains("\"kills\":8"));
        Assert.assertTrue(json.contains("\"result\":\"胜利\""));
        Assert.assertTrue(json.contains("\"levelName\":\"第一关\""));
    }

    @Test
    public void testScoreRecordBoundary() {
        ScoreRecord record = new ScoreRecord("失败", LevelEnum.FIRST_LEVEL, 0, 0, 500, 0, 9999);
        String json = record.toJson();
        Assert.assertTrue(json.contains("\"score\":0"));
        Assert.assertTrue(json.contains("\"kills\":0"));
    }
}
