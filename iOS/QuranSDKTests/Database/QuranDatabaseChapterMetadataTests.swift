//
//  QuranDatabaseChapterMetadataTests.swift
//  QuranSDKTests
//
//  Created by Adil on 13/06/2019.
//  Copyright © 2019 Tazkiya Tech. All rights reserved.
//

import XCTest
@testable import QuranSDK

class QuranDatabaseChapterMetadataTests: XCTestCase {
    
    private var quranDatabase: QuranDatabase!
    
    override func setUp() {
        super.setUp()
        
        quranDatabase = QuranDatabase()
        
        do {
            try quranDatabase.deleteDatabaseInInternalStorage()
        } catch {
            XCTFail("Failed deleting the database file in the test setup: \(error)")
        }
    }
    
    override func tearDown() {
        try? quranDatabase.closeDatabase()
    }
    
    func test_getMetadataForChapter_with_chapter_type_surah_and_chapter_number_1() throws {
        // When.
        let chapterMetadata = try quranDatabase.getMetadataForChapter(
            chapterType: .surah,
            chapterNumber: 1
        )
        
        // Then.
        XCTAssertEqual(.surah, chapterMetadata.chapterType)
        XCTAssertEqual(1, chapterMetadata.chapterNumber)
        XCTAssertEqual(7, chapterMetadata.numAyahs)
        XCTAssertEqual(1, chapterMetadata.surahNumber)
        XCTAssertEqual(1, chapterMetadata.ayahNumber)
    }
    
    func test_getMetadataForChapterType_with_chapter_type_surah() throws {
        // Given.
        let chapterType = ChapterType.surah
        
        // When.
        let chapterMetadataList = try quranDatabase.getMetadataForChapterType(chapterType)
        
        // Then.
        XCTAssertEqual(114, chapterMetadataList.count)
    }
    
    func test_getMetadataForChapterType_with_chapter_type_juz() throws {
        // Given.
        let chapterType = ChapterType.juz
        
        // When.
        let chapterMetadataList = try quranDatabase.getMetadataForChapterType(chapterType)
        
        // Then.
        XCTAssertEqual(30, chapterMetadataList.count)
    }
    
    func test_getMetadataForChapterType_with_chapter_type_hizb() throws {
        // Given.
        let chapterType = ChapterType.hizb
        
        // When.
        let chapterMetadataList = try quranDatabase.getMetadataForChapterType(chapterType)
        
        // Then.
        XCTAssertEqual(60, chapterMetadataList.count)
    }
    
    func test_getMetadataForChapterType_with_chapter_type_hizb_quarter() throws {
        // Given.
        let chapterType = ChapterType.hizbQuarter
        
        // When.
        let chapterMetadataList = try quranDatabase.getMetadataForChapterType(chapterType)
        
        // Then.
        XCTAssertEqual(240, chapterMetadataList.count)
    }
}
