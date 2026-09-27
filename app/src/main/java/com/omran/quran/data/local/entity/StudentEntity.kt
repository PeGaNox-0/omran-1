package com.omran.quran.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.omran.quran.data.local.SyncStatus
import com.omran.quran.data.model.Student

@Entity(tableName = "students")
data class StudentEntity(
    @PrimaryKey val id: String,
    val name: String,
    val age: Int,
    val parentPhone: String,
    val teacherId: String,
    val teacherName: String,
    val circleName: String,
    val instituteId: String,
    val photoUrl: String,
    val joinDate: Long?,
    val gender: String = "MALE",
    val syncStatus: SyncStatus = SyncStatus.SYNCED,
    val updatedAt: Long = System.currentTimeMillis(),
) {
    fun toModel(): Student = Student(
        studentId = id,
        name = name,
        age = age,
        parentPhone = parentPhone,
        teacherId = teacherId,
        teacherName = teacherName,
        circleName = circleName,
        instituteId = instituteId,
        photoUrl = photoUrl,
        joinDate = joinDate,
        gender = gender,
    )

    companion object {
        fun fromModel(student: Student, syncStatus: SyncStatus = SyncStatus.SYNCED): StudentEntity =
            StudentEntity(
                id = student.studentId,
                name = student.name,
                age = student.age,
                parentPhone = student.parentPhone,
                teacherId = student.teacherId,
                teacherName = student.teacherName,
                circleName = student.circleName,
                instituteId = student.instituteId,
                photoUrl = student.photoUrl,
                joinDate = student.joinDate,
                gender = student.gender,
                syncStatus = syncStatus,
            )
    }
}
