package com.tazkiyatech.quran.sdk.model

/**
 * An enum of the different ways that the Quran is sectioned in the Madinah and Majeedi mushafs.
 *
 * The Madinah script mushaf is the mushaf common in the Arab world which sections the Quran by Hizbs and Hizb-Quarters.
 *
 * The Majeedi script mushaf is the simplified mushaf common in South Asia which sections the Quran by Rukus and Juz-Quarters.
 *
 * @param nameInDatabase The name of the section type in the 'section_type' column of the 'quran_metadata' table.
 */
enum class SectionType(val nameInDatabase: String) {

    SURAH("surah"),
    JUZ_IN_MADINAH_MUSHAF("juz_in_madinah_mushaf"),
    HIZB_IN_MADINAH_MUSHAF("hizb_in_madinah_mushaf"),
    HIZB_QUARTER_IN_MADINAH_MUSHAF("hizb_quarter_in_madinah_mushaf"),
    JUZ_IN_MAJEEDI_MUSHAF("juz_in_majeedi_mushaf"),
    JUZ_QUARTER_IN_MAJEEDI_MUSHAF("juz_quarter_in_majeedi_mushaf");

    companion object {
        fun parseOrdinal(ordinal: Int): SectionType {
            val values = values()

            return if (ordinal < 0 || ordinal >= values.size) {
                throw IllegalArgumentException("Bad section type ordinal ($ordinal) passed in.")
            } else {
                values[ordinal]
            }
        }
    }
}
