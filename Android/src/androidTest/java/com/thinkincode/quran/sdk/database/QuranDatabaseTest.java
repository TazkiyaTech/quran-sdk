package com.thinkincode.quran.sdk.database;

import android.support.test.runner.AndroidJUnit4;

import com.thinkincode.quran.sdk.BaseTestCase;
import com.thinkincode.quran.sdk.model.SurahEnum;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.util.List;

import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.core.IsNot.not;
import static org.hamcrest.core.IsNull.nullValue;
import static org.junit.Assert.assertThat;

@RunWith(AndroidJUnit4.class)
public class QuranDatabaseTest extends BaseTestCase {

    private QuranDatabase quranDatabase;

    @Before
    public void setUp() throws IOException {
        quranDatabase = new QuranDatabase();
        quranDatabase.openDatabase(getTargetContext());
    }

    @After
    public void tearDown() {
        quranDatabase.closeDatabase();
    }

    @Test
    public void testGetSurahName() {
        // When.
        String surahName = quranDatabase.getSurahName(getTargetContext(), 1);

        // Then.
        assertThat(surahName, is(not(nullValue())));
    }

    @Test
	public void testGetSurahNames() {
		// When.
		List<String> surahNames = quranDatabase.getSurahNames(getTargetContext());

		// Then.
        assertThat(surahNames, hasSize(114));
	}

    @Test
	public void testGetAyahsInSurah() {
		// Given.
		SurahEnum[] surahs = SurahEnum.values();

		for (int i = 1; i <= surahs.length; i++) {
            // Given.
            SurahEnum surahEnum = surahs[i - 1];

			// When.
			List<String> ayahsInSurah = quranDatabase.getAyahsInSurah(getTargetContext(), i);

			// Then.
            assertThat(ayahsInSurah.size(), is(equalTo(surahEnum.getNumVerses())));
		}
	}

    @Test
    public void testGetAyah() {
        // When.
        String ayah = quranDatabase.getAyah(getTargetContext(), 1, 1);

        // Then.
        assertThat(ayah, is(not(nullValue())));
    }
}
