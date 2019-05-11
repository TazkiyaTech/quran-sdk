package com.tazkiyatech.quran.sdk.exception

class QuranDatabaseException : RuntimeException {

    constructor(message: String) : super(message)

    constructor(message: String, cause: Throwable) : super(message, cause)
}
