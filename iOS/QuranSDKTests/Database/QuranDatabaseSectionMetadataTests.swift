//
//  QuranDatabaseSectionMetadataTests.swift
//  QuranSDKTests
//
//  Created by Adil on 13/06/2019.
//  Copyright © 2019 Tazkiya Tech. All rights reserved.
//

import Testing
@testable import QuranSDK

@Suite(.serialized)
class QuranDatabaseSectionMetadataTests {
    
    private let quranDatabase = QuranDatabase()
    
    init() throws {
        do {
            try quranDatabase.deleteDatabaseInInternalStorage()
        } catch {
            throw QuranSDKTestsError(
                message: "Failed deleting the database file in the test initialiser",
                underlyingError: error,
            )
        }
    }
    
    deinit {
        try? quranDatabase.closeDatabase()
    }
    
    @Test
    func getMetadataForSection_with_section_type_surah_and_section_number_1() throws {
        // When.
        let sectionMetadata = try quranDatabase.getMetadataForSection(
            sectionType: .surah,
            sectionNumber: 1
        )
        
        // Then.
        #expect(sectionMetadata.sectionType == .surah)
        #expect(sectionMetadata.sectionNumber == 1)
        #expect(sectionMetadata.numAyahs == 7)
        #expect(sectionMetadata.surahNumber == 1)
        #expect(sectionMetadata.ayahNumber == 1)
    }
    
    @Test
    func getMetadataForSection_with_section_type_surah_and_section_number_0() throws {
        // When. / Then.
        let error = try #require(throws: QuranDatabaseError.self) {
            try quranDatabase.getMetadataForSection(sectionType: .surah, sectionNumber: 0)
        }
        
        #expect(error.message == "Failed executing query. Failed getting section metadata for section type = surah, section number = 0.")
        
        let underlyingError = try #require(error.underlyingError as? QuranDatabaseError)
        
        #expect(underlyingError.message == "No rows returned in query. Step result was 101.")
        #expect(underlyingError.underlyingError == nil)
    }
    
    @Test
    func getMetadataForSection_with_section_type_surah_and_section_number_115() throws {
        // When. / Then.
        let error = try #require(throws: QuranDatabaseError.self) {
            try quranDatabase.getMetadataForSection(sectionType: .surah, sectionNumber: 115)
        }
        
        #expect(error.message == "Failed executing query. Failed getting section metadata for section type = surah, section number = 115.")
        
        let underlyingError = try #require(error.underlyingError as? QuranDatabaseError)
        
        #expect(underlyingError.message == "No rows returned in query. Step result was 101.")
        #expect(underlyingError.underlyingError == nil)
    }
    
    @Test
    func getMetadataForSections_of_type_surah() throws {
        // Given.
        let sectionType = SectionType.surah
        
        // When.
        let sectionMetadataArray = try quranDatabase.getMetadataForSections(ofType: sectionType)
        
        // Then.
        #expect(sectionMetadataArray.count == 114)
        
        // And.
        assert(eachItemIn: sectionMetadataArray, hasSectionType: sectionType)
        assertSectionNumbers(in: sectionMetadataArray)
        assertTotalNumberOfAyahs(in: sectionMetadataArray, isEqualTo: 6236)
    }
    
    @Test
    func getMetadataForSections_of_type_juz_in_madinah_mushaf() throws {
        // Given.
        let sectionType = SectionType.juzInMadinahMushaf
        
        // When.
        let sectionMetadataArray = try quranDatabase.getMetadataForSections(ofType: sectionType)
        
        // Then.
        #expect(sectionMetadataArray.count == 30)
        
        // And.
        assert(eachItemIn: sectionMetadataArray, hasSectionType: sectionType)
        assertSectionNumbers(in: sectionMetadataArray)
        assertTotalNumberOfAyahs(in: sectionMetadataArray, isEqualTo: 6236)
    }
    
    @Test
    func getMetadataForSections_of_type_hizb() throws {
        // Given.
        let sectionType = SectionType.hizbInMadinahMushaf
        
        // When.
        let sectionMetadataArray = try quranDatabase.getMetadataForSections(ofType: sectionType)
        
        // Then.
        #expect(sectionMetadataArray.count == 60)
        
        // And.
        assert(eachItemIn: sectionMetadataArray, hasSectionType: sectionType)
        assertSectionNumbers(in: sectionMetadataArray)
        assertTotalNumberOfAyahs(in: sectionMetadataArray, isEqualTo: 6236)
    }
    
    @Test
    func getMetadataForSections_of_type_hizb_quarter() throws {
        // Given.
        let sectionType = SectionType.hizbQuarterInMadinahMushaf
        
        // When.
        let sectionMetadataArray = try quranDatabase.getMetadataForSections(ofType: sectionType)
        
        // Then.
        #expect(sectionMetadataArray.count == 240)
        
        // And.
        assert(eachItemIn: sectionMetadataArray, hasSectionType: sectionType)
        assertSectionNumbers(in: sectionMetadataArray)
        assertTotalNumberOfAyahs(in: sectionMetadataArray, isEqualTo: 6236)
    }
    
    @Test
    func getMetadataForSections_of_type_juz_in_majeedi_mushaf() throws {
        // Given.
        let sectionType = SectionType.juzInMajeediMushaf
        
        // When.
        let sectionMetadataArray = try quranDatabase.getMetadataForSections(ofType: sectionType)
        
        // Then.
        #expect(sectionMetadataArray.count == 30)
        
        // And.
        assert(eachItemIn: sectionMetadataArray, hasSectionType: sectionType)
        assertSectionNumbers(in: sectionMetadataArray)
        assertTotalNumberOfAyahs(in: sectionMetadataArray, isEqualTo: (6236 - 7))
    }
    
    @Test
    func getMetadataForSections_of_type_juz_quarter_in_majeedi_mushaf() throws {
        // Given.
        let sectionType = SectionType.juzQuarterInMajeediMushaf
        
        // When.
        let sectionMetadataArray = try quranDatabase.getMetadataForSections(ofType: sectionType)
        
        // Then.
        #expect(sectionMetadataArray.count == 120)
        
        // And.
        assert(eachItemIn: sectionMetadataArray, hasSectionType: sectionType)
        assertSectionNumbers(in: sectionMetadataArray)
        assertTotalNumberOfAyahs(in: sectionMetadataArray, isEqualTo: (6236 - 7))
    }
    
    @Test
    func number_of_verses_in_each_hizb_matches_the_number_of_verses_in_each_hizb_quarter() throws {
        let hizbMetadataArray = try quranDatabase.getMetadataForSections(ofType: .hizbInMadinahMushaf)
        
        let hizbQuarterMetadataArray = try quranDatabase.getMetadataForSections(ofType: .hizbQuarterInMadinahMushaf)
        
        for i in hizbMetadataArray.indices {
            let expected = hizbMetadataArray[i].numAyahs
            
            let actual = (hizbQuarterMetadataArray[i * 4].numAyahs
                + hizbQuarterMetadataArray[i * 4 + 1].numAyahs
                + hizbQuarterMetadataArray[i * 4 + 2].numAyahs
                + hizbQuarterMetadataArray[i * 4 + 3].numAyahs)
            
            #expect(expected == actual, "Failed comparison of Hizb \(i + 1)")
        }
    }
    
    @Test
    func number_of_verses_in_each_juz_matches_the_number_of_verses_in_each_hizb() throws {
        let juzMetadataArray = try quranDatabase.getMetadataForSections(ofType: .juzInMadinahMushaf)
        let hizbMetadataArray = try quranDatabase.getMetadataForSections(ofType: .hizbInMadinahMushaf)
        
        for i in juzMetadataArray.indices {
            let expected = juzMetadataArray[i].numAyahs
            
            let actual = hizbMetadataArray[i * 2].numAyahs + hizbMetadataArray[i * 2 + 1].numAyahs
            
            #expect(expected == actual, "Failed comparison of Juz \(i + 1)")
        }
    }
    
    @Test
    func number_of_verses_in_each_juz_matches_the_number_of_verses_in_each_juz_quarter() throws {
        let juzMetadataArray = try quranDatabase.getMetadataForSections(ofType: .juzInMajeediMushaf)
        let juzQuarterMetadataArray = try quranDatabase.getMetadataForSections(ofType: .juzQuarterInMajeediMushaf)
        
        for i in juzMetadataArray.indices {
            let expected = juzMetadataArray[i].numAyahs
            
            let actual = juzQuarterMetadataArray[i * 4].numAyahs
                + juzQuarterMetadataArray[i * 4 + 1].numAyahs
                + juzQuarterMetadataArray[i * 4 + 2].numAyahs
                + juzQuarterMetadataArray[i * 4 + 3].numAyahs
            
            #expect(expected == actual, "Failed comparison of Juz \(i + 1)")
        }
    }
    
    @Test
    func surahNumber_and_ayahNumber_in_each_surah_is_as_expected() throws {
        let surahMetadataArray = try quranDatabase.getMetadataForSections(ofType: .surah)
        
        for i in surahMetadataArray.indices {
            let sectionMetadata = surahMetadataArray[i]
            
            #expect((i + 1) == sectionMetadata.surahNumber)
            #expect(1 == sectionMetadata.ayahNumber)
        }
    }
    
    @Test
    func surahNumber_and_ayahNumber_in_each_madinah_mushaf_juz_is_as_expected() throws {
        try assertSurahAndVerseNumberOfFirstVerseInEachSectionOfType(.juzInMadinahMushaf)
    }
    
    @Test
    func surahNumber_and_ayahNumber_in_each_majeedi_mushaf_juz_is_as_expected() throws {
        try assertSurahAndVerseNumberOfFirstVerseInEachSectionOfType(.juzInMajeediMushaf)
    }
    
    @Test
    func surahNumber_and_ayahNumber_in_each_hizb_is_as_expected() throws {
        try assertSurahAndVerseNumberOfFirstVerseInEachSectionOfType(.hizbInMadinahMushaf)
    }
    
    @Test
    func surahNumber_and_ayahNumber_in_each_hizb_quarter_is_as_expected() throws {
        try assertSurahAndVerseNumberOfFirstVerseInEachSectionOfType(.hizbQuarterInMadinahMushaf)
    }
    
    @Test
    func surahNumber_and_ayahNumber_in_each_juz_quarter_is_as_expected() throws {
        try assertSurahAndVerseNumberOfFirstVerseInEachSectionOfType(.juzQuarterInMajeediMushaf)
    }
    
    private func assert(eachItemIn sectionMetadataArray: Array<SectionMetadata>,
                        hasSectionType sectionType: SectionType) {
        sectionMetadataArray.forEach { (sectionMetadata) in
            #expect(sectionType == sectionMetadata.sectionType)
        }
    }
    
    private func assertSectionNumbers(in sectionMetadataArray: Array<SectionMetadata>) {
        for i in sectionMetadataArray.indices {
            #expect((i + 1) == sectionMetadataArray[i].sectionNumber)
        }
    }
    
    private func assertTotalNumberOfAyahs(in sectionMetadataArray: Array<SectionMetadata>,
                                          isEqualTo expectedNumberOfAyahs: Int) {
        var count = 0
    
        for sectionMetadata in sectionMetadataArray {
            count += sectionMetadata.numAyahs
        }

        #expect(expectedNumberOfAyahs == count)
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
            
            try #require(count == sectionMetadataArray[i].numAyahs, "Unexpected number of ayahs between \(sectionType) \(i + 1) and the next one.")
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
