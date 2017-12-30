package com.thinkincode.quran.sdk.model;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

@RunWith(JUnit4.class)
public class HizbQuarterTest {

    @Test
    public void test_parse_forHizbNumber1andQuarterNumber1() {
        // When.
        HizbQuarter hizbQuarter = HizbQuarter.parse(1, 1);

        // Then.
        assertEquals(HizbQuarter.Hizb_01_Quarter_01, hizbQuarter);
    }

    @Test
    public void test_parse_forHizbNumber60andQuarterNumber4() {
        // When.
        HizbQuarter hizbQuarter = HizbQuarter.parse(60, 4);

        // Then.
        assertEquals(HizbQuarter.Hizb_60_Quarter_04, hizbQuarter);
    }

    @Test
    public void test_getHizbNumber_and_getQuarterNumber_forAllHizbQuarters() {
        // Given.
        HizbQuarter[] hizbQuarters = HizbQuarter.values();

        // When. / Then.
        int expectedHizbNumber = 1;
        int expectedQuarterNumber = 1;

        for (HizbQuarter hizbQuarter : hizbQuarters) {
            assertEquals(expectedHizbNumber, hizbQuarter.getHizbNumber());
            assertEquals(expectedQuarterNumber, hizbQuarter.getQuarterNumber());

            if (expectedQuarterNumber == 4) {
                expectedHizbNumber++;
                expectedQuarterNumber = 1;
            } else {
                expectedQuarterNumber++;
            }
        }
    }

    @Test
    public void test_getNumVerses_forAllHizbQuarters() {
        // Given.
        HizbQuarter[] hizbQuarters = HizbQuarter.values();

        // When.
        int verseCount = 0;

        for (HizbQuarter hizbQuarter : hizbQuarters) {
            int numVerses = hizbQuarter.getNumVerses();
            verseCount += numVerses;
        }

        // Then.
        assertThat(verseCount, equalTo(6236));
    }
}