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
        quranDatabase = QuranDatabase()
        
        do {
            try quranDatabase.deleteDatabaseInInternalStorage()
        } catch {
            XCTFail("Failed deleting the database file in the test setup: \(error)")
        }
    }

    override func tearDown() {
        try! quranDatabase.closeDatabase()
    }
    
    func test_isDatabaseExistsInDocumentsDirectory_when_database_not_opened() throws {
        // When.
        let result = try quranDatabase.isDatabaseExistsInInternalStorage();
        
        // Then.
        XCTAssertFalse(result);
    }
    
    func test_isDatabaseExistsInDocumentsDirectory_when_database_opened() throws {
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

    func test_getSurahName() throws {
        XCTAssertEqual("الفاتحة", try quranDatabase.getSurahName(1));
    }

    func test_getAyahsInSurah() throws {
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

    func test_getAyah() throws {
        // When.
        let actual = try quranDatabase.getAyah(surahNumber: 1, ayahNumber: 1)

        // Then.
        XCTAssertEqual("بِسْمِ اللَّهِ الرَّحْمَٰنِ الرَّحِيمِ", actual);
    }
}
