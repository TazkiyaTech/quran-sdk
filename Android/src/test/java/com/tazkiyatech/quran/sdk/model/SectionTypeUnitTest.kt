package com.tazkiyatech.quran.sdk.model

import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class SectionTypeUnitTest {

    @Test
    fun parseOrdinal() {
        // When.
        val result = SectionType.parseOrdinal(0)

        // Then.
        assertEquals(SectionType.SURAH, result)
    }
}