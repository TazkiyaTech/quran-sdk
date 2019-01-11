package com.thinkincode.quran.sdk.matchers;

import org.hamcrest.Matcher;
import org.junit.Test;
import org.junit.runner.RunWith;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@RunWith(AndroidJUnit4.class)
public class StringHasLengthGreaterThanTests {

    @Test
    public void test_matches_whenStringLengthIsGreaterThanMatchValue() {
        // Given.
        Matcher<String> matcher = new StringHasLengthGreaterThan(4);

        // When.
        boolean result = matcher.matches("Hello");

        // Then.
        assertTrue(result);
    }

    @Test
    public void test_matches_whenStringLengthIsEqualToMatchValue() {
        // Given.
        Matcher<String> matcher = new StringHasLengthGreaterThan(5);

        // When.
        boolean result = matcher.matches("Hello");

        // Then.
        assertFalse(result);
    }

    @Test
    public void test_matches_whenStringLengthIsLessThanMatchValue() {
        // Given.
        Matcher<String> matcher = new StringHasLengthGreaterThan(6);

        // When.
        boolean result = matcher.matches("Hello");

        // Then.
        assertFalse(result);
    }
}