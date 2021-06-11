package com.tazkiyatech.quran.sdk.database

import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.tazkiyatech.quran.sdk.exception.QuranDatabaseException
import com.tazkiyatech.quran.sdk.model.SectionType
import org.hamcrest.collection.IsIterableContainingInOrder.contains
import org.hamcrest.core.Is.`is`
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class QuranDatabaseTests {

    private lateinit var quranDatabase: QuranDatabase

    @Before
    fun setUp() {
        quranDatabase = QuranDatabase(ApplicationProvider.getApplicationContext())
    }

    @After
    fun tearDown() {
        quranDatabase.closeDatabase()
    }

    @Test
    fun isFileExistsInInternalStorage_when_database_not_opened() {
        // When.
        val result = quranDatabase.isFileExistsInInternalStorage(QuranDatabase.DATABASE_NAME)

        // Then.
        assertFalse(result)
    }

    @Test
    fun isFileExistsInInternalStorage_when_database_opened() {
        // Given.
        quranDatabase.openDatabase()

        // When.
        val result = quranDatabase.isFileExistsInInternalStorage(QuranDatabase.DATABASE_NAME)

        // Then.
        assertTrue(result)
    }

    @Test
    fun isDatabaseOpen_when_database_not_opened() {
        // When.
        val result = quranDatabase.isDatabaseOpen()

        // Then.
        assertFalse(result)
    }

    @Test
    fun isDatabaseOpen_when_database_opened() {
        // Given.
        quranDatabase.openDatabase()

        // When.
        val result = quranDatabase.isDatabaseOpen()

        // Then.
        assertTrue(result)
    }

    @Test
    fun openDatabase_on_two_separate_instances() {
        // Given.
        QuranDatabase(ApplicationProvider.getApplicationContext()).openDatabase()

        // When.
        quranDatabase.openDatabase()

        // Then.
        assertTrue(quranDatabase.isDatabaseOpen())
    }

    @Test
    fun getNameOfSurah_with_surah_number_1() {
        // When.
        val surahName = quranDatabase.getNameOfSurah(1)

        // Then.
        assertThat(surahName, `is`("الفاتحة"))
    }

    @Test(expected = QuranDatabaseException::class)
    fun getNameOfSurah_with_invalid_surah_number() {
        // When.
        quranDatabase.getNameOfSurah(115)
    }

    @Test
    fun getSurahNames() {
        // Given.
        val expected = arrayOf(
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
        )

        // When.
        val actual = quranDatabase.getSurahNames()

        // Then.
        assertThat(actual, contains(*expected))
    }

    @Test
    fun getAyahsInSurah_for_each_and_every_surah() {
        // Given.
        val surahMetadataList = quranDatabase.getMetadataForSectionsOfType(SectionType.SURAH)

        assertEquals(114, surahMetadataList.size)

        for (surahMetadata in surahMetadataList) {
            // Given.
            val expectedNumberOfVerses = surahMetadata.numAyahs

            // When.
            val actualNumberOfVerses = quranDatabase.getAyahsInSurah(surahMetadata.sectionNumber).size

            // Then.
            assertEquals(expectedNumberOfVerses, actualNumberOfVerses)
        }
    }

    @Test
    fun getAyahsInSurah_with_valid_surah_number() {
        // Given.
        val expected = arrayOf(
            "بِسْمِ اللَّهِ الرَّحْمَٰنِ الرَّحِيمِ",
            "الْحَمْدُ لِلَّهِ رَبِّ الْعَالَمِينَ",
            "الرَّحْمَٰنِ الرَّحِيمِ",
            "مَالِكِ يَوْمِ الدِّينِ",
            "إِيَّاكَ نَعْبُدُ وَإِيَّاكَ نَسْتَعِينُ",
            "اهْدِنَا الصِّرَاطَ الْمُسْتَقِيمَ",
            "صِرَاطَ الَّذِينَ أَنْعَمْتَ عَلَيْهِمْ غَيْرِ الْمَغْضُوبِ عَلَيْهِمْ وَلَا الضَّالِّينَ"
        )

        // When.
        val actual = quranDatabase.getAyahsInSurah(1)

        // Then.
        assertThat(actual, contains(*expected))
    }

    @Test(expected = QuranDatabaseException::class)
    fun getAyahsInSurah_with_invalid_surah_number() {
        quranDatabase.getAyahsInSurah(115)
    }

    @Test
    fun getAyah_with_surah_number_1_and_ayah_number_1() {
        // Given.
        val expected = "بِسْمِ اللَّهِ الرَّحْمَٰنِ الرَّحِيمِ"

        // When.
        val actual = quranDatabase.getAyah(1, 1)

        // Then.
        assertEquals(expected, actual)
    }

    @Test
    fun getAyah_with_surah_number_58_and_ayah_number_6() {
        // Given.
        val expected =
            "يَوْمَ يَبْعَثُهُمُ اللَّهُ جَمِيعًا فَيُنَبِّئُهُمْ بِمَا عَمِلُوا ۚ أَحْصَاهُ اللَّهُ وَنَسُوهُ ۚ وَاللَّهُ عَلَىٰ كُلِّ شَيْءٍ شَهِيدٌ"

        // When.
        val actual = quranDatabase.getAyah(58, 6)

        // Then.
        assertEquals(expected, actual)
    }

    @Test(expected = QuranDatabaseException::class)
    fun getAyah_with_invalid_surah_number() {
        // When.
        quranDatabase.getAyah(115, 1)
    }

    @Test(expected = QuranDatabaseException::class)
    fun getAyah_with_invalid_ayah_number() {
        // When.
        quranDatabase.getAyah(1, 8)
    }
}
