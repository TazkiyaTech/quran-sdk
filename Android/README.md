# Quran SDK for Android

This library makes it easy for you to access verses of the Quran in your Android projects.

## Setup

Getting setup by means of [Gradle](https://gradle.org/) is a simple two-step process.

Firstly, add the following repository and dependency declaration in the `build.gradle` file of your Android project:

```groovy
repositories {
    mavenCentral()
}
dependencies {
    implementation 'com.tazkiyatech:quran-sdk:1.0.5'
}
```

Next, run a Gradle project sync in your IDE of choice and you should then be able to import classes from the `com.tazkiyatech.quran.sdk` package.

## Initialisation

On startup of your application, create a [QuranDatabase](src/main/java/com/tazkiyatech/quran/sdk/database/QuranDatabase.kt) instance as follows:

```kotlin
val quranDatabase = QuranDatabase(myApplicationContext);
quranDatabase.openDatabase();
```

Be sure to pass the application context into the [QuranDatabase](src/main/java/com/tazkiyatech/quran/sdk/database/QuranDatabase.kt) constructor rather than the activity context. Also, be sure to call the `QuranDatabase.openDatabse()` method in a background thread since it accesses file storage.

## Accessing Surahs and Ayahs

You can access the names of the Surahs and the text of the Ayahs in the Quran by calling any of the following methods on an instance of the [QuranDatabase](src/main/java/com/tazkiyatech/quran/sdk/database/QuranDatabase.kt) class:

```kotlin
// get the names of all of the Surahs in the Quran
val surahNames: List<String> = quranDatabase.getSurahNames()

// get the name of the specified Surah
val surahName: String = quranDatabase.getNameOfSurah(surahNumber)

// get the text of all of the Ayahs in the specified Surah
val ayahs: List<String> = quranDatabase.getAyahsInSurah(surahNumber)

// get the text of the specified Ayah
val ayah: String = quranDatabase.getAyah(surahNumber, ayahNumber)
```

Be sure to call these methods in a background thread since they access file storage.

## Accessing Quran metadata

You can access [metadata](src/main/java/com/tazkiyatech/quran/sdk/model/SectionMetadata.kt) about the Quran by calling any of the following methods on an instance of the [QuranDatabase](src/main/java/com/tazkiyatech/quran/sdk/database/QuranDatabase.kt) class:

```kotlin
// get metadata for all sections in the Quran of a particular type (i.e. for all Surahs, Juzs, Juz-Quarters, Hizbs or Hizb-Quarters)
val sectionType = SectionType.SURAH
val sectionMetadataList: List<SectionMetadata> = getMetadataForSectionsOfType(sectionType)

// get metadata for a particular section of the Quran (i.e. a particular Surah, Juz, Juz-Quarter, Hizb or Hizb-Quarter)
val sectionType = SectionType.SURAH
val sectionNumber = 1
val sectionMetadata: SectionMetadata = getMetadataForSection(sectionType, sectionNumber)
```

Be sure to call these methods in a background thread since they access file storage.

## Quran Quotes and Hifdh Tips

As a bonus feature we have also included within the library some short, concise quotes and tips as inspiration for those memorising the Quran.

Quotes about the magnificence and virtues of the Quran can be accessed by means of the [QuranQuotes](src/main/java/com/tazkiyatech/quran/sdk/database/QuranQuotes.kt) class as follows:

```kotlin
val quranQuotes = QuranQuotes(myApplicationContext.getResources());
val quranQuote: String = quranQuotes.getNextRandom();
```

Practical tips about how to learn and remember the Quran can be accessed by means of the [HifdhTips](src/main/java/com/tazkiyatech/quran/sdk/database/HifdhTips.kt) class as follows:

```kotlin
val hifdhTips = HifdhTips(myApplicationContext.getResources());
val hifdhTip: String = hifdhTips.getNextRandom();
```
