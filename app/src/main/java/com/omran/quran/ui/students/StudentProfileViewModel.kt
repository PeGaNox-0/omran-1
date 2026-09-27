package com.omran.quran.ui.students

import android.content.Context
import android.content.Intent
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.omran.quran.data.model.AppUser
import com.omran.quran.data.model.Attendance
import com.omran.quran.data.model.AttendanceStatus
import com.omran.quran.data.model.Exam
import com.omran.quran.data.model.Memorization
import com.omran.quran.data.model.Revision
import com.omran.quran.data.model.Student
import com.omran.quran.data.repository.AcademicRepository
import com.omran.quran.data.repository.AttendanceRepository
import com.omran.quran.data.repository.AuthRepository
import com.omran.quran.data.repository.StudentRepository
import com.omran.quran.util.PdfReportGenerator
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

enum class StudentReportType(val title: String) {
    PARENT("تقرير ولي الأمر 👨‍👩‍👧"),
    ADMIN("تقرير الإدارة الأكاديمي 🏢"),
}

data class StudentAnalytics(
    val totalMemorizations: Int = 0,
    val newMemorizationsCount: Int = 0,
    val oldMemorizationsCount: Int = 0,
    val distinctAjzaaCount: Int = 0,
    val distinctAjzaaList: List<String> = emptyList(),
    val distinctSurahsCount: Int = 0,
    val distinctSurahsList: List<String> = emptyList(),
    val quranCompletionPercentage: Int = 0,
    val totalExams: Int = 0,
    val averageScore: Double = 0.0,
    val totalRevisions: Int = 0,
    val newRevisionsCount: Int = 0,
    val oldRevisionsCount: Int = 0,
    val totalAttendanceDays: Int = 0,
    val presentDays: Int = 0,
    val absentDays: Int = 0,
    val lateDays: Int = 0,
    val attendancePercentage: Int = 100,
    val juzBreakdown: List<com.omran.quran.util.JuzProgress> = emptyList(),
)

data class StudentProfileState(
    val student: Student? = null,
    val memorizations: List<Memorization> = emptyList(),
    val exams: List<Exam> = emptyList(),
    val revisions: List<Revision> = emptyList(),
    val attendance: List<Attendance> = emptyList(),
    val analytics: StudentAnalytics = StudentAnalytics(),
    val loading: Boolean = true,
)

@HiltViewModel
class StudentProfileViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val students: StudentRepository,
    private val academic: AcademicRepository,
    private val attendanceRepo: AttendanceRepository,
    private val auth: AuthRepository,
    @ApplicationContext private val context: Context,
) : ViewModel() {
    val studentId: String = savedStateHandle["studentId"] ?: ""
    val user: StateFlow<AppUser?> = auth.currentUser.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = null,
    )

    private val _state = MutableStateFlow(StudentProfileState())
    val state = _state.asStateFlow()

    private val _deleted = MutableSharedFlow<Unit>()
    val deleted = _deleted.asSharedFlow()

    init {
        refresh()
        viewModelScope.launch { students.updates.collect { refresh() } }
        viewModelScope.launch { academic.updates.collect { refresh() } }
        viewModelScope.launch { attendanceRepo.updates.collect { refresh() } }
    }

    fun deleteMemorization(id: String) {
        viewModelScope.launch {
            academic.deleteMemorization(id)
            refresh()
        }
    }

    fun deleteExam(id: String) {
        viewModelScope.launch {
            academic.deleteExam(id)
            refresh()
        }
    }

    fun deleteRevision(id: String) {
        viewModelScope.launch {
            academic.deleteRevision(id)
            refresh()
        }
    }

    fun refresh() {
        if (studentId.isBlank()) return
        viewModelScope.launch {
            _state.update { it.copy(loading = true) }
            val student = students.get(studentId)
            val instituteId = student?.instituteId.orEmpty()
            val memorizations = academic.memorizations(instituteId, null, studentId, "", null, null).items
            val exams = academic.exams(instituteId, null, studentId, "", null, null).items
            val revisions = academic.revisions(instituteId, null, studentId, "", null, null).items
            val attendance = attendanceRepo.studentHistory(studentId, null).items

            // Calculate smart Quran progress
            val rawEntries = memorizations.map { it.surah } + memorizations.map { it.amount } + exams.map { it.surah } + revisions.map { it.content }
            val quranProgress = com.omran.quran.util.QuranData.calculateQuranProgress(rawEntries)

            val avgScore = if (exams.isNotEmpty()) exams.map { it.score }.average() else 0.0
            val presentCount = attendance.count { it.attendanceStatus == com.omran.quran.data.model.AttendanceStatus.PRESENT }
            val absentCount = attendance.count { it.attendanceStatus == com.omran.quran.data.model.AttendanceStatus.ABSENT }
            val lateCount = attendance.count { it.attendanceStatus == com.omran.quran.data.model.AttendanceStatus.LATE }
            val totalDays = attendance.size
            val attPct = if (totalDays > 0) ((presentCount + lateCount).toDouble() / totalDays * 100).toInt() else 100

            val newMemCount = memorizations.count { it.memorizationCategory == com.omran.quran.data.model.MemorizationCategory.NEW }
            val oldMemCount = memorizations.count { it.memorizationCategory == com.omran.quran.data.model.MemorizationCategory.OLD }
            val newRevCount = revisions.count { it.revisionCategory == com.omran.quran.data.model.RevisionCategory.NEW }
            val oldRevCount = revisions.count { it.revisionCategory == com.omran.quran.data.model.RevisionCategory.OLD }

            val analytics = StudentAnalytics(
                totalMemorizations = memorizations.size,
                newMemorizationsCount = newMemCount,
                oldMemorizationsCount = oldMemCount,
                distinctAjzaaCount = quranProgress.ajzaaCount,
                distinctAjzaaList = quranProgress.ajzaaList,
                distinctSurahsCount = quranProgress.surahsCount,
                distinctSurahsList = quranProgress.surahsList,
                quranCompletionPercentage = quranProgress.completionPercentage,
                totalExams = exams.size,
                averageScore = avgScore,
                totalRevisions = revisions.size,
                newRevisionsCount = newRevCount,
                oldRevisionsCount = oldRevCount,
                totalAttendanceDays = totalDays,
                presentDays = presentCount,
                absentDays = absentCount,
                lateDays = lateCount,
                attendancePercentage = attPct,
                juzBreakdown = quranProgress.juzBreakdown,
            )

            _state.update {
                it.copy(
                    student = student,
                    memorizations = memorizations,
                    exams = exams,
                    revisions = revisions,
                    attendance = attendance,
                    analytics = analytics,
                    loading = false,
                )
            }
        }
    }

    fun deleteStudent() {
        viewModelScope.launch {
            _state.update { it.copy(loading = true) }
            runCatching {
                students.delete(studentId)
            }.onSuccess {
                _deleted.emit(Unit)
            }
            _state.update { it.copy(loading = false) }
        }
    }

    fun generateReportText(type: StudentReportType = StudentReportType.PARENT): String {
        val s = _state.value
        val st = s.student ?: return ""
        val an = s.analytics
        val dateFormat = java.text.SimpleDateFormat("yyyy/MM/dd", java.util.Locale("ar"))
        val today = dateFormat.format(java.util.Date())
        val todayHijri = com.omran.quran.util.HijriDateHelper.getTodayHijri(includeDayName = false)

        val lastMem = s.memorizations.firstOrNull()
        val lastExam = s.exams.firstOrNull()
        val lastRev = s.revisions.firstOrNull()

        return when (type) {
            StudentReportType.PARENT -> buildString {
                appendLine("🌟 *تقرير إنجاز الطالب القرآني* 🌟")
                appendLine("━━━━━━━━━━━━━━━━━━━")
                appendLine("👤 *الاسم:* ${st.name}")
                if (st.age > 0) appendLine("🎂 *العمر:* ${st.age} سنة")
                if (st.teacherName.isNotBlank()) appendLine("👨‍🏫 *المعلم:* ${st.teacherName}")
                appendLine("📅 *تاريخ التقرير:* $today م  •  🌙 $todayHijri")
                appendLine("━━━━━━━━━━━━━━━━━━━")
                appendLine("📖 *مجمل الحفظ القرآني:*")
                if (an.distinctAjzaaCount > 0) {
                    appendLine("• الأجزاء المنجزة: ${an.distinctAjzaaCount} جزءاً من 30 (إتمام ${an.quranCompletionPercentage}%)")
                    appendLine("  الأجزاء: ${an.distinctAjzaaList.joinToString("، ")}")
                }
                appendLine("• عدد السور المتقنة: ${an.distinctSurahsCount} سورة")
                appendLine("• إجمالي عمليات التسميع: ${an.totalMemorizations} (${an.newMemorizationsCount} جديد | ${an.oldMemorizationsCount} محفوظ سابق)")
                if (an.distinctSurahsList.isNotEmpty()) {
                    appendLine("• السور المنجزة: ${an.distinctSurahsList.take(12).joinToString("، ")}${if (an.distinctSurahsList.size > 12) " وغيرها..." else ""}")
                }
                if (lastMem != null) {
                    val catLabel = if (lastMem.memorizationCategory == com.omran.quran.data.model.MemorizationCategory.OLD) " [محفوظ سابق]" else ""
                    appendLine("• آخر تسميع: ${lastMem.surah} (${lastMem.amount})$catLabel - التقييم: ${lastMem.rating}")
                }
                appendLine()
                appendLine("🎯 *مجمل السبر والاختبارات:*")
                appendLine("• عدد الاختبارات: ${an.totalExams}")
                if (an.totalExams > 0) {
                    appendLine("• متوسط الدرجة: ${String.format(java.util.Locale.US, "%.1f", an.averageScore)} / 100")
                }
                if (lastExam != null) {
                    appendLine("• آخر سبر: ${lastExam.surah} - الدرجة: ${lastExam.score} (${lastExam.mastery})")
                }
                appendLine()
                appendLine("🔄 *المراجعة والتمكين:*")
                appendLine("• عدد المراجعات: ${an.totalRevisions} (${an.newRevisionsCount} جديد | ${an.oldRevisionsCount} سابق)")
                if (lastRev != null) {
                    appendLine("• آخر مراجعة: ${lastRev.content} (${lastRev.rating})")
                }
                appendLine()
                appendLine("📊 *الالتزام والحضور:*")
                appendLine("• نسبة الحضور: ${an.attendancePercentage}%")
                appendLine("• الأيام المسجلة: ${an.totalAttendanceDays} (حاضر: ${an.presentDays} | غائب: ${an.absentDays} | متأخر: ${an.lateDays})")
                appendLine("━━━━━━━━━━━━━━━━━━━")
                appendLine("✨ _تطبيق معهد المقرئ لإدارة حلقات القرآن الكريم_ ✨")
            }

            StudentReportType.ADMIN -> buildString {
                val profile = user.value
                val instName = profile?.instituteName?.ifBlank { "معهد المقرئ" } ?: "معهد المقرئ"
                appendLine("📋 *تقرير إداري أكاديمي للطالب*")
                appendLine("🏢 $instName")
                appendLine("👤 الطالب: ${st.name} | المعلم: ${st.teacherName.ifBlank { "—" }}")
                appendLine("📅 تاريخ الإصدار: $today م  •  🌙 $todayHijri")
                appendLine("━━━━━━━━━━━━━━━━━━━")
                appendLine("📊 *المؤشرات الأكاديمية:*")
                if (an.distinctAjzaaCount > 0) {
                    appendLine("• الأجزاء المنجزة: ${an.distinctAjzaaCount} من 30 جزءاً (إتمام ${an.quranCompletionPercentage}%)")
                }
                appendLine("• السور المتقنة: ${an.distinctSurahsCount} سورة")
                appendLine("• اختبارات السَّبْر: ${an.totalExams} (المتوسط: ${if (an.totalExams > 0) String.format(java.util.Locale.US, "%.1f", an.averageScore) else "0"}%)")
                appendLine("• إجمالي الحفظ والتسميع: ${an.totalMemorizations} عملية (${an.newMemorizationsCount} جديد / ${an.oldMemorizationsCount} سابق)")
                appendLine("• إجمالي المراجعة: ${an.totalRevisions} عملية (${an.newRevisionsCount} جديد / ${an.oldRevisionsCount} سابق)")
                appendLine("• سجل الحضور: ${an.attendancePercentage}% (${an.presentDays} حاضر / ${an.absentDays} غائب / ${an.lateDays} متأخر)")
                if (an.distinctAjzaaList.isNotEmpty()) {
                    appendLine()
                    appendLine("🕋 *الأجزاء المنجزة:*")
                    appendLine(an.distinctAjzaaList.joinToString(" • "))
                }
                if (an.distinctSurahsList.isNotEmpty()) {
                    appendLine()
                    appendLine("📖 *السور المنجزة:*")
                    appendLine(an.distinctSurahsList.joinToString(" • "))
                }
                if (s.exams.isNotEmpty()) {
                    appendLine()
                    appendLine("🎯 *آخر اختبارات السبر:*")
                    s.exams.take(5).forEach { ex ->
                        val target = com.omran.quran.util.QuranData.formatQuranTargetName(ex.surah)
                        appendLine("• $target: ${ex.score}% (${ex.mastery})")
                    }
                }
                if (s.memorizations.isNotEmpty()) {
                    appendLine()
                    appendLine("📖 *آخر عمليات التسميع:*")
                    s.memorizations.take(5).forEach { m ->
                        val target = com.omran.quran.util.QuranData.formatQuranTargetName(m.surah)
                        val amt = m.amount.ifBlank { "الآيات ${m.fromVerse}-${m.toVerse}" }
                        val tag = if (m.memorizationCategory == com.omran.quran.data.model.MemorizationCategory.OLD) "[سابق] " else "[جديد] "
                        appendLine("• $tag $target ($amt) — ${m.rating}")
                    }
                }
                if (s.revisions.isNotEmpty()) {
                    appendLine()
                    appendLine("🔄 *آخر عمليات المراجعة:*")
                    s.revisions.take(5).forEach { r ->
                        val tag = if (r.revisionCategory == com.omran.quran.data.model.RevisionCategory.OLD) "[قديم] " else "[جديد] "
                        appendLine("• $tag ${r.content} — ${r.rating}")
                    }
                }
            }
        }
    }

    fun shareReport(context: Context, type: StudentReportType = StudentReportType.PARENT) {
        val text = generateReportText(type)
        if (text.isBlank()) return
        val sendIntent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, text)
            this.type = "text/plain"
        }
        val title = if (type == StudentReportType.ADMIN) "مشاركة التقرير الإداري" else "مشاركة تقرير الطالب لولي الأمر"
        val shareIntent = Intent.createChooser(sendIntent, title)
        shareIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(shareIntent)
    }

    fun shareReportPdf(context: Context, type: StudentReportType = StudentReportType.PARENT) {
        val s = _state.value
        val st = s.student ?: return
        val profile = user.value
        val instName = profile?.instituteName?.ifBlank { "معهد المقرئ" } ?: "معهد المقرئ"

        val file = if (type == StudentReportType.ADMIN) {
            PdfReportGenerator.generateStudentAdminPdf(
                context = context,
                student = st,
                analytics = s.analytics,
                memorizations = s.memorizations,
                exams = s.exams,
                revisions = s.revisions,
                instituteName = instName,
            )
        } else {
            PdfReportGenerator.generateStudentProfilePdf(
                context = context,
                student = st,
                analytics = s.analytics,
                memorizations = s.memorizations,
                exams = s.exams,
                revisions = s.revisions,
                instituteName = instName,
            )
        }

        PdfReportGenerator.sharePdf(context, file)
    }
}
