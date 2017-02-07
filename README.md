This Quran Android SDK (library) has been put together to make it easy for developers to access verses of the Quran in their Android apps.

To start using the library you'll need to declare the repository from which you'll get the library. You can do this by copying the following `jcenter` repository declaration into the `build.gradle` file of your app:

    repositories {
        jcenter()
    }

Next, you need to add this library as a dependency in your app. You can do this by copying the following dependency declaration into the `build.gradle` file of your app:

    dependencies {
        compile 'com.thinkincode.quran.sdk:Android:0.1.3'
    }

Next, on startup of your app, call the `QuranDatabase.openDatabase()` method. Be sure to pass the application context into the [QuranDatabase](Android/src/main/java/com/thinkincode/quran/sdk/database/QuranDatabase.java) constructor rather than the activity context.

Now that the Quran database is setup for your application, you can call any of the public getter methods of the [QuranDatabase](Android/src/main/java/com/thinkincode/quran/sdk/database/QuranDatabase.java) class to access information in the Quran database. These methods are as follows:

* `QuranDatabase.getSurahName(int surahNumber)` – returns the name of the specified Surah.
* `QuranDatabase.getSurahNames()` – returns the names of all of the Surahs in the Quran.
* `QuranDatabase.getAyahsInSurah(int surahNumber)` – returns all of the Ayahs of the specified Surah.
* `QuranDatabase.getAyah(int surahNumber, int ayahNumber)` – returns the text of the specified Ayah.

We advise calling all of the above methods (especially the `QuranDatabase.openDatabase()` method) in a background thread since the methods are accessing file storage.
