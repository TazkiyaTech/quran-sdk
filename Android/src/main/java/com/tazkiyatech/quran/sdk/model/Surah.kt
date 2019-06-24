package com.tazkiyatech.quran.sdk.model

/**
 * An enum representation of the Surahs that make up the Quran.
 *
 * @property surahNumber The number (not index) of this Surah.
 * @property numVerses The number of Verses/Ayahs in this Surah.
 */
@Deprecated("Use the QuranDatabase.getMetadataForSectionType(SectionType) and QuranDatabase.getMetadataForSection(SectionType, int) methods instead.")
enum class Surah(
    val surahNumber: Int,
    val numVerses: Int
) {
    Surah_001(1, 7),
    Surah_002(2, 286),
    Surah_003(3, 200),
    Surah_004(4, 176),
    Surah_005(5, 120),
    Surah_006(6, 165),
    Surah_007(7, 206),
    Surah_008(8, 75),
    Surah_009(9, 129),
    Surah_010(10, 109),
    Surah_011(11, 123),
    Surah_012(12, 111),
    Surah_013(13, 43),
    Surah_014(14, 52),
    Surah_015(15, 99),
    Surah_016(16, 128),
    Surah_017(17, 111),
    Surah_018(18, 110),
    Surah_019(19, 98),
    Surah_020(20, 135),
    Surah_021(21, 112),
    Surah_022(22, 78),
    Surah_023(23, 118),
    Surah_024(24, 64),
    Surah_025(25, 77),
    Surah_026(26, 227),
    Surah_027(27, 93),
    Surah_028(28, 88),
    Surah_029(29, 69),
    Surah_030(30, 60),
    Surah_031(31, 34),
    Surah_032(32, 30),
    Surah_033(33, 73),
    Surah_034(34, 54),
    Surah_035(35, 45),
    Surah_036(36, 83),
    Surah_037(37, 182),
    Surah_038(38, 88),
    Surah_039(39, 75),
    Surah_040(40, 85),
    Surah_041(41, 54),
    Surah_042(42, 53),
    Surah_043(43, 89),
    Surah_044(44, 59),
    Surah_045(45, 37),
    Surah_046(46, 35),
    Surah_047(47, 38),
    Surah_048(48, 29),
    Surah_049(49, 18),
    Surah_050(50, 45),
    Surah_051(51, 60),
    Surah_052(52, 49),
    Surah_053(53, 62),
    Surah_054(54, 55),
    Surah_055(55, 78),
    Surah_056(56, 96),
    Surah_057(57, 29),
    Surah_058(58, 22),
    Surah_059(59, 24),
    Surah_060(60, 13),
    Surah_061(61, 14),
    Surah_062(62, 11),
    Surah_063(63, 11),
    Surah_064(64, 18),
    Surah_065(65, 12),
    Surah_066(66, 12),
    Surah_067(67, 30),
    Surah_068(68, 52),
    Surah_069(69, 52),
    Surah_070(70, 44),
    Surah_071(71, 28),
    Surah_072(72, 28),
    Surah_073(73, 20),
    Surah_074(74, 56),
    Surah_075(75, 40),
    Surah_076(76, 31),
    Surah_077(77, 50),
    Surah_078(78, 40),
    Surah_079(79, 46),
    Surah_080(80, 42),
    Surah_081(81, 29),
    Surah_082(82, 19),
    Surah_083(83, 36),
    Surah_084(84, 25),
    Surah_085(85, 22),
    Surah_086(86, 17),
    Surah_087(87, 19),
    Surah_088(88, 26),
    Surah_089(89, 30),
    Surah_090(90, 20),
    Surah_091(91, 15),
    Surah_092(92, 21),
    Surah_093(93, 11),
    Surah_094(94, 8),
    Surah_095(95, 8),
    Surah_096(96, 19),
    Surah_097(97, 5),
    Surah_098(98, 8),
    Surah_099(99, 8),
    Surah_100(100, 11),
    Surah_101(101, 11),
    Surah_102(102, 8),
    Surah_103(103, 3),
    Surah_104(104, 9),
    Surah_105(105, 5),
    Surah_106(106, 4),
    Surah_107(107, 7),
    Surah_108(108, 3),
    Surah_109(109, 6),
    Surah_110(110, 3),
    Surah_111(111, 5),
    Surah_112(112, 4),
    Surah_113(113, 5),
    Surah_114(114, 6);

    companion object {

        /**
         * Parses the given Surah number to an instance of [Surah].
         *
         * @param surahNumber is a value between 1 and 114 (inclusive).
         * @return the [Surah] corresponding to the [surahNumber] provided.
         */
        @JvmStatic
        fun parse(surahNumber: Int): Surah {
            val values = values()

            return if (surahNumber < 1 || surahNumber > values.size) {
                throw IllegalArgumentException("Bad Surah number ($surahNumber) passed in.")
            } else {
                values[surahNumber - 1]
            }
        }
    }
}
