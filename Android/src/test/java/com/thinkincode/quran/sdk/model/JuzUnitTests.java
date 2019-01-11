package com.thinkincode.quran.sdk.model;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.junit.Assert.assertEquals;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;

@RunWith(JUnit4.class)
public class JuzUnitTests {

    @Test
    public void test_parse_forJuz1() {
        // When.
        Juz juz = Juz.parse(1);

        // Then.
        assertEquals(Juz.Juz_01, juz);
    }

    @Test
    public void test_parse_forJuz30() {
        // When.
        Juz juz = Juz.parse(30);

        // Then.
        assertEquals(Juz.Juz_30, juz);
    }

    @Test
    public void test_getJuzNumber_forAllJuzs() {
        // Given.
        Juz[] juzs = Juz.values();

        // When. / Then.
        int expectedJuzNumber = 1;

        for (Juz juz : juzs) {
            assertEquals(expectedJuzNumber, juz.getJuzNumber());
            expectedJuzNumber++;
        }
    }

    @Test
    public void test_getNumVerses_forJuz1() {
        // When.
        int numVerses = Juz.Juz_01.getNumVerses();

        // Then.
        assertThat(numVerses, equalTo(148));
    }

    @Test
    public void test_getNumVerses_forJuz27() {
        // When.
        int numVerses = Juz.Juz_27.getNumVerses();

        // Then.
        assertThat(numVerses, equalTo(399));
    }

    @Test
    public void test_getNumVerses_forJuz30() {
        // When.
        int numVerses = Juz.Juz_30.getNumVerses();

        // Then.
        assertThat(numVerses, equalTo(564));
    }

    @Test
    public void test_getNumVerses_forAllJuzs() {
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
