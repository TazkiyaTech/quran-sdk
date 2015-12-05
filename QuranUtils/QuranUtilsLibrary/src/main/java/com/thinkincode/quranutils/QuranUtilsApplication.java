package com.thinkincode.quranutils;

import android.app.Application;

import com.thinkincode.quranutils.database.QuranDatabaseHelper;

public class QuranUtilsApplication extends Application {

    private static QuranUtilsApplication instance;

    /**
     * Instance of {@link QuranDatabaseHelper} used to get a readable {@link android.database.sqlite.SQLiteDatabase} object.
     */
    private QuranDatabaseHelper quranDatabaseHelper;

    /**
     * @return {@link #instance}
     */
    public static QuranUtilsApplication getInstance() {
        return instance;
    }

    /**
     * @return {@link #quranDatabaseHelper}
     */
    public QuranDatabaseHelper getQuranDatabaseHelper() {
        return quranDatabaseHelper;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        instance = this;

        quranDatabaseHelper = new QuranDatabaseHelper();
    }

    @Override
    public void onTerminate() {
        super.onTerminate();

        // this method is not actually called
        // so clear-up should be done in 'Activity.onDestroy()' methods

        quranDatabaseHelper.closeDatabase();

        instance = null;
    }
}
