package com.thinkincode.quran.sdk.database;

import android.support.test.runner.AndroidJUnit4;

import com.thinkincode.quran.sdk.BaseTestCase;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNot.not;
import static org.hamcrest.core.IsNull.nullValue;
import static org.junit.Assert.assertThat;

@RunWith(AndroidJUnit4.class)
public class QuranQuotesTest extends BaseTestCase {

    @Test
    public void getNextRandom() {
        // Given.
        QuranQuotes quranQuotes = new QuranQuotes(getTargetContext().getResources());

        // When.
        String quote = quranQuotes.getNextRandom();

        // Then.
        assertThat(quote, is(not(nullValue())));
    }
}
