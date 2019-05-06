package com.tazkiyatech.quran.sdk.model;

public enum ChapterType {

    SURAH("sura"),
    JUZ("juz"),
    HIZB("hizb"),
    HIZB_QUARTER("hizb_quarter");

    /**
     * The name of the chapter type in the 'chapter_type' column of the 'quran_metadata' table.
     */
    private final String nameInDatabase;

    ChapterType(String nameInDatabase) {
        this.nameInDatabase = nameInDatabase;
    }

    public String getNameInDatabase() {
        return nameInDatabase;
    }
}
