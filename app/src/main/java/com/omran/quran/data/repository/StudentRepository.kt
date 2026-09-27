package com.omran.quran.data.repository

import android.content.Context
import android.net.Uri
import com.omran.quran.data.local.SyncStatus
import com.omran.quran.data.local.dao.OmranDao
import com.omran.quran.data.local.entity.StudentEntity
import com.omran.quran.data.model.AppUser
import com.omran.quran.data.model.CircleStats
import com.omran.quran.data.model.Institute
import com.omran.quran.data.model.PageResult
import com.omran.quran.data.model.Student
import com.omran.quran.util.PAGE_SIZE
import dagger.hilt.android.qualifiers.ApplicationContext
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.query.Count
import io.github.jan.supabase.postgrest.query.Order
import io.github.jan.supabase.storage.storage
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class StudentRepository @Inject constructor(
    private val supabase: SupabaseClient,
    private val dao: OmranDao,
    @ApplicationContext private val context: Context,
) {
    private val _updates = MutableSharedFlow<Unit>(replay = 0, extraBufferCapacity = 1)
    val updates = _updates.asSharedFlow()

    suspend fun page(
        instituteId: String,
        teacherId: String?,
        queryText: String,
        lastId: String?,
        gender: String? = null,
    ): PageResult<Student> {
        val q = queryText.trim()

        var remoteItems: List<Student>? = null
        // 1. Try remote fetch and cache in local Room database
        if (instituteId.isNotBlank()) {
            runCatching {
                val fetched = supabase.from(COL).select {
                    filter {
                        eq("institute_id", instituteId)
                        if (!teacherId.isNullOrBlank()) {
                            eq("teacher_id", teacherId)
                        }
                        if (!gender.isNullOrBlank()) {
                            eq("gender", gender)
                        }
                        if (q.isNotEmpty()) {
                            ilike("name", "%$q%")
                        }
                    }
                    order("name", Order.ASCENDING)
                    limit(PAGE_SIZE.toLong())
                }.decodeList<Student>()

                remoteItems = fetched
                if (fetched.isNotEmpty()) {
                    dao.upsertStudents(fetched.map { StudentEntity.fromModel(it, SyncStatus.SYNCED) })
                }
            }.onFailure { e ->
                android.util.Log.e("StudentRepo", "Error fetching remote students: ${e.message}", e)
            }
        }

        // 2. Read from Room database (offline-first & robust)
        val localEntities = dao.getStudents(instituteId, teacherId, q, gender)
        val localItems = localEntities.map { it.toModel() }

        // Use local items if available, or fall back to freshly fetched remote items
        val finalItems = if (localItems.isNotEmpty()) {
            localItems
        } else {
            remoteItems ?: emptyList()
        }

        return PageResult(
            items = finalItems,
            lastId = finalItems.lastOrNull()?.studentId,
            endReached = true,
        )
    }

    /**
     * إعادة جلب وتحديث جميع طلاب المعهد من السحابة وتخزينهم في قاعدة البيانات المحلية.
     * هذا يضمن ظهور بيانات طلاب المعلمين للمدير حتى وإن كان الحساب منشأ مسبقاً.
     */
    suspend fun refreshAllFromRemote(instituteId: String) {
        if (instituteId.isBlank()) return
        runCatching {
            val allRemote = supabase.from(COL).select {
                filter {
                    eq("institute_id", instituteId)
                }
                order("name", Order.ASCENDING)
                limit(500)
            }.decodeList<Student>()

            if (allRemote.isNotEmpty()) {
                dao.upsertStudents(allRemote.map { StudentEntity.fromModel(it, SyncStatus.SYNCED) })
                _updates.tryEmit(Unit)
            }
        }.onFailure { e ->
            android.util.Log.e("StudentRepo", "Failed to refresh all students: ${e.message}", e)
        }
    }

    suspend fun get(studentId: String): Student? {
        if (studentId.isBlank()) return null

        // 1. Check local cache first
        val local = dao.getStudentById(studentId)?.toModel()

        // 2. Try refreshing from remote if online
        val remote = runCatching {
            supabase.from(COL).select {
                filter { eq("id", studentId) }
            }.decodeSingleOrNull<Student>()
        }.getOrNull()

        if (remote != null) {
            dao.upsertStudent(StudentEntity.fromModel(remote, SyncStatus.SYNCED))
            return remote
        }

        return local
    }

    suspend fun save(student: Student): String {
        val id = student.studentId.ifBlank { UUID.randomUUID().toString() }
        var instId = student.instituteId.trim()
        if (instId.isBlank()) {
            val currentUid = supabase.auth.currentUserOrNull()?.id
            if (currentUid != null) {
                val user = runCatching {
                    supabase.from("users").select {
                        filter { eq("id", currentUid) }
                    }.decodeSingleOrNull<AppUser>()
                }.getOrNull()
                if (user != null && user.instituteId.isNotBlank()) {
                    instId = user.instituteId
                }
            }
            if (instId.isBlank()) {
                instId = UUID.randomUUID().toString()
                runCatching {
                    supabase.from("institutes").upsert(
                        Institute(
                            instituteId = instId,
                            name = "معهد المقرئ",
                            shortCode = UUID.randomUUID().toString().take(6).uppercase(),
                            createdAt = System.currentTimeMillis(),
                        ),
                    )
                }
            }
        }

        val toSave = student.copy(
            studentId = id,
            instituteId = instId,
            joinDate = student.joinDate ?: System.currentTimeMillis(),
        )

        // 1. Save locally with PENDING status immediately
        dao.upsertStudent(StudentEntity.fromModel(toSave, SyncStatus.PENDING_INSERT))
        _updates.tryEmit(Unit)

        // 2. Attempt remote sync
        runCatching {
            supabase.from(COL).upsert(toSave)
            dao.upsertStudent(StudentEntity.fromModel(toSave, SyncStatus.SYNCED))
        }

        return id
    }

    suspend fun delete(studentId: String) {
        if (studentId.isBlank()) return

        // 1. Mark as pending delete in local DB
        dao.markStudentDeleted(studentId)
        _updates.tryEmit(Unit)

        // 2. Attempt remote delete
        runCatching {
            supabase.from(COL).delete {
                filter { eq("id", studentId) }
            }
            supabase.storage.from("students").delete(listOf("$studentId/photo.jpg"))
            dao.deleteStudentPermanently(studentId)
        }
    }

    suspend fun uploadPhoto(studentId: String, uri: Uri): String {
        return runCatching {
            val bytes = context.contentResolver.openInputStream(uri)?.use { it.readBytes() }
                ?: error("تعذر قراءة ملف الصورة")
            val path = "$studentId/photo.jpg"
            val bucket = supabase.storage.from("students")
            bucket.upload(path, bytes) {
                upsert = true
            }
            bucket.publicUrl(path)
        }.getOrDefault("")
    }

    suspend fun count(instituteId: String, teacherId: String?): Int {
        val localCount = dao.countStudents(instituteId, teacherId)

        if (instituteId.isBlank()) return localCount

        val remoteCount = runCatching {
            supabase.from(COL).select {
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

    /** إحصائيات حلقة محددة (أو كامل المعهد إذا كان teacherId فارغاً) من قاعدة البيانات المحلية مع تفصيل البنين والبنات */
    suspend fun getCircleStats(
        instituteId: String,
        teacherId: String?,
        gender: String? = null,
    ): CircleStats {
        val totalStudents = dao.countStudents(instituteId, teacherId, gender)
        val maleStudents = dao.countStudents(instituteId, teacherId, "MALE")
        val femaleStudents = dao.countStudents(instituteId, teacherId, "FEMALE")
        val memorizations = dao.countMemorizations(instituteId, teacherId)
        val revisions = dao.countRevisions(instituteId, teacherId)
        val exams = dao.countExams(instituteId, teacherId)
        return CircleStats(
            studentCount = totalStudents,
            maleStudentCount = maleStudents,
            femaleStudentCount = femaleStudents,
            memorizationCount = memorizations,
            revisionCount = revisions,
            examCount = exams,
        )
    }

    /** أفضل الطلاب نشاطاً في المعهد مرتبين حسب عدد جلسات الحفظ مع إمكانية التصفية حسب الجنس */
    suspend fun getTopStudents(instituteId: String, limit: Int = 10, gender: String? = null): List<Student> {
        return dao.getTopStudents(instituteId, limit, gender).map { it.toModel() }
    }

    /** جميع طلاب حلقة معينة (أو المعهد كله) للتقارير مع إمكانية التصفية حسب الجنس */
    suspend fun getStudentsByTeacher(instituteId: String, teacherId: String?, gender: String? = null): List<Student> {
        return dao.getStudentsByTeacher(instituteId, teacherId, gender).map { it.toModel() }
    }

    /** عدد جلسات طالب محدد (mem/rev/exam) من القاعدة المحلية */
    suspend fun countByStudent(studentId: String, type: String): Int = when (type) {
        "mem" -> dao.countMemorizationsByStudent(studentId)
        "rev" -> dao.countRevisionsByStudent(studentId)
        "exam" -> dao.countExamsByStudent(studentId)
        else -> 0
    }

    companion object {
        const val COL = "students"
    }
}
