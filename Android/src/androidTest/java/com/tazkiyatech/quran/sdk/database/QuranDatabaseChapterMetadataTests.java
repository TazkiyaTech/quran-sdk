package com.tazkiyatech.quran.sdk.database;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.tazkiyatech.quran.sdk.exception.QuranDatabaseException;
import com.tazkiyatech.quran.sdk.model.ChapterMetadata;
import com.tazkiyatech.quran.sdk.model.ChapterType;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

import static org.junit.Assert.assertEquals;

@RunWith(AndroidJUnit4.class)
public class QuranDatabaseChapterMetadataTests {

    private QuranDatabase quranDatabase;

    @Before
    public void setUp() {
        quranDatabase = new QuranDatabase(ApplicationProvider.getApplicationContext());
    }

    @After
    public void tearDown() {
        quranDatabase.closeDatabase();
    }

    @Test
    public void getMetadataForChapter_with_chapter_type_surah_and_chapter_number_1() {
        // When.
        ChapterMetadata chapterMetadata = quranDatabase.getMetadataForChapter(ChapterType.SURAH, 1);

        // Then.
        assertEquals(ChapterType.SURAH.getNameInDatabase(), chapterMetadata.getChapterType());
        assertEquals(1, chapterMetadata.getChapterNumber());
        assertEquals(7, chapterMetadata.getNumAyahs());
        assertEquals(1, chapterMetadata.getSurahNumber());
        assertEquals(1, chapterMetadata.getAyahNumber());
    }

    @Test(expected = QuranDatabaseException.class)
    public void getMetadataForChapter_with_chapter_type_surah_and_chapter_number_0() {
        // When.
        quranDatabase.getMetadataForChapter(ChapterType.SURAH, 0);
    }

    @Test(expected = QuranDatabaseException.class)
    public void getMetadataForChapter_with_chapter_type_surah_and_chapter_number_115() {
        // When.
        quranDatabase.getMetadataForChapter(ChapterType.SURAH, 115);
    }

    @Test
    public void getMetadataForChapterType_with_chapter_type_surah() {
        // Given.
        ChapterType chapterType = ChapterType.SURAH;

        // When.
        List<ChapterMetadata> chapterMetadataList = quranDatabase.getMetadataForChapterType(chapterType);

        // Then.
        assertEquals(114, chapterMetadataList.size());

        // And.
        assertChapterTypeAndChapterNumberInChapterMetadataList(chapterType, chapterMetadataList);

        // And.
        assertTotalNumberOfAyahsInChapterMetadataList(chapterMetadataList);
    }

    @Test
    public void getMetadataForChapterType_with_chapter_type_juz() {
        // Given.
        ChapterType chapterType = ChapterType.JUZ;

        // When.
        List<ChapterMetadata> chapterMetadataList = quranDatabase.getMetadataForChapterType(chapterType);

        // Then.
        assertEquals(30, chapterMetadataList.size());

        // And.
        assertChapterTypeAndChapterNumberInChapterMetadataList(chapterType, chapterMetadataList);

        // And.
        assertTotalNumberOfAyahsInChapterMetadataList(chapterMetadataList);
    }

    @Test
    public void getMetadataForChapterType_with_chapter_type_hizb() {
        // Given.
        ChapterType chapterType = ChapterType.HIZB;

        // When.
        List<ChapterMetadata> chapterMetadataList = quranDatabase.getMetadataForChapterType(chapterType);

        // Then.
        assertEquals(60, chapterMetadataList.size());

        // And.
        assertChapterTypeAndChapterNumberInChapterMetadataList(chapterType, chapterMetadataList);

        // And.
        assertTotalNumberOfAyahsInChapterMetadataList(chapterMetadataList);
    }

    @Test
    public void getMetadataForChapterType_with_chapter_type_hizb_quarter() {
        // Given.
        ChapterType chapterType = ChapterType.HIZB_QUARTER;

        // When.
        List<ChapterMetadata> chapterMetadataList = quranDatabase.getMetadataForChapterType(chapterType);

        // Then.
        assertEquals(240, chapterMetadataList.size());

        // And.
        assertChapterTypeAndChapterNumberInChapterMetadataList(chapterType, chapterMetadataList);

        // And.
        assertTotalNumberOfAyahsInChapterMetadataList(chapterMetadataList);
    }

    @Test
    public void number_of_verses_in_each_hizb_matches_the_number_of_verses_in_each_hizb_quarter() {
        List<ChapterMetadata> hizbMetadataList = quranDatabase.getMetadataForChapterType(ChapterType.HIZB);
        List<ChapterMetadata> hizbQuarterMetadataList = quranDatabase.getMetadataForChapterType(ChapterType.HIZB_QUARTER);

        for (int i = 0; i < hizbMetadataList.size(); i++) {
            int expected = hizbMetadataList.get(i).getNumAyahs();

            int actual = (hizbQuarterMetadataList.get(i * 4).getNumAyahs()
                    + hizbQuarterMetadataList.get(i * 4 + 1).getNumAyahs()
                    + hizbQuarterMetadataList.get(i * 4 + 2).getNumAyahs()
                    + hizbQuarterMetadataList.get(i * 4 + 3).getNumAyahs());

            assertEquals("HIZB " + (i + 1), expected, actual);
        }
    }

    @Test
    public void number_of_verses_in_each_juz_matches_the_number_of_verses_in_each_hizb() {
        List<ChapterMetadata> juzMetadataList = quranDatabase.getMetadataForChapterType(ChapterType.JUZ);
        List<ChapterMetadata> hizbMetadataList = quranDatabase.getMetadataForChapterType(ChapterType.HIZB);

        for (int i = 0; i < juzMetadataList.size(); i++) {
            int expected = juzMetadataList.get(i).getNumAyahs();

            int actual = hizbMetadataList.get(i * 2).getNumAyahs() + hizbMetadataList.get(i * 2 + 1).getNumAyahs();

            assertEquals("JUZ " + (i + 1), expected, actual);
        }
    }

    @Test
    public void surahNumber_and_ayahNumber_in_each_surah_is_as_expected() {
        List<ChapterMetadata> surahMetadataList = quranDatabase.getMetadataForChapterType(ChapterType.SURAH);

        for (int i = 0; i < surahMetadataList.size(); i++) {
            ChapterMetadata surahMetadata = surahMetadataList.get(i);

            assertEquals((i + 1), surahMetadata.getSurahNumber());
            assertEquals(1, surahMetadata.getAyahNumber());
        }
    }

    @Test
    public void surahNumber_and_ayahNumber_in_each_juz_is_as_expected() {
        assertSurahAndVerseNumberOfFirstVerseInEachTarget(ChapterType.JUZ);
    }

    @Test
    public void surahNumber_and_ayahNumber_in_each_hizb_is_as_expected() {
        assertSurahAndVerseNumberOfFirstVerseInEachTarget(ChapterType.HIZB);
    }

    @Test
    public void surahNumber_and_ayahNumber_in_each_hizb_quarter_is_as_expected() {
        assertSurahAndVerseNumberOfFirstVerseInEachTarget(ChapterType.HIZB_QUARTER);
    }

    private void assertChapterTypeAndChapterNumberInChapterMetadataList(ChapterType chapterType,
                                                                        List<ChapterMetadata> chapterMetadataList) {
        for (int i = 0; i < chapterMetadataList.size(); i++) {
            ChapterMetadata chapterMetadata = chapterMetadataList.get(i);

            assertEquals(chapterType.getNameInDatabase(), chapterMetadata.getChapterType());
            assertEquals(i + 1, chapterMetadata.getChapterNumber());
        }
    }

    private void assertTotalNumberOfAyahsInChapterMetadataList(List<ChapterMetadata> chapterMetadataList) {
        int count = 0;

        for (ChapterMetadata chapterMetadata : chapterMetadataList) {
            count += chapterMetadata.getNumAyahs();
        }

        // And.
        assertEquals(6236, count);
    }

    private void assertSurahAndVerseNumberOfFirstVerseInEachTarget(ChapterType chapterType) {
        List<ChapterMetadata> chapterMetadataList = quranDatabase.getMetadataForChapterType(chapterType);

        for (int i = 0; i < chapterMetadataList.size(); i++) {
            int surahNumberA = chapterMetadataList.get(i).getSurahNumber();
            int verseNumberA = chapterMetadataList.get(i).getAyahNumber();

            int surahNumberB;
            int verseNumberB;

            if (i < chapterMetadataList.size() - 1) {
                surahNumberB = chapterMetadataList.get(i + 1).getSurahNumber();
                verseNumberB = chapterMetadataList.get(i + 1).getAyahNumber();
            } else {
                surahNumberB = 115;
                verseNumberB = 1;
            }

            int count = getNumberOfVersesInBetween(
                    surahNumberA,
                    verseNumberA,
                    surahNumberB,
                    verseNumberB
            );

            assertEquals(
                    "Unexpected number of ayahs between " + chapterType.name() + " " + (i + 1) + " and the next one.",
                    count,
                    chapterMetadataList.get(i).getNumAyahs()
            );
        }
    }

    /**
     * @return the number of verses between location A and location B,
     * or -1 if location B is not greater than location A.
     */
    private int getNumberOfVersesInBetween(
            int surahNumberA,
            int verseNumberA,
            int surahNumberB,
            int verseNumberB
    ) {
        if (surahNumberB < surahNumberA) {
            return -1;
        } else if (surahNumberB == surahNumberA && verseNumberB <= verseNumberA) {
            return -1;
        } else if (surahNumberB == surahNumberA) {
            return verseNumberB - verseNumberA;
        } else {
            int count = 0;

            ChapterMetadata surahA = quranDatabase.getMetadataForChapter(ChapterType.SURAH, surahNumberA);
            count += surahA.getNumAyahs() + 1 - verseNumberA;

            // add to count the number of verses in each Surah between A and B (exclusive of A and B)
            for (int i = surahNumberA + 1; i < surahNumberB; i++) {
                ChapterMetadata surahIth = quranDatabase.getMetadataForChapter(ChapterType.SURAH, i);
                count += surahIth.getNumAyahs();
            }

            count += verseNumberB - 1;

            return count;
        }
    }
}
