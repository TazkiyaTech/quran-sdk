package com.tazkiyatech.quran.sdk.database

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.tazkiyatech.quran.sdk.matchers.StringHasLengthGreaterThan.hasLengthGreaterThan
import com.tazkiyatech.quran.sdk.matchers.StringHasLengthLessThan.hasLengthLessThan
import org.hamcrest.Matchers.greaterThan
import org.hamcrest.core.Is.`is`
import org.junit.Assert.assertThat
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class HifdhTipsTests {

    private lateinit var hifdhTips: HifdhTips

    @Before
    fun setUp() {
        hifdhTips = HifdhTips(ApplicationProvider.getApplicationContext<Context>().resources)
    }

    @Test
    fun getNextRandom() {
        // When.
        val quote = hifdhTips.nextRandom

        // Then.
        assertThat(quote, hasLengthGreaterThan(0))
    }

    @Test
    fun getSize() {
        // When.
        val size = hifdhTips.size

        // Then.
        assertThat(size, `is`(greaterThan(0)))
    }

    @Test
    fun allQuotesHaveLessThanMaximumAllowedQuoteLength() {
        // When / Then.
        for (i in 0 until hifdhTips.size) {
            val quote = hifdhTips.getTip(i)
            assertThat(quote, hasLengthGreaterThan(0))
            assertThat(quote, hasLengthLessThan(Constants.MAX_QUOTE_LENGTH + 1))
        }
    }
}
