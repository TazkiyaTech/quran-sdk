package com.thinkincode.quranutils.model;

/**
 * An enum representation of the Juzs that make up the Qur'an.
 */
public enum JuzEnum {

	Juz_01 (1, 1, 1),
	Juz_02 (2, 2, 142),
	Juz_03 (3, 2, 253),
	Juz_04 (4, 3, 93),
	Juz_05 (5, 4, 24),
	Juz_06 (6, 4, 148),
	Juz_07 (7, 5, 82),
	Juz_08 (8, 6, 111),
	Juz_09 (9, 7, 88),
	Juz_10 (10, 8, 41),
	Juz_11 (11, 9, 93),
	Juz_12 (12, 11, 6),
	Juz_13 (13, 12, 53),
	Juz_14 (14, 15, 1),
	Juz_15 (15, 17, 1),
	Juz_16 (16, 18, 75),
	Juz_17 (17, 21, 1),
	Juz_18 (18, 23, 1),
	Juz_19 (19, 25, 21),
	Juz_20 (20, 27, 56),
	Juz_21 (21, 29, 46),
	Juz_22 (22, 33, 31),
	Juz_23 (23, 36, 28),
	Juz_24 (24, 39, 32),
	Juz_25 (25, 41, 47),
	Juz_26 (26, 46, 1),
	Juz_27 (27, 51, 31),
	Juz_28 (28, 58, 1),
	Juz_29 (29, 67, 1),
	Juz_30 (30, 78, 1);

	/**
	 * The number (not index) of this Juz.
	 */
	private final int juzNumber;

	/**
	 * The number (not index) of the Surah in which the Juz begins.
	 */
	private final int fromSurah;
	
	/**
	 * The number (not index) of the Verse within {@link #fromSurah} at which this Juz begins.
	 */
	private final int fromVerse;

	/**
	 * Constructor.
	 *
     * @param juzNumber
	 * @param fromSurah
	 * @param fromVerse
	 * */
	JuzEnum(int juzNumber, int fromSurah, int fromVerse) {
        this.juzNumber = juzNumber;
		this.fromSurah = fromSurah;
		this.fromVerse = fromVerse;
	}

	/**
	 * @return the number of verses in the specified Juz.
	 */
	public int getNumVerses() {
		int toSurah;
		int toVerse;

		if (juzNumber == 30) {
			toSurah = 115;
			toVerse = 1;
		} else {
			JuzEnum juzNext = JuzEnum.values()[juzNumber];

			toSurah = juzNext.fromSurah;
			toVerse = juzNext.fromVerse;
		}

		// check whether the Juz starts and ends in the same Surah
		if (fromSurah == toSurah) {
			return toVerse - fromVerse;
		}

		int verseCount = 0;

		for (int surahNumber = fromSurah; surahNumber < toSurah; surahNumber++) {
			SurahEnum surah = SurahEnum.parse(surahNumber);

			if (surahNumber == fromSurah) {
				verseCount += surah.getNumVerses() - fromVerse + 1;
			} else {
				verseCount += surah.getNumVerses();
			}
		}

		verseCount += (toVerse - 1);

		return verseCount;
	}
}
