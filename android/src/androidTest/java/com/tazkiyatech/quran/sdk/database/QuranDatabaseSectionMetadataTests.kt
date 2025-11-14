package com.tazkiyatech.quran.sdk.database

import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.tazkiyatech.quran.sdk.exception.QuranDatabaseException
import com.tazkiyatech.quran.sdk.model.SectionMetadata
import com.tazkiyatech.quran.sdk.model.SectionType
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class QuranDatabaseSectionMetadataTests {

    private lateinit var quranDatabase: QuranDatabase

    @Before
    fun setUp() {
        quranDatabase = QuranDatabase(ApplicationProvider.getApplicationContext())
    }

    @After
    fun tearDown() {
        quranDatabase.closeDatabase()
    }

    @Test
    fun getMetadataForSection_with_section_type_surah_and_section_number_1() {
        // When.
        val sectionMetadata = quranDatabase.getMetadataForSection(
            SectionType.SURAH,
            1
        )

        // Then.
        assertEquals(SectionType.SURAH.nameInDatabase, sectionMetadata.sectionType)
        assertEquals(1, sectionMetadata.sectionNumber)
        assertEquals(7, sectionMetadata.numAyahs)
        assertEquals(1, sectionMetadata.surahNumber)
        assertEquals(1, sectionMetadata.ayahNumber)
    }

    @Test(expected = QuranDatabaseException::class)
    fun getMetadataForSection_with_section_type_surah_and_section_number_0() {
        // When.
        quranDatabase.getMetadataForSection(SectionType.SURAH, 0)
    }

    @Test(expected = QuranDatabaseException::class)
    fun getMetadataForSection_with_section_type_surah_and_section_number_115() {
        // When.
        quranDatabase.getMetadataForSection(SectionType.SURAH, 115)
    }

    @Test
    fun getMetadataForSectionsOfType_surah() {
        // Given.
        val sectionType = SectionType.SURAH

        // When.
        val sectionMetadataList = quranDatabase.getMetadataForSectionsOfType(sectionType)

        // Then.
        assertEquals(114, sectionMetadataList.size)

        // And.
        assertSectionTypesInSectionMetadataList(sectionType, sectionMetadataList)
        assertSectionNumbersInSectionMetadataList(sectionMetadataList)
        assertTotalNumberOfAyahsInSectionMetadataList(sectionMetadataList, 6236)
    }

    @Test
    fun getMetadataForSectionsOfType_juz_in_madinah_mushaf() {
        // Given.
        val sectionType = SectionType.JUZ_IN_MADINAH_MUSHAF

        // When.
        val sectionMetadataList = quranDatabase.getMetadataForSectionsOfType(sectionType)

        // Then.
        assertEquals(30, sectionMetadataList.size)

        // And.
        assertSectionTypesInSectionMetadataList(sectionType, sectionMetadataList)
        assertSectionNumbersInSectionMetadataList(sectionMetadataList)
        assertTotalNumberOfAyahsInSectionMetadataList(sectionMetadataList, 6236)
    }

    @Test
    fun getMetadataForSectionsOfType_hizb_in_madinah_mushaf() {
        // Given.
        val sectionType = SectionType.HIZB_IN_MADINAH_MUSHAF

        // When.
        val sectionMetadataList = quranDatabase.getMetadataForSectionsOfType(sectionType)

        // Then.
        assertEquals(60, sectionMetadataList.size)

        // And.
        assertSectionTypesInSectionMetadataList(sectionType, sectionMetadataList)
        assertSectionNumbersInSectionMetadataList(sectionMetadataList)
        assertTotalNumberOfAyahsInSectionMetadataList(sectionMetadataList, 6236)
    }

    @Test
    fun getMetadataForSectionsOfType_hizb_quarter_in_madinah_mushaf() {
        // Given.
        val sectionType = SectionType.HIZB_QUARTER_IN_MADINAH_MUSHAF

        // When.
        val sectionMetadataList = quranDatabase.getMetadataForSectionsOfType(sectionType)

        // Then.
        assertEquals(240, sectionMetadataList.size)

        // And.
        assertSectionTypesInSectionMetadataList(sectionType, sectionMetadataList)
        assertSectionNumbersInSectionMetadataList(sectionMetadataList)
        assertTotalNumberOfAyahsInSectionMetadataList(sectionMetadataList, 6236)
    }

    @Test
    fun getMetadataForSectionsOfType_juz_in_majeedi_mushaf() {
        // Given.
        val sectionType = SectionType.JUZ_IN_MAJEEDI_MUSHAF

        // When.
        val sectionMetadataList = quranDatabase.getMetadataForSectionsOfType(sectionType)

        // Then.
        assertEquals(30, sectionMetadataList.size)

        // And.
        assertSectionTypesInSectionMetadataList(sectionType, sectionMetadataList)
        assertSectionNumbersInSectionMetadataList(sectionMetadataList)
        assertTotalNumberOfAyahsInSectionMetadataList(sectionMetadataList, (6236 - 7))
    }

    @Test
    fun getMetadataForSectionsOfType_juz_quarter_in_majeedi_mushaf() {
        // Given.
        val sectionType = SectionType.JUZ_QUARTER_IN_MAJEEDI_MUSHAF

        // When.
        val sectionMetadataList = quranDatabase.getMetadataForSectionsOfType(sectionType)

        // Then.
        assertEquals(120, sectionMetadataList.size)

        // And.
        assertSectionTypesInSectionMetadataList(sectionType, sectionMetadataList)
        assertSectionNumbersInSectionMetadataList(sectionMetadataList)
        assertTotalNumberOfAyahsInSectionMetadataList(sectionMetadataList, (6236 - 7))
    }

    @Test
    fun number_of_verses_in_each_hizb_matches_the_number_of_verses_in_each_hizb_quarter() {
        val hizbMetadataList =
            quranDatabase.getMetadataForSectionsOfType(SectionType.HIZB_IN_MADINAH_MUSHAF)

        val hizbQuarterMetadataList =
            quranDatabase.getMetadataForSectionsOfType(SectionType.HIZB_QUARTER_IN_MADINAH_MUSHAF)

        for (i in hizbMetadataList.indices) {
            val expected = hizbMetadataList[i].numAyahs

            val actual = (hizbQuarterMetadataList[i * 4].numAyahs
                    + hizbQuarterMetadataList[i * 4 + 1].numAyahs
                    + hizbQuarterMetadataList[i * 4 + 2].numAyahs
                    + hizbQuarterMetadataList[i * 4 + 3].numAyahs)

            assertEquals("Failed comparison of Hizb ${i + 1}", expected, actual)
        }
    }

    @Test
    fun number_of_verses_in_each_juz_matches_the_number_of_verses_in_each_hizb() {
        val juzMetadataList =
            quranDatabase.getMetadataForSectionsOfType(SectionType.JUZ_IN_MADINAH_MUSHAF)

        val hizbMetadataList =
            quranDatabase.getMetadataForSectionsOfType(SectionType.HIZB_IN_MADINAH_MUSHAF)

        for (i in juzMetadataList.indices) {
            val expected = juzMetadataList[i].numAyahs

            val actual = hizbMetadataList[i * 2].numAyahs + hizbMetadataList[i * 2 + 1].numAyahs

            assertEquals("Failed comparison of Juz ${i + 1}", expected, actual)
        }
    }

    @Test
    fun number_of_verses_in_each_juz_matches_the_number_of_verses_in_each_juz_quarter() {
        val juzMetadataList =
            quranDatabase.getMetadataForSectionsOfType(SectionType.JUZ_IN_MAJEEDI_MUSHAF)

        val juzQuarterMetadataList =
            quranDatabase.getMetadataForSectionsOfType(SectionType.JUZ_QUARTER_IN_MAJEEDI_MUSHAF)

        for (i in juzMetadataList.indices) {
            val expected = juzMetadataList[i].numAyahs

            val actual = juzQuarterMetadataList[i * 4].numAyahs + juzQuarterMetadataList[i * 4 + 1].numAyahs + juzQuarterMetadataList[i * 4 + 2].numAyahs + juzQuarterMetadataList[i * 4 + 3].numAyahs

            assertEquals("Failed comparison of Juz ${i + 1}", expected, actual)
        }
    }

    @Test
    fun surahNumber_and_ayahNumber_in_each_surah_is_as_expected() {
        val surahMetadataList = quranDatabase.getMetadataForSectionsOfType(SectionType.SURAH)

        for (i in surahMetadataList.indices) {
            val sectionMetadata = surahMetadataList[i]

            assertEquals((i + 1), sectionMetadata.surahNumber)
            assertEquals(1, sectionMetadata.ayahNumber)
        }
    }

    @Test
    fun surahNumber_and_ayahNumber_in_each_madinah_mushaf_juz_is_as_expected() {
        assertSurahAndVerseNumberOfFirstVerseInEachSection(SectionType.JUZ_IN_MADINAH_MUSHAF)
    }

    @Test
    fun surahNumber_and_ayahNumber_in_each_majeedi_mushaf_juz_is_as_expected() {
        assertSurahAndVerseNumberOfFirstVerseInEachSection(SectionType.JUZ_IN_MAJEEDI_MUSHAF)
    }

    @Test
    fun surahNumber_and_ayahNumber_in_each_hizb_is_as_expected() {
        assertSurahAndVerseNumberOfFirstVerseInEachSection(SectionType.HIZB_IN_MADINAH_MUSHAF)
    }

    @Test
    fun surahNumber_and_ayahNumber_in_each_hizb_quarter_is_as_expected() {
        assertSurahAndVerseNumberOfFirstVerseInEachSection(SectionType.HIZB_QUARTER_IN_MADINAH_MUSHAF)
    }

    @Test
    fun surahNumber_and_ayahNumber_in_each_juz_quarter_is_as_expected() {
        assertSurahAndVerseNumberOfFirstVerseInEachSection(SectionType.JUZ_QUARTER_IN_MAJEEDI_MUSHAF)
    }

    private fun assertSectionTypesInSectionMetadataList(sectionType: SectionType,
                                                        sectionMetadataList: List<SectionMetadata>) {
        sectionMetadataList.forEach { assertEquals(sectionType.nameInDatabase, it.sectionType) }
    }

    private fun assertSectionNumbersInSectionMetadataList(sectionMetadataList: List<SectionMetadata>) {
        for (i in sectionMetadataList.indices) {
            assertEquals((i + 1), sectionMetadataList[i].sectionNumber)
        }
    }

    private fun assertTotalNumberOfAyahsInSectionMetadataList(sectionMetadataList: List<SectionMetadata>,
                                                              expectedTotal: Int) {
        var count = 0

        for (sectionMetadata in sectionMetadataList) {
            count += sectionMetadata.numAyahs
        }

        assertEquals(expectedTotal, count)
    }

    private fun assertSurahAndVerseNumberOfFirstVerseInEachSection(sectionType: SectionType) {
        val sectionMetadataList = quranDatabase.getMetadataForSectionsOfType(sectionType)

        for (i in sectionMetadataList.indices) {
            val surahNumberA = sectionMetadataList[i].surahNumber
            val verseNumberA = sectionMetadataList[i].ayahNumber

            val surahNumberB: Int
            val verseNumberB: Int

            if (i < sectionMetadataList.size - 1) {
                surahNumberB = sectionMetadataList[i + 1].surahNumber
                verseNumberB = sectionMetadataList[i + 1].ayahNumber
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
                "Unexpected number of ayahs between ${sectionType.name} ${i + 1} and the next one.",
                count,
                sectionMetadataList[i].numAyahs
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

            val surahAMetadata = quranDatabase.getMetadataForSection(
                SectionType.SURAH,
                surahNumberA
            )
            count += surahAMetadata.numAyahs + 1 - verseNumberA

            // add to count the number of verses in each Surah between A and B (exclusive of A and B)
            for (i in surahNumberA + 1 until surahNumberB) {
                val currentSurahMetadata = quranDatabase.getMetadataForSection(SectionType.SURAH, i)
                count += currentSurahMetadata.numAyahs
            }

            count += verseNumberB - 1

            return count
        }
    }
}
