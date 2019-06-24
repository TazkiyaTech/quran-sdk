package com.tazkiyatech.quran.sdk.model

/**
 * Metadata which describes a section of the Quran.
 *
 * @property sectionType The type of the section.
 * @property sectionNumber The number of this section within its section type.
 * @property numAyahs The number of Ayahs that this section consists of.
 * @property surahNumber The Surah number of the first verse in this section.
 * @property ayahNumber The Ayah number of the first verse in this section (relative to the Surah).
 */
data class SectionMetadata(
    val sectionType: String,
    val sectionNumber: Int,
    val numAyahs: Int,
    val surahNumber: Int,
    val ayahNumber: Int
)