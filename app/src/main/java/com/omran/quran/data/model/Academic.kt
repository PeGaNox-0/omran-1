package com.omran.quran.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

enum class MemorizationCategory(val label: String, val shortLabel: String) {
    NEW("محفوظ جديد", "جديد"),
    OLD("محفوظ قديم / سابق", "سابق"),
    ;

    companion object {
        fun from(value: String?): MemorizationCategory =
            entries.find { it.name.equals(value, ignoreCase = true) || it.label == value || it.shortLabel == value }
                ?: NEW
    }
}

@Serializable
data class Memorization(
    @SerialName("id") val id: String = "",
    @SerialName("student_id") val studentId: String = "",
    @SerialName("student_name") val studentName: String = "",
    @SerialName("teacher_id") val teacherId: String = "",
    @SerialName("institute_id") val instituteId: String = "",
    @SerialName("surah") val surah: String = "",
    @SerialName("from_verse") val fromVerse: Int = 0,
    @SerialName("to_verse") val toVerse: Int = 0,
    @SerialName("amount") val amount: String = "",
    @SerialName("rating") val rating: String = "",
    @SerialName("category") val category: String = MemorizationCategory.NEW.name,
    @SerialName("note") val note: String = "",
    @SerialName("date") val date: Long = 0L,
) {
    val memorizationCategory: MemorizationCategory get() = MemorizationCategory.from(category)
}

@Serializable
data class Exam(
    @SerialName("id") val id: String = "",
    @SerialName("student_id") val studentId: String = "",
    @SerialName("student_name") val studentName: String = "",
    @SerialName("teacher_id") val teacherId: String = "",
    @SerialName("institute_id") val instituteId: String = "",
    @SerialName("surah") val surah: String = "",
    @SerialName("score") val score: Int = 0,
    @SerialName("mistakes") val mistakes: String = "",
    @SerialName("mastery") val mastery: String = "",
    @SerialName("note") val note: String = "",
    @SerialName("date") val date: Long = 0L,
)

enum class RevisionCategory(val label: String, val shortLabel: String) {
    NEW("مراجعة جديد", "جديد"),
    OLD("مراجعة قديم / سابق", "قديم"),
    ;

    companion object {
        fun from(value: String?): RevisionCategory =
            entries.find { it.name.equals(value, ignoreCase = true) || it.label == value || it.shortLabel == value }
                ?: NEW
    }
}

@Serializable
data class Revision(
    @SerialName("id") val id: String = "",
    @SerialName("student_id") val studentId: String = "",
    @SerialName("student_name") val studentName: String = "",
    @SerialName("teacher_id") val teacherId: String = "",
    @SerialName("institute_id") val instituteId: String = "",
    @SerialName("content") val content: String = "",
    @SerialName("level") val level: String = "",
    @SerialName("rating") val rating: String = "",
    @SerialName("category") val category: String = RevisionCategory.NEW.name,
    @SerialName("note") val note: String = "",
    @SerialName("date") val date: Long = 0L,
) {
    val revisionCategory: RevisionCategory get() = RevisionCategory.from(category)
}

enum class AttendanceStatus(val label: String) {
    PRESENT("حاضر"),
    ABSENT("غائب"),
    LATE("متأخر"),
    ;

    companion object {
        fun from(value: String?): AttendanceStatus =
            entries.find { it.name.equals(value, ignoreCase = true) || it.label == value }
                ?: PRESENT
    }
}

@Serializable
data class Attendance(
    @SerialName("id") val id: String = "",
    @SerialName("student_id") val studentId: String = "",
    @SerialName("student_name") val studentName: String = "",
    @SerialName("teacher_id") val teacherId: String = "",
    @SerialName("institute_id") val instituteId: String = "",
    @SerialName("status") val status: String = AttendanceStatus.PRESENT.name,
    @SerialName("date") val date: String = "",
    @SerialName("created_at") val createdAt: Long = 0L,
) {
    val attendanceStatus: AttendanceStatus get() = AttendanceStatus.from(status)
}
