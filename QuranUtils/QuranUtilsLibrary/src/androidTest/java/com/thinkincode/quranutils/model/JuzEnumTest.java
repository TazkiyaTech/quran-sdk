package com.thinkincode.quranutils.model;

import com.thinkincode.quranutils.BaseTestCase;
import com.thinkincode.quranutils.database.model.JuzEnum;

public class JuzEnumTest extends BaseTestCase {

    public void testNumberOfVersesInJuz01() {
        int numVerses = JuzEnum.getNumVerses(1);
        assertEquals(148, numVerses);
    }

	public void testNumberOfVersesInJuz27() {
		int numVerses = JuzEnum.getNumVerses(27);
		assertEquals(399, numVerses);
	}

	public void testNumberOfVersesInJuz30() {
		int numVerses = JuzEnum.getNumVerses(30);
		assertEquals(564, numVerses);
	}

	public void testTotalNumberOfVersesInAllJuzs() {
		int verseCount = 0;

		for (int i = 1; i <= 30; i++) {
			int numVerses = JuzEnum.getNumVerses(i);
			verseCount += numVerses;
		}

		assertEquals(6236, verseCount);
	}
}
