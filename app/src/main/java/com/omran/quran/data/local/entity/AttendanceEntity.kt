package com.omran.quran.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.omran.quran.data.local.SyncStatus
import com.omran.quran.data.model.Attendance

@Entity(tableName = "attendance")
data class AttendanceEntity(
    @PrimaryKey val id: String,
    val studentId: String,
    val studentName: String,
    val teacherId: String,
    val instituteId: String,
    val status: String,
    val date: String,
    val createdAt: Long,
    val syncStatus: SyncStatus = SyncStatus.SYNCED,
    val updatedAt: Long = System.currentTimeMillis(),
) {
    fun toModel(): Attendance = Attendance(
        id = id,
        studentId = studentId,
        studentName = studentName,
        teacherId = teacherId,
        instituteId = instituteId,
        status = status,
        date = date,
        createdAt = createdAt,
    )

    companion object {
        fun fromModel(a: Attendance, syncStatus: SyncStatus = SyncStatus.SYNCED): AttendanceEntity =
            AttendanceEntity(
                id = a.id,
                studentId = a.studentId,
                studentName = a.studentName,
                teacherId = a.teacherId,
                instituteId = a.instituteId,
                status = a.status,
                date = a.date,
                createdAt = a.createdAt,
                syncStatus = syncStatus,
            )
    }
}
