//
//  QuranDatabaseCleanupTests.swift
//  QuranSDKTests
//
//  Created by Adil Hussain on 13/11/2025.
//  Copyright © 2025 Tazkiya Tech. All rights reserved.
//

import Foundation
import Testing
@testable import QuranSDK

@Suite(.serialized)
struct QuranDatabaseCleanupTests {
    
    @Test
    func deleteLegacyDatabaseFiles_when_files_exist() async throws {
        // Given.
        let dbV1URL = try urlForDocument(named: "com.tazkiyatech.quran.db")
        let dbV2URL = try urlForDocument(named: "com.tazkiyatech.quran.v2.db")
        
        try Data("test1".utf8).write(to: dbV1URL)
        try Data("test2".utf8).write(to: dbV2URL)

        let fm = FileManager.default
        
        #expect(fm.fileExists(atPath: dbV1URL.path))
        #expect(fm.fileExists(atPath: dbV2URL.path))

        // When.
        await deleteLegacyDatabaseFiles()

        // Then.
        #expect(!fm.fileExists(atPath: dbV1URL.path))
        #expect(!fm.fileExists(atPath: dbV2URL.path))
    }
    
    @Test
    func deleteLegacyDatabaseFiles_when_files_do_not_exist() async throws {
        // Given.
        let dbV1Path = try urlForDocument(named: "com.tazkiyatech.quran.db").path
        let dbV2Path = try urlForDocument(named: "com.tazkiyatech.quran.v2.db").path
        
        let fm = FileManager.default
        
        if fm.fileExists(atPath: dbV1Path) { try fm.removeItem(atPath: dbV1Path) }
        if fm.fileExists(atPath: dbV2Path) { try fm.removeItem(atPath: dbV2Path) }

        #expect(!fm.fileExists(atPath: dbV1Path))
        #expect(!fm.fileExists(atPath: dbV1Path))

        // When.
        await deleteLegacyDatabaseFiles()

        // Then.
        #expect(!fm.fileExists(atPath: dbV1Path))
        #expect(!fm.fileExists(atPath: dbV1Path))
    }
}
