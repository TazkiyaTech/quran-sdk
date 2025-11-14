package com.tazkiyatech.quran.sdk.extensions

import java.util.*

/**
 * Extracts the year, month and day-of-month values from this [Calendar] instance
 * and joins them up into a number of the form `YYYYMMdd`.
 *
 * @return A number in the form `YYYYMMdd`, e.g. `20220109`.
 */
fun Calendar.getPackedDate(): Int {
    return getPackedDate(getYear(), getMonth(), getDayOfMonth())
}

/**
 * Joins up the given `year`, `month` and `dayOfMonth` values
 * into a number of the form `YYYYMMdd`.
 *
 * @return A number in the form `YYYYMMdd`, e.g. `20220109`.
 */
private fun getPackedDate(year: Int, month: Int, dayOfMonth: Int): Int {
    return (year * 10000) + (month * 100) + dayOfMonth
}

/**
 * @return the [Calendar.DAY_OF_MONTH] value of this [Calendar] instance.
 */
private fun Calendar.getDayOfMonth() = get(Calendar.DAY_OF_MONTH)

/**
 * @return the [Calendar.MONTH] value of this [Calendar] instance plus one
 * so it's the actual month number and not the month index.
 */
private fun Calendar.getMonth() = getMonthIndex() + 1

/**
 * @return the [Calendar.MONTH] value of this [Calendar] instance.
 */
private fun Calendar.getMonthIndex() = get(Calendar.MONTH)

/**
 * @return the [Calendar.YEAR] value of this [Calendar] instance.
 */
private fun Calendar.getYear() = get(Calendar.YEAR)