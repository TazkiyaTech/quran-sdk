package com.tazkiyatech.quran.sdk.database

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.tazkiyatech.quran.sdk.exception.QuranDatabaseException
import com.tazkiyatech.quran.sdk.model.ChapterMetadata
import com.tazkiyatech.quran.sdk.model.ChapterType
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class QuranDatabaseChapterMetadataTests {

    private lateinit var quranDatabase: QuranDatabase

    @Before
    fun setUp() {
        quranDatabase = QuranDatabase(ApplicationProvider.getApplicationContext<Context>())
    }

    @After
    fun tearDown() {
        quranDatabase.closeDatabase()
    }

    @Test
    fun getMetadataForChapter_with_chapter_type_surah_and_chapter_number_1() {
        // When.
        val chapterMetadata = quranDatabase.getMetadataForChapter(
            ChapterType.SURAH,
            1
        )

        // Then.
        assertEquals(ChapterType.SURAH.nameInDatabase, chapterMetadata.chapterType)
        assertEquals(1, chapterMetadata.chapterNumber)
        assertEquals(7, chapterMetadata.numAyahs)
        assertEquals(1, chapterMetadata.surahNumber)
        assertEquals(1, chapterMetadata.ayahNumber)
    }

    @Test(expected = QuranDatabaseException::class)
    fun getMetadataForChapter_with_chapter_type_surah_and_chapter_number_0() {
        // When.
        quranDatabase.getMetadataForChapter(ChapterType.SURAH, 0)
    }

    @Test(expected = QuranDatabaseException::class)
    fun getMetadataForChapter_with_chapter_type_surah_and_chapter_number_115() {
        // When.
        quranDatabase.getMetadataForChapter(ChapterType.SURAH, 115)
    }

    @Test
    fun getMetadataForChapterType_with_chapter_type_surah() {
        // Given.
        val chapterType = ChapterType.SURAH

        // When.
        val chapterMetadataList = quranDatabase.getMetadataForChapterType(chapterType)

        // Then.
        assertEquals(114, chapterMetadataList.size)

        // And.
        assertChapterTypeAndChapterNumberInChapterMetadataList(chapterType, chapterMetadataList)

        // And.
        assertTotalNumberOfAyahsInChapterMetadataList(chapterMetadataList)
    }

    @Test
    fun getMetadataForChapterType_with_chapter_type_juz() {
        // Given.
        val chapterType = ChapterType.JUZ

        // When.
        val chapterMetadataList = quranDatabase.getMetadataForChapterType(chapterType)

        // Then.
        assertEquals(30, chapterMetadataList.size)

        // And.
        assertChapterTypeAndChapterNumberInChapterMetadataList(chapterType, chapterMetadataList)

        // And.
        assertTotalNumberOfAyahsInChapterMetadataList(chapterMetadataList)
    }

    @Test
    fun getMetadataForChapterType_with_chapter_type_hizb() {
        // Given.
        val chapterType = ChapterType.HIZB

        // When.
        val chapterMetadataList = quranDatabase.getMetadataForChapterType(chapterType)

        // Then.
        assertEquals(60, chapterMetadataList.size)

        // And.
        assertChapterTypeAndChapterNumberInChapterMetadataList(chapterType, chapterMetadataList)

        // And.
        assertTotalNumberOfAyahsInChapterMetadataList(chapterMetadataList)
    }

    @Test
    fun getMetadataForChapterType_with_chapter_type_hizb_quarter() {
        // Given.
        val chapterType = ChapterType.HIZB_QUARTER

        // When.
        val chapterMetadataList = quranDatabase.getMetadataForChapterType(chapterType)

        // Then.
        assertEquals(240, chapterMetadataList.size)

        // And.
        assertChapterTypeAndChapterNumberInChapterMetadataList(chapterType, chapterMetadataList)

        // And.
        assertTotalNumberOfAyahsInChapterMetadataList(chapterMetadataList)
    }

    @Test
    fun number_of_verses_in_each_hizb_matches_the_number_of_verses_in_each_hizb_quarter() {
        val hizbMetadataList = quranDatabase.getMetadataForChapterType(ChapterType.HIZB)

        val hizbQuarterMetadataList =
            quranDatabase.getMetadataForChapterType(ChapterType.HIZB_QUARTER)

        for (i in hizbMetadataList.indices) {
            val expected = hizbMetadataList[i].numAyahs

            val actual = (hizbQuarterMetadataList[i * 4].numAyahs
                    + hizbQuarterMetadataList[i * 4 + 1].numAyahs
                    + hizbQuarterMetadataList[i * 4 + 2].numAyahs
                    + hizbQuarterMetadataList[i * 4 + 3].numAyahs)

            assertEquals("HIZB " + (i + 1), expected, actual)
        }
    }

    @Test
    fun number_of_verses_in_each_juz_matches_the_number_of_verses_in_each_hizb() {
        val juzMetadataList = quranDatabase.getMetadataForChapterType(ChapterType.JUZ)
        val hizbMetadataList = quranDatabase.getMetadataForChapterType(ChapterType.HIZB)

        for (i in juzMetadataList.indices) {
            val expected = juzMetadataList[i].numAyahs

            val actual = hizbMetadataList[i * 2].numAyahs + hizbMetadataList[i * 2 + 1].numAyahs

            assertEquals("JUZ " + (i + 1), expected, actual)
        }
    }

    @Test
    fun surahNumber_and_ayahNumber_in_each_surah_is_as_expected() {
        val surahMetadataList = quranDatabase.getMetadataForChapterType(ChapterType.SURAH)

        for (i in surahMetadataList.indices) {
            val chapterMetadata = surahMetadataList[i]

            assertEquals((i + 1), chapterMetadata.surahNumber)
            assertEquals(1, chapterMetadata.ayahNumber)
        }
    }

    @Test
    fun surahNumber_and_ayahNumber_in_each_juz_is_as_expected() {
        assertSurahAndVerseNumberOfFirstVerseInEachTarget(ChapterType.JUZ)
    }

    @Test
    fun surahNumber_and_ayahNumber_in_each_hizb_is_as_expected() {
        assertSurahAndVerseNumberOfFirstVerseInEachTarget(ChapterType.HIZB)
    }

    @Test
    fun surahNumber_and_ayahNumber_in_each_hizb_quarter_is_as_expected() {
        assertSurahAndVerseNumberOfFirstVerseInEachTarget(ChapterType.HIZB_QUARTER)
    }

    private fun assertChapterTypeAndChapterNumberInChapterMetadataList(chapterType: ChapterType,
                                                                       chapterMetadataList: List<ChapterMetadata>) {
        for (i in chapterMetadataList.indices) {
            val (chapterType1, chapterNumber) = chapterMetadataList[i]

            assertEquals(chapterType.nameInDatabase, chapterType1)
            assertEquals((i + 1), chapterNumber)
        }
    }

    private fun assertTotalNumberOfAyahsInChapterMetadataList(chapterMetadataList: List<ChapterMetadata>) {
        var count = 0

        for (chapterMetadata in chapterMetadataList) {
            count += chapterMetadata.numAyahs
        }

        // And.
        assertEquals(6236, count)
    }

    private fun assertSurahAndVerseNumberOfFirstVerseInEachTarget(chapterType: ChapterType) {
        val chapterMetadataList = quranDatabase.getMetadataForChapterType(chapterType)

        for (i in chapterMetadataList.indices) {
            val surahNumberA = chapterMetadataList[i].surahNumber
            val verseNumberA = chapterMetadataList[i].ayahNumber

            val surahNumberB: Int
            val verseNumberB: Int

            if (i < chapterMetadataList.size - 1) {
                surahNumberB = chapterMetadataList[i + 1].surahNumber
                verseNumberB = chapterMetadataList[i + 1].ayahNumber
            } else {
                surahNumberB = 115
                verseNumberB = 1
            }

            val count = getNumberOfVersesInBetween(
                surahNumberA,
                verseNumberA,
                surahNumberB,
                verseNumberB
            )

            assertEquals(
                "Unexpected number of ayahs between " + chapterType.name + " " + (i + 1) + " and the next one.",
                count,
                chapterMetadataList[i].numAyahs
            )
        }
    }

    /**
     * @return the number of verses between location A and location B,
     * or -1 if location B is not greater than location A.
     */
    private fun getNumberOfVersesInBetween(
        surahNumberA: Int,
        verseNumberA: Int,
        surahNumberB: Int,
        verseNumberB: Int
    ): Int {
        if (surahNumberB < surahNumberA) {
            return -1
        } else if (surahNumberB == surahNumberA && verseNumberB <= verseNumberA) {
            return -1
        } else if (surahNumberB == surahNumberA) {
            return verseNumberB - verseNumberA
        } else {
            var count = 0

            val surahAMetadata = quranDatabase.getMetadataForChapter(
                ChapterType.SURAH,
                surahNumberA
            )
            count += surahAMetadata.numAyahs + 1 - verseNumberA

            // add to count the number of verses in each Surah between A and B (exclusive of A and B)
            for (i in surahNumberA + 1 until surahNumberB) {
                val currentSurahMetadata = quranDatabase.getMetadataForChapter(ChapterType.SURAH, i)
                count += currentSurahMetadata.numAyahs
            }

            count += verseNumberB - 1

            return count
        }
    }
}
