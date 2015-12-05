package com.thinkincode.quranutils;

import android.test.AndroidTestCase;

public class BaseTestCase extends AndroidTestCase {

    @Override
    protected void setUp() throws Exception {
        super.setUp();

//        QuranTesterApplication.getInstance().getQuranDatabaseHelper().createDatabaseIfDoesNotExist();
//        QuranTesterApplication.getInstance().getAnswersDatabaseHelper().clearAnswersTables();
//
//        QuranTesterApplication.getInstance().clearGeneratedVerseFromSharedPreferences();
//        QuranTesterApplication.getInstance().clearChapterSelectionFromSharedPreferences();
//        QuranTesterApplication.getInstance().clearSettingsFromSharedPreferences();
    }
}
