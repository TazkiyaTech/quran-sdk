package com.thinkincode.quranutils;

import android.test.AndroidTestCase;

public class BaseTestCase extends AndroidTestCase {

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        QuranUtilsApplication.getInstance().getQuranDatabaseHelper().createDatabaseIfDoesNotExist(QuranUtilsApplication.getInstance());
    }
}
