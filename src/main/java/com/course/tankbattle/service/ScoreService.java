package com.course.tankbattle.service;

import com.course.tankbattle.constant.GameConstants;
import com.course.tankbattle.dto.RealTimeGameData;
import com.course.tankbattle.dto.ScoreRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class ScoreService {
    private static final Logger LOGGER = LoggerFactory.getLogger(ScoreService.class);
    private static final int MAX_RECORDS = 50;
    private static final Pattern OBJECT_PATTERN = Pattern.compile("\\{[^}]*}");

    private static final String[] NAMES = {
        "Rachel", "Monica", "Phoebe", "Chandler", "Joey", "Ross",
        "Michael", "Jim", "Pam", "Dwight", "Angela", "Oscar",
        "Jerry", "Elaine", "George", "Kramer",
        "Walter", "Jesse", "Skyler", "Gus", "Mike",
        "Eleven", "Mike", "Dustin", "Lucas", "Will", "Maxine",
        "Homer", "Marge", "Bart", "Lisa", "Maggie",
        "Arya", "Jon", "Tyrion", "Sansa", "Daenerys",
        "Sheldon", "Leonard", "Penny", "Howard", "Raj",
        "Ted", "Robin", "Barney", "Lily", "Marshall",
        "Phil", "Claire", "Jay", "Gloria", "Mitchell", "Cameron",
        "Carrie", "Samantha", "Charlotte", "Miranda",
        "Dexter", "Debra", "Harry", "Donna", "Mad Men",
        "Don", "Peggy", "Roger", "Joan", "Pete",
        "Jack", "Kate", "Sawyer", "Hurley", "Desmond",
        "Tony", "Carmela", "Christopher", "Paulie",
        "Nucky", "Jimmy", "Margaret", "Richard",
        "Frank", "Karen", "Brendan", "Fiona",
        "Liz", "Red", "Dembe", "Ressler",
        "Meredith", "Derek", "Cristina", "Alex", "April",
        "Leslie", "Ron", "April", "Andy", "Tom",
        "Jeff", "Abed", "Britta", "Shirley", "Pierce",
        "Lorelai", "Rory", "Luke", "Emily", "Richard"
    };
    private static final java.util.Random NAME_RANDOM = new java.util.Random();

    public int calculateScore(RealTimeGameData data, int elapsedSeconds) {
        int kills = GameConstants.INIT_ENEMY_TANK_NUM - data.getEnemyTankNum();
        int usedBullets = GameConstants.MY_TANK_INIT_BULLET_NUM - data.getMyBulletNum();
        int score = kills * 100 + data.getMyTankNum() * 200 + data.getMyBulletNum() / 5 - elapsedSeconds;
        return Math.max(score, 0);
    }

    public ScoreRecord buildRecord(RealTimeGameData data, boolean victory) {
        int elapsedSeconds = data.getElapsedSeconds();
        int score = calculateScore(data, elapsedSeconds);
        int kills = GameConstants.INIT_ENEMY_TANK_NUM - data.getEnemyTankNum();
        int usedBullets = GameConstants.MY_TANK_INIT_BULLET_NUM - data.getMyBulletNum();
        ScoreRecord record = new ScoreRecord(victory ? "胜利" : "失败", data.getLevel(), score, kills, usedBullets, data.getMyTankNum(), elapsedSeconds);
        record.setPlayerName(NAMES[NAME_RANDOM.nextInt(NAMES.length)]);
        return record;
    }

    public void save(ScoreRecord record) {
        List<ScoreRecord> records = loadAll();
        records.add(record);
        sort(records);
        while (records.size() > MAX_RECORDS) {
            records.remove(records.size() - 1);
        }
        File file = getRankingFile();
        File parent = file.getParentFile();
        if (!parent.exists() && !parent.mkdirs()) {
            LOGGER.warn("Cannot create ranking directory: {}", parent.getAbsolutePath());
            return;
        }
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.write("[\n");
            for (int i = 0; i < records.size(); i++) {
                writer.write("  ");
                writer.write(records.get(i).toJson());
                if (i < records.size() - 1) {
                    writer.write(",");
                }
                writer.write("\n");
            }
            writer.write("]\n");
        } catch (Exception e) {
            LOGGER.warn("Save ranking failed", e);
        }
    }

    public List<ScoreRecord> loadTop(int limit) {
        List<ScoreRecord> records = loadAll();
        sort(records);
        return records.subList(0, Math.min(limit, records.size()));
    }

    public List<ScoreRecord> loadAll() {
        File file = getRankingFile();
        List<ScoreRecord> records = new ArrayList<>();
        if (!file.exists()) {
            return records;
        }
        StringBuilder json = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                json.append(line);
            }
        } catch (Exception e) {
            LOGGER.warn("Read ranking failed", e);
            return records;
        }
        Matcher matcher = OBJECT_PATTERN.matcher(json.toString());
        while (matcher.find()) {
            records.add(parseRecord(matcher.group()));
        }
        sort(records);
        return records;
    }

    private ScoreRecord parseRecord(String json) {
        ScoreRecord record = new ScoreRecord();
        record.setPlayerName(readString(json, "playerName", "Player"));
        record.setResult(readString(json, "result", ""));
        record.setLevelName(readString(json, "levelName", ""));
        record.setScore(readInt(json, "score", 0));
        record.setKills(readInt(json, "kills", 0));
        record.setUsedBullets(readInt(json, "usedBullets", 0));
        record.setRemainingLives(readInt(json, "remainingLives", 0));
        record.setElapsedSeconds(readInt(json, "elapsedSeconds", 0));
        record.setCreatedAt(readLong(json, "createdAt", System.currentTimeMillis()));
        return record;
    }

    private String readString(String json, String key, String defaultValue) {
        Matcher matcher = Pattern.compile("\"" + key + "\"\\s*:\\s*\"([^\"]*)\"").matcher(json);
        return matcher.find() ? matcher.group(1) : defaultValue;
    }

    private int readInt(String json, String key, int defaultValue) {
        return (int) readLong(json, key, defaultValue);
    }

    private long readLong(String json, String key, long defaultValue) {
        Matcher matcher = Pattern.compile("\"" + key + "\"\\s*:\\s*(-?\\d+)").matcher(json);
        return matcher.find() ? Long.parseLong(matcher.group(1)) : defaultValue;
    }

    private void sort(List<ScoreRecord> records) {
        Collections.sort(records, new Comparator<ScoreRecord>() {
            @Override
            public int compare(ScoreRecord a, ScoreRecord b) {
                int scoreCompare = Integer.compare(b.getScore(), a.getScore());
                return scoreCompare != 0 ? scoreCompare : Long.compare(b.getCreatedAt(), a.getCreatedAt());
            }
        });
    }

    private File getRankingFile() {
        return new File(System.getProperty("user.home") + File.separator + ".tankBattle" + File.separator + "ranking.json");
    }
}
