package com.thinkincode.quranutils;

import android.content.Context;
import android.support.test.InstrumentationRegistry;

public class BaseTestCase {

    /**
     * @return the {@link Context} for the target application being intrumented.
     */
    protected Context getTargetContext() {
        return InstrumentationRegistry.getTargetContext();
    }
}
