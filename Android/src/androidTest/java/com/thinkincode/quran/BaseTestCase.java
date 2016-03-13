package com.thinkincode.quran;

import android.content.Context;
import android.support.test.InstrumentationRegistry;

public class BaseTestCase {

    /**
     * @return the {@link Context} for the target application being instrumented.
     */
    protected Context getTargetContext() {
        return InstrumentationRegistry.getTargetContext();
    }
}
