package com.tazkiyatech.quran.sdk.database

import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException
import com.tazkiyatech.quran.sdk.exception.QuranDatabaseException
import com.tazkiyatech.quran.sdk.model.SectionMetadata
import com.tazkiyatech.quran.sdk.model.SectionType
import java.io.File
import java.io.IOException
import java.util.*

/**
 * A wrapper around the SQLite Quran database.
 * Provides easy methods for accessing the contents of the database.
 *
 * It is safe to call the functions in this class on a single instance or on multiple instances
 * in separate threads concurrently.
 *
 * @property applicationContext The application context (and not the activity or service context).
 */
class QuranDatabase(private val applicationContext: Context) {

    /**
     * (Internal visibility for testing purposes.)
     */
    internal var sqLiteDatabase: SQLiteDatabase? = null
        private set

    /**
     * Opens the Quran database for reading if it's not already open.
     * Does nothing if the database is already open for reading.
     *
     * Calling this function is optional.
     * The "get..." functions on this class take care of opening the database if it not already open.
     * Call this function if you wish to open the database ahead of time
     * before the first "get..." function call in your application.
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

        val file = getFileInInternalStorage(DATABASE_NAME)

        try {
            sqLiteDatabase =
                SQLiteDatabase.openDatabase(file.path, null, SQLiteDatabase.OPEN_READONLY)
        } catch (e: SQLiteException) {
            throw QuranDatabaseException("Failed opening the Quran database", e)
        }

        deleteFileInInternalStorage(VERSION_0_DATABASE_NAME)
        deleteFileInInternalStorage(VERSION_1_DATABASE_NAME)
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
    fun getNameOfSurah(surahNumber: Int): String {
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
     * Gets the metadata for the sections of the specified section type.
     *
     * @param sectionType the section type for which to get metadata.
     * @return the metadata for the sections of the specified section type.
     * @throws QuranDatabaseException if there was an error getting the metadata from the database.
     */
    @Throws(QuranDatabaseException::class)
    fun getMetadataForSectionsOfType(sectionType: SectionType): List<SectionMetadata> {
        val sectionMetadataList = ArrayList<SectionMetadata>()

        val selection = "$COLUMN_NAME_SECTION_TYPE = ? "
        val selectionArgs = arrayOf(sectionType.nameInDatabase)
        val orderBy = "$COLUMN_NAME_SECTION_TYPE ASC, $COLUMN_NAME_SECTION_NUMBER ASC"

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
                String.format("Failed getting metadata for section type = %s", sectionType.name)
            throw QuranDatabaseException(message, ex)
        }

        val sectionTypeColumnIndex = cursor.getColumnIndex(COLUMN_NAME_SECTION_TYPE)
        val sectionNumberColumnIndex = cursor.getColumnIndex(COLUMN_NAME_SECTION_NUMBER)
        val ayaCountColumnIndex = cursor.getColumnIndex(COLUMN_NAME_AYA_COUNT)
        val suraColumnIndex = cursor.getColumnIndex(COLUMN_NAME_SURA)
        val ayaColumnIndex = cursor.getColumnIndex(COLUMN_NAME_AYA)

        while (cursor.moveToNext()) {
            sectionMetadataList.add(
                SectionMetadata(
                    cursor.getString(sectionTypeColumnIndex),
                    cursor.getInt(sectionNumberColumnIndex),
                    cursor.getInt(ayaCountColumnIndex),
                    cursor.getInt(suraColumnIndex),
                    cursor.getInt(ayaColumnIndex)
                )
            )
        }

        cursor.close()

        if (sectionMetadataList.isEmpty()) {
            val message =
                String.format("Failed getting metadata for section type = %s", sectionType.name)
            throw QuranDatabaseException(message)
        }

        return sectionMetadataList
    }

    /**
     * Gets the metadata for the specified section.
     *
     * @param sectionType   the section type for which to get metadata.
     * @param sectionNumber the number of the section within the given section type.
     * @return the metadata for the specified section.
     * @throws QuranDatabaseException if there was an error getting the metadata from the database.
     */
    @Suppress("SameParameterValue")
    @Throws(QuranDatabaseException::class)
    fun getMetadataForSection(sectionType: SectionType, sectionNumber: Int): SectionMetadata {
        var sectionMetadata: SectionMetadata? = null

        val columns = arrayOf(COLUMN_NAME_AYA_COUNT, COLUMN_NAME_SURA, COLUMN_NAME_AYA)
        val selection = "$COLUMN_NAME_SECTION_TYPE = ? AND $COLUMN_NAME_SECTION_NUMBER = ? "
        val selectionArgs = arrayOf(sectionType.nameInDatabase, sectionNumber.toString())
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
                "Failed getting section metadata for section type = %s, section number = %s",
                sectionType.name,
                sectionNumber
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

            sectionMetadata = SectionMetadata(
                sectionType.nameInDatabase,
                sectionNumber,
                ayaCount,
                sura,
                aya
            )
        }

        cursor.close()

        if (sectionMetadata == null) {
            val message = String.format(
                "Failed getting section metadata for section type = %s, section number = %s",
                sectionType.name,
                sectionNumber
            )
            throw QuranDatabaseException(message)
        }

        return sectionMetadata
    }

    /**
     * Determines whether the database is open for reading.
     *
     * (Internal visibility for unit testing purposes.)
     *
     * @return true iff the Quran database is open for reading.
     */
    internal fun isDatabaseOpen() = sqLiteDatabase?.isOpen == true

    /**
     * Determines whether a file with the given name exists in the application's internal storage area.
     *
     * (Internal visibility for unit testing purposes.)
     *
     * @return true iff the file with the given name exists in the application's internal storage area.
     */
    internal fun isFileExistsInInternalStorage(filename: String): Boolean {
        return getFileInInternalStorage(filename).isFile
    }

    /**
     * Deletes the file with the given name from the application's internal storage area.
     *
     * @return true iff the file with the give name is deleted from the application's internal storage area.
     */
    private fun deleteFileInInternalStorage(filename: String): Boolean {
        val file = getFileInInternalStorage(filename)

        return !file.exists() || file.delete()
    }

    /**
     * A [File] representation of the Quran database file as it exists in the application's internal storage area.
     */
    private fun getFileInInternalStorage(filename: String): File =
        File(applicationContext.noBackupFilesDir, filename)

    /**
     * Copies the Quran database file from the application's assets to the application's internal storage area
     * so that it can be accessed as a database.
     *
     * @throws QuranDatabaseException if the database file could not be copied.
     */
    @Suppress("SameParameterValue")
    private fun copyFileFromAssetsToInternalStorage(filename: String) {
        try {
            applicationContext.assets.open(filename).use { inputStream ->
                // for thread safety: copy the contents of the assets file to a file with a unique name
                // and then move the file to the desired destination

                val uuid = UUID.randomUUID().toString()
                val temporaryFile = File(applicationContext.cacheDir, "$filename-$uuid")

                temporaryFile.outputStream().use { outputStream ->
                    inputStream.copyTo(outputStream)
                }

                temporaryFile.renameTo(getFileInInternalStorage(filename))
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
     * @throws QuranDatabaseException if the database could not be opened.
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

    internal companion object {
        /**
         * (Internal visibility for unit testing purposes.)
         */
        internal const val DATABASE_NAME = "com.tazkiyatech.quran.v2.db"

        private const val VERSION_0_DATABASE_NAME = "com.thinkincode.quran.db"
        private const val VERSION_1_DATABASE_NAME = "com.tazkiyatech.quran.db"

        private const val TABLE_NAME_QURAN_METADATA = "quran_metadata"
        private const val TABLE_NAME_QURAN_TEXT = "quran_text"
        private const val TABLE_NAME_SURA_NAMES = "sura_names"

        private const val COLUMN_NAME_AYA = "aya"
        private const val COLUMN_NAME_AYA_COUNT = "aya_count"
        private const val COLUMN_NAME_SECTION_TYPE = "section_type"
        private const val COLUMN_NAME_SECTION_NUMBER = "section_number"
        private const val COLUMN_NAME_NAME = "name"
        private const val COLUMN_NAME_SURA = "sura"
        private const val COLUMN_NAME_TEXT = "text"
    }
}