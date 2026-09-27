package com.omran.quran.ui.records

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.omran.quran.data.model.AdminSession
import com.omran.quran.data.model.AppUser
import com.omran.quran.data.model.Exam
import com.omran.quran.data.model.Memorization
import com.omran.quran.data.model.Revision
import com.omran.quran.data.repository.AcademicRepository
import com.omran.quran.data.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

enum class RecordType { MEMORIZATION, EXAM, REVISION }

data class RecordsUiState(
    val memorizations: List<Memorization> = emptyList(),
    val exams: List<Exam> = emptyList(),
    val revisions: List<Revision> = emptyList(),
    val query: String = "",
    val teacherId: String? = null,
    val dayMillis: Long? = null,
    val loading: Boolean = false,
    val loadingMore: Boolean = false,
    val endReached: Boolean = false,
    val teachers: List<AppUser> = emptyList(),
)

@OptIn(FlowPreview::class)
open class RecordsViewModel(
    private val type: RecordType,
    private val authRepository: AuthRepository,
    private val academic: AcademicRepository,
    private val adminSession: AdminSession,
) : ViewModel() {
    val user = authRepository.currentUser.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), null)
    private val _state = MutableStateFlow(RecordsUiState())
    val state = _state.asStateFlow()
    private var lastId: String? = null

    init {
        viewModelScope.launch {
            user.filterNotNull().collect { profile ->
                if (profile.isAdmin) {
                    _state.update { it.copy(teachers = authRepository.teachers(profile.instituteId)) }
                }
                reload()
            }
        }
        viewModelScope.launch {
            _state.map { Triple(it.query, it.teacherId, it.dayMillis) }
                .debounce(300)
                .distinctUntilChanged()
                .collect { reload() }
        }
        viewModelScope.launch {
            academic.updates.collect { reload() }
        }
        viewModelScope.launch {
            adminSession.selectedTeacher.collect { reload() }
        }
    }

    fun onQuery(value: String) = _state.update { it.copy(query = value) }
    fun onTeacher(value: String?) = _state.update { it.copy(teacherId = value) }
    fun onDay(value: Long?) = _state.update { it.copy(dayMillis = value) }

    fun reload() {
        val profile = user.value ?: return
        viewModelScope.launch {
            lastId = null
            load(profile, false)
        }
    }

    fun loadMore() {
        val profile = user.value ?: return
        val s = _state.value
        if (s.loading || s.loadingMore || s.endReached) return
        viewModelScope.launch { load(profile, true) }
    }

    private suspend fun load(profile: AppUser, append: Boolean) {
        _state.update { it.copy(loading = !append, loadingMore = append) }
        val teacherFilter = when {
            !profile.isAdmin -> profile.userId
            adminSession.selectedTeacherId != null -> adminSession.selectedTeacherId
            !_state.value.teacherId.isNullOrBlank() -> _state.value.teacherId
            else -> null
        }
        val s = _state.value
        runCatching {
            when (type) {
                RecordType.MEMORIZATION -> {
                    val page = academic.memorizations(profile.instituteId, teacherFilter, null, s.query, s.dayMillis, if (append) lastId else null)
                    lastId = page.lastId
                    _state.update {
                        it.copy(
                            memorizations = if (append) it.memorizations + page.items else page.items,
                            endReached = page.endReached,
                            loading = false,
                            loadingMore = false,
                        )
                    }
                }
                RecordType.EXAM -> {
                    val page = academic.exams(profile.instituteId, teacherFilter, null, s.query, s.dayMillis, if (append) lastId else null)
                    lastId = page.lastId
                    _state.update {
                        it.copy(
                            exams = if (append) it.exams + page.items else page.items,
                            endReached = page.endReached,
                            loading = false,
                            loadingMore = false,
                        )
                    }
                }
                RecordType.REVISION -> {
                    val page = academic.revisions(profile.instituteId, teacherFilter, null, s.query, s.dayMillis, if (append) lastId else null)
                    lastId = page.lastId
                    _state.update {
                        it.copy(
                            revisions = if (append) it.revisions + page.items else page.items,
                            endReached = page.endReached,
                            loading = false,
                            loadingMore = false,
                        )
                    }
                }
            }
        }.onFailure {
            _state.update { it.copy(loading = false, loadingMore = false) }
        }
    }

    fun deleteItem(id: String) {
        viewModelScope.launch {
            when (type) {
                RecordType.MEMORIZATION -> academic.deleteMemorization(id)
                RecordType.EXAM -> academic.deleteExam(id)
                RecordType.REVISION -> academic.deleteRevision(id)
            }
            reload()
        }
    }
}

@HiltViewModel
class MemorizationListViewModel @Inject constructor(
    authRepository: AuthRepository,
    academic: AcademicRepository,
    adminSession: AdminSession,
) : RecordsViewModel(RecordType.MEMORIZATION, authRepository, academic, adminSession)

@HiltViewModel
class ExamListViewModel @Inject constructor(
    authRepository: AuthRepository,
    academic: AcademicRepository,
    adminSession: AdminSession,
) : RecordsViewModel(RecordType.EXAM, authRepository, academic, adminSession)

@HiltViewModel
class RevisionListViewModel @Inject constructor(
    authRepository: AuthRepository,
    academic: AcademicRepository,
    adminSession: AdminSession,
) : RecordsViewModel(RecordType.REVISION, authRepository, academic, adminSession)
