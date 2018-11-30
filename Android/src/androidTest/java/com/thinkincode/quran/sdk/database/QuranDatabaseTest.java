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

import static com.thinkincode.quran.sdk.matchers.StringHasLengthGreaterThan.hasLengthGreaterThan;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNot.not;
import static org.hamcrest.core.IsNull.nullValue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

@RunWith(AndroidJUnit4.class)
public class QuranDatabaseTest extends BaseTestCase {

    private QuranDatabase quranDatabase;

    @Before
    public void setUp() {
        quranDatabase = new QuranDatabase(getTargetContext());
    }

    @After
    public void tearDown() {
        quranDatabase.closeDatabase();
    }

    @Test
    public void isDatabaseExistsInInternalStorage_when_database_opened() {
        // Given.
        quranDatabase.openDatabase();

        // When.
        boolean result = quranDatabase.isDatabaseExistsInInternalStorage();

        // Then.
        assertTrue(result);
    }

    @Test
    public void isDatabaseOpen_when_database_opened() {
        // Given.
        quranDatabase.openDatabase();

        // When.
        boolean result = quranDatabase.isDatabaseOpen();

        // Then.
        assertTrue(result);
    }

    @Test
    public void isDatabaseOpen_when_database_not_opened() {
        // When.
        boolean result = quranDatabase.isDatabaseOpen();

        // Then.
        assertFalse(result);
    }

    @Test
    public void getSurahName_with_surah_number_1() {
        // When.
        String surahName = quranDatabase.getSurahName(1);

        // Then.
        assertThat(surahName, is(not(nullValue())));
    }

    @Test(expected = QuranDatabaseException.class)
    public void getSurahName_with_invalid_surah_number() {
        // When.
        quranDatabase.getSurahName(115);
    }

    @Test
    public void getSurahNames() {
        // When.
        List<String> surahNames = quranDatabase.getSurahNames();

        // Then.
        assertThat(surahNames, hasSize(114));
    }

    @Test
    public void getAyahsInSurah_with_valid_surah_number() {
        // Given.
        Surah[] surahs = Surah.values();

        for (int surahNumber = 1; surahNumber <= surahs.length; surahNumber++) {
            // Given.
            int expectedNumberOfVerses = surahs[surahNumber - 1].getNumVerses();

            // When.
            List<String> ayahsInSurah = quranDatabase.getAyahsInSurah(surahNumber);

            // Then.
            assertThat(ayahsInSurah, hasSize(expectedNumberOfVerses));
        }
    }

    @Test(expected = QuranDatabaseException.class)
    public void getAyahsInSurah_with_invalid_surah_number() {
        quranDatabase.getAyahsInSurah(115);
    }

    @Test
    public void getAyah_with_surah_number_1_and_ayah_number_1() {
        // Given.
        String expected = "بِسْمِ اللَّهِ الرَّحْمَٰنِ الرَّحِيمِ";

        // When.
        String actual = quranDatabase.getAyah(1, 1);

        // Then.
        assertEquals(expected, actual);
    }

    @Test
    public void getAyah_with_surah_number_58_and_ayah_number_6() {
        // Given.
        String expected = "يَوْمَ يَبْعَثُهُمُ اللَّهُ جَمِيعًا فَيُنَبِّئُهُمْ بِمَا عَمِلُوا ۚ أَحْصَاهُ اللَّهُ وَنَسُوهُ ۚ وَاللَّهُ عَلَىٰ كُلِّ شَيْءٍ شَهِيدٌ";

        // When.
        String actual = quranDatabase.getAyah(58, 6);

        // Then.
        assertEquals(expected, actual);
    }

    @Test
    public void getAyah_with_valid_surah_number_and_ayah_number() {
        // When.
        String ayah = quranDatabase.getAyah(1, 1);

        // Then.
        assertThat(ayah, hasLengthGreaterThan(0));
    }

    @Test(expected = QuranDatabaseException.class)
    public void getAyah_with_invalid_surah_number() {
        // When.
        quranDatabase.getAyah(115, 1);
    }

    @Test(expected = QuranDatabaseException.class)
    public void getAyah_with_invalid_ayah_number() {
        // When.
        quranDatabase.getAyah(1, 8);
    }
}
