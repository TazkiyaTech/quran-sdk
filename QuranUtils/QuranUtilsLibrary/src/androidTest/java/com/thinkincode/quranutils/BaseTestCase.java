package com.thinkincode.quranutils;

import android.test.AndroidTestCase;

import com.thinkincode.quranutils.database.QuranDatabaseHelper;

public class BaseTestCase extends AndroidTestCase {

    /**
     * Instance of {@link QuranDatabaseHelper} used to get a readable {@link android.database.sqlite.SQLiteDatabase} object.
     */
    private QuranDatabaseHelper quranDatabaseHelper;

    /**
     * @return {@link #quranDatabaseHelper}
     */
    protected QuranDatabaseHelper getQuranDatabaseHelper() {
        return quranDatabaseHelper;
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        quranDatabaseHelper = new QuranDatabaseHelper();
        quranDatabaseHelper.createDatabaseIfDoesNotExist(getContext());
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
        quranDatabaseHelper.closeDatabase();
    }
}
