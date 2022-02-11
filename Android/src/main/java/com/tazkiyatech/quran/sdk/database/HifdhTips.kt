package com.tazkiyatech.quran.sdk.database

import android.content.res.Resources
import com.tazkiyatech.quran.sdk.R
import com.tazkiyatech.quran.sdk.extensions.getPackedDate
import java.util.*

/**
 * Helper class which provides Hifdh tips.
 *
 * @property resources The application's [Resources] instance.
 */
class HifdhTips(private val resources: Resources) {

    private val random: Random = Random()

    /**
     * @return A randomly selected Hifdh tip.
     */
    val nextRandom: String
        get() = getTip(random.nextInt(size))

    /**
     * @return The Hifdh tip which is unique to the current day.
     */
    val tipOfTheDay: String
        get() {
            val packedDate = Calendar.getInstance().getPackedDate()

            val tipOfTheDayIndex = packedDate.rem(size)

            return getTip(tipOfTheDayIndex)
        }

    /**
     * The number of Hifdh tips available.
     */
    val size: Int
        get() = tipsArray.size

    /**
     * The [Array] of Hifdh tips.
     */
    private val tipsArray: Array<String>
        get() = resources.getStringArray(R.array.hifdh_tips)

    /**
     * @param index The index of the Hifdh tip to get.
     * @return The Hifdh tip at the specified index.
     */
    fun getTip(index: Int): String {
        return tipsArray[index]
    }
}
