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
class QuranDatabase: NSObject {

    var db: OpaquePointer? = nil

    /**
     * Opens the Quran database for reading, if it's not already open.
     *
     * - Throws: `QuranDatabaseError.FailedOpeningDatabase` if the database could not be opened.
     */
    func openDatabase() throws {
        if (db != nil) {
            return
        }

        let fileManager = FileManager.default

        var documentsURL: URL

        do {
            documentsURL = try fileManager.url(
                    for: .documentDirectory,
                    in: .userDomainMask,
                    appropriateFor: nil,
                    create: true
            ).appendingPathComponent("com.tazkiyatech.quran.db")
        } catch {
            throw QuranDatabaseError.FailedOpeningDatabase(
                    message: "Failed created document directory URL",
                    underlyingError: error
            )
        }

        var resultCode = sqlite3_open_v2(documentsURL.path, &db, SQLITE_OPEN_READWRITE, nil)

        if resultCode == SQLITE_CANTOPEN {
            let bundle = Bundle(for: type(of: self))
            let bundleURL = bundle.url(forResource: "com.tazkiyatech.quran", withExtension: "db")!
            
            do {
                try fileManager.copyItem(at: bundleURL, to: documentsURL)
            } catch {
                throw QuranDatabaseError.FailedOpeningDatabase(
                        message: "Failed copying database from Bundle to Document directory URL",
                        underlyingError: error
                )
            }
            resultCode = sqlite3_open_v2(documentsURL.path, &db, SQLITE_OPEN_READWRITE, nil)
        }

        if resultCode != SQLITE_OK {
            throw QuranDatabaseError.FailedOpeningDatabase(
                    message: "Failed opening database: \(resultCode)",
                    underlyingError: nil
            )
        }
    }

    /**
     * Closes the Quran database.
     *
     * - Throws: `QuranDatabaseError.FailedClosingDatabase` if the database could not be closed.
     */
    func closeDatabase() throws {
        if (db == nil) {
            return;
        }

        let resultCode = sqlite3_close(db)

        if (resultCode != SQLITE_OK) {
            throw QuranDatabaseError.FailedClosingDatabase(message: "Failed closing database: \(resultCode)")
        }

        db = nil;
    }

    /**
     * Gets the names of all of the Surahs in the Quran.
     *
     * - Returns: the names of all of the Surahs in the Quran.
     * - Throws: `QuranDatabaseError.FailedExecutingQuery` if there was an error getting the Surah names from the database.
     */
    func getSurahNames() throws -> [String] {
        do {
            return try query("SELECT name FROM sura_names;")
        } catch {
            throw QuranDatabaseError.FailedExecutingQuery(
                message: "Failed getting Surah names",
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
    func getSurahName(_ surahNumber: Int) throws -> String {
        do {
            return try query("SELECT name FROM sura_names WHERE sura=\(surahNumber);")[0]
        } catch {
            throw QuranDatabaseError.FailedExecutingQuery(
                message: "Failed getting Surah name for Surah \(surahNumber)",
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
    func getAyahsInSurah(_ surahNumber: Int) throws -> [String] {
        do {
            return try query("SELECT text FROM quran_text WHERE sura=\(surahNumber);")
        } catch {
            throw QuranDatabaseError.FailedExecutingQuery(
                message: "Failed getting Ayahs for Surah \(surahNumber)",
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
    func getAyah(surahNumber: Int, ayahNumber: Int) throws -> String {
        do {
            return try query("SELECT text FROM quran_text WHERE sura=\(surahNumber) AND aya=\(ayahNumber);")[0]
        } catch {
            throw QuranDatabaseError.FailedExecutingQuery(
                message: "Failed Ayah for Surah \(surahNumber), Ayah \(ayahNumber)",
                underlyingError: error
            )
        }
    }

    /**
     * Queries the Quran database with the specified SQL query.
     *
     * - Parameter queryStatementString: the SQL query to perform.
     * - Returns: the result of the query.
     */
    private func query(_ queryStatementString: String) throws -> [String] {
        if (db == nil) {
            try openDatabase()
        }
        
        var queryStatement: OpaquePointer? = nil

        let resultCode = sqlite3_prepare_v2(db, queryStatementString, -1, &queryStatement, nil)

        var rows: [String] = []

        if (resultCode == SQLITE_OK) {
            while (sqlite3_step(queryStatement) == SQLITE_ROW) {
                let columnText = sqlite3_column_text(queryStatement, 0)
                let name = String(cString: columnText!)
                rows.append(name)
            }
        } else {
            if let errorPointer = sqlite3_errmsg(db) {
                let message = String.init(cString: errorPointer)
                throw QuranDatabaseError.FailedPreparingQuery(message: message)
            } else {
                throw QuranDatabaseError.FailedPreparingQuery(message: "No error message provided from sqlite.")
            }
        }

        sqlite3_finalize(queryStatement)

        return rows
    }
}

enum QuranDatabaseError: Error {

    case FailedOpeningDatabase(message: String, underlyingError: Error?)
    case FailedPreparingQuery(message: String)
    case FailedExecutingQuery(message: String, underlyingError: Error)
    case FailedClosingDatabase(message: String)

}
