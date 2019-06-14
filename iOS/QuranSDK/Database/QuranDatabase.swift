//
//  QuranDatabase.swift
//  QuranSDK
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

        var databaseExistsInInternalStorage: Bool

        do {
            databaseExistsInInternalStorage = try isDatabaseExistsInInternalStorage()
        } catch {
            throw QuranDatabaseError.FailedOpeningDatabase(
                    "Failed determining whether database exists in internal storage",
                    underlyingError: error
            )
        }

        do {
            if (!databaseExistsInInternalStorage) {
                try copyDatabaseFromFrameworkBundleToInternalStorage()
            }
        } catch {
            throw QuranDatabaseError.FailedOpeningDatabase(
                    "Failed copying database from framework bundle to internal storage",
                    underlyingError: error
            )
        }

        var internalStorageURL: URL

        do {
            internalStorageURL = try getURLForQuranDatabaseInInternalStorage()
        } catch {
            throw QuranDatabaseError.FailedOpeningDatabase(
                    "Failed created internal storage URL for Quran database",
                    underlyingError: error
            )
        }

        let resultCode = sqlite3_open_v2(internalStorageURL.path, &database, SQLITE_OPEN_READONLY, nil)

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
        var statementObject: OpaquePointer? = nil
        
        defer {
            sqlite3_finalize(statementObject)
        }
        
        let statement = "SELECT name FROM sura_names;";
        
        do {
            try compile(statement, into: &statementObject)
            
            var rows: [String] = []
            
            while (sqlite3_step(statementObject) == SQLITE_ROW) {
                let columnTextPointer = sqlite3_column_text(statementObject, 0)
                let columnText = String(cString: columnTextPointer!)
                rows.append(columnText)
            }
            
            if (rows.isEmpty) {
                throw QuranDatabaseError.FailedExecutingQuery(
                    "No rows returned in query",
                    underlyingError: nil
                )
            }
            
            return rows
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
        var statementObject: OpaquePointer? = nil
        
        defer {
            sqlite3_finalize(statementObject)
        }
        
        let statement = "SELECT name FROM sura_names WHERE sura=\(surahNumber);";
        
        do {
            try compile(statement, into: &statementObject)
            
            let stepResult = sqlite3_step(statementObject)
            
            if (stepResult == SQLITE_ROW) {
                let columnTextPointer = sqlite3_column_text(statementObject, 0)
                let columnText = String(cString: columnTextPointer!)
                return columnText
            } else {
                throw QuranDatabaseError.FailedExecutingQuery(
                    "No rows returned in query. Step result was \(stepResult)",
                    underlyingError: nil
                )
            }
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
        var statementObject: OpaquePointer? = nil

        defer {
            sqlite3_finalize(statementObject)
        }

        let statement = "SELECT text FROM quran_text WHERE sura=\(surahNumber);";

        do {
            try compile(statement, into: &statementObject)

            var rows: [String] = []

            while (sqlite3_step(statementObject) == SQLITE_ROW) {
                let columnTextPointer = sqlite3_column_text(statementObject, 0)
                let columnText = String(cString: columnTextPointer!)
                rows.append(columnText)
            }

            if (rows.isEmpty) {
                throw QuranDatabaseError.FailedExecutingQuery(
                    "No rows returned in query for Surah \(surahNumber)",
                    underlyingError: nil
                )
            }
            
            return rows
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
        var statementObject: OpaquePointer? = nil

        defer {
            sqlite3_finalize(statementObject)
        }

        let statement = "SELECT text FROM quran_text WHERE sura=\(surahNumber) AND aya=\(ayahNumber);";

        do {
            try compile(statement, into: &statementObject)

            let stepResult = sqlite3_step(statementObject)

            if (stepResult == SQLITE_ROW) {
                let columnTextPointer = sqlite3_column_text(statementObject, 0)
                let columnText = String(cString: columnTextPointer!)
                return columnText
            } else {
                throw QuranDatabaseError.FailedExecutingQuery(
                        "No rows returned in query. Step result was \(stepResult)",
                        underlyingError: nil
                )
            }
        } catch {
            throw QuranDatabaseError.FailedExecutingQuery(
                    "Failed getting Ayah for Surah \(surahNumber), Ayah \(ayahNumber)",
                    underlyingError: error
            )
        }
    }
    
    /**
     * Gets the metadata for the chapters of the specified chapter type.
     *
     * - Parameter chapterType: The chapter type for which to get metadata.
     * - Returns: The metadata for the chapters of the specified chapter type.
     * - Throws: `QuranDatabaseError.FailedExecutingQuery` if there was an error getting the metadata from the database.
     */
    public func getMetadataForChapterType(_ chapterType: ChapterType) throws -> [ChapterMetadata] {
        var statementObject: OpaquePointer? = nil
        
        defer {
            sqlite3_finalize(statementObject)
        }
        
        let statement = "SELECT chapter_type, chapter_number, aya_count, sura, aya FROM quran_metadata WHERE chapter_type='\(chapterType.rawValue)';";
        
        do {
            try compile(statement, into: &statementObject)
            
            var rows: [ChapterMetadata] = []
            
            while (sqlite3_step(statementObject) == SQLITE_ROW) {
                let chapterTypePointer = sqlite3_column_text(statementObject, 0)
                let chapterType = String(cString: chapterTypePointer!)
                
                let chapterNumber = sqlite3_column_int(statementObject, 1)
                let ayahCount = sqlite3_column_int(statementObject, 2)
                let surahNumber = sqlite3_column_int(statementObject, 3)
                let ayahNumber = sqlite3_column_int(statementObject, 4)
                
                let chapterMetadata = ChapterMetadata(
                    chapterType: ChapterType(rawValue: chapterType)!,
                    chapterNumber: Int(chapterNumber),
                    numAyahs: Int(ayahCount),
                    surahNumber: Int(surahNumber),
                    ayahNumber: Int(ayahNumber)
                )
                
                rows.append(chapterMetadata)
            }
            
            if (rows.isEmpty) {
                throw QuranDatabaseError.FailedExecutingQuery(
                    "No rows returned in query for chapter type = \(chapterType)",
                    underlyingError: nil
                )
            }
            
            return rows
        } catch {
            throw QuranDatabaseError.FailedExecutingQuery(
                "Failed getting metadata for chapter type = \(chapterType)",
                underlyingError: error
            )
        }
    }
    
    /**
     * Gets the metadata for the specified chapter.
     *
     * - Parameter chapterType: The chapter type for which to get metadata.
     * - Parameter chapterNumber: The number of the chapter within the given chapter type.
     * - Returns: The metadata for the specified chapter.
     * - Throws: `QuranDatabaseError.FailedExecutingQuery` if there was an error getting the metadata from the database.
     */
    public func getMetadataForChapter(chapterType: ChapterType, chapterNumber: Int) throws -> ChapterMetadata {
        var statementObject: OpaquePointer? = nil
        
        defer {
            sqlite3_finalize(statementObject)
        }
        
        let statement = "SELECT aya_count, sura, aya FROM quran_metadata WHERE chapter_type='\(chapterType.rawValue)' AND chapter_number=\(chapterNumber) LIMIT 1;";
        
        do {
            try compile(statement, into: &statementObject)
            
            let stepResult = sqlite3_step(statementObject)
            
            if (stepResult == SQLITE_ROW) {
                let ayahCount = sqlite3_column_int(statementObject, 0)
                let surahNumber = sqlite3_column_int(statementObject, 1)
                let ayahNumber = sqlite3_column_int(statementObject, 2)
                
                return ChapterMetadata(
                    chapterType: chapterType,
                    chapterNumber: chapterNumber,
                    numAyahs: Int(ayahCount),
                    surahNumber: Int(surahNumber),
                    ayahNumber: Int(ayahNumber)
                )
            } else {
                throw QuranDatabaseError.FailedExecutingQuery(
                    "No rows returned in query. Step result was \(stepResult)",
                    underlyingError: nil
                )
            }
        } catch {
            throw QuranDatabaseError.FailedExecutingQuery(
                "Failed getting chapter metadata for chapter type = \(chapterType), chapter number = \(chapterNumber)",
                underlyingError: error
            )
        }
    }

    /**
     * Determines whether the database file exists in internal storage.
     *
     * (Internal visibility for unit testing purposes.)
     *
     * - Returns: true if the database file exists in internal storage, and false otherwise.
     */
    internal func isDatabaseExistsInInternalStorage() throws -> Bool {
        let path = try getURLForQuranDatabaseInInternalStorage().path
        return FileManager.default.fileExists(atPath: path)
    }

    /**
     * Determines whether the database is open for reading.
     *
     * (Internal visibility for unit testing purposes.)
     *
     * - Returns: true if the Quran database is open for reading, and false otherwise.
     */
    internal func isDatabaseOpen() -> Bool {
        return database != nil;
    }

    /**
     * Deletes the database file from internal storage.
     *
     * (Internal visibility for unit testing purposes.)
     */
    internal func deleteDatabaseInInternalStorage() throws {
        do {
            let fileManager = FileManager.default
            let path = try getURLForQuranDatabaseInInternalStorage().path

            if fileManager.fileExists(atPath: path) {
                try fileManager.removeItem(atPath: path)
            }
        } catch {
            throw QuranDatabaseError.FailedDeletingDatabase(underlyingError: error)
        }
    }

    /**
     * Prepares a query on the Quran database.
     *
     * - Parameter statement: The SQL statement to compile.
     * - Parameter statementObjectPointer: A pointer to the statement object into which the SQL statement will be compiled.
     */
    private func compile(_ statement: String, into statementObjectPointer: UnsafeMutablePointer<OpaquePointer?>) throws {
        if (!isDatabaseOpen()) {
            try openDatabase()
        }

        let resultCode = sqlite3_prepare_v2(database, statement, -1, statementObjectPointer, nil)

        if (resultCode != SQLITE_OK) {
            throw QuranDatabaseError.FailedCompilingQuery("SQLite result code = \(resultCode)")
        }
    }

    private func copyDatabaseFromFrameworkBundleToInternalStorage() throws {
        let storageURL = try getURLForQuranDatabaseInInternalStorage()

        guard let bundleURL = getURLForQuranDatabaseInFrameworkBundle() else {
            throw QuranDatabaseError.FailedLocatingQuranDatabaseInFrameworkBundle
        }

        try FileManager.default.copyItem(at: bundleURL, to: storageURL)
    }

    private func getURLForQuranDatabaseInInternalStorage() throws -> URL {
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

    case FailedDeletingDatabase(underlyingError: Error?)
    case FailedOpeningDatabase(_ message: String, underlyingError: Error?)
    case FailedCompilingQuery(_ message: String)
    case FailedExecutingQuery(_ message: String, underlyingError: Error?)
    case FailedClosingDatabase(_ message: String)
    case FailedLocatingQuranDatabaseInFrameworkBundle

}
