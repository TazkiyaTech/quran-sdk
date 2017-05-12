package com.thinkincode.quran.sdk.database;

import android.content.res.Resources;
import android.support.annotation.NonNull;

import com.thinkincode.quran.sdk.R;

import java.util.Random;

/**
 * Helper class which provides inspirational quotes and reminders relating to the virtues of the Qur'an.
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
    public String getNextRandom() {
        String[] inspirationMessages = resources.getStringArray(R.array.array_inspiration);

        int inspirationMessageIndex = random.nextInt(inspirationMessages.length);

        return inspirationMessages[inspirationMessageIndex];
    }
}
