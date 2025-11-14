package com.tazkiyatech.quran.sdk.database

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.tazkiyatech.quran.sdk.matchers.StringHasLengthGreaterThan.hasLengthGreaterThan
import com.tazkiyatech.quran.sdk.matchers.StringHasLengthLessThan.hasLengthLessThan
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.greaterThan
import org.hamcrest.core.Is.`is`
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class QuranQuotesTests {

    private lateinit var quranQuotes: QuranQuotes

    @Before
    fun setUp() {
        quranQuotes = QuranQuotes(ApplicationProvider.getApplicationContext<Context>().resources)
    }

    @Test
    fun getNextRandom() {
        // When.
        val quote = quranQuotes.nextRandom

        // Then.
        assertThat(quote, hasLengthGreaterThan(0))
    }

    @Test
    fun getQuoteOfTheDay() {
        // Give.
        val quote1 = quranQuotes.quoteOfTheDay

        // When.
        val quote2 = quranQuotes.quoteOfTheDay

        // Then.
        assertEquals(quote1, quote2)
    }

    @Test
    fun getSize() {
        // When.
        val size = quranQuotes.size

        // Then.
        assertThat(size, `is`(greaterThan(0)))
    }

    @Test
    fun allQuotesHaveLessThanMaximumAllowedQuoteLength() {
        // When / Then.
        for (i in 0 until quranQuotes.size) {
            val quote = quranQuotes.getQuote(i)
            assertThat(quote, hasLengthGreaterThan(0))
            assertThat(quote, hasLengthLessThan(Constants.MAX_QUOTE_LENGTH + 1))
        }
    }
}
