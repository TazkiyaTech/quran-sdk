package com.tazkiyatech.quran.sdk.database;

import android.database.sqlite.SQLiteDatabase;

import com.tazkiyatech.utils.database.QueryPlanExplainer;
import com.tazkiyatech.utils.database.QueryPlanRow;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Collections;
import java.util.List;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import static org.junit.Assert.assertEquals;

@RunWith(AndroidJUnit4.class)
public class QuranDatabaseQueryPlanTests {

    private QueryPlanExplainer queryPlanExplainer;

    @Before
    public void setUp() {
        QuranDatabase quranDatabase = new QuranDatabase(ApplicationProvider.getApplicationContext());
        quranDatabase.openDatabase();

        SQLiteDatabase sqLiteDatabase = quranDatabase.getSQLiteDatabase();
        queryPlanExplainer = new QueryPlanExplainer(sqLiteDatabase);
    }

    @Test
    public void test_explainQueryPlan_for_getSurahNames() {
        // Given.
        List<QueryPlanRow> expected = Collections.singletonList(
                new QueryPlanRow(
                        0,
                        0,
                        0,
                        "SCAN TABLE sura_names USING INDEX index_sura_number_on_table_sura_names"
                )
        );

        // When.
        List<QueryPlanRow> actual = queryPlanExplainer.explainQueryPlanForSelectStatement(
                "sura_names",
                new String[]{"name"},
                null,
                null,
                null,
                null,
                "sura ASC",
                null
        );

        // Then.
        assertEquals(expected, actual);
    }

    @Test
    public void test_explainQueryPlan_for_getSurahName() {
        // Given.
        List<QueryPlanRow> expected = Collections.singletonList(
                new QueryPlanRow(
                        0,
                        0,
                        0,
                        "SEARCH TABLE sura_names USING INDEX index_sura_number_on_table_sura_names (sura=?)"
                )
        );

        // When.
        List<QueryPlanRow> actual = queryPlanExplainer.explainQueryPlanForSelectStatement(
                "sura_names",
                new String[]{"name"},
                "sura = ?",
                new String[]{"1"},
                null,
                null,
                null,
                "1"
        );

        // Then.
        assertEquals(expected, actual);
    }

    @Test
    public void test_explainQueryPlan_for_getAyahsInSurah() {
        // Given.
        List<QueryPlanRow> expected = Collections.singletonList(
                new QueryPlanRow(
                        0,
                        0,
                        0,
                        "SEARCH TABLE quran_text USING INDEX index_sura_number_aya_number_on_table_quran_text (sura=?)"
                )
        );

        // When.
        List<QueryPlanRow> actual = queryPlanExplainer.explainQueryPlanForSelectStatement(
                "quran_text",
                null,
                "sura = ?",
                new String[]{"1"},
                null,
                null,
                "aya ASC",
                null
        );

        // Then.
        assertEquals(expected, actual);
    }

    @Test
    public void test_explainQueryPlan_for_getAyah() {
        // Given.
        List<QueryPlanRow> expected = Collections.singletonList(
                new QueryPlanRow(
                        0,
                        0,
                        0,
                        "SEARCH TABLE quran_text USING INDEX index_sura_number_aya_number_on_table_quran_text (sura=? AND aya=?)"
                )
        );

        // When.
        List<QueryPlanRow> actual = queryPlanExplainer.explainQueryPlanForSelectStatement(
                "quran_text",
                null,
                "sura = ? AND aya = ?",
                new String[]{"1", "1"},
                null,
                null,
                null,
                "1"
        );

        // Then.
        assertEquals(expected, actual);
    }

    @Test
    public void test_explainQueryPlan_for_getMetadataForChapterType() {
        // Given.
        List<QueryPlanRow> expected = Collections.singletonList(
                new QueryPlanRow(
                        0,
                        0,
                        0,
                        "SEARCH TABLE quran_metadata USING INDEX index_chapter_type_chapter_number_on_table_quran_metadata (chapter_type=?)"
                )
        );

        // When.
        List<QueryPlanRow> actual = queryPlanExplainer.explainQueryPlanForSelectStatement(
                "quran_metadata",
                null,
                "chapter_type = ?",
                new String[]{"sura"},
                null,
                null,
                "chapter_type ASC, chapter_number ASC",
                null
        );

        // Then.
        assertEquals(expected, actual);
    }

    @Test
    public void test_explainQueryPlan_for_getMetadataForChapter() {
        // Given.
        List<QueryPlanRow> expected = Collections.singletonList(
                new QueryPlanRow(
                        0,
                        0,
                        0,
                        "SEARCH TABLE quran_metadata USING INDEX index_chapter_type_chapter_number_on_table_quran_metadata (chapter_type=? AND chapter_number=?)"
                )
        );

        // When.
        List<QueryPlanRow> actual = queryPlanExplainer.explainQueryPlanForSelectStatement(
                "quran_metadata",
                null,
                "chapter_type = ? AND chapter_number = ?",
                new String[]{"sura", "1"},
                null,
                null,
                null,
                "1"
        );

        // Then.
        assertEquals(expected, actual);
    }
}
