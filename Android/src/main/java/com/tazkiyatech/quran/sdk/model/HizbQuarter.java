package com.tazkiyatech.quran.sdk.model;

import androidx.annotation.NonNull;

/**
 * An enum representation of the Hizb-Quarters that make up the Quran.
 *
 * @deprecated Use the {@link com.tazkiyatech.quran.sdk.database.QuranDatabase#getMetadataForChapters(ChapterType)} and {@link com.tazkiyatech.quran.sdk.database.QuranDatabase#getMetadataForChapter(ChapterType, int)} methods instead.
 */
@Deprecated
public enum HizbQuarter {

    Hizb_01_Quarter_01(1, 1, 32, 1, 1),
    Hizb_01_Quarter_02(1, 2, 18, 2, 26),
    Hizb_01_Quarter_03(1, 3, 16, 2, 44),
    Hizb_01_Quarter_04(1, 4, 15, 2, 60),
    Hizb_02_Quarter_01(2, 1, 17, 2, 75),
    Hizb_02_Quarter_02(2, 2, 14, 2, 92),
    Hizb_02_Quarter_03(2, 3, 18, 2, 106),
    Hizb_02_Quarter_04(2, 4, 18, 2, 124),
    Hizb_03_Quarter_01(3, 1, 16, 2, 142),
    Hizb_03_Quarter_02(3, 2, 19, 2, 158),
    Hizb_03_Quarter_03(3, 3, 12, 2, 177),
    Hizb_03_Quarter_04(3, 4, 14, 2, 189),
    Hizb_04_Quarter_01(4, 1, 16, 2, 203),
    Hizb_04_Quarter_02(4, 2, 14, 2, 219),
    Hizb_04_Quarter_03(4, 3, 10, 2, 233),
    Hizb_04_Quarter_04(4, 4, 10, 2, 243),
    Hizb_05_Quarter_01(5, 1, 10, 2, 253),
    Hizb_05_Quarter_02(5, 2, 9, 2, 263),
    Hizb_05_Quarter_03(5, 3, 11, 2, 272),
    Hizb_05_Quarter_04(5, 4, 18, 2, 283),
    Hizb_06_Quarter_01(6, 1, 18, 3, 15),
    Hizb_06_Quarter_02(6, 2, 19, 3, 33),
    Hizb_06_Quarter_03(6, 3, 23, 3, 52),
    Hizb_06_Quarter_04(6, 4, 18, 3, 75),
    Hizb_07_Quarter_01(7, 1, 20, 3, 93),
    Hizb_07_Quarter_02(7, 2, 20, 3, 113),
    Hizb_07_Quarter_03(7, 3, 20, 3, 133),
    Hizb_07_Quarter_04(7, 4, 18, 3, 153),
    Hizb_08_Quarter_01(8, 1, 15, 3, 171),
    Hizb_08_Quarter_02(8, 2, 15, 3, 186),
    Hizb_08_Quarter_03(8, 3, 11, 4, 1),
    Hizb_08_Quarter_04(8, 4, 12, 4, 12),
    Hizb_09_Quarter_01(9, 1, 12, 4, 24),
    Hizb_09_Quarter_02(9, 2, 22, 4, 36),
    Hizb_09_Quarter_03(9, 3, 16, 4, 58),
    Hizb_09_Quarter_04(9, 4, 14, 4, 74),
    Hizb_10_Quarter_01(10, 1, 12, 4, 88),
    Hizb_10_Quarter_02(10, 2, 14, 4, 100),
    Hizb_10_Quarter_03(10, 3, 21, 4, 114),
    Hizb_10_Quarter_04(10, 4, 13, 4, 135),
    Hizb_11_Quarter_01(11, 1, 15, 4, 148),
    Hizb_11_Quarter_02(11, 2, 14, 4, 163),
    Hizb_11_Quarter_03(11, 3, 11, 5, 1),
    Hizb_11_Quarter_04(11, 4, 15, 5, 12),
    Hizb_12_Quarter_01(12, 1, 14, 5, 27),
    Hizb_12_Quarter_02(12, 2, 10, 5, 41),
    Hizb_12_Quarter_03(12, 3, 16, 5, 51),
    Hizb_12_Quarter_04(12, 4, 15, 5, 67),
    Hizb_13_Quarter_01(13, 1, 15, 5, 82),
    Hizb_13_Quarter_02(13, 2, 12, 5, 97),
    Hizb_13_Quarter_03(13, 3, 24, 5, 109),
    Hizb_13_Quarter_04(13, 4, 23, 6, 13),
    Hizb_14_Quarter_01(14, 1, 23, 6, 36),
    Hizb_14_Quarter_02(14, 2, 15, 6, 59),
    Hizb_14_Quarter_03(14, 3, 21, 6, 74),
    Hizb_14_Quarter_04(14, 4, 16, 6, 95),
    Hizb_15_Quarter_01(15, 1, 16, 6, 111),
    Hizb_15_Quarter_02(15, 2, 14, 6, 127),
    Hizb_15_Quarter_03(15, 3, 10, 6, 141),
    Hizb_15_Quarter_04(15, 4, 15, 6, 151),
    Hizb_16_Quarter_01(16, 1, 30, 7, 1),
    Hizb_16_Quarter_02(16, 2, 16, 7, 31),
    Hizb_16_Quarter_03(16, 3, 18, 7, 47),
    Hizb_16_Quarter_04(16, 4, 23, 7, 65),
    Hizb_17_Quarter_01(17, 1, 29, 7, 88),
    Hizb_17_Quarter_02(17, 2, 25, 7, 117),
    Hizb_17_Quarter_03(17, 3, 14, 7, 142),
    Hizb_17_Quarter_04(17, 4, 15, 7, 156),
    Hizb_18_Quarter_01(18, 1, 18, 7, 171),
    Hizb_18_Quarter_02(18, 2, 18, 7, 189),
    Hizb_18_Quarter_03(18, 3, 21, 8, 1),
    Hizb_18_Quarter_04(18, 4, 19, 8, 22),
    Hizb_19_Quarter_01(19, 1, 20, 8, 41),
    Hizb_19_Quarter_02(19, 2, 15, 8, 61),
    Hizb_19_Quarter_03(19, 3, 18, 9, 1),
    Hizb_19_Quarter_04(19, 4, 15, 9, 19),
    Hizb_20_Quarter_01(20, 1, 12, 9, 34),
    Hizb_20_Quarter_02(20, 2, 14, 9, 46),
    Hizb_20_Quarter_03(20, 3, 15, 9, 60),
    Hizb_20_Quarter_04(20, 4, 18, 9, 75),
    Hizb_21_Quarter_01(21, 1, 18, 9, 93),
    Hizb_21_Quarter_02(21, 2, 11, 9, 111),
    Hizb_21_Quarter_03(21, 3, 18, 9, 122),
    Hizb_21_Quarter_04(21, 4, 15, 10, 11),
    Hizb_22_Quarter_01(22, 1, 27, 10, 26),
    Hizb_22_Quarter_02(22, 2, 18, 10, 53),
    Hizb_22_Quarter_03(22, 3, 19, 10, 71),
    Hizb_22_Quarter_04(22, 4, 25, 10, 90),
    Hizb_23_Quarter_01(23, 1, 18, 11, 6),
    Hizb_23_Quarter_02(23, 2, 17, 11, 24),
    Hizb_23_Quarter_03(23, 3, 20, 11, 41),
    Hizb_23_Quarter_04(23, 4, 23, 11, 61),
    Hizb_24_Quarter_01(24, 1, 24, 11, 84),
    Hizb_24_Quarter_02(24, 2, 22, 11, 108),
    Hizb_24_Quarter_03(24, 3, 23, 12, 7),
    Hizb_24_Quarter_04(24, 4, 23, 12, 30),
    Hizb_25_Quarter_01(25, 1, 24, 12, 53),
    Hizb_25_Quarter_02(25, 2, 24, 12, 77),
    Hizb_25_Quarter_03(25, 3, 15, 12, 101),
    Hizb_25_Quarter_04(25, 4, 14, 13, 5),
    Hizb_26_Quarter_01(26, 1, 16, 13, 19),
    Hizb_26_Quarter_02(26, 2, 18, 13, 35),
    Hizb_26_Quarter_03(26, 3, 18, 14, 10),
    Hizb_26_Quarter_04(26, 4, 25, 14, 28),
    Hizb_27_Quarter_01(27, 1, 48, 15, 1),
    Hizb_27_Quarter_02(27, 2, 51, 15, 49),
    Hizb_27_Quarter_03(27, 3, 29, 16, 1),
    Hizb_27_Quarter_04(27, 4, 21, 16, 30),
    Hizb_28_Quarter_01(28, 1, 24, 16, 51),
    Hizb_28_Quarter_02(28, 2, 15, 16, 75),
    Hizb_28_Quarter_03(28, 3, 21, 16, 90),
    Hizb_28_Quarter_04(28, 4, 18, 16, 111),
    Hizb_29_Quarter_01(29, 1, 22, 17, 1),
    Hizb_29_Quarter_02(29, 2, 27, 17, 23),
    Hizb_29_Quarter_03(29, 3, 20, 17, 50),
    Hizb_29_Quarter_04(29, 4, 29, 17, 70),
    Hizb_30_Quarter_01(30, 1, 29, 17, 99),
    Hizb_30_Quarter_02(30, 2, 15, 18, 17),
    Hizb_30_Quarter_03(30, 3, 19, 18, 32),
    Hizb_30_Quarter_04(30, 4, 24, 18, 51),
    Hizb_31_Quarter_01(31, 1, 24, 18, 75),
    Hizb_31_Quarter_02(31, 2, 33, 18, 99),
    Hizb_31_Quarter_03(31, 3, 37, 19, 22),
    Hizb_31_Quarter_04(31, 4, 40, 19, 59),
    Hizb_32_Quarter_01(32, 1, 54, 20, 1),
    Hizb_32_Quarter_02(32, 2, 28, 20, 55),
    Hizb_32_Quarter_03(32, 3, 28, 20, 83),
    Hizb_32_Quarter_04(32, 4, 25, 20, 111),
    Hizb_33_Quarter_01(33, 1, 28, 21, 1),
    Hizb_33_Quarter_02(33, 2, 22, 21, 29),
    Hizb_33_Quarter_03(33, 3, 32, 21, 51),
    Hizb_33_Quarter_04(33, 4, 30, 21, 83),
    Hizb_34_Quarter_01(34, 1, 18, 22, 1),
    Hizb_34_Quarter_02(34, 2, 19, 22, 19),
    Hizb_34_Quarter_03(34, 3, 22, 22, 38),
    Hizb_34_Quarter_04(34, 4, 19, 22, 60),
    Hizb_35_Quarter_01(35, 1, 35, 23, 1),
    Hizb_35_Quarter_02(35, 2, 39, 23, 36),
    Hizb_35_Quarter_03(35, 3, 44, 23, 75),
    Hizb_35_Quarter_04(35, 4, 20, 24, 1),
    Hizb_36_Quarter_01(36, 1, 14, 24, 21),
    Hizb_36_Quarter_02(36, 2, 18, 24, 35),
    Hizb_36_Quarter_03(36, 3, 12, 24, 53),
    Hizb_36_Quarter_04(36, 4, 20, 25, 1),
    Hizb_37_Quarter_01(37, 1, 32, 25, 21),
    Hizb_37_Quarter_02(37, 2, 25, 25, 53),
    Hizb_37_Quarter_03(37, 3, 51, 26, 1),
    Hizb_37_Quarter_04(37, 4, 59, 26, 52),
    Hizb_38_Quarter_01(38, 1, 70, 26, 111),
    Hizb_38_Quarter_02(38, 2, 47, 26, 181),
    Hizb_38_Quarter_03(38, 3, 26, 27, 1),
    Hizb_38_Quarter_04(38, 4, 29, 27, 27),
    Hizb_39_Quarter_01(39, 1, 26, 27, 56),
    Hizb_39_Quarter_02(39, 2, 23, 27, 82),
    Hizb_39_Quarter_03(39, 3, 17, 28, 12),
    Hizb_39_Quarter_04(39, 4, 22, 28, 29),
    Hizb_40_Quarter_01(40, 1, 25, 28, 51),
    Hizb_40_Quarter_02(40, 2, 13, 28, 76),
    Hizb_40_Quarter_03(40, 3, 25, 29, 1),
    Hizb_40_Quarter_04(40, 4, 20, 29, 26),
    Hizb_41_Quarter_01(41, 1, 24, 29, 46),
    Hizb_41_Quarter_02(41, 2, 30, 30, 1),
    Hizb_41_Quarter_03(41, 3, 23, 30, 31),
    Hizb_41_Quarter_04(41, 4, 28, 30, 54),
    Hizb_42_Quarter_01(42, 1, 23, 31, 22),
    Hizb_42_Quarter_02(42, 2, 20, 32, 11),
    Hizb_42_Quarter_03(42, 3, 17, 33, 1),
    Hizb_42_Quarter_04(42, 4, 13, 33, 18),
    Hizb_43_Quarter_01(43, 1, 20, 33, 31),
    Hizb_43_Quarter_02(43, 2, 9, 33, 51),
    Hizb_43_Quarter_03(43, 3, 23, 33, 60),
    Hizb_43_Quarter_04(43, 4, 14, 34, 10),
    Hizb_44_Quarter_01(44, 1, 22, 34, 24),
    Hizb_44_Quarter_02(44, 2, 23, 34, 46),
    Hizb_44_Quarter_03(44, 3, 26, 35, 15),
    Hizb_44_Quarter_04(44, 4, 32, 35, 41),
    Hizb_45_Quarter_01(45, 1, 32, 36, 28),
    Hizb_45_Quarter_02(45, 2, 45, 36, 60),
    Hizb_45_Quarter_03(45, 3, 61, 37, 22),
    Hizb_45_Quarter_04(45, 4, 62, 37, 83),
    Hizb_46_Quarter_01(46, 1, 58, 37, 145),
    Hizb_46_Quarter_02(46, 2, 31, 38, 21),
    Hizb_46_Quarter_03(46, 3, 44, 38, 52),
    Hizb_46_Quarter_04(46, 4, 24, 39, 8),
    Hizb_47_Quarter_01(47, 1, 21, 39, 32),
    Hizb_47_Quarter_02(47, 2, 23, 39, 53),
    Hizb_47_Quarter_03(47, 3, 20, 40, 1),
    Hizb_47_Quarter_04(47, 4, 20, 40, 21),
    Hizb_48_Quarter_01(48, 1, 25, 40, 41),
    Hizb_48_Quarter_02(48, 2, 28, 40, 66),
    Hizb_48_Quarter_03(48, 3, 16, 41, 9),
    Hizb_48_Quarter_04(48, 4, 22, 41, 25),
    Hizb_49_Quarter_01(49, 1, 20, 41, 47),
    Hizb_49_Quarter_02(49, 2, 14, 42, 13),
    Hizb_49_Quarter_03(49, 3, 24, 42, 27),
    Hizb_49_Quarter_04(49, 4, 26, 42, 51),
    Hizb_50_Quarter_01(50, 1, 33, 43, 24),
    Hizb_50_Quarter_02(50, 2, 49, 43, 57),
    Hizb_50_Quarter_03(50, 3, 54, 44, 17),
    Hizb_50_Quarter_04(50, 4, 26, 45, 12),
    Hizb_51_Quarter_01(51, 1, 20, 46, 1),
    Hizb_51_Quarter_02(51, 2, 24, 46, 21),
    Hizb_51_Quarter_03(51, 3, 23, 47, 10),
    Hizb_51_Quarter_04(51, 4, 23, 47, 33),
    Hizb_52_Quarter_01(52, 1, 12, 48, 18),
    Hizb_52_Quarter_02(52, 2, 13, 49, 1),
    Hizb_52_Quarter_03(52, 3, 31, 49, 14),
    Hizb_52_Quarter_04(52, 4, 49, 50, 27),
    Hizb_53_Quarter_01(53, 1, 53, 51, 31),
    Hizb_53_Quarter_02(53, 2, 51, 52, 24),
    Hizb_53_Quarter_03(53, 3, 45, 53, 26),
    Hizb_53_Quarter_04(53, 4, 47, 54, 9),
    Hizb_54_Quarter_01(54, 1, 78, 55, 1),
    Hizb_54_Quarter_02(54, 2, 74, 56, 1),
    Hizb_54_Quarter_03(54, 3, 37, 56, 75),
    Hizb_54_Quarter_04(54, 4, 14, 57, 16),
    Hizb_55_Quarter_01(55, 1, 13, 58, 1),
    Hizb_55_Quarter_02(55, 2, 19, 58, 14),
    Hizb_55_Quarter_03(55, 3, 20, 59, 11),
    Hizb_55_Quarter_04(55, 4, 21, 60, 7),
    Hizb_56_Quarter_01(56, 1, 14, 62, 1),
    Hizb_56_Quarter_02(56, 2, 26, 63, 4),
    Hizb_56_Quarter_03(56, 3, 12, 65, 1),
    Hizb_56_Quarter_04(56, 4, 12, 66, 1),
    Hizb_57_Quarter_01(57, 1, 30, 67, 1),
    Hizb_57_Quarter_02(57, 2, 52, 68, 1),
    Hizb_57_Quarter_03(57, 3, 70, 69, 1),
    Hizb_57_Quarter_04(57, 4, 54, 70, 19),
    Hizb_58_Quarter_01(58, 1, 47, 72, 1),
    Hizb_58_Quarter_02(58, 2, 57, 73, 20),
    Hizb_58_Quarter_03(58, 3, 58, 75, 1),
    Hizb_58_Quarter_04(58, 4, 63, 76, 19),
    Hizb_59_Quarter_01(59, 1, 86, 78, 1),
    Hizb_59_Quarter_02(59, 2, 71, 80, 1),
    Hizb_59_Quarter_03(59, 3, 55, 82, 1),
    Hizb_59_Quarter_04(59, 4, 64, 84, 1),
    Hizb_60_Quarter_01(60, 1, 75, 87, 1),
    Hizb_60_Quarter_02(60, 2, 67, 90, 1),
    Hizb_60_Quarter_03(60, 3, 64, 94, 1),
    Hizb_60_Quarter_04(60, 4, 82, 100, 9);

    /**
     * Parses the given Hizb number and Quarter number values to an instance of {@link HizbQuarter}.
     *
     * @param hizbNumber    is a value between 1 and 60 (inclusive).
     * @param quarterNumber is a value between 1 and 4 (inclusive).
     * @return the {@link HizbQuarter} corresponding to the provided {@code hizbNumber} and {@code quarterNumber} values.
     */
    @NonNull
    public static HizbQuarter parse(int hizbNumber, int quarterNumber) {
        if (hizbNumber < 1 || hizbNumber > 60) {
            throw new IllegalArgumentException("Bad hizbNumber passed in.");
        } else if (quarterNumber < 1 || quarterNumber > 4) {
            throw new IllegalArgumentException("Bad quarterNumber passed in.");
        } else {
            HizbQuarter[] hizbQuarters = HizbQuarter.values();

            int index = ((hizbNumber - 1) * 4) + (quarterNumber - 1);

            return hizbQuarters[index];
        }
    }

    /**
     * The Hizb number (not index) that this Hizb-quarter corresponds to.
     */
    private final int hizbNumber;

    /**
     * The quarter (1 to 4) within the Hizb that this Hizb-Quarter corresponds to.
     */
    private final int quarterNumber;

    /**
     * The number of Verses/Ayahs in this Hizb-Quarter.
     */
    private final int numVerses;

    /**
     * The number (not index) of the Surah in which this Hizb-Quarter begins.
     */
    private final int fromSurah;

    /**
     * The number (not index) of the Verse/Ayah within {@link #fromSurah} at which this Hizb-Quarter begins.
     */
    private final int fromVerse;

    /**
     * Constructor.
     */
    HizbQuarter(int hizbNumber,
                int quarterNumber,
                int numVerses,
                int fromSurah,
                int fromVerse) {
        this.hizbNumber = hizbNumber;
        this.quarterNumber = quarterNumber;
        this.numVerses = numVerses;
        this.fromSurah = fromSurah;
        this.fromVerse = fromVerse;
    }

    /**
     * @return the Hizb number (not index) that this Hizb-quarter corresponds to.
     */
    public int getHizbNumber() {
        return hizbNumber;
    }

    /**
     * @return the quarter (1 to 4) within the Hizb that this Hizb-Quarter corresponds to.
     */
    public int getQuarterNumber() {
        return quarterNumber;
    }

    /**
     * @return the number of Verses/Ayahs in this Hizb-Quarter.
     */
    public int getNumVerses() {
        return numVerses;
    }

    /**
     * @return the number (not index) of the Surah in which this Hizb-Quarter begins.
     */
    public int getFromSurah() {
        return fromSurah;
    }

    /**
     * @return the number (not index) of the Verse/Ayah within {@link #fromSurah} at which this Hizb-Quarter begins.
     */
    public int getFromVerse() {
        return fromVerse;
    }
}
