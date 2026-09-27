package com.omran.quran.ui.admin

import android.widget.Toast
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.adamglin.PhosphorIcons
import com.adamglin.phosphoricons.Regular
import com.adamglin.phosphoricons.regular.ArrowLeft
import com.adamglin.phosphoricons.regular.ArrowsClockwise
import com.adamglin.phosphoricons.regular.BookOpen
import com.adamglin.phosphoricons.regular.CalendarCheck
import com.adamglin.phosphoricons.regular.ChalkboardTeacher
import com.adamglin.phosphoricons.regular.Crown
import com.adamglin.phosphoricons.regular.Gear
import com.adamglin.phosphoricons.regular.PencilSimple
import com.adamglin.phosphoricons.regular.Plus
import com.adamglin.phosphoricons.regular.Star
import com.adamglin.phosphoricons.regular.Trash
import com.adamglin.phosphoricons.regular.Users
import com.adamglin.phosphoricons.regular.Warning
import com.omran.quran.data.model.AppUser
import com.omran.quran.ui.components.DarkGlassBackground
import com.omran.quran.ui.components.EmptyState
import com.omran.quran.ui.components.ErrorBanner
import com.omran.quran.ui.components.HijriDateBanner
import com.omran.quran.ui.components.LoadingBox
import com.omran.quran.ui.components.OmranCard
import com.omran.quran.ui.components.OmranField
import com.omran.quran.ui.components.PrimaryButton
import com.omran.quran.ui.components.darkGlass
import com.omran.quran.ui.theme.GlassMuted
import com.omran.quran.ui.theme.GlassSilver
import com.omran.quran.ui.theme.GlassWhite

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminTeachersScreen(
    onSelectTeacher: () -> Unit,
    onSettings: () -> Unit = {},
    viewModel: AdminViewModel = hiltViewModel(),
) {
    val context = LocalContext.current
    val state by viewModel.state.collectAsStateWithLifecycle()
    val user by viewModel.user.collectAsStateWithLifecycle()

    var teacherToDelete by remember { mutableStateOf<AppUser?>(null) }

    LaunchedEffect(state.successMessage) {
        state.successMessage?.let {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
            viewModel.clearSuccessMessage()
        }
    }

    DarkGlassBackground {
        Scaffold(
            containerColor = Color.Transparent,
            topBar = {
                TopAppBar(
                    title = {
                        Column {
                            Text(
                                when (state.section) {
                                    AdminSection.DASHBOARD -> "لوحة الإدارة"
                                    AdminSection.CIRCLES -> "الحلقات القرآنية"
                                    AdminSection.DISTINGUISHED -> "المتميزون"
                                    AdminSection.REPORTS -> "التقارير"
                                },
                                fontWeight = FontWeight.Bold,
                                color = GlassWhite,
                            )
                            Text(
                                user?.instituteName?.ifBlank { "معهد المقرئ" } ?: "معهد المقرئ",
                                style = MaterialTheme.typography.bodySmall,
                                color = GlassMuted,
                            )
                        }
                    },
                    navigationIcon = {
                        if (state.section != AdminSection.DASHBOARD) {
                            IconButton(onClick = viewModel::navigateToDashboard) {
                                Box(
                                    Modifier
                                        .size(38.dp)
                                        .clip(CircleShape)
                                        .background(Color.White.copy(alpha = 0.08f)),
                                    contentAlignment = Alignment.Center,
                                ) {
                                    Icon(
                                        PhosphorIcons.Regular.ArrowLeft,
                                        contentDescription = "رجوع للوحة الإدارة",
                                        tint = GlassWhite,
                                        modifier = Modifier.size(20.dp),
                                    )
                                }
                            }
                        }
                    },
                    actions = {
                        // شارة المدير
                        Box(
                            Modifier
                                .padding(end = 4.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(Color(0xFFFBBF24).copy(alpha = 0.15f))
                                .border(1.dp, Color(0xFFFBBF24).copy(alpha = 0.3f), RoundedCornerShape(12.dp))
                                .padding(horizontal = 9.dp, vertical = 5.dp),
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp),
                            ) {
                                Icon(
                                    PhosphorIcons.Regular.Crown,
                                    contentDescription = null,
                                    tint = Color(0xFFFBBF24),
                                    modifier = Modifier.size(15.dp),
                                )
                                Text(
                                    "مدير المعهد",
                                    color = Color(0xFFFBBF24),
                                    style = MaterialTheme.typography.labelSmall,
                                    fontWeight = FontWeight.Bold,
                                )
                            }
                        }
                        IconButton(onClick = viewModel::refreshAll) {
                            Box(
                                Modifier
                                    .size(38.dp)
                                    .clip(CircleShape)
                                    .background(Color.White.copy(alpha = 0.08f)),
                                contentAlignment = Alignment.Center,
                            ) {
                                Icon(
                                    PhosphorIcons.Regular.ArrowsClockwise,
                                    contentDescription = "تحديث ومزامنة البيانات",
                                    tint = GlassWhite,
                                    modifier = Modifier.size(20.dp),
                                )
                            }
                        }
                        IconButton(onClick = onSettings) {
                            Box(
                                Modifier
                                    .size(38.dp)
                                    .clip(CircleShape)
                                    .background(Color.White.copy(alpha = 0.08f)),
                                contentAlignment = Alignment.Center,
                            ) {
                                Icon(
                                    PhosphorIcons.Regular.Gear,
                                    contentDescription = "الإعدادات",
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
            AnimatedContent(
                targetState = state.section,
                transitionSpec = {
                    if (targetState == AdminSection.DASHBOARD) {
                        slideInHorizontally { -it } + fadeIn() togetherWith slideOutHorizontally { it } + fadeOut()
                    } else {
                        slideInHorizontally { it } + fadeIn() togetherWith slideOutHorizontally { -it } + fadeOut()
                    }
                },
                label = "adminSection",
            ) { section ->
                when (section) {
                    AdminSection.DASHBOARD -> DashboardSection(
                        padding = padding,
                        state = state,
                        onNavigate = { viewModel.navigateTo(it) },
                    )
                    AdminSection.CIRCLES -> CirclesSection(
                        padding = padding,
                        state = state,
                        onOpenAdd = viewModel::openAddCircle,
                        onGenderFilter = { viewModel.setCirclesGenderFilter(it) },
                        onSelectTeacher = { teacher ->
                            viewModel.selectTeacher(teacher)
                            onSelectTeacher()
                        },
                        onEdit = { viewModel.openEditCircle(it) },
                        onDelete = { teacherToDelete = it },
                    )
                    AdminSection.DISTINGUISHED -> DistinguishedSection(
                        padding = padding,
                        state = state,
                        onGenderFilter = { viewModel.setTopStudentsGender(it) },
                        onSelectTeacher = { teacher ->
                            viewModel.selectTeacher(teacher)
                            onSelectTeacher()
                        },
                    )
                    AdminSection.REPORTS -> Column(
                        Modifier
                            .fillMaxSize()
                            .padding(padding)
                            .padding(top = 8.dp),
                    ) {
                        AdminReportsScreen(viewModel = viewModel)
                    }
                }
            }
        }

        // ── نافذة إنشاء حلقة جديدة ──────────────────────────────────────────
        if (state.isAddTeacherOpen) {
            AlertDialog(
                onDismissRequest = viewModel::closeAddTeacher,
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            PhosphorIcons.Regular.BookOpen,
                            contentDescription = null,
                            tint = GlassWhite,
                            modifier = Modifier.size(22.dp),
                        )
                        Spacer(Modifier.width(8.dp))
                        Text("إنشاء حلقة قرآنية جديدة", fontWeight = FontWeight.Bold, color = GlassWhite)
                    }
                },
                containerColor = Color(0xFF14171E),
                text = {
                    Column(
                        Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(10.dp),
                    ) {
                        ErrorBanner(state.addError) { viewModel.updateCircleForm() }
                        OmranField(
                            value = state.circleName,
                            onValueChange = { viewModel.updateCircleForm(circle = it) },
                            label = "اسم الحلقة القرآنية (مثال: حلقة الإمام الشاطبي)",
                        )

                        // ── نوع الحلقة (بنين / بنات) ──
                        Text(
                            "نوع الحلقة القرآنية:",
                            style = MaterialTheme.typography.labelMedium,
                            color = GlassSilver,
                        )
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                        ) {
                            val isMale = state.circleGender != "FEMALE"
                            // خيار بنين
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(
                                        if (isMale) Color(0xFF1E3A5F).copy(alpha = 0.85f)
                                        else Color.White.copy(alpha = 0.05f)
                                    )
                                    .border(
                                        1.5.dp,
                                        if (isMale) Color(0xFF60A5FA) else Color.White.copy(alpha = 0.12f),
                                        RoundedCornerShape(12.dp),
                                    )
                                    .clickable { viewModel.updateCircleForm(circleGender = "MALE") }
                                    .padding(vertical = 10.dp),
                                contentAlignment = Alignment.Center,
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                                ) {
                                    Text("👦", style = MaterialTheme.typography.bodyLarge)
                                    Text(
                                        "حلقة بنين (ذكور)",
                                        style = MaterialTheme.typography.bodySmall,
                                        fontWeight = if (isMale) FontWeight.Bold else FontWeight.Normal,
                                        color = if (isMale) GlassWhite else GlassMuted,
                                    )
                                }
                            }

                            // خيار بنات
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(
                                        if (!isMale) Color(0xFF4C1D45).copy(alpha = 0.85f)
                                        else Color.White.copy(alpha = 0.05f)
                                    )
                                    .border(
                                        1.5.dp,
                                        if (!isMale) Color(0xFFF472B6) else Color.White.copy(alpha = 0.12f),
                                        RoundedCornerShape(12.dp),
                                    )
                                    .clickable { viewModel.updateCircleForm(circleGender = "FEMALE") }
                                    .padding(vertical = 10.dp),
                                contentAlignment = Alignment.Center,
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                                ) {
                                    Text("👧", style = MaterialTheme.typography.bodyLarge)
                                    Text(
                                        "حلقة بنات (إناث)",
                                        style = MaterialTheme.typography.bodySmall,
                                        fontWeight = if (!isMale) FontWeight.Bold else FontWeight.Normal,
                                        color = if (!isMale) GlassWhite else GlassMuted,
                                    )
                                }
                            }
                        }

                        OmranField(
                            value = state.teacherName,
                            onValueChange = { viewModel.updateTeacherForm(name = it) },
                            label = "اسم معلم الحلقة",
                        )
                        OmranField(
                            value = state.teacherEmail,
                            onValueChange = { viewModel.updateTeacherForm(email = it) },
                            label = "البريد الإلكتروني للمعلم",
                            keyboardType = KeyboardType.Email,
                        )
                        OmranField(
                            value = state.teacherPassword,
                            onValueChange = { viewModel.updateTeacherForm(pass = it) },
                            label = "كلمة مرور حساب المعلم (6 أحرف فأكثر)",
                            keyboardType = KeyboardType.Password,
                            isPassword = true,
                        )
                    }
                },
                confirmButton = {
                    PrimaryButton(
                        text = "إنشاء الحلقة",
                        onClick = viewModel::saveCircle,
                        loading = state.savingTeacher,
                    )
                },
                dismissButton = {
                    TextButton(onClick = viewModel::closeAddTeacher) {
                        Text("إلغاء", color = GlassMuted)
                    }
                },
            )
        }

        // ── نافذة تعديل بيانات الحلقة والمعلم ────────────────────────────────
        if (state.isEditCircleOpen) {
            AlertDialog(
                onDismissRequest = viewModel::closeEditCircle,
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            PhosphorIcons.Regular.PencilSimple,
                            contentDescription = null,
                            tint = GlassWhite,
                            modifier = Modifier.size(22.dp),
                        )
                        Spacer(Modifier.width(8.dp))
                        Text("تعديل بيانات الحلقة والمعلم", fontWeight = FontWeight.Bold, color = GlassWhite)
                    }
                },
                containerColor = Color(0xFF14171E),
                text = {
                    Column(
                        Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(10.dp),
                    ) {
                        Text(
                            "يمكنك تغيير اسم الحلقة أو تغيير اسم المعلم ونوع الحلقة مع الحفاظ على جميع الطلاب وسجلاتهم في نفس الحلقة.",
                            style = MaterialTheme.typography.bodySmall,
                            color = GlassSilver,
                        )
                        ErrorBanner(state.editError) { viewModel.updateEditCircleForm() }
                        OmranField(
                            value = state.editCircleName,
                            onValueChange = { viewModel.updateEditCircleForm(circleName = it) },
                            label = "اسم الحلقة القرآنية",
                        )

                        // ── نوع الحلقة (بنين / بنات) ──
                        Text(
                            "نوع الحلقة القرآنية:",
                            style = MaterialTheme.typography.labelMedium,
                            color = GlassSilver,
                        )
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                        ) {
                            val isMale = state.editCircleGender != "FEMALE"
                            // خيار بنين
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(
                                        if (isMale) Color(0xFF1E3A5F).copy(alpha = 0.85f)
                                        else Color.White.copy(alpha = 0.05f)
                                    )
                                    .border(
                                        1.5.dp,
                                        if (isMale) Color(0xFF60A5FA) else Color.White.copy(alpha = 0.12f),
                                        RoundedCornerShape(12.dp),
                                    )
                                    .clickable { viewModel.updateEditCircleForm(circleGender = "MALE") }
                                    .padding(vertical = 10.dp),
                                contentAlignment = Alignment.Center,
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                                ) {
                                    Text("👦", style = MaterialTheme.typography.bodyLarge)
                                    Text(
                                        "حلقة بنين (ذكور)",
                                        style = MaterialTheme.typography.bodySmall,
                                        fontWeight = if (isMale) FontWeight.Bold else FontWeight.Normal,
                                        color = if (isMale) GlassWhite else GlassMuted,
                                    )
                                }
                            }

                            // خيار بنات
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(
                                        if (!isMale) Color(0xFF4C1D45).copy(alpha = 0.85f)
                                        else Color.White.copy(alpha = 0.05f)
                                    )
                                    .border(
                                        1.5.dp,
                                        if (!isMale) Color(0xFFF472B6) else Color.White.copy(alpha = 0.12f),
                                        RoundedCornerShape(12.dp),
                                    )
                                    .clickable { viewModel.updateEditCircleForm(circleGender = "FEMALE") }
                                    .padding(vertical = 10.dp),
                                contentAlignment = Alignment.Center,
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                                ) {
                                    Text("👧", style = MaterialTheme.typography.bodyLarge)
                                    Text(
                                        "حلقة بنات (إناث)",
                                        style = MaterialTheme.typography.bodySmall,
                                        fontWeight = if (!isMale) FontWeight.Bold else FontWeight.Normal,
                                        color = if (!isMale) GlassWhite else GlassMuted,
                                    )
                                }
                            }
                        }

                        OmranField(
                            value = state.editTeacherName,
                            onValueChange = { viewModel.updateEditCircleForm(teacherName = it) },
                            label = "اسم معلم الحلقة",
                        )
                    }
                },
                confirmButton = {
                    PrimaryButton(
                        text = "حفظ التعديلات",
                        onClick = viewModel::saveEditedCircle,
                        loading = state.savingEdit,
                    )
                },
                dismissButton = {
                    TextButton(onClick = viewModel::closeEditCircle) {
                        Text("إلغاء", color = GlassMuted)
                    }
                },
            )
        }

        // ── نافذة تأكيد حذف الحلقة ─────────────────────────────────────────
        teacherToDelete?.let { teacher ->
            AlertDialog(
                onDismissRequest = { teacherToDelete = null },
                icon = {
                    Icon(
                        PhosphorIcons.Regular.Warning,
                        contentDescription = null,
                        tint = Color(0xFFF87171),
                        modifier = Modifier.size(28.dp),
                    )
                },
                title = {
                    Text("تأكيد حذف الحلقة", fontWeight = FontWeight.Bold, color = GlassWhite)
                },
                text = {
                    Text(
                        "هل أنت متأكد من رغبتك في حذف «${teacher.displayCircleName}» (المشرف عليها: ${teacher.name})؟",
                        color = GlassSilver,
                    )
                },
                containerColor = Color(0xFF14171E),
                confirmButton = {
                    Button(
                        onClick = {
                            viewModel.deleteTeacher(teacher.userId)
                            teacherToDelete = null
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFDC2626)),
                        shape = RoundedCornerShape(12.dp),
                    ) {
                        Text("نعم، حذف الحلقة", color = Color.White, fontWeight = FontWeight.Bold)
                    }
                },
                dismissButton = {
                    TextButton(onClick = { teacherToDelete = null }) {
                        Text("إلغاء", color = GlassMuted)
                    }
                },
            )
        }
    }
}

// ══════════════════════════════════════════════════════════════════════════
// ── قسم Dashboard ─────────────────────────────────────────────────────────
// ══════════════════════════════════════════════════════════════════════════

@Composable
private fun DashboardSection(
    padding: PaddingValues,
    state: AdminUiState,
    onNavigate: (AdminSection) -> Unit,
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(padding)
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp),
    ) {
        // ── شريط التاريخ الهجري ──────────────────────────────────────────
        item {
            HijriDateBanner(
                modifier = Modifier.padding(bottom = 2.dp),
            )
        }

        // ── بطاقات الإحصائيات (غير قابلة للضغط) ─────────────────────────
        item {
            Text(
                "ملخص المعهد",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = GlassWhite,
            )
        }
        item {
            // شبكة 2×2
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Row(
                    Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                ) {
                    DashboardStatCard(
                        modifier = Modifier.weight(1f),
                        icon = PhosphorIcons.Regular.Users,
                        label = "إجمالي الطلاب",
                        value = "${state.totalStudents}",
                        accentColor = Color(0xFF818CF8),
                        subText = if (state.totalStudents > 0) "👦 ${state.totalMaleStudents}  •  👧 ${state.totalFemaleStudents}" else null,
                    )
                    val maleCircles = state.teachers.count { it.isMaleCircle }
                    val femaleCircles = state.teachers.count { it.isFemaleCircle }
                    DashboardStatCard(
                        modifier = Modifier.weight(1f),
                        icon = PhosphorIcons.Regular.BookOpen,
                        label = "عدد الحلقات",
                        value = "${state.teachers.size}",
                        accentColor = Color(0xFF34D399),
                        subText = if (state.teachers.isNotEmpty()) "👦 $maleCircles  •  👧 $femaleCircles" else null,
                    )
                }
                Row(
                    Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                ) {
                    DashboardStatCard(
                        modifier = Modifier.weight(1f),
                        icon = PhosphorIcons.Regular.ChalkboardTeacher,
                        label = "المعلمون",
                        value = "${state.teachers.size}",
                        accentColor = Color(0xFF60A5FA),
                    )
                    DashboardStatCard(
                        modifier = Modifier.weight(1f),
                        icon = PhosphorIcons.Regular.CalendarCheck,
                        label = "متوسط حضور الطلاب",
                        value = if (state.totalAttendanceRecords == 0) "—" else "${state.averageAttendancePercentage}%",
                        accentColor = Color(0xFF10B981),
                        subText = if (state.totalAttendanceRecords > 0) "حاضر: ${state.presentAttendanceCount}  •  غائب: ${state.absentAttendanceCount}" else null,
                    )
                }
            }
        }

        item { Spacer(Modifier.height(4.dp)) }

        // ── عنوان مربعات التنقل ───────────────────────────────────────────
        item {
            Text(
                "الأقسام",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = GlassWhite,
            )
        }

        // ── مربعات التنقل القابلة للضغط ──────────────────────────────────
        item {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                // الحلقات
                DashboardNavCard(
                    title = "الحلقات القرآنية",
                    subtitle = "${state.teachers.size} حلقة مسجلة في المعهد",
                    icon = PhosphorIcons.Regular.BookOpen,
                    gradientColors = listOf(Color(0xFF1E3A5F), Color(0xFF0F2744)),
                    accentColor = Color(0xFF60A5FA),
                    onClick = { onNavigate(AdminSection.CIRCLES) },
                )
                // صف المتميزون والتقارير
                Row(
                    Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                ) {
                    DashboardNavCardSmall(
                        modifier = Modifier.weight(1f),
                        title = "المتميزون",
                        icon = PhosphorIcons.Regular.Star,
                        gradientColors = listOf(Color(0xFF3D2A00), Color(0xFF201500)),
                        accentColor = Color(0xFFFBBF24),
                        onClick = { onNavigate(AdminSection.DISTINGUISHED) },
                    )
                    DashboardNavCardSmall(
                        modifier = Modifier.weight(1f),
                        title = "التقارير",
                        icon = PhosphorIcons.Regular.ArrowsClockwise,
                        gradientColors = listOf(Color(0xFF0E3D2B), Color(0xFF052016)),
                        accentColor = Color(0xFF34D399),
                        onClick = { onNavigate(AdminSection.REPORTS) },
                    )
                }
            }
        }
        item { Spacer(Modifier.height(80.dp)) }
    }
}

@Composable
private fun DashboardStatCard(
    icon: ImageVector,
    label: String,
    value: String,
    accentColor: Color,
    modifier: Modifier = Modifier,
    subText: String? = null,
) {
    Box(
        modifier = modifier
            .darkGlass(
                shape = RoundedCornerShape(18.dp),
                backgroundColor = Color(0xFF161922).copy(alpha = 0.85f),
            )
            .padding(16.dp),
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(accentColor.copy(alpha = 0.15f))
                    .border(1.dp, accentColor.copy(alpha = 0.3f), CircleShape),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    icon,
                    contentDescription = null,
                    tint = accentColor,
                    modifier = Modifier.size(20.dp),
                )
            }
            Text(
                value,
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = GlassWhite,
            )
            Text(
                label,
                style = MaterialTheme.typography.labelSmall,
                color = GlassMuted,
                lineHeight = 16.sp,
            )
            if (!subText.isNullOrBlank()) {
                Text(
                    subText,
                    style = MaterialTheme.typography.labelSmall,
                    color = accentColor.copy(alpha = 0.9f),
                    fontWeight = FontWeight.Bold,
                )
            }
        }
    }
}

@Composable
private fun DashboardNavCard(
    title: String,
    subtitle: String,
    icon: ImageVector,
    gradientColors: List<Color>,
    accentColor: Color,
    onClick: () -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(Brush.linearGradient(gradientColors))
            .border(1.dp, accentColor.copy(alpha = 0.25f), RoundedCornerShape(20.dp))
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onClick,
            )
            .padding(20.dp),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .clip(CircleShape)
                    .background(accentColor.copy(alpha = 0.18f))
                    .border(1.dp, accentColor.copy(alpha = 0.4f), CircleShape),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    icon,
                    contentDescription = null,
                    tint = accentColor,
                    modifier = Modifier.size(28.dp),
                )
            }
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    title,
                    fontWeight = FontWeight.Bold,
                    color = GlassWhite,
                    style = MaterialTheme.typography.titleMedium,
                )
                Text(
                    subtitle,
                    style = MaterialTheme.typography.bodySmall,
                    color = accentColor.copy(alpha = 0.8f),
                )
            }
            Icon(
                PhosphorIcons.Regular.ArrowLeft,
                contentDescription = null,
                tint = accentColor.copy(alpha = 0.6f),
                modifier = Modifier.size(20.dp),
            )
        }
    }
}

@Composable
private fun DashboardNavCardSmall(
    title: String,
    icon: ImageVector,
    gradientColors: List<Color>,
    accentColor: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(20.dp))
            .background(Brush.linearGradient(gradientColors))
            .border(1.dp, accentColor.copy(alpha = 0.25f), RoundedCornerShape(20.dp))
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onClick,
            )
            .padding(20.dp),
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.fillMaxWidth(),
        ) {
            Box(
                modifier = Modifier
                    .size(52.dp)
                    .clip(CircleShape)
                    .background(accentColor.copy(alpha = 0.18f))
                    .border(1.dp, accentColor.copy(alpha = 0.4f), CircleShape),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    icon,
                    contentDescription = null,
                    tint = accentColor,
                    modifier = Modifier.size(26.dp),
                )
            }
            Text(
                title,
                fontWeight = FontWeight.Bold,
                color = GlassWhite,
                style = MaterialTheme.typography.titleSmall,
            )
        }
    }
}

// ══════════════════════════════════════════════════════════════════════════
// ── قسم الحلقات ──────────────────────────────────────────────────────────
// ══════════════════════════════════════════════════════════════════════════

@Composable
private fun CirclesSection(
    padding: PaddingValues,
    state: AdminUiState,
    onOpenAdd: () -> Unit,
    onGenderFilter: (String?) -> Unit,
    onSelectTeacher: (AppUser) -> Unit,
    onEdit: (AppUser) -> Unit,
    onDelete: (AppUser) -> Unit,
) {
    Column(
        Modifier
            .fillMaxSize()
            .padding(padding)
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                "الحلقات القرآنية",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = GlassWhite,
            )
            Button(
                onClick = onOpenAdd,
                colors = ButtonDefaults.buttonColors(
                    containerColor = GlassWhite,
                    contentColor = Color.Black,
                ),
                shape = RoundedCornerShape(12.dp),
            ) {
                Icon(PhosphorIcons.Regular.Plus, contentDescription = null, Modifier.size(18.dp))
                Spacer(Modifier.width(6.dp))
                Text("إنشاء حلقة", fontWeight = FontWeight.Bold)
            }
        }

        // ── شريط تصنيف الحلقات (الكل / بنين / بنات) ───────────────────────────
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            val filter = state.circlesGenderFilter
            listOf(
                Triple("الكل", "👥", null),
                Triple("حلقات البنين", "👦", "MALE"),
                Triple("حلقات البنات", "👧", "FEMALE"),
            ).forEach { (label, icon, g) ->
                val isSelected = filter == g
                val accentColor = when (g) {
                    "MALE" -> Color(0xFF60A5FA)
                    "FEMALE" -> Color(0xFFF472B6)
                    else -> Color(0xFF34D399)
                }
                Box(
                    modifier = Modifier
                        .weight(1f)
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
                        .clickable { onGenderFilter(g) }
                        .padding(vertical = 9.dp),
                    contentAlignment = Alignment.Center,
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(5.dp),
                    ) {
                        Text(icon, style = MaterialTheme.typography.bodyMedium)
                        Text(
                            label,
                            style = MaterialTheme.typography.bodySmall,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                            color = if (isSelected) GlassWhite else GlassMuted,
                        )
                    }
                }
            }
        }

        val displayedTeachers = when (state.circlesGenderFilter) {
            "MALE" -> state.teachers.filter { it.isMaleCircle }
            "FEMALE" -> state.teachers.filter { it.isFemaleCircle }
            else -> state.teachers
        }

        AnimatedContent(
            targetState = state.loading,
            transitionSpec = { fadeIn() togetherWith fadeOut() },
            label = "teacherList",
        ) { loading ->
            when {
                loading -> LoadingBox()
                displayedTeachers.isEmpty() -> EmptyState(
                    when (state.circlesGenderFilter) {
                        "MALE" -> "لا توجد حلقات بنين مسجلة بعد"
                        "FEMALE" -> "لا توجد حلقات بنات مسجلة بعد"
                        else -> "لا توجد حلقات قرآنية مسجلة بعد. أنشئ أول حلقة الآن!"
                    }
                )
                else -> LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    items(displayedTeachers, key = { it.userId }) { teacher ->
                        TeacherCard(
                            teacher = teacher,
                            onSelect = { onSelectTeacher(teacher) },
                            onEdit = { onEdit(teacher) },
                            onDelete = { onDelete(teacher) },
                        )
                    }
                    item { Spacer(Modifier.height(100.dp)) }
                }
            }
        }
    }
}

// ══════════════════════════════════════════════════════════════════════════
// ── قسم المتميزون ──────────────────────────────────────────────────
// ══════════════════════════════════════════════════════════════════════════

@Composable
private fun DistinguishedSection(
    padding: PaddingValues,
    state: AdminUiState,
    onGenderFilter: (String?) -> Unit,
    onSelectTeacher: (AppUser) -> Unit,
) {
    Column(
        Modifier
            .fillMaxSize()
            .padding(padding)
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Text(
            "🏆 الطلاب المتميزون",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = GlassWhite,
        )
        Text(
            "مرتبون حسب عدد جلسات الحفظ عبر كامل فترة عملهم في المعهد",
            style = MaterialTheme.typography.bodySmall,
            color = GlassMuted,
        )

        // ── شريط تصنيف الجنس (الكل / بنين / بنات) ───────────────────────────
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            val filter = state.topStudentsGenderFilter
            listOf(
                Triple("الكل", "👥", null),
                Triple("بنين", "👦", "MALE"),
                Triple("بنات", "👧", "FEMALE"),
            ).forEach { (label, icon, g) ->
                val isSelected = filter == g
                val accentColor = when (label) {
                    "بنين" -> Color(0xFF60A5FA)
                    "بنات" -> Color(0xFFF472B6)
                    else -> Color(0xFFFBBF24)
                }
                Box(
                    modifier = Modifier
                        .weight(1f)
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
                        .clickable { onGenderFilter(g) }
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
        }

        when {
            state.topStudentsLoading -> LoadingBox()
            state.topStudents.isEmpty() -> EmptyState("لا توجد بيانات طلاب بعد في هذا التصنيف")
            else -> LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                items(state.topStudents.size, key = { state.topStudents[it].studentId }) { index ->
                    val student = state.topStudents[index]
                    val rank = index + 1
                    val (medalEmoji, medalColor) = when (rank) {
                        1 -> "🥇" to Color(0xFFFFD700)
                        2 -> "🥈" to Color(0xFFC0C0C0)
                        3 -> "🥉" to Color(0xFFCD7F32)
                        else -> "$rank" to GlassMuted
                    }

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(16.dp))
                            .background(
                                if (rank <= 3)
                                    Brush.linearGradient(
                                        when (rank) {
                                            1 -> listOf(Color(0xFF3D2E00), Color(0xFF201800))
                                            2 -> listOf(Color(0xFF252525), Color(0xFF1A1A1A))
                                            else -> listOf(Color(0xFF2D1B0A), Color(0xFF1A0F05))
                                        }
                                    )
                                else Brush.linearGradient(listOf(Color(0xFF161922), Color(0xFF161922)))
                            )
                            .border(
                                1.dp,
                                medalColor.copy(alpha = if (rank <= 3) 0.4f else 0.1f),
                                RoundedCornerShape(16.dp),
                            )
                            .padding(14.dp),
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                        ) {
                            // رقم الترتيب / ميدالية
                            Box(
                                modifier = Modifier
                                    .size(44.dp)
                                    .clip(CircleShape)
                                    .background(medalColor.copy(alpha = if (rank <= 3) 0.18f else 0.08f))
                                    .border(1.dp, medalColor.copy(alpha = 0.35f), CircleShape),
                                contentAlignment = Alignment.Center,
                            ) {
                                Text(
                                    medalEmoji,
                                    style = MaterialTheme.typography.titleMedium,
                                )
                            }

                            Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                                ) {
                                    Text(
                                        student.name,
                                        fontWeight = FontWeight.Bold,
                                        color = GlassWhite,
                                        style = MaterialTheme.typography.titleSmall,
                                    )
                                    Text(
                                        if (student.isFemale) "👧" else "👦",
                                        style = MaterialTheme.typography.bodySmall,
                                    )
                                }
                                Text(
                                    student.circleName.ifBlank { student.teacherName.ifBlank { "حلقة قرآنية" } },
                                    style = MaterialTheme.typography.bodySmall,
                                    color = GlassSilver,
                                )
                            }

                            if (rank <= 3) {
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(10.dp))
                                        .background(medalColor.copy(alpha = 0.18f))
                                        .padding(horizontal = 8.dp, vertical = 4.dp),
                                ) {
                                    Text(
                                        "متميز",
                                        color = medalColor,
                                        style = MaterialTheme.typography.labelSmall,
                                        fontWeight = FontWeight.Bold,
                                    )
                                }
                            }
                        }
                    }
                }
                item { Spacer(Modifier.height(80.dp)) }
            }
        }
    }
}

// ══════════════════════════════════════════════════════════════════════════
// ── بطاقة الحلقة ─────────────────────────────────────────────────────────
// ══════════════════════════════════════════════════════════════════════════

@Composable
private fun TeacherCard(
    teacher: AppUser,
    onSelect: () -> Unit,
    onEdit: () -> Unit,
    onDelete: () -> Unit,
) {
    val isFemale = teacher.isFemaleCircle
    val circleAccent = if (isFemale) Color(0xFFF472B6) else Color(0xFF60A5FA)

    OmranCard(
        onClick = onSelect,
        modifier = Modifier.fillMaxWidth(),
    ) {
        Row(
            Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(14.dp),
        ) {
            Box(
                Modifier
                    .size(50.dp)
                    .clip(CircleShape)
                    .background(circleAccent.copy(alpha = 0.12f))
                    .border(1.dp, circleAccent.copy(alpha = 0.35f), CircleShape),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    PhosphorIcons.Regular.BookOpen,
                    contentDescription = null,
                    tint = circleAccent,
                    modifier = Modifier.size(24.dp),
                )
            }

            Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    Text(
                        teacher.displayCircleName,
                        fontWeight = FontWeight.Bold,
                        color = GlassWhite,
                        style = MaterialTheme.typography.titleMedium,
                    )
                    // شارة جنس الحلقة
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(circleAccent.copy(alpha = 0.18f))
                            .border(1.dp, circleAccent.copy(alpha = 0.4f), RoundedCornerShape(8.dp))
                            .padding(horizontal = 6.dp, vertical = 2.dp),
                    ) {
                        Text(
                            if (isFemale) "👧 بنات" else "👦 بنين",
                            color = circleAccent,
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                        )
                    }
                }
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(5.dp),
                ) {
                    Icon(
                        PhosphorIcons.Regular.ChalkboardTeacher,
                        contentDescription = null,
                        tint = GlassMuted,
                        modifier = Modifier.size(15.dp),
                    )
                    Text(
                        "معلم الحلقة: ${teacher.name}",
                        style = MaterialTheme.typography.bodySmall,
                        color = GlassSilver,
                    )
                }
                Text(
                    teacher.email,
                    style = MaterialTheme.typography.labelSmall,
                    color = GlassMuted,
                )
            }

            IconButton(onClick = onEdit, modifier = Modifier.size(36.dp)) {
                Icon(
                    PhosphorIcons.Regular.PencilSimple,
                    contentDescription = "تعديل الحلقة والمعلم",
                    tint = Color(0xFF38BDF8),
                    modifier = Modifier.size(20.dp),
                )
            }

            IconButton(onClick = onDelete, modifier = Modifier.size(36.dp)) {
                Icon(
                    PhosphorIcons.Regular.Trash,
                    contentDescription = "حذف الحلقة",
                    tint = Color(0xFFF87171),
                    modifier = Modifier.size(20.dp),
                )
            }
        }
    }
}
