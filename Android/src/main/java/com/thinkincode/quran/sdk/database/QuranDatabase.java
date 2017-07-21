package com.thinkincode.quran.sdk.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.thinkincode.quran.sdk.exception.QuranDatabaseException;
import com.thinkincode.utils.streams.StreamCopier;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Helper class which is able to create and access the local Quran SQLite database.
 */
public class QuranDatabase {

    private static final String DATABASE_NAME = "com.thinkincode.quran.db";

    private static final String TABLE_NAME_QURAN_TEXT = "quran_text";
    private static final String TABLE_NAME_SURA_NAMES = "sura_names";

    private static final String COLUMN_NAME_AYA = "aya";
    private static final String COLUMN_NAME_NAME = "name";
    private static final String COLUMN_NAME_SURA = "sura";
    private static final String COLUMN_NAME_TEXT = "text";

    @NonNull
    private final Context applicationContext;

    @Nullable
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
     * Initialises the Quran database for reading.
     *
     * @throws QuranDatabaseException if the database could not be initialised.
     */
    public void initialise() throws QuranDatabaseException {
        try {
            if (!isDatabaseExistsInInternalStorage()) {
                copyDatabaseFromAssetsToInternalStorage();
            }
        } catch (IOException e) {
            throw new QuranDatabaseException("Failed initialising the Quran database", e);
        }
    }

    /**
     * Closes the Quran database.
     */
    public void closeDatabase() {
        if (sqliteDatabase != null) {
            sqliteDatabase.close();
        }
    }

    /**
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

        Cursor cursor = queryDatabase(TABLE_NAME_SURA_NAMES, columns, selection, selectionArgs, null, null, null, limit);

        int columnIndexName = cursor.getColumnIndex(COLUMN_NAME_NAME);

        if (cursor.moveToFirst()) {
            surahName = cursor.getString(columnIndexName);
        }

        cursor.close();

        if (surahName == null) {
            String message = String.format("Failed getting Surah name from the Quran database for Surah %s", surahNumber);
            throw new QuranDatabaseException(message);
        }

        return surahName;
    }

    /**
     * @return the names of all the Surahs in the Quran.
     * @throws QuranDatabaseException if there was an error getting the Surah names from the database.
     */
    @NonNull
    public List<String> getSurahNames() throws QuranDatabaseException {
        List<String> surahNames = new ArrayList<>();

        String[] columns = new String[]{COLUMN_NAME_NAME};
        String orderBy = COLUMN_NAME_SURA + " ASC ";

        Cursor cursor = queryDatabase(TABLE_NAME_SURA_NAMES, columns, null, null, null, null, orderBy, null);

        int columnIndexName = cursor.getColumnIndex(COLUMN_NAME_NAME);

        while (cursor.moveToNext()) {
            surahNames.add(cursor.getString(columnIndexName));
        }

        cursor.close();

        if (surahNames.isEmpty()) {
            String message = "Failed getting Surah names from the Quran database";
            throw new QuranDatabaseException(message);
        }

        return surahNames;
    }

    /**
     * @param surahNumber is a value between 1 and 114 (inclusive).
     * @return the ayahs of the specified Surah.
     * @throws QuranDatabaseException if there was an error getting the Ayahs from the database.
     */
    @NonNull
    public List<String> getAyahsInSurah(int surahNumber) throws QuranDatabaseException {
        List<String> surahAyahs = new ArrayList<>();

        String[] columns = new String[]{COLUMN_NAME_TEXT};
        String selection = COLUMN_NAME_SURA + " = ? ";
        String[] selectionArgs = new String[]{String.valueOf(surahNumber)};
        String orderBy = COLUMN_NAME_AYA + " ASC ";

        Cursor cursor = queryDatabase(TABLE_NAME_QURAN_TEXT, columns, selection, selectionArgs, null, null, orderBy, null);

        int columnIndexText = cursor.getColumnIndex(COLUMN_NAME_TEXT);

        while (cursor.moveToNext()) {
            surahAyahs.add(cursor.getString(columnIndexText));
        }

        cursor.close();

        if (surahAyahs.isEmpty()) {
            String message = String.format("Failed getting Ayahs from the Quran database for Surah %s", surahNumber);
            throw new QuranDatabaseException(message);
        }

        return surahAyahs;
    }

    /**
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

        Cursor cursor = queryDatabase(TABLE_NAME_QURAN_TEXT, columns, selection, selectionArgs, null, null, null, limit);

        int columnIndexText = cursor.getColumnIndex(COLUMN_NAME_TEXT);

        if (cursor.moveToFirst()) {
            ayah = cursor.getString(columnIndexText);
        }

        cursor.close();

        if (ayah == null) {
            String message = String.format("Failed getting Ayah from the Quran database for Surah %s, Ayah %s", surahNumber, ayahNumber);
            throw new QuranDatabaseException(message);
        }

        return ayah;
    }

    /**
     * (Default package-private visibility for unit testing purposes.)
     *
     * @return the {@link #sqliteDatabase}.
     */
    @NonNull
    SQLiteDatabase getSQLiteDatabase() {
        if (!isDatabaseOpen()) {
            openDatabase();
        }

        return sqliteDatabase;
    }

    /**
     * (Default package-private visibility for unit testing purposes.)
     *
     * @return true iff the Quran database exists in internal storage.
     */
    boolean isDatabaseExistsInInternalStorage() {
        String path = applicationContext.getFilesDir().getPath() + "/" + DATABASE_NAME;
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

    /**
     * Opens the Quran database for reading, if it's not already open.
     *
     * @throws SQLiteException if the database could not be opened.
     */
    void openDatabase() throws SQLiteException {
        if (!isDatabaseOpen()) {
            String myPath = applicationContext.getFilesDir().getPath() + "/" + DATABASE_NAME;
            sqliteDatabase = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);
        }
    }

    /**
     * Copies the Quran database from assets to internal storage,
     * so that it can be accessed and handled.
     *
     * @throws IOException if there was an I/O error.
     */
    private void copyDatabaseFromAssetsToInternalStorage() throws IOException {
        // Read from the local database in assets
        InputStream inputStream = applicationContext.getAssets().open(DATABASE_NAME);

        // Write to a local database in internal storage
        OutputStream outputStream = applicationContext.openFileOutput(DATABASE_NAME, Context.MODE_PRIVATE);

        // Transfer bytes from the input file to the output file
        StreamCopier streamCopier = new StreamCopier();
        streamCopier.copy(inputStream, outputStream);

        // Close the streams
        outputStream.flush();
        outputStream.close();
        inputStream.close();
    }

    /**
     * Queries the local Quran database with the specified parameters.
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
        if (!isDatabaseExistsInInternalStorage()) {
            String message = "Could not query the Quran database. " +
                    "Ensure that the QuranDatabase.initialise() method has been called before attempting to read from the database.";

            throw new QuranDatabaseException(message);
        }

        openDatabase();

        return sqliteDatabase.query(table, columns, selection, selectionArgs, groupBy, having, orderBy, limit);
    }
}
