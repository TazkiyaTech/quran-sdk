package com.tazkiyatech.quran.sdk.model;

import androidx.annotation.NonNull;

/**
 * An enum representation of the Juzs that make up the Quran.
 *
 * @deprecated Use the {@link com.tazkiyatech.quran.sdk.database.QuranDatabase#getMetadataForChapters(ChapterType)} and {@link com.tazkiyatech.quran.sdk.database.QuranDatabase#getMetadataForChapter(ChapterType, int)} methods instead.
 */
@Deprecated
public enum Juz {

    Juz_01(1, 148, 1, 1),
    Juz_02(2, 111, 2, 142),
    Juz_03(3, 126, 2, 253),
    Juz_04(4, 131, 3, 93),
    Juz_05(5, 124, 4, 24),
    Juz_06(6, 110, 4, 148),
    Juz_07(7, 149, 5, 82),
    Juz_08(8, 142, 6, 111),
    Juz_09(9, 159, 7, 88),
    Juz_10(10, 127, 8, 41),
    Juz_11(11, 151, 9, 93),
    Juz_12(12, 170, 11, 6),
    Juz_13(13, 154, 12, 53),
    Juz_14(14, 227, 15, 1),
    Juz_15(15, 185, 17, 1),
    Juz_16(16, 269, 18, 75),
    Juz_17(17, 190, 21, 1),
    Juz_18(18, 202, 23, 1),
    Juz_19(19, 339, 25, 21),
    Juz_20(20, 171, 27, 56),
    Juz_21(21, 178, 29, 46),
    Juz_22(22, 169, 33, 31),
    Juz_23(23, 357, 36, 28),
    Juz_24(24, 175, 39, 32),
    Juz_25(25, 246, 41, 47),
    Juz_26(26, 195, 46, 1),
    Juz_27(27, 399, 51, 31),
    Juz_28(28, 137, 58, 1),
    Juz_29(29, 431, 67, 1),
    Juz_30(30, 564, 78, 1);

    /**
     * Parses the given Juz number to an instance of {@link Juz}.
     *
     * @param juzNumber is a value between 1 and 30 (inclusive).
     * @return the {@link Juz} corresponding to the {@code juzNumber} provided.
     */
    @NonNull
    public static Juz parse(int juzNumber) {
        Juz[] juzValues = Juz.values();

        if (juzNumber < 1 || juzNumber > juzValues.length) {
            throw new IllegalArgumentException("Bad Juz number passed in.");
        } else {
            return juzValues[juzNumber - 1];
        }
    }

    /**
     * The number (not index) of this Juz.
     */
    private final int juzNumber;

    /**
     * The number of Verses/Ayahs in this Juz.
     */
    private final int numVerses;

    /**
     * The number (not index) of the Surah in which this Juz begins.
     */
    private final int fromSurah;

    /**
     * The number (not index) of the Verse/Ayah within {@link #fromSurah} at which this Juz begins.
     */
    private final int fromVerse;

    /**
     * Constructor.
     */
    Juz(int juzNumber,
        int numVerses,
        int fromSurah,
        int fromVerse) {
        this.juzNumber = juzNumber;
        this.numVerses = numVerses;
        this.fromSurah = fromSurah;
        this.fromVerse = fromVerse;
    }

    /**
     * @return the number (not index) of this Juz.
     */
    public int getJuzNumber() {
        return juzNumber;
    }

    /**
     * @return the number of Verses/Ayahs in this Juz.
     */
    public int getNumVerses() {
        return numVerses;
    }

    /**
     * @return the number (not index) of the Surah in which this Juz begins.
     */
    public int getFromSurah() {
        return fromSurah;
    }

    /**
     * @return the number (not index) of the Verse/Ayah within {@link #fromSurah} at which this Juz begins.
     */
    public int getFromVerse() {
        return fromVerse;
    }
}
