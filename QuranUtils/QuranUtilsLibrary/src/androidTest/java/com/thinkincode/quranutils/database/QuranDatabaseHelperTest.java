package com.thinkincode.quranutils.database;

import com.thinkincode.quranutils.BaseTestCase;
import com.thinkincode.quranutils.database.model.SurahEnum;

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

		SurahEnum[] surahs = SurahEnum.values();

		for (int i = 1; i <= surahs.length; i++) {
			// When.
			List<String> ayahsInSurah = getQuranDatabaseHelper().getAyahsInSurah(getContext(), i);

			// Then.
			assertEquals(surahs[i - 1].getNumVerses(), ayahsInSurah.size());
		}
	}
}
