package com.omran.quran.ui.admin

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.adamglin.PhosphorIcons
import com.adamglin.phosphoricons.Regular
import com.adamglin.phosphoricons.regular.ArrowsClockwise
import com.adamglin.phosphoricons.regular.BookOpen
import com.adamglin.phosphoricons.regular.Crown
import com.adamglin.phosphoricons.regular.Exam
import com.adamglin.phosphoricons.regular.FilePdf
import com.adamglin.phosphoricons.regular.Users
import com.omran.quran.data.model.CircleStats
import com.omran.quran.ui.components.HijriDateBanner
import com.omran.quran.ui.components.LoadingBox
import com.omran.quran.ui.components.OmranCard
import com.omran.quran.ui.components.darkGlass
import com.omran.quran.ui.theme.GlassMuted
import com.omran.quran.ui.theme.GlassSilver
import com.omran.quran.ui.theme.GlassWhite
import com.omran.quran.util.PdfReportGenerator

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminReportsScreen(
    viewModel: AdminViewModel = hiltViewModel(),
) {
    val context = LocalContext.current
    val state by viewModel.state.collectAsStateWithLifecycle()

    // عند توليد PDF يفتحه مباشرةً
    LaunchedEffect(state.pdfFile) {
        state.pdfFile?.let { file ->
            PdfReportGenerator.sharePdf(context, file)
            viewModel.clearPdfFile()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp),
    ) {
        // ── شريط التاريخ الهجري ──
        HijriDateBanner(compact = true)

        // ── اختيار نوع التقرير ─────────────────────────────────────────────
        Text(
            "اختر نوع التقرير",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = GlassWhite,
        )

        // ── شريط تصنيف الجنس (الكل / بنين / بنات) ───────────────────────────
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            GenderFilterTab(
                label = "الكل",
                icon = "👥",
                isSelected = state.reportGenderFilter == null,
                onClick = { viewModel.setReportGenderFilter(null) },
                modifier = Modifier.weight(1f),
            )
            GenderFilterTab(
                label = "بنين",
                icon = "👦",
                isSelected = state.reportGenderFilter == "MALE",
                onClick = { viewModel.setReportGenderFilter("MALE") },
                modifier = Modifier.weight(1f),
            )
            GenderFilterTab(
                label = "بنات",
                icon = "👧",
                isSelected = state.reportGenderFilter == "FEMALE",
                onClick = { viewModel.setReportGenderFilter("FEMALE") },
                modifier = Modifier.weight(1f),
            )
        }

        // بطاقة التقرير الكلي
        ReportTypeCard(
            title = "تقرير كلي للمعهد",
            subtitle = "إحصائيات جميع الحلقات مجتمعةً",
            icon = PhosphorIcons.Regular.Crown,
            accentColor = Color(0xFF34D399),
            isSelected = state.selectedReportTeacher == null,
            onClick = { viewModel.selectReportTeacher(null) },
        )

        // بطاقات الحلقات الفردية
        if (state.teachers.isNotEmpty()) {
            Text(
                "تقارير الحلقات",
                style = MaterialTheme.typography.labelMedium,
                color = GlassMuted,
            )
            val displayedReportTeachers = when (state.reportGenderFilter) {
                "MALE" -> state.teachers.filter { it.isMaleCircle }
                "FEMALE" -> state.teachers.filter { it.isFemaleCircle }
                else -> state.teachers
            }
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                items(displayedReportTeachers.size) { idx ->
                    val teacher = displayedReportTeachers[idx]
                    val isFemale = teacher.isFemaleCircle
                    ReportTypeCard(
                        title = teacher.displayCircleName,
                        subtitle = "معلم الحلقة: ${teacher.name}  •  ${if (isFemale) "👧 حلقة بنات" else "👦 حلقة بنين"}",
                        icon = PhosphorIcons.Regular.BookOpen,
                        accentColor = if (isFemale) Color(0xFFF472B6) else Color(0xFF60A5FA),
                        isSelected = state.selectedReportTeacher?.userId == teacher.userId,
                        onClick = { viewModel.selectReportTeacher(teacher) },
                    )
                }
                item { Spacer(Modifier.height(16.dp)) }
            }
        }

        // ── نتيجة التقرير + زر PDF ─────────────────────────────────────────
        AnimatedContent(
            targetState = state.reportLoading to state.reportStats,
            transitionSpec = { fadeIn() togetherWith fadeOut() },
            label = "reportStats",
        ) { (loading, stats) ->
            when {
                loading -> LoadingBox()
                stats != null -> Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    val genderTitleSuffix = when (state.reportGenderFilter) {
                        "MALE" -> " (بنين)"
                        "FEMALE" -> " (بنات)"
                        else -> ""
                    }
                    ReportStatsPanel(
                        stats = stats,
                        title = (state.selectedReportTeacher?.displayCircleName ?: "المعهد بأكمله") + genderTitleSuffix,
                        genderFilter = state.reportGenderFilter,
                    )
                    // زر تصدير PDF
                    val pdfButtonLabel = when {
                        state.pdfGenerating -> "جارٍ توليد التقرير..."
                        state.reportGenderFilter == "MALE" -> "📄 تصدير كـ PDF (بنين)"
                        state.reportGenderFilter == "FEMALE" -> "📄 تصدير كـ PDF (بنات)"
                        else -> "📄 تصدير كـ PDF (شامل مفصل)"
                    }
                    Button(
                        onClick = { viewModel.generateCirclePdf() },
                        enabled = !state.pdfGenerating,
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF1E3A5F),
                            contentColor = Color.White,
                        ),
                        shape = RoundedCornerShape(14.dp),
                    ) {
                        if (state.pdfGenerating) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(18.dp),
                                color = Color.White,
                                strokeWidth = 2.dp,
                            )
                        } else {
                            Icon(
                                PhosphorIcons.Regular.FilePdf,
                                contentDescription = null,
                                modifier = Modifier.size(20.dp),
                            )
                        }
                        Spacer(Modifier.width(8.dp))
                        Text(
                            pdfButtonLabel,
                            fontWeight = FontWeight.Bold,
                        )
                    }
                }
                else -> {}
            }
        }
        Spacer(Modifier.height(80.dp))
    }
}

@Composable
private fun ReportTypeCard(
    title: String,
    subtitle: String,
    icon: ImageVector,
    accentColor: Color,
    isSelected: Boolean,
    onClick: () -> Unit,
) {
    OmranCard(
        modifier = Modifier.fillMaxWidth(),
        onClick = onClick,
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(14.dp),
            modifier = Modifier.fillMaxWidth(),
        ) {
            Box(
                modifier = Modifier
                    .size(46.dp)
                    .clip(CircleShape)
                    .background(accentColor.copy(alpha = if (isSelected) 0.25f else 0.10f))
                    .border(
                        1.dp,
                        accentColor.copy(alpha = if (isSelected) 0.6f else 0.25f),
                        CircleShape,
                    ),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    icon,
                    contentDescription = null,
                    tint = accentColor,
                    modifier = Modifier.size(22.dp),
                )
            }
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    title,
                    fontWeight = FontWeight.Bold,
                    color = GlassWhite,
                    style = MaterialTheme.typography.titleSmall,
                )
                Text(
                    subtitle,
                    style = MaterialTheme.typography.bodySmall,
                    color = GlassSilver,
                )
            }
            if (isSelected) {
                Box(
                    modifier = Modifier
                        .size(10.dp)
                        .clip(CircleShape)
                        .background(accentColor),
                )
            }
        }
    }
}

@Composable
private fun ReportStatsPanel(
    stats: CircleStats,
    title: String,
    genderFilter: String? = null,
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        // عنوان
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Box(
                Modifier
                    .size(3.dp, 22.dp)
                    .background(
                        Brush.verticalGradient(listOf(Color(0xFF818CF8), Color(0xFF38BDF8))),
                        RoundedCornerShape(2.dp),
                    )
            )
            Text(
                "تقرير: $title",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = GlassWhite,
            )
        }

        // تفصيل توزيع البنين والبنات عند اختيار تقرير شامل
        if (genderFilter == null && (stats.maleStudentCount > 0 || stats.femaleStudentCount > 0)) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color(0xFF1E293B).copy(alpha = 0.6f))
                    .border(1.dp, Color.White.copy(alpha = 0.08f), RoundedCornerShape(12.dp))
                    .padding(horizontal = 14.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                ) {
                    Text("👦", style = MaterialTheme.typography.titleSmall)
                    Text(
                        "البنين: ${stats.maleStudentCount} طالب",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color(0xFF93C5FD),
                        fontWeight = FontWeight.Bold,
                    )
                }
                Box(
                    modifier = Modifier
                        .size(1.dp, 16.dp)
                        .background(Color.White.copy(alpha = 0.2f))
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                ) {
                    Text("👧", style = MaterialTheme.typography.titleSmall)
                    Text(
                        "البنات: ${stats.femaleStudentCount} طالبة",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color(0xFFF9A8D4),
                        fontWeight = FontWeight.Bold,
                    )
                }
            }
        }

        // مربعات الإحصائيات
        val studentLabel = when (genderFilter) {
            "MALE" -> "طالب (بنين)"
            "FEMALE" -> "طالبة (بنات)"
            else -> "طالب"
        }
        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            ReportStatChip(
                modifier = Modifier.weight(1f),
                value = "${stats.studentCount}",
                label = studentLabel,
                icon = PhosphorIcons.Regular.Users,
                color = when (genderFilter) {
                    "MALE" -> Color(0xFF60A5FA)
                    "FEMALE" -> Color(0xFFF472B6)
                    else -> Color(0xFF818CF8)
                },
            )
            ReportStatChip(
                modifier = Modifier.weight(1f),
                value = "${stats.memorizationCount}",
                label = "جلسة حفظ",
                icon = PhosphorIcons.Regular.BookOpen,
                color = Color(0xFF34D399),
            )
        }
        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            ReportStatChip(
                modifier = Modifier.weight(1f),
                value = "${stats.revisionCount}",
                label = "جلسة مراجعة",
                icon = PhosphorIcons.Regular.ArrowsClockwise,
                color = Color(0xFFFBBF24),
            )
            ReportStatChip(
                modifier = Modifier.weight(1f),
                value = "${stats.examCount}",
                label = "اختبار سبر",
                icon = PhosphorIcons.Regular.Exam,
                color = Color(0xFFF87171),
            )
        }
    }
}

@Composable
private fun GenderFilterTab(
    label: String,
    icon: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val accentColor = when (label) {
        "بنين" -> Color(0xFF60A5FA)
        "بنات" -> Color(0xFFF472B6)
        else -> Color(0xFF34D399)
    }

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(
                if (isSelected) accentColor.copy(alpha = 0.22f)
                else Color.White.copy(alpha = 0.05f)
            )
            .border(
                1.dp,
                if (isSelected) accentColor.copy(alpha = 0.6f) else Color.White.copy(alpha = 0.12f),
                RoundedCornerShape(12.dp),
            )
            .clickable(onClick = onClick)
            .padding(vertical = 10.dp),
        contentAlignment = Alignment.Center,
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp),
        ) {
            Text(icon, style = MaterialTheme.typography.bodyMedium)
            Text(
                label,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                color = if (isSelected) GlassWhite else GlassMuted,
            )
        }
    }
}

@Composable
private fun ReportStatChip(
    value: String,
    label: String,
    icon: ImageVector,
    color: Color,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .darkGlass(
                shape = RoundedCornerShape(16.dp),
                backgroundColor = color.copy(alpha = 0.08f),
            )
            .padding(16.dp),
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Icon(
                icon,
                contentDescription = null,
                tint = color,
                modifier = Modifier.size(22.dp),
            )
            Text(
                value,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = GlassWhite,
            )
            Text(
                label,
                style = MaterialTheme.typography.labelSmall,
                color = GlassMuted,
            )
        }
    }
}
