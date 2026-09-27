package com.omran.quran.util

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

private val ar = Locale("ar")

enum class WeekDay(val id: String, val arabicName: String, val calendarDay: Int) {
    SATURDAY("SATURDAY", "السبت", Calendar.SATURDAY),
    SUNDAY("SUNDAY", "الأحد", Calendar.SUNDAY),
    MONDAY("MONDAY", "الإثنين", Calendar.MONDAY),
    TUESDAY("TUESDAY", "الثلاثاء", Calendar.TUESDAY),
    WEDNESDAY("WEDNESDAY", "الأربعاء", Calendar.WEDNESDAY),
    THURSDAY("THURSDAY", "الخميس", Calendar.THURSDAY),
    FRIDAY("FRIDAY", "الجمعة", Calendar.FRIDAY);

    companion object {
        val DEFAULT_WORKING_DAYS = setOf(
            SATURDAY.id, SUNDAY.id, MONDAY.id, TUESDAY.id, WEDNESDAY.id, THURSDAY.id,
        )

        fun fromDayKey(key: String): WeekDay {
            val millis = dayKeyToMillis(key)
            val cal = Calendar.getInstance().apply { timeInMillis = millis }
            val calDay = cal.get(Calendar.DAY_OF_WEEK)
            return entries.find { it.calendarDay == calDay } ?: SATURDAY
        }
    }
}

fun Long.toDateText(): String =
    SimpleDateFormat("EEEE d MMMM yyyy", ar).format(Date(this))

fun Long.toDayKey(): String =
    SimpleDateFormat("yyyy-MM-dd", Locale.US).format(Date(this))

fun todayKey(): String = System.currentTimeMillis().toDayKey()

fun startOfDay(millis: Long = System.currentTimeMillis()): Long {
    val cal = Calendar.getInstance()
    cal.timeInMillis = millis
    cal.set(Calendar.HOUR_OF_DAY, 0)
    cal.set(Calendar.MINUTE, 0)
    cal.set(Calendar.SECOND, 0)
    cal.set(Calendar.MILLISECOND, 0)
    return cal.timeInMillis
}

fun endOfDay(millis: Long = System.currentTimeMillis()): Long =
    startOfDay(millis) + 24L * 60 * 60 * 1000 - 1

fun dayKeyToMillis(key: String): Long =
    SimpleDateFormat("yyyy-MM-dd", Locale.US).parse(key)?.time ?: System.currentTimeMillis()

fun displayDayKey(key: String): String = dayKeyToMillis(key).toDateText()
