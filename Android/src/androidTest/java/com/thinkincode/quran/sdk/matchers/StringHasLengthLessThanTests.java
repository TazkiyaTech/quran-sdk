package com.thinkincode.quran.sdk.matchers;

import org.hamcrest.Matcher;
import org.junit.Test;
import org.junit.runner.RunWith;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@RunWith(AndroidJUnit4.class)
public class StringHasLengthLessThanTests {

    @Test
    public void test_matches_when_string_length_is_greater_than_match_value() {
        // Given.
        Matcher<String> matcher = new StringHasLengthLessThan(4);

        // When.
        boolean result = matcher.matches("Hello");

        // Then.
        assertFalse(result);
    }

    @Test
    public void test_matches_when_string_length_is_equal_to_match_value() {
        // Given.
        Matcher<String> matcher = new StringHasLengthLessThan(5);

        // When.
        boolean result = matcher.matches("Hello");

        // Then.
        assertFalse(result);
    }

    @Test
    public void test_matches_when_string_length_is_less_than_match_value() {
        // Given.
        Matcher<String> matcher = new StringHasLengthLessThan(6);

        // When.
        boolean result = matcher.matches("Hello");

        // Then.
        assertTrue(result);
    }
}