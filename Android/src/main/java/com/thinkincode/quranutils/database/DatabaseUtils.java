package com.thinkincode.quranutils.database;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;
import android.util.Log;

public class DatabaseUtils {

	private final static String TAG = DatabaseUtils.class.getSimpleName();

	/**
	 * Composes and executes an EXECUTE QUERY PLAN command
	 * for the select query provided.
	 * 
	 * @param database is non-null.
	 * @param sql non-null.
	 */
	public static void explainQueryPlanForSelectStatement(SQLiteDatabase database, String sql) {
		sql = "EXPLAIN QUERY PLAN " + sql;
		executeExplainQueryPlanStatement(database, sql, null);
	}

	/**
	 * Composes and executes an EXECUTE QUERY PLAN command
	 * for the SELECT query that would be composed from the parameters provided.
	 * 
	 * @see {@link SQLiteDatabase#query(String, String[], String, String[], String, String, String, String)} for a description of this method's parameters.
	 */
	public static void explainQueryPlanForSelectStatement(SQLiteDatabase database, String table, String[] columns, String selection, String[] selectionArgs, String groupBy, String having, String orderBy, String limit) {
		final StringBuilder sb = new StringBuilder();
		sb.append("EXPLAIN QUERY PLAN SELECT ");

		if (columns == null || columns.length == 0) {
			sb.append(" * ");
		} else {
			boolean firstColumn = true;

			for (String column : columns) {
				if (!firstColumn) {
					sb.append(", ");
				}

				sb.append(column);

				firstColumn = false;
			}
		}

		sb.append(" FROM ");
		sb.append(table);

		if (!TextUtils.isEmpty(selection)) {
			sb.append(" WHERE ");
			sb.append(selection);
		}

		if (!TextUtils.isEmpty(groupBy)) {
			sb.append(" GROUP BY ");
			sb.append(groupBy);
		}

		if (!TextUtils.isEmpty(having)) {
			sb.append(" HAVING ");
			sb.append(having);
		}

		if (!TextUtils.isEmpty(orderBy)) {
			sb.append(" ORDER BY ");
			sb.append(orderBy);
		}

		if (!TextUtils.isEmpty(limit)) {
			sb.append(" LIMIT ");
			sb.append(limit);
		}

		executeExplainQueryPlanStatement(database, sb.toString(), selectionArgs);
	}

	/**
	 * Executes <code>sql</code> using <code>database</code>
	 * and prints the result to logs.
	 * 
	 * @param database the {@link SQLiteDatabase} instance to use to execute the query.
	 * @param sql is an EXPLAIN QUERY PLAN command which must not be ; terminated.
	 * @param selectionArgs the values to replace the ?s in the where clause of <code>sql</code>.
	 */
	public static void executeExplainQueryPlanStatement(SQLiteDatabase database, String sql, String[] selectionArgs) {
		final Cursor cursor = database.rawQuery(sql, selectionArgs);

		if (cursor.moveToFirst()) {
			final int colIndexSelectId = cursor.getColumnIndex("selectid");
			final int colIndexOrder = cursor.getColumnIndex("order");
			final int colIndexFrom = cursor.getColumnIndex("from");
			final int colIndexDetail = cursor.getColumnIndex("detail");

			final int selectId = cursor.getInt(colIndexSelectId);
			final int order = cursor.getInt(colIndexOrder);
			final int from = cursor.getInt(colIndexFrom);
			final String detail = cursor.getString(colIndexDetail);

			Log.d(TAG, sql);
			Log.d(TAG, String.format("%d | %d | %d | %s", selectId, order, from, detail));
		}

		cursor.close();
	}
}
