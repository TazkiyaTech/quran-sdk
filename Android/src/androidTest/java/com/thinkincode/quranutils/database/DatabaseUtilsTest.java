package com.thinkincode.quranutils.database;

import android.database.sqlite.SQLiteDatabase;
import android.support.test.runner.AndroidJUnit4;

import com.thinkincode.quranutils.BaseTestCase;

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
        quranDatabaseHelper.createDatabaseIfDoesNotExist(getTargetContext());
        quranDatabaseHelper.openDatabaseForReadingIfClosed(getTargetContext());

        sqLiteDatabase = quranDatabaseHelper.getSQLiteDatabase();
    }

    @Test
    public void test_explainQueryPlanForSelectStatement() {
        // Given.
        String tableName = QuranDatabaseHelper.TABLE_NAME_SURA_NAMES;

        // When.
        QueryPlan result = DatabaseUtils.explainQueryPlanForSelectStatement(sqLiteDatabase, tableName, null, null, null, null, null, null, null);

        // Then.
        assertThat(result.getDetail(), is(equalTo("SCAN TABLE " + tableName)));
    }
}
