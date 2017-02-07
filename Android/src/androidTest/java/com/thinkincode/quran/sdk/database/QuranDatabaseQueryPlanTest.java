package com.thinkincode.quran.sdk.database;

import android.database.sqlite.SQLiteDatabase;
import android.support.test.runner.AndroidJUnit4;

import com.thinkincode.quran.sdk.BaseTestCase;
import com.thinkincode.utils.database.QueryPlanExplainer;
import com.thinkincode.utils.database.QueryPlanRow;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;

@RunWith(AndroidJUnit4.class)
public class QuranDatabaseQueryPlanTest extends BaseTestCase {

    private QueryPlanExplainer queryPlanExplainer;

    @Before
    public void setUp() throws IOException {
        QuranDatabase quranDatabase = new QuranDatabase(getTargetContext());
        quranDatabase.openDatabase();

        SQLiteDatabase sqLiteDatabase = quranDatabase.getSQLiteDatabase();
        queryPlanExplainer = new QueryPlanExplainer(sqLiteDatabase);
    }

    @Test
    public void test_explainQueryPlan_for_getSurahNames() {
        // Given.
        List<QueryPlanRow> expected = Collections.singletonList(new QueryPlanRow(
                0,
                0,
                0,
                "SCAN TABLE sura_names USING INDEX index_sura_number_on_table_sura_names"
        ));

        // When.
        List<QueryPlanRow> actual = queryPlanExplainer.explainQueryPlanForSelectStatement(
                "sura_names",
                new String[]{"name"},
                null,
                null,
                null,
                null,
                "sura ASC",
                null);

        // Then.
        assertEquals(expected, actual);
    }

    @Test
    public void test_explainQueryPlan_for_getSurahName() {
        // Given.
        List<QueryPlanRow> expected = Collections.singletonList(new QueryPlanRow(
                0,
                0,
                0,
                "SEARCH TABLE sura_names USING INDEX index_sura_number_on_table_sura_names (sura=?)"
        ));

        // When.
        List<QueryPlanRow> actual = queryPlanExplainer.explainQueryPlanForSelectStatement(
                "sura_names",
                new String[]{"name"},
                "sura = ?",
                new String[]{"1"},
                null,
                null,
                null,
                "1");

        // Then.
        assertEquals(expected, actual);
    }

    @Test
    public void test_explainQueryPlan_for_getAyahsInSurah() {
        // Given.
        List<QueryPlanRow> expected = Collections.singletonList(new QueryPlanRow(
                0,
                0,
                0,
                "SEARCH TABLE quran_text USING INDEX index_sura_number_aya_number_on_table_quran_text (sura=?)"
        ));

        // When.
        List<QueryPlanRow> actual = queryPlanExplainer.explainQueryPlanForSelectStatement(
                "quran_text",
                null,
                "sura = ?",
                new String[]{"1"},
                null,
                null,
                "aya ASC",
                null);

        // Then.
        assertEquals(expected, actual);
    }

    @Test
    public void test_explainQueryPlan_for_getAyah() {
        // Given.
        List<QueryPlanRow> expected = Collections.singletonList(new QueryPlanRow(
                0,
                0,
                0,
                "SEARCH TABLE quran_text USING INDEX index_sura_number_aya_number_on_table_quran_text (sura=? AND aya=?)"
        ));

        // When.
        List<QueryPlanRow> actual = queryPlanExplainer.explainQueryPlanForSelectStatement(
                "quran_text",
                null,
                "sura = ? AND aya = ?",
                new String[]{"1", "1"},
                null,
                null,
                null,
                "1");

        // Then.
        assertEquals(expected, actual);
    }
}
