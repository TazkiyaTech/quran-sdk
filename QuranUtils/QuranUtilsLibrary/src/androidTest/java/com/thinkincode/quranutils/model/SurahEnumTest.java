package com.thinkincode.quranutils.model;

import com.thinkincode.quranutils.BaseTestCase;
import com.thinkincode.quranutils.database.model.SurahEnum;

public class SurahEnumTest extends BaseTestCase {

    public void testGetNumVerses() {
        // Given.
        SurahEnum[] surahs = SurahEnum.values();

        // When.

        int count = 0;

        for (SurahEnum chapterEnum : surahs) {
            count += chapterEnum.getNumVerses();
        }

        // Then.
        assertEquals(6236, count);
    }
}
