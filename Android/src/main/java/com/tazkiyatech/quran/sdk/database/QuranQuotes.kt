package com.tazkiyatech.quran.sdk.database

import android.content.res.Resources

import com.tazkiyatech.quran.sdk.R
import com.tazkiyatech.quran.sdk.extensions.getPackedDate
import java.util.*

/**
 * Helper class which provides quotes and reminders relating to the importance and virtues of the Quran.
 *
 * @property resources The application's [Resources] instance.
 */
class QuranQuotes(private val resources: Resources) {

    private val random: Random = Random()

    /**
     * @return A randomly selected quote.
     */
    val nextRandom: String
        get() = getQuote(random.nextInt(size))

    /**
     * @return The quote which is unique to the current day.
     */
    val quoteOfTheDay: String
        get() {
            val packedDate = Calendar.getInstance().getPackedDate()

            val tipOfTheDayIndex = packedDate.rem(size)

            return getQuote(tipOfTheDayIndex)
        }

    /**
     * The number of quotes available.
     */
    val size: Int
        get() = quotesArray.size

    /**
     * The [Array] of quotes.
     */
    private val quotesArray: Array<String>
        get() = resources.getStringArray(R.array.quran_quotes)

    /**
     * @param index The index of the quote to get.
     * @return The quote at the specified index.
     */
    fun getQuote(index: Int): String {
        return quotesArray[index]
    }
}
