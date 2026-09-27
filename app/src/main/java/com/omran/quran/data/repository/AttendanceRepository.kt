package com.omran.quran.data.repository

import com.omran.quran.data.local.SyncStatus
import com.omran.quran.data.local.dao.OmranDao
import com.omran.quran.data.local.entity.AttendanceEntity
import com.omran.quran.data.model.Attendance
import com.omran.quran.data.model.AttendanceStatus
import com.omran.quran.data.model.PageResult
import com.omran.quran.util.PAGE_SIZE
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.query.Order
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AttendanceRepository @Inject constructor(
    private val supabase: SupabaseClient,
    private val dao: OmranDao,
) {
    private val _updates = MutableSharedFlow<Unit>(replay = 0, extraBufferCapacity = 1)
    val updates = _updates.asSharedFlow()

    suspend fun forDate(
        instituteId: String,
        teacherId: String?,
        date: String,
    ): List<Attendance> {
        // 1. Try remote fetch and cache in Room
        if (instituteId.isNotBlank() && date.isNotBlank()) {
            runCatching {
                val remote = supabase.from(COL).select {
                    filter {
                        eq("institute_id", instituteId)
                        eq("date", date)
                        if (!teacherId.isNullOrBlank()) {
                            eq("teacher_id", teacherId)
                        }
                    }
                }.decodeList<Attendance>()

                if (remote.isNotEmpty()) {
                    dao.upsertAttendances(remote.map { AttendanceEntity.fromModel(it, SyncStatus.SYNCED) })
                }
            }
        }

        // 2. Read from Room local cache
        val localEntities = dao.getAttendanceForDate(instituteId, date)
        val filtered = if (!teacherId.isNullOrBlank()) {
            localEntities.filter { it.teacherId == teacherId || it.teacherId.isBlank() }
        } else {
            localEntities
        }
        return filtered.map { it.toModel() }
    }

    suspend fun saveAll(records: List<Attendance>) {
        if (records.isEmpty()) return
        val prepared = records.map { item ->
            val id = item.id.ifBlank {
                "${item.studentId}_${item.date}"
            }
            item.copy(
                id = id,
                createdAt = if (item.createdAt != 0L) item.createdAt else System.currentTimeMillis(),
            )
        }

        // 1. Save to local Room DB with PENDING status
        dao.upsertAttendances(prepared.map { AttendanceEntity.fromModel(it, SyncStatus.PENDING_INSERT) })
        _updates.tryEmit(Unit)

        // 2. Attempt remote sync
        runCatching {
            supabase.from(COL).upsert(prepared)
            dao.upsertAttendances(prepared.map { AttendanceEntity.fromModel(it, SyncStatus.SYNCED) })
        }
    }

    suspend fun studentHistory(studentId: String, lastId: String?): PageResult<Attendance> {
        if (studentId.isBlank()) return PageResult(emptyList(), null, true)

        // 1. Try remote fetch and cache
        runCatching {
            val remote = supabase.from(COL).select {
                filter {
                    eq("student_id", studentId)
                }
                order("date", Order.DESCENDING)
                limit(PAGE_SIZE.toLong())
            }.decodeList<Attendance>()

            if (remote.isNotEmpty()) {
                dao.upsertAttendances(remote.map { AttendanceEntity.fromModel(it, SyncStatus.SYNCED) })
            }
        }

        // 2. Read from local cache
        val local = dao.getAttendanceForStudent(studentId).map { it.toModel() }
        return PageResult(
            items = local,
            lastId = local.lastOrNull()?.id,
            endReached = true,
        )
    }

    suspend fun historyDates(
        instituteId: String,
        teacherId: String?,
        lastId: String?,
    ): PageResult<Attendance> {
        if (instituteId.isNotBlank()) {
            runCatching {
                val remote = supabase.from(COL).select {
                    filter {
                        eq("institute_id", instituteId)
                        if (!teacherId.isNullOrBlank()) {
                            eq("teacher_id", teacherId)
                        }
                    }
                    order("date", Order.DESCENDING)
                    limit(PAGE_SIZE.toLong())
                }.decodeList<Attendance>()

                if (remote.isNotEmpty()) {
                    dao.upsertAttendances(remote.map { AttendanceEntity.fromModel(it, SyncStatus.SYNCED) })
                }
            }
        }

        val local = dao.getAttendanceHistory(instituteId).map { it.toModel() }
        return PageResult(
            items = local,
            lastId = local.lastOrNull()?.id,
            endReached = true,
        )
    }

    suspend fun countsForDate(
        instituteId: String,
        teacherId: String?,
        date: String,
    ): Triple<Int, Int, Int> {
        val records = forDate(instituteId, teacherId, date)
        val present = records.count { it.attendanceStatus == AttendanceStatus.PRESENT }
        val absent = records.count { it.attendanceStatus == AttendanceStatus.ABSENT }
        val late = records.count { it.attendanceStatus == AttendanceStatus.LATE }
        return Triple(present, absent, late)
    }

    suspend fun getInstituteAttendanceStats(instituteId: String): InstituteAttendanceStats {
        if (instituteId.isNotBlank()) {
            runCatching {
                val remote = supabase.from(COL).select {
                    filter {
                        eq("institute_id", instituteId)
                    }
                    limit(1000)
                }.decodeList<Attendance>()
                if (remote.isNotEmpty()) {
                    dao.upsertAttendances(remote.map { AttendanceEntity.fromModel(it, SyncStatus.SYNCED) })
                }
            }
        }
        val total = dao.countTotalAttendance(instituteId)
        val present = dao.countPresentOrLateAttendance(instituteId)
        val absent = dao.countAbsentAttendance(instituteId)
        val pct = if (total > 0) ((present.toDouble() / total) * 100).toInt() else 0
        return InstituteAttendanceStats(
            totalRecords = total,
            presentCount = present,
            absentCount = absent,
            percentage = pct,
        )
    }

    companion object {
        const val COL = "attendance"
    }
}

data class InstituteAttendanceStats(
    val totalRecords: Int = 0,
    val presentCount: Int = 0,
    val absentCount: Int = 0,
    val percentage: Int = 0,
)
