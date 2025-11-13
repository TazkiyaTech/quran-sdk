//
//  QuranDatabaseCleanup.swift
//  QuranSDK
//
//  Created by Adil Hussain on 13/11/2025.
//  Copyright © 2025 Tazkiya Tech. All rights reserved.
//

import Foundation

@concurrent func deleteLegacyDatabaseFiles() async {
    do {
        try deleteDatabaseFromDocumentDirectory()
    } catch {
        // swallow the error as this is just a cleanup operation
    }
}

/// Deletes the database file from the Document directory.
private func deleteDatabaseFromDocumentDirectory() throws {
    let fileManager = FileManager.default
    let path = try getURLForQuranDatabaseInDocumentDirectory().path
    
    if fileManager.fileExists(atPath: path) {
        try fileManager.removeItem(atPath: path)
    }
}

/// - Returns: The location of the database file in the Document directory.
private func getURLForQuranDatabaseInDocumentDirectory() throws -> URL {
    let baseURL = try FileManager.default.url(
        for: .documentDirectory,
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
