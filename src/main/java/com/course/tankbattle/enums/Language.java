package com.course.tankbattle.enums;

public enum Language {
    ENGLISH("English"),
    CHINESE("中文");

    private final String displayName;

    Language(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public Language toggle() {
        return this == ENGLISH ? CHINESE : ENGLISH;
    }
}
