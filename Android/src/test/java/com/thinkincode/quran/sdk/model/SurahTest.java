package com.thinkincode.quran.sdk.model;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static junit.framework.Assert.assertEquals;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;

@RunWith(JUnit4.class)
public class SurahTest {

    @Test
    public void test_getSurahNumber_forAllSurahs() {
        // Given.
        Surah[] surahs = Surah.values();

        // When. / Then.
        int expectedSurahNumber = 1;

        for (Surah surah : surahs) {
            assertEquals(expectedSurahNumber, surah.getSurahNumber());
            expectedSurahNumber++;
        }
    }

    @Test
    public void test_getNumVerses_forAllSurahs() {
        // Given.
        Surah[] surahs = Surah.values();

        // When.
        int verseCount = 0;

        for (Surah surah : surahs) {
            verseCount += surah.getNumVerses();
        }

        // Then.
        assertThat(verseCount, equalTo(6236));
    }
}
