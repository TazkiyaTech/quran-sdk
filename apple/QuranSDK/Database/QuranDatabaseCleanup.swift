//
//  QuranDatabaseCleanup.swift
//  QuranSDK
//
//  Created by Adil Hussain on 13/11/2025.
//  Copyright © 2025 Tazkiya Tech. All rights reserved.
//

import Foundation

/// Deletes legacy versions of the Quran database from the file system.
@concurrent func deleteLegacyDatabaseFiles() async {
    do {
        let databaseV1URL = try urlForDocument(named: "com.tazkiyatech.quran.db")
        let databaseV2URL = try urlForDocument(named: "com.tazkiyatech.quran.v2.db")
        
        try deleteFile(at: databaseV1URL)
        try deleteFile(at: databaseV2URL)
    } catch {
        // swallow the error as this is just a cleanup operation
    }
}

/// Creates a `URL` for the file with the given name within the Document directory.
///
/// Internal visibility for testing purposes.
///
/// - Returns: The location of the file with the given name within the Document directory.
func urlForDocument(named filename: String) throws -> URL {
    let baseURL = try FileManager.default.url(
        for: .documentDirectory,
        in: .userDomainMask,
        appropriateFor: nil,
        create: true
    )
    
    if #available(iOS 16.0, macCatalyst 16.0, macOS 13.0, tvOS 16.0, watchOS 9.0, *) {
        return baseURL.appending(path: filename, directoryHint: .notDirectory)
    } else {
        return baseURL.appendingPathComponent(filename)
    }
}

/// Deletes the file at the given location if it exists.
/// Does nothing if the file does not exist.
private func deleteFile(at url: URL) throws {
    let fileManager = FileManager.default
    let path = url.path
    
    if fileManager.fileExists(atPath: path) {
        try fileManager.removeItem(atPath: path)
    }
}
