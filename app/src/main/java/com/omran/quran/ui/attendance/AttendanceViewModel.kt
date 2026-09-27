package com.omran.quran.ui.attendance

import android.content.Context
import android.content.Intent
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.omran.quran.data.model.Attendance
import com.omran.quran.data.model.AdminSession
import com.omran.quran.data.model.AttendanceStatus
import com.omran.quran.data.model.Exam
import com.omran.quran.data.model.Memorization
import com.omran.quran.data.model.Revision
import com.omran.quran.data.model.Student
import com.omran.quran.data.repository.AcademicRepository
import com.omran.quran.data.repository.AttendanceRepository
import com.omran.quran.data.repository.AuthRepository
import com.omran.quran.data.repository.SettingsRepository
import com.omran.quran.data.repository.StudentRepository
import com.omran.quran.util.WeekDay
import com.omran.quran.util.dayKeyToMillis
import com.omran.quran.util.displayDayKey
import com.omran.quran.util.toArabicMessage
import com.omran.quran.util.todayKey
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class AttendanceRow(
    val student: Student,
    val status: AttendanceStatus?,
)

data class AttendanceUiState(
    val date: String = todayKey(),
    val rows: List<AttendanceRow> = emptyList(),
    val loading: Boolean = true,
    val saving: Boolean = false,
    val saved: Boolean = false,
    val error: String? = null,
    val history: List<Attendance> = emptyList(),
    val workingDays: Set<String> = WeekDay.DEFAULT_WORKING_DAYS,
    val isHoliday: Boolean = false,
    val currentWeekDay: WeekDay = WeekDay.fromDayKey(todayKey()),
    val exceptionUnlocked: Boolean = false,
    val dayMemorizations: List<Memorization> = emptyList(),
    val dayExams: List<Exam> = emptyList(),
    val dayRevisions: List<Revision> = emptyList(),
)

@HiltViewModel
class AttendanceViewModel @Inject constructor(
    authRepository: AuthRepository,
    private val students: StudentRepository,
    private val attendance: AttendanceRepository,
    private val academic: AcademicRepository,
    private val settings: SettingsRepository,
    private val adminSession: AdminSession,
) : ViewModel() {
    val user = authRepository.currentUser.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), null)
    private val _state = MutableStateFlow(AttendanceUiState())
    val state = _state.asStateFlow()

    init {
        viewModelScope.launch {
            user.filterNotNull().collect { profile ->
                launch {
                    settings.workingDays(profile.userId).collect { days ->
                        val weekDay = WeekDay.fromDayKey(_state.value.date)
                        val isHoliday = !days.contains(weekDay.id)
                        _state.update {
                            it.copy(
                                workingDays = days,
                                isHoliday = isHoliday,
                                currentWeekDay = weekDay,
                            )
                        }
                    }
                }
                load()
            }
        }
        viewModelScope.launch {
            students.updates.collect { load() }
        }
        viewModelScope.launch {
            adminSession.selectedTeacher.collect { load() }
        }
        viewModelScope.launch {
            attendance.updates.collect { load() }
        }
        viewModelScope.launch {
            academic.updates.collect { load() }
        }
    }

    fun onDate(date: String) {
        val weekDay = WeekDay.fromDayKey(date)
        val isHoliday = !_state.value.workingDays.contains(weekDay.id)
        _state.update {
            it.copy(
                date = date,
                currentWeekDay = weekDay,
                isHoliday = isHoliday,
                exceptionUnlocked = false,
            )
        }
        load()
    }

    fun unlockException() {
        _state.update { it.copy(exceptionUnlocked = true) }
    }

    fun setStatus(studentId: String, status: AttendanceStatus) {
        _state.update {
            it.copy(
                saved = false,
                rows = it.rows.map { row ->
                    if (row.student.studentId == studentId) row.copy(status = status) else row
                },
            )
        }
    }

    fun markAllPresent() {
        _state.update {
            it.copy(saved = false, rows = it.rows.map { row -> row.copy(status = AttendanceStatus.PRESENT) })
        }
    }

    fun load() {
        val profile = user.value ?: return
        viewModelScope.launch {
            _state.update { it.copy(loading = true, error = null) }
            runCatching {
                val teacherId = when {
                    !profile.isAdmin -> profile.userId
                    adminSession.selectedTeacherId != null -> adminSession.selectedTeacherId
                    else -> null
                }
                val all = mutableListOf<Student>()
                var last: String? = null
                do {
                    val page = students.page(profile.instituteId, teacherId, "", last)
                    all += page.items
                    last = page.lastId
                    if (page.endReached || all.size >= 500) break
                } while (true)

                val existing = attendance.forDate(profile.instituteId, teacherId, _state.value.date)
                    .associateBy { it.studentId }
                val history = attendance.historyDates(profile.instituteId, teacherId, null).items

                val (dayMems, dayExms, dayRevs) = academic.dayAchievements(
                    profile.instituteId,
                    teacherId,
                    dayKeyToMillis(_state.value.date),
                )

                val rows = all.map { student ->
                    AttendanceRow(student, existing[student.studentId]?.attendanceStatus)
                }

                Triple(rows, history, Triple(dayMems, dayExms, dayRevs))
            }.onSuccess { (rows, history, achs) ->
                _state.update {
                    it.copy(
                        rows = rows,
                        history = history,
                        dayMemorizations = achs.first,
                        dayExams = achs.second,
                        dayRevisions = achs.third,
                        loading = false,
                    )
                }
            }.onFailure { e ->
                _state.update { it.copy(loading = false, error = e.toArabicMessage()) }
            }
        }
    }

    fun save() {
        val profile = user.value ?: return
        val date = _state.value.date
        val records = _state.value.rows.mapNotNull { row ->
            val status = row.status ?: return@mapNotNull null
            Attendance(
                studentId = row.student.studentId,
                studentName = row.student.name,
                teacherId = row.student.teacherId.ifBlank { profile.userId },
                instituteId = profile.instituteId,
                status = status.name,
                date = date,
            )
        }
        if (records.isEmpty()) {
            _state.update { it.copy(error = "حدد حالة طالب واحد على الأقل") }
            return
        }
        viewModelScope.launch {
            _state.update { it.copy(saving = true, error = null) }
            runCatching { attendance.saveAll(records) }
                .onSuccess { _state.update { it.copy(saving = false, saved = true) } }
                .onFailure { e -> _state.update { it.copy(saving = false, error = e.toArabicMessage()) } }
        }
    }

    fun generateDailyReportText(): String {
        val profile = user.value
        val instName = profile?.instituteName?.ifBlank { "معهد المقرئ" } ?: "معهد المقرئ"
        val dateText = displayDayKey(_state.value.date)

        val rows = _state.value.rows
        val presentRows = rows.filter { it.status == AttendanceStatus.PRESENT }
        val absentRows = rows.filter { it.status == AttendanceStatus.ABSENT }
        val lateRows = rows.filter { it.status == AttendanceStatus.LATE }

        val memByStudent = _state.value.dayMemorizations.groupBy { it.studentId }
        val examByStudent = _state.value.dayExams.groupBy { it.studentId }
        val revByStudent = _state.value.dayRevisions.groupBy { it.studentId }

        return buildString {
            appendLine("👑 تقرير التسميع والسَّبْر اليومي 👑")
            appendLine("📅 $dateText")
            appendLine("🌿 $instName 🌿")
            appendLine("> 💎 «ليس المهم أن تكون البداية قوية فقط، بل أن يستمر المثابر حتى يصل إلى الإتقان.»")
            appendLine()

            if (presentRows.isNotEmpty()) {
                appendLine("📊 حصيلة إنجاز الطلاب:")
                presentRows.forEachIndexed { idx, row ->
                    val num = formatArabicIndex(idx + 1)
                    val stId = row.student.studentId
                    val mems = memByStudent[stId].orEmpty()
                    val exms = examByStudent[stId].orEmpty()
                    val revs = revByStudent[stId].orEmpty()

                    appendLine("$num ${row.student.name}")

                    var hasDetails = false
                    if (exms.isNotEmpty()) {
                        val exmText = exms.joinToString(" — ") { e ->
                            val scoreStr = if (e.score > 0) " (${e.mastery} ${e.score}%)" else " (${e.mastery})"
                            "${e.surah}$scoreStr"
                        }
                        appendLine(" * 🎯 السَّبْر: $exmText")
                        hasDetails = true
                    }
                    if (mems.isNotEmpty()) {
                        val memItems = mems.map { m ->
                            val amt = m.amount.ifBlank { "" }
                            val base = if (amt.isNotBlank()) "${m.surah} ($amt)" else m.surah
                            if (m.memorizationCategory == com.omran.quran.data.model.MemorizationCategory.OLD) "$base [سابق]" else base
                        }
                        val rating = mems.firstOrNull()?.rating?.ifBlank { "ممتاز" } ?: "ممتاز"
                        appendLine(" * 📖 الحفظ: ${memItems.joinToString("، ")} ($rating)")
                        hasDetails = true
                    }
                    if (revs.isNotEmpty()) {
                        val revItems = revs.map { r ->
                            if (r.revisionCategory == com.omran.quran.data.model.RevisionCategory.OLD) "${r.content} [قديم]" else r.content
                        }
                        val rating = revs.firstOrNull()?.rating?.ifBlank { "جيد" } ?: "جيد"
                        appendLine(" * 🔄 المراجعة: ${revItems.joinToString("، ")} ($rating)")
                        hasDetails = true
                    }

                    if (!hasDetails) {
                        appendLine(" * ✅ الحضور: حاضر")
                    }
                    appendLine()
                }
            }

            if (absentRows.isNotEmpty()) {
                appendLine("❌ الطلاب الغائبون:")
                absentRows.forEach { r ->
                    appendLine("• ${r.student.name}")
                }
                appendLine()
            }

            if (lateRows.isNotEmpty()) {
                appendLine("⏰ الطلاب المتأخرون:")
                lateRows.forEach { r ->
                    appendLine("• ${r.student.name}")
                }
                appendLine()
            }

            append("👏🏻 بارك الله في جهود أبطالنا ونفع بهم الإسلام والمسلمين.")
        }
    }

    fun shareDailyReport(context: Context) {
        val text = generateDailyReportText()
        val sendIntent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, text)
            type = "text/plain"
        }
        val shareIntent = Intent.createChooser(sendIntent, "مشاركة التقرير اليومي الشامل (نص)")
        context.startActivity(shareIntent)
    }

    fun shareDailyReportPdf(context: Context) {
        val profile = user.value
        val instName = profile?.instituteName?.ifBlank { "معهد المقرئ" } ?: "معهد المقرئ"
        val teacherName = profile?.name.orEmpty().ifBlank { "المعلم" }
        val roleTitle = if (profile?.isAdmin == true) "إدارة المعهد" else "حلقة الأستاذ $teacherName"
        val dateText = displayDayKey(_state.value.date)

        val file = com.omran.quran.util.PdfReportGenerator.generateDailyReportPdf(
            context = context,
            instituteName = instName,
            teacherOrCircleName = roleTitle,
            dateText = dateText,
            rows = _state.value.rows,
            dayMemorizations = _state.value.dayMemorizations,
            dayExams = _state.value.dayExams,
            dayRevisions = _state.value.dayRevisions,
            hijriDateText = com.omran.quran.util.HijriDateHelper.formatHijriFromDateString(_state.value.date, includeDayName = false),
        )

        com.omran.quran.util.PdfReportGenerator.sharePdf(context, file)
    }

    private fun formatArabicIndex(index: Int): String {
        val numberMap = mapOf(
            '0' to "0️⃣", '1' to "1️⃣", '2' to "2️⃣", '3' to "3️⃣", '4' to "4️⃣",
            '5' to "5️⃣", '6' to "6️⃣", '7' to "7️⃣", '8' to "8️⃣", '9' to "9️⃣",
        )
        return index.toString().map { numberMap[it] ?: it.toString() }.joinToString("")
    }
}
