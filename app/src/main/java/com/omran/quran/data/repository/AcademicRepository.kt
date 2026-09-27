package com.omran.quran.data.repository

import com.omran.quran.data.local.SyncStatus
import com.omran.quran.data.local.dao.OmranDao
import com.omran.quran.data.local.entity.ExamEntity
import com.omran.quran.data.local.entity.MemorizationEntity
import com.omran.quran.data.local.entity.RevisionEntity
import com.omran.quran.data.model.Exam
import com.omran.quran.data.model.Memorization
import com.omran.quran.data.model.PageResult
import com.omran.quran.data.model.Revision
import com.omran.quran.util.PAGE_SIZE
import com.omran.quran.util.endOfDay
import com.omran.quran.util.startOfDay
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.query.Count
import io.github.jan.supabase.postgrest.query.Order
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AcademicRepository @Inject constructor(
    private val supabase: SupabaseClient,
    private val dao: OmranDao,
) {
    private val _updates = MutableSharedFlow<Unit>(replay = 0, extraBufferCapacity = 1)
    val updates = _updates.asSharedFlow()

    // ======================== MEMORIZATION ========================
    suspend fun memorizations(
        instituteId: String,
        teacherId: String?,
        studentId: String?,
        search: String,
        dayMillis: Long?,
        lastId: String?,
    ): PageResult<Memorization> {
        if (instituteId.isNotBlank()) {
            runCatching {
                val remote = supabase.from(MEM).select {
                    filter {
                        eq("institute_id", instituteId)
                        when {
                            !studentId.isNullOrBlank() -> eq("student_id", studentId)
                            !teacherId.isNullOrBlank() -> eq("teacher_id", teacherId)
                        }
                        if (dayMillis != null) {
                            gte("date", startOfDay(dayMillis))
                            lte("date", endOfDay(dayMillis))
                        }
                    }
                    order("date", Order.DESCENDING)
                    limit(PAGE_SIZE.toLong())
                }.decodeList<Memorization>()

                if (remote.isNotEmpty()) {
                    dao.upsertMemorizations(remote.map { MemorizationEntity.fromModel(it, SyncStatus.SYNCED) })
                }
            }
        }

        val local = if (dayMillis != null) {
            dao.getMemorizationsForDay(instituteId, startOfDay(dayMillis), endOfDay(dayMillis)).map { it.toModel() }
        } else {
            dao.getMemorizations(instituteId, teacherId, studentId).map { it.toModel() }
        }

        val filtered = filterByName(local, search) { it.studentName }
        return PageResult(filtered, filtered.lastOrNull()?.id, true)
    }

    suspend fun saveMemorization(item: Memorization): String {
        val id = item.id.ifBlank { UUID.randomUUID().toString() }
        val toSave = item.copy(
            id = id,
            date = if (item.date != 0L) item.date else System.currentTimeMillis(),
        )

        dao.upsertMemorization(MemorizationEntity.fromModel(toSave, SyncStatus.PENDING_INSERT))
        _updates.tryEmit(Unit)

        runCatching {
            supabase.from(MEM).upsert(toSave)
            dao.upsertMemorization(MemorizationEntity.fromModel(toSave, SyncStatus.SYNCED))
        }

        return id
    }

    suspend fun getMemorization(id: String): Memorization? {
        if (id.isBlank()) return null
        return dao.getMemorizationById(id)?.toModel()
    }

    suspend fun addMemorization(item: Memorization): String = saveMemorization(item)

    suspend fun deleteMemorization(id: String) {
        if (id.isBlank()) return
        dao.markMemorizationDeleted(id)
        _updates.tryEmit(Unit)

        runCatching {
            supabase.from(MEM).delete { filter { eq("id", id) } }
            dao.deleteMemorizationPermanently(id)
        }
    }

    suspend fun countMemorizations(instituteId: String, teacherId: String?): Int {
        val localCount = dao.getMemorizations(instituteId, teacherId, null).size
        if (instituteId.isBlank()) return localCount

        val remoteCount = runCatching {
            supabase.from(MEM).select {
                count(Count.EXACT)
                filter {
                    eq("institute_id", instituteId)
                    if (!teacherId.isNullOrBlank()) {
                        eq("teacher_id", teacherId)
                    }
                }
            }.countOrNull()?.toInt()
        }.getOrNull()

        return remoteCount ?: localCount
    }

    // ======================== EXAMS ========================
    suspend fun exams(
        instituteId: String,
        teacherId: String?,
        studentId: String?,
        search: String,
        dayMillis: Long?,
        lastId: String?,
    ): PageResult<Exam> {
        if (instituteId.isNotBlank()) {
            runCatching {
                val remote = supabase.from(EXAMS).select {
                    filter {
                        eq("institute_id", instituteId)
                        when {
                            !studentId.isNullOrBlank() -> eq("student_id", studentId)
                            !teacherId.isNullOrBlank() -> eq("teacher_id", teacherId)
                        }
                        if (dayMillis != null) {
                            gte("date", startOfDay(dayMillis))
                            lte("date", endOfDay(dayMillis))
                        }
                    }
                    order("date", Order.DESCENDING)
                    limit(PAGE_SIZE.toLong())
                }.decodeList<Exam>()

                if (remote.isNotEmpty()) {
                    dao.upsertExams(remote.map { ExamEntity.fromModel(it, SyncStatus.SYNCED) })
                }
            }
        }

        val local = if (dayMillis != null) {
            dao.getExamsForDay(instituteId, startOfDay(dayMillis), endOfDay(dayMillis)).map { it.toModel() }
        } else {
            dao.getExams(instituteId, teacherId, studentId).map { it.toModel() }
        }

        val filtered = filterByName(local, search) { it.studentName }
        return PageResult(filtered, filtered.lastOrNull()?.id, true)
    }

    suspend fun saveExam(item: Exam): String {
        val id = item.id.ifBlank { UUID.randomUUID().toString() }
        val toSave = item.copy(
            id = id,
            date = if (item.date != 0L) item.date else System.currentTimeMillis(),
        )

        dao.upsertExam(ExamEntity.fromModel(toSave, SyncStatus.PENDING_INSERT))
        _updates.tryEmit(Unit)

        runCatching {
            supabase.from(EXAMS).upsert(toSave)
            dao.upsertExam(ExamEntity.fromModel(toSave, SyncStatus.SYNCED))
        }

        return id
    }

    suspend fun getExam(id: String): Exam? {
        if (id.isBlank()) return null
        return dao.getExamById(id)?.toModel()
    }

    suspend fun addExam(item: Exam): String = saveExam(item)

    suspend fun deleteExam(id: String) {
        if (id.isBlank()) return
        dao.markExamDeleted(id)
        _updates.tryEmit(Unit)

        runCatching {
            supabase.from(EXAMS).delete { filter { eq("id", id) } }
            dao.deleteExamPermanently(id)
        }
    }

    suspend fun countExams(instituteId: String, teacherId: String?): Int {
        val localCount = dao.getExams(instituteId, teacherId, null).size
        if (instituteId.isBlank()) return localCount

        val remoteCount = runCatching {
            supabase.from(EXAMS).select {
                count(Count.EXACT)
                filter {
                    eq("institute_id", instituteId)
                    if (!teacherId.isNullOrBlank()) {
                        eq("teacher_id", teacherId)
                    }
                }
            }.countOrNull()?.toInt()
        }.getOrNull()

        return remoteCount ?: localCount
    }

    // ======================== REVISIONS ========================
    suspend fun revisions(
        instituteId: String,
        teacherId: String?,
        studentId: String?,
        search: String,
        dayMillis: Long?,
        lastId: String?,
    ): PageResult<Revision> {
        if (instituteId.isNotBlank()) {
            runCatching {
                val remote = supabase.from(REVISIONS).select {
                    filter {
                        eq("institute_id", instituteId)
                        when {
                            !studentId.isNullOrBlank() -> eq("student_id", studentId)
                            !teacherId.isNullOrBlank() -> eq("teacher_id", teacherId)
                        }
                        if (dayMillis != null) {
                            gte("date", startOfDay(dayMillis))
                            lte("date", endOfDay(dayMillis))
                        }
                    }
                    order("date", Order.DESCENDING)
                    limit(PAGE_SIZE.toLong())
                }.decodeList<Revision>()

                if (remote.isNotEmpty()) {
                    dao.upsertRevisions(remote.map { RevisionEntity.fromModel(it, SyncStatus.SYNCED) })
                }
            }
        }

        val local = if (dayMillis != null) {
            dao.getRevisionsForDay(instituteId, startOfDay(dayMillis), endOfDay(dayMillis)).map { it.toModel() }
        } else {
            dao.getRevisions(instituteId, teacherId, studentId).map { it.toModel() }
        }

        val filtered = filterByName(local, search) { it.studentName }
        return PageResult(filtered, filtered.lastOrNull()?.id, true)
    }

    suspend fun saveRevision(item: Revision): String {
        val id = item.id.ifBlank { UUID.randomUUID().toString() }
        val toSave = item.copy(
            id = id,
            date = if (item.date != 0L) item.date else System.currentTimeMillis(),
        )

        dao.upsertRevision(RevisionEntity.fromModel(toSave, SyncStatus.PENDING_INSERT))
        _updates.tryEmit(Unit)

        runCatching {
            supabase.from(REVISIONS).upsert(toSave)
            dao.upsertRevision(RevisionEntity.fromModel(toSave, SyncStatus.SYNCED))
        }

        return id
    }

    suspend fun getRevision(id: String): Revision? {
        if (id.isBlank()) return null
        return dao.getRevisionById(id)?.toModel()
    }

    suspend fun addRevision(item: Revision): String = saveRevision(item)

    suspend fun deleteRevision(id: String) {
        if (id.isBlank()) return
        dao.markRevisionDeleted(id)
        _updates.tryEmit(Unit)

        runCatching {
            supabase.from(REVISIONS).delete { filter { eq("id", id) } }
            dao.deleteRevisionPermanently(id)
        }
    }

    suspend fun countRevisions(instituteId: String, teacherId: String?): Int {
        val localCount = dao.getRevisions(instituteId, teacherId, null).size
        if (instituteId.isBlank()) return localCount

        val remoteCount = runCatching {
            supabase.from(REVISIONS).select {
                count(Count.EXACT)
                filter {
                    eq("institute_id", instituteId)
                    if (!teacherId.isNullOrBlank()) {
                        eq("teacher_id", teacherId)
                    }
                }
            }.countOrNull()?.toInt()
        }.getOrNull()

        return remoteCount ?: localCount
    }

    suspend fun countToday(table: String, instituteId: String, teacherId: String?): Int {
        val now = System.currentTimeMillis()
        val s = startOfDay(now)
        val e = endOfDay(now)
        return when (table) {
            MEM -> dao.getMemorizationsForDay(instituteId, s, e).size
            EXAMS -> dao.getExamsForDay(instituteId, s, e).size
            REVISIONS, REV -> dao.getRevisionsForDay(instituteId, s, e).size
            else -> 0
        }
    }

    suspend fun dayAchievements(
        instituteId: String,
        teacherId: String?,
        dayMillis: Long,
    ): Triple<List<Memorization>, List<Exam>, List<Revision>> {
        val mems = memorizations(instituteId, teacherId, null, "", dayMillis, null).items
        val exms = exams(instituteId, teacherId, null, "", dayMillis, null).items
        val revs = revisions(instituteId, teacherId, null, "", dayMillis, null).items
        return Triple(mems, exms, revs)
    }

    private fun <T> filterByName(list: List<T>, query: String, nameExtractor: (T) -> String): List<T> {
        val q = query.trim()
        if (q.isEmpty()) return list
        return list.filter { nameExtractor(it).contains(q, ignoreCase = true) }
    }

    companion object {
        const val MEM = "memorization"
        const val EXAMS = "exams"
        const val REVISIONS = "revisions"
        const val REV = "revisions"
    }
}
