package com.thinkincode.quran.sdk.database;

import android.support.test.runner.AndroidJUnit4;

import com.thinkincode.quran.sdk.BaseTestCase;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static com.thinkincode.quran.sdk.matchers.StringHasLengthGreaterThan.hasLengthGreaterThan;
import static com.thinkincode.quran.sdk.matchers.StringHasLengthLessThan.hasLengthLessThan;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNot.not;
import static org.hamcrest.core.IsNull.nullValue;
import static org.junit.Assert.assertThat;

@RunWith(AndroidJUnit4.class)
public class HifdhTipsTest extends BaseTestCase {

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
        assertThat(quote, is(not(nullValue())));
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
