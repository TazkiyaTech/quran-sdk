package com.tazkiyatech.quran.sdk.database;

import com.tazkiyatech.quran.sdk.BaseTestCase;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import static com.tazkiyatech.quran.sdk.matchers.StringHasLengthGreaterThan.hasLengthGreaterThan;
import static com.tazkiyatech.quran.sdk.matchers.StringHasLengthLessThan.hasLengthLessThan;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

@RunWith(AndroidJUnit4.class)
public class QuranQuotesInstrumentationTests extends BaseTestCase {

    private QuranQuotes quranQuotes;

    @Before
    public void setUp() {
        quranQuotes = new QuranQuotes(getTargetContext().getResources());
    }

    @Test
    public void getNextRandom() {
        // When.
        String quote = quranQuotes.getNextRandom();

        // Then.
        assertThat(quote, hasLengthGreaterThan(0));
    }

    @Test
    public void getSize() {
        // When.
        int size = quranQuotes.getSize();

        // Then.
        assertThat(size, is(greaterThan(0)));
    }

    @Test
    public void allQuotesHaveLessThan281Characters() {
        // When / Then.
        for (int i = 0; i < quranQuotes.getSize(); i++) {
            String quote = quranQuotes.getQuote(i);
            assertThat(quote, hasLengthGreaterThan(0));
            assertThat(quote, hasLengthLessThan(Constants.MAX_QUOTE_LENGTH));
        }
    }
}
