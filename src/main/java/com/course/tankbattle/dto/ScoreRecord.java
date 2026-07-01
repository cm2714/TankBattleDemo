package com.course.tankbattle.dto;

import com.course.tankbattle.enums.LevelEnum;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ScoreRecord {
    private String playerName = "Player";
    private String result;
    private String levelName;
    private int score;
    private int kills;
    private int usedBullets;
    private int remainingLives;
    private int elapsedSeconds;
    private long createdAt;

    public ScoreRecord() {
    }

    public ScoreRecord(String result, LevelEnum level, int score, int kills, int usedBullets, int remainingLives, int elapsedSeconds) {
        this.result = result;
        this.levelName = level.getName();
        this.score = score;
        this.kills = kills;
        this.usedBullets = usedBullets;
        this.remainingLives = remainingLives;
        this.elapsedSeconds = elapsedSeconds;
        this.createdAt = System.currentTimeMillis();
    }

    public String getDisplayTime() {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm").format(new Date(createdAt));
    }

    public String toJson() {
        return "{" +
                "\"playerName\":\"" + escape(playerName) + "\"," +
                "\"result\":\"" + escape(result) + "\"," +
                "\"levelName\":\"" + escape(levelName) + "\"," +
                "\"score\":" + score + "," +
                "\"kills\":" + kills + "," +
                "\"usedBullets\":" + usedBullets + "," +
                "\"remainingLives\":" + remainingLives + "," +
                "\"elapsedSeconds\":" + elapsedSeconds + "," +
                "\"createdAt\":" + createdAt +
                "}";
    }

    private String escape(String value) {
        return value == null ? "" : value.replace("\\", "\\\\").replace("\"", "\\\"");
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getLevelName() {
        return levelName;
    }

    public void setLevelName(String levelName) {
        this.levelName = levelName;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getKills() {
        return kills;
    }

    public void setKills(int kills) {
        this.kills = kills;
    }

    public int getUsedBullets() {
        return usedBullets;
    }

    public void setUsedBullets(int usedBullets) {
        this.usedBullets = usedBullets;
    }

    public int getRemainingLives() {
        return remainingLives;
    }

    public void setRemainingLives(int remainingLives) {
        this.remainingLives = remainingLives;
    }

    public int getElapsedSeconds() {
        return elapsedSeconds;
    }

    public void setElapsedSeconds(int elapsedSeconds) {
        this.elapsedSeconds = elapsedSeconds;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(long createdAt) {
        this.createdAt = createdAt;
    }
}
