package com.tazkiyatech.quran.sdk.model

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class SectionTypeUnitTest {

    @Test
    fun parseOrdinal() {
        // When.
        val result = SectionType.parseOrdinal(0)

        // Then.
        assertEquals(SectionType.SURAH, result)
    }
}