package com.tazkiyatech.quran.sdk.database

import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException

import com.tazkiyatech.quran.sdk.exception.QuranDatabaseException
import com.tazkiyatech.quran.sdk.model.ChapterMetadata
import com.tazkiyatech.quran.sdk.model.ChapterType
import com.tazkiyatech.utils.streams.StreamCopier

import java.io.File
import java.io.IOException
import java.util.ArrayList

/**
 * Helper class which handles the creation and opening of the SQLite-based Quran database
 * and provides easy methods for accessing its content.
 *
 * @property applicationContext The application context (and not the activity or service context).
 */
class QuranDatabase(private val applicationContext: Context) {

    /**
     * (Internal visibility for testing purposes only.)
     */
    internal var sqLiteDatabase: SQLiteDatabase? = null
        private set
        @JvmName("getSQLiteDatabase") get

    /**
     * Opens the Quran database for reading, if it's not already open.
     *
     * @throws QuranDatabaseException if the database could not be opened.
     */
    @Throws(QuranDatabaseException::class)
    fun openDatabase() {
        if (isDatabaseOpen()) {
            return
        }

        if (!isFileExistsInInternalStorage(DATABASE_NAME)) {
            copyFileFromAssetsToInternalStorage(DATABASE_NAME)
        }

        val path = getPathToFileInInternalStorage(DATABASE_NAME)

        try {
            sqLiteDatabase = SQLiteDatabase.openDatabase(path, null, SQLiteDatabase.OPEN_READONLY)
        } catch (e: SQLiteException) {
            throw QuranDatabaseException("Failed opening the Quran database", e)
        }

        deleteFileInInternalStorage(LEGACY_DATABASE_NAME)
    }

    /**
     * Closes the Quran database.
     */
    fun closeDatabase() {
        if (isDatabaseOpen()) {
            sqLiteDatabase?.close()
        }
    }

    /**
     * Gets the names of all of the Surahs in the Quran.
     *
     * @return the names of all of the Surahs in the Quran.
     * @throws QuranDatabaseException if there was an error getting the Surah names from the database.
     */
    @Throws(QuranDatabaseException::class)
    fun getSurahNames(): List<String> {
        val surahNames = ArrayList<String>()

        val columns = arrayOf(COLUMN_NAME_NAME)
        val orderBy = "$COLUMN_NAME_SURA ASC "

        val cursor: Cursor

        try {
            cursor = queryDatabase(TABLE_NAME_SURA_NAMES, columns, null, null, orderBy, null)
        } catch (ex: QuranDatabaseException) {
            val message = "Failed getting Surah names"
            throw QuranDatabaseException(message, ex)
        }

        val columnIndexName = cursor.getColumnIndex(COLUMN_NAME_NAME)

        while (cursor.moveToNext()) {
            surahNames.add(cursor.getString(columnIndexName))
        }

        cursor.close()

        if (surahNames.isEmpty()) {
            val message = "Failed getting Surah names"
            throw QuranDatabaseException(message)
        }

        return surahNames
    }

    /**
     * Gets the name of the specified Surah.
     *
     * @param surahNumber is a value between 1 and 114 (inclusive).
     * @return the name of the specified Surah.
     * @throws QuranDatabaseException if there was an error getting the Surah name from the database.
     */
    @Throws(QuranDatabaseException::class)
    fun getSurahName(surahNumber: Int): String {
        var surahName: String? = null

        val columns = arrayOf(COLUMN_NAME_NAME)
        val selection = "$COLUMN_NAME_SURA = ? "
        val selectionArgs = arrayOf(surahNumber.toString())
        val limit = "1"

        val cursor: Cursor

        try {
            cursor =
                queryDatabase(TABLE_NAME_SURA_NAMES, columns, selection, selectionArgs, null, limit)
        } catch (ex: QuranDatabaseException) {
            val message = String.format("Failed getting Surah name for Surah %s", surahNumber)
            throw QuranDatabaseException(message, ex)
        }

        val columnIndexName = cursor.getColumnIndex(COLUMN_NAME_NAME)

        if (cursor.moveToFirst()) {
            surahName = cursor.getString(columnIndexName)
        }

        cursor.close()

        if (surahName == null) {
            val message = String.format("Failed getting Surah name for Surah %s", surahNumber)
            throw QuranDatabaseException(message)
        }

        return surahName
    }

    /**
     * Gets all of the Ayahs in the specified Surah.
     *
     * @param surahNumber is a value between 1 and 114 (inclusive).
     * @return the Ayahs of the specified Surah.
     * @throws QuranDatabaseException if there was an error getting the Ayahs from the database.
     */
    @Throws(QuranDatabaseException::class)
    fun getAyahsInSurah(surahNumber: Int): List<String> {
        val surahAyahs = ArrayList<String>()

        val columns = arrayOf(COLUMN_NAME_TEXT)
        val selection = "$COLUMN_NAME_SURA = ? "
        val selectionArgs = arrayOf(surahNumber.toString())
        val orderBy = "$COLUMN_NAME_AYA ASC "

        val cursor: Cursor

        try {
            cursor = queryDatabase(
                TABLE_NAME_QURAN_TEXT,
                columns,
                selection,
                selectionArgs,
                orderBy,
                null
            )
        } catch (ex: QuranDatabaseException) {
            val message = String.format("Failed getting Ayahs for Surah %s", surahNumber)
            throw QuranDatabaseException(message, ex)
        }

        val columnIndexText = cursor.getColumnIndex(COLUMN_NAME_TEXT)

        while (cursor.moveToNext()) {
            surahAyahs.add(cursor.getString(columnIndexText))
        }

        cursor.close()

        if (surahAyahs.isEmpty()) {
            val message = String.format("Failed getting Ayahs for Surah %s", surahNumber)
            throw QuranDatabaseException(message)
        }

        return surahAyahs
    }

    /**
     * Gets the text of the specified Ayah.
     *
     * @param surahNumber is a value between 1 and 114 (inclusive).
     * @param ayahNumber  is a value greater than or equal to 1.
     * @return the text of the specified Ayah.
     * @throws QuranDatabaseException if there was an error getting the Ayah from the database.
     */
    @Throws(QuranDatabaseException::class)
    fun getAyah(surahNumber: Int, ayahNumber: Int): String {
        var ayah: String? = null

        val columns = arrayOf(COLUMN_NAME_TEXT)
        val selection = "$COLUMN_NAME_SURA = ? AND $COLUMN_NAME_AYA = ? "
        val selectionArgs = arrayOf(surahNumber.toString(), ayahNumber.toString())
        val limit = "1"

        val cursor: Cursor

        try {
            cursor =
                queryDatabase(TABLE_NAME_QURAN_TEXT, columns, selection, selectionArgs, null, limit)
        } catch (ex: QuranDatabaseException) {
            val message =
                String.format("Failed getting Ayah for Surah %s, Ayah %s", surahNumber, ayahNumber)
            throw QuranDatabaseException(message, ex)
        }

        val columnIndexText = cursor.getColumnIndex(COLUMN_NAME_TEXT)

        if (cursor.moveToFirst()) {
            ayah = cursor.getString(columnIndexText)
        }

        cursor.close()

        if (ayah == null) {
            val message =
                String.format("Failed getting Ayah for Surah %s, Ayah %s", surahNumber, ayahNumber)
            throw QuranDatabaseException(message)
        }

        return ayah
    }

    /**
     * Gets the metadata for the chapters of the specified chapter type.
     *
     * @param chapterType the chapter type for which to get metadata.
     * @return the metadata for the chapters of the specified chapter type.
     * @throws QuranDatabaseException if there was an error getting the metadata from the database.
     */
    @Throws(QuranDatabaseException::class)
    fun getMetadataForChapterType(chapterType: ChapterType): List<ChapterMetadata> {
        val chapterMetadataList = ArrayList<ChapterMetadata>()

        val selection = "$COLUMN_NAME_CHAPTER_TYPE = ? "
        val selectionArgs = arrayOf(chapterType.nameInDatabase)
        val orderBy = "$COLUMN_NAME_CHAPTER_TYPE ASC, $COLUMN_NAME_CHAPTER_NUMBER ASC"

        val cursor: Cursor

        try {
            cursor = queryDatabase(
                TABLE_NAME_QURAN_METADATA,
                null,
                selection,
                selectionArgs,
                orderBy,
                null
            )
        } catch (ex: QuranDatabaseException) {
            val message =
                String.format("Failed getting metadata for chapter type = %s", chapterType.name)
            throw QuranDatabaseException(message, ex)
        }

        val chapterTypeColumnIndex = cursor.getColumnIndex(COLUMN_NAME_CHAPTER_TYPE)
        val chapterNumberColumnIndex = cursor.getColumnIndex(COLUMN_NAME_CHAPTER_NUMBER)
        val ayaCountColumnIndex = cursor.getColumnIndex(COLUMN_NAME_AYA_COUNT)
        val suraColumnIndex = cursor.getColumnIndex(COLUMN_NAME_SURA)
        val ayaColumnIndex = cursor.getColumnIndex(COLUMN_NAME_AYA)

        while (cursor.moveToNext()) {
            chapterMetadataList.add(
                ChapterMetadata(
                    cursor.getString(chapterTypeColumnIndex),
                    cursor.getInt(chapterNumberColumnIndex),
                    cursor.getInt(ayaCountColumnIndex),
                    cursor.getInt(suraColumnIndex),
                    cursor.getInt(ayaColumnIndex)
                )
            )
        }

        cursor.close()

        if (chapterMetadataList.isEmpty()) {
            val message =
                String.format("Failed getting metadata for chapter type = %s", chapterType.name)
            throw QuranDatabaseException(message)
        }

        return chapterMetadataList
    }

    /**
     * Gets the metadata for the specified chapter.
     *
     * @param chapterType   the chapter type for which to get metadata.
     * @param chapterNumber the number of the chapter within the given chapter type.
     * @return the metadata for the specified chapter.
     * @throws QuranDatabaseException if there was an error getting the metadata from the database.
     */
    @Throws(QuranDatabaseException::class)
    fun getMetadataForChapter(chapterType: ChapterType, chapterNumber: Int): ChapterMetadata {
        var chapterMetadata: ChapterMetadata? = null

        val columns = arrayOf(COLUMN_NAME_AYA_COUNT, COLUMN_NAME_SURA, COLUMN_NAME_AYA)
        val selection = "$COLUMN_NAME_CHAPTER_TYPE = ? AND $COLUMN_NAME_CHAPTER_NUMBER = ? "
        val selectionArgs = arrayOf(chapterType.nameInDatabase, chapterNumber.toString())
        val limit = "1"

        val cursor: Cursor

        try {
            cursor = queryDatabase(
                TABLE_NAME_QURAN_METADATA,
                columns,
                selection,
                selectionArgs,
                null,
                limit
            )
        } catch (ex: QuranDatabaseException) {
            val message = String.format(
                "Failed getting chapter metadata for chapter type = %s, chapter number = %s",
                chapterType.name,
                chapterNumber
            )
            throw QuranDatabaseException(message, ex)
        }

        val ayaCountColumnIndex = cursor.getColumnIndex(COLUMN_NAME_AYA_COUNT)
        val suraColumnIndex = cursor.getColumnIndex(COLUMN_NAME_SURA)
        val ayaColumnIndex = cursor.getColumnIndex(COLUMN_NAME_AYA)

        if (cursor.moveToFirst()) {
            val ayaCount = cursor.getInt(ayaCountColumnIndex)
            val sura = cursor.getInt(suraColumnIndex)
            val aya = cursor.getInt(ayaColumnIndex)

            chapterMetadata = ChapterMetadata(
                chapterType.nameInDatabase,
                chapterNumber,
                ayaCount,
                sura,
                aya
            )
        }

        cursor.close()

        if (chapterMetadata == null) {
            val message = String.format(
                "Failed getting chapter metadata for chapter type = %s, chapter number = %s",
                chapterType.name,
                chapterNumber
            )
            throw QuranDatabaseException(message)
        }

        return chapterMetadata
    }

    /**
     * (Internal visibility for unit testing purposes only.)
     *
     * @return true iff the Quran database is open for reading.
     */
    @JvmName("isDatabaseOpen")
    internal fun isDatabaseOpen() = sqLiteDatabase?.isOpen == true

    /**
     * (Internal visibility for unit testing purposes only.)
     *
     * @return true iff the file with the given name exists in internal storage.
     */
    @JvmName("isFileExistsInInternalStorage")
    internal fun isFileExistsInInternalStorage(filename: String): Boolean {
        val path = getPathToFileInInternalStorage(filename)
        val file = File(path)

        return file.isFile
    }

    /**
     * Deletes the file with the given name from internal storage.
     *
     * @return true iff the file with the give name is deleted from internal storage.
     */
    private fun deleteFileInInternalStorage(filename: String): Boolean {
        val path = getPathToFileInInternalStorage(filename)
        val file = File(path)

        return !file.exists() || file.delete()
    }

    private fun getPathToFileInInternalStorage(filename: String): String =
        File(applicationContext.filesDir, filename).path

    /**
     * Copies the Quran database from assets to internal storage,
     * so that it can be accessed and handled.
     *
     * @throws QuranDatabaseException if the database could not be copied.
     */
    private fun copyFileFromAssetsToInternalStorage(filename: String) {
        try {
            applicationContext.assets.open(filename).use { inputStream ->
                applicationContext.openFileOutput(filename, Context.MODE_PRIVATE)
                    .use { outputStream ->
                        val streamCopier = StreamCopier()
                        streamCopier.copy(inputStream, outputStream)
                    }
            }
        } catch (e: IOException) {
            throw QuranDatabaseException(
                "Failed copying Quran database from assets to internal storage",
                e
            )
        }
    }

    /**
     * Queries the Quran database with the specified parameters.
     *
     * @return the result of the query.
     * @throws QuranDatabaseException if the database is not open for reading.
     */
    @Throws(QuranDatabaseException::class)
    private fun queryDatabase(table: String,
                              columns: Array<String>?,
                              selection: String?,
                              selectionArgs: Array<String>?,
                              orderBy: String?,
                              limit: String?): Cursor {
        if (!isDatabaseOpen()) {
            openDatabase()
        }
        return sqLiteDatabase!!.query(
            table,
            columns,
            selection,
            selectionArgs,
            null,
            null,
            orderBy,
            limit
        )
    }

    companion object {
        private const val DATABASE_NAME = "com.tazkiyatech.quran.db"
        private const val LEGACY_DATABASE_NAME = "com.thinkincode.quran.db"

        private const val TABLE_NAME_QURAN_METADATA = "quran_metadata"
        private const val TABLE_NAME_QURAN_TEXT = "quran_text"
        private const val TABLE_NAME_SURA_NAMES = "sura_names"

        private const val COLUMN_NAME_AYA = "aya"
        private const val COLUMN_NAME_AYA_COUNT = "aya_count"
        private const val COLUMN_NAME_CHAPTER_TYPE = "chapter_type"
        private const val COLUMN_NAME_CHAPTER_NUMBER = "chapter_number"
        private const val COLUMN_NAME_NAME = "name"
        private const val COLUMN_NAME_SURA = "sura"
        private const val COLUMN_NAME_TEXT = "text"
    }
}