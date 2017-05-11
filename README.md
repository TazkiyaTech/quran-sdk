#Quran Android SDK

##About

This Quran Android SDK (library) has been put together to make it easy for developers to access verses of the Quran in their Android apps.

##Setup

To start using the library in your app you'll need to first declare the repository from which you'll get the library. You can do this by copying the following `jcenter` repository declaration into the `build.gradle` file of your app:

    repositories {
        jcenter()
    }

Next, you need to add this library as a dependency in your app. You can do this by copying the following dependency declaration into the `build.gradle` file of your app:

    dependencies {
        compile 'com.thinkincode.quran.sdk:Android:0.1.3'
    }

##Initialisation

On startup of your app, create a [QuranDatabase](Android/src/main/java/com/thinkincode/quran/sdk/database/QuranDatabase.java) instance as follows:

    QuranDatabase quranDatabase = new QuranDatabase(myApplicationContext);
    quranDatabase.openDatabase();

Be sure to pass the application context into the [QuranDatabase](Android/src/main/java/com/thinkincode/quran/sdk/database/QuranDatabase.java) constructor rather than the activity context. Also, be sure to call the `QuranDatabase.openDatabase()` method in a background thread since it accesses file storage.

##Accessing Surahs and Ayahs

Once the Quran database is setup for your application (see above), you can call any of the public getter methods of the [QuranDatabase](Android/src/main/java/com/thinkincode/quran/sdk/database/QuranDatabase.java) class to access Surahs and Ayahs in the Quran database. These methods are as follows:

* `QuranDatabase.getSurahName(int surahNumber)` – returns the name of the specified Surah.
* `QuranDatabase.getSurahNames()` – returns the names of all of the Surahs in the Quran.
* `QuranDatabase.getAyahsInSurah(int surahNumber)` – returns all of the Ayahs of the specified Surah.
* `QuranDatabase.getAyah(int surahNumber, int ayahNumber)` – returns the text of the specified Ayah.

We advise calling all of the above methods in a background thread since the methods access file storage.
