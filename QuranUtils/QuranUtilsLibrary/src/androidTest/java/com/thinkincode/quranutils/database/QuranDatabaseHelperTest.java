package com.thinkincode.quranutils.database;

import com.thinkincode.quranutils.BaseTestCase;

import java.util.List;

public class QuranDatabaseHelperTest extends BaseTestCase {

	public void testGetSurahNames() {
		// When.
		List<String> surahNames = getQuranDatabaseHelper().getSurahNames(getContext());

		// Then.
		assertEquals(114, surahNames.size());
	}

	public void testGetAyahsInSurah() {
		// go through and test method call for each surah of the Qur'an

//		for (int i = 0; i < ChapterEnum.values().length; i++) {
//			// When.
//			List<String> ayahsInSurah = QuranTesterApplication.getInstance().getQuranDatabaseHelper().getAyahsInSurah(i + 1);
//
//			// Then.
//			assertEquals(ChapterEnum.values()[i].getNumVerses(), ayahsInSurah.size());
//		}
	}
}
