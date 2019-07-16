# Quran SDK for iOS

This library makes it easy for you to access verses of the Quran in your iOS projects.

## Setup

Getting setup by means of [Cocoapods](https://cocoapods.org/) is a simple two-step process.

Firstly, add the following pod declaration in the `Podfile` of your iOS project:

    platform :ios, '10.0'
    use_frameworks!
    pod 'QuranSDK', '~> 1.0.0'

Next, run the `pod install` command for your iOS project and you should then be able to import the `QuranSDK` module in your Swift code.

## Initialisation

On startup of your application, create a [QuranDatabase](QuranSDK/Database/QuranDatabase.swift) instance as follows:

    let quranDatabase = QuranDatabase();
    quranDatabase.openDatabase();

Be sure to call the `QuranDatabase.openDatabse()` method in a background thread since it accesses file storage.

## Accessing Surahs and Ayahs

You can access the names of the Surahs and the text of the Ayahs in the Quran database by calling any of the following methods on an instance of the [QuranDatabase](QuranSDK/Database/QuranDatabase.swift) class:

    // get the names of all of the Surahs in the Quran
    let surahNames: [String] = try quranDatabase.getSurahNames();
    
    // get the name of the specified Surah
    let surahName: String = try quranDatabase.getNameOfSurah(surahNumber)
    
    // get the text of all of the Ayahs in the specified Surah
    let ayahs: [String] = try quranDatabase.getAyahsInSurah(surahNumber);
    
    // get the text of the specified Ayah
    let ayah: String = try quranDatabase.getAyah(surahNumber: surahNumber, ayahNumber: ayahNumber);

Be sure to call these methods in a background thread since they access file storage.

## Accessing Quran metadata

You can access [metadata](QuranSDK/Models/SectionMetadata.swift) about the Quran by calling any of the following methods on an instance of the [QuranDatabase](QuranSDK/Database/QuranDatabase.swift) class:

    // get metadata for all sections in the Quran of a particular type (i.e. for all Surahs, Juzs, Juz-Quarters, Hizbs or Hizb-Quarters)
    let sectionType = SectionType.surah
    let sectionMetadataArray: [SectionMetadata] = try getMetadataForSections(ofType: sectionType)

    /// get metadata for a particular section of the Quran (i.e. a particular Surah, Juz, Juz-Quarter, Hizb or Hizb-Quarter)
    let sectionType = SectionType.surah
    let sectionNumber = 1
    let sectionMetadata: SectionMetadata = try getMetadataForSection(sectionType: sectionType, sectionNumber: sectionNumber)

Be sure to call these methods in a background thread since they access file storage.
