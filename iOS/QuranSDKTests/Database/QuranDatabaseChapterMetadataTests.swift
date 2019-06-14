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
        continueAfterFailure = false
        
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
    
    func test_getMetadataForChapter_with_chapter_type_surah_and_chapter_number_0() throws {
        // When.
        XCTAssertThrowsError(try quranDatabase.getMetadataForChapter(chapterType: .surah, chapterNumber: 0))
    }
    
    func test_getMetadataForChapter_with_chapter_type_surah_and_chapter_number_115() throws {
        // When.
        XCTAssertThrowsError(try quranDatabase.getMetadataForChapter(chapterType: .surah, chapterNumber: 115))
    }
    
    func test_getMetadataForChapterType_with_chapter_type_surah() throws {
        // Given.
        let chapterType = ChapterType.surah
        
        // When.
        let chapterMetadataArray = try quranDatabase.getMetadataForChapterType(chapterType)
        
        // Then.
        XCTAssertEqual(114, chapterMetadataArray.count)
        
        // And.
        assert(eachItemIn: chapterMetadataArray, hasChapterType: chapterType)
        assertChapterNumbersInChapterMetadataArray(chapterMetadataArray)
        assertNumberOfAyahsInChapterMetadataArray(chapterMetadataArray)
    }
    
    func test_getMetadataForChapterType_with_chapter_type_juz() throws {
        // Given.
        let chapterType = ChapterType.juz
        
        // When.
        let chapterMetadataArray = try quranDatabase.getMetadataForChapterType(chapterType)
        
        // Then.
        XCTAssertEqual(30, chapterMetadataArray.count)
        
        // And.
        assert(eachItemIn: chapterMetadataArray, hasChapterType: chapterType)
        assertChapterNumbersInChapterMetadataArray(chapterMetadataArray)
        assertNumberOfAyahsInChapterMetadataArray(chapterMetadataArray)
    }
    
    func test_getMetadataForChapterType_with_chapter_type_hizb() throws {
        // Given.
        let chapterType = ChapterType.hizb
        
        // When.
        let chapterMetadataArray = try quranDatabase.getMetadataForChapterType(chapterType)
        
        // Then.
        XCTAssertEqual(60, chapterMetadataArray.count)
        
        // And.
        assert(eachItemIn: chapterMetadataArray, hasChapterType: chapterType)
        assertChapterNumbersInChapterMetadataArray(chapterMetadataArray)
        assertNumberOfAyahsInChapterMetadataArray(chapterMetadataArray)
    }
    
    func test_getMetadataForChapterType_with_chapter_type_hizb_quarter() throws {
        // Given.
        let chapterType = ChapterType.hizbQuarter
        
        // When.
        let chapterMetadataArray = try quranDatabase.getMetadataForChapterType(chapterType)
        
        // Then.
        XCTAssertEqual(240, chapterMetadataArray.count)
        
        // And.
        assert(eachItemIn: chapterMetadataArray, hasChapterType: chapterType)
        assertChapterNumbersInChapterMetadataArray(chapterMetadataArray)
        assertNumberOfAyahsInChapterMetadataArray(chapterMetadataArray)
    }
    
    func test_number_of_verses_in_each_hizb_matches_the_number_of_verses_in_each_hizb_quarter() throws {
        let hizbMetadataArray = try quranDatabase.getMetadataForChapterType(.hizb)
        
        let hizbQuarterMetadataArray = try quranDatabase.getMetadataForChapterType(.hizbQuarter)
        
        for i in hizbMetadataArray.indices {
            let expected = hizbMetadataArray[i].numAyahs
            
            let actual = (hizbQuarterMetadataArray[i * 4].numAyahs
                + hizbQuarterMetadataArray[i * 4 + 1].numAyahs
                + hizbQuarterMetadataArray[i * 4 + 2].numAyahs
                + hizbQuarterMetadataArray[i * 4 + 3].numAyahs)
            
            XCTAssertEqual(expected, actual, "HIZB \(i + 1)")
        }
    }
    
    func test_number_of_verses_in_each_juz_matches_the_number_of_verses_in_each_hizb() throws {
        let juzMetadataArray = try quranDatabase.getMetadataForChapterType(.juz)
        let hizbMetadataArray = try quranDatabase.getMetadataForChapterType(.hizb)
        
        for i in juzMetadataArray.indices {
            let expected = juzMetadataArray[i].numAyahs
            
            let actual = hizbMetadataArray[i * 2].numAyahs + hizbMetadataArray[i * 2 + 1].numAyahs
            
            XCTAssertEqual(expected, actual, "JUZ ${i + 1}")
        }
    }
    
    func test_surahNumber_and_ayahNumber_in_each_surah_is_as_expected() throws {
        let surahMetadataArray = try quranDatabase.getMetadataForChapterType(.surah)
        
        for i in surahMetadataArray.indices {
            let chapterMetadata = surahMetadataArray[i]
            
            XCTAssertEqual((i + 1), chapterMetadata.surahNumber)
            XCTAssertEqual(1, chapterMetadata.ayahNumber)
        }
    }
    
    func test_surahNumber_and_ayahNumber_in_each_juz_is_as_expected() throws {
        try assertSurahAndVerseNumberOfFirstVerseInEachChapterOfType(.juz)
    }
    
    func test_surahNumber_and_ayahNumber_in_each_hizb_is_as_expected() throws {
        try assertSurahAndVerseNumberOfFirstVerseInEachChapterOfType(.hizb)
    }
    
    func test_surahNumber_and_ayahNumber_in_each_hizb_quarter_is_as_expected() throws {
        try assertSurahAndVerseNumberOfFirstVerseInEachChapterOfType(.hizbQuarter)
    }
    
    private func assert(eachItemIn chapterMetadataArray: Array<ChapterMetadata>,
                        hasChapterType chapterType: ChapterType) {
        chapterMetadataArray.forEach { (chapterMetadata) in
            XCTAssertEqual(chapterType, chapterMetadata.chapterType)
        }
    }
    
    private func assertChapterNumbersInChapterMetadataArray(_ chapterMetadataArray: Array<ChapterMetadata>) {
        for i in chapterMetadataArray.indices {
            XCTAssertEqual((i + 1), chapterMetadataArray[i].chapterNumber)
        }
    }
    
    private func assertNumberOfAyahsInChapterMetadataArray(_ chapterMetadataArray: Array<ChapterMetadata>) {
        var count = 0
    
        for chapterMetadata in chapterMetadataArray {
            count += chapterMetadata.numAyahs
        }

        XCTAssertEqual(6236, count)
    }
    
    private func assertSurahAndVerseNumberOfFirstVerseInEachChapterOfType(_ chapterType: ChapterType) throws {
        let chapterMetadataArray = try quranDatabase.getMetadataForChapterType(chapterType)
        
        for i in chapterMetadataArray.indices {
            let surahNumberA = chapterMetadataArray[i].surahNumber
            let verseNumberA = chapterMetadataArray[i].ayahNumber
            
            let surahNumberB: Int
            let verseNumberB: Int
            
            if (i < chapterMetadataArray.count - 1) {
                surahNumberB = chapterMetadataArray[i + 1].surahNumber
                verseNumberB = chapterMetadataArray[i + 1].ayahNumber
            } else {
                surahNumberB = 115
                verseNumberB = 1
            }
            
            let count = try getNumberOfVersesInBetween(
                surahNumberA: surahNumberA,
                verseNumberA: verseNumberA,
                surahNumberB: surahNumberB,
                verseNumberB: verseNumberB
            )
            
            XCTAssertEqual(
                count,
                chapterMetadataArray[i].numAyahs,
                "Unexpected number of ayahs between \(chapterType) \(i + 1) and the next one."
            )
        }
    }
    
    /**
     * - Returns: The number of verses between location A and location B,
     * or -1 if location B is not greater than location A.
     */
    private func getNumberOfVersesInBetween(surahNumberA: Int,
                                            verseNumberA: Int,
                                            surahNumberB: Int,
                                            verseNumberB: Int) throws -> Int {
        if (surahNumberB < surahNumberA) {
            return -1
        } else if (surahNumberB == surahNumberA && verseNumberB <= verseNumberA) {
            return -1
        } else if (surahNumberB == surahNumberA) {
            return verseNumberB - verseNumberA
        } else {
            var count = 0
        
            let surahAMetadata = try quranDatabase.getMetadataForChapter(
                chapterType: .surah,
                chapterNumber: surahNumberA
            )
            
            count += surahAMetadata.numAyahs + 1 - verseNumberA
        
            // add to count the number of verses in each Surah between A and B (exclusive of A and B)
            for i in ((surahNumberA + 1)..<surahNumberB) {
                let currentSurahMetadata = try quranDatabase.getMetadataForChapter(
                    chapterType: .surah,
                    chapterNumber: i
                )
                count += currentSurahMetadata.numAyahs
            }
            
            count += verseNumberB - 1
            
            return count
        }
    }
}
