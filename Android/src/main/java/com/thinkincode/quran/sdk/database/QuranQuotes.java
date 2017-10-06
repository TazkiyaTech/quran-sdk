package com.thinkincode.quran.sdk.database;

import android.content.res.Resources;
import android.support.annotation.NonNull;

import com.thinkincode.quran.sdk.R;

import java.util.Random;

/**
 * Helper class which provides inspirational quotes and reminders relating to the virtues of the Quran.
 */
public class QuranQuotes {

    @NonNull
    private final Resources resources;

    @NonNull
    private final Random random;

    /**
     * Constructor.
     *
     * @param resources the application's {@link Resources} instance.
     */
    public QuranQuotes(@NonNull Resources resources) {
        this.resources = resources;
        this.random = new Random();
    }

    /**
     * @return a randomly selected quote.
     */
    @NonNull
    public String getNextRandom() {
        int index = random.nextInt(getSize());
        return getQuote(index);
    }

    String getQuote(int index) {
        return getQuotesArray()[index];
    }

    int getSize() {
        return getQuotesArray().length;
    }

    private String[] getQuotesArray() {
        return resources.getStringArray(R.array.quran_quotes);
    }
}
