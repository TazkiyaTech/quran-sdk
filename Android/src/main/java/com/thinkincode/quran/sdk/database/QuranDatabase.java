package com.thinkincode.quran.sdk.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.support.annotation.NonNull;

import com.thinkincode.quran.sdk.exception.QuranDatabaseException;
import com.thinkincode.utils.streams.StreamCopier;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Helper class which handles the creation and opening of the SQLite-based Quran database
 * and provides easy methods for accessing its content.
 */
public class QuranDatabase {

    private static final String DATABASE_NAME = "com.thinkincode.quran.db";

    private static final String TABLE_NAME_QURAN_TEXT = "quran_text";
    private static final String TABLE_NAME_SURA_NAMES = "sura_names";

    private static final String COLUMN_NAME_AYA = "aya";
    private static final String COLUMN_NAME_NAME = "name";
    private static final String COLUMN_NAME_SURA = "sura";
    private static final String COLUMN_NAME_TEXT = "text";

    private final Context applicationContext;
    private SQLiteDatabase sqliteDatabase;

    /**
     * Constructor.
     *
     * @param applicationContext the application context (and not the activity or service context).
     */
    public QuranDatabase(@NonNull Context applicationContext) {
        this.applicationContext = applicationContext;
    }

    /**
     * Opens the Quran database for reading, if it's not already open.
     *
     * @throws QuranDatabaseException if the database could not be opened.
     */
    public void openDatabase() throws QuranDatabaseException {
        if (isDatabaseOpen()) {
            return;
        }

        if (!isDatabaseExistsInInternalStorage()) {
            copyDatabaseFromAssetsToInternalStorage();
        }

        String path = getPathToQuranDatabaseInInternalStorage();

        try {
            sqliteDatabase = SQLiteDatabase.openDatabase(path, null, SQLiteDatabase.OPEN_READONLY);
        } catch (SQLiteException e) {
            throw new QuranDatabaseException("Failed opening the Quran database", e);
        }
    }

    /**
     * Closes the Quran database.
     */
    public void closeDatabase() {
        if (isDatabaseOpen()) {
            sqliteDatabase.close();
        }
    }

    /**
     * Gets the names of all of the Surahs in the Quran.
     *
     * @return the names of all of the Surahs in the Quran.
     * @throws QuranDatabaseException if there was an error getting the Surah names from the database.
     */
    @NonNull
    public List<String> getSurahNames() throws QuranDatabaseException {
        List<String> surahNames = new ArrayList<>();

        String[] columns = new String[]{COLUMN_NAME_NAME};
        String orderBy = COLUMN_NAME_SURA + " ASC ";

        Cursor cursor;

        try {
            cursor = queryDatabase(TABLE_NAME_SURA_NAMES, columns, null, null, null, null, orderBy, null);
        } catch (QuranDatabaseException ex) {
            String message = "Failed getting Surah names";
            throw new QuranDatabaseException(message, ex);
        }

        int columnIndexName = cursor.getColumnIndex(COLUMN_NAME_NAME);

        while (cursor.moveToNext()) {
            surahNames.add(cursor.getString(columnIndexName));
        }

        cursor.close();

        if (surahNames.isEmpty()) {
            String message = "Failed getting Surah names";
            throw new QuranDatabaseException(message);
        }

        return surahNames;
    }

    /**
     * Gets the name of the specified Surah.
     *
     * @param surahNumber is a value between 1 and 114 (inclusive).
     * @return the name of the specified Surah.
     * @throws QuranDatabaseException if there was an error getting the Surah name from the database.
     */
    @NonNull
    public String getSurahName(int surahNumber) throws QuranDatabaseException {
        String surahName = null;

        String[] columns = new String[]{COLUMN_NAME_NAME};
        String selection = COLUMN_NAME_SURA + " = ? ";
        String[] selectionArgs = new String[]{String.valueOf(surahNumber)};
        String limit = "1";

        Cursor cursor;

        try {
            cursor = queryDatabase(TABLE_NAME_SURA_NAMES, columns, selection, selectionArgs, null, null, null, limit);
        } catch (QuranDatabaseException ex) {
            String message = String.format("Failed getting Surah name for Surah %s", surahNumber);
            throw new QuranDatabaseException(message, ex);
        }

        int columnIndexName = cursor.getColumnIndex(COLUMN_NAME_NAME);

        if (cursor.moveToFirst()) {
            surahName = cursor.getString(columnIndexName);
        }

        cursor.close();

        if (surahName == null) {
            String message = String.format("Failed getting Surah name for Surah %s", surahNumber);
            throw new QuranDatabaseException(message);
        }

        return surahName;
    }

    /**
     * Gets all of the Ayahs in the specified Surah.
     *
     * @param surahNumber is a value between 1 and 114 (inclusive).
     * @return the Ayahs of the specified Surah.
     * @throws QuranDatabaseException if there was an error getting the Ayahs from the database.
     */
    @NonNull
    public List<String> getAyahsInSurah(int surahNumber) throws QuranDatabaseException {
        List<String> surahAyahs = new ArrayList<>();

        String[] columns = new String[]{COLUMN_NAME_TEXT};
        String selection = COLUMN_NAME_SURA + " = ? ";
        String[] selectionArgs = new String[]{String.valueOf(surahNumber)};
        String orderBy = COLUMN_NAME_AYA + " ASC ";

        Cursor cursor;

        try {
            cursor = queryDatabase(TABLE_NAME_QURAN_TEXT, columns, selection, selectionArgs, null, null, orderBy, null);
        } catch (QuranDatabaseException ex) {
            String message = String.format("Failed getting Ayahs for Surah %s", surahNumber);
            throw new QuranDatabaseException(message, ex);
        }

        int columnIndexText = cursor.getColumnIndex(COLUMN_NAME_TEXT);

        while (cursor.moveToNext()) {
            surahAyahs.add(cursor.getString(columnIndexText));
        }

        cursor.close();

        if (surahAyahs.isEmpty()) {
            String message = String.format("Failed getting Ayahs for Surah %s", surahNumber);
            throw new QuranDatabaseException(message);
        }

        return surahAyahs;
    }

    /**
     * Gets the text of the specified Ayah.
     *
     * @param surahNumber is a value between 1 and 114 (inclusive).
     * @param ayahNumber  is a value greater than or equal to 1.
     * @return the text of the specified Ayah.
     * @throws QuranDatabaseException if there was an error getting the Ayah from the database.
     */
    @NonNull
    public String getAyah(int surahNumber, int ayahNumber) throws QuranDatabaseException {
        String ayah = null;

        String[] columns = new String[]{COLUMN_NAME_TEXT};
        String selection = COLUMN_NAME_SURA + " = ? AND " + COLUMN_NAME_AYA + " = ? ";
        String[] selectionArgs = new String[]{String.valueOf(surahNumber), String.valueOf(ayahNumber)};
        String limit = "1";

        Cursor cursor;

        try {
            cursor = queryDatabase(TABLE_NAME_QURAN_TEXT, columns, selection, selectionArgs, null, null, null, limit);
        } catch (QuranDatabaseException ex) {
            String message = String.format("Failed getting Ayah for Surah %s, Ayah %s", surahNumber, ayahNumber);
            throw new QuranDatabaseException(message, ex);
        }

        int columnIndexText = cursor.getColumnIndex(COLUMN_NAME_TEXT);

        if (cursor.moveToFirst()) {
            ayah = cursor.getString(columnIndexText);
        }

        cursor.close();

        if (ayah == null) {
            String message = String.format("Failed getting Ayah for Surah %s, Ayah %s", surahNumber, ayahNumber);
            throw new QuranDatabaseException(message);
        }

        return ayah;
    }

    /**
     * This method exists for testing purposes only.
     */
    SQLiteDatabase getSQLiteDatabase() {
        return sqliteDatabase;
    }

    /**
     * (Default package-private visibility for unit testing purposes.)
     *
     * @return true iff the Quran database exists in internal storage.
     */
    boolean isDatabaseExistsInInternalStorage() {
        String path = getPathToQuranDatabaseInInternalStorage();
        File file = new File(path);

        return file.isFile();
    }

    /**
     * (Default package-private visibility for unit testing purposes.)
     *
     * @return true iff the Quran database is open for reading.
     */
    boolean isDatabaseOpen() {
        return sqliteDatabase != null && sqliteDatabase.isOpen();
    }

    @NonNull
    private String getPathToQuranDatabaseInInternalStorage() {
        return applicationContext.getFilesDir().getPath() + "/" + DATABASE_NAME;
    }

    /**
     * Copies the Quran database from assets to internal storage,
     * so that it can be accessed and handled.
     *
     * @throws QuranDatabaseException if the database could not be copied.
     */
    private void copyDatabaseFromAssetsToInternalStorage() {
        try (InputStream inputStream = applicationContext.getAssets().open(DATABASE_NAME);
             OutputStream outputStream = applicationContext.openFileOutput(DATABASE_NAME, Context.MODE_PRIVATE)) {
            StreamCopier streamCopier = new StreamCopier();
            streamCopier.copy(inputStream, outputStream);
        } catch (IOException e) {
            throw new QuranDatabaseException("Failed copying Quran database from assets to internal storage", e);
        }
    }

    /**
     * Queries the Quran database with the specified parameters.
     *
     * @return the result of the query.
     * @throws QuranDatabaseException if the database is not open for reading.
     */
    @NonNull
    private Cursor queryDatabase(String table,
                                 String[] columns,
                                 String selection,
                                 String[] selectionArgs,
                                 String groupBy,
                                 String having,
                                 String orderBy,
                                 String limit) throws QuranDatabaseException {
        if (!isDatabaseOpen()) {
            openDatabase();
        }
        return sqliteDatabase.query(table, columns, selection, selectionArgs, groupBy, having, orderBy, limit);
    }
}