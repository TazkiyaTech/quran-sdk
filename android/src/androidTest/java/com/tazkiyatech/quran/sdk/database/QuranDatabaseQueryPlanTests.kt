package com.tazkiyatech.quran.sdk.database

import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.tazkiyatech.utils.sqlite.QueryPlanExplainer
import com.tazkiyatech.utils.sqlite.QueryPlanRow
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class QuranDatabaseQueryPlanTests {

    private lateinit var queryPlanExplainer: QueryPlanExplainer

    @Before
    fun setUp() {
        val quranDatabase = QuranDatabase(ApplicationProvider.getApplicationContext())
        quranDatabase.openDatabase()

        val sqLiteDatabase = quranDatabase.sqLiteDatabase
        queryPlanExplainer = QueryPlanExplainer(sqLiteDatabase!!)
    }

    @Test
    fun test_explainQueryPlan_for_getSurahNames() {
        // Given.
        val expected = listOf(
            QueryPlanRow("SCAN sura_names USING INDEX index_sura_number_on_table_sura_names")
        )

        // When.
        val actual = queryPlanExplainer.explainQueryPlanForSelectStatement(
            "sura_names",
            arrayOf("name"),
            null, null, null, null,
            "sura ASC", null
        )

        // Then.
        assertEquals(expected, actual)
    }

    @Test
    fun test_explainQueryPlan_for_getSurahName() {
        // Given.
        val expected = listOf(
            QueryPlanRow("SEARCH sura_names USING INDEX index_sura_number_on_table_sura_names (sura=?)")
        )

        // When.
        val actual = queryPlanExplainer.explainQueryPlanForSelectStatement(
            "sura_names",
            arrayOf("name"),
            "sura = ?",
            arrayOf("1"), null, null, null,
            "1"
        )

        // Then.
        assertEquals(expected, actual)
    }

    @Test
    fun test_explainQueryPlan_for_getAyahsInSurah() {
        // Given.
        val expected = listOf(
            QueryPlanRow("SEARCH quran_text USING INDEX index_sura_number_aya_number_on_table_quran_text (sura=?)")
        )

        // When.
        val actual = queryPlanExplainer.explainQueryPlanForSelectStatement(
            "quran_text", null,
            "sura = ?",
            arrayOf("1"), null, null,
            "aya ASC", null
        )

        // Then.
        assertEquals(expected, actual)
    }

    @Test
    fun test_explainQueryPlan_for_getAyah() {
        // Given.
        val expected = listOf(
            QueryPlanRow("SEARCH quran_text USING INDEX index_sura_number_aya_number_on_table_quran_text (sura=? AND aya=?)")
        )

        // When.
        val actual = queryPlanExplainer.explainQueryPlanForSelectStatement(
            "quran_text", null,
            "sura = ? AND aya = ?",
            arrayOf("1", "1"), null, null, null,
            "1"
        )

        // Then.
        assertEquals(expected, actual)
    }

    @Test
    fun test_explainQueryPlan_for_getMetadataForSectionType() {
        // Given.
        val expected = listOf(
            QueryPlanRow("SEARCH quran_metadata USING INDEX index_section_type_section_number_on_table_quran_metadata (section_type=?)")
        )

        // When.
        val actual = queryPlanExplainer.explainQueryPlanForSelectStatement(
            "quran_metadata", null,
            "section_type = ?",
            arrayOf("sura"), null, null,
            "section_type ASC, section_number ASC", null
        )

        // Then.
        assertEquals(expected, actual)
    }

    @Test
    fun test_explainQueryPlan_for_getMetadataForSection() {
        // Given.
        val expected = listOf(
            QueryPlanRow("SEARCH quran_metadata USING INDEX index_section_type_section_number_on_table_quran_metadata (section_type=? AND section_number=?)")
        )

        // When.
        val actual = queryPlanExplainer.explainQueryPlanForSelectStatement(
            "quran_metadata", null,
            "section_type = ? AND section_number = ?",
            arrayOf("sura", "1"), null, null, null,
            "1"
        )

        // Then.
        assertEquals(expected, actual)
    }
}
