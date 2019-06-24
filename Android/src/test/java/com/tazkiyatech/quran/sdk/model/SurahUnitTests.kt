package com.tazkiyatech.quran.sdk.model

import org.hamcrest.core.IsEqual.equalTo
import org.junit.Assert.assertEquals
import org.junit.Assert.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
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
