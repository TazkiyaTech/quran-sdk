This Android library has been put together to make it easy for you to access verses of the Qur'an in your app.

To start using the library you'll need to declare the repository from which you'll get the library. You can do this by copying the following declaration in the `build.gradle` file of your app:

    repositories {
        maven {
            url  "http://dl.bintray.com/adil-hussain-84/maven" 
        }
    }

Next, you need to add the library as a dependency in your app. You can do this by copying the following declaration in the `build.gradle` file of your app:

    dependencies {
        compile 'com.thinkincode.quran.sdk:Android:0.0.1'
    }

Next, on startup of your app, call the `QuranDatabaseHelper.openDatabase(Context)` method. Be sure to pass in the application context rather than the activity context.

Now that the Qur'an database is setup for your application, you can call any of the public getter methods of the `QuranDatabaseHelper` class to access information in the Qur'an database. These methods are as follows:

* `QuranDatabaseHelper.getSurahName(Context context, int surahNumber)` – returns the name of the specified Surah.
* `QuranDatabaseHelper.getSurahNames(Context context)` – return the names of all of the Surahs in the Qur'an.
* `QuranDatabaseHelper.getAyahsInSurah(Context context, int surahNumber)` – return all of the ayahs of the specified Surah.
* `QuranDatabaseHelper.getAyah(Context context, int surahNumber, int ayahNumber)` – returns the text of the specified Ayah.

We advise calling all of the above methods (especially the `QuranDatabaseHelper.openDatabase(Context)` method) in a background thread since the methods are accessing file storage.
