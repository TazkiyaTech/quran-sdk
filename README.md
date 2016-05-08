This Android SDK (library) has been put together to make it easy for developers to access verses of the Qur'an in their apps.

To start using the library you'll need to declare the repository from which you'll get the library. You can do this by copying the following declaration in the `build.gradle` file of your app:

    repositories {
        maven {
            url  "http://dl.bintray.com/adil-hussain-84/maven" 
        }
    }

Next, you need to add the library as a dependency in your app. You can do this by copying the following declaration in the `build.gradle` file of your app:

    dependencies {
        compile 'com.thinkincode.quran.sdk:Android:0.0.4'
    }

Next, on startup of your app, call the `QuranDatabase.openDatabase(Context)` method. Be sure to pass in the application context rather than the activity context.

Now that the Qur'an database is setup for your application, you can call any of the public getter methods of the `QuranDatabase` class to access information in the Qur'an database. These methods are as follows:

* `QuranDatabase.getSurahName(int surahNumber)` – returns the name of the specified Surah.
* `QuranDatabase.getSurahNames()` – return the names of all of the Surahs in the Qur'an.
* `QuranDatabase.getAyahsInSurah(int surahNumber)` – return all of the ayahs of the specified Surah.
* `QuranDatabase.getAyah(int surahNumber, int ayahNumber)` – returns the text of the specified Ayah.

We advise calling all of the above methods (especially the `QuranDatabase.openDatabase(Context)` method) in a background thread since the methods are accessing file storage.
