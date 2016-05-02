package com.thinkincode.quran.sdk.database;

import android.database.sqlite.SQLiteDatabase;
import android.support.test.runner.AndroidJUnit4;

import com.thinkincode.quran.sdk.BaseTestCase;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;

@RunWith(AndroidJUnit4.class)
public class DatabaseUtilsTest extends BaseTestCase {

    private SQLiteDatabase sqLiteDatabase;

    @Before
    public void setUp() throws IOException {
        QuranDatabaseHelper quranDatabaseHelper = new QuranDatabaseHelper();
        quranDatabaseHelper.openDatabase(getTargetContext());

        sqLiteDatabase = quranDatabaseHelper.getSQLiteDatabase();
    }

    @Test
    public void test_explainQueryPlanForSelectStatement_when_tableNameIsSuraNames_and_noWhereClauseProvided() {
        // When.
        QueryPlan result = DatabaseUtils.explainQueryPlanForSelectStatement(
                sqLiteDatabase,
                QuranDatabaseHelper.TABLE_NAME_SURA_NAMES,
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
        QueryPlan result = DatabaseUtils.explainQueryPlanForSelectStatement(sqLiteDatabase,
                QuranDatabaseHelper.TABLE_NAME_SURA_NAMES,
                null,
                QuranDatabaseHelper.COLUMN_NAME_SURA + " = ? ",
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
        QueryPlan result = DatabaseUtils.explainQueryPlanForSelectStatement(
                sqLiteDatabase,
                QuranDatabaseHelper.TABLE_NAME_QURAN_TEXT,
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
        QueryPlan result = DatabaseUtils.explainQueryPlanForSelectStatement(sqLiteDatabase,
                QuranDatabaseHelper.TABLE_NAME_QURAN_TEXT,
                null,
                QuranDatabaseHelper.COLUMN_NAME_SURA + " = ? ",
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
        QueryPlan result = DatabaseUtils.explainQueryPlanForSelectStatement(sqLiteDatabase,
                QuranDatabaseHelper.TABLE_NAME_QURAN_TEXT,
                null,
                QuranDatabaseHelper.COLUMN_NAME_SURA + " = ? " +
                        " AND " +
                        QuranDatabaseHelper.COLUMN_NAME_AYA + " = ? ",
                new String[] { "1", "1" },
                null,
                null,
                null,
                null);

        // Then.
        assertThat(result.getDetail(), is(equalTo("SEARCH TABLE quran_text USING INDEX index_sura_number_aya_number_on_table_quran_text (sura=? AND aya=?)")));
    }
}
