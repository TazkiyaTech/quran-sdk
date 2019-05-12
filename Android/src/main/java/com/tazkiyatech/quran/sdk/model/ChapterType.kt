package com.tazkiyatech.quran.sdk.model

import java.lang.IllegalArgumentException

/**
 * @property nameInDatabase The name of the chapter type in the 'chapter_type' column of the 'quran_metadata' table.
 */
enum class ChapterType(val nameInDatabase: String) {

    SURAH("sura"),
    JUZ("juz"),
    HIZB("hizb"),
    HIZB_QUARTER("hizb_quarter");

    companion object {
        fun parseOrdinal(ordinal: Int): ChapterType {
            val values = values()

            return if (ordinal < 0 || ordinal >= values.size) {
                throw IllegalArgumentException("Bad chapter type ordinal ($ordinal) passed in.")
            } else {
                values[ordinal]
            }
        }
    }
}
