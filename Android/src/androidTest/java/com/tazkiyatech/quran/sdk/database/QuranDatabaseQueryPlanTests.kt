package com.tazkiyatech.quran.sdk.database

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.tazkiyatech.utils.database.QueryPlanExplainer
import com.tazkiyatech.utils.database.QueryPlanRow
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class QuranDatabaseQueryPlanTests {

    private lateinit var queryPlanExplainer: QueryPlanExplainer

    @Before
    fun setUp() {
        val quranDatabase = QuranDatabase(ApplicationProvider.getApplicationContext<Context>())
        quranDatabase.openDatabase()

        val sqLiteDatabase = quranDatabase.sqLiteDatabase
        queryPlanExplainer = QueryPlanExplainer(sqLiteDatabase!!)
    }

    @Test
    fun test_explainQueryPlan_for_getSurahNames() {
        // Given.
        val expected = listOf(
            QueryPlanRow(
                0,
                0,
                0,
                "SCAN TABLE sura_names USING INDEX index_sura_number_on_table_sura_names"
            )
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
            QueryPlanRow(
                0,
                0,
                0,
                "SEARCH TABLE sura_names USING INDEX index_sura_number_on_table_sura_names (sura=?)"
            )
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
            QueryPlanRow(
                0,
                0,
                0,
                "SEARCH TABLE quran_text USING INDEX index_sura_number_aya_number_on_table_quran_text (sura=?)"
            )
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
            QueryPlanRow(
                0,
                0,
                0,
                "SEARCH TABLE quran_text USING INDEX index_sura_number_aya_number_on_table_quran_text (sura=? AND aya=?)"
            )
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
    fun test_explainQueryPlan_for_getMetadataForChapterType() {
        // Given.
        val expected = listOf(
            QueryPlanRow(
                0,
                0,
                0,
                "SEARCH TABLE quran_metadata USING INDEX index_chapter_type_chapter_number_on_table_quran_metadata (chapter_type=?)"
            )
        )

        // When.
        val actual = queryPlanExplainer.explainQueryPlanForSelectStatement(
            "quran_metadata", null,
            "chapter_type = ?",
            arrayOf("sura"), null, null,
            "chapter_type ASC, chapter_number ASC", null
        )

        // Then.
        assertEquals(expected, actual)
    }

    @Test
    fun test_explainQueryPlan_for_getMetadataForChapter() {
        // Given.
        val expected = listOf(
            QueryPlanRow(
                0,
                0,
                0,
                "SEARCH TABLE quran_metadata USING INDEX index_chapter_type_chapter_number_on_table_quran_metadata (chapter_type=? AND chapter_number=?)"
            )
        )

        // When.
        val actual = queryPlanExplainer.explainQueryPlanForSelectStatement(
            "quran_metadata", null,
            "chapter_type = ? AND chapter_number = ?",
            arrayOf("sura", "1"), null, null, null,
            "1"
        )

        // Then.
        assertEquals(expected, actual)
    }
}
