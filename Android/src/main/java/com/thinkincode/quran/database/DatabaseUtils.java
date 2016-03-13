package com.thinkincode.quran.database;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;

public class DatabaseUtils {

	/**
	 * Composes and executes an "EXECUTE QUERY PLAN" command
	 * for the SELECT query provided.
	 * 
	 * @param database the (non-null) SQLite database object against which to run the "EXPLAIN QUERY PLAN" query.
	 * @param sql the (non-null) SQLite SELECT statement for which to run the "EXPLAIN QUERY PLAN" query.
	 * @return the result of the "EXPLAIN QUERY PLAN" query, or null in case of error.
	 */
	public static QueryPlan explainQueryPlanForSelectStatement(SQLiteDatabase database, String sql) {
		sql = "EXPLAIN QUERY PLAN " + sql;
		return executeExplainQueryPlanStatement(database, sql, null);
	}

	/**
	 * Composes and executes an "EXECUTE QUERY PLAN" command
	 * for the SELECT query that would be composed from the parameters provided.
	 *
	 * @return the result of the "EXPLAIN QUERY PLAN" query, or null in case of error.
	 * @see SQLiteDatabase#query(String, String[], String, String[], String, String, String, String)
	 */
	public static QueryPlan explainQueryPlanForSelectStatement(SQLiteDatabase database, String table, String[] columns, String selection, String[] selectionArgs, String groupBy, String having, String orderBy, String limit) {
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

		return executeExplainQueryPlanStatement(database, sb.toString(), selectionArgs);
	}

	/**
	 * Executes the sql command provided using the database object provided.
	 * 
	 * @param database the (non-null) SQLite database object against which to run the "EXPLAIN QUERY PLAN" query.
	 * @param sql the (non-null) "EXPLAIN QUERY PLAN" command which must not be ; terminated.
	 * @param selectionArgs the values to use in place of the ?s in the where clause of <code>sql</code>.
	 * @return the result of the "EXPLAIN QUERY PLAN" query, or null in case of error.
	 */
	private static QueryPlan executeExplainQueryPlanStatement(SQLiteDatabase database, String sql, String[] selectionArgs) {
		Cursor cursor = null;

		try {
			cursor = database.rawQuery(sql, selectionArgs);

			if (cursor.moveToFirst()) {
				final int colIndexSelectId = cursor.getColumnIndex("selectid");
				final int colIndexOrder = cursor.getColumnIndex("order");
				final int colIndexFrom = cursor.getColumnIndex("from");
				final int colIndexDetail = cursor.getColumnIndex("detail");

				final int selectId = cursor.getInt(colIndexSelectId);
				final int order = cursor.getInt(colIndexOrder);
				final int from = cursor.getInt(colIndexFrom);
				final String detail = cursor.getString(colIndexDetail);

				return new QueryPlan(selectId, order, from, detail);
			} else {
				return null;
			}
		} finally {
			if (cursor != null) {
				cursor.close();
			}
		}
	}
}
