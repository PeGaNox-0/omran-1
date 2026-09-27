package com.omran.quran.util

data class SurahInfo(
    val number: Int,
    val name: String,
    val verseCount: Int,
    val startJuz: Int,
    val endJuz: Int = startJuz,
)

data class JuzInfo(
    val number: Int,
    val name: String,
    val startDescription: String,
    val surahs: List<String>,
    val description: String,
)

data class JuzProgress(
    val juzNumber: Int,
    val juzName: String,
    val totalSurahs: Int,
    val completedSurahsCount: Int,
    val isCompleted: Boolean,
    val completedSurahNames: List<String>,
    val missingSurahNames: List<String>,
)

data class QuranProgress(
    val ajzaaCount: Int,
    val ajzaaList: List<String>,
    val surahsCount: Int,
    val surahsList: List<String>,
    val completionPercentage: Int,
    val juzBreakdown: List<JuzProgress> = emptyList(),
)

object QuranData {
    val surahs: List<SurahInfo> = listOf(
        SurahInfo(1, "الفاتحة", 7, 1, 1),
        SurahInfo(2, "البقرة", 286, 1, 3),
        SurahInfo(3, "آل عمران", 200, 3, 4),
        SurahInfo(4, "النساء", 176, 4, 6),
        SurahInfo(5, "المائدة", 120, 6, 7),
        SurahInfo(6, "الأنعام", 165, 7, 8),
        SurahInfo(7, "الأعراف", 206, 8, 9),
        SurahInfo(8, "الأنفال", 75, 9, 10),
        SurahInfo(9, "التوبة", 129, 10, 11),
        SurahInfo(10, "يونس", 109, 11, 11),
        SurahInfo(11, "هود", 123, 11, 12),
        SurahInfo(12, "يوسف", 111, 12, 13),
        SurahInfo(13, "الرعد", 43, 13, 13),
        SurahInfo(14, "إبراهيم", 52, 13, 13),
        SurahInfo(15, "الحجر", 99, 14, 14),
        SurahInfo(16, "النحل", 128, 14, 14),
        SurahInfo(17, "الإسراء", 111, 15, 15),
        SurahInfo(18, "الكهف", 110, 15, 16),
        SurahInfo(19, "مريم", 98, 16, 16),
        SurahInfo(20, "طه", 135, 16, 16),
        SurahInfo(21, "الأنبياء", 112, 17, 17),
        SurahInfo(22, "الحج", 78, 17, 17),
        SurahInfo(23, "المؤمنون", 118, 18, 18),
        SurahInfo(24, "النور", 64, 18, 18),
        SurahInfo(25, "الفرقان", 77, 18, 19),
        SurahInfo(26, "الشعراء", 227, 19, 19),
        SurahInfo(27, "النمل", 93, 19, 20),
        SurahInfo(28, "القصص", 88, 20, 20),
        SurahInfo(29, "العنكبوت", 69, 20, 21),
        SurahInfo(30, "الروم", 60, 21, 21),
        SurahInfo(31, "لقمان", 34, 21, 21),
        SurahInfo(32, "السجدة", 30, 21, 21),
        SurahInfo(33, "الأحزاب", 73, 21, 22),
        SurahInfo(34, "سبأ", 54, 22, 22),
        SurahInfo(35, "فاطر", 45, 22, 22),
        SurahInfo(36, "يس", 83, 22, 23),
        SurahInfo(37, "الصافات", 182, 23, 23),
        SurahInfo(38, "ص", 88, 23, 23),
        SurahInfo(39, "الزمر", 75, 23, 24),
        SurahInfo(40, "غافر", 85, 24, 24),
        SurahInfo(41, "فصلت", 54, 24, 25),
        SurahInfo(42, "الشورى", 53, 25, 25),
        SurahInfo(43, "الزخرف", 89, 25, 25),
        SurahInfo(44, "الدخان", 59, 25, 25),
        SurahInfo(45, "الجاثية", 37, 25, 25),
        SurahInfo(46, "الأحقاف", 35, 26, 26),
        SurahInfo(47, "محمد", 38, 26, 26),
        SurahInfo(48, "الفتح", 29, 26, 26),
        SurahInfo(49, "الحجرات", 18, 26, 26),
        SurahInfo(50, "ق", 45, 26, 26),
        SurahInfo(51, "الذاريات", 60, 26, 27),
        SurahInfo(52, "الطور", 49, 27, 27),
        SurahInfo(53, "النجم", 62, 27, 27),
        SurahInfo(54, "القمر", 55, 27, 27),
        SurahInfo(55, "الرحمن", 78, 27, 27),
        SurahInfo(56, "الواقعة", 96, 27, 27),
        SurahInfo(57, "الحديد", 29, 27, 27),
        SurahInfo(58, "المجادلة", 22, 28, 28),
        SurahInfo(59, "الحشر", 24, 28, 28),
        SurahInfo(60, "الممتحنة", 13, 28, 28),
        SurahInfo(61, "الصف", 14, 28, 28),
        SurahInfo(62, "الجمعة", 11, 28, 28),
        SurahInfo(63, "المنافقون", 11, 28, 28),
        SurahInfo(64, "التغابن", 18, 28, 28),
        SurahInfo(65, "الطلاق", 12, 28, 28),
        SurahInfo(66, "التحريم", 12, 28, 28),
        SurahInfo(67, "الملك", 30, 29, 29),
        SurahInfo(68, "القلم", 52, 29, 29),
        SurahInfo(69, "الحاقة", 52, 29, 29),
        SurahInfo(70, "المعارج", 44, 29, 29),
        SurahInfo(71, "نوح", 28, 29, 29),
        SurahInfo(72, "الجن", 28, 29, 29),
        SurahInfo(73, "المزمل", 20, 29, 29),
        SurahInfo(74, "المدثر", 56, 29, 29),
        SurahInfo(75, "القيامة", 40, 29, 29),
        SurahInfo(76, "الإنسان", 31, 29, 29),
        SurahInfo(77, "المرسلات", 50, 29, 29),
        SurahInfo(78, "النبأ", 40, 30, 30),
        SurahInfo(79, "النازعات", 46, 30, 30),
        SurahInfo(80, "عبس", 42, 30, 30),
        SurahInfo(81, "التكوير", 29, 30, 30),
        SurahInfo(82, "الانفطار", 19, 30, 30),
        SurahInfo(83, "المطففين", 36, 30, 30),
        SurahInfo(84, "الانشقاق", 25, 30, 30),
        SurahInfo(85, "البروج", 22, 30, 30),
        SurahInfo(86, "الطارق", 17, 30, 30),
        SurahInfo(87, "الأعلى", 19, 30, 30),
        SurahInfo(88, "الغاشية", 26, 30, 30),
        SurahInfo(89, "الفجر", 30, 30, 30),
        SurahInfo(90, "البلد", 20, 30, 30),
        SurahInfo(91, "الشمس", 15, 30, 30),
        SurahInfo(92, "الليل", 21, 30, 30),
        SurahInfo(93, "الضحى", 11, 30, 30),
        SurahInfo(94, "الشرح", 8, 30, 30),
        SurahInfo(95, "التين", 8, 30, 30),
        SurahInfo(96, "العلق", 19, 30, 30),
        SurahInfo(97, "القدر", 5, 30, 30),
        SurahInfo(98, "البينة", 8, 30, 30),
        SurahInfo(99, "الزلزلة", 8, 30, 30),
        SurahInfo(100, "العاديات", 11, 30, 30),
        SurahInfo(101, "القارعة", 11, 30, 30),
        SurahInfo(102, "التكاثر", 8, 30, 30),
        SurahInfo(103, "العصر", 3, 30, 30),
        SurahInfo(104, "الهمزة", 9, 30, 30),
        SurahInfo(105, "الفيل", 5, 30, 30),
        SurahInfo(106, "قريش", 4, 30, 30),
        SurahInfo(107, "الماعون", 7, 30, 30),
        SurahInfo(108, "الكوثر", 3, 30, 30),
        SurahInfo(109, "الكافرون", 6, 30, 30),
        SurahInfo(110, "النصر", 3, 30, 30),
        SurahInfo(111, "المسد", 5, 30, 30),
        SurahInfo(112, "الإخلاص", 4, 30, 30),
        SurahInfo(113, "الفلق", 5, 30, 30),
        SurahInfo(114, "الناس", 6, 30, 30),
    )

    val surahNames: List<String> = surahs.map { it.name }

    val juzList: List<JuzInfo> = listOf(
        JuzInfo(1, "الجزء 1", "الجزء 1 (الم / الفاتحة والبقرة)", listOf("الفاتحة", "البقرة"), "سورة الفاتحة وسورة البقرة (1-141)"),
        JuzInfo(2, "الجزء 2", "الجزء 2 (سيقول السفهاء)", listOf("البقرة"), "سورة البقرة (142-252)"),
        JuzInfo(3, "الجزء 3", "الجزء 3 (تلك الرسل)", listOf("البقرة", "آل عمران"), "سورة البقرة (253-286) وسورة آل عمران (1-92)"),
        JuzInfo(4, "الجزء 4", "الجزء 4 (لن تنالوا البر)", listOf("آل عمران", "النساء"), "سورة آل عمران (93-200) وسورة النساء (1-23)"),
        JuzInfo(5, "الجزء 5", "الجزء 5 (والمحصنات)", listOf("النساء"), "سورة النساء (24-147)"),
        JuzInfo(6, "الجزء 6", "الجزء 6 (لا يحب الله)", listOf("النساء", "المائدة"), "سورة النساء (148-176) وسورة المائدة (1-81)"),
        JuzInfo(7, "الجزء 7", "الجزء 7 (وإذا سمعوا)", listOf("المائدة", "الأنعام"), "سورة المائدة (82-120) وسورة الأنعام (1-110)"),
        JuzInfo(8, "الجزء 8", "الجزء 8 (ولو أننا)", listOf("الأنعام", "الأعراف"), "سورة الأنعام (111-165) وسورة الأعراف (1-87)"),
        JuzInfo(9, "الجزء 9", "الجزء 9 (قال الملأ)", listOf("الأعراف", "الأنفال"), "سورة الأعراف (88-206) وسورة الأنفال (1-40)"),
        JuzInfo(10, "الجزء 10", "الجزء 10 (واعلموا)", listOf("الأنفال", "التوبة"), "سورة الأنفال (41-75) وسورة التوبة (1-92)"),
        JuzInfo(11, "الجزء 11", "الجزء 11 (يعتذرون)", listOf("التوبة", "يونس", "هود"), "سورة التوبة (93-129) وسورة يونس وسورة هود (1-5)"),
        JuzInfo(12, "الجزء 12", "الجزء 12 (وما من دابة)", listOf("هود", "يوسف"), "سورة هود (6-123) وسورة يوسف (1-52)"),
        JuzInfo(13, "الجزء 13", "الجزء 13 (وما أبرئ نفسي)", listOf("يوسف", "الرعد", "إبراهيم"), "سورة يوسف (53-111) والرعد وإبراهيم"),
        JuzInfo(14, "الجزء 14", "الجزء 14 (ربما)", listOf("الحجر", "النحل"), "سورة الحجر وسورة النحل كاملاً"),
        JuzInfo(15, "الجزء 15", "الجزء 15 (سبحان الذي)", listOf("الإسراء", "الكهف"), "سورة الإسراء وسورة الكهف (1-74)"),
        JuzInfo(16, "الجزء 16", "الجزء 16 (قال ألم)", listOf("الكهف", "مريم", "طه"), "سورة الكهف (75-110) ومريم وطه"),
        JuzInfo(17, "الجزء 17", "الجزء 17 (اقترب للناس)", listOf("الأنبياء", "الحج"), "سورة الأنبياء وسورة الحج كاملاً"),
        JuzInfo(18, "الجزء 18", "الجزء 18 (قد أفلح)", listOf("المؤمنون", "النور", "الفرقان"), "سورة المؤمنون والنور والفرقان (1-20)"),
        JuzInfo(19, "الجزء 19", "الجزء 19 (وقال الذين)", listOf("الفرقان", "الشعراء", "النمل"), "سورة الفرقان (21-77) والشعراء والنمل (1-55)"),
        JuzInfo(20, "الجزء 20", "الجزء 20 (أمن خلق)", listOf("النمل", "القصص", "العنكبوت"), "سورة النمل (56-93) والقصص والعنكبوت (1-45)"),
        JuzInfo(21, "الجزء 21", "الجزء 21 (اتل ما أوحي)", listOf("العنكبوت", "الروم", "لقمان", "السجدة", "الأحزاب"), "سورة العنكبوت والروم ولقمان والسجدة والأحزاب (1-30)"),
        JuzInfo(22, "الجزء 22", "الجزء 22 (ومن يقنت)", listOf("الأحزاب", "سبأ", "فاطر", "يس"), "سورة الأحزاب (31-73) وسبأ وفاطر ويس (1-27)"),
        JuzInfo(23, "الجزء 23", "الجزء 23 (وما أنزلنا)", listOf("يس", "الصافات", "ص", "الزمر"), "سورة يس (28-83) والصافات وص والزمر (1-31)"),
        JuzInfo(24, "الجزء 24", "الجزء 24 (فمن أظلم)", listOf("الزمر", "غافر", "فصلت"), "سورة الزمر (32-75) وغافر وفصلت (1-46)"),
        JuzInfo(25, "الجزء 25", "الجزء 25 (إليه يرد)", listOf("فصلت", "الشورى", "الزخرف", "الدخان", "الجاثية"), "سورة فصلت (47-54) والشورى والزخرف والدخان والجاثية"),
        JuzInfo(26, "الجزء 26", "الجزء 26 (حم / الأحقاف)", listOf("الأحقاف", "محمد", "الفتح", "الحجرات", "ق", "الذاريات"), "سورة الأحقاف ومحمد والفتح والحجرات وق والذاريات (1-30)"),
        JuzInfo(27, "الجزء 27", "الجزء 27 (قال فما خطبكم)", listOf("الذاريات", "الطور", "النجم", "القمر", "الرحمن", "الواقعة", "الحديد"), "سورة الذاريات والطور والنجم والقمر والرحمن والواقعة والحديد"),
        JuzInfo(28, "الجزء 28", "الجزء 28 (قد سمع الله)", listOf("المجادلة", "الحشر", "الممتحنة", "الصف", "الجمعة", "المنافقون", "التغابن", "الطلاق", "التحريم"), "9 سور: المجادلة، الحشر، الممتحنة، الصف، الجمعة، المنافقون، التغابن، الطلاق، التحريم"),
        JuzInfo(29, "الجزء 29", "الجزء 29 (تبارك)", listOf("الملك", "القلم", "الحاقة", "المعارج", "نوح", "الجن", "المزمل", "المدثر", "القيامة", "الإنسان", "المرسلات"), "11 سورة: الملك، القلم، الحاقة، المعارج، نوح، الجن، المزمل، المدثر، القيامة، الإنسان، المرسلات"),
        JuzInfo(30, "الجزء 30", "الجزء 30 (عمّ)", listOf("النبأ", "النازعات", "عبس", "التكوير", "الانفطار", "المطففين", "الانشقاق", "البروج", "الطارق", "الأعلى", "الغاشية", "الفجر", "البلد", "الشمس", "الليل", "الضحى", "الشرح", "التين", "العلق", "القدر", "البينة", "الزلزلة", "العاديات", "القارعة", "التكاثر", "العصر", "الهمزة", "الفيل", "قريش", "الماعون", "الكوثر", "الكافرون", "النصر", "المسد", "الإخلاص", "الفلق", "الناس"), "37 سورة: من سورة النبأ إلى سورة الناس"),
    )

    val juzNames: List<String> = juzList.map { it.startDescription }

    fun getSurah(name: String): SurahInfo? =
        surahs.find { it.name.equals(name.trim(), ignoreCase = true) || "سورة ${it.name}" == name.trim() }

    fun getVerseCount(surahName: String): Int =
        getSurah(surahName)?.verseCount ?: 286

    fun getJuz(number: Int): JuzInfo? =
        juzList.find { it.number == number }

    fun extractJuzNumber(text: String): Int? {
        val trimmed = text.trim()
        val regex = Regex("""الجزء\s*(\d+)""")
        val match = regex.find(trimmed)
        if (match != null) {
            return match.groupValues[1].toIntOrNull()
        }
        val directNumber = trimmed.toIntOrNull()
        if (directNumber != null && directNumber in 1..30) return directNumber
        return null
    }

    fun isJuzEntry(text: String): Boolean {
        val trimmed = text.trim()
        return trimmed.startsWith("الجزء") || extractJuzNumber(trimmed) != null
    }

    /**
     * حساب تقدم الطالب القرآني وتفصيل الأجزاء والسور بدقة
     */
    fun calculateQuranProgress(
        rawEntries: List<String>,
    ): QuranProgress {
        val completedAjzaa = mutableSetOf<Int>()
        val completedSurahs = mutableSetOf<String>()

        for (entry in rawEntries) {
            val t = entry.trim()
            if (t.isBlank()) continue

            val juzNum = extractJuzNumber(t)
            if (juzNum != null && juzNum in 1..30) {
                completedAjzaa.add(juzNum)
                val jInfo = getJuz(juzNum)
                if (jInfo != null) {
                    completedSurahs.addAll(jInfo.surahs)
                }
            } else {
                val cleanSurah = t.removePrefix("سورة ")
                    .substringBefore(" (")
                    .substringBefore(" كاملة")
                    .substringBefore(":")
                    .trim()
                val found = getSurah(cleanSurah)
                if (found != null) {
                    completedSurahs.add(found.name)
                }
            }
        }

        val breakdown = juzList.map { juz ->
            val completedInJuz = juz.surahs.filter { completedSurahs.contains(it) }
            val missingInJuz = juz.surahs.filterNot { completedSurahs.contains(it) }
            val isComplete = completedAjzaa.contains(juz.number) || (juz.surahs.isNotEmpty() && missingInJuz.isEmpty())

            if (isComplete) {
                completedAjzaa.add(juz.number)
                completedSurahs.addAll(juz.surahs)
            }

            JuzProgress(
                juzNumber = juz.number,
                juzName = juz.name,
                totalSurahs = juz.surahs.size,
                completedSurahsCount = if (isComplete) juz.surahs.size else completedInJuz.size,
                isCompleted = isComplete,
                completedSurahNames = if (isComplete) juz.surahs else completedInJuz,
                missingSurahNames = if (isComplete) emptyList() else missingInJuz,
            )
        }

        if (completedAjzaa.size == 30) {
            completedSurahs.addAll(surahs.map { it.name })
        }

        val ajzaaNames = completedAjzaa.sorted().map { "الجزء $it" }
        val surahsSorted = completedSurahs.sortedBy { surahName ->
            surahs.find { it.name == surahName }?.number ?: 999
        }

        val percentage = ((completedAjzaa.size.toDouble() / 30.0) * 100).toInt().coerceIn(0, 100)

        return QuranProgress(
            ajzaaCount = completedAjzaa.size,
            ajzaaList = ajzaaNames,
            surahsCount = surahsSorted.size,
            surahsList = surahsSorted,
            completionPercentage = percentage,
            juzBreakdown = breakdown,
        )
    }

    fun formatQuranTargetName(name: String): String {
        val t = name.trim()
        if (t.isBlank()) return ""
        if (t.startsWith("الجزء") || t.contains("جزء")) return t
        if (t.startsWith("سورة ")) return t
        return "سورة $t"
    }
}
