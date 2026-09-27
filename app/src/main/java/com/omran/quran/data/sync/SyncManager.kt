package com.omran.quran.data.sync

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import com.omran.quran.data.local.SyncStatus
import com.omran.quran.data.local.dao.OmranDao
import com.omran.quran.data.local.entity.AttendanceEntity
import com.omran.quran.data.local.entity.ExamEntity
import com.omran.quran.data.local.entity.MemorizationEntity
import com.omran.quran.data.local.entity.RevisionEntity
import com.omran.quran.data.local.entity.StudentEntity
import dagger.hilt.android.qualifiers.ApplicationContext
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.from
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SyncManager @Inject constructor(
    @ApplicationContext private val context: Context,
    private val dao: OmranDao,
    private val supabase: SupabaseClient,
) {
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    private val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as? ConnectivityManager

    private val _isOnline = MutableStateFlow(checkInitialOnlineState())
    val isOnline: StateFlow<Boolean> = _isOnline.asStateFlow()

    private val _isSyncing = MutableStateFlow(false)
    val isSyncing: StateFlow<Boolean> = _isSyncing.asStateFlow()

    init {
        registerNetworkCallback()
        scope.launch {
            if (_isOnline.value) {
                syncPendingData()
            }
        }
    }

    private fun checkInitialOnlineState(): Boolean {
        val cm = connectivityManager ?: return false
        val activeNetwork = cm.activeNetwork ?: return false
        val caps = cm.getNetworkCapabilities(activeNetwork) ?: return false
        return caps.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) &&
                caps.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)
    }

    private fun registerNetworkCallback() {
        val cm = connectivityManager ?: return
        val request = NetworkRequest.Builder()
            .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            .build()

        cm.registerNetworkCallback(request, object : ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) {
                _isOnline.value = true
                scope.launch { syncPendingData() }
            }

            override fun onLost(network: Network) {
                _isOnline.value = false
            }

            override fun onCapabilitiesChanged(network: Network, networkCapabilities: NetworkCapabilities) {
                val hasInternet = networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) &&
                        networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)
                _isOnline.value = hasInternet
                if (hasInternet) {
                    scope.launch { syncPendingData() }
                }
            }
        })
    }

    suspend fun syncPendingData() {
        if (_isSyncing.value || !_isOnline.value) return
        _isSyncing.value = true

        try {
            // 1. Sync Students
            val pendingStudents = dao.getPendingStudents()
            for (student in pendingStudents) {
                when (student.syncStatus) {
                    SyncStatus.PENDING_INSERT, SyncStatus.PENDING_UPDATE -> {
                        runCatching {
                            supabase.from("students").upsert(student.toModel())
                            dao.upsertStudent(student.copy(syncStatus = SyncStatus.SYNCED))
                        }
                    }
                    SyncStatus.PENDING_DELETE -> {
                        runCatching {
                            supabase.from("students").delete { filter { eq("id", student.id) } }
                            dao.deleteStudentPermanently(student.id)
                        }
                    }
                    SyncStatus.SYNCED -> Unit
                }
            }

            // 2. Sync Memorizations
            val pendingMems = dao.getPendingMemorizations()
            for (mem in pendingMems) {
                when (mem.syncStatus) {
                    SyncStatus.PENDING_INSERT, SyncStatus.PENDING_UPDATE -> {
                        runCatching {
                            supabase.from("memorization").upsert(mem.toModel())
                            dao.upsertMemorization(mem.copy(syncStatus = SyncStatus.SYNCED))
                        }
                    }
                    SyncStatus.PENDING_DELETE -> {
                        runCatching {
                            supabase.from("memorization").delete { filter { eq("id", mem.id) } }
                            dao.deleteMemorizationPermanently(mem.id)
                        }
                    }
                    SyncStatus.SYNCED -> Unit
                }
            }

            // 3. Sync Exams
            val pendingExams = dao.getPendingExams()
            for (exam in pendingExams) {
                when (exam.syncStatus) {
                    SyncStatus.PENDING_INSERT, SyncStatus.PENDING_UPDATE -> {
                        runCatching {
                            supabase.from("exams").upsert(exam.toModel())
                            dao.upsertExam(exam.copy(syncStatus = SyncStatus.SYNCED))
                        }
                    }
                    SyncStatus.PENDING_DELETE -> {
                        runCatching {
                            supabase.from("exams").delete { filter { eq("id", exam.id) } }
                            dao.deleteExamPermanently(exam.id)
                        }
                    }
                    SyncStatus.SYNCED -> Unit
                }
            }

            // 4. Sync Revisions
            val pendingRevs = dao.getPendingRevisions()
            for (rev in pendingRevs) {
                when (rev.syncStatus) {
                    SyncStatus.PENDING_INSERT, SyncStatus.PENDING_UPDATE -> {
                        runCatching {
                            supabase.from("revisions").upsert(rev.toModel())
                            dao.upsertRevision(rev.copy(syncStatus = SyncStatus.SYNCED))
                        }
                    }
                    SyncStatus.PENDING_DELETE -> {
                        runCatching {
                            supabase.from("revisions").delete { filter { eq("id", rev.id) } }
                            dao.deleteRevisionPermanently(rev.id)
                        }
                    }
                    SyncStatus.SYNCED -> Unit
                }
            }

            // 5. Sync Attendance
            val pendingAtts = dao.getPendingAttendances()
            for (att in pendingAtts) {
                when (att.syncStatus) {
                    SyncStatus.PENDING_INSERT, SyncStatus.PENDING_UPDATE -> {
                        runCatching {
                            supabase.from("attendance").upsert(att.toModel())
                            dao.upsertAttendance(att.copy(syncStatus = SyncStatus.SYNCED))
                        }
                    }
                    SyncStatus.PENDING_DELETE -> {
                        runCatching {
                            supabase.from("attendance").delete { filter { eq("id", att.id) } }
                            dao.deleteAttendancePermanently(att.id)
                        }
                    }
                    SyncStatus.SYNCED -> Unit
                }
            }
        } finally {
            _isSyncing.value = false
        }
    }
}
