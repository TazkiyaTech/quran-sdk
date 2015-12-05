package com.thinkincode.quranutils.model;

import com.thinkincode.quranutils.BaseTestCase;
import com.thinkincode.quranutils.database.model.JuzEnum;

public class JuzEnumTest extends BaseTestCase {

    public void testNumberOfVersesInJuz01() {
        // When.
        int numVerses = JuzEnum.Juz_01.getNumVerses();
        // Then.
        assertEquals(148, numVerses);
    }

	public void testNumberOfVersesInJuz27() {
        // When.
		int numVerses = JuzEnum.Juz_27.getNumVerses();
        // Then.
		assertEquals(399, numVerses);
	}

	public void testNumberOfVersesInJuz30() {
        // When.
		int numVerses = JuzEnum.Juz_30.getNumVerses();
		// Then.
		assertEquals(564, numVerses);
	}

	public void testTotalNumberOfVersesInAllJuzs() {
        // Given.
        JuzEnum[] juzs = JuzEnum.values();

        // When.

		int verseCount = 0;

		for (int i = 0; i < juzs.length; i++) {
			int numVerses = juzs[i].getNumVerses();
			verseCount += numVerses;
		}

        // Then.
		assertEquals(6236, verseCount);
	}
}
