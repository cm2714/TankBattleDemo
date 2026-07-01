package com.course.tankbattle.util;

import com.course.tankbattle.enums.Language;

public final class Texts {
    private Texts() {}

    public static String get(Language lang, String en, String zh) {
        return lang == Language.ENGLISH ? en : zh;
    }

    public static String stage(Language lang, int level) {
        return lang == Language.ENGLISH ? "STAGE " + level : "第" + level + "關";
    }

    public static String enemy(Language lang) {
        return get(lang, "ENEMY", "敵軍");
    }

    public static String player(Language lang) {
        return get(lang, "PLAYER", "玩家");
    }

    public static String score(Language lang) {
        return get(lang, "SCORE", "得分");
    }

    public static String kills(Language lang) {
        return get(lang, "KILLS", "擊殺");
    }

    public static String bullets(Language lang) {
        return get(lang, "BULLETS", "用彈");
    }

    public static String lives(Language lang) {
        return get(lang, "LIVES", "剩餘生命");
    }

    public static String time(Language lang) {
        return get(lang, "TIME", "耗時");
    }

    public static String level(Language lang) {
        return get(lang, "LEVEL", "關卡");
    }

    public static String stageClear(Language lang) {
        return get(lang, "STAGE CLEAR", "勝利");
    }

    public static String gameOver(Language lang) {
        return get(lang, "GAME OVER", "失敗");
    }

    public static String nextStage(Language lang) {
        return get(lang, "NEXT STAGE:", "下一關");
    }

    public static String pressEnter(Language lang) {
        return get(lang, "PRESS ENTER", "按 Enter 繼續");
    }

    public static String ranking(Language lang) {
        return get(lang, "RANKING", "排行榜");
    }

    public static String language(Language lang) {
        return get(lang, "Language: ", "語言: ");
    }
}

