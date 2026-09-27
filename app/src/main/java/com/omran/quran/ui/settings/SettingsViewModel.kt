package com.omran.quran.ui.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.omran.quran.data.model.AppUser
import com.omran.quran.data.model.Institute
import com.omran.quran.data.repository.AuthRepository
import com.omran.quran.data.repository.SettingsRepository
import com.omran.quran.util.WeekDay
import com.omran.quran.util.toArabicMessage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class SettingsUiState(
    val institute: Institute? = null,
    val teachers: List<AppUser> = emptyList(),
    val darkTheme: Boolean? = null,
    val workingDays: Set<String> = WeekDay.DEFAULT_WORKING_DAYS,
    val isAddTeacherOpen: Boolean = false,
    val circleName: String = "",
    val teacherName: String = "",
    val teacherEmail: String = "",
    val teacherPassword: String = "",
    val circleGender: String = "MALE", // "MALE" = بنين, "FEMALE" = بنات
    val savingTeacher: Boolean = false,
    val error: String? = null,
    val successMessage: String? = null,
)

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val settings: SettingsRepository,
) : ViewModel() {
    val user = authRepository.currentUser.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), null)
    val darkTheme = settings.darkTheme.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), null)

    private val _state = MutableStateFlow(SettingsUiState())
    val state = _state.asStateFlow()

    init {
        viewModelScope.launch {
            user.filterNotNull().collect { profile ->
                refreshTeachers(profile)
                launch {
                    settings.workingDays(profile.userId).collect { days ->
                        _state.update { it.copy(workingDays = days) }
                    }
                }
            }
        }
        viewModelScope.launch {
            authRepository.teacherUpdates.collect {
                user.value?.let { refreshTeachers(it) }
            }
        }
    }

    private suspend fun refreshTeachers(profile: AppUser) {
        val institute = authRepository.institute(profile.instituteId)
        val teachers = if (profile.isAdmin) authRepository.teachers(profile.instituteId) else emptyList()
        _state.update {
            it.copy(
                institute = institute,
                teachers = teachers,
            )
        }
    }

    fun toggleWorkingDay(dayId: String) {
        val profile = user.value ?: return
        val current = _state.value.workingDays.toMutableSet()
        if (current.contains(dayId)) {
            if (current.size > 1) current.remove(dayId)
        } else {
            current.add(dayId)
        }
        viewModelScope.launch {
            settings.setWorkingDays(profile.userId, current)
        }
    }

    fun openAddTeacherDialog() {
        _state.update {
            it.copy(
                isAddTeacherOpen = true,
                circleName = "",
                teacherName = "",
                teacherEmail = "",
                teacherPassword = "",
                circleGender = "MALE",
                error = null,
                successMessage = null,
            )
        }
    }

    fun closeAddTeacherDialog() {
        _state.update { it.copy(isAddTeacherOpen = false, error = null) }
    }

    fun updateTeacherForm(
        circle: String? = null,
        name: String? = null,
        email: String? = null,
        pass: String? = null,
        circleGender: String? = null,
    ) {
        _state.update {
            it.copy(
                circleName = circle ?: it.circleName,
                teacherName = name ?: it.teacherName,
                teacherEmail = email ?: it.teacherEmail,
                teacherPassword = pass ?: it.teacherPassword,
                circleGender = circleGender ?: it.circleGender,
                error = null,
            )
        }
    }

    fun saveTeacher() {
        val s = _state.value
        val profile = user.value ?: return
        if (s.circleName.isBlank() || s.teacherName.isBlank() || s.teacherEmail.isBlank() || s.teacherPassword.length < 6) {
            _state.update { it.copy(error = "أدخل اسم الحلقة وبيانات المعلم بالكامل (كلمة المرور 6 أحرف على الأقل)") }
            return
        }
        viewModelScope.launch {
            _state.update { it.copy(savingTeacher = true, error = null) }
            runCatching {
                authRepository.createTeacherAccount(
                    instituteId = profile.instituteId,
                    instituteName = profile.instituteName,
                    name = s.teacherName.trim(),
                    email = s.teacherEmail.trim(),
                    password = s.teacherPassword,
                    circleName = s.circleName.trim(),
                    circleGender = s.circleGender,
                )
            }.onSuccess {
                refreshTeachers(profile)
                _state.update {
                    it.copy(
                        isAddTeacherOpen = false,
                        savingTeacher = false,
                        successMessage = "تم إنشاء الحلقة بنجاح",
                    )
                }
            }.onFailure { e ->
                _state.update {
                    it.copy(
                        savingTeacher = false,
                        error = e.toArabicMessage(),
                    )
                }
            }
        }
    }

    fun deleteTeacher(teacherId: String) {
        val profile = user.value ?: return
        viewModelScope.launch {
            runCatching {
                authRepository.deleteTeacher(teacherId)
            }.onSuccess {
                refreshTeachers(profile)
                _state.update { it.copy(successMessage = "تم حذف المعلم من المعهد بنجاح") }
            }
        }
    }

    fun clearSuccessMessage() {
        _state.update { it.copy(successMessage = null) }
    }

    fun setDark(enabled: Boolean) {
        viewModelScope.launch { settings.setDarkTheme(enabled) }
    }

    fun signOut() {
        viewModelScope.launch { authRepository.signOut() }
    }
}
