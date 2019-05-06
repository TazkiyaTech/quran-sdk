package com.tazkiyatech.quran.sdk.model;

/**
 * Metadata which describes a chapter.
 */
public class ChapterMetadata {

    /**
     * The type of the chapter.
     */
    private final String chapterType;

    /**
     * The number of this chapter within its chapter type.
     */
    private final int chapterNumber;

    /**
     * The number of Ayahs that this chapter consists of.
     */
    private final int numAyahs;

    /**
     * The Surah number of the first verse in this chapter.
     */
    private final int surahNumber;

    /**
     * The Ayah number of the first verse in this chapter (relative to the Surah).
     */
    private final int ayahNumber;

    public ChapterMetadata(String chapterType,
                           int chapterNumber,
                           int numAyahs,
                           int surahNumber,
                           int ayahNumber) {
        this.chapterType = chapterType;
        this.chapterNumber = chapterNumber;
        this.numAyahs = numAyahs;
        this.surahNumber = surahNumber;
        this.ayahNumber = ayahNumber;
    }

    public String getChapterType() {
        return chapterType;
    }

    public int getChapterNumber() {
        return chapterNumber;
    }

    public int getNumAyahs() {
        return numAyahs;
    }

    public int getSurahNumber() {
        return surahNumber;
    }

    public int getAyahNumber() {
        return ayahNumber;
    }
}
