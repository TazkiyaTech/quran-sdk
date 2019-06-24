//
//  QuranDatabaseSectionMetadataTests.swift
//  QuranSDKTests
//
//  Created by Adil on 13/06/2019.
//  Copyright © 2019 Tazkiya Tech. All rights reserved.
//

import XCTest
@testable import QuranSDK

class QuranDatabaseSectionMetadataTests: XCTestCase {
    
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
    
    func test_getMetadataForSection_with_section_type_surah_and_section_number_1() throws {
        // When.
        let sectionMetadata = try quranDatabase.getMetadataForSection(
            sectionType: .surah,
            sectionNumber: 1
        )
        
        // Then.
        XCTAssertEqual(.surah, sectionMetadata.sectionType)
        XCTAssertEqual(1, sectionMetadata.sectionNumber)
        XCTAssertEqual(7, sectionMetadata.numAyahs)
        XCTAssertEqual(1, sectionMetadata.surahNumber)
        XCTAssertEqual(1, sectionMetadata.ayahNumber)
    }
    
    func test_getMetadataForSection_with_section_type_surah_and_section_number_0() throws {
        // When.
        XCTAssertThrowsError(try quranDatabase.getMetadataForSection(sectionType: .surah, sectionNumber: 0))
    }
    
    func test_getMetadataForSection_with_section_type_surah_and_section_number_115() throws {
        // When.
        XCTAssertThrowsError(try quranDatabase.getMetadataForSection(sectionType: .surah, sectionNumber: 115))
    }
    
    func test_getMetadataForSections_of_type_surah() throws {
        // Given.
        let sectionType = SectionType.surah
        
        // When.
        let sectionMetadataArray = try quranDatabase.getMetadataForSections(ofType: sectionType)
        
        // Then.
        XCTAssertEqual(114, sectionMetadataArray.count)
        
        // And.
        assert(eachItemIn: sectionMetadataArray, hasSectionType: sectionType)
        assertSectionNumbers(in: sectionMetadataArray)
        assertTotalNumberOfAyahs(in: sectionMetadataArray, isEqualTo: 6236)
    }
    
    func test_getMetadataForSections_of_type_juz_in_madinah_mushaf() throws {
        // Given.
        let sectionType = SectionType.juzInMadinahMushaf
        
        // When.
        let sectionMetadataArray = try quranDatabase.getMetadataForSections(ofType: sectionType)
        
        // Then.
        XCTAssertEqual(30, sectionMetadataArray.count)
        
        // And.
        assert(eachItemIn: sectionMetadataArray, hasSectionType: sectionType)
        assertSectionNumbers(in: sectionMetadataArray)
        assertTotalNumberOfAyahs(in: sectionMetadataArray, isEqualTo: 6236)
    }
    
    func test_getMetadataForSections_of_type_hizb() throws {
        // Given.
        let sectionType = SectionType.hizbInMadinahMushaf
        
        // When.
        let sectionMetadataArray = try quranDatabase.getMetadataForSections(ofType: sectionType)
        
        // Then.
        XCTAssertEqual(60, sectionMetadataArray.count)
        
        // And.
        assert(eachItemIn: sectionMetadataArray, hasSectionType: sectionType)
        assertSectionNumbers(in: sectionMetadataArray)
        assertTotalNumberOfAyahs(in: sectionMetadataArray, isEqualTo: 6236)
    }
    
    func test_getMetadataForSections_of_type_hizb_quarter() throws {
        // Given.
        let sectionType = SectionType.hizbQuarterInMadinahMushaf
        
        // When.
        let sectionMetadataArray = try quranDatabase.getMetadataForSections(ofType: sectionType)
        
        // Then.
        XCTAssertEqual(240, sectionMetadataArray.count)
        
        // And.
        assert(eachItemIn: sectionMetadataArray, hasSectionType: sectionType)
        assertSectionNumbers(in: sectionMetadataArray)
        assertTotalNumberOfAyahs(in: sectionMetadataArray, isEqualTo: 6236)
    }
    
    func test_getMetadataForSections_of_type_juz_in_majeedi_mushaf() throws {
        // Given.
        let sectionType = SectionType.juzInMajeediMushaf
        
        // When.
        let sectionMetadataArray = try quranDatabase.getMetadataForSections(ofType: sectionType)
        
        // Then.
        XCTAssertEqual(30, sectionMetadataArray.count)
        
        // And.
        assert(eachItemIn: sectionMetadataArray, hasSectionType: sectionType)
        assertSectionNumbers(in: sectionMetadataArray)
        assertTotalNumberOfAyahs(in: sectionMetadataArray, isEqualTo: (6236 - 7))
    }
    
    func test_getMetadataForSections_of_type_juz_quarter_in_majeedi_mushaf() throws {
        // Given.
        let sectionType = SectionType.juzQuarterInMajeediMushaf
        
        // When.
        let sectionMetadataArray = try quranDatabase.getMetadataForSections(ofType: sectionType)
        
        // Then.
        XCTAssertEqual(120, sectionMetadataArray.count)
        
        // And.
        assert(eachItemIn: sectionMetadataArray, hasSectionType: sectionType)
        assertSectionNumbers(in: sectionMetadataArray)
        assertTotalNumberOfAyahs(in: sectionMetadataArray, isEqualTo: (6236 - 7))
    }
    
    func test_number_of_verses_in_each_hizb_matches_the_number_of_verses_in_each_hizb_quarter() throws {
        let hizbMetadataArray = try quranDatabase.getMetadataForSections(ofType: .hizbInMadinahMushaf)
        
        let hizbQuarterMetadataArray = try quranDatabase.getMetadataForSections(ofType: .hizbQuarterInMadinahMushaf)
        
        for i in hizbMetadataArray.indices {
            let expected = hizbMetadataArray[i].numAyahs
            
            let actual = (hizbQuarterMetadataArray[i * 4].numAyahs
                + hizbQuarterMetadataArray[i * 4 + 1].numAyahs
                + hizbQuarterMetadataArray[i * 4 + 2].numAyahs
                + hizbQuarterMetadataArray[i * 4 + 3].numAyahs)
            
            XCTAssertEqual(expected, actual, "Failed comparison of Hizb \(i + 1)")
        }
    }
    
    func test_number_of_verses_in_each_juz_matches_the_number_of_verses_in_each_hizb() throws {
        let juzMetadataArray = try quranDatabase.getMetadataForSections(ofType: .juzInMadinahMushaf)
        let hizbMetadataArray = try quranDatabase.getMetadataForSections(ofType: .hizbInMadinahMushaf)
        
        for i in juzMetadataArray.indices {
            let expected = juzMetadataArray[i].numAyahs
            
            let actual = hizbMetadataArray[i * 2].numAyahs + hizbMetadataArray[i * 2 + 1].numAyahs
            
            XCTAssertEqual(expected, actual, "Failed comparison of Juz ${i + 1}")
        }
    }
    
    func test_number_of_verses_in_each_juz_matches_the_number_of_verses_in_each_juz_quarter() throws {
        let juzMetadataArray = try quranDatabase.getMetadataForSections(ofType: .juzInMajeediMushaf)
        let juzQuarterMetadataArray = try quranDatabase.getMetadataForSections(ofType: .juzQuarterInMajeediMushaf)
        
        for i in juzMetadataArray.indices {
            let expected = juzMetadataArray[i].numAyahs
            
            let actual = juzQuarterMetadataArray[i * 4].numAyahs
                + juzQuarterMetadataArray[i * 4 + 1].numAyahs
                + juzQuarterMetadataArray[i * 4 + 2].numAyahs
                + juzQuarterMetadataArray[i * 4 + 3].numAyahs
            
            XCTAssertEqual(expected, actual, "Failed comparison of Juz ${i + 1}")
        }
    }
    
    func test_surahNumber_and_ayahNumber_in_each_surah_is_as_expected() throws {
        let surahMetadataArray = try quranDatabase.getMetadataForSections(ofType: .surah)
        
        for i in surahMetadataArray.indices {
            let sectionMetadata = surahMetadataArray[i]
            
            XCTAssertEqual((i + 1), sectionMetadata.surahNumber)
            XCTAssertEqual(1, sectionMetadata.ayahNumber)
        }
    }
    
    func test_surahNumber_and_ayahNumber_in_each_madinah_mushaf_juz_is_as_expected() throws {
        try assertSurahAndVerseNumberOfFirstVerseInEachSectionOfType(.juzInMadinahMushaf)
    }
    
    func test_surahNumber_and_ayahNumber_in_each_majeedi_mushaf_juz_is_as_expected() throws {
        try assertSurahAndVerseNumberOfFirstVerseInEachSectionOfType(.juzInMajeediMushaf)
    }
    
    func test_surahNumber_and_ayahNumber_in_each_hizb_is_as_expected() throws {
        try assertSurahAndVerseNumberOfFirstVerseInEachSectionOfType(.hizbInMadinahMushaf)
    }
    
    func test_surahNumber_and_ayahNumber_in_each_hizb_quarter_is_as_expected() throws {
        try assertSurahAndVerseNumberOfFirstVerseInEachSectionOfType(.hizbQuarterInMadinahMushaf)
    }
    
    func test_surahNumber_and_ayahNumber_in_each_juz_quarter_is_as_expected() throws {
        try assertSurahAndVerseNumberOfFirstVerseInEachSectionOfType(.juzQuarterInMajeediMushaf)
    }
    
    private func assert(eachItemIn sectionMetadataArray: Array<SectionMetadata>,
                        hasSectionType sectionType: SectionType) {
        sectionMetadataArray.forEach { (sectionMetadata) in
            XCTAssertEqual(sectionType, sectionMetadata.sectionType)
        }
    }
    
    private func assertSectionNumbers(in sectionMetadataArray: Array<SectionMetadata>) {
        for i in sectionMetadataArray.indices {
            XCTAssertEqual((i + 1), sectionMetadataArray[i].sectionNumber)
        }
    }
    
    private func assertTotalNumberOfAyahs(in sectionMetadataArray: Array<SectionMetadata>,
                                          isEqualTo expectedNumberOfAyahs: Int) {
        var count = 0
    
        for sectionMetadata in sectionMetadataArray {
            count += sectionMetadata.numAyahs
        }

        XCTAssertEqual(expectedNumberOfAyahs, count)
    }
    
    private func assertSurahAndVerseNumberOfFirstVerseInEachSectionOfType(_ sectionType: SectionType) throws {
        let sectionMetadataArray = try quranDatabase.getMetadataForSections(ofType: sectionType)
        
        for i in sectionMetadataArray.indices {
            let surahNumberA = sectionMetadataArray[i].surahNumber
            let verseNumberA = sectionMetadataArray[i].ayahNumber
            
            let surahNumberB: Int
            let verseNumberB: Int
            
            if (i < sectionMetadataArray.count - 1) {
                surahNumberB = sectionMetadataArray[i + 1].surahNumber
                verseNumberB = sectionMetadataArray[i + 1].ayahNumber
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
                sectionMetadataArray[i].numAyahs,
                "Unexpected number of ayahs between \(sectionType) \(i + 1) and the next one."
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
        
            let surahAMetadata = try quranDatabase.getMetadataForSection(
                sectionType: .surah,
                sectionNumber: surahNumberA
            )
            
            count += surahAMetadata.numAyahs + 1 - verseNumberA
        
            // add to count the number of verses in each Surah between A and B (exclusive of A and B)
            for i in ((surahNumberA + 1)..<surahNumberB) {
                let currentSurahMetadata = try quranDatabase.getMetadataForSection(
                    sectionType: .surah,
                    sectionNumber: i
                )
                count += currentSurahMetadata.numAyahs
            }
            
            count += verseNumberB - 1
            
            return count
        }
    }
}
