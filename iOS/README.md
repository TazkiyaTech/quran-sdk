# Quran SDK for iOS

This library gives you easy, offline access to verses of the Quran in your iOS projects.

## Setup

### Swift Package Manager

Getting setup by means of [Swift Package Manager](https://swift.org/package-manager) is a simple two-step process, as follows:

1. Add this library to your Xcode project's list of Swift packages by means of this repo's URL.
2. Select the target(s) that you would like to add this Swift package to.

### CocoaPods

Getting setup by means of [Cocoapods](https://cocoapods.org) is a simple two-step process.

Firstly, add the following pod declaration in the `Podfile` of your iOS project:

```ruby
platform :ios, '11.0'
use_frameworks!
pod 'QuranSDK', '~> 1.2.0'
```

Next, run the `pod install` command for your Xcode workspace and you should then be able to import the `QuranSDK` module in your Swift code.

## Initialisation

On startup of your application, create a [QuranDatabase](QuranSDK/Database/QuranDatabase.swift) instance as follows:

```swift
let quranDatabase = QuranDatabase();
quranDatabase.openDatabase();
```

Be sure to call the `QuranDatabase.openDatabse()` method in a background thread since it accesses file storage.

## Accessing Surahs and Ayahs

You can access the names of the Surahs and the text of the Ayahs in the Quran by calling any of the following methods on an instance of the [QuranDatabase](QuranSDK/Database/QuranDatabase.swift) class:

```swift
// get the names of all of the Surahs in the Quran
let surahNames: [String] = try quranDatabase.getSurahNames();

// get the name of the specified Surah
let surahName: String = try quranDatabase.getNameOfSurah(surahNumber)

// get the text of all of the Ayahs in the specified Surah
let ayahs: [String] = try quranDatabase.getAyahsInSurah(surahNumber);

// get the text of the specified Ayah
let ayah: String = try quranDatabase.getAyah(surahNumber: surahNumber, ayahNumber: ayahNumber);
```

Be sure to call these methods in a background thread since they access file storage.

## Accessing Quran metadata

You can access [metadata](QuranSDK/Models/SectionMetadata.swift) about the Quran by calling any of the following methods on an instance of the [QuranDatabase](QuranSDK/Database/QuranDatabase.swift) class:

```swift
// get metadata for all sections in the Quran of a particular type (i.e. for all Surahs, Juzs, Juz-Quarters, Hizbs or Hizb-Quarters)
let sectionType = SectionType.surah
let sectionMetadataArray: [SectionMetadata] = try getMetadataForSections(ofType: sectionType)

// get metadata for a particular section of the Quran (i.e. a particular Surah, Juz, Juz-Quarter, Hizb or Hizb-Quarter)
let sectionType = SectionType.surah
let sectionNumber = 1
let sectionMetadata: SectionMetadata = try getMetadataForSection(sectionType: sectionType, sectionNumber: sectionNumber)
```

Be sure to call these methods in a background thread since they access file storage.
