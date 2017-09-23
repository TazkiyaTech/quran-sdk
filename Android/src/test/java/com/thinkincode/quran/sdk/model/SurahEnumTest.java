package com.thinkincode.quran.sdk.model;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;

@RunWith(JUnit4.class)
public class SurahEnumTest {

    @Test
    public void testGetNumVerses() {
        // Given.
        SurahEnum[] surahs = SurahEnum.values();

        // When.

        int verseCount = 0;

        for (SurahEnum chapterEnum : surahs) {
            verseCount += chapterEnum.getNumVerses();
        }

        // Then.
        assertThat(verseCount, equalTo(6236));
    }
}
