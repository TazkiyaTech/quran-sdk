package com.tazkiyatech.quran.sdk.database

import android.content.res.Resources
import com.tazkiyatech.quran.sdk.R
import java.util.*

/**
 * Helper class which provides Hifdh tips.
 *
 * @property resources The application's [Resources] instance.
 */
class HifdhTips(private val resources: Resources) {

    private val random: Random = Random()

    /**
     * @return a randomly selected Hifdh tip.
     */
    val nextRandom: String
        get() = getTip(random.nextInt(size))

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
     * @param index the index of the Hifdh tip to get.
     * @return the Hifdh tip at the specified index.
     */
    fun getTip(index: Int): String {
        return tipsArray[index]
    }
}
