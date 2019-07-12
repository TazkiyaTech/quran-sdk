# Quran SDK for iOS

This library makes it easy for you to access verses of the Quran in your iOS projects.

## Setup

Getting setup by means of [Cocoapods](https://cocoapods.org/) is a simple two-step process.

Firstly, add the following pod declaration in the `Podfile` of your iOS project:

    platform :ios, '10.0'
    use_frameworks!
    pod 'QuranSDK', '~> 1.0.0'

Next, run the `pod install` command for your iOS project and you should then be able to add `import QuranSDK` in your Swift code.

## Initialisation

On startup of your application, create a [QuranDatabase](QuranSDK/Database/QuranDatabase.swift) instance as follows:

    let quranDatabase = QuranDatabase();
    quranDatabase.openDatabase();

Be sure to call the `QuranDatabase.openDatabse()` method in a background thread since it accesses file storage.

## Accessing Surahs and Ayahs

Once the Quran database is setup for your application (see above), you can call any of the public getter methods of the [QuranDatabase](QuranSDK/Database/QuranDatabase.swift) class to access Surahs and Ayahs in the Quran database. These methods are as follows:

    // get the names of all of the Surahs in the Quran
    let surahNames: [String] = try quranDatabase.getSurahNames();
    
    // get the name of the specified Surah
    let surahName: String = try quranDatabase.getNameOfSurah(surahNumber)
    
    // get the text of all of the Ayahs in the specified Surah
    let ayahs: [String] = try quranDatabase.getAyahsInSurah(surahNumber);
    
    // get the text of the specified Ayah
    let ayah: String = try quranDatabase.getAyah(surahNumber: surahNumber, ayahNumber: ayahNumber);

We advise calling all of the above methods in a background thread since the methods access file storage.
