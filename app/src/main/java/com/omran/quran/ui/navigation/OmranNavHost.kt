package com.omran.quran.ui.navigation

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.adamglin.PhosphorIcons
import com.adamglin.phosphoricons.Regular
import com.adamglin.phosphoricons.regular.ArrowRight
import com.adamglin.phosphoricons.regular.ArrowsClockwise
import com.adamglin.phosphoricons.regular.BookOpen
import com.adamglin.phosphoricons.regular.CalendarCheck
import com.adamglin.phosphoricons.regular.ChalkboardTeacher
import com.adamglin.phosphoricons.regular.Exam
import com.adamglin.phosphoricons.regular.House
import com.adamglin.phosphoricons.regular.Users
import com.omran.quran.ui.admin.AdminTeachersScreen
import com.omran.quran.ui.admin.AdminViewModel
import com.omran.quran.ui.attendance.AttendanceScreen
import com.omran.quran.ui.auth.LoginScreen
import com.omran.quran.ui.auth.RegisterScreen
import com.omran.quran.ui.components.DarkGlassBackground
import com.omran.quran.ui.components.GlassBottomBar
import com.omran.quran.ui.home.HomeScreen
import com.omran.quran.ui.records.ExamListScreen
import com.omran.quran.ui.records.MemorizationListScreen
import com.omran.quran.ui.records.RecordFormScreen
import com.omran.quran.ui.records.RecordType
import com.omran.quran.ui.records.RevisionListScreen
import com.omran.quran.ui.settings.SettingsScreen
import com.omran.quran.ui.students.StudentEditorScreen
import com.omran.quran.ui.students.StudentProfileScreen
import com.omran.quran.ui.students.StudentsScreen
import com.omran.quran.ui.theme.GlassMuted
import com.omran.quran.ui.theme.GlassSilver
import com.omran.quran.ui.theme.GlassWhite

object Routes {
    const val LOGIN = "login"
    const val REGISTER = "register"
    const val MAIN = "main"
    const val SETTINGS = "settings"
    const val PROFILE = "profile/{studentId}"
    const val STUDENT_EDIT = "student/edit/{studentId}"
    const val RECORD = "record/{type}?studentId={studentId}&recordId={recordId}"

    fun profile(id: String) = "profile/$id"
    fun editStudent(id: String = "new") = "student/edit/$id"
    fun record(type: RecordType, studentId: String = "", recordId: String = "") =
        "record/${type.name}?studentId=$studentId&recordId=$recordId"
}

private enum class MainTab(val label: String, val icon: ImageVector) {
    HOME("الرئيسية", PhosphorIcons.Regular.House),
    STUDENTS("الطلاب", PhosphorIcons.Regular.Users),
    MEM("الحفظ", PhosphorIcons.Regular.BookOpen),
    EXAM("السبر", PhosphorIcons.Regular.Exam),
    REV("المراجعة", PhosphorIcons.Regular.ArrowsClockwise),
    ATT("الحضور", PhosphorIcons.Regular.CalendarCheck),
}

@Composable
fun OmranNavHost(
    startDestination: String,
    navController: NavHostController = rememberNavController(),
) {
    DarkGlassBackground {
        NavHost(navController = navController, startDestination = startDestination) {
            composable(Routes.LOGIN) {
                LoginScreen(onRegister = { navController.navigate(Routes.REGISTER) })
            }
            composable(Routes.REGISTER) {
                RegisterScreen(onBack = { navController.popBackStack() })
            }
            composable(Routes.MAIN) {
                MainEntry(
                    onSettings = { navController.navigate(Routes.SETTINGS) },
                    onStudent = { navController.navigate(Routes.profile(it)) },
                    onAddStudent = { navController.navigate(Routes.editStudent()) },
                    onRecord = { type, studentId, recordId ->
                        navController.navigate(Routes.record(type, studentId, recordId))
                    },
                )
            }
            composable(Routes.SETTINGS) {
                SettingsScreen(onBack = { navController.popBackStack() })
            }
            composable(
                Routes.PROFILE,
                arguments = listOf(navArgument("studentId") { type = NavType.StringType }),
            ) { entry ->
                val id = entry.arguments?.getString("studentId").orEmpty()
                StudentProfileScreen(
                    onBack = { navController.popBackStack() },
                    onEditStudent = { navController.navigate(Routes.editStudent(it)) },
                    onAddRecord = { navController.navigate(Routes.record(it, id)) },
                    onEditRecord = { type, studentId, recordId -> navController.navigate(Routes.record(type, studentId, recordId)) },
                )
            }
            composable(
                Routes.STUDENT_EDIT,
                arguments = listOf(navArgument("studentId") { type = NavType.StringType }),
            ) {
                StudentEditorScreen(onBack = { navController.popBackStack() })
            }
            composable(
                Routes.RECORD,
                arguments = listOf(
                    navArgument("type") { type = NavType.StringType },
                    navArgument("studentId") { type = NavType.StringType; defaultValue = "" },
                    navArgument("recordId") { type = NavType.StringType; defaultValue = "" },
                ),
            ) {
                RecordFormScreen(onBack = { navController.popBackStack() })
            }
        }
    }
}


/**
 * نقطة دخول رئيسية — توجّه حسب الدور:
 * - مدير بلا معلم مختار  → AdminTeachersScreen
 * - مدير اختار معلماً   → MainTabs مع بانر المعلم وزر رجوع
 * - معلم                → MainTabs عادية
 */
@Composable
private fun MainEntry(
    onSettings: () -> Unit,
    onStudent: (String) -> Unit,
    onAddStudent: () -> Unit,
    onRecord: (RecordType, String, String) -> Unit,
    adminViewModel: AdminViewModel = hiltViewModel(),
) {
    val user by adminViewModel.user.collectAsStateWithLifecycle()
    val selectedTeacher by adminViewModel.selectedTeacher.collectAsStateWithLifecycle()

    when {
        // مدير + لم يختر معلماً بعد → شاشة قائمة المعلمين
        user?.isAdmin == true && selectedTeacher == null -> {
            AdminTeachersScreen(
                onSelectTeacher = { /* teacher already stored in AdminSession */ },
                onSettings = onSettings,
            )
        }
        // مدير + اختار معلماً → tabs بسياق المعلم المختار
        user?.isAdmin == true && selectedTeacher != null -> {
            Column(Modifier.fillMaxSize()) {
                // بانر: اسم المعلم المختار + زر رجوع (يراعي شريط الحالة / notch في الجوال)
                Box(
                    Modifier
                        .fillMaxWidth()
                        .background(Color(0xFF1A1F2E))
                        .statusBarsPadding()
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                ) {
                    Row(
                        Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                    ) {
                        // زر رجوع لقائمة المعلمين
                        Box(
                            Modifier
                                .clip(CircleShape)
                                .background(Color.White.copy(alpha = 0.08f))
                                .clickable(
                                    interactionSource = remember { MutableInteractionSource() },
                                    indication = null,
                                ) { adminViewModel.clearSelectedTeacher() }
                                .padding(8.dp),
                        ) {
                            Icon(
                                PhosphorIcons.Regular.ArrowRight,
                                contentDescription = "رجوع لقائمة المعلمين",
                                tint = GlassWhite,
                                modifier = Modifier.size(20.dp),
                            )
                        }
                        Icon(
                            PhosphorIcons.Regular.BookOpen,
                            contentDescription = null,
                            tint = GlassSilver,
                            modifier = Modifier.size(18.dp),
                        )
                        Column(modifier = Modifier.weight(1f, fill = false)) {
                            Text(
                                selectedTeacher?.displayCircleName.orEmpty(),
                                color = GlassWhite,
                                fontWeight = FontWeight.Bold,
                                style = MaterialTheme.typography.titleSmall,
                            )
                            Text(
                                "المعلم: ${selectedTeacher?.name.orEmpty()}",
                                color = GlassSilver,
                                style = MaterialTheme.typography.bodySmall,
                            )
                        }
                        Spacer(Modifier.weight(1f))
                        // زر تحديث ومزامنة بيانات الحلقة والطلاب
                        Box(
                            Modifier
                                .clip(CircleShape)
                                .background(Color.White.copy(alpha = 0.08f))
                                .clickable(
                                    interactionSource = remember { MutableInteractionSource() },
                                    indication = null,
                                ) { adminViewModel.refreshAll() }
                                .padding(8.dp),
                        ) {
                            Icon(
                                PhosphorIcons.Regular.ArrowsClockwise,
                                contentDescription = "تحديث بيانات الحلقة والطلاب",
                                tint = GlassWhite,
                                modifier = Modifier.size(18.dp),
                            )
                        }
                    }
                }
                HorizontalDivider(color = Color.White.copy(alpha = 0.08f), thickness = 1.dp)
                // Tabs عادية لكن بسياق المعلم المختار مع استهلاك شريط الحالة
                Box(
                    Modifier
                        .weight(1f)
                        .consumeWindowInsets(WindowInsets.statusBars)
                ) {
                    MainTabs(
                        onSettings = onSettings,
                        onStudent = onStudent,
                        onAddStudent = onAddStudent,
                        onRecord = onRecord,
                    )
                }
            }
        }
        // معلم → Tabs عادية بلا تغيير
        else -> {
            MainTabs(
                onSettings = onSettings,
                onStudent = onStudent,
                onAddStudent = onAddStudent,
                onRecord = onRecord,
            )
        }
    }
}

@Composable
private fun MainTabs(
    onSettings: () -> Unit,
    onStudent: (String) -> Unit,
    onAddStudent: () -> Unit,
    onRecord: (RecordType, String, String) -> Unit,
) {
    var tab by rememberSaveable { mutableStateOf(MainTab.HOME) }
    Scaffold(
        containerColor = Color.Transparent,
        bottomBar = {
            GlassBottomBar {
                MainTab.entries.forEach { item ->
                    val isSelected = tab == item
                    val iconColor by animateColorAsState(if (isSelected) GlassWhite else GlassMuted, label = "iconColor")
                    val bgColor by animateColorAsState(if (isSelected) Color.White.copy(alpha = 0.15f) else Color.Transparent, label = "bgColor")

                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center,
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(16.dp))
                            .background(bgColor)
                            .clickable(
                                interactionSource = remember { MutableInteractionSource() },
                                indication = null,
                            ) { tab = item }
                            .padding(vertical = 6.dp),
                    ) {
                        Icon(
                            item.icon,
                            contentDescription = item.label,
                            tint = iconColor,
                            modifier = Modifier.size(20.dp),
                        )
                        androidx.compose.foundation.layout.Spacer(Modifier.size(2.dp))
                        Text(
                            item.label,
                            fontSize = 10.5.sp,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            color = iconColor,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                        )
                    }
                }
            }
        },
    ) { padding ->
        Box(
            Modifier
                .fillMaxSize()
                .padding(bottom = padding.calculateBottomPadding()),
        ) {
            when (tab) {
                MainTab.HOME -> HomeScreen(
                    onSettings = onSettings,
                    onAddStudent = onAddStudent,
                    onAddRecord = { onRecord(it, "", "") },
                    onAttendance = { tab = MainTab.ATT },
                )
                MainTab.STUDENTS -> StudentsScreen(onStudent = onStudent, onAdd = onAddStudent)
                MainTab.MEM -> MemorizationListScreen(
                    onAdd = { onRecord(RecordType.MEMORIZATION, "", "") },
                    onEdit = { sId, rId -> onRecord(RecordType.MEMORIZATION, sId, rId) },
                )
                MainTab.EXAM -> ExamListScreen(
                    onAdd = { onRecord(RecordType.EXAM, "", "") },
                    onEdit = { sId, rId -> onRecord(RecordType.EXAM, sId, rId) },
                )
                MainTab.REV -> RevisionListScreen(
                    onAdd = { onRecord(RecordType.REVISION, "", "") },
                    onEdit = { sId, rId -> onRecord(RecordType.REVISION, sId, rId) },
                )
                MainTab.ATT -> AttendanceScreen()
            }
        }
    }
}
