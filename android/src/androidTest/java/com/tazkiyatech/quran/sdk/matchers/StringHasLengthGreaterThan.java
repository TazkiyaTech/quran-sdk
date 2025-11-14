package com.tazkiyatech.quran.sdk.matchers;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

/**
 * Matches if a {@link String} has length greater than the specified amount.
 */
public class StringHasLengthGreaterThan extends TypeSafeMatcher<String> {

    private final int length;

    /**
     * Creates a {@link Matcher} that matches when the length of a {@link String}
     * is greater than the provided length.
     */
    public static Matcher<String> hasLengthGreaterThan(int length) {
        return new StringHasLengthGreaterThan(length);
    }

    /**
     * Constructor.
     */
    StringHasLengthGreaterThan(int length) {
        this.length = length;
    }

    @Override
    protected boolean matchesSafely(String item) {
        return item.length() > length;
    }

    @Override
    public void describeTo(Description description) {
        description.appendText("length greater than: ").appendValue(length);
    }

    @Override
    public void describeMismatchSafely(String item,
                                       Description description) {
        description.appendText("found String ")
                .appendValue(item)
                .appendText(" with length ")
                .appendValue(item.length());
    }
}
