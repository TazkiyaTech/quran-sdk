package com.thinkincode.quranutils.model;

import com.thinkincode.quranutils.BaseTestCase;
import com.thinkincode.quranutils.database.model.JuzEnum;

public class JuzEnumTest extends BaseTestCase {

    public void testNumberOfVersesInJuz01() {
        int numVerses = JuzEnum.Juz_01.getNumVerses();
        assertEquals(148, numVerses);
    }

	public void testNumberOfVersesInJuz27() {
		int numVerses = JuzEnum.Juz_27.getNumVerses();
		assertEquals(399, numVerses);
	}

	public void testNumberOfVersesInJuz30() {
		int numVerses = JuzEnum.Juz_30.getNumVerses();
		assertEquals(564, numVerses);
	}

	public void testTotalNumberOfVersesInAllJuzs() {
		int verseCount = 0;

        JuzEnum[] juzs = JuzEnum.values();

		for (int i = 0; i < juzs.length; i++) {
			int numVerses = juzs[i].getNumVerses();
			verseCount += numVerses;
		}

		assertEquals(6236, verseCount);
	}
}
