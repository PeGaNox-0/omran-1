package com.omran.quran.ui.records

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.omran.quran.data.model.AppUser
import com.omran.quran.data.model.Exam
import com.omran.quran.data.model.Memorization
import com.omran.quran.data.model.Revision
import com.omran.quran.data.model.Student
import com.omran.quran.data.repository.AcademicRepository
import com.omran.quran.data.repository.AuthRepository
import com.omran.quran.data.repository.StudentRepository
import com.omran.quran.util.QuranData
import com.omran.quran.util.Ratings
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

import com.omran.quran.data.model.MemorizationCategory
import com.omran.quran.data.model.RevisionCategory

enum class EntryMode {
    BY_SURAH,
    BY_JUZ,
}

data class RecordFormState(
    val students: List<Student> = emptyList(),
    val studentId: String = "",
    val studentName: String = "",
    val entryMode: EntryMode = EntryMode.BY_SURAH,
    val isMultiSelect: Boolean = false,
    val surah: String = QuranData.surahs.first().name,
    val selectedSurahs: Set<String> = setOf(QuranData.surahs.first().name),
    val selectedJuz: String = QuranData.juzList.first().startDescription,
    val selectedAjzaa: Set<String> = emptySet(),
    val surahFilterQuery: String = "",
    val fromVerse: String = "1",
    val toVerse: String = "7",
    val isFullSurah: Boolean = true,
    val amount: String = "سورة الفاتحة كاملة",
    val memorizationCategory: MemorizationCategory = MemorizationCategory.NEW,
    val revisionCategory: RevisionCategory = RevisionCategory.NEW,
    val rating: String = Ratings.memorization.first(),
    val score: String = "100",
    val mistakes: String = "",
    val mastery: String = Ratings.mastery.first(),
    val level: String = Ratings.revisionLevel.first(),
    val content: String = "",
    val note: String = "",
    val saving: Boolean = false,
    val error: String? = null,
) {
    val currentSurahMaxVerses: Int
        get() = QuranData.getVerseCount(surah)
}

@HiltViewModel
class RecordFormViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    authRepository: AuthRepository,
    private val studentsRepo: StudentRepository,
    private val academic: AcademicRepository,
) : ViewModel() {
    val type: RecordType = RecordType.valueOf(savedStateHandle["type"] ?: RecordType.MEMORIZATION.name)
    private val presetStudentId: String = savedStateHandle["studentId"] ?: ""
    val recordId: String? = savedStateHandle.get<String>("recordId")?.ifBlank { null }
    private var originalDate: Long? = null

    val user = authRepository.currentUser.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), null)
    private val _state = MutableStateFlow(RecordFormState())
    val state = _state.asStateFlow()
    private val _saved = MutableSharedFlow<Unit>()
    val saved = _saved.asSharedFlow()

    init {
        viewModelScope.launch {
            val profile = user.filterNotNull().first()
            val all = mutableListOf<Student>()
            var last: String? = null
            do {
                val page = studentsRepo.page(
                    instituteId = profile.instituteId,
                    teacherId = if (profile.isAdmin) null else profile.userId,
                    queryText = "",
                    lastId = last,
                )
                all += page.items
                last = page.lastId
                if (page.endReached || all.size >= 500) break
            } while (true)

            val selected = all.find { it.studentId == presetStudentId }
            val firstSurah = QuranData.surahs.first()

            _state.update {
                it.copy(
                    students = all,
                    studentId = selected?.studentId.orEmpty().ifBlank { presetStudentId },
                    studentName = selected?.name.orEmpty(),
                    surah = firstSurah.name,
                    fromVerse = "1",
                    toVerse = firstSurah.verseCount.toString(),
                    isFullSurah = true,
                    amount = "سورة ${firstSurah.name} كاملة",
                    rating = if (type == RecordType.REVISION) Ratings.revision.first() else Ratings.memorization.first(),
                )
            }

            // Load existing record if in EDIT mode
            if (!recordId.isNullOrBlank()) {
                when (type) {
                    RecordType.MEMORIZATION -> {
                        academic.getMemorization(recordId)?.let { mem ->
                            originalDate = mem.date
                            val sStudent = all.find { it.studentId == mem.studentId }
                            val isJuz = QuranData.isJuzEntry(mem.surah)
                            val matchedJuz = if (isJuz) {
                                val jNum = QuranData.extractJuzNumber(mem.surah) ?: 1
                                QuranData.juzList.find { it.number == jNum } ?: QuranData.juzList.first()
                            } else null

                            _state.update { s ->
                                s.copy(
                                    studentId = mem.studentId,
                                    studentName = sStudent?.name ?: mem.studentName,
                                    entryMode = if (isJuz) EntryMode.BY_JUZ else EntryMode.BY_SURAH,
                                    surah = mem.surah,
                                    selectedJuz = matchedJuz?.startDescription ?: s.selectedJuz,
                                    fromVerse = mem.fromVerse.toString(),
                                    toVerse = mem.toVerse.toString(),
                                    amount = mem.amount,
                                    rating = mem.rating,
                                    memorizationCategory = mem.memorizationCategory,
                                    note = mem.note,
                                )
                            }
                        }
                    }
                    RecordType.EXAM -> {
                        academic.getExam(recordId)?.let { ex ->
                            originalDate = ex.date
                            val sStudent = all.find { it.studentId == ex.studentId }
                            _state.update { s ->
                                s.copy(
                                    studentId = ex.studentId,
                                    studentName = sStudent?.name ?: ex.studentName,
                                    surah = ex.surah,
                                    score = ex.score.toString(),
                                    mistakes = ex.mistakes,
                                    mastery = ex.mastery,
                                    note = ex.note,
                                )
                            }
                        }
                    }
                    RecordType.REVISION -> {
                        academic.getRevision(recordId)?.let { rev ->
                            originalDate = rev.date
                            val sStudent = all.find { it.studentId == rev.studentId }
                            _state.update { s ->
                                s.copy(
                                    studentId = rev.studentId,
                                    studentName = sStudent?.name ?: rev.studentName,
                                    content = rev.content,
                                    level = rev.level,
                                    rating = rev.rating,
                                    revisionCategory = rev.revisionCategory,
                                    note = rev.note,
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    fun update(transform: (RecordFormState) -> RecordFormState) {
        _state.update { transform(it).copy(error = null) }
    }

    fun selectSurah(surahName: String) {
        val count = QuranData.getVerseCount(surahName)
        _state.update {
            it.copy(
                surah = surahName,
                fromVerse = "1",
                toVerse = count.toString(),
                isFullSurah = true,
                amount = "سورة $surahName كاملة",
                content = if (it.entryMode == EntryMode.BY_SURAH) surahName else it.content,
            )
        }
    }

    fun selectJuz(juzDesc: String) {
        val juz = QuranData.juzList.find { it.startDescription == juzDesc } ?: QuranData.juzList.first()
        _state.update {
            it.copy(
                selectedJuz = juzDesc,
                surah = "الجزء ${juz.number}",
                fromVerse = "1",
                toVerse = "1",
                isFullSurah = true,
                amount = "${juz.name} كاملاً",
                content = juz.name,
            )
        }
    }

    fun setFullSurah() {
        val count = QuranData.getVerseCount(_state.value.surah)
        _state.update {
            it.copy(
                fromVerse = "1",
                toVerse = count.toString(),
                isFullSurah = true,
                amount = "سورة ${it.surah} كاملة",
            )
        }
    }

    fun adjustFromVerse(delta: Int) {
        val current = _state.value.fromVerse.toIntOrNull() ?: 1
        val max = _state.value.currentSurahMaxVerses
        val next = (current + delta).coerceIn(1, max)
        val to = _state.value.toVerse.toIntOrNull() ?: max
        val adjustedTo = if (next > to) next else to
        _state.update {
            it.copy(
                fromVerse = next.toString(),
                toVerse = adjustedTo.toString(),
                isFullSurah = next == 1 && adjustedTo == max,
                amount = if (next == 1 && adjustedTo == max) "سورة ${it.surah} كاملة" else "الآيات $next - $adjustedTo",
            )
        }
    }

    fun adjustToVerse(delta: Int) {
        val current = _state.value.toVerse.toIntOrNull() ?: _state.value.currentSurahMaxVerses
        val max = _state.value.currentSurahMaxVerses
        val from = _state.value.fromVerse.toIntOrNull() ?: 1
        val next = (current + delta).coerceIn(from, max)
        _state.update {
            it.copy(
                toVerse = next.toString(),
                isFullSurah = from == 1 && next == max,
                amount = if (from == 1 && next == max) "سورة ${it.surah} كاملة" else "الآيات $from - $next",
            )
        }
    }

    fun toggleMultiSelect(enabled: Boolean) {
        _state.update { s ->
            s.copy(
                isMultiSelect = enabled,
                selectedSurahs = if (s.selectedSurahs.isEmpty()) setOf(s.surah) else s.selectedSurahs,
                selectedAjzaa = if (s.selectedAjzaa.isEmpty()) setOf(s.selectedJuz) else s.selectedAjzaa,
            )
        }
    }

    fun toggleSurahSelection(surahName: String) {
        _state.update { s ->
            val set = s.selectedSurahs.toMutableSet()
            if (set.contains(surahName)) {
                if (set.size > 1) set.remove(surahName)
            } else {
                set.add(surahName)
            }
            s.copy(
                selectedSurahs = set,
                surah = set.firstOrNull() ?: s.surah,
            )
        }
    }

    fun selectSurahsOfJuz(juzNumber: Int) {
        val jInfo = QuranData.getJuz(juzNumber) ?: return
        _state.update { s ->
            s.copy(
                selectedSurahs = s.selectedSurahs + jInfo.surahs,
                isMultiSelect = true,
            )
        }
    }

    fun selectAllSurahs() {
        _state.update { s ->
            s.copy(
                selectedSurahs = QuranData.surahs.map { it.name }.toSet(),
                isMultiSelect = true,
            )
        }
    }

    fun clearSurahSelection() {
        _state.update { s ->
            s.copy(selectedSurahs = setOf(QuranData.surahs.first().name))
        }
    }

    fun toggleJuzSelection(juzDesc: String) {
        _state.update { s ->
            val set = s.selectedAjzaa.toMutableSet()
            if (set.contains(juzDesc)) {
                if (set.size > 1) set.remove(juzDesc)
            } else {
                set.add(juzDesc)
            }
            s.copy(
                selectedAjzaa = set,
                selectedJuz = set.firstOrNull() ?: s.selectedJuz,
            )
        }
    }

    fun selectAllAjzaa() {
        _state.update { s ->
            s.copy(
                selectedAjzaa = QuranData.juzList.map { it.startDescription }.toSet(),
                isMultiSelect = true,
            )
        }
    }

    fun clearJuzSelection() {
        _state.update { s ->
            s.copy(selectedAjzaa = setOf(QuranData.juzList.first().startDescription))
        }
    }

    fun setSurahFilter(query: String) {
        _state.update { it.copy(surahFilterQuery = query) }
    }

    fun save() {
        val profile = user.value ?: return
        val s = _state.value
        if (s.studentId.isBlank()) {
            _state.update { it.copy(error = "اختر الطالب أولاً") }
            return
        }
        viewModelScope.launch {
            _state.update { it.copy(saving = true, error = null) }
            runCatching { persist(profile, s) }
                .onSuccess { _saved.emit(Unit) }
                .onFailure { e -> _state.update { it.copy(error = e.toArabicMessage()) } }
            _state.update { it.copy(saving = false) }
        }
    }

    private suspend fun persist(profile: AppUser, s: RecordFormState) {
        val now = originalDate ?: System.currentTimeMillis()

        if (s.isMultiSelect && recordId == null) {
            // Batch saving multiple items
            if (s.entryMode == EntryMode.BY_SURAH) {
                for (surahName in s.selectedSurahs) {
                    val count = QuranData.getVerseCount(surahName)
                    val id = java.util.UUID.randomUUID().toString()
                    when (type) {
                        RecordType.MEMORIZATION -> academic.addMemorization(
                            Memorization(
                                id = id,
                                studentId = s.studentId,
                                studentName = s.studentName,
                                teacherId = profile.userId,
                                instituteId = profile.instituteId,
                                surah = surahName,
                                fromVerse = 1,
                                toVerse = count,
                                amount = "سورة $surahName كاملة",
                                rating = s.rating,
                                category = s.memorizationCategory.name,
                                note = s.note,
                                date = now,
                            ),
                        )
                        RecordType.EXAM -> academic.addExam(
                            Exam(
                                id = id,
                                studentId = s.studentId,
                                studentName = s.studentName,
                                teacherId = profile.userId,
                                instituteId = profile.instituteId,
                                surah = surahName,
                                score = s.score.toIntOrNull() ?: 100,
                                mistakes = s.mistakes,
                                mastery = s.mastery,
                                note = s.note,
                                date = now,
                            ),
                        )
                        RecordType.REVISION -> academic.addRevision(
                            Revision(
                                id = id,
                                studentId = s.studentId,
                                studentName = s.studentName,
                                teacherId = profile.userId,
                                instituteId = profile.instituteId,
                                content = "سورة $surahName كاملة",
                                level = s.level,
                                rating = s.rating,
                                category = s.revisionCategory.name,
                                note = s.note,
                                date = now,
                            ),
                        )
                    }
                }
            } else {
                val targets = if (s.selectedAjzaa.isNotEmpty()) s.selectedAjzaa else setOf(s.selectedJuz)
                for (juzDesc in targets) {
                    val jNum = QuranData.extractJuzNumber(juzDesc) ?: 1
                    val id = java.util.UUID.randomUUID().toString()
                    val targetSurah = "الجزء $jNum"
                    val targetAmount = "الجزء $jNum كاملاً"
                    when (type) {
                        RecordType.MEMORIZATION -> academic.addMemorization(
                            Memorization(
                                id = id,
                                studentId = s.studentId,
                                studentName = s.studentName,
                                teacherId = profile.userId,
                                instituteId = profile.instituteId,
                                surah = targetSurah,
                                fromVerse = 1,
                                toVerse = 1,
                                amount = targetAmount,
                                rating = s.rating,
                                category = s.memorizationCategory.name,
                                note = s.note,
                                date = now,
                            ),
                        )
                        RecordType.EXAM -> academic.addExam(
                            Exam(
                                id = id,
                                studentId = s.studentId,
                                studentName = s.studentName,
                                teacherId = profile.userId,
                                instituteId = profile.instituteId,
                                surah = targetSurah,
                                score = s.score.toIntOrNull() ?: 100,
                                mistakes = s.mistakes,
                                mastery = s.mastery,
                                note = s.note,
                                date = now,
                            ),
                        )
                        RecordType.REVISION -> academic.addRevision(
                            Revision(
                                id = id,
                                studentId = s.studentId,
                                studentName = s.studentName,
                                teacherId = profile.userId,
                                instituteId = profile.instituteId,
                                content = targetAmount,
                                level = s.level,
                                rating = s.rating,
                                category = s.revisionCategory.name,
                                note = s.note,
                                date = now,
                            ),
                        )
                    }
                }
            }
            return
        }

        val targetSurah = if (s.entryMode == EntryMode.BY_JUZ) s.selectedJuz.substringBefore(" (") else s.surah
        val targetAmount = if (s.amount.isNotBlank()) s.amount else if (s.entryMode == EntryMode.BY_JUZ) s.selectedJuz else "الآيات ${s.fromVerse} - ${s.toVerse}"
        val chosenId = recordId ?: java.util.UUID.randomUUID().toString()

        when (type) {
            RecordType.MEMORIZATION -> academic.addMemorization(
                Memorization(
                    id = chosenId,
                    studentId = s.studentId,
                    studentName = s.studentName,
                    teacherId = profile.userId,
                    instituteId = profile.instituteId,
                    surah = targetSurah,
                    fromVerse = s.fromVerse.toIntOrNull() ?: 1,
                    toVerse = s.toVerse.toIntOrNull() ?: s.currentSurahMaxVerses,
                    amount = targetAmount,
                    rating = s.rating,
                    category = s.memorizationCategory.name,
                    note = s.note,
                    date = now,
                ),
            )
            RecordType.EXAM -> academic.addExam(
                Exam(
                    id = chosenId,
                    studentId = s.studentId,
                    studentName = s.studentName,
                    teacherId = profile.userId,
                    instituteId = profile.instituteId,
                    surah = targetSurah,
                    score = s.score.toIntOrNull() ?: 100,
                    mistakes = s.mistakes,
                    mastery = s.mastery,
                    note = s.note,
                    date = now,
                ),
            )
            RecordType.REVISION -> academic.addRevision(
                Revision(
                    id = chosenId,
                    studentId = s.studentId,
                    studentName = s.studentName,
                    teacherId = profile.userId,
                    instituteId = profile.instituteId,
                    content = if (s.content.isNotBlank()) s.content else targetAmount,
                    level = s.level,
                    rating = s.rating,
                    category = s.revisionCategory.name,
                    note = s.note,
                    date = now,
                ),
            )
        }
    }
}
