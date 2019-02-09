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
public class HifdhTipsInstrumentationTests extends BaseTestCase {

    private HifdhTips hifdhTips;

    @Before
    public void setUp() {
        hifdhTips = new HifdhTips(getTargetContext().getResources());
    }

    @Test
    public void getNextRandom() {
        // When.
        String quote = hifdhTips.getNextRandom();

        // Then.
        assertThat(quote, hasLengthGreaterThan(0));
    }

    @Test
    public void getSize() {
        // When.
        int size = hifdhTips.getSize();

        // Then.
        assertThat(size, is(greaterThan(0)));
    }

    @Test
    public void allQuotesHaveLessThan281Characters() {
        // When / Then.
        for (int i = 0; i < hifdhTips.getSize(); i++) {
            String quote = hifdhTips.getTip(i);
            assertThat(quote, hasLengthGreaterThan(0));
            assertThat(quote, hasLengthLessThan(Constants.MAX_QUOTE_LENGTH));
        }
    }
}
