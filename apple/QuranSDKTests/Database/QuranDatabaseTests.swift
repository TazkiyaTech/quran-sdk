//
//  QuranDatabaseTests.swift
//  QuranSDKTests
//
//  Created by Adil Hussain on 23/11/2018.
//  Copyright © 2018 Tazkiya Tech. All rights reserved.
//

import Testing
@testable import QuranSDK

@Suite(.serialized)
class QuranDatabaseTests {

    private let quranDatabase = QuranDatabase()

    init() throws {
        do {
            try QuranDatabase.deleteDatabaseInInternalStorage()
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
    func isDatabaseExistsInInternalStorage_when_database_not_opened() throws {
        // When.
        let result = try QuranDatabase.isDatabaseExistsInInternalStorage()

        // Then.
        #expect(result == false)
    }

    @Test
    func isDatabaseExistsInInternalStorage_when_database_opened() throws {
        // Given.
        try quranDatabase.openDatabase()

        // When.
        let result = try QuranDatabase.isDatabaseExistsInInternalStorage()

        // Then.
        #expect(result == true)
    }

    @Test
    func isDatabaseOpen_when_database_not_opened() {
        // When.
        let result = quranDatabase.isDatabaseOpen()

        // Then.
        #expect(result == false)
    }

    @Test
    func isDatabaseOpen_when_database_opened() throws {
        // Given.
        try quranDatabase.openDatabase()

        // When.
        let result = quranDatabase.isDatabaseOpen()

        // Then.
        #expect(result == true)
    }

    @Test
    func openDatabase_on_two_separate_instances() throws {
        // Given.
        try QuranDatabase().openDatabase()

        // When.
        try quranDatabase.openDatabase()

        // Then.
        #expect(quranDatabase.isDatabaseOpen() == true)
    }

    @Test
    func getNameOfSurah_with_surah_number_1() throws {
        // When.
        let surahName = try quranDatabase.getNameOfSurah(1)
        
        // Then.
        #expect(surahName == "الفاتحة")
    }
    
    @Test
    func getNameOfSurah_with_invalid_surah_number() throws {
        // When. / Then.
        let error = try #require(throws: QuranDatabaseError.self) {
            try quranDatabase.getNameOfSurah(115)
        }
        
        try #require(error.message == "Failed executing query. Failed getting Surah name for Surah 115.")
        
        let underlyingError = try #require(error.underlyingError as? QuranDatabaseError)
        
        #expect(underlyingError.message == "No rows returned in query. Step result was 101.")
        #expect(underlyingError.underlyingError == nil)
    }
    
    @Test
    func getSurahNames() throws {
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
        #expect(actual == expected)
    }

    @Test
    func getAyahsInSurah_with_valid_surah_number() throws {
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
        #expect(actual == expected)
    }
    
    @Test
    func getAyahsInSurah_for_each_and_every_surah() throws {
        // Given.
        let surahMetadataArray = try quranDatabase.getMetadataForSections(ofType: .surah)
        
        try #require(surahMetadataArray.count == 114)
        
        for surahMetadata in surahMetadataArray {
            // Given.
            let expectedNumberOfVerses = surahMetadata.numAyahs
            
            // When.
            let actualNumberOfVerses = try quranDatabase.getAyahsInSurah(surahMetadata.sectionNumber).count
            
            // Then.
            #expect(actualNumberOfVerses == expectedNumberOfVerses, "Mismatch for Surah number \(surahMetadata.sectionNumber)")
        }
    }

    @Test
    func getAyahsInSurah_with_invalid_surah_number() throws {
        // When. / Then.
        let error = try #require(throws: QuranDatabaseError.self) {
            try quranDatabase.getAyahsInSurah(115)
        }
        
        #expect(error.message == "Failed executing query. Failed getting Ayahs for Surah 115.")
        
        let underlyingError = try #require(error.underlyingError as? QuranDatabaseError)
        
        #expect(underlyingError.message == "No rows returned in query for Surah 115.")
        #expect(underlyingError.underlyingError == nil)
    }
    
    @Test
    func getAyah_with_surah_number_1_and_ayah_number_1() throws {
        // Given.
        let expected = "بِسْمِ اللَّهِ الرَّحْمَٰنِ الرَّحِيمِ"
        
        // When.
        let actual = try quranDatabase.getAyah(surahNumber: 1, ayahNumber: 1)

        // Then.
        #expect(actual == expected)
    }
    
    @Test
    func getAyah_with_surah_number_58_and_ayah_number_6() throws {
        // Given.
        let expected = "يَوْمَ يَبْعَثُهُمُ اللَّهُ جَمِيعًا فَيُنَبِّئُهُمْ بِمَا عَمِلُوا ۚ أَحْصَاهُ اللَّهُ وَنَسُوهُ ۚ وَاللَّهُ عَلَىٰ كُلِّ شَيْءٍ شَهِيدٌ"
        
        // When.
        let actual = try quranDatabase.getAyah(surahNumber: 58, ayahNumber: 6)
        
        // Then.
        #expect(actual == expected)
    }
    
    @Test
    func getAyah_with_invalid_surah_number() throws {
        // When. / Then.
        let error = try #require(throws: QuranDatabaseError.self) {
            try quranDatabase.getAyah(surahNumber: 115, ayahNumber: 1)
        }
        
        #expect(error.message == "Failed executing query. Failed getting Ayah for Surah 115, Ayah 1.")
        
        let underlyingError = try #require(error.underlyingError as? QuranDatabaseError)
        
        #expect(underlyingError.message == "No rows returned in query. Step result was 101.")
        #expect(underlyingError.underlyingError == nil)
    }
    
    @Test
    func getAyah_with_invalid_ayah_number() throws {
        // When. / Then.
        let error = try #require(throws: QuranDatabaseError.self) {
            try quranDatabase.getAyah(surahNumber: 1, ayahNumber: 8)
        }
        
        #expect(error.message == "Failed executing query. Failed getting Ayah for Surah 1, Ayah 8.")
        
        let underlyingError = try #require(error.underlyingError as? QuranDatabaseError)
        
        #expect(underlyingError.message == "No rows returned in query. Step result was 101.")
        #expect(underlyingError.underlyingError == nil)
    }
}
