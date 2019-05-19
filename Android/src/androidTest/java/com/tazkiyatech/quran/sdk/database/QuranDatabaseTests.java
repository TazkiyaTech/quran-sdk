package com.tazkiyatech.quran.sdk.database;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.tazkiyatech.quran.sdk.exception.QuranDatabaseException;
import com.tazkiyatech.quran.sdk.model.ChapterMetadata;
import com.tazkiyatech.quran.sdk.model.ChapterType;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

import static org.hamcrest.collection.IsIterableContainingInOrder.contains;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNot.not;
import static org.hamcrest.core.IsNull.nullValue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

@RunWith(AndroidJUnit4.class)
public class QuranDatabaseTests {

    private QuranDatabase quranDatabase;

    @Before
    public void setUp() {
        quranDatabase = new QuranDatabase(ApplicationProvider.getApplicationContext());

        boolean deleteResult = quranDatabase.deleteFileInInternalStorage(QuranDatabase.DATABASE_NAME);

        assertTrue("Failed deleting the Quran database file as part of the test setup", deleteResult);
    }

    @After
    public void tearDown() {
        quranDatabase.closeDatabase();
    }

    @Test
    public void isFileExistsInInternalStorage_when_database_not_opened() {
        // When.
        boolean result = quranDatabase.isFileExistsInInternalStorage(QuranDatabase.DATABASE_NAME);

        // Then.
        assertFalse(result);
    }

    @Test
    public void isFileExistsInInternalStorage_when_database_opened() {
        // Given.
        quranDatabase.openDatabase();

        // When.
        boolean result = quranDatabase.isFileExistsInInternalStorage(QuranDatabase.DATABASE_NAME);

        // Then.
        assertTrue(result);
    }

    @Test
    public void isDatabaseOpen_when_database_not_opened() {
        // When.
        boolean result = quranDatabase.isDatabaseOpen();

        // Then.
        assertFalse(result);
    }

    @Test
    public void isDatabaseOpen_when_database_opened() {
        // Given.
        quranDatabase.openDatabase();

        // When.
        boolean result = quranDatabase.isDatabaseOpen();

        // Then.
        assertTrue(result);
    }

    @Test
    public void openDatabase_on_two_separate_instances() {
        // Given.
        new QuranDatabase(ApplicationProvider.getApplicationContext()).openDatabase();

        // When.
        quranDatabase.openDatabase();

        // Then.
        assertTrue(quranDatabase.isDatabaseOpen());
    }

    @Test
    public void getSurahName_with_surah_number_1() {
        // When.
        String surahName = quranDatabase.getSurahName(1);

        // Then.
        assertThat(surahName, is(not(nullValue())));
    }

    @Test(expected = QuranDatabaseException.class)
    public void getSurahName_with_invalid_surah_number() {
        // When.
        quranDatabase.getSurahName(115);
    }

    @Test
    public void getSurahNames() {
        // Given.
        String[] expected = {
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
        };

        // When.
        List<String> actual = quranDatabase.getSurahNames();

        // Then.
        assertThat(actual, contains(expected));
    }

    @Test
    public void getAyahsInSurah_for_each_and_every_surah() {
        // Given.
        List<ChapterMetadata> surahMetadataList = quranDatabase.getMetadataForChapterType(ChapterType.SURAH);

        assertEquals(114, surahMetadataList.size());

        for (ChapterMetadata surahMetadata : surahMetadataList) {
            // Given.
            int expectedNumberOfVerses = surahMetadata.getNumAyahs();

            // When.
            int actualNumberOfVerses = quranDatabase.getAyahsInSurah(surahMetadata.getChapterNumber()).size();

            // Then.
            assertEquals(expectedNumberOfVerses, actualNumberOfVerses);
        }
    }

    @Test
    public void getAyahsInSurah_with_valid_surah_number() {
        // Given.
        String[] expected = {
                "بِسْمِ اللَّهِ الرَّحْمَٰنِ الرَّحِيمِ",
                "الْحَمْدُ لِلَّهِ رَبِّ الْعَالَمِينَ",
                "الرَّحْمَٰنِ الرَّحِيمِ",
                "مَالِكِ يَوْمِ الدِّينِ",
                "إِيَّاكَ نَعْبُدُ وَإِيَّاكَ نَسْتَعِينُ",
                "اهْدِنَا الصِّرَاطَ الْمُسْتَقِيمَ",
                "صِرَاطَ الَّذِينَ أَنْعَمْتَ عَلَيْهِمْ غَيْرِ الْمَغْضُوبِ عَلَيْهِمْ وَلَا الضَّالِّينَ"
        };

        // When.
        List<String> actual = quranDatabase.getAyahsInSurah(1);

        // Then.
        assertThat(actual, contains(expected));
    }

    @Test(expected = QuranDatabaseException.class)
    public void getAyahsInSurah_with_invalid_surah_number() {
        quranDatabase.getAyahsInSurah(115);
    }

    @Test
    public void getAyah_with_surah_number_1_and_ayah_number_1() {
        // Given.
        String expected = "بِسْمِ اللَّهِ الرَّحْمَٰنِ الرَّحِيمِ";

        // When.
        String actual = quranDatabase.getAyah(1, 1);

        // Then.
        assertEquals(expected, actual);
    }

    @Test
    public void getAyah_with_surah_number_58_and_ayah_number_6() {
        // Given.
        String expected = "يَوْمَ يَبْعَثُهُمُ اللَّهُ جَمِيعًا فَيُنَبِّئُهُمْ بِمَا عَمِلُوا ۚ أَحْصَاهُ اللَّهُ وَنَسُوهُ ۚ وَاللَّهُ عَلَىٰ كُلِّ شَيْءٍ شَهِيدٌ";

        // When.
        String actual = quranDatabase.getAyah(58, 6);

        // Then.
        assertEquals(expected, actual);
    }

    @Test(expected = QuranDatabaseException.class)
    public void getAyah_with_invalid_surah_number() {
        // When.
        quranDatabase.getAyah(115, 1);
    }

    @Test(expected = QuranDatabaseException.class)
    public void getAyah_with_invalid_ayah_number() {
        // When.
        quranDatabase.getAyah(1, 8);
    }
}
