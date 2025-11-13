//
//  QuranDatabase.swift
//  QuranSDK
//
//  Created by Adil Hussain on 23/11/2018.
//  Copyright © 2018 Tazkiya Tech. All rights reserved.
//

import Foundation
import SQLite3

/**
 * A wrapper around the SQLite Quran database.
 * Provides easy methods for accessing the contents of the database.
 *
 * It is safe to call the functions in this class on a single instance or on multiple instances
 * in separate threads concurrently.
 *
 */
public class QuranDatabase: @unchecked Sendable {
    
    private static let sharedLock = NSLock()
    
    private var database: OpaquePointer? = nil
    
    /**
     * Opens the Quran database for reading if it's not already open.
     * Does nothing if the database is already open for reading.
     *
     * Calling this function is optional.
     * The "get..." functions on this class take care of opening the database if it not already open.
     * Call this function if you wish to open the database ahead of time
     * before the first "get..." function call in your application.
     *
     * - Throws: `QuranDatabaseError` if the database could not be opened.
     */
    public func openDatabase() throws(QuranDatabaseError) {
        
        if (isDatabaseOpen()) {
            return
        }
        
        let internalStorageURL: URL
        
        do {
            internalStorageURL = try QuranDatabase.copyDatabaseToInternalStorageIfMissing()
        } catch {
            throw QuranDatabaseError(
                message: "Failed opening database. Failed copying database from framework bundle to internal storage.",
                underlyingError: error,
            )
        }
        
        let resultCode = sqlite3_open_v2(internalStorageURL.path, &database, SQLITE_OPEN_READONLY|SQLITE_OPEN_FULLMUTEX, nil)
        
        if resultCode != SQLITE_OK {
            throw QuranDatabaseError(
                message: "Failed opening database. SQLite result code = \(resultCode).",
                underlyingError: nil,
            )
        }
        
        Task { await deleteLegacyDatabaseFiles() }
    }
    
    /**
     * Closes the Quran database.
     *
     * - Throws: `QuranDatabaseError` if the database could not be closed.
     */
    public func closeDatabase() throws(QuranDatabaseError) {
        
        Self.sharedLock.lock()
        
        defer { Self.sharedLock.unlock() }
        
        if (!isDatabaseOpen()) {
            return
        }
        
        let resultCode = sqlite3_close(database)
        
        if (resultCode != SQLITE_OK) {
            throw QuranDatabaseError(
                message: "Failed closing database. SQLite result code = \(resultCode).",
            )
        }
        
        database = nil
    }
    
    /**
     * Gets the names of all of the Surahs in the Quran.
     *
     * - Returns: the names of all of the Surahs in the Quran.
     * - Throws: `QuranDatabaseError` if there was an error getting the Surah names from the database.
     */
    public func getSurahNames() throws(QuranDatabaseError) -> [String] {
        var compiledStatement: OpaquePointer? = nil
        
        defer {
            sqlite3_finalize(compiledStatement)
        }
        
        let statement = "SELECT name FROM sura_names"
        
        do {
            try compile(statement, into: &compiledStatement)
            
            var rows: [String] = []
            
            while (sqlite3_step(compiledStatement) == SQLITE_ROW) {
                let columnTextPointer = sqlite3_column_text(compiledStatement, 0)
                let columnText = String(cString: columnTextPointer!)
                rows.append(columnText)
            }
            
            if (rows.isEmpty) {
                throw QuranDatabaseError(message: "No rows returned in query")
            }
            
            return rows
        } catch {
            throw QuranDatabaseError(
                message: "Failed executing query. Failed getting Surah names.",
                underlyingError: error
            )
        }
    }
    
    /**
     * Gets the name of the specified Surah.
     *
     * - Parameter surahNumber: is a value between 1 and 114 (inclusive).
     * - Returns: the name of the specified Surah.
     * - Throws: `QuranDatabaseError` if there was an error getting the Surah name from the database.
     */
    public func getNameOfSurah(_ surahNumber: Int) throws(QuranDatabaseError) -> String {
        var compiledStatement: OpaquePointer? = nil
        
        defer {
            sqlite3_finalize(compiledStatement)
        }
        
        let statement = "SELECT name FROM sura_names WHERE sura=\(surahNumber)"
        
        do {
            try compile(statement, into: &compiledStatement)
            
            let stepResult = sqlite3_step(compiledStatement)
            
            if (stepResult == SQLITE_ROW) {
                let columnTextPointer = sqlite3_column_text(compiledStatement, 0)
                let columnText = String(cString: columnTextPointer!)
                return columnText
            } else {
                throw QuranDatabaseError(
                    message: "No rows returned in query. Step result was \(stepResult).",
                )
            }
        } catch {
            throw QuranDatabaseError(
                message: "Failed executing query. Failed getting Surah name for Surah \(surahNumber).",
                underlyingError: error,
            )
        }
    }
    
    /**
     * Gets all of the Ayahs in the specified Surah.
     *
     * - Parameter surahNumber: is a value between 1 and 114 (inclusive).
     * - Returns: the Ayahs of the specified Surah.
     * - Throws: `QuranDatabaseError` if there was an error getting the Ayahs from the database.
     */
    public func getAyahsInSurah(_ surahNumber: Int) throws(QuranDatabaseError) -> [String] {
        var compiledStatement: OpaquePointer? = nil
        
        defer {
            sqlite3_finalize(compiledStatement)
        }
        
        let statement = "SELECT text FROM quran_text WHERE sura=\(surahNumber)"
        
        do {
            try compile(statement, into: &compiledStatement)
            
            var rows: [String] = []
            
            while (sqlite3_step(compiledStatement) == SQLITE_ROW) {
                let columnTextPointer = sqlite3_column_text(compiledStatement, 0)
                let columnText = String(cString: columnTextPointer!)
                rows.append(columnText)
            }
            
            if (rows.isEmpty) {
                throw QuranDatabaseError(
                    message: "No rows returned in query for Surah \(surahNumber).",
                )
            }
            
            return rows
        } catch {
            throw QuranDatabaseError(
                message: "Failed executing query. Failed getting Ayahs for Surah \(surahNumber).",
                underlyingError: error,
            )
        }
    }
    
    /**
     * Gets the text of the specified Ayah.
     *
     * - Parameter surahNumber: is a value between 1 and 114 (inclusive).
     * - Parameter ayahNumber: is a value greater than or equal to 1.
     * - Returns: the text of the specified Ayah.
     * - Throws: `QuranDatabaseError` if there was an error getting the Ayah from the database.
     */
    public func getAyah(
        surahNumber: Int,
        ayahNumber: Int,
    ) throws(QuranDatabaseError) -> String {
        var compiledStatement: OpaquePointer? = nil
        
        defer {
            sqlite3_finalize(compiledStatement)
        }
        
        let statement = "SELECT text FROM quran_text WHERE sura=\(surahNumber) AND aya=\(ayahNumber)"
        
        do {
            try compile(statement, into: &compiledStatement)
            
            let stepResult = sqlite3_step(compiledStatement)
            
            if (stepResult == SQLITE_ROW) {
                let columnTextPointer = sqlite3_column_text(compiledStatement, 0)
                let columnText = String(cString: columnTextPointer!)
                return columnText
            } else {
                throw QuranDatabaseError(
                    message: "No rows returned in query. Step result was \(stepResult).",
                )
            }
        } catch {
            throw QuranDatabaseError(
                message: "Failed executing query. Failed getting Ayah for Surah \(surahNumber), Ayah \(ayahNumber).",
                underlyingError: error,
            )
        }
    }
    
    /**
     * Gets the metadata for the sections of the specified section type.
     *
     * - Parameter sectionType: The section type for which to get metadata.
     * - Returns: The metadata for the sections of the specified section type.
     * - Throws: `QuranDatabaseError` if there was an error getting the metadata from the database.
     */
    public func getMetadataForSections(
        ofType sectionType: SectionType
    ) throws(QuranDatabaseError) -> [SectionMetadata] {
        var compiledStatement: OpaquePointer? = nil
        
        defer {
            sqlite3_finalize(compiledStatement)
        }
        
        let statement = "SELECT section_type, section_number, aya_count, sura, aya FROM quran_metadata WHERE section_type='\(sectionType.rawValue)'"
        
        do {
            try compile(statement, into: &compiledStatement)
            
            var rows: [SectionMetadata] = []
            
            while (sqlite3_step(compiledStatement) == SQLITE_ROW) {
                let sectionTypePointer = sqlite3_column_text(compiledStatement, 0)
                let sectionType = String(cString: sectionTypePointer!)
                
                let sectionNumber = sqlite3_column_int(compiledStatement, 1)
                let ayahCount = sqlite3_column_int(compiledStatement, 2)
                let surahNumber = sqlite3_column_int(compiledStatement, 3)
                let ayahNumber = sqlite3_column_int(compiledStatement, 4)
                
                let sectionMetadata = SectionMetadata(
                    sectionType: SectionType(rawValue: sectionType)!,
                    sectionNumber: Int(sectionNumber),
                    numAyahs: Int(ayahCount),
                    surahNumber: Int(surahNumber),
                    ayahNumber: Int(ayahNumber)
                )
                
                rows.append(sectionMetadata)
            }
            
            if (rows.isEmpty) {
                throw QuranDatabaseError(
                    message: "No rows returned in query for section type = \(sectionType)",
                )
            }
            
            return rows
        } catch {
            throw QuranDatabaseError(
                message: "Failed executing query. Failed getting metadata for section type = \(sectionType).",
                underlyingError: error
            )
        }
    }
    
    /**
     * Gets the metadata for the specified section.
     *
     * - Parameter sectionType: The section type for which to get metadata.
     * - Parameter sectionNumber: The number of the section within the given section type.
     * - Returns: The metadata for the specified section.
     * - Throws: `QuranDatabaseError` if there was an error getting the metadata from the database.
     */
    public func getMetadataForSection(
        sectionType: SectionType,
        sectionNumber: Int
    ) throws(QuranDatabaseError) -> SectionMetadata {
        var compiledStatement: OpaquePointer? = nil
        
        defer {
            sqlite3_finalize(compiledStatement)
        }
        
        let statement = "SELECT aya_count, sura, aya FROM quran_metadata WHERE section_type='\(sectionType.rawValue)' AND section_number=\(sectionNumber) LIMIT 1"
        
        do {
            try compile(statement, into: &compiledStatement)
            
            let stepResult = sqlite3_step(compiledStatement)
            
            if (stepResult == SQLITE_ROW) {
                let ayahCount = sqlite3_column_int(compiledStatement, 0)
                let surahNumber = sqlite3_column_int(compiledStatement, 1)
                let ayahNumber = sqlite3_column_int(compiledStatement, 2)
                
                return SectionMetadata(
                    sectionType: sectionType,
                    sectionNumber: sectionNumber,
                    numAyahs: Int(ayahCount),
                    surahNumber: Int(surahNumber),
                    ayahNumber: Int(ayahNumber)
                )
            } else {
                throw QuranDatabaseError(
                    message: "No rows returned in query. Step result was \(stepResult).",
                )
            }
        } catch {
            throw QuranDatabaseError(
                message: "Failed executing query. Failed getting section metadata for section type = \(sectionType), section number = \(sectionNumber).",
                underlyingError: error,
            )
        }
    }
    
    /**
     * Determines whether the database file exists in internal storage.
     *
     * This function exists for testing purposes only.
     *
     * - Returns: true if the database file exists in internal storage, and false otherwise.
     */
    internal static func isDatabaseExistsInInternalStorage() throws -> Bool {
        let path = try getURLForQuranDatabaseInInternalStorage().path
        return fileExists(atPath: path)
    }
    
    /**
     * Determines whether the database is open for reading.
     *
     * (Internal visibility for testing purposes.)
     *
     * - Returns: true if the Quran database is open for reading, and false otherwise.
     */
    internal func isDatabaseOpen() -> Bool {
        return database != nil
    }
    
    /**
     * Deletes the database file from internal storage.
     *
     * This function exists for testing purposes only.
     *
     * - Throws: `QuranDatabaseError` if there was an error deleting the database from internal storage.
     */
    internal static func deleteDatabaseInInternalStorage() throws {
        
        do {
            let fileManager = FileManager.default
            let path = try getURLForQuranDatabaseInInternalStorage().path
            
            if fileManager.fileExists(atPath: path) {
                try fileManager.removeItem(atPath: path)
            }
        } catch {
            throw QuranDatabaseError(
                message: "Failed deleting database.",
                underlyingError: error,
            )
        }
    }
    
    /**
     * Prepares a query on the Quran database.
     *
     * - Parameter statement: The SQL statement to compile.
     * - Parameter compiledStatementPointer: A pointer to the object into which the SQL statement will be compiled.
     */
    private func compile(_ statement: String, into compiledStatementPointer: UnsafeMutablePointer<OpaquePointer?>) throws {
        try openDatabase()
        
        let resultCode = sqlite3_prepare_v2(database, statement, -1, compiledStatementPointer, nil)
        
        if (resultCode != SQLITE_OK) {
            throw QuranDatabaseError(
                message: "Failed compiling query. SQLite result code = \(resultCode)."
            )
        }
    }
    
    /// - Returns: The location of the database in internal storage.
    private static func copyDatabaseToInternalStorageIfMissing() throws -> URL {
        
        Self.sharedLock.lock()
        
        defer { Self.sharedLock.unlock() }
        
        let internalStorageURL = try getURLForQuranDatabaseInInternalStorage()
        let databaseExistsInInternalStorage = fileExists(atPath: internalStorageURL.path)
        
        if (!databaseExistsInInternalStorage) {
            let bundleURL = try getURLForQuranDatabaseInFrameworkBundle()
            try FileManager.default.copyItem(at: bundleURL, to: internalStorageURL)
        }
        
        return internalStorageURL
    }
    
    /**
     * - Returns: `true` if a file exists at the given path, and `false` otherwise.
     */
    private static func fileExists(atPath path: String) -> Bool {
        return FileManager.default.fileExists(atPath: path)
    }
    
    /// - Returns: The location of the database file in internal storage.
    private static func getURLForQuranDatabaseInInternalStorage() throws -> URL {
        let baseURL = try FileManager.default.url(
            for: .applicationSupportDirectory,
            in: .userDomainMask,
            appropriateFor: nil,
            create: true
        )
        
        if #available(iOS 16.0, macCatalyst 16.0, macOS 13.0, tvOS 16.0, watchOS 9.0, *) {
            return baseURL.appending(path: "com.tazkiyatech.quran.v2.db", directoryHint: .notDirectory)
        } else {
            return baseURL.appendingPathComponent("com.tazkiyatech.quran.v2.db")
        }
    }
    
    /// - Returns: The location of the database file in the framework bundle.
    private static func getURLForQuranDatabaseInFrameworkBundle() throws -> URL {
#if SWIFT_PACKAGE
        let bundle = Bundle.module
#else
        let bundle = Bundle(for: Self.self)
#endif
        
        guard let url = bundle.url(forResource: "com.tazkiyatech.quran.v2", withExtension: "db") else {
            throw QuranDatabaseError(
                message: "Failed locating Quran Database in framework bundle."
            )
        }
        
        return url
    }
}
