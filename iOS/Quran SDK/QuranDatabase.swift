//
//  QuranDatabase.swift
//  Quran SDK
//
//  Created by Adil Hussain on 23/11/2018.
//  Copyright © 2018 Tazkiya Tech. All rights reserved.
//

import SQLite3

class QuranDatabase: NSObject {

    var db: OpaquePointer? = nil

    func openDatabase() throws {

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
            throw QuranDatabaseError.OpenDatabase(
                    message: "Failed created document directory URL",
                    underlyingError: error
            )
        }

        var resultCode = sqlite3_open_v2(documentsURL.path, &db, SQLITE_OPEN_READWRITE, nil)

        if resultCode == SQLITE_CANTOPEN {
            let bundleURL = Bundle(for: type(of: self)).url(forResource: "com.tazkiyatech.quran", withExtension: "db")!
            do {
                try fileManager.copyItem(at: bundleURL, to: documentsURL)
            } catch {
                throw QuranDatabaseError.OpenDatabase(
                        message: "Failed copying database from Bundle to Document directory URL",
                        underlyingError: error
                )
            }
            resultCode = sqlite3_open_v2(documentsURL.path, &db, SQLITE_OPEN_READWRITE, nil)
        }

        if resultCode != SQLITE_OK {
            throw QuranDatabaseError.OpenDatabase(
                    message: "Failed opening database: \(resultCode)",
                    underlyingError: nil
            )
        }
    }

    func closeDatabase() throws {
        if (db == nil) {
            return;
        }

        let resultCode = sqlite3_close(db)

        if (resultCode != SQLITE_OK) {
            throw QuranDatabaseError.CloseDatabase(message: "Failed closing database: \(resultCode)")
        }

        db = nil;
    }

    func getSurahNames() throws -> [String] {
        if (db == nil) {
            try openDatabase()
        }
        return try query("SELECT name FROM sura_names;")
    }

    func getSurahName(_ surahNumber: Int) throws -> String {
        if (db == nil) {
            try openDatabase()
        }
        return try query("SELECT name FROM sura_names WHERE sura=\(surahNumber);")[0]
    }

    func getAyahsInSurah(_ surahNumber: Int) throws -> [String] {
        if (db == nil) {
            try openDatabase()
        }
        return try query("SELECT text FROM quran_text WHERE sura=\(surahNumber);")
    }

    func getAyah(surahNumber: Int, ayahNumber: Int) throws -> String {
        if (db == nil) {
            try openDatabase()
        }
        return try query("SELECT text FROM quran_text WHERE sura=\(surahNumber) AND aya=\(ayahNumber);")[0]
    }

    func query(_ queryStatementString: String) throws -> [String] {
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
                throw QuranDatabaseError.PrepareQuery(message: message)
            } else {
                throw QuranDatabaseError.PrepareQuery(message: "No error message provided from sqlite.")
            }
        }

        sqlite3_finalize(queryStatement)

        return rows
    }
}

enum QuranDatabaseError: Error {

    case OpenDatabase(message: String, underlyingError: Error?)
    case PrepareQuery(message: String)
    case CloseDatabase(message: String)

}
