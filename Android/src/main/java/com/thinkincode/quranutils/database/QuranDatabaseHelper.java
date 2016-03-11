package com.thinkincode.quranutils.database;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Helper class which is able to create and access the local Qur'an SQLite database.
 */
public class QuranDatabaseHelper {

    private static final String DATABASE_NAME = "quran.db";

    private static final String TABLE_NAME_QURAN_TEXT = "quran_text";
    private static final String TABLE_NAME_SURA_NAMES = "sura_names";

    private static final String COLUMN_NAME_AYA = "aya";
    private static final String COLUMN_NAME_NAME = "name";
    private static final String COLUMN_NAME_SURA = "sura";
    private static final String COLUMN_NAME_TEXT = "text";

    private SQLiteDatabase sqliteDatabase; 

	/**
     * Copies the Qur'an database from assets to internal storage database
     * if it does not already exist in internal storage.
     *
     * @param context is non-null.
     * */
    public void createDatabaseIfDoesNotExist(Context context) throws IOException {
    	if (!isDatabaseExistsInInternalStorage(context)) {
        	copyDatabaseFromAssetsToInternalStorage(context);
    	}
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
     * @param context is non-null.
	 * @param surahNumber >= 1 and <= 114.
	 * @return the name of the specified Surah,
	 * or null if the Surah number is not valid.
	 */
	public String getSurahName(Context context, int surahNumber) {
		String surahName = null;

		String[] columns = new String[] { COLUMN_NAME_NAME };
		String selection = COLUMN_NAME_SURA + " = ? ";
		String[] selectionArgs = new String[] { String.valueOf(surahNumber) };
		String limit = "1";

		Cursor cursor = queryDatabase(context, TABLE_NAME_SURA_NAMES, columns, selection, selectionArgs, null, null, null, limit);

		int columnIndexName = cursor.getColumnIndex(COLUMN_NAME_NAME);

		if (cursor.moveToFirst()) {
			surahName = cursor.getString(columnIndexName);
		}

		cursor.close();

		return surahName;
	}

	/**
     * @param context is non-null.
	 * @return the names of all the Surahs in the Qur'an.
	 */
	public List<String> getSurahNames(Context context) {
		List<String> surahNames = new ArrayList<>();

		String[] columns = new String[] { COLUMN_NAME_NAME };
		String orderBy = COLUMN_NAME_SURA + " ASC ";

		Cursor cursor = queryDatabase(context, TABLE_NAME_SURA_NAMES, columns, null, null, null, null, orderBy, null);

		int columnIndexName = cursor.getColumnIndex(COLUMN_NAME_NAME);

		while (cursor.moveToNext()) {
			surahNames.add(cursor.getString(columnIndexName));
		}

		cursor.close();

		return surahNames;
	}

	/**
     * @param context is non-null.
	 * @param surahNumber >= 1 and <= 114.
	 * @return the ayahs of the specified Surah,
	 * or null if the Surah number is not valid.
	 */
	public List<String> getAyahsInSurah(Context context, int surahNumber) {
		List<String> surahAyahs = new ArrayList<>();

		String[] columns = new String[] { COLUMN_NAME_TEXT };
		String selection = COLUMN_NAME_SURA + " = ? ";
		String[] selectionArgs = new String[] { String.valueOf(surahNumber) };
		String orderBy = COLUMN_NAME_AYA + " ASC ";

		Cursor cursor = queryDatabase(context, TABLE_NAME_QURAN_TEXT, columns, selection, selectionArgs, null, null, orderBy, null);

		int columnIndexText = cursor.getColumnIndex(COLUMN_NAME_TEXT);

		while (cursor.moveToNext()) {
			surahAyahs.add(cursor.getString(columnIndexText));
		}

		cursor.close();

		return surahAyahs;
	}

	/**
     * @param context is non-null.
	 * @param surahNumber >= 1 and <= 114.
	 * @param ayahNumber >= 1.
	 * @return the text of the specified Ayah,
	 * or null if the Surah and Ayah number provided do not map to an Ayah.
	 */
	public String getAyah(Context context, int surahNumber, int ayahNumber) {
		String ayah = null;

		String[] columns = new String[] { COLUMN_NAME_TEXT };
		String selection = COLUMN_NAME_SURA + " = ? AND " + COLUMN_NAME_AYA + " = ? ";
		String[] selectionArgs = new String[] { String.valueOf(surahNumber), String.valueOf(ayahNumber) };
		String limit = "1";

		Cursor cursor = queryDatabase(context, TABLE_NAME_QURAN_TEXT, columns, selection, selectionArgs, null, null, null, limit);

		int columnIndexText = cursor.getColumnIndex(COLUMN_NAME_TEXT);

		if (cursor.moveToFirst()) {
			ayah = cursor.getString(columnIndexText);
		}

		cursor.close();

		return ayah;
	}

	/**
	 * Checks whether the Qur'an database exists in internal storage.
	 *
	 * @param context is non-null.
	 * @return true iff the Qur'an database exists in internal storage.
	 */
	private boolean isDatabaseExistsInInternalStorage(Context context) {
		SQLiteDatabase checkDatabase = null;

		try {
			String myPath = context.getFilesDir().getPath() + "/" + DATABASE_NAME;
			checkDatabase = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);
		} catch (SQLiteException ex) {
			//database doesn't exist
		}

		if (checkDatabase != null){
			checkDatabase.close();
		}

		return checkDatabase != null;
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
		byte[] buffer = new byte[1024];
		int length;
		while ((length = inputStream.read(buffer)) > 0) {
			outputStream.write(buffer, 0, length);
		}

		// Close the streams
		outputStream.flush();
		outputStream.close();
		inputStream.close();
	}

	/**
	 * @param context is non-null.
	 * @throws SQLException
	 */
	private void openDatabaseForReadingIfClosed(Context context) throws SQLException {
		if (sqliteDatabase == null || !sqliteDatabase.isOpen()) {
			String myPath = context.getFilesDir().getPath() + "/" + DATABASE_NAME;
			sqliteDatabase = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);
		}
	}

	/**
     * Queries the local Qur'an database with the specified parameters.
     *
	 * @param context is non-null.
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
	private Cursor queryDatabase(Context context, String table, String[] columns, String selection, String[] selectionArgs, String groupBy, String having, String orderBy, String limit) {
		openDatabaseForReadingIfClosed(context);
//		DatabaseUtils.explainQueryPlanForSelectStatement(sqliteDatabase, table, columns, selection, selectionArgs, groupBy, having, orderBy, limit);
		return sqliteDatabase.query(table, columns, selection, selectionArgs, groupBy, having, orderBy, limit);
	}
}
