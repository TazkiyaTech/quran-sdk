package com.tazkiyatech.quran.sdk;

import android.content.Context;

import androidx.test.core.app.ApplicationProvider;

public class BaseTestCase {

    /**
     * @return the {@link Context} for the target application being instrumented.
     */
    protected Context getTargetContext() {
        return ApplicationProvider.getApplicationContext();
    }
}
