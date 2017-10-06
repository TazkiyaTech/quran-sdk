package com.thinkincode.quran.sdk.model;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;

@RunWith(JUnit4.class)
public class JuzTest {

    @Test
    public void testNumberOfVersesInJuz01() {
        // When.
        int numVerses = Juz.Juz_01.getNumVerses();

        // Then.
        assertThat(numVerses, equalTo(148));
    }

    @Test
    public void testNumberOfVersesInJuz27() {
        // When.
        int numVerses = Juz.Juz_27.getNumVerses();

        // Then.
        assertThat(numVerses, equalTo(399));
    }

    @Test
    public void testNumberOfVersesInJuz30() {
        // When.
        int numVerses = Juz.Juz_30.getNumVerses();

        // Then.
        assertThat(numVerses, equalTo(564));
    }

    @Test
    public void testTotalNumberOfVersesInAllJuzs() {
        // Given.
        Juz[] juzs = Juz.values();

        // When.

        int verseCount = 0;

        for (Juz juz : juzs) {
            int numVerses = juz.getNumVerses();
            verseCount += numVerses;
        }

        // Then.
        assertThat(verseCount, equalTo(6236));
    }
}
