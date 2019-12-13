package com.tazkiyatech.quran.sdk.model

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class SurahUnitTests {

    @Test
    fun getSurahNumber_forAllSurahs() {
        // Given.
        val surahs = Surah.values()

        // When. / Then.
        var expectedSurahNumber = 1

        for (surah in surahs) {
            assertEquals(expectedSurahNumber, surah.surahNumber)
            expectedSurahNumber++
        }
    }

    @Test
    fun getNumVerses_forAllSurahs() {
        // Given.
        val surahs = Surah.values()

        // When.
        var verseCount = 0

        for (surah in surahs) {
            verseCount += surah.numVerses
        }

        // Then.
        assertEquals(6236, verseCount)
    }
}
