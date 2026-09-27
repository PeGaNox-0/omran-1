package com.omran.quran.ui.attendance

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.adamglin.PhosphorIcons
import com.adamglin.phosphoricons.Regular
import com.adamglin.phosphoricons.regular.Calendar
import com.adamglin.phosphoricons.regular.CheckCircle
import com.adamglin.phosphoricons.regular.Checks
import com.adamglin.phosphoricons.regular.Clock
import com.adamglin.phosphoricons.regular.Copy
import com.adamglin.phosphoricons.regular.FilePdf
import com.adamglin.phosphoricons.regular.FileText
import com.adamglin.phosphoricons.regular.ShareNetwork
import com.adamglin.phosphoricons.regular.Sun
import com.adamglin.phosphoricons.regular.XCircle
import com.omran.quran.data.model.AttendanceStatus
import com.omran.quran.ui.components.DarkGlassBackground
import com.omran.quran.ui.components.EmptyState
import com.omran.quran.ui.components.ErrorBanner
import com.omran.quran.ui.components.LoadingBox
import com.omran.quran.ui.components.OmranCard
import com.omran.quran.ui.components.PrimaryButton
import com.omran.quran.ui.components.darkGlass
import com.omran.quran.ui.components.listBottomPadding
import com.omran.quran.ui.theme.GlassMuted
import com.omran.quran.ui.theme.GlassSilver
import com.omran.quran.ui.theme.GlassWhite
import com.omran.quran.util.displayDayKey
import com.omran.quran.util.toDayKey
import com.omran.quran.util.todayKey

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AttendanceScreen(viewModel: AttendanceViewModel = hiltViewModel()) {
    val context = LocalContext.current
    val state by viewModel.state.collectAsStateWithLifecycle()
    var showDate by remember { mutableStateOf(false) }
    var showReportDialog by remember { mutableStateOf(false) }

    DarkGlassBackground {
        Scaffold(
            containerColor = Color.Transparent,
            topBar = {
                TopAppBar(
                    title = { Text("الحضور والغياب", fontWeight = FontWeight.Bold, color = GlassWhite) },
                    actions = {
                        IconButton(onClick = { showReportDialog = true }) {
                            Box(
                                Modifier
                                    .size(38.dp)
                                    .clip(CircleShape)
                                    .background(Color.White.copy(alpha = 0.08f)),
                                contentAlignment = Alignment.Center,
                            ) {
                                Icon(
                                    PhosphorIcons.Regular.ShareNetwork,
                                    contentDescription = "مشاركة التقرير اليومي",
                                    tint = GlassWhite,
                                    modifier = Modifier.size(20.dp),
                                )
                            }
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent),
                )
            },
        ) { padding ->
            Column(
                Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(horizontal = 16.dp),
            ) {
                ErrorBanner(state.error) { viewModel.load() }

                // شريط التاريخ والتحضير السريع وزر التقرير
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth().padding(vertical = 6.dp),
                ) {
                    val hijriSelectedDay = remember(state.date) {
                        com.omran.quran.util.HijriDateHelper.formatHijriFromDateString(state.date, includeDayName = false)
                    }

                    Row(
                        modifier = Modifier
                            .darkGlass(shape = RoundedCornerShape(14.dp), backgroundColor = Color(0xFF161922).copy(alpha = 0.7f))
                            .clickable { showDate = true }
                            .padding(horizontal = 14.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Icon(PhosphorIcons.Regular.Calendar, contentDescription = null, tint = GlassSilver, modifier = Modifier.size(18.dp))
                        Spacer(Modifier.width(8.dp))
                        Column {
                            Text(
                                if (state.date == todayKey()) "اليوم (${state.currentWeekDay.arabicName})" else displayDayKey(state.date),
                                color = GlassWhite,
                                fontWeight = FontWeight.SemiBold,
                                style = MaterialTheme.typography.bodyMedium,
                            )
                            if (hijriSelectedDay.isNotBlank()) {
                                Text(
                                    "🌙 $hijriSelectedDay",
                                    color = Color(0xFFFDE68A),
                                    style = MaterialTheme.typography.labelSmall,
                                )
                            }
                        }
                    }

                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        if (!state.isHoliday || state.exceptionUnlocked) {
                            Row(
                                modifier = Modifier
                                    .darkGlass(shape = RoundedCornerShape(14.dp), backgroundColor = Color(0xFF1B2230).copy(alpha = 0.85f))
                                    .clickable { viewModel.markAllPresent() }
                                    .padding(horizontal = 12.dp, vertical = 10.dp),
                                verticalAlignment = Alignment.CenterVertically,
                            ) {
                                Icon(PhosphorIcons.Regular.Checks, contentDescription = null, tint = GlassWhite, modifier = Modifier.size(18.dp))
                                Spacer(Modifier.width(6.dp))
                                Text("الجميع حاضر", color = GlassWhite, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.bodySmall)
                            }
                        }

                        Row(
                            modifier = Modifier
                                .darkGlass(shape = RoundedCornerShape(14.dp), backgroundColor = Color(0xFF222838).copy(alpha = 0.9f))
                                .clickable { showReportDialog = true }
                                .padding(horizontal = 12.dp, vertical = 10.dp),
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Icon(PhosphorIcons.Regular.FileText, contentDescription = null, tint = GlassWhite, modifier = Modifier.size(18.dp))
                            Spacer(Modifier.width(6.dp))
                            Text("تقرير اليوم", color = GlassWhite, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.bodySmall)
                        }
                    }
                }

                if (state.saved) {
                    Text("تم حفظ الحضور بنجاح ✨", color = GlassWhite, fontWeight = FontWeight.Bold, modifier = Modifier.padding(vertical = 4.dp))
                }

                when {
                    state.loading -> LoadingBox()

                    // عرض بطاقة العطلة
                    state.isHoliday && !state.exceptionUnlocked -> {
                        Column(
                            Modifier
                                .fillMaxSize()
                                .padding(top = 32.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(16.dp),
                        ) {
                            OmranCard(Modifier.fillMaxWidth()) {
                                Column(
                                    Modifier.fillMaxWidth().padding(12.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.spacedBy(14.dp),
                                ) {
                                    Box(
                                        Modifier
                                            .size(68.dp)
                                            .clip(CircleShape)
                                            .background(Color.White.copy(alpha = 0.08f))
                                            .border(1.dp, Color.White.copy(alpha = 0.2f), CircleShape),
                                        contentAlignment = Alignment.Center,
                                    ) {
                                        Icon(
                                            PhosphorIcons.Regular.Sun,
                                            contentDescription = null,
                                            tint = GlassWhite,
                                            modifier = Modifier.size(36.dp),
                                        )
                                    }

                                    Text(
                                        "اليوم عطلة أسبوعية",
                                        style = MaterialTheme.typography.titleLarge,
                                        fontWeight = FontWeight.Bold,
                                        color = GlassWhite,
                                    )

                                    Text(
                                        "وفقاً لأيام الدوام المحددة لحسابك، لا توجد حلقة مقررة في يوم (${state.currentWeekDay.arabicName}).",
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = GlassMuted,
                                    )

                                    Spacer(Modifier.height(4.dp))

                                    PrimaryButton(
                                        text = "فتح التحضير استثنائياً اليوم",
                                        onClick = { viewModel.unlockException() },
                                        modifier = Modifier.fillMaxWidth().height(48.dp),
                                    )
                                }
                            }
                        }
                    }

                    state.rows.isEmpty() -> EmptyState("لا يوجد طلاب لتسجيل الحضور")

                    else -> LazyColumn(
                        contentPadding = listBottomPadding(),
                        verticalArrangement = Arrangement.spacedBy(10.dp),
                        modifier = Modifier.weight(1f),
                    ) {
                        items(state.rows, key = { it.student.studentId }) { row ->
                            OmranCard {
                                Text(row.student.name, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = GlassWhite)
                                Spacer(Modifier.height(8.dp))
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                                ) {
                                    AttendanceStatus.entries.forEach { status ->
                                        val isSelected = row.status == status
                                        val (label, icon, statusColor) = when (status) {
                                            AttendanceStatus.PRESENT -> Triple("حاضر", PhosphorIcons.Regular.CheckCircle, Color(0xFF34D399))
                                            AttendanceStatus.ABSENT -> Triple("غائب", PhosphorIcons.Regular.XCircle, Color(0xFFF87171))
                                            AttendanceStatus.LATE -> Triple("متأخر", PhosphorIcons.Regular.Clock, Color(0xFFFBBF24))
                                        }
                                        FilterChip(
                                            modifier = Modifier.weight(1f),
                                            selected = isSelected,
                                            onClick = { viewModel.setStatus(row.student.studentId, status) },
                                            leadingIcon = {
                                                Icon(
                                                    icon,
                                                    contentDescription = null,
                                                    tint = if (isSelected) Color.Black else statusColor,
                                                    modifier = Modifier.size(16.dp),
                                                )
                                            },
                                            label = {
                                                Text(
                                                    label,
                                                    color = if (isSelected) Color.Black else GlassSilver,
                                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                                    style = MaterialTheme.typography.bodySmall,
                                                    maxLines = 1,
                                                )
                                            },
                                            colors = FilterChipDefaults.filterChipColors(
                                                selectedContainerColor = GlassWhite,
                                                containerColor = Color(0xFF161922).copy(alpha = 0.6f),
                                            ),
                                            border = FilterChipDefaults.filterChipBorder(
                                                enabled = true,
                                                selected = isSelected,
                                                borderColor = if (isSelected) GlassWhite else Color.White.copy(alpha = 0.15f),
                                            ),
                                            shape = RoundedCornerShape(12.dp),
                                        )
                                    }
                                }
                            }
                        }
                        if (state.history.isNotEmpty()) {
                            item {
                                Spacer(Modifier.height(14.dp))
                                Text("سجل الأيام السابقة", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold, color = GlassMuted)
                            }
                            val days = state.history.map { it.date }.distinct().take(10)
                            items(days, key = { it }) { day ->
                                Text("• ${displayDayKey(day)}", color = GlassSilver, modifier = Modifier.padding(vertical = 4.dp))
                            }
                        }
                        item {
                            Spacer(Modifier.height(12.dp))
                            PrimaryButton("حفظ سجل الحضور", onClick = viewModel::save, loading = state.saving)
                        }
                    }
                }
            }
        }

        if (showDate) {
            val dateState = rememberDatePickerState()
            DatePickerDialog(
                onDismissRequest = { showDate = false },
                confirmButton = {
                    TextButton(onClick = {
                        dateState.selectedDateMillis?.let { viewModel.onDate(it.toDayKey()) }
                        showDate = false
                    }) { Text("تم", color = GlassWhite, fontWeight = FontWeight.Bold) }
                },
                dismissButton = { TextButton(onClick = { showDate = false }) { Text("إلغاء", color = GlassMuted) } },
            ) { DatePicker(state = dateState) }
        }

        // نافذة عرض ومشاركة تقرير الإنجاز والحضور اليومي (نص + PDF)
        if (showReportDialog) {
            val reportContent = viewModel.generateDailyReportText()
            AlertDialog(
                onDismissRequest = { showReportDialog = false },
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(PhosphorIcons.Regular.FileText, contentDescription = null, tint = GlassWhite, modifier = Modifier.size(24.dp))
                        Spacer(Modifier.width(8.dp))
                        Text("تقرير التسميع والسَّبْر اليومي", fontWeight = FontWeight.Bold, color = GlassWhite)
                    }
                },
                containerColor = Color(0xFF14171E),
                text = {
                    Column(
                        Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(10.dp),
                    ) {
                        Column(
                            Modifier
                                .fillMaxWidth()
                                .height(260.dp)
                                .verticalScroll(rememberScrollState())
                                .darkGlass(shape = RoundedCornerShape(12.dp), backgroundColor = Color(0xFF0C0E12).copy(alpha = 0.85f))
                                .padding(12.dp),
                        ) {
                            Text(reportContent, style = MaterialTheme.typography.bodySmall, color = GlassSilver)
                        }

                        // أزرار التصدير (PDF + نص + نسخ)
                        Row(
                            Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                        ) {
                            Button(
                                onClick = {
                                    viewModel.shareDailyReportPdf(context)
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
                                    viewModel.shareDailyReport(context)
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
                            clipboard.setPrimaryClip(ClipData.newPlainText("تقرير الحضور والإنجاز اليومي", reportContent))
                            Toast.makeText(context, "تم نسخ تقرير اليوم إلى الحافظة بنجاح ✨", Toast.LENGTH_SHORT).show()
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
