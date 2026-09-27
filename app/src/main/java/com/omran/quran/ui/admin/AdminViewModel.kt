package com.omran.quran.ui.admin

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.omran.quran.data.model.AdminSession
import com.omran.quran.data.model.AppUser
import com.omran.quran.data.model.CircleStats
import com.omran.quran.data.model.Student
import com.omran.quran.data.repository.AcademicRepository
import com.omran.quran.data.repository.AttendanceRepository
import com.omran.quran.data.repository.AuthRepository
import com.omran.quran.data.repository.StudentRepository
import com.omran.quran.data.sync.SyncManager
import com.omran.quran.util.PdfReportGenerator
import com.omran.quran.util.toArabicMessage
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import javax.inject.Inject

// ── أقسام لوحة تحكم المدير ────────────────────────────────────────────────
enum class AdminSection {
    DASHBOARD,    // الصفحة الرئيسية مع الإحصائيات ومربعات التنقل
    CIRCLES,      // قائمة الحلقات
    DISTINGUISHED, // المتميزون
    REPORTS,      // التقارير
}

data class AdminUiState(
    // ── بيانات الحلقات ────────────────────────────────────────────────────
    val teachers: List<AppUser> = emptyList(),
    val loading: Boolean = true,
    val error: String? = null,
    // ── القسم الحالي ──────────────────────────────────────────────────────
    val section: AdminSection = AdminSection.DASHBOARD,
    // ── إحصائيات الـ Dashboard ───────────────────────────────────────────
    val totalStudents: Int = 0,
    val totalMaleStudents: Int = 0,
    val totalFemaleStudents: Int = 0,
    val averageAttendancePercentage: Int = 0,
    val totalAttendanceRecords: Int = 0,
    val presentAttendanceCount: Int = 0,
    val absentAttendanceCount: Int = 0,
    val statsLoading: Boolean = false,
    // ── إحصائيات التقارير ─────────────────────────────────────────────────
    val selectedReportTeacher: AppUser? = null,      // null = تقرير كلي
    val reportStats: CircleStats? = null,
    val reportLoading: Boolean = false,
    val reportGenderFilter: String? = null,          // null = الكل, "MALE" = بنين, "FEMALE" = بنات
    // ── نافذة إنشاء حلقة جديدة ───────────────────────────────────────────
    val isAddTeacherOpen: Boolean = false,
    val circleName: String = "",
    val teacherName: String = "",
    val teacherEmail: String = "",
    val teacherPassword: String = "",
    val circleGender: String = "MALE",               // "MALE" = بنين (ذكور), "FEMALE" = بنات (إناث)
    val circlesGenderFilter: String? = null,         // null = الكل, "MALE" = بنين, "FEMALE" = بنات
    val savingTeacher: Boolean = false,
    val addError: String? = null,
    // ── الطلاب المتميزون ───────────────────────────────────────────────────
    val topStudents: List<Student> = emptyList(),
    val topStudentsLoading: Boolean = false,
    val topStudentsGenderFilter: String? = null,     // null = الكل, "MALE" = بنين, "FEMALE" = بنات
    // ── توليد PDF ─────────────────────────────────────────────────────────
    val pdfGenerating: Boolean = false,
    val pdfFile: File? = null,
    // ── نافذة تعديل الحلقة والمعلم ────────────────────────────────────────
    val isEditCircleOpen: Boolean = false,
    val editingCircle: AppUser? = null,
    val editCircleName: String = "",
    val editTeacherName: String = "",
    val editCircleGender: String = "MALE",
    val savingEdit: Boolean = false,
    val editError: String? = null,
    val successMessage: String? = null,
)

@HiltViewModel
class AdminViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val studentRepository: StudentRepository,
    private val academicRepository: AcademicRepository,
    private val attendanceRepository: AttendanceRepository,
    private val syncManager: SyncManager,
    val adminSession: AdminSession,
    @ApplicationContext private val context: Context,
) : ViewModel() {

    val user = authRepository.currentUser.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5_000),
        null,
    )

    val selectedTeacher = adminSession.selectedTeacher

    private val _state = MutableStateFlow(AdminUiState())
    val state = _state.asStateFlow()

    init {
        viewModelScope.launch {
            user.filterNotNull().collect {
                loadTeachers(it)
                studentRepository.refreshAllFromRemote(it.instituteId)
                loadDashboardStats(it.instituteId)
            }
        }
        viewModelScope.launch {
            authRepository.teacherUpdates.collect {
                user.value?.let { loadTeachers(it) }
            }
        }
        viewModelScope.launch {
            attendanceRepository.updates.collect {
                user.value?.instituteId?.let { loadDashboardStats(it) }
            }
        }
    }

    // ── تحميل إحصائيات الـ Dashboard ────────────────────────────────────────

    private fun loadDashboardStats(instituteId: String) {
        viewModelScope.launch {
            _state.update { it.copy(statsLoading = true) }
            runCatching {
                val stats = studentRepository.getCircleStats(instituteId, null)
                val attStats = attendanceRepository.getInstituteAttendanceStats(instituteId)
                _state.update {
                    it.copy(
                        totalStudents = stats.studentCount,
                        totalMaleStudents = stats.maleStudentCount,
                        totalFemaleStudents = stats.femaleStudentCount,
                        averageAttendancePercentage = attStats.percentage,
                        totalAttendanceRecords = attStats.totalRecords,
                        presentAttendanceCount = attStats.presentCount,
                        absentAttendanceCount = attStats.absentCount,
                        statsLoading = false,
                    )
                }
            }.onFailure {
                _state.update { it.copy(statsLoading = false) }
            }
        }
    }

    // ── التنقل بين الأقسام ──────────────────────────────────────────────────

    fun navigateTo(section: AdminSection) {
        _state.update { it.copy(section = section) }
        val profile = user.value ?: return
        when (section) {
            AdminSection.REPORTS -> loadReportStats(_state.value.selectedReportTeacher, _state.value.reportGenderFilter)
            AdminSection.DISTINGUISHED -> loadTopStudents(profile.instituteId, _state.value.topStudentsGenderFilter)
            else -> {}
        }
    }

    fun navigateToDashboard() {
        _state.update { it.copy(section = AdminSection.DASHBOARD) }
    }

    // ── قسم المتميزين ──────────────────────────────────────────────────

    fun setTopStudentsGender(gender: String?) {
        _state.update { it.copy(topStudentsGenderFilter = gender) }
        val profile = user.value ?: return
        loadTopStudents(profile.instituteId, gender)
    }

    private fun loadTopStudents(instituteId: String, gender: String? = _state.value.topStudentsGenderFilter) {
        viewModelScope.launch {
            _state.update { it.copy(topStudentsLoading = true) }
            runCatching { studentRepository.getTopStudents(instituteId, limit = 15, gender = gender) }
                .onSuccess { list -> _state.update { it.copy(topStudents = list, topStudentsLoading = false) } }
                .onFailure { _state.update { it.copy(topStudentsLoading = false) } }
        }
    }

    // ── إحصائيات التقارير ──────────────────────────────────────────────────

    fun setReportGenderFilter(gender: String?) {
        _state.update { it.copy(reportGenderFilter = gender) }
        loadReportStats(_state.value.selectedReportTeacher, gender)
    }

    fun selectReportTeacher(teacher: AppUser?) {
        _state.update { it.copy(selectedReportTeacher = teacher, reportStats = null) }
        loadReportStats(teacher, _state.value.reportGenderFilter)
    }

    private fun loadReportStats(teacher: AppUser?, gender: String? = _state.value.reportGenderFilter) {
        val profile = user.value ?: return
        viewModelScope.launch {
            _state.update { it.copy(reportLoading = true) }
            runCatching {
                val stats = studentRepository.getCircleStats(
                    instituteId = profile.instituteId,
                    teacherId = teacher?.userId,
                    gender = gender,
                )
                _state.update { it.copy(reportStats = stats, reportLoading = false) }
            }.onFailure {
                _state.update { it.copy(reportLoading = false) }
            }
        }
    }

    // ── توليد PDF للتقارير ───────────────────────────────────────────────

    fun generateCirclePdf() {
        val profile = user.value ?: return
        val reportStats = _state.value.reportStats ?: return
        val selectedTeacher = _state.value.selectedReportTeacher
        val currentGender = _state.value.reportGenderFilter
        viewModelScope.launch {
            _state.update { it.copy(pdfGenerating = true, pdfFile = null) }
            runCatching {
                withContext(Dispatchers.IO) {
                    val students = studentRepository.getStudentsByTeacher(
                        instituteId = profile.instituteId,
                        teacherId = selectedTeacher?.userId,
                        gender = currentGender,
                    )
                    val memCounts = students.associate { s ->
                        s.studentId to studentRepository.countByStudent(s.studentId, "mem")
                    }
                    val revCounts = students.associate { s ->
                        s.studentId to studentRepository.countByStudent(s.studentId, "rev")
                    }
                    val examCounts = students.associate { s ->
                        s.studentId to studentRepository.countByStudent(s.studentId, "exam")
                    }
                    PdfReportGenerator.generateCircleReportPdf(
                        context = context,
                        instituteName = profile.instituteName.ifBlank { "معهد المقرئ" },
                        circleName = selectedTeacher?.displayCircleName ?: "المعهد بأكمله",
                        teacherName = selectedTeacher?.name ?: "",
                        stats = reportStats,
                        students = students,
                        memCounts = memCounts,
                        revCounts = revCounts,
                        examCounts = examCounts,
                        genderFilter = currentGender,
                    )
                }
            }.onSuccess { file ->
                _state.update { it.copy(pdfGenerating = false, pdfFile = file) }
            }.onFailure { e ->
                _state.update { it.copy(pdfGenerating = false, error = "تعذر توليد التقرير: ${e.message}") }
            }
        }
    }

    fun clearPdfFile() {
        _state.update { it.copy(pdfFile = null) }
    }

    fun refreshAll() {
        val profile = user.value ?: return
        viewModelScope.launch {
            _state.update { it.copy(loading = true, error = null) }
            runCatching {
                syncManager.syncPendingData()
                loadTeachers(profile)
                studentRepository.refreshAllFromRemote(profile.instituteId)
                loadDashboardStats(profile.instituteId)
                // أعد تحميل إحصائيات التقارير إذا كان القسم مفتوحاً
                if (_state.value.section == AdminSection.REPORTS) {
                    loadReportStats(_state.value.selectedReportTeacher)
                }
                _state.update {
                    it.copy(
                        loading = false,
                        successMessage = "تم تحديث ومزامنة بيانات المعهد والطلاب بنجاح",
                    )
                }
            }.onFailure { e ->
                _state.update {
                    it.copy(
                        loading = false,
                        error = "تعذر تحديث البيانات: ${e.toArabicMessage()}",
                    )
                }
            }
        }
    }

    private suspend fun loadTeachers(profile: AppUser) {
        if (!profile.isAdmin) return
        _state.update { it.copy(loading = true, error = null) }
        runCatching { authRepository.teachers(profile.instituteId) }
            .onSuccess { list -> _state.update { it.copy(teachers = list, loading = false) } }
            .onFailure { e -> _state.update { it.copy(loading = false, error = "تعذر تحميل قائمة الحلقات") } }
    }

    fun selectTeacher(teacher: AppUser) {
        adminSession.selectTeacher(teacher)
    }

    fun clearSelectedTeacher() {
        adminSession.clear()
    }

    fun setCirclesGenderFilter(gender: String?) {
        _state.update { it.copy(circlesGenderFilter = gender) }
    }

    // ── إنشاء حلقة جديدة ──────────────────────────────────────────────────

    fun openAddCircle() {
        _state.update {
            it.copy(
                isAddTeacherOpen = true,
                circleName = "",
                teacherName = "",
                teacherEmail = "",
                teacherPassword = "",
                circleGender = "MALE",
                addError = null,
            )
        }
    }

    fun openAddTeacher() = openAddCircle()

    fun closeAddCircle() {
        _state.update { it.copy(isAddTeacherOpen = false, addError = null) }
    }

    fun closeAddTeacher() = closeAddCircle()

    fun updateCircleForm(
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
                addError = null,
            )
        }
    }

    fun updateTeacherForm(name: String? = null, email: String? = null, pass: String? = null) {
        updateCircleForm(name = name, email = email, pass = pass)
    }

    fun saveCircle() {
        val s = _state.value
        val profile = user.value ?: return
        if (s.circleName.isBlank() || s.teacherName.isBlank() || s.teacherEmail.isBlank() || s.teacherPassword.length < 6) {
            _state.update { it.copy(addError = "أدخل اسم الحلقة واسم المعلم والبريد وكلمة المرور (6 أحرف على الأقل)") }
            return
        }
        viewModelScope.launch {
            _state.update { it.copy(savingTeacher = true, addError = null) }
            runCatching {
                authRepository.createTeacherAccount(
                    instituteId = profile.instituteId,
                    instituteName = profile.instituteName,
                    circleName = s.circleName.trim(),
                    name = s.teacherName.trim(),
                    email = s.teacherEmail.trim(),
                    password = s.teacherPassword,
                    circleGender = s.circleGender,
                )
            }.onSuccess {
                _state.update {
                    it.copy(
                        isAddTeacherOpen = false,
                        savingTeacher = false,
                        successMessage = "تم إنشاء الحلقة وحساب المعلم بنجاح ✓",
                    )
                }
                loadDashboardStats(profile.instituteId)
            }.onFailure { e ->
                _state.update { it.copy(savingTeacher = false, addError = e.toArabicMessage()) }
            }
        }
    }

    fun saveTeacher() = saveCircle()

    // ── تعديل الحلقة والمعلم ────────────────────────────────────────────────

    fun openEditCircle(circle: AppUser) {
        _state.update {
            it.copy(
                isEditCircleOpen = true,
                editingCircle = circle,
                editCircleName = circle.displayCircleName,
                editTeacherName = circle.name,
                editCircleGender = circle.circleGender.ifBlank { "MALE" },
                editError = null,
            )
        }
    }

    fun closeEditCircle() {
        _state.update { it.copy(isEditCircleOpen = false, editingCircle = null, editError = null) }
    }

    fun updateEditCircleForm(
        circleName: String? = null,
        teacherName: String? = null,
        circleGender: String? = null,
    ) {
        _state.update {
            it.copy(
                editCircleName = circleName ?: it.editCircleName,
                editTeacherName = teacherName ?: it.editTeacherName,
                editCircleGender = circleGender ?: it.editCircleGender,
                editError = null,
            )
        }
    }

    fun saveEditedCircle() {
        val s = _state.value
        val circle = s.editingCircle ?: return
        if (s.editCircleName.isBlank() || s.editTeacherName.isBlank()) {
            _state.update { it.copy(editError = "أدخل اسم الحلقة واسم المعلم") }
            return
        }
        viewModelScope.launch {
            _state.update { it.copy(savingEdit = true, editError = null) }
            runCatching {
                authRepository.updateCircle(
                    userId = circle.userId,
                    circleName = s.editCircleName.trim(),
                    teacherName = s.editTeacherName.trim(),
                    circleGender = s.editCircleGender,
                )
            }.onSuccess {
                _state.update {
                    it.copy(
                        isEditCircleOpen = false,
                        editingCircle = null,
                        savingEdit = false,
                        successMessage = "تم تعديل بيانات الحلقة والمعلم بنجاح ✓",
                    )
                }
            }.onFailure { e ->
                _state.update { it.copy(savingEdit = false, editError = e.toArabicMessage()) }
            }
        }
    }

    // ── حذف حلقة ──────────────────────────────────────────────────────────

    fun deleteTeacher(teacherId: String) {
        viewModelScope.launch {
            runCatching { authRepository.deleteTeacher(teacherId) }
                .onSuccess { _state.update { it.copy(successMessage = "تم حذف الحلقة بنجاح") } }
        }
    }

    fun clearSuccessMessage() {
        _state.update { it.copy(successMessage = null) }
    }
}
