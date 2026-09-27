package com.omran.quran.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.omran.quran.data.local.SyncStatus
import com.omran.quran.data.model.Revision

@Entity(tableName = "revisions")
data class RevisionEntity(
    @PrimaryKey val id: String,
    val studentId: String,
    val studentName: String,
    val teacherId: String,
    val instituteId: String,
    val content: String,
    val level: String,
    val rating: String,
    val category: String,
    val note: String,
    val date: Long,
    val syncStatus: SyncStatus = SyncStatus.SYNCED,
    val updatedAt: Long = System.currentTimeMillis(),
) {
    fun toModel(): Revision = Revision(
        id = id,
        studentId = studentId,
        studentName = studentName,
        teacherId = teacherId,
        instituteId = instituteId,
        content = content,
        level = level,
        rating = rating,
        category = category,
        note = note,
        date = date,
    )

    companion object {
        fun fromModel(r: Revision, syncStatus: SyncStatus = SyncStatus.SYNCED): RevisionEntity =
            RevisionEntity(
                id = r.id,
                studentId = r.studentId,
                studentName = r.studentName,
                teacherId = r.teacherId,
                instituteId = r.instituteId,
                content = r.content,
                level = r.level,
                rating = r.rating,
                category = r.category,
                note = r.note,
                date = r.date,
                syncStatus = syncStatus,
            )
    }
}
