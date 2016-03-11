package com.thinkincode.quranutils.database;

import android.support.test.runner.AndroidJUnit4;

import com.thinkincode.quranutils.BaseTestCase;
import com.thinkincode.quranutils.model.SurahEnum;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.util.List;

import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;

@RunWith(AndroidJUnit4.class)
public class QuranDatabaseHelperTest extends BaseTestCase {

    private QuranDatabaseHelper quranDatabaseHelper;

    @Before
    public void setUp() throws IOException {
        quranDatabaseHelper = new QuranDatabaseHelper();
        quranDatabaseHelper.createDatabaseIfDoesNotExist(getTargetContext());
    }

    @After
    public void tearDown() {
        quranDatabaseHelper.closeDatabase();
    }

    @Test
	public void testGetSurahNames() {
		// When.
		List<String> surahNames = quranDatabaseHelper.getSurahNames(getTargetContext());

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
			List<String> ayahsInSurah = quranDatabaseHelper.getAyahsInSurah(getTargetContext(), i);

			// Then.
            assertThat(ayahsInSurah.size(), is(equalTo(surahEnum.getNumVerses())));
		}
	}
}
