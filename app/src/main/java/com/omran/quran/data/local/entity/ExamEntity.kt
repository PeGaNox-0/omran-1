package com.omran.quran.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.omran.quran.data.local.SyncStatus
import com.omran.quran.data.model.Exam

@Entity(tableName = "exams")
data class ExamEntity(
    @PrimaryKey val id: String,
    val studentId: String,
    val studentName: String,
    val teacherId: String,
    val instituteId: String,
    val surah: String,
    val score: Int,
    val mistakes: String,
    val mastery: String,
    val note: String,
    val date: Long,
    val syncStatus: SyncStatus = SyncStatus.SYNCED,
    val updatedAt: Long = System.currentTimeMillis(),
) {
    fun toModel(): Exam = Exam(
        id = id,
        studentId = studentId,
        studentName = studentName,
        teacherId = teacherId,
        instituteId = instituteId,
        surah = surah,
        score = score,
        mistakes = mistakes,
        mastery = mastery,
        note = note,
        date = date,
    )

    companion object {
        fun fromModel(e: Exam, syncStatus: SyncStatus = SyncStatus.SYNCED): ExamEntity =
            ExamEntity(
                id = e.id,
                studentId = e.studentId,
                studentName = e.studentName,
                teacherId = e.teacherId,
                instituteId = e.instituteId,
                surah = e.surah,
                score = e.score,
                mistakes = e.mistakes,
                mastery = e.mastery,
                note = e.note,
                date = e.date,
                syncStatus = syncStatus,
            )
    }
}
