# Quran SDK for Android

This library makes it easy for you to access verses of the Quran in your Android projects.

## Setup

To start using this library you need to add the following `jcenter` repository declaration into the `build.gradle` file of your Android project:

    repositories {
        jcenter()
    }

Next, you need to copy the following dependency declaration into the `build.gradle` file of your Android project:

    dependencies {
        compile 'com.tazkiyatech:quran-sdk:1.0.0'
    }

## Initialisation

On startup of your application, create a [QuranDatabase](src/main/java/com/tazkiyatech/quran/sdk/database/QuranDatabase.kt) instance as follows:

    QuranDatabase quranDatabase = new QuranDatabase(myApplicationContext);
    quranDatabase.openDatabase();

Be sure to pass the application context into the [QuranDatabase](src/main/java/com/tazkiyatech/quran/sdk/database/QuranDatabase.kt) constructor rather than the activity context. Also, be sure to call the `QuranDatabase.openDatabse()` method in a background thread since it accesses file storage.

## Accessing Surahs and Ayahs

Once the Quran database is setup for your application (see above), you can call any of the public getter methods of the [QuranDatabase](src/main/java/com/tazkiyatech/quran/sdk/database/QuranDatabase.kt) class to access Surahs and Ayahs in the Quran database. These methods are as follows:

    // get the names of all of the Surahs in the Quran
    List<String> surahNames = quranDatabase.getSurahNames();
    
    // get the name of the specified Surah
    String surahName = quranDatabase.getSurahName(surahNumber);
    
    // get the text of all of the Ayahs in the specified Surah
    List<String> ayahs = quranDatabase.getAyahsInSurah(surahNumber);
    
    // get the text of the specified Ayah
    String ayah = quranDatabase.getAyah(surahNumber, ayahNumber);

We advise calling all of the above methods in a background thread since the methods access file storage.

## Quran Quotes and Hifdh Tips

As a bonus feature we have also included within the library some short, concise quotes and tips as inspiration for those memorising the Quran.

Quotes about the magnificence and virtues of the Quran can be accessed by means of the [QuranQuotes](src/main/java/com/tazkiyatech/quran/sdk/database/QuranQuotes.kt) class as follows:

    QuranQuotes quranQuotes = new QuranQuotes(myApplicationContext.getResources());
    String quranQuote = quranQuotes.getNextRandom();

Practical tips about how to learn and remember the Quran can be accessed by means of the [HifdhTips](src/main/java/com/tazkiyatech/quran/sdk/database/HifdhTips.kt) class as follows:

    HifdhTips hifdhTips = new HifdhTips(myApplicationContext.getResources());
    String hifdhTip = hifdhTips.getNextRandom();
