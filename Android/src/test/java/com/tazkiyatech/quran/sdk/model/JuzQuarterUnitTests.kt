package com.tazkiyatech.quran.sdk.model

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class JuzQuarterUnitTests {

    @Test
    fun getJuzNumber_and_getQuarterNumber_forAllJuzQuarters() {
        // Given.
        val juzQuarters = JuzQuarter.values()

        // When. / Then.
        var expectedJuzNumber = 1
        var expectedQuarterNumber = 1

        for (juzQuarter in juzQuarters) {
            assertEquals(expectedJuzNumber, juzQuarter.juzNumber)
            assertEquals(expectedQuarterNumber, juzQuarter.quarterNumber)

            if (expectedQuarterNumber == 4) {
                expectedJuzNumber++
                expectedQuarterNumber = 1
            } else {
                expectedQuarterNumber++
            }
        }
    }

    @Test
    fun getNumVerses_forAllJuzQuarters() {
        // Given.
        val juzQuarters = JuzQuarter.values()

        // When.
        var verseCount = 0

        for (juzQuarter in juzQuarters) {
            val numVerses = juzQuarter.numVerses
            verseCount += numVerses
        }

        // Then.
        assertEquals((6236 - 7), verseCount)
    }
}