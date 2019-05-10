package com.tazkiyatech.quran.sdk.model

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
            return values()[ordinal]
        }
    }
}
