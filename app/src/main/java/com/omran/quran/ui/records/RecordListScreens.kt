package com.omran.quran.ui.records

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.adamglin.PhosphorIcons
import com.adamglin.phosphoricons.Regular
import com.adamglin.phosphoricons.regular.PencilSimple
import com.adamglin.phosphoricons.regular.Plus
import com.adamglin.phosphoricons.regular.Trash
import com.adamglin.phosphoricons.regular.Warning
import androidx.compose.foundation.layout.width
import com.omran.quran.ui.components.DarkGlassBackground
import com.omran.quran.ui.components.EmptyState
import com.omran.quran.ui.components.LoadingBox
import com.omran.quran.ui.components.OmranCard
import com.omran.quran.ui.components.SearchField
import com.omran.quran.ui.components.listBottomPadding
import com.omran.quran.ui.theme.GlassMuted
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import com.omran.quran.ui.theme.GlassSilver
import com.omran.quran.ui.theme.GlassWhite
import com.omran.quran.util.toDateText

@Composable
fun MemorizationListScreen(
    onAdd: () -> Unit,
    onEdit: (studentId: String, recordId: String) -> Unit = { _, _ -> },
    vm: MemorizationListViewModel = hiltViewModel(),
) {
    var itemToDelete by remember { mutableStateOf<com.omran.quran.data.model.Memorization?>(null) }

    RecordsScaffold("سجل الحفظ اليومي", onAdd, vm) { state ->
        if (state.memorizations.isEmpty()) EmptyState("لا توجد عمليات حفظ مسجلة")
        else LazyColumn(contentPadding = listBottomPadding(), verticalArrangement = Arrangement.spacedBy(10.dp)) {
            itemsIndexed(state.memorizations, key = { _, item -> item.id }) { index, item ->
                if (index == state.memorizations.lastIndex && !state.endReached && !state.loadingMore) {
                    androidx.compose.runtime.LaunchedEffect(state.memorizations.size) { vm.loadMore() }
                }
                OmranCard {
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            Text(item.studentName, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = GlassWhite)
                            val isOld = item.memorizationCategory == com.omran.quran.data.model.MemorizationCategory.OLD
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(if (isOld) Color(0xFF0284C7).copy(alpha = 0.25f) else Color(0xFF10B981).copy(alpha = 0.25f))
                                    .border(1.dp, if (isOld) Color(0xFF38BDF8).copy(alpha = 0.5f) else Color(0xFF34D399).copy(alpha = 0.5f), RoundedCornerShape(8.dp))
                                    .padding(horizontal = 8.dp, vertical = 3.dp),
                            ) {
                                Text(
                                    text = item.memorizationCategory.shortLabel,
                                    style = MaterialTheme.typography.labelSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = if (isOld) Color(0xFF7DD3FC) else Color(0xFF6EE7B7),
                                )
                            }
                        }
                        Text(item.date.toDateText(), style = MaterialTheme.typography.bodySmall, color = GlassMuted)
                    }
                    Text("سورة ${item.surah}  (الآيات ${item.fromVerse} إلى ${item.toVerse})", color = GlassSilver)
                    Text("التقييم: ${item.rating}", color = GlassWhite, fontWeight = FontWeight.SemiBold)
                    if (item.note.isNotBlank()) Text("ملاحظة: ${item.note}", style = MaterialTheme.typography.bodySmall, color = GlassMuted)

                    Spacer(Modifier.height(4.dp))
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End, verticalAlignment = Alignment.CenterVertically) {
                        TextButton(onClick = { onEdit(item.studentId, item.id) }) {
                            Icon(PhosphorIcons.Regular.PencilSimple, contentDescription = "تعديل", Modifier.size(16.dp), tint = Color(0xFF38BDF8))
                            Spacer(Modifier.width(4.dp))
                            Text("تعديل", color = Color(0xFF38BDF8), style = MaterialTheme.typography.labelMedium)
                        }
                        Spacer(Modifier.width(4.dp))
                        TextButton(onClick = { itemToDelete = item }) {
                            Icon(PhosphorIcons.Regular.Trash, contentDescription = "حذف", Modifier.size(16.dp), tint = Color(0xFFF87171))
                            Spacer(Modifier.width(4.dp))
                            Text("حذف", color = Color(0xFFF87171), style = MaterialTheme.typography.labelMedium)
                        }
                    }
                }
            }
        }
    }

    if (itemToDelete != null) {
        val item = itemToDelete!!
        androidx.compose.material3.AlertDialog(
            onDismissRequest = { itemToDelete = null },
            icon = { Icon(PhosphorIcons.Regular.Warning, contentDescription = null, tint = Color(0xFFF87171), modifier = Modifier.size(28.dp)) },
            title = { Text("تأكيد حذف التسميع", fontWeight = FontWeight.Bold, color = GlassWhite) },
            text = { Text("هل أنت متأكد من حذف سجل تسميع (${item.studentName} - سورة ${item.surah})؟", color = GlassSilver) },
            containerColor = Color(0xFF14171E),
            confirmButton = {
                androidx.compose.material3.Button(
                    onClick = {
                        vm.deleteItem(item.id)
                        itemToDelete = null
                    },
                    colors = androidx.compose.material3.ButtonDefaults.buttonColors(containerColor = Color(0xFFDC2626)),
                    shape = RoundedCornerShape(12.dp),
                ) {
                    Text("نعم، حذف", color = Color.White, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { itemToDelete = null }) { Text("إلغاء", color = GlassMuted) }
            },
        )
    }
}

@Composable
fun ExamListScreen(
    onAdd: () -> Unit,
    onEdit: (studentId: String, recordId: String) -> Unit = { _, _ -> },
    vm: ExamListViewModel = hiltViewModel(),
) {
    var itemToDelete by remember { mutableStateOf<com.omran.quran.data.model.Exam?>(null) }

    RecordsScaffold("سجل اختبارات السبر", onAdd, vm) { state ->
        if (state.exams.isEmpty()) EmptyState("لا توجد اختبارات سبر مسجلة")
        else LazyColumn(contentPadding = listBottomPadding(), verticalArrangement = Arrangement.spacedBy(10.dp)) {
            itemsIndexed(state.exams, key = { _, item -> item.id }) { index, item ->
                if (index == state.exams.lastIndex && !state.endReached && !state.loadingMore) {
                    androidx.compose.runtime.LaunchedEffect(state.exams.size) { vm.loadMore() }
                }
                OmranCard {
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                        Text(item.studentName, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = GlassWhite)
                        Text("الدرجة: ${item.score}/100", color = GlassWhite, fontWeight = FontWeight.Bold)
                    }
                    Text("سورة ${item.surah}  •  الإتقان: ${item.mastery}", color = GlassSilver)
                    Text(item.date.toDateText(), style = MaterialTheme.typography.bodySmall, color = GlassMuted)
                    if (item.mistakes.isNotBlank()) Text("الأخطاء: ${item.mistakes}", color = GlassSilver)

                    Spacer(Modifier.height(4.dp))
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End, verticalAlignment = Alignment.CenterVertically) {
                        TextButton(onClick = { onEdit(item.studentId, item.id) }) {
                            Icon(PhosphorIcons.Regular.PencilSimple, contentDescription = "تعديل", Modifier.size(16.dp), tint = Color(0xFF38BDF8))
                            Spacer(Modifier.width(4.dp))
                            Text("تعديل", color = Color(0xFF38BDF8), style = MaterialTheme.typography.labelMedium)
                        }
                        Spacer(Modifier.width(4.dp))
                        TextButton(onClick = { itemToDelete = item }) {
                            Icon(PhosphorIcons.Regular.Trash, contentDescription = "حذف", Modifier.size(16.dp), tint = Color(0xFFF87171))
                            Spacer(Modifier.width(4.dp))
                            Text("حذف", color = Color(0xFFF87171), style = MaterialTheme.typography.labelMedium)
                        }
                    }
                }
            }
        }
    }

    if (itemToDelete != null) {
        val item = itemToDelete!!
        androidx.compose.material3.AlertDialog(
            onDismissRequest = { itemToDelete = null },
            icon = { Icon(PhosphorIcons.Regular.Warning, contentDescription = null, tint = Color(0xFFF87171), modifier = Modifier.size(28.dp)) },
            title = { Text("تأكيد حذف اختبار السبر", fontWeight = FontWeight.Bold, color = GlassWhite) },
            text = { Text("هل أنت متأكد من حذف اختبار سبر (${item.studentName} - ${item.surah})؟", color = GlassSilver) },
            containerColor = Color(0xFF14171E),
            confirmButton = {
                androidx.compose.material3.Button(
                    onClick = {
                        vm.deleteItem(item.id)
                        itemToDelete = null
                    },
                    colors = androidx.compose.material3.ButtonDefaults.buttonColors(containerColor = Color(0xFFDC2626)),
                    shape = RoundedCornerShape(12.dp),
                ) {
                    Text("نعم، حذف", color = Color.White, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { itemToDelete = null }) { Text("إلغاء", color = GlassMuted) }
            },
        )
    }
}

@Composable
fun RevisionListScreen(
    onAdd: () -> Unit,
    onEdit: (studentId: String, recordId: String) -> Unit = { _, _ -> },
    vm: RevisionListViewModel = hiltViewModel(),
) {
    var itemToDelete by remember { mutableStateOf<com.omran.quran.data.model.Revision?>(null) }

    RecordsScaffold("سجل المراجعة", onAdd, vm) { state ->
        if (state.revisions.isEmpty()) EmptyState("لا توجد مراجعات مسجلة")
        else LazyColumn(contentPadding = listBottomPadding(), verticalArrangement = Arrangement.spacedBy(10.dp)) {
            itemsIndexed(state.revisions, key = { _, item -> item.id }) { index, item ->
                if (index == state.revisions.lastIndex && !state.endReached && !state.loadingMore) {
                    androidx.compose.runtime.LaunchedEffect(state.revisions.size) { vm.loadMore() }
                }
                OmranCard {
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            Text(item.studentName, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = GlassWhite)
                            val isOld = item.revisionCategory == com.omran.quran.data.model.RevisionCategory.OLD
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(if (isOld) Color(0xFF0284C7).copy(alpha = 0.25f) else Color(0xFF10B981).copy(alpha = 0.25f))
                                    .border(1.dp, if (isOld) Color(0xFF38BDF8).copy(alpha = 0.5f) else Color(0xFF34D399).copy(alpha = 0.5f), RoundedCornerShape(8.dp))
                                    .padding(horizontal = 8.dp, vertical = 3.dp),
                            ) {
                                Text(
                                    text = item.revisionCategory.shortLabel,
                                    style = MaterialTheme.typography.labelSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = if (isOld) Color(0xFF7DD3FC) else Color(0xFF6EE7B7),
                                )
                            }
                        }
                        Text(item.date.toDateText(), style = MaterialTheme.typography.bodySmall, color = GlassMuted)
                    }
                    Text("${item.content}  •  المستوى: ${item.level}", color = GlassSilver)
                    Text("التقييم: ${item.rating}", color = GlassWhite, fontWeight = FontWeight.SemiBold)

                    Spacer(Modifier.height(4.dp))
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End, verticalAlignment = Alignment.CenterVertically) {
                        TextButton(onClick = { onEdit(item.studentId, item.id) }) {
                            Icon(PhosphorIcons.Regular.PencilSimple, contentDescription = "تعديل", Modifier.size(16.dp), tint = Color(0xFF38BDF8))
                            Spacer(Modifier.width(4.dp))
                            Text("تعديل", color = Color(0xFF38BDF8), style = MaterialTheme.typography.labelMedium)
                        }
                        Spacer(Modifier.width(4.dp))
                        TextButton(onClick = { itemToDelete = item }) {
                            Icon(PhosphorIcons.Regular.Trash, contentDescription = "حذف", Modifier.size(16.dp), tint = Color(0xFFF87171))
                            Spacer(Modifier.width(4.dp))
                            Text("حذف", color = Color(0xFFF87171), style = MaterialTheme.typography.labelMedium)
                        }
                    }
                }
            }
        }
    }

    if (itemToDelete != null) {
        val item = itemToDelete!!
        androidx.compose.material3.AlertDialog(
            onDismissRequest = { itemToDelete = null },
            icon = { Icon(PhosphorIcons.Regular.Warning, contentDescription = null, tint = Color(0xFFF87171), modifier = Modifier.size(28.dp)) },
            title = { Text("تأكيد حذف سجل المراجعة", fontWeight = FontWeight.Bold, color = GlassWhite) },
            text = { Text("هل أنت متأكد من حذف سجل مراجعة (${item.studentName} - ${item.content})؟", color = GlassSilver) },
            containerColor = Color(0xFF14171E),
            confirmButton = {
                androidx.compose.material3.Button(
                    onClick = {
                        vm.deleteItem(item.id)
                        itemToDelete = null
                    },
                    colors = androidx.compose.material3.ButtonDefaults.buttonColors(containerColor = Color(0xFFDC2626)),
                    shape = RoundedCornerShape(12.dp),
                ) {
                    Text("نعم، حذف", color = Color.White, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { itemToDelete = null }) { Text("إلغاء", color = GlassMuted) }
            },
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun RecordsScaffold(
    title: String,
    onAdd: () -> Unit,
    vm: RecordsViewModel,
    content: @Composable (RecordsUiState) -> Unit,
) {
    val state by vm.state.collectAsStateWithLifecycle()
    val user by vm.user.collectAsStateWithLifecycle()
    var showDate by remember { mutableStateOf(false) }

    DarkGlassBackground {
        Scaffold(
            containerColor = Color.Transparent,
            topBar = {
                TopAppBar(
                    title = { Text(title, fontWeight = FontWeight.Bold, color = GlassWhite) },
                    colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent),
                )
            },
            floatingActionButton = {
                FloatingActionButton(
                    onClick = onAdd,
                    containerColor = GlassWhite,
                    contentColor = Color.Black,
                    shape = RoundedCornerShape(18.dp),
                    modifier = Modifier.padding(bottom = 12.dp),
                ) {
                    Icon(PhosphorIcons.Regular.Plus, contentDescription = "إضافة", modifier = Modifier.size(24.dp))
                }
            },
        ) { padding ->
            Column(
                Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(horizontal = 16.dp),
            ) {
                SearchField(state.query, vm::onQuery)
                Spacer(Modifier.height(10.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
                    val isDaySelected = state.dayMillis != null
                    FilterChip(
                        selected = isDaySelected,
                        onClick = { showDate = true },
                        label = { Text(state.dayMillis?.toDateText() ?: "تصفية بالتاريخ", color = if (isDaySelected) Color.Black else GlassSilver) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = GlassWhite,
                            containerColor = Color(0xFF161922).copy(alpha = 0.6f),
                        ),
                        border = FilterChipDefaults.filterChipBorder(enabled = true, selected = isDaySelected, borderColor = if (isDaySelected) GlassWhite else Color.White.copy(alpha = 0.15f)),
                        shape = RoundedCornerShape(10.dp),
                    )
                    if (state.dayMillis != null) {
                        FilterChip(
                            selected = false,
                            onClick = { vm.onDay(null) },
                            label = { Text("مسح التاريخ", color = GlassMuted) },
                            colors = FilterChipDefaults.filterChipColors(containerColor = Color.Transparent),
                            border = FilterChipDefaults.filterChipBorder(enabled = true, selected = false, borderColor = Color.White.copy(alpha = 0.1f)),
                            shape = RoundedCornerShape(10.dp),
                        )
                    }
                }
                if (user?.isAdmin == true && state.teachers.isNotEmpty()) {
                    Spacer(Modifier.height(6.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        val isAllSelected = state.teacherId == null
                        FilterChip(
                            selected = isAllSelected,
                            onClick = { vm.onTeacher(null) },
                            label = { Text("كل المعلمين", color = if (isAllSelected) Color.Black else GlassSilver) },
                            colors = FilterChipDefaults.filterChipColors(selectedContainerColor = GlassWhite, containerColor = Color(0xFF161922).copy(alpha = 0.6f)),
                            border = FilterChipDefaults.filterChipBorder(enabled = true, selected = isAllSelected, borderColor = if (isAllSelected) GlassWhite else Color.White.copy(alpha = 0.15f)),
                            shape = RoundedCornerShape(10.dp),
                        )
                        state.teachers.take(5).forEach { teacher ->
                            val isTeacherSelected = state.teacherId == teacher.userId
                            FilterChip(
                                selected = isTeacherSelected,
                                onClick = { vm.onTeacher(teacher.userId) },
                                label = { Text(teacher.name, color = if (isTeacherSelected) Color.Black else GlassSilver) },
                                colors = FilterChipDefaults.filterChipColors(selectedContainerColor = GlassWhite, containerColor = Color(0xFF161922).copy(alpha = 0.6f)),
                                border = FilterChipDefaults.filterChipBorder(enabled = true, selected = isTeacherSelected, borderColor = if (isTeacherSelected) GlassWhite else Color.White.copy(alpha = 0.15f)),
                                shape = RoundedCornerShape(10.dp),
                            )
                        }
                    }
                }
                Spacer(Modifier.height(10.dp))
                if (state.loading && state.memorizations.isEmpty() && state.exams.isEmpty() && state.revisions.isEmpty()) {
                    LoadingBox()
                } else {
                    content(state)
                }
            }
        }

        if (showDate) {
            val dateState = rememberDatePickerState()
            DatePickerDialog(
                onDismissRequest = { showDate = false },
                confirmButton = {
                    TextButton(onClick = {
                        vm.onDay(dateState.selectedDateMillis)
                        showDate = false
                    }) { Text("تم", color = GlassWhite, fontWeight = FontWeight.Bold) }
                },
                dismissButton = { TextButton(onClick = { showDate = false }) { Text("إلغاء", color = GlassMuted) } },
            ) { DatePicker(state = dateState) }
        }
    }
}
