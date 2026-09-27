package com.omran.quran.ui.students

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.SuggestionChipDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.adamglin.PhosphorIcons
import com.adamglin.phosphoricons.Regular
import com.adamglin.phosphoricons.regular.ArrowRight
import com.adamglin.phosphoricons.regular.ArrowsClockwise
import com.adamglin.phosphoricons.regular.BookOpen
import com.adamglin.phosphoricons.regular.CalendarCheck
import com.adamglin.phosphoricons.regular.Certificate
import com.adamglin.phosphoricons.regular.CheckCircle
import com.adamglin.phosphoricons.regular.Copy
import com.adamglin.phosphoricons.regular.Exam
import com.adamglin.phosphoricons.regular.FilePdf
import com.adamglin.phosphoricons.regular.FileText
import com.adamglin.phosphoricons.regular.PencilSimple
import com.adamglin.phosphoricons.regular.ShareNetwork
import com.adamglin.phosphoricons.regular.Trash
import com.adamglin.phosphoricons.regular.Warning
import com.omran.quran.ui.components.DarkGlassBackground
import com.omran.quran.ui.components.EmptyState
import com.omran.quran.ui.components.LoadingBox
import com.omran.quran.ui.components.OmranCard
import com.omran.quran.ui.components.PrimaryButton
import com.omran.quran.ui.components.darkGlass
import com.omran.quran.ui.records.RecordType
import com.omran.quran.ui.theme.GlassMuted
import com.omran.quran.ui.theme.GlassSilver
import com.omran.quran.ui.theme.GlassWhite
import com.omran.quran.util.displayDayKey
import com.omran.quran.util.toDateText

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun StudentProfileScreen(
    onBack: () -> Unit,
    onEditStudent: (String) -> Unit,
    onAddRecord: (RecordType) -> Unit,
    onEditRecord: (RecordType, String, String) -> Unit = { _, _, _ -> },
    viewModel: StudentProfileViewModel = hiltViewModel(),
) {
    val context = LocalContext.current
    val state by viewModel.state.collectAsStateWithLifecycle()
    var tab by rememberSaveable { mutableIntStateOf(0) }
    var showReportDialog by remember { mutableStateOf(false) }
    var showDeleteDialog by remember { mutableStateOf(false) }
    var recordToDelete by remember { mutableStateOf<Triple<RecordType, String, String>?>(null) }
    val student = state.student
    val an = state.analytics
    val tabs = listOf("الإنجاز القرآني", "الحفظ", "السبر", "المراجعة", "الحضور")

    LaunchedEffect(Unit) {
        viewModel.deleted.collect {
            Toast.makeText(context, "تم حذف الطالب بنجاح", Toast.LENGTH_SHORT).show()
            onBack()
        }
    }

    DarkGlassBackground {
        Scaffold(
            containerColor = Color.Transparent,
            topBar = {
                TopAppBar(
                    title = { Text(student?.name ?: "ملف الطالب", fontWeight = FontWeight.Bold, color = GlassWhite) },
                    navigationIcon = {
                        IconButton(onClick = onBack) {
                            Icon(PhosphorIcons.Regular.ArrowRight, contentDescription = "رجوع", tint = GlassWhite)
                        }
                    },
                    actions = {
                        IconButton(onClick = { onEditStudent(viewModel.studentId) }) {
                            Icon(PhosphorIcons.Regular.PencilSimple, contentDescription = "تعديل بيانات الطالب", tint = GlassSilver)
                        }
                        IconButton(onClick = { showDeleteDialog = true }) {
                            Icon(PhosphorIcons.Regular.Trash, contentDescription = "حذف الطالب", tint = Color(0xFFF87171))
                        }
                        IconButton(onClick = { viewModel.shareReport(context) }) {
                            Icon(PhosphorIcons.Regular.ShareNetwork, contentDescription = "مشاركة التقرير", tint = GlassWhite)
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent),
                )
            },
        ) { padding ->
            if (state.loading && student == null) {
                LoadingBox(Modifier.padding(padding))
                return@Scaffold
            }

            Column(
                Modifier
                    .fillMaxSize()
                    .padding(padding),
            ) {
                // 1. بطاقة الطالب الرئيسية
                OmranCard(
                    Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 6.dp),
                ) {
                    Row(
                        Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(14.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Box(
                            Modifier
                                .size(64.dp)
                                .clip(CircleShape)
                                .background(Color.White.copy(alpha = 0.08f))
                                .border(1.dp, Color.White.copy(alpha = 0.2f), CircleShape),
                            contentAlignment = Alignment.Center,
                        ) {
                            AsyncImage(
                                model = student?.photoUrl?.ifBlank { null },
                                contentDescription = null,
                                modifier = Modifier.fillMaxSize().clip(CircleShape),
                                contentScale = ContentScale.Crop,
                            )
                        }
                        Column(Modifier.weight(1f)) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                            ) {
                                Text(student?.name.orEmpty(), style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, color = GlassWhite)
                                if (student != null) {
                                    Box(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(8.dp))
                                            .background(
                                                if (student.isFemale) Color(0xFF4C1D45).copy(alpha = 0.8f)
                                                else Color(0xFF1E3A5F).copy(alpha = 0.8f)
                                            )
                                            .padding(horizontal = 8.dp, vertical = 2.dp),
                                    ) {
                                        Text(
                                            if (student.isFemale) "👧 بنات" else "👦 بنين",
                                            style = MaterialTheme.typography.labelSmall,
                                            fontWeight = FontWeight.Bold,
                                            color = if (student.isFemale) Color(0xFFF472B6) else Color(0xFF60A5FA),
                                        )
                                    }
                                }
                            }
                            if (!student?.teacherName.isNullOrBlank()) {
                                Text(
                                    "المعلم: ${student.teacherName}",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = GlassSilver,
                                )
                            }
                            if (!student?.parentPhone.isNullOrBlank()) {
                                Text("ولي الأمر: ${student.parentPhone}", style = MaterialTheme.typography.labelSmall, color = GlassMuted)
                            }
                        }
                    }

                    Spacer(Modifier.height(14.dp))

                    // إحصاءات الإنجاز السريعة
                    Row(
                        Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                    ) {
                        SummaryPill("الأجزاء", if (an.distinctAjzaaCount > 0) "${an.distinctAjzaaCount}/30" else "0", PhosphorIcons.Regular.BookOpen, Modifier.weight(1f))
                        SummaryPill("السور", "${an.distinctSurahsCount}", PhosphorIcons.Regular.CheckCircle, Modifier.weight(1f))
                        SummaryPill("متوسط السبر", if (an.totalExams > 0) "${an.averageScore.toInt()}%" else "—", PhosphorIcons.Regular.Exam, Modifier.weight(1f))
                        SummaryPill("المراجعات", "${an.totalRevisions}", PhosphorIcons.Regular.ArrowsClockwise, Modifier.weight(1f))
                    }

                    Spacer(Modifier.height(12.dp))

                    // أزرار التقرير والشهادة
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                    ) {
                        PrimaryButton(
                            text = "عرض التقرير ✨",
                            onClick = { showReportDialog = true },
                            modifier = Modifier.weight(1f).height(48.dp),
                        )
                        Button(
                            onClick = {
                                Toast.makeText(context, "سيتم توفير تصدير الشهادة قريباً", Toast.LENGTH_SHORT).show()
                            },
                            modifier = Modifier.weight(1f).height(48.dp),
                            shape = RoundedCornerShape(14.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFFD97706),
                                contentColor = Color.White,
                            ),
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp),
                            ) {
                                Icon(
                                    PhosphorIcons.Regular.Certificate,
                                    contentDescription = null,
                                    modifier = Modifier.size(18.dp),
                                    tint = Color.White,
                                )
                                Text(
                                    "إصدار شهادة 📜",
                                    fontWeight = FontWeight.Bold,
                                    style = MaterialTheme.typography.labelLarge,
                                )
                            }
                        }
                    }
                }

                // 2. شريط التبويبات الزجاجي
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 6.dp)
                        .darkGlass(shape = RoundedCornerShape(16.dp), backgroundColor = Color(0xFF101318).copy(alpha = 0.8f))
                        .padding(4.dp),
                    horizontalArrangement = Arrangement.SpaceAround,
                ) {
                    tabs.forEachIndexed { index, title ->
                        val isSelected = tab == index
                        val bgColor by animateColorAsState(if (isSelected) Color.White.copy(alpha = 0.18f) else Color.Transparent, label = "tabBg")
                        val textColor by animateColorAsState(if (isSelected) GlassWhite else GlassMuted, label = "tabText")

                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(12.dp))
                                .background(bgColor)
                                .clickable(
                                    interactionSource = remember { MutableInteractionSource() },
                                    indication = null,
                                ) { tab = index }
                                .padding(vertical = 8.dp),
                            contentAlignment = Alignment.Center,
                        ) {
                            Text(
                                title,
                                fontSize = 11.5.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                color = textColor,
                                maxLines = 1,
                            )
                        }
                    }
                }

                // 3. محتوى التبويبات
                when (tab) {
                    0 -> {
                        // تبويب الإنجاز العام
                        Column(
                            Modifier
                                .fillMaxSize()
                                .verticalScroll(rememberScrollState())
                                .padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(14.dp),
                        ) {
                            // قسم الأجزاء المنجزة
                            Text(
                                "الأجزاء المكتملة بالكامل (${an.distinctAjzaaCount} من 30 جزءاً - إتمام ${an.quranCompletionPercentage}%):",
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFFFBBF24),
                            )
                            if (an.distinctAjzaaList.isEmpty()) {
                                Text("لم يتم تسجيل أجزاء مكتملة بالكامل بعد (يجب إتمام جميع سور الجزء)", style = MaterialTheme.typography.bodySmall, color = GlassMuted)
                            } else {
                                FlowRow(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                                    verticalArrangement = Arrangement.spacedBy(6.dp),
                                ) {
                                    an.distinctAjzaaList.forEach { juzTitle ->
                                        SuggestionChip(
                                            onClick = {},
                                            label = { Text(juzTitle, color = Color(0xFFFDE68A), fontWeight = FontWeight.Bold) },
                                            icon = { Icon(PhosphorIcons.Regular.BookOpen, contentDescription = null, Modifier.size(16.dp), tint = Color(0xFFF59E0B)) },
                                            colors = SuggestionChipDefaults.suggestionChipColors(containerColor = Color(0xFF78350F).copy(alpha = 0.35f)),
                                            border = SuggestionChipDefaults.suggestionChipBorder(enabled = true, borderColor = Color(0xFFF59E0B).copy(alpha = 0.4f)),
                                            shape = RoundedCornerShape(12.dp),
                                        )
                                    }
                                }
                            }

                            // أجزاء قيد الإنجاز
                            val inProgressAjzaa = an.juzBreakdown.filter { !it.isCompleted && it.completedSurahsCount > 0 }
                            if (inProgressAjzaa.isNotEmpty()) {
                                Spacer(Modifier.height(4.dp))
                                Text("أجزاء قيد الإنجاز (لم تكتمل جميع سورها بعد):", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold, color = Color(0xFF60A5FA))
                                FlowRow(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                                    verticalArrangement = Arrangement.spacedBy(6.dp),
                                ) {
                                    inProgressAjzaa.forEach { jb ->
                                        SuggestionChip(
                                            onClick = {},
                                            label = { Text("${jb.juzName} (${jb.completedSurahsCount}/${jb.totalSurahs} سورة)", color = Color(0xFF93C5FD), fontWeight = FontWeight.SemiBold) },
                                            icon = { Icon(PhosphorIcons.Regular.ArrowsClockwise, contentDescription = null, Modifier.size(16.dp), tint = Color(0xFF60A5FA)) },
                                            colors = SuggestionChipDefaults.suggestionChipColors(containerColor = Color(0xFF1E3A8A).copy(alpha = 0.35f)),
                                            border = SuggestionChipDefaults.suggestionChipBorder(enabled = true, borderColor = Color(0xFF3B82F6).copy(alpha = 0.35f)),
                                            shape = RoundedCornerShape(12.dp),
                                        )
                                    }
                                }
                            }

                            Spacer(Modifier.height(4.dp))

                            // قسم السور المتقنة
                            Text("السور التي أنجزها الطالب (${an.distinctSurahsCount} سورة):", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold, color = GlassWhite)
                            if (an.distinctSurahsList.isEmpty()) {
                                EmptyState("لم يتم تسجيل سور بعد")
                            } else {
                                FlowRow(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                                    verticalArrangement = Arrangement.spacedBy(6.dp),
                                ) {
                                    an.distinctSurahsList.forEach { surahName ->
                                        SuggestionChip(
                                            onClick = {},
                                            label = { Text(surahName, color = GlassWhite, fontWeight = FontWeight.SemiBold) },
                                            icon = { Icon(PhosphorIcons.Regular.CheckCircle, contentDescription = null, Modifier.size(16.dp), tint = Color(0xFF34D399)) },
                                            colors = SuggestionChipDefaults.suggestionChipColors(containerColor = Color(0xFF191C24).copy(alpha = 0.8f)),
                                            border = SuggestionChipDefaults.suggestionChipBorder(enabled = true, borderColor = Color.White.copy(alpha = 0.15f)),
                                            shape = RoundedCornerShape(12.dp),
                                        )
                                    }
                                }
                            }

                            Spacer(Modifier.height(8.dp))
                            Text("آخر العمليات المسجلة:", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold, color = GlassWhite)

                            state.memorizations.firstOrNull()?.let {
                                OmranCard {
                                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                        Icon(PhosphorIcons.Regular.BookOpen, contentDescription = null, tint = GlassWhite, modifier = Modifier.size(18.dp))
                                        Text("آخر حفظ: ${it.surah} (${it.amount})", fontWeight = FontWeight.Bold, color = GlassWhite)
                                    }
                                    Text("التقييم: ${it.rating}  •  ${it.date.toDateText()}", style = MaterialTheme.typography.bodySmall, color = GlassMuted)
                                }
                            }

                            state.exams.firstOrNull()?.let {
                                OmranCard {
                                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                        Icon(PhosphorIcons.Regular.Exam, contentDescription = null, tint = GlassWhite, modifier = Modifier.size(18.dp))
                                        Text("آخر سبر: ${it.surah} (الدرجة: ${it.score}/100)", fontWeight = FontWeight.Bold, color = GlassWhite)
                                    }
                                    Text("الإتقان: ${it.mastery}  •  ${it.date.toDateText()}", style = MaterialTheme.typography.bodySmall, color = GlassMuted)
                                }
                            }

                            state.revisions.firstOrNull()?.let {
                                OmranCard {
                                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                        Icon(PhosphorIcons.Regular.ArrowsClockwise, contentDescription = null, tint = GlassWhite, modifier = Modifier.size(18.dp))
                                        Text("آخر مراجعة: ${it.content}", fontWeight = FontWeight.Bold, color = GlassWhite)
                                    }
                                    Text("التقييم: ${it.rating} (${it.level})  •  ${it.date.toDateText()}", style = MaterialTheme.typography.bodySmall, color = GlassMuted)
                                }
                            }
                        }
                    }
                    1 -> {
                        // تبويب الحفظ
                        PrimaryButton(
                            text = "+ تسجيل حفظ جديد للطالب",
                            onClick = { onAddRecord(RecordType.MEMORIZATION) },
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp).height(48.dp),
                        )
                        if (state.memorizations.isEmpty()) EmptyState("لا توجد عمليات حفظ مسجلة")
                        else LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.padding(horizontal = 16.dp)) {
                            items(state.memorizations, key = { it.id }) { item ->
                                OmranCard {
                                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                            Text(item.surah, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = GlassWhite)
                                            val isOld = item.memorizationCategory == com.omran.quran.data.model.MemorizationCategory.OLD
                                            Box(
                                                modifier = Modifier
                                                    .clip(RoundedCornerShape(6.dp))
                                                    .background(if (isOld) Color(0xFF0284C7).copy(alpha = 0.25f) else Color(0xFF10B981).copy(alpha = 0.25f))
                                                    .border(1.dp, if (isOld) Color(0xFF38BDF8).copy(alpha = 0.5f) else Color(0xFF34D399).copy(alpha = 0.5f), RoundedCornerShape(6.dp))
                                                    .padding(horizontal = 6.dp, vertical = 2.dp),
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
                                    Text("المقدار: ${item.amount.ifBlank { "الآيات ${item.fromVerse} - ${item.toVerse}" }}", color = GlassSilver)
                                    Text("التقييم: ${item.rating}", color = GlassWhite, fontWeight = FontWeight.Bold)
                                    if (item.note.isNotBlank()) Text("ملاحظة: ${item.note}", style = MaterialTheme.typography.bodySmall, color = GlassMuted)

                                    // شريط الإجراءات (تعديل وحذف)
                                    Spacer(Modifier.height(4.dp))
                                    Row(
                                        Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.End,
                                        verticalAlignment = Alignment.CenterVertically,
                                    ) {
                                        TextButton(
                                            onClick = { onEditRecord(RecordType.MEMORIZATION, viewModel.studentId, item.id) },
                                        ) {
                                            Icon(PhosphorIcons.Regular.PencilSimple, contentDescription = "تعديل", Modifier.size(16.dp), tint = Color(0xFF38BDF8))
                                            Spacer(Modifier.width(4.dp))
                                            Text("تعديل", color = Color(0xFF38BDF8), style = MaterialTheme.typography.labelMedium)
                                        }
                                        Spacer(Modifier.width(4.dp))
                                        TextButton(
                                            onClick = { recordToDelete = Triple(RecordType.MEMORIZATION, item.id, item.surah) },
                                        ) {
                                            Icon(PhosphorIcons.Regular.Trash, contentDescription = "حذف", Modifier.size(16.dp), tint = Color(0xFFF87171))
                                            Spacer(Modifier.width(4.dp))
                                            Text("حذف", color = Color(0xFFF87171), style = MaterialTheme.typography.labelMedium)
                                        }
                                    }
                                }
                            }
                        }
                    }
                    2 -> {
                        // تبويب السبر
                        PrimaryButton(
                            text = "+ تسجيل سبر جديد للطالب",
                            onClick = { onAddRecord(RecordType.EXAM) },
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp).height(48.dp),
                        )
                        if (state.exams.isEmpty()) EmptyState("لا يوجد سجل سبر سابق")
                        else LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.padding(horizontal = 16.dp)) {
                            items(state.exams, key = { it.id }) { item ->
                                OmranCard {
                                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                        Text(item.surah, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = GlassWhite)
                                        Text("الدرجة: ${item.score}/100", color = GlassWhite, fontWeight = FontWeight.Bold)
                                    }
                                    Text("مستوى الإتقان: ${item.mastery}  •  ${item.date.toDateText()}", style = MaterialTheme.typography.bodySmall, color = GlassMuted)
                                    if (item.mistakes.isNotBlank()) Text("الأخطاء: ${item.mistakes}", style = MaterialTheme.typography.bodyMedium, color = GlassSilver)
                                    if (item.note.isNotBlank()) Text("ملاحظة: ${item.note}", style = MaterialTheme.typography.bodySmall, color = GlassMuted)

                                    // شريط الإجراءات (تعديل وحذف)
                                    Spacer(Modifier.height(4.dp))
                                    Row(
                                        Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.End,
                                        verticalAlignment = Alignment.CenterVertically,
                                    ) {
                                        TextButton(
                                            onClick = { onEditRecord(RecordType.EXAM, viewModel.studentId, item.id) },
                                        ) {
                                            Icon(PhosphorIcons.Regular.PencilSimple, contentDescription = "تعديل", Modifier.size(16.dp), tint = Color(0xFF38BDF8))
                                            Spacer(Modifier.width(4.dp))
                                            Text("تعديل", color = Color(0xFF38BDF8), style = MaterialTheme.typography.labelMedium)
                                        }
                                        Spacer(Modifier.width(4.dp))
                                        TextButton(
                                            onClick = { recordToDelete = Triple(RecordType.EXAM, item.id, item.surah) },
                                        ) {
                                            Icon(PhosphorIcons.Regular.Trash, contentDescription = "حذف", Modifier.size(16.dp), tint = Color(0xFFF87171))
                                            Spacer(Modifier.width(4.dp))
                                            Text("حذف", color = Color(0xFFF87171), style = MaterialTheme.typography.labelMedium)
                                        }
                                    }
                                }
                            }
                        }
                    }
                    3 -> {
                        // تبويب المراجعة
                        PrimaryButton(
                            text = "+ تسجيل مراجعة جديدة للطالب",
                            onClick = { onAddRecord(RecordType.REVISION) },
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp).height(48.dp),
                        )
                        if (state.revisions.isEmpty()) EmptyState("لا توجد مراجعات مسجلة")
                        else LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.padding(horizontal = 16.dp)) {
                            items(state.revisions, key = { it.id }) { item ->
                                OmranCard {
                                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                            Text(item.content, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = GlassWhite)
                                            val isOld = item.revisionCategory == com.omran.quran.data.model.RevisionCategory.OLD
                                            Box(
                                                modifier = Modifier
                                                    .clip(RoundedCornerShape(6.dp))
                                                    .background(if (isOld) Color(0xFF0284C7).copy(alpha = 0.25f) else Color(0xFF10B981).copy(alpha = 0.25f))
                                                    .border(1.dp, if (isOld) Color(0xFF38BDF8).copy(alpha = 0.5f) else Color(0xFF34D399).copy(alpha = 0.5f), RoundedCornerShape(6.dp))
                                                    .padding(horizontal = 6.dp, vertical = 2.dp),
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
                                    Text("المستوى: ${item.level}  •  التقييم: ${item.rating}", color = GlassSilver)
                                    if (item.note.isNotBlank()) Text("ملاحظة: ${item.note}", style = MaterialTheme.typography.bodySmall, color = GlassMuted)

                                    // شريط الإجراءات (تعديل وحذف)
                                    Spacer(Modifier.height(4.dp))
                                    Row(
                                        Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.End,
                                        verticalAlignment = Alignment.CenterVertically,
                                    ) {
                                        TextButton(
                                            onClick = { onEditRecord(RecordType.REVISION, viewModel.studentId, item.id) },
                                        ) {
                                            Icon(PhosphorIcons.Regular.PencilSimple, contentDescription = "تعديل", Modifier.size(16.dp), tint = Color(0xFF38BDF8))
                                            Spacer(Modifier.width(4.dp))
                                            Text("تعديل", color = Color(0xFF38BDF8), style = MaterialTheme.typography.labelMedium)
                                        }
                                        Spacer(Modifier.width(4.dp))
                                        TextButton(
                                            onClick = { recordToDelete = Triple(RecordType.REVISION, item.id, item.content) },
                                        ) {
                                            Icon(PhosphorIcons.Regular.Trash, contentDescription = "حذف", Modifier.size(16.dp), tint = Color(0xFFF87171))
                                            Spacer(Modifier.width(4.dp))
                                            Text("حذف", color = Color(0xFFF87171), style = MaterialTheme.typography.labelMedium)
                                        }
                                    }
                                }
                            }
                        }
                    }
                    else -> {
                        // تبويب الحضور
                        if (state.attendance.isEmpty()) EmptyState("لا يوجد سجل حضور مسجل")
                        else LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.padding(16.dp)) {
                            items(state.attendance, key = { it.id }) { item ->
                                OmranCard {
                                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                        Text(displayDayKey(item.date), style = MaterialTheme.typography.titleMedium, color = GlassWhite, fontWeight = FontWeight.SemiBold)
                                        Text(item.attendanceStatus.label, fontWeight = FontWeight.Bold, color = GlassWhite)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        // نافذة تأكيد حذف سجل
        if (recordToDelete != null) {
            val rec = recordToDelete!!
            val recName = when (rec.first) {
                RecordType.MEMORIZATION -> "سجل التسميع (${rec.third})"
                RecordType.EXAM -> "اختبار السبر (${rec.third})"
                RecordType.REVISION -> "سجل المراجعة (${rec.third})"
            }
            AlertDialog(
                onDismissRequest = { recordToDelete = null },
                icon = { Icon(PhosphorIcons.Regular.Warning, contentDescription = null, tint = Color(0xFFF87171), modifier = Modifier.size(28.dp)) },
                title = { Text("تأكيد حذف السجل", fontWeight = FontWeight.Bold, color = GlassWhite) },
                text = { Text("هل أنت متأكد من رغبتك في حذف $recName نهائياً؟ يمكنك إعادة تسجيله أو تعديله في أي وقت.", color = GlassSilver) },
                containerColor = Color(0xFF14171E),
                confirmButton = {
                    Button(
                        onClick = {
                            when (rec.first) {
                                RecordType.MEMORIZATION -> viewModel.deleteMemorization(rec.second)
                                RecordType.EXAM -> viewModel.deleteExam(rec.second)
                                RecordType.REVISION -> viewModel.deleteRevision(rec.second)
                            }
                            recordToDelete = null
                            Toast.makeText(context, "تم حذف السجل بنجاح", Toast.LENGTH_SHORT).show()
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFDC2626)),
                        shape = RoundedCornerShape(12.dp),
                    ) {
                        Text("نعم، حذف السجل", color = Color.White, fontWeight = FontWeight.Bold)
                    }
                },
                dismissButton = {
                    TextButton(onClick = { recordToDelete = null }) {
                        Text("إلغاء", color = GlassMuted)
                    }
                },
            )
        }

        // نافذة تأكيد حذف الطالب
        if (showDeleteDialog) {
            AlertDialog(
                onDismissRequest = { showDeleteDialog = false },
                icon = { Icon(PhosphorIcons.Regular.Warning, contentDescription = null, tint = Color(0xFFF87171), modifier = Modifier.size(28.dp)) },
                title = { Text("تأكيد حذف الطالب", fontWeight = FontWeight.Bold, color = GlassWhite) },
                text = { Text("هل أنت متأكد من رغبتك في حذف بيانات الطالب (${student?.name}) نهائياً؟ سيتم مسح كافة سجلاته.", color = GlassSilver) },
                containerColor = Color(0xFF14171E),
                confirmButton = {
                    Button(
                        onClick = {
                            showDeleteDialog = false
                            viewModel.deleteStudent()
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFDC2626)),
                        shape = RoundedCornerShape(12.dp),
                    ) {
                        Text("نعم، حذف الطالب", color = Color.White, fontWeight = FontWeight.Bold)
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showDeleteDialog = false }) {
                        Text("إلغاء", color = GlassMuted)
                    }
                },
            )
        }

        // نافذة عرض ومشاركة التقرير الشامل (نوعان: ولي الأمر + الإدارة | نص + PDF)
        if (showReportDialog) {
            var selectedReportType by remember { mutableStateOf(StudentReportType.PARENT) }
            val reportContent = viewModel.generateReportText(selectedReportType)
            AlertDialog(
                onDismissRequest = { showReportDialog = false },
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(PhosphorIcons.Regular.FileText, contentDescription = null, tint = GlassWhite, modifier = Modifier.size(24.dp))
                        Spacer(Modifier.width(8.dp))
                        Text("استخراج تقرير الطالب", fontWeight = FontWeight.Bold, color = GlassWhite)
                    }
                },
                containerColor = Color(0xFF14171E),
                text = {
                    Column(
                        Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(10.dp),
                    ) {
                        // شريط اختيار نوع التقرير (ولي الأمر / الإدارة)
                        Row(
                            Modifier
                                .fillMaxWidth()
                                .darkGlass(shape = RoundedCornerShape(10.dp), backgroundColor = Color(0xFF0F1218))
                                .padding(4.dp),
                            horizontalArrangement = Arrangement.spacedBy(4.dp),
                        ) {
                            StudentReportType.entries.forEach { type ->
                                val isSelected = selectedReportType == type
                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(if (isSelected) GlassWhite else Color.Transparent)
                                        .clickable { selectedReportType = type }
                                        .padding(vertical = 8.dp),
                                    contentAlignment = Alignment.Center,
                                ) {
                                    Text(
                                        text = type.title,
                                        style = MaterialTheme.typography.bodySmall,
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                        color = if (isSelected) Color.Black else GlassSilver,
                                    )
                                }
                            }
                        }

                        // معاينة نص التقرير
                        Column(
                            Modifier
                                .fillMaxWidth()
                                .height(250.dp)
                                .verticalScroll(rememberScrollState())
                                .darkGlass(shape = RoundedCornerShape(12.dp), backgroundColor = Color(0xFF0C0E12).copy(alpha = 0.85f))
                                .padding(12.dp),
                        ) {
                            Text(reportContent, style = MaterialTheme.typography.bodySmall, color = GlassSilver)
                        }

                        // أزرار التصدير (PDF + نص)
                        Row(
                            Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                        ) {
                            Button(
                                onClick = {
                                    viewModel.shareReportPdf(context, selectedReportType)
                                    showReportDialog = false
                                },
                                modifier = Modifier.weight(1f).height(44.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color(0xFFD97706), // Amber/Gold for PDF
                                    contentColor = Color.White,
                                ),
                                shape = RoundedCornerShape(12.dp),
                            ) {
                                Icon(PhosphorIcons.Regular.FilePdf, contentDescription = null, Modifier.size(18.dp))
                                Spacer(Modifier.width(4.dp))
                                Text("ملف PDF 📄", fontWeight = FontWeight.Bold)
                            }

                            Button(
                                onClick = {
                                    viewModel.shareReport(context, selectedReportType)
                                    showReportDialog = false
                                },
                                modifier = Modifier.weight(1f).height(44.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = GlassWhite,
                                    contentColor = Color.Black,
                                ),
                                shape = RoundedCornerShape(12.dp),
                            ) {
                                Icon(PhosphorIcons.Regular.ShareNetwork, contentDescription = null, Modifier.size(18.dp))
                                Spacer(Modifier.width(4.dp))
                                Text("رسالة نصية 💬", fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                },
                confirmButton = {
                    OutlinedButton(
                        onClick = {
                            val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                            clipboard.setPrimaryClip(ClipData.newPlainText("تقرير الطالب", reportContent))
                            Toast.makeText(context, "تم نسخ التقرير إلى الحافظة بنجاح ✨", Toast.LENGTH_SHORT).show()
                        },
                        shape = RoundedCornerShape(12.dp),
                    ) {
                        Icon(PhosphorIcons.Regular.Copy, contentDescription = null, Modifier.size(16.dp), tint = GlassWhite)
                        Spacer(Modifier.width(4.dp))
                        Text("نسخ النص", color = GlassWhite)
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showReportDialog = false }) {
                        Text("إغلاق", color = GlassMuted)
                    }
                },
            )
        }
    }
}

@Composable
private fun SummaryPill(
    label: String,
    value: String,
    icon: ImageVector,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.padding(horizontal = 2.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        Box(
            Modifier
                .size(44.dp)
                .clip(RoundedCornerShape(14.dp))
                .background(Color.White.copy(alpha = 0.08f))
                .border(1.dp, Color.White.copy(alpha = 0.18f), RoundedCornerShape(14.dp)),
            contentAlignment = Alignment.Center,
        ) {
            Icon(icon, contentDescription = null, tint = GlassWhite, modifier = Modifier.size(22.dp))
        }
        Text(value, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium, color = GlassWhite, maxLines = 1)
        Text(label, style = MaterialTheme.typography.labelSmall, color = GlassMuted, maxLines = 1)
    }
}
