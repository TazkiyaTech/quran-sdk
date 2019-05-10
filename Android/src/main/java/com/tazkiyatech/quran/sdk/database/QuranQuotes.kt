package com.tazkiyatech.quran.sdk.database

import android.content.res.Resources

import com.tazkiyatech.quran.sdk.R

import java.util.Random

/**
 * Helper class which provides quotes and reminders relating to the importance and virtues of the Quran.
 *
 * @property resources The application's [Resources] instance.
 */
class QuranQuotes(private val resources: Resources) {

    private val random: Random = Random()

    /**
     * @return a randomly selected quote.
     */
    val nextRandom: String
        get() = getQuote(random.nextInt(size))

    /**
     * The number of quotes available.
     */
    internal val size: Int
        get() = quotesArray.size

    /**
     * The [Array] of quotes.
     */
    private val quotesArray: Array<String>
        get() = resources.getStringArray(R.array.quran_quotes)

    /**
     * @param index the index of the quote to get.
     * @return the quote at the specified index.
     */
    internal fun getQuote(index: Int): String {
        return quotesArray[index]
    }
}
