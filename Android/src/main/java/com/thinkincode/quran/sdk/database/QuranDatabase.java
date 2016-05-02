package com.thinkincode.quran.sdk.database;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.google.common.io.ByteStreams;
import com.thinkincode.quran.sdk.exception.QuranDatabaseException;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Helper class which is able to create and access the local Qur'an SQLite database.
 */
public class QuranDatabase {

    private static final String DATABASE_NAME = "com.thinkincode.quran.db";

    static final String TABLE_NAME_QURAN_TEXT = "quran_text";
    static final String TABLE_NAME_SURA_NAMES = "sura_names";

    static final String COLUMN_NAME_AYA = "aya";
    static final String COLUMN_NAME_NAME = "name";
    static final String COLUMN_NAME_SURA = "sura";
    static final String COLUMN_NAME_TEXT = "text";

    private SQLiteDatabase sqliteDatabase; 

	/**
     * Opens the Qur'an database for reading.
     *
     * @param context is non-null.
     * */
    public void openDatabase(Context context) throws IOException {
    	if (!isDatabaseExistsInInternalStorage(context)) {
        	copyDatabaseFromAssetsToInternalStorage(context);
    	}

		openDatabaseForReadingIfClosed(context);
    }

    /**
     * Closes the Qur'an database.
     */
	public void closeDatabase() {
    	if (sqliteDatabase != null) {
    		sqliteDatabase.close();
    	}
	}

	/**
	 * @param surahNumber >= 1 and <= 114.
	 * @return the name of the specified Surah,
	 * or null if the Surah number is not valid.
	 */
	public String getSurahName(int surahNumber) {
		String surahName = null;

		String[] columns = new String[] { COLUMN_NAME_NAME };
		String selection = COLUMN_NAME_SURA + " = ? ";
		String[] selectionArgs = new String[] { String.valueOf(surahNumber) };
		String limit = "1";

		Cursor cursor = queryDatabase(TABLE_NAME_SURA_NAMES, columns, selection, selectionArgs, null, null, null, limit);

		int columnIndexName = cursor.getColumnIndex(COLUMN_NAME_NAME);

		if (cursor.moveToFirst()) {
			surahName = cursor.getString(columnIndexName);
		}

		cursor.close();

		return surahName;
	}

	/**
	 * @return the names of all the Surahs in the Qur'an.
	 */
	public List<String> getSurahNames() {
		List<String> surahNames = new ArrayList<>();

		String[] columns = new String[] { COLUMN_NAME_NAME };
		String orderBy = COLUMN_NAME_SURA + " ASC ";

		Cursor cursor = queryDatabase(TABLE_NAME_SURA_NAMES, columns, null, null, null, null, orderBy, null);

		int columnIndexName = cursor.getColumnIndex(COLUMN_NAME_NAME);

		while (cursor.moveToNext()) {
			surahNames.add(cursor.getString(columnIndexName));
		}

		cursor.close();

		return surahNames;
	}

	/**
	 * @param surahNumber >= 1 and <= 114.
	 * @return the ayahs of the specified Surah,
	 * or null if the Surah number is not valid.
	 */
	public List<String> getAyahsInSurah(int surahNumber) {
		List<String> surahAyahs = new ArrayList<>();

		String[] columns = new String[] { COLUMN_NAME_TEXT };
		String selection = COLUMN_NAME_SURA + " = ? ";
		String[] selectionArgs = new String[] { String.valueOf(surahNumber) };
		String orderBy = COLUMN_NAME_AYA + " ASC ";

		Cursor cursor = queryDatabase(TABLE_NAME_QURAN_TEXT, columns, selection, selectionArgs, null, null, orderBy, null);

		int columnIndexText = cursor.getColumnIndex(COLUMN_NAME_TEXT);

		while (cursor.moveToNext()) {
			surahAyahs.add(cursor.getString(columnIndexText));
		}

		cursor.close();

		return surahAyahs;
	}

	/**
	 * @param surahNumber >= 1 and <= 114.
	 * @param ayahNumber >= 1.
	 * @return the text of the specified Ayah,
	 * or null if the Surah and Ayah number provided do not map to an Ayah.
	 */
	public String getAyah(int surahNumber, int ayahNumber) {
		String ayah = null;

		String[] columns = new String[] { COLUMN_NAME_TEXT };
		String selection = COLUMN_NAME_SURA + " = ? AND " + COLUMN_NAME_AYA + " = ? ";
		String[] selectionArgs = new String[] { String.valueOf(surahNumber), String.valueOf(ayahNumber) };
		String limit = "1";

		Cursor cursor = queryDatabase(TABLE_NAME_QURAN_TEXT, columns, selection, selectionArgs, null, null, null, limit);

		int columnIndexText = cursor.getColumnIndex(COLUMN_NAME_TEXT);

		if (cursor.moveToFirst()) {
			ayah = cursor.getString(columnIndexText);
		}

		cursor.close();

		return ayah;
	}

	/**
	 * (Default package-private visibility for unit testing purposes.)
	 *
	 * @return the {@link #sqliteDatabase}.
	 */
	SQLiteDatabase getSQLiteDatabase() {
		return sqliteDatabase;
	}

	/**
	 * (Default package-private visibility for unit testing purposes.)
	 *
	 * @param context is non-null.
	 * @return true iff the Qur'an database exists in internal storage.
	 */
    boolean isDatabaseExistsInInternalStorage(Context context) {
		String path = context.getFilesDir().getPath() + "/" + DATABASE_NAME;
		File file = new File(path);

		return file.isFile();
	}

    /**
     * (Default package-private visibility for unit testing purposes.)
     *
     * @return true iff the Qur'an database is open for reading.
     */
    boolean isDatabaseOpen() {
        return sqliteDatabase != null && sqliteDatabase.isOpen();
    }

	/**
	 * Copies the Qur'an database from assets to internal storage,
	 * so that it can be accessed and handled.
	 *
	 * @param context is non-null.
	 * */
	private void copyDatabaseFromAssetsToInternalStorage(Context context) throws IOException {
		// Read from the local database in assets
		InputStream inputStream = context.getAssets().open(DATABASE_NAME);

		// Write to a local database in internal storage
		OutputStream outputStream = context.openFileOutput(DATABASE_NAME, Context.MODE_PRIVATE);

		// Transfer bytes from the input file to the output file
        ByteStreams.copy(inputStream, outputStream);

		// Close the streams
		outputStream.flush();
		outputStream.close();
		inputStream.close();
	}

	/**
	 * @param context is non-null.
	 * @throws SQLException
	 */
    private void openDatabaseForReadingIfClosed(Context context) {
        if (!isDatabaseOpen()) {
            String myPath = context.getFilesDir().getPath() + "/" + DATABASE_NAME;
            sqliteDatabase = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);
        }
	}

	/**
     * Queries the local Qur'an database with the specified parameters.
     *
	 * @param table
	 * @param columns
	 * @param selection
	 * @param selectionArgs
	 * @param groupBy
	 * @param having
	 * @param orderBy
	 * @param limit
	 * @return the result of the query.
	 */
	private Cursor queryDatabase(String table, String[] columns, String selection, String[] selectionArgs, String groupBy, String having, String orderBy, String limit) {
        if (!isDatabaseOpen()) {
            String message = "Could not query the Qur'an database. " +
                    "Ensure that the QuranDatabase.openDatabase(Context) method has been called before attempting to read from the database.";

            throw new QuranDatabaseException(message);
        }

		return sqliteDatabase.query(table, columns, selection, selectionArgs, groupBy, having, orderBy, limit);
	}
}
