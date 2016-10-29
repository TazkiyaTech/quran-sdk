package com.thinkincode.quran.sdk.database;

import android.database.sqlite.SQLiteDatabase;
import android.support.test.runner.AndroidJUnit4;

import com.thinkincode.quran.sdk.BaseTestCase;
import com.thinkincode.utils.database.QueryPlan;
import com.thinkincode.utils.database.QueryPlanExplainer;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;

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
    public void test_explainQueryPlanForSelectStatement_when_tableNameIsSuraNames_and_noWhereClauseProvided() {
        // When.
        QueryPlan result = queryPlanExplainer.explainQueryPlanForSelectStatement(
                QuranDatabase.TABLE_NAME_SURA_NAMES,
                null,
                null,
                null,
                null,
                null,
                null,
                null);

        // Then.
        assertThat(result.getDetail(), is(equalTo("SCAN TABLE sura_names")));
    }

    @Test
    public void test_explainQueryPlanForSelectStatement_when_tableNameIsSuraNames_and_whereClauseProvided() {
        // When.
        QueryPlan result = queryPlanExplainer.explainQueryPlanForSelectStatement(
                QuranDatabase.TABLE_NAME_SURA_NAMES,
                null,
                QuranDatabase.COLUMN_NAME_SURA + " = ? ",
                new String[] { "1" },
                null,
                null,
                null,
                null);

        // Then.
        assertThat(result.getDetail(), is(equalTo("SEARCH TABLE sura_names USING INDEX index_sura_number_on_table_sura_names (sura=?)")));
    }

    @Test
    public void test_explainQueryPlanForSelectStatement_when_tableNameIsQuranText_and_noWhereClauseProvided() {
        // When.
        QueryPlan result = queryPlanExplainer.explainQueryPlanForSelectStatement(
                QuranDatabase.TABLE_NAME_QURAN_TEXT,
                null,
                null,
                null,
                null,
                null,
                null,
                null);

        // Then.
        assertThat(result.getDetail(), is(equalTo("SCAN TABLE quran_text")));
    }

    @Test
    public void test_explainQueryPlanForSelectStatement_when_tableNameIsQuranText_and_whereClauseProvided_1() {
        // When.
        QueryPlan result = queryPlanExplainer.explainQueryPlanForSelectStatement(
                QuranDatabase.TABLE_NAME_QURAN_TEXT,
                null,
                QuranDatabase.COLUMN_NAME_SURA + " = ? ",
                new String[] { "1" },
                null,
                null,
                null,
                null);

        // Then.
        assertThat(result.getDetail(), is(equalTo("SEARCH TABLE quran_text USING INDEX index_sura_number_on_table_quran_text (sura=?)")));
    }

    @Test
    public void test_explainQueryPlanForSelectStatement_when_tableNameIsQuranText_and_whereClauseProvided_2() {
        // When.
        QueryPlan result = queryPlanExplainer.explainQueryPlanForSelectStatement(
                QuranDatabase.TABLE_NAME_QURAN_TEXT,
                null,
                QuranDatabase.COLUMN_NAME_SURA + " = ? " +
                        " AND " +
                        QuranDatabase.COLUMN_NAME_AYA + " = ? ",
                new String[] { "1", "1" },
                null,
                null,
                null,
                null);

        // Then.
        assertThat(result.getDetail(), is(equalTo("SEARCH TABLE quran_text USING INDEX index_sura_number_aya_number_on_table_quran_text (sura=? AND aya=?)")));
    }
}
