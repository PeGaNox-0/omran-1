package com.omran.quran.ui.students

import android.net.Uri
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.omran.quran.data.model.AdminSession
import com.omran.quran.data.model.AppUser
import com.omran.quran.data.model.Student
import com.omran.quran.data.repository.AuthRepository
import com.omran.quran.data.repository.StudentRepository
import com.omran.quran.util.toArabicMessage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class StudentEditorState(
    val name: String = "",
    val age: String = "",
    val gender: String = "MALE", // "MALE" = بنين / "FEMALE" = بنات
    val parentPhone: String = "",
    val circleName: String = "",
    val teacherId: String = "",
    val teacherName: String = "",
    val joinDate: Long? = null,
    val photoUrl: String = "",
    val localPhoto: Uri? = null,
    val teachers: List<AppUser> = emptyList(),
    val saving: Boolean = false,
    val error: String? = null,
)

@HiltViewModel
class StudentEditorViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    authRepository: AuthRepository,
    private val students: StudentRepository,
    private val auth: AuthRepository,
    private val adminSession: AdminSession,
) : ViewModel() {
    private val rawId: String = savedStateHandle["studentId"] ?: "new"
    private val studentId: String = rawId.takeIf { it != "new" }.orEmpty()
    val user = authRepository.currentUser.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), null)

    private val _state = MutableStateFlow(StudentEditorState())
    val state = _state.asStateFlow()

    private val _saved = MutableSharedFlow<Unit>()
    val saved = _saved.asSharedFlow()

    init {
        viewModelScope.launch {
            val profile = user.filterNotNull().first()
            val teachers = if (profile.isAdmin) auth.teachers(profile.instituteId) else emptyList()
            val existing = if (studentId.isNotBlank()) students.get(studentId) else null
            val selectedTeacher = adminSession.selectedTeacher.value

            val defaultCircle = existing?.circleName?.ifBlank { null }
                ?: if (profile.isAdmin && selectedTeacher != null) selectedTeacher.displayCircleName
                else if (!profile.isAdmin) profile.displayCircleName
                else teachers.firstOrNull()?.displayCircleName.orEmpty()

            val defaultTeacherId = existing?.teacherId?.ifBlank { null }
                ?: if (profile.isAdmin && selectedTeacher != null) selectedTeacher.userId
                else if (!profile.isAdmin) profile.userId
                else teachers.firstOrNull()?.userId.orEmpty()

            val defaultTeacherName = existing?.teacherName?.ifBlank { null }
                ?: if (profile.isAdmin && selectedTeacher != null) selectedTeacher.name
                else if (!profile.isAdmin) profile.name
                else teachers.firstOrNull()?.name.orEmpty()

            val defaultGender = existing?.gender?.ifBlank { null }
                ?: if (profile.isAdmin && selectedTeacher != null) selectedTeacher.circleGender
                else if (!profile.isAdmin) profile.circleGender
                else teachers.firstOrNull()?.circleGender ?: "MALE"

            _state.update {
                it.copy(
                    teachers = teachers,
                    name = existing?.name.orEmpty(),
                    age = existing?.age?.takeIf { age -> age > 0 }?.toString().orEmpty(),
                    gender = defaultGender,
                    parentPhone = existing?.parentPhone.orEmpty(),
                    circleName = defaultCircle,
                    teacherId = defaultTeacherId,
                    teacherName = defaultTeacherName,
                    joinDate = existing?.joinDate,
                    photoUrl = existing?.photoUrl.orEmpty(),
                )
            }
        }
    }

    fun update(transform: (StudentEditorState) -> StudentEditorState) {
        _state.update { transform(it).copy(error = null) }
    }

    fun save() {
        val profile = user.value ?: return
        val s = _state.value
        if (s.name.isBlank()) {
            _state.update { it.copy(error = "أدخل اسم الطالب") }
            return
        }
        val teacherId = s.teacherId.ifBlank { profile.userId }
        val teacher = s.teachers.find { it.userId == teacherId }
        val teacherName = s.teacherName.ifBlank { teacher?.name ?: profile.name }
        val circleName = s.circleName.ifBlank { teacher?.displayCircleName ?: profile.displayCircleName }
        val circleGender = teacher?.circleGender
            ?: if (!profile.isAdmin) profile.circleGender
            else (adminSession.selectedTeacher.value?.circleGender ?: s.gender.ifBlank { "MALE" })

        viewModelScope.launch {
            _state.update { it.copy(saving = true, error = null) }
            runCatching {
                var id = studentId
                val student = Student(
                    studentId = id,
                    name = s.name.trim(),
                    age = s.age.toIntOrNull() ?: 0,
                    gender = circleGender,
                    parentPhone = s.parentPhone.trim(),
                    teacherId = teacherId,
                    teacherName = teacherName,
                    circleName = circleName.trim(),
                    instituteId = profile.instituteId,
                    photoUrl = s.photoUrl,
                    joinDate = s.joinDate,
                )
                id = students.save(student)
                val photo = s.localPhoto
                if (photo != null) {
                    val url = students.uploadPhoto(id, photo)
                    students.save(student.copy(studentId = id, photoUrl = url))
                }
            }.onSuccess {
                _saved.emit(Unit)
            }.onFailure { e ->
                _state.update { it.copy(error = e.toArabicMessage()) }
            }
            _state.update { it.copy(saving = false) }
        }
    }
}
