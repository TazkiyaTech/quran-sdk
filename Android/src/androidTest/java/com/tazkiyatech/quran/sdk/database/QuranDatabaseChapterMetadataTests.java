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

    private static int NUMBER_OF_AYAHS_IN_THE_QURAN = 6236;

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
        ChapterMetadata actual = quranDatabase.getMetadataForChapter(ChapterType.SURAH, 1);

        // Then.
        assertEquals(ChapterType.SURAH.getNameInDatabase(), actual.getChapterType());
        assertEquals(1, actual.getChapterNumber());
        assertEquals(7, actual.getNumAyahs());
        assertEquals(1, actual.getSurahNumber());
        assertEquals(1, actual.getAyahNumber());
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
    public void getMetadataForChapters_with_chapter_type_surah() {
        // Given.
        ChapterType chapterType = ChapterType.SURAH;

        // When.
        List<ChapterMetadata> actual = quranDatabase.getMetadataForChapters(chapterType);

        // Then.
        assertEquals(114, actual.size());

        // And.
        int count = 0;

        for (int i = 0; i < actual.size(); i++) {
            ChapterMetadata chapterMetadata = actual.get(i);

            assertEquals(chapterType.getNameInDatabase(), chapterMetadata.getChapterType());
            assertEquals(i + 1, chapterMetadata.getChapterNumber());

            count += chapterMetadata.getNumAyahs();
        }

        // And.
        assertEquals(NUMBER_OF_AYAHS_IN_THE_QURAN, count);
    }

    @Test
    public void getMetadataForChapters_with_chapter_type_juz() {
        // Given.
        ChapterType chapterType = ChapterType.JUZ;

        // When.
        List<ChapterMetadata> actual = quranDatabase.getMetadataForChapters(chapterType);

        // Then.
        assertEquals(30, actual.size());

        // And.
        int count = 0;

        for (int i = 0; i < actual.size(); i++) {
            ChapterMetadata chapterMetadata = actual.get(i);

            assertEquals(chapterType.getNameInDatabase(), chapterMetadata.getChapterType());
            assertEquals(i + 1, chapterMetadata.getChapterNumber());

            count += chapterMetadata.getNumAyahs();
        }

        // And.
        assertEquals(NUMBER_OF_AYAHS_IN_THE_QURAN, count);
    }

    @Test
    public void getMetadataForChapters_with_chapter_type_hizb() {
        // Given.
        ChapterType chapterType = ChapterType.HIZB;

        // When.
        List<ChapterMetadata> actual = quranDatabase.getMetadataForChapters(chapterType);

        // Then.
        assertEquals(60, actual.size());

        // And.
        int count = 0;

        for (int i = 0; i < actual.size(); i++) {
            ChapterMetadata chapterMetadata = actual.get(i);

            assertEquals(chapterType.getNameInDatabase(), chapterMetadata.getChapterType());
            assertEquals(i + 1, chapterMetadata.getChapterNumber());

            count += chapterMetadata.getNumAyahs();
        }

        // And.
        assertEquals(NUMBER_OF_AYAHS_IN_THE_QURAN, count);
    }

    @Test
    public void getMetadataForChapters_with_chapter_type_hizb_quarter() {
        // Given.
        ChapterType chapterType = ChapterType.HIZB_QUARTER;

        // When.
        List<ChapterMetadata> actual = quranDatabase.getMetadataForChapters(chapterType);

        // Then.
        assertEquals(240, actual.size());

        // And.
        int count = 0;

        for (int i = 0; i < actual.size(); i++) {
            ChapterMetadata chapterMetadata = actual.get(i);

            assertEquals(chapterType.getNameInDatabase(), chapterMetadata.getChapterType());
            assertEquals(i + 1, chapterMetadata.getChapterNumber());

            count += chapterMetadata.getNumAyahs();
        }

        // And.
        assertEquals(NUMBER_OF_AYAHS_IN_THE_QURAN, count);
    }
}
