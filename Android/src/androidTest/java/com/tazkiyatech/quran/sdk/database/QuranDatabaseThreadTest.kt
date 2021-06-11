package com.tazkiyatech.quran.sdk.database

import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class QuranDatabaseThreadTest {

    private var success: Boolean = true
    private var failureMessage: String? = null

    @Test
    fun openDatabase_in_multiple_simultaneous_threads_on_the_same_QuranDatabase_instance() {
        val threads = mutableListOf<Thread>()

        val quranDatabase = QuranDatabase(ApplicationProvider.getApplicationContext())

        for (i in 0..100) {
            threads.add(createThread(quranDatabase))
        }

        startAndJoin(threads)

        quranDatabase.closeDatabase()

        assertTrue("Not all threads succeeded: $failureMessage", success)
    }

    @Test
    fun openDatabase_in_multiple_simultaneous_threads_on_different_QuranDatabase_instances() {
        val threads = mutableListOf<Thread>()

        for (i in 0..100) {
            threads.add(createThread())
        }

        startAndJoin(threads)

        assertTrue("Not all threads succeeded: $failureMessage", success)
    }

    private fun createThread(quranDatabase: QuranDatabase): Thread {
        return Thread {
            try {
                // the database query below will implicitly make a call to open the database
                assertEquals(7, quranDatabase.getAyahsInSurah(1).size)
            } catch (e: Exception) {
                success = false
                failureMessage = e.message
            }
        }
    }

    private fun createThread(): Thread {
        return Thread {
            val quranDatabase = QuranDatabase(ApplicationProvider.getApplicationContext())

            try {
                // the database query below will implicitly make a call to open the database
                assertEquals(7, quranDatabase.getAyahsInSurah(1).size)
            } catch (e: Exception) {
                success = false
                failureMessage = e.message
            } finally {
                quranDatabase.closeDatabase()
            }
        }
    }

    private fun startAndJoin(threads: List<Thread>) {
        for (thread in threads) {
            thread.start()
        }

        for (thread in threads) {
            thread.join()
        }
    }
}