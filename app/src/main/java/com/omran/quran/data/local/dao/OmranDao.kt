package com.omran.quran.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.omran.quran.data.local.SyncStatus
import com.omran.quran.data.local.entity.AttendanceEntity
import com.omran.quran.data.local.entity.ExamEntity
import com.omran.quran.data.local.entity.MemorizationEntity
import com.omran.quran.data.local.entity.RevisionEntity
import com.omran.quran.data.local.entity.StudentEntity

@Dao
interface OmranDao {

    // ======================== STUDENTS ========================
    @Query("""
        SELECT * FROM students 
        WHERE syncStatus != 'PENDING_DELETE'
        AND (:instituteId = '' OR instituteId = :instituteId)
        AND (:teacherId IS NULL OR :teacherId = '' OR teacherId = :teacherId)
        AND (:gender IS NULL OR :gender = '' OR gender = :gender)
        AND (:query = '' OR name LIKE '%' || :query || '%')
        ORDER BY name ASC
    """)
    suspend fun getStudents(
        instituteId: String,
        teacherId: String?,
        query: String = "",
        gender: String? = null,
    ): List<StudentEntity>

    @Query("SELECT * FROM students WHERE id = :id AND syncStatus != 'PENDING_DELETE' LIMIT 1")
    suspend fun getStudentById(id: String): StudentEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertStudents(students: List<StudentEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertStudent(student: StudentEntity)

    @Query("UPDATE students SET syncStatus = 'PENDING_DELETE' WHERE id = :id")
    suspend fun markStudentDeleted(id: String)

    @Query("DELETE FROM students WHERE id = :id")
    suspend fun deleteStudentPermanently(id: String)

    @Query("""
        SELECT COUNT(*) FROM students 
        WHERE syncStatus != 'PENDING_DELETE'
        AND (:instituteId = '' OR instituteId = :instituteId)
        AND (:teacherId IS NULL OR :teacherId = '' OR teacherId = :teacherId)
        AND (:gender IS NULL OR :gender = '' OR gender = :gender)
    """)
    suspend fun countStudents(instituteId: String, teacherId: String?, gender: String? = null): Int

    @Query("""
        SELECT COUNT(*) FROM memorizations
        WHERE syncStatus != 'PENDING_DELETE'
        AND (:instituteId = '' OR instituteId = :instituteId)
        AND (:teacherId IS NULL OR :teacherId = '' OR teacherId = :teacherId)
    """)
    suspend fun countMemorizations(instituteId: String, teacherId: String?): Int

    @Query("""
        SELECT COUNT(*) FROM revisions
        WHERE syncStatus != 'PENDING_DELETE'
        AND (:instituteId = '' OR instituteId = :instituteId)
        AND (:teacherId IS NULL OR :teacherId = '' OR teacherId = :teacherId)
    """)
    suspend fun countRevisions(instituteId: String, teacherId: String?): Int

    @Query("""
        SELECT COUNT(*) FROM exams
        WHERE syncStatus != 'PENDING_DELETE'
        AND (:instituteId = '' OR instituteId = :instituteId)
        AND (:teacherId IS NULL OR :teacherId = '' OR teacherId = :teacherId)
    """)
    suspend fun countExams(instituteId: String, teacherId: String?): Int

    @Query("""
        SELECT s.* FROM students s
        LEFT JOIN (
            SELECT studentId, COUNT(*) as memCount
            FROM memorizations
            WHERE syncStatus != 'PENDING_DELETE'
            GROUP BY studentId
        ) m ON m.studentId = s.id
        WHERE s.syncStatus != 'PENDING_DELETE'
        AND (:instituteId = '' OR s.instituteId = :instituteId)
        AND (:gender IS NULL OR :gender = '' OR s.gender = :gender)
        ORDER BY COALESCE(m.memCount, 0) DESC
        LIMIT :limitCount
    """)
    suspend fun getTopStudents(
        instituteId: String,
        limitCount: Int,
        gender: String? = null,
    ): List<StudentEntity>

    @Query("""
        SELECT * FROM students
        WHERE syncStatus != 'PENDING_DELETE'
        AND (:instituteId = '' OR instituteId = :instituteId)
        AND (:teacherId IS NULL OR :teacherId = '' OR teacherId = :teacherId)
        AND (:gender IS NULL OR :gender = '' OR gender = :gender)
        ORDER BY name ASC
    """)
    suspend fun getStudentsByTeacher(
        instituteId: String,
        teacherId: String?,
        gender: String? = null,
    ): List<StudentEntity>

    @Query("SELECT * FROM students WHERE syncStatus != 'SYNCED'")
    suspend fun getPendingStudents(): List<StudentEntity>

    @Query("SELECT COUNT(*) FROM memorizations WHERE syncStatus != 'PENDING_DELETE' AND studentId = :studentId")
    suspend fun countMemorizationsByStudent(studentId: String): Int

    @Query("SELECT COUNT(*) FROM revisions WHERE syncStatus != 'PENDING_DELETE' AND studentId = :studentId")
    suspend fun countRevisionsByStudent(studentId: String): Int

    @Query("SELECT COUNT(*) FROM exams WHERE syncStatus != 'PENDING_DELETE' AND studentId = :studentId")
    suspend fun countExamsByStudent(studentId: String): Int

    // ======================== MEMORIZATION ========================
    @Query("""
        SELECT * FROM memorizations 
        WHERE syncStatus != 'PENDING_DELETE'
        AND (:instituteId = '' OR instituteId = :instituteId)
        AND (:studentId IS NULL OR :studentId = '' OR studentId = :studentId)
        AND (:teacherId IS NULL OR :teacherId = '' OR teacherId = :teacherId)
        ORDER BY date DESC
    """)
    suspend fun getMemorizations(instituteId: String, teacherId: String?, studentId: String?): List<MemorizationEntity>

    @Query("""
        SELECT * FROM memorizations 
        WHERE syncStatus != 'PENDING_DELETE'
        AND (:instituteId = '' OR instituteId = :instituteId)
        AND date >= :startOfDay AND date <= :endOfDay
        ORDER BY date DESC
    """)
    suspend fun getMemorizationsForDay(instituteId: String, startOfDay: Long, endOfDay: Long): List<MemorizationEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertMemorizations(mems: List<MemorizationEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertMemorization(mem: MemorizationEntity)

    @Query("UPDATE memorizations SET syncStatus = 'PENDING_DELETE' WHERE id = :id")
    suspend fun markMemorizationDeleted(id: String)

    @Query("DELETE FROM memorizations WHERE id = :id")
    suspend fun deleteMemorizationPermanently(id: String)

    @Query("SELECT * FROM memorizations WHERE id = :id AND syncStatus != 'PENDING_DELETE' LIMIT 1")
    suspend fun getMemorizationById(id: String): MemorizationEntity?

    @Query("SELECT * FROM memorizations WHERE syncStatus != 'SYNCED'")
    suspend fun getPendingMemorizations(): List<MemorizationEntity>

    // ======================== EXAMS ========================
    @Query("""
        SELECT * FROM exams 
        WHERE syncStatus != 'PENDING_DELETE'
        AND (:instituteId = '' OR instituteId = :instituteId)
        AND (:studentId IS NULL OR :studentId = '' OR studentId = :studentId)
        AND (:teacherId IS NULL OR :teacherId = '' OR teacherId = :teacherId)
        ORDER BY date DESC
    """)
    suspend fun getExams(instituteId: String, teacherId: String?, studentId: String?): List<ExamEntity>

    @Query("SELECT * FROM exams WHERE id = :id AND syncStatus != 'PENDING_DELETE' LIMIT 1")
    suspend fun getExamById(id: String): ExamEntity?

    @Query("""
        SELECT * FROM exams 
        WHERE syncStatus != 'PENDING_DELETE'
        AND (:instituteId = '' OR instituteId = :instituteId)
        AND date >= :startOfDay AND date <= :endOfDay
        ORDER BY date DESC
    """)
    suspend fun getExamsForDay(instituteId: String, startOfDay: Long, endOfDay: Long): List<ExamEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertExams(exams: List<ExamEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertExam(exam: ExamEntity)

    @Query("UPDATE exams SET syncStatus = 'PENDING_DELETE' WHERE id = :id")
    suspend fun markExamDeleted(id: String)

    @Query("DELETE FROM exams WHERE id = :id")
    suspend fun deleteExamPermanently(id: String)

    @Query("SELECT * FROM exams WHERE syncStatus != 'SYNCED'")
    suspend fun getPendingExams(): List<ExamEntity>

    // ======================== REVISIONS ========================
    @Query("""
        SELECT * FROM revisions 
        WHERE syncStatus != 'PENDING_DELETE'
        AND (:instituteId = '' OR instituteId = :instituteId)
        AND (:studentId IS NULL OR :studentId = '' OR studentId = :studentId)
        AND (:teacherId IS NULL OR :teacherId = '' OR teacherId = :teacherId)
        ORDER BY date DESC
    """)
    suspend fun getRevisions(instituteId: String, teacherId: String?, studentId: String?): List<RevisionEntity>

    @Query("SELECT * FROM revisions WHERE id = :id AND syncStatus != 'PENDING_DELETE' LIMIT 1")
    suspend fun getRevisionById(id: String): RevisionEntity?

    @Query("""
        SELECT * FROM revisions 
        WHERE syncStatus != 'PENDING_DELETE'
        AND (:instituteId = '' OR instituteId = :instituteId)
        AND date >= :startOfDay AND date <= :endOfDay
        ORDER BY date DESC
    """)
    suspend fun getRevisionsForDay(instituteId: String, startOfDay: Long, endOfDay: Long): List<RevisionEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertRevisions(revisions: List<RevisionEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertRevision(rev: RevisionEntity)

    @Query("UPDATE revisions SET syncStatus = 'PENDING_DELETE' WHERE id = :id")
    suspend fun markRevisionDeleted(id: String)

    @Query("DELETE FROM revisions WHERE id = :id")
    suspend fun deleteRevisionPermanently(id: String)

    @Query("SELECT * FROM revisions WHERE syncStatus != 'SYNCED'")
    suspend fun getPendingRevisions(): List<RevisionEntity>

    // ======================== ATTENDANCE ========================
    @Query("""
        SELECT * FROM attendance 
        WHERE syncStatus != 'PENDING_DELETE'
        AND (:instituteId = '' OR instituteId = :instituteId)
        AND date = :date
        ORDER BY createdAt DESC
    """)
    suspend fun getAttendanceForDate(instituteId: String, date: String): List<AttendanceEntity>

    @Query("""
        SELECT * FROM attendance 
        WHERE syncStatus != 'PENDING_DELETE'
        AND studentId = :studentId
        ORDER BY date DESC
    """)
    suspend fun getAttendanceForStudent(studentId: String): List<AttendanceEntity>

    @Query("""
        SELECT * FROM attendance 
        WHERE syncStatus != 'PENDING_DELETE'
        AND (:instituteId = '' OR instituteId = :instituteId)
        ORDER BY date DESC
    """)
    suspend fun getAttendanceHistory(instituteId: String): List<AttendanceEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertAttendances(attendances: List<AttendanceEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertAttendance(attendance: AttendanceEntity)

    @Query("SELECT * FROM attendance WHERE syncStatus != 'SYNCED'")
    suspend fun getPendingAttendances(): List<AttendanceEntity>

    @Query("DELETE FROM attendance WHERE id = :id")
    suspend fun deleteAttendancePermanently(id: String)

    @Query("""
        SELECT COUNT(*) FROM attendance 
        WHERE syncStatus != 'PENDING_DELETE' 
        AND (:instituteId = '' OR instituteId = :instituteId)
    """)
    suspend fun countTotalAttendance(instituteId: String): Int

    @Query("""
        SELECT COUNT(*) FROM attendance 
        WHERE syncStatus != 'PENDING_DELETE' 
        AND (:instituteId = '' OR instituteId = :instituteId)
        AND status IN ('PRESENT', 'LATE')
    """)
    suspend fun countPresentOrLateAttendance(instituteId: String): Int

    @Query("""
        SELECT COUNT(*) FROM attendance 
        WHERE syncStatus != 'PENDING_DELETE' 
        AND (:instituteId = '' OR instituteId = :instituteId)
        AND status = 'ABSENT'
    """)
    suspend fun countAbsentAttendance(instituteId: String): Int
}
