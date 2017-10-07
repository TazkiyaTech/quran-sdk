package com.thinkincode.quran.sdk.database;

import android.content.res.Resources;
import android.support.annotation.NonNull;

import com.thinkincode.quran.sdk.R;

import java.util.Random;

/**
 * Helper class which provides Hifdh tips.
 */
public class HifdhTips {

    @NonNull
    private final Resources resources;

    @NonNull
    private final Random random;

    /**
     * Constructor.
     *
     * @param resources the application's {@link Resources} instance.
     */
    public HifdhTips(@NonNull Resources resources) {
        this.resources = resources;
        this.random = new Random();
    }

    /**
     * @return a randomly selected Hifdh tip.
     */
    @NonNull
    public String getNextRandom() {
        int index = random.nextInt(getSize());
        return getTip(index);
    }

    String getTip(int index) {
        return getTipsArray()[index];
    }

    int getSize() {
        return getTipsArray().length;
    }

    private String[] getTipsArray() {
        return resources.getStringArray(R.array.hifdh_tips);
    }
}
