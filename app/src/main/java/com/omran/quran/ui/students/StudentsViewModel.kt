package com.omran.quran.ui.students

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.omran.quran.data.model.AdminSession
import com.omran.quran.data.model.AppUser
import com.omran.quran.data.model.Student
import com.omran.quran.data.repository.AuthRepository
import com.omran.quran.data.repository.StudentRepository
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

data class StudentsUiState(
    val items: List<Student> = emptyList(),
    val query: String = "",
    val genderFilter: String? = null, // null = الكل, "MALE" = بنين, "FEMALE" = بنات
    val loading: Boolean = false,
    val loadingMore: Boolean = false,
    val endReached: Boolean = false,
    val error: String? = null,
)

@OptIn(FlowPreview::class)
@HiltViewModel
class StudentsViewModel @Inject constructor(
    authRepository: AuthRepository,
    private val repository: StudentRepository,
    private val adminSession: AdminSession,
) : ViewModel() {
    val user = authRepository.currentUser.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), null)

    private val _state = MutableStateFlow(StudentsUiState())
    val state = _state.asStateFlow()
    private var lastId: String? = null

    init {
        viewModelScope.launch {
            user.filterNotNull().collect { reload() }
        }
        viewModelScope.launch {
            adminSession.selectedTeacher.collect { reload() }
        }
        viewModelScope.launch {
            _state.map { it.query }.debounce(350).distinctUntilChanged().collect { reload() }
        }
        viewModelScope.launch {
            repository.updates.collect { reload() }
        }
    }

    fun onQuery(value: String) {
        _state.update { it.copy(query = value) }
    }

    fun setGenderFilter(gender: String?) {
        _state.update { it.copy(genderFilter = gender) }
        reload()
    }

    fun forceRefresh() {
        val profile = user.value ?: return
        viewModelScope.launch {
            _state.update { it.copy(loading = true, error = null) }
            repository.refreshAllFromRemote(profile.instituteId)
            reload()
        }
    }

    fun reload() {
        val profile = user.value ?: return
        viewModelScope.launch {
            lastId = null
            _state.update { it.copy(loading = true, error = null, endReached = false) }
            load(profile, append = false)
        }
    }

    fun loadMore() {
        val profile = user.value ?: return
        val s = _state.value
        if (s.loading || s.loadingMore || s.endReached) return
        viewModelScope.launch { load(profile, append = true) }
    }

    private suspend fun load(profile: AppUser, append: Boolean) {
        _state.update { it.copy(loadingMore = append, loading = !append) }
        runCatching {
            repository.page(
                instituteId = profile.instituteId,
                teacherId = if (profile.isAdmin) adminSession.selectedTeacherId else profile.userId,
                queryText = _state.value.query,
                lastId = if (append) lastId else null,
                gender = _state.value.genderFilter,
            )
        }.onSuccess { page ->
            lastId = page.lastId
            _state.update {
                it.copy(
                    items = if (append) it.items + page.items else page.items,
                    loading = false,
                    loadingMore = false,
                    endReached = page.endReached,
                )
            }
        }.onFailure {
            _state.update { it.copy(loading = false, loadingMore = false, error = "تعذر تحميل الطلاب") }
        }
    }
}
