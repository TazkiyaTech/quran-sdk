package com.thinkincode.quranutils.database.model;

/**
 * An enum representation of the Juzs that make up the Qur'an.
 */
public enum JuzEnum {

	Juz_01 (1, 1),
	Juz_02 (2, 142),
	Juz_03 (2, 253),
	Juz_04 (3, 93),
	Juz_05 (4, 24),
	Juz_06 (4, 148),
	Juz_07 (5, 82),
	Juz_08 (6, 111),
	Juz_09 (7, 88),
	Juz_10 (8, 41),
	Juz_11 (9, 93),
	Juz_12 (11, 6),
	Juz_13 (12, 53),
	Juz_14 (15, 1),
	Juz_15 (17, 1),
	Juz_16 (18, 75),
	Juz_17 (21, 1),
	Juz_18 (23, 1),
	Juz_19 (25, 21),
	Juz_20 (27, 56),
	Juz_21 (29, 46),
	Juz_22 (33, 31),
	Juz_23 (36, 28),
	Juz_24 (39, 32),
	Juz_25 (41, 47),
	Juz_26 (46, 1),
	Juz_27 (51, 31),
	Juz_28 (58, 1),
	Juz_29 (67, 1),
	Juz_30 (78, 1);

	/**
	 * The number (not index) of the Surah in which the Juz begins.
	 */
	private int fromSurah;
	
	/**
	 * The number (not index) of the Verse within {@link #fromSurah} at which this Juz begins.
	 */
	private int fromVerse;

	/**
	 * Constructor.
	 * 
	 * @param fromSurah
	 * @param fromVerse
	 * */
	JuzEnum(int fromSurah, int fromVerse) {
		this.fromSurah = fromSurah;
		this.fromVerse = fromVerse;
	}

	/**
	 * @param juzNumber >= 1 && <= 30.
	 * @return the number of verses in the specified Juz.
	 */
	public static int getNumVerses(int juzNumber) {
		JuzEnum juz = JuzEnum.values()[juzNumber - 1];

		int fromSurahForJuz = juz.fromSurah;
		int toSurahForJuz;

		int fromVerse = juz.fromVerse;
		int toVerse;

		if (juzNumber == 30) {
			toSurahForJuz = 115;
			toVerse = 1;
		} else {
			JuzEnum juzNext = JuzEnum.values()[juzNumber];

			toSurahForJuz = juzNext.fromSurah;
			toVerse = juzNext.fromVerse;
		}

		// check whether the Juz starts and ends in the same Surah
		if (fromSurahForJuz == toSurahForJuz) {
			return toVerse - fromVerse;
		}

		int verseCount = 0;

		for (int surahNumber = fromSurahForJuz; surahNumber < toSurahForJuz; surahNumber++) {
			SurahEnum surah = SurahEnum.parse(surahNumber);

			if (surahNumber == fromSurahForJuz) {
				verseCount += surah.getNumVerses() - fromVerse + 1;
			} else {
				verseCount += surah.getNumVerses();
			}
		}

		verseCount += (toVerse - 1);

		return verseCount;
	}
}
