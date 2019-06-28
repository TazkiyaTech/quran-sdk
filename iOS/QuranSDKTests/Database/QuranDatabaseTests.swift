//
//  QuranDatabaseTests.swift
//  QuranSDKTests
//
//  Created by Adil Hussain on 23/11/2018.
//  Copyright © 2018 Tazkiya Tech. All rights reserved.
//

import XCTest
@testable import QuranSDK

class QuranDatabaseTests: XCTestCase {

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

    func test_isDatabaseExistsInInternalStorage_when_database_not_opened() throws {
        // When.
        let result = try quranDatabase.isDatabaseExistsInInternalStorage();

        // Then.
        XCTAssertFalse(result);
    }

    func test_isDatabaseExistsInInternalStorage_when_database_opened() throws {
        // Given.
        try quranDatabase.openDatabase();

        // When.
        let result = try quranDatabase.isDatabaseExistsInInternalStorage();

        // Then.
        XCTAssertTrue(result);
    }

    func isDatabaseOpen_when_database_not_opened() throws {
        // When.
        let result = quranDatabase.isDatabaseOpen();

        // Then.
        XCTAssertFalse(result);
    }

    func test_isDatabaseOpen_when_database_opened() throws {
        // Given.
        try quranDatabase.openDatabase();

        // When.
        let result = quranDatabase.isDatabaseOpen();

        // Then.
        XCTAssertTrue(result);
    }

    func test_openDatabase_on_two_separate_instances() throws {
        // Given.
        try QuranDatabase().openDatabase();

        // When.
        try quranDatabase.openDatabase();

        // Then.
        XCTAssertTrue(quranDatabase.isDatabaseOpen());
    }

    func test_getNameOfSurah_with_surah_number_1() throws {
        // When.
        let surahName = try quranDatabase.getNameOfSurah(1)
        
        // Then.
        XCTAssertEqual("الفاتحة", surahName);
    }
    
    func test_getNameOfSurah_with_invalid_surah_number() {
        // When. / Then.
        XCTAssertThrowsError(try quranDatabase.getNameOfSurah(115))
    }
    
    func test_getSurahNames() throws {
        // Given.
        let expected = [
            "الفاتحة",
            "البقرة",
            "آل عمران",
            "النساء",
            "المائدة",
            "الأنعام",
            "الأعراف",
            "الأنفال",
            "التوبة",
            "يونس",
            "هود",
            "يوسف",
            "الرعد",
            "ابراهيم",
            "الحجر",
            "النحل",
            "الإسراء",
            "الكهف",
            "مريم",
            "طه",
            "الأنبياء",
            "الحج",
            "المؤمنون",
            "النور",
            "الفرقان",
            "الشعراء",
            "النمل",
            "القصص",
            "العنكبوت",
            "الروم",
            "لقمان",
            "السجدة",
            "الأحزاب",
            "سبإ",
            "فاطر",
            "يس",
            "الصافات",
            "ص",
            "الزمر",
            "غافر",
            "فصلت",
            "الشورى",
            "الزخرف",
            "الدخان",
            "الجاثية",
            "الأحقاف",
            "محمد",
            "الفتح",
            "الحجرات",
            "ق",
            "الذاريات",
            "الطور",
            "النجم",
            "القمر",
            "الرحمن",
            "الواقعة",
            "الحديد",
            "المجادلة",
            "الحشر",
            "الممتحنة",
            "الصف",
            "الجمعة",
            "المنافقون",
            "التغابن",
            "الطلاق",
            "التحريم",
            "الملك",
            "القلم",
            "الحاقة",
            "المعارج",
            "نوح",
            "الجن",
            "المزمل",
            "المدثر",
            "القيامة",
            "الانسان",
            "المرسلات",
            "النبإ",
            "النازعات",
            "عبس",
            "التكوير",
            "الإنفطار",
            "المطففين",
            "الإنشقاق",
            "البروج",
            "الطارق",
            "الأعلى",
            "الغاشية",
            "الفجر",
            "البلد",
            "الشمس",
            "الليل",
            "الضحى",
            "الشرح",
            "التين",
            "العلق",
            "القدر",
            "البينة",
            "الزلزلة",
            "العاديات",
            "القارعة",
            "التكاثر",
            "العصر",
            "الهمزة",
            "الفيل",
            "قريش",
            "الماعون",
            "الكوثر",
            "الكافرون",
            "النصر",
            "المسد",
            "الإخلاص",
            "الفلق",
            "الناس"
        ]

        // When.
        let actual = try quranDatabase.getSurahNames()

        // Then.
        XCTAssertEqual(expected, actual);
    }

    func test_getAyahsInSurah_with_valid_surah_number() throws {
        // Given.
        let expected = [
            "بِسْمِ اللَّهِ الرَّحْمَٰنِ الرَّحِيمِ",
            "الْحَمْدُ لِلَّهِ رَبِّ الْعَالَمِينَ",
            "الرَّحْمَٰنِ الرَّحِيمِ",
            "مَالِكِ يَوْمِ الدِّينِ",
            "إِيَّاكَ نَعْبُدُ وَإِيَّاكَ نَسْتَعِينُ",
            "اهْدِنَا الصِّرَاطَ الْمُسْتَقِيمَ",
            "صِرَاطَ الَّذِينَ أَنْعَمْتَ عَلَيْهِمْ غَيْرِ الْمَغْضُوبِ عَلَيْهِمْ وَلَا الضَّالِّينَ"
        ]

        // When.
        let actual = try quranDatabase.getAyahsInSurah(1)

        // Then.
        XCTAssertEqual(expected, actual);
    }
    
    func test_getAyahsInSurah_for_each_and_every_surah() throws {
        // Given.
        let surahMetadataArray = try quranDatabase.getMetadataForSections(ofType: .surah)
        
        XCTAssertEqual(114, surahMetadataArray.count)
        
        for surahMetadata in surahMetadataArray {
            // Given.
            let expectedNumberOfVerses = surahMetadata.numAyahs
            
            // When.
            let actualNumberOfVerses = try quranDatabase.getAyahsInSurah(surahMetadata.sectionNumber).count
            
            // Then.
            XCTAssertEqual(expectedNumberOfVerses, actualNumberOfVerses)
        }
    }

    func test_getAyahsInSurah_with_invalid_surah_number() {
        // When. / Then.
        XCTAssertThrowsError(try quranDatabase.getAyahsInSurah(115))
    }
    
    func test_getAyah_with_surah_number_1_and_ayah_number_1() throws {
        // Given.
        let expected = "بِسْمِ اللَّهِ الرَّحْمَٰنِ الرَّحِيمِ"
        
        // When.
        let actual = try quranDatabase.getAyah(surahNumber: 1, ayahNumber: 1)

        // Then.
        XCTAssertEqual(expected, actual);
    }
    
    func test_getAyah_with_surah_number_58_and_ayah_number_6() throws {
        // Given.
        let expected = "يَوْمَ يَبْعَثُهُمُ اللَّهُ جَمِيعًا فَيُنَبِّئُهُمْ بِمَا عَمِلُوا ۚ أَحْصَاهُ اللَّهُ وَنَسُوهُ ۚ وَاللَّهُ عَلَىٰ كُلِّ شَيْءٍ شَهِيدٌ"
        
        // When.
        let actual = try quranDatabase.getAyah(surahNumber: 58, ayahNumber: 6)
        
        // Then.
        XCTAssertEqual(expected, actual)
    }
    
    func test_getAyah_with_invalid_surah_number() throws {
        // When. / Then.
        XCTAssertThrowsError(try quranDatabase.getAyah(surahNumber: 115, ayahNumber: 1))
    }
    
    func test_getAyah_with_invalid_ayah_number() throws {
        // When. / Then.
        XCTAssertThrowsError(try quranDatabase.getAyah(surahNumber: 1, ayahNumber: 8))
    }
}
