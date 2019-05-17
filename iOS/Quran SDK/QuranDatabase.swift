//
//  QuranDatabase.swift
//  Quran SDK
//
//  Created by Adil Hussain on 23/11/2018.
//  Copyright © 2018 Tazkiya Tech. All rights reserved.
//

import SQLite3

/**
 * Helper class which handles the creation and opening of the SQLite-based Quran database
 * and provides easy methods for accessing its content.
 */
public class QuranDatabase: NSObject {

    private var database: OpaquePointer? = nil

    /**
     * Opens the Quran database for reading, if it's not already open.
     *
     * - Throws: `QuranDatabaseError.FailedOpeningDatabase` if the database could not be opened.
     */
    public func openDatabase() throws {
        if (isDatabaseOpen()) {
            return
        }

        var databaseExistsInDocumentsDirectory: Bool

        do {
            databaseExistsInDocumentsDirectory = try isDatabaseExistsInDocumentsDirectory()
        } catch {
            throw QuranDatabaseError.FailedOpeningDatabase(
                    "Failed determining whether database exists in Documents directory",
                    underlyingError: error
            )
        }

        do {
            if (!databaseExistsInDocumentsDirectory) {
                try copyDatabaseFromBundleToDocumentsDirectory()
            }
        } catch {
            throw QuranDatabaseError.FailedOpeningDatabase(
                    "Failed copying database from Bundle to Documents directory",
                    underlyingError: error
            )
        }

        var documentsDirectoryURL: URL

        do {
            documentsDirectoryURL = try getURLForQuranDatabaseInDocumentsDirectory()
        } catch {
            throw QuranDatabaseError.FailedOpeningDatabase(
                    "Failed created Documents directory URL",
                    underlyingError: error
            )
        }

        let resultCode = sqlite3_open_v2(documentsDirectoryURL.path, &database, SQLITE_OPEN_READONLY, nil)

        if resultCode != SQLITE_OK {
            throw QuranDatabaseError.FailedOpeningDatabase(
                    "SQLite result code = \(resultCode)",
                    underlyingError: nil
            )
        }
    }

    /**
     * Closes the Quran database.
     *
     * - Throws: `QuranDatabaseError.FailedClosingDatabase` if the database could not be closed.
     */
    public func closeDatabase() throws {
        if (!isDatabaseOpen()) {
            return;
        }

        let resultCode = sqlite3_close(database)

        if (resultCode != SQLITE_OK) {
            throw QuranDatabaseError.FailedClosingDatabase("SQLite result code = \(resultCode)")
        }

        database = nil;
    }

    /**
     * Gets the names of all of the Surahs in the Quran.
     *
     * - Returns: the names of all of the Surahs in the Quran.
     * - Throws: `QuranDatabaseError.FailedExecutingQuery` if there was an error getting the Surah names from the database.
     */
    public func getSurahNames() throws -> [String] {
        do {
            return try query("SELECT name FROM sura_names;")
        } catch {
            throw QuranDatabaseError.FailedExecutingQuery(
                    "Failed getting Surah names",
                    underlyingError: error
            )
        }
    }

    /**
     * Gets the name of the specified Surah.
     *
     * - Parameter surahNumber: is a value between 1 and 114 (inclusive).
     * - Returns: the name of the specified Surah.
     * - Throws: `QuranDatabaseError.FailedExecutingQuery` if there was an error getting the Surah name from the database.
     */
    public func getSurahName(_ surahNumber: Int) throws -> String {
        do {
            return try query("SELECT name FROM sura_names WHERE sura=\(surahNumber);")[0]
        } catch {
            throw QuranDatabaseError.FailedExecutingQuery(
                    "Failed getting Surah name for Surah \(surahNumber)",
                    underlyingError: error
            )
        }
    }

    /**
     * Gets all of the Ayahs in the specified Surah.
     *
     * - Parameter surahNumber: is a value between 1 and 114 (inclusive).
     * - Returns: the Ayahs of the specified Surah.
     * - Throws: `QuranDatabaseError.FailedExecutingQuery` if there was an error getting the Ayahs from the database.
     */
    public func getAyahsInSurah(_ surahNumber: Int) throws -> [String] {
        do {
            return try query("SELECT text FROM quran_text WHERE sura=\(surahNumber);")
        } catch {
            throw QuranDatabaseError.FailedExecutingQuery(
                    "Failed getting Ayahs for Surah \(surahNumber)",
                    underlyingError: error
            )
        }
    }

    /**
     * Gets the text of the specified Ayah.
     *
     * - Parameter surahNumber: is a value between 1 and 114 (inclusive).
     * - Parameter ayahNumber: is a value greater than or equal to 1.
     * - Returns: the text of the specified Ayah.
     * - Throws: `QuranDatabaseError.FailedExecutingQuery` if there was an error getting the Ayah from the database.
     */
    public func getAyah(surahNumber: Int, ayahNumber: Int) throws -> String {
        do {
            return try query("SELECT text FROM quran_text WHERE sura=\(surahNumber) AND aya=\(ayahNumber);")[0]
        } catch {
            throw QuranDatabaseError.FailedExecutingQuery(
                    "Failed Ayah for Surah \(surahNumber), Ayah \(ayahNumber)",
                    underlyingError: error
            )
        }
    }

    internal func isDatabaseExistsInDocumentsDirectory() throws -> Bool {
        let documentsDirectoryURL = try getURLForQuranDatabaseInDocumentsDirectory()

        return (try? documentsDirectoryURL.checkResourceIsReachable()) ?? false
    }

    internal func isDatabaseOpen() -> Bool {
        return database != nil;
    }

    /**
     * Queries the Quran database with the specified SQL query.
     *
     * - Parameter sql: the SQL query to perform.
     * - Returns: the result of the query.
     */
    private func query(_ sql: String) throws -> [String] {
        if (!isDatabaseOpen()) {
            try openDatabase()
        }

        var statementHandle: OpaquePointer? = nil

        defer {
            sqlite3_finalize(statementHandle)
        }

        let resultCode = sqlite3_prepare_v2(database, sql, -1, &statementHandle, nil)

        if (resultCode != SQLITE_OK) {
            throw QuranDatabaseError.FailedPreparingQuery("SQLite result code = \(resultCode)")
        }

        var rows: [String] = []

        while (sqlite3_step(statementHandle) == SQLITE_ROW) {
            let columnTextPointer = sqlite3_column_text(statementHandle, 0)
            let columnText = String(cString: columnTextPointer!)
            rows.append(columnText)
        }

        return rows
    }

    private func copyDatabaseFromBundleToDocumentsDirectory() throws {
        let documentsDirectoryURL = try getURLForQuranDatabaseInDocumentsDirectory()

        guard let bundleURL = getURLForQuranDatabaseInFrameworkBundle() else {
            throw QuranDatabaseError.FailedLocatingQuranDatabaseInFrameworkBundle
        }

        try FileManager.default.copyItem(at: bundleURL, to: documentsDirectoryURL)
    }

    private func getURLForQuranDatabaseInDocumentsDirectory() throws -> URL {
        return try FileManager.default.url(
                for: .documentDirectory,
                in: .userDomainMask,
                appropriateFor: nil,
                create: true
        ).appendingPathComponent("com.tazkiyatech.quran.db")
    }

    private func getURLForQuranDatabaseInFrameworkBundle() -> URL? {
        let bundle = Bundle(for: type(of: self))
        return bundle.url(forResource: "com.tazkiyatech.quran", withExtension: "db")!
    }
}

public enum QuranDatabaseError: Error {

    case FailedOpeningDatabase(_ message: String, underlyingError: Error?)
    case FailedPreparingQuery(_ message: String)
    case FailedExecutingQuery(_ message: String, underlyingError: Error)
    case FailedClosingDatabase(_ message: String)
    case FailedLocatingQuranDatabaseInFrameworkBundle

}
