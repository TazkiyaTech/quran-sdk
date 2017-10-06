package com.thinkincode.quran.sdk.database;

import android.support.test.runner.AndroidJUnit4;

import com.thinkincode.quran.sdk.BaseTestCase;
import com.thinkincode.quran.sdk.exception.QuranDatabaseException;
import com.thinkincode.quran.sdk.model.Surah;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.core.IsNot.not;
import static org.hamcrest.core.IsNull.nullValue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

@RunWith(AndroidJUnit4.class)
public class QuranDatabaseTest extends BaseTestCase {

    private QuranDatabase quranDatabase;

    @Before
    public void setUp() throws Exception {
        quranDatabase = new QuranDatabase(getTargetContext());
        quranDatabase.initialise();
    }

    @After
    public void tearDown() {
        quranDatabase.closeDatabase();
    }

    @Test
    public void testIsDatabaseExistsInInternalStorage() {
        // When.
        boolean result = quranDatabase.isDatabaseExistsInInternalStorage();

        // Then.
        assertTrue(result);
    }

    @Test
    public void testIsDatabaseOpen_whenDatabaseOpened() {
        // Given.
        quranDatabase.openDatabase();

        // When.
        boolean result = quranDatabase.isDatabaseOpen();

        // Then.
        assertTrue(result);
    }

    @Test
    public void testIsDatabaseOpen_whenDatabaseNotOpened() {
        // When.
        boolean result = quranDatabase.isDatabaseOpen();

        // Then.
        assertFalse(result);
    }

    @Test
    public void testGetSurahName_withValidSurahNumber() {
        // When.
        String surahName = quranDatabase.getSurahName(1);

        // Then.
        assertThat(surahName, is(not(nullValue())));
    }

    @Test(expected = QuranDatabaseException.class)
    public void testGetSurahName_withInvalidSurahNumber() {
        // When.
        quranDatabase.getSurahName(115);
    }

    @Test
    public void testGetSurahNames() {
        // When.
        List<String> surahNames = quranDatabase.getSurahNames();

        // Then.
        assertThat(surahNames, hasSize(114));
    }

    @Test
    public void testGetAyahsInSurah_withValidSurahNumber() {
        // Given.
        Surah[] surahs = Surah.values();

        for (int surahNumber = 1; surahNumber <= surahs.length; surahNumber++) {
            // Given.
            Surah surah = surahs[surahNumber - 1];

            // When.
            List<String> ayahsInSurah = quranDatabase.getAyahsInSurah(surahNumber);

            // Then.
            assertThat(ayahsInSurah.size(), is(equalTo(surah.getNumVerses())));
        }
    }

    @Test(expected = QuranDatabaseException.class)
    public void testGetAyahsInSurah_withInvalidSurahNumber() {
        quranDatabase.getAyahsInSurah(115);
    }

    @Test
    public void testGetAyah_withValidSurahNumberAndAyahNumber() {
        // When.
        String ayah = quranDatabase.getAyah(1, 1);

        // Then.
        assertThat(ayah, is(not(nullValue())));
    }

    @Test(expected = QuranDatabaseException.class)
    public void testGetAyah_withInvalidSurahNumber() {
        // When.
        quranDatabase.getAyah(115, 1);
    }

    @Test(expected = QuranDatabaseException.class)
    public void testGetAyah_withInvalidAyahNumber() {
        // When.
        quranDatabase.getAyah(1, 8);
    }
}
