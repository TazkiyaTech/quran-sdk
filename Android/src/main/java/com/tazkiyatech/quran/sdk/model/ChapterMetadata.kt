package com.tazkiyatech.quran.sdk.model

/**
 * Metadata which describes a chapter.
 *
 * @property chapterType The type of the chapter.
 * @property chapterNumber The number of this chapter within its chapter type.
 * @property numAyahs The number of Ayahs that this chapter consists of.
 * @property surahNumber The Surah number of the first verse in this chapter.
 * @property ayahNumber The Ayah number of the first verse in this chapter (relative to the Surah).
 */
data class ChapterMetadata(
    val chapterType: String,
    val chapterNumber: Int,
    val numAyahs: Int,
    val surahNumber: Int,
    val ayahNumber: Int
)