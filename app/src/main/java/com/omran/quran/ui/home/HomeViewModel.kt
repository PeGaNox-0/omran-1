package com.omran.quran.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.omran.quran.data.model.AdminSession
import com.omran.quran.data.model.AppUser
import com.omran.quran.data.model.DashboardStats
import com.omran.quran.data.repository.AcademicRepository
import com.omran.quran.data.repository.AttendanceRepository
import com.omran.quran.data.repository.AuthRepository
import com.omran.quran.data.repository.StudentRepository
import com.omran.quran.data.sync.SyncManager
import com.omran.quran.util.todayKey
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

data class HomeUiState(
    val stats: DashboardStats = DashboardStats(),
    val loading: Boolean = true,
    val error: String? = null,
)

@HiltViewModel
class HomeViewModel @Inject constructor(
    authRepository: AuthRepository,
    private val students: StudentRepository,
    private val academic: AcademicRepository,
    private val attendance: AttendanceRepository,
    private val syncManager: SyncManager,
    private val adminSession: AdminSession,
) : ViewModel() {
    val user = authRepository.currentUser.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), null)
    val isOnline = syncManager.isOnline
    val isSyncing = syncManager.isSyncing

    fun syncNow() {
        viewModelScope.launch {
            syncManager.syncPendingData()
            refresh()
        }
    }

    private val _state = MutableStateFlow(HomeUiState())
    val state = _state.asStateFlow()

    init {
        viewModelScope.launch {
            user.filterNotNull().collect { refresh(it) }
        }
        viewModelScope.launch {
            adminSession.selectedTeacher.collect { refresh() }
        }
        viewModelScope.launch {
            students.updates.collect { refresh() }
        }
        viewModelScope.launch {
            academic.updates.collect { refresh() }
        }
        viewModelScope.launch {
            attendance.updates.collect { refresh() }
        }
    }

    fun refresh(current: AppUser? = user.value) {
        val profile = current ?: return
        viewModelScope.launch {
            _state.value = _state.value.copy(loading = true, error = null)
            runCatching {
                val scopeTeacher = when {
                    !profile.isAdmin -> profile.userId
                    adminSession.selectedTeacherId != null -> adminSession.selectedTeacherId
                    else -> null
                }
                val count = students.count(profile.instituteId, scopeTeacher)
                val (present, absent, late) = attendance.countsForDate(
                    profile.instituteId,
                    scopeTeacher,
                    todayKey(),
                )
                val mem = academic.countToday(AcademicRepository.MEM, profile.instituteId, scopeTeacher)
                val exams = academic.countToday(AcademicRepository.EXAMS, profile.instituteId, scopeTeacher)
                val rev = academic.countToday(AcademicRepository.REV, profile.instituteId, scopeTeacher)
                DashboardStats(count, present, absent, late, mem, exams, rev)
            }.onSuccess {
                _state.value = HomeUiState(stats = it, loading = false)
            }.onFailure {
                _state.value = _state.value.copy(loading = false, error = "تعذر تحميل الإحصائيات. تحقق من الاتصال")
            }
        }
    }
}
