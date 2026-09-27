package com.omran.quran.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.omran.quran.data.local.SyncStatus
import com.omran.quran.data.model.Memorization

@Entity(tableName = "memorizations")
data class MemorizationEntity(
    @PrimaryKey val id: String,
    val studentId: String,
    val studentName: String,
    val teacherId: String,
    val instituteId: String,
    val surah: String,
    val fromVerse: Int,
    val toVerse: Int,
    val amount: String,
    val rating: String,
    val category: String,
    val note: String,
    val date: Long,
    val syncStatus: SyncStatus = SyncStatus.SYNCED,
    val updatedAt: Long = System.currentTimeMillis(),
) {
    fun toModel(): Memorization = Memorization(
        id = id,
        studentId = studentId,
        studentName = studentName,
        teacherId = teacherId,
        instituteId = instituteId,
        surah = surah,
        fromVerse = fromVerse,
        toVerse = toVerse,
        amount = amount,
        rating = rating,
        category = category,
        note = note,
        date = date,
    )

    companion object {
        fun fromModel(m: Memorization, syncStatus: SyncStatus = SyncStatus.SYNCED): MemorizationEntity =
            MemorizationEntity(
                id = m.id,
                studentId = m.studentId,
                studentName = m.studentName,
                teacherId = m.teacherId,
                instituteId = m.instituteId,
                surah = m.surah,
                fromVerse = m.fromVerse,
                toVerse = m.toVerse,
                amount = m.amount,
                rating = m.rating,
                category = m.category,
                note = m.note,
                date = m.date,
                syncStatus = syncStatus,
            )
    }
}
