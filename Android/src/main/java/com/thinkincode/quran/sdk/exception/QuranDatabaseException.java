package com.thinkincode.quran.sdk.exception;

public class QuranDatabaseException extends RuntimeException {

    public QuranDatabaseException(String error) {
        super(error);
    }

    public QuranDatabaseException(String message, Throwable cause) {
        super(message, cause);
    }
}
