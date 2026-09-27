package com.omran.quran.util

import java.time.DayOfWeek
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.chrono.HijrahDate
import java.time.temporal.ChronoField

/**
 * مساعد حساب وتنسيق التقويم والتاريخ الهجري المعتمد على تقويم أم القرى (Umm al-Qura)
 */
object HijriDateHelper {

    private val HIJRI_MONTHS = listOf(
        "محرم", "صفر", "ربيع الأول", "ربيع الآخر",
        "جمادى الأولى", "جمادى الآخرة", "رجب", "شعبان",
        "رمضان", "شوال", "ذو القعدة", "ذو الحجة"
    )

    private val ARABIC_DAYS = mapOf(
        DayOfWeek.SATURDAY to "السبت",
        DayOfWeek.SUNDAY to "الأحد",
        DayOfWeek.MONDAY to "الإثنين",
        DayOfWeek.TUESDAY to "الثلاثاء",
        DayOfWeek.WEDNESDAY to "الأربعاء",
        DayOfWeek.THURSDAY to "الخميس",
        DayOfWeek.FRIDAY to "الجمعة"
    )

    private val GREGORIAN_MONTHS = listOf(
        "يناير", "فبراير", "مارس", "أبريل", "مايو", "يونيو",
        "يوليو", "أغسطس", "سبتمبر", "أكتوبر", "نوفمبر", "ديسمبر"
    )

    /** يعيد التاريخ الهجري لليوم الحالي، مثال: "السبت، 8 ربيع الآخر 1448 هـ" */
    fun getTodayHijri(includeDayName: Boolean = true): String {
        return formatHijri(LocalDate.now(), includeDayName)
    }

    /** يعيد التاريخ الميلادي لليوم الحالي بالعربية، مثال: "19 سبتمبر 2026 م" */
    fun getTodayGregorian(includeDayName: Boolean = false): String {
        return formatGregorian(LocalDate.now(), includeDayName)
    }

    /** يعيد نصاً مدمجاً يجمع التاريخين الهجري والميلادي لليوم الحالي */
    fun getTodayCombined(): String {
        val today = LocalDate.now()
        val hijri = formatHijri(today, includeDayName = true)
        val gregorian = formatGregorian(today, includeDayName = false)
        return "$hijri  •  $gregorian"
    }

    /** تحويل تاريخ محدد (LocalDate) إلى تاريخ هجري منسق */
    fun formatHijri(localDate: LocalDate, includeDayName: Boolean = true): String {
        return try {
            val hijrahDate = HijrahDate.from(localDate)
            val day = hijrahDate.get(ChronoField.DAY_OF_MONTH)
            val month = hijrahDate.get(ChronoField.MONTH_OF_YEAR)
            val year = hijrahDate.get(ChronoField.YEAR)

            val monthName = HIJRI_MONTHS.getOrElse(month - 1) { "شهر $month" }
            val dayName = if (includeDayName) "${ARABIC_DAYS[localDate.dayOfWeek] ?: ""}، " else ""

            "$dayName$day $monthName $year هـ"
        } catch (e: Exception) {
            ""
        }
    }

    /** تحويل نص تاريخ بصيغة yyyy-MM-dd أو ما يماثلها إلى تاريخ هجري */
    fun formatHijriFromDateString(dateStr: String, includeDayName: Boolean = false): String {
        return try {
            val cleanStr = dateStr.trim().take(10) // يأخذ yyyy-MM-dd في حال وجد توقيت
            val localDate = LocalDate.parse(cleanStr)
            formatHijri(localDate, includeDayName)
        } catch (e: Exception) {
            ""
        }
    }

    /** تحويل طابع زمني بالميلي ثانية إلى تاريخ هجري */
    fun formatHijriFromMillis(millis: Long, includeDayName: Boolean = false): String {
        return try {
            val localDate = Instant.ofEpochMilli(millis)
                .atZone(ZoneId.systemDefault())
                .toLocalDate()
            formatHijri(localDate, includeDayName)
        } catch (e: Exception) {
            ""
        }
    }

    /** تنسيق التاريخ الميلادي بالعربية، مثال: "19 سبتمبر 2026 م" */
    fun formatGregorian(localDate: LocalDate, includeDayName: Boolean = false): String {
        val day = localDate.dayOfMonth
        val monthName = GREGORIAN_MONTHS.getOrElse(localDate.monthValue - 1) { "${localDate.monthValue}" }
        val year = localDate.year
        val dayName = if (includeDayName) "${ARABIC_DAYS[localDate.dayOfWeek] ?: ""}، " else ""
        return "$dayName$day $monthName $year م"
    }
}
