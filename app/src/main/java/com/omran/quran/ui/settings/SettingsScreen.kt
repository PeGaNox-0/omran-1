package com.omran.quran.ui.settings

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.adamglin.PhosphorIcons
import com.adamglin.phosphoricons.Regular
import com.adamglin.phosphoricons.regular.ArrowRight
import com.adamglin.phosphoricons.regular.BookOpen
import com.adamglin.phosphoricons.regular.Calendar
import com.adamglin.phosphoricons.regular.ChalkboardTeacher
import com.adamglin.phosphoricons.regular.Check
import com.adamglin.phosphoricons.regular.Crown
import com.adamglin.phosphoricons.regular.Plus
import com.adamglin.phosphoricons.regular.Trash
import com.adamglin.phosphoricons.regular.UserPlus
import com.adamglin.phosphoricons.regular.Warning
import com.omran.quran.data.model.AppUser
import com.omran.quran.ui.components.DarkGlassBackground
import com.omran.quran.ui.components.EmptyState
import com.omran.quran.ui.components.ErrorBanner
import com.omran.quran.ui.components.OmranCard
import com.omran.quran.ui.components.OmranField
import com.omran.quran.ui.components.PrimaryButton
import com.omran.quran.ui.theme.GlassMuted
import com.omran.quran.ui.theme.GlassSilver
import com.omran.quran.ui.theme.GlassWhite
import com.omran.quran.util.WeekDay

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun SettingsScreen(
    onBack: () -> Unit,
    viewModel: SettingsViewModel = hiltViewModel(),
) {
    val context = LocalContext.current
    val user by viewModel.user.collectAsStateWithLifecycle()
    val state by viewModel.state.collectAsStateWithLifecycle()
    val dark by viewModel.darkTheme.collectAsStateWithLifecycle()

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
                    title = { Text("الإعدادات العامة", fontWeight = FontWeight.Bold, color = GlassWhite) },
                    navigationIcon = {
                        IconButton(onClick = onBack) {
                            Icon(PhosphorIcons.Regular.ArrowRight, contentDescription = "رجوع", tint = GlassWhite)
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
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp),
            ) {
                // 1. بطاقة المستخدم الحالية مع أيقونة المدير والمعلم
                OmranCard {
                    Row(
                        Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(14.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Box(
                            Modifier
                                .size(54.dp)
                                .clip(CircleShape)
                                .background(Color.White.copy(alpha = 0.08f))
                                .border(1.dp, Color.White.copy(alpha = 0.2f), CircleShape),
                            contentAlignment = Alignment.Center,
                        ) {
                            Icon(
                                if (user?.isAdmin == true) PhosphorIcons.Regular.Crown else PhosphorIcons.Regular.ChalkboardTeacher,
                                contentDescription = null,
                                tint = if (user?.isAdmin == true) Color(0xFFFBBF24) else GlassWhite,
                                modifier = Modifier.size(28.dp),
                            )
                        }
                        Column(Modifier.weight(1f)) {
                            Text(user?.name.orEmpty(), style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, color = GlassWhite)
                            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                                Icon(
                                    if (user?.isAdmin == true) PhosphorIcons.Regular.Crown else PhosphorIcons.Regular.ChalkboardTeacher,
                                    contentDescription = null,
                                    tint = if (user?.isAdmin == true) Color(0xFFFBBF24) else GlassSilver,
                                    modifier = Modifier.size(16.dp),
                                )
                                Text(if (user?.isAdmin == true) "مدير المعهد" else "معلم في المعهد", color = GlassSilver, fontWeight = FontWeight.SemiBold)
                            }
                            Text(user?.email.orEmpty(), style = MaterialTheme.typography.bodySmall, color = GlassMuted)
                            Text("معهد: ${user?.instituteName.orEmpty().ifBlank { "معهد المقرئ" }}", style = MaterialTheme.typography.bodySmall, color = GlassMuted)
                        }
                    }
                }

                // 2. أيام دوام المعلم الأسبوعية (خاص بالمعلمين فقط)
                if (user?.isAdmin != true) {
                    OmranCard {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(PhosphorIcons.Regular.Calendar, contentDescription = null, tint = GlassWhite, modifier = Modifier.size(20.dp))
                            Spacer(Modifier.width(8.dp))
                            Text("أيام دوامك الأسبوعية", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = GlassWhite)
                        }
                        Spacer(Modifier.height(4.dp))
                        Text(
                            "حدد الأيام الفعلية لدوامك. سيتم اعتبار باقي الأيام عطلة في شاشة التحضير:",
                            style = MaterialTheme.typography.bodySmall,
                            color = GlassMuted,
                        )
                        Spacer(Modifier.height(8.dp))

                        FlowRow(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalArrangement = Arrangement.spacedBy(6.dp),
                        ) {
                            WeekDay.entries.forEach { day ->
                                val isSelected = state.workingDays.contains(day.id)
                                FilterChip(
                                    selected = isSelected,
                                    onClick = { viewModel.toggleWorkingDay(day.id) },
                                    label = { Text(day.arabicName, color = if (isSelected) Color.Black else GlassSilver, fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal) },
                                    leadingIcon = if (isSelected) {
                                        { Icon(PhosphorIcons.Regular.Check, contentDescription = null, Modifier.size(16.dp), tint = Color.Black) }
                                    } else null,
                                    colors = FilterChipDefaults.filterChipColors(
                                        selectedContainerColor = GlassWhite,
                                        containerColor = Color(0xFF161922).copy(alpha = 0.6f),
                                    ),
                                    border = FilterChipDefaults.filterChipBorder(enabled = true, selected = isSelected, borderColor = if (isSelected) GlassWhite else Color.White.copy(alpha = 0.15f)),
                                    shape = RoundedCornerShape(10.dp),
                                )
                            }
                        }
                    }
                }

                // 3. إدارة الحلقات (خاص بالمدير فقط)
                if (user?.isAdmin == true) {
                    OmranCard {
                        Row(
                            Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Text("إدارة الحلقات (${state.teachers.size})", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = GlassWhite)
                            Button(
                                onClick = { viewModel.openAddTeacherDialog() },
                                colors = ButtonDefaults.buttonColors(containerColor = GlassWhite, contentColor = Color.Black),
                                shape = RoundedCornerShape(12.dp),
                            ) {
                                Icon(PhosphorIcons.Regular.Plus, contentDescription = null, Modifier.size(18.dp))
                                Spacer(Modifier.width(6.dp))
                                Text("إنشاء حلقة", fontWeight = FontWeight.Bold)
                            }
                        }
                        Spacer(Modifier.height(10.dp))

                        if (state.teachers.isEmpty()) {
                            EmptyState("لا توجد حلقات قرآنية مسجلة في المعهد بعد")
                        } else {
                            state.teachers.forEach { teacher ->
                                Box(
                                    Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 4.dp)
                                        .background(Color.White.copy(alpha = 0.05f), RoundedCornerShape(12.dp))
                                        .padding(horizontal = 12.dp, vertical = 8.dp),
                                ) {
                                    Row(
                                        Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically,
                                    ) {
                                        Column(Modifier.weight(1f)) {
                                             Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                                 val isFemale = teacher.isFemaleCircle
                                                 val badgeColor = if (isFemale) Color(0xFFF472B6) else Color(0xFF60A5FA)
                                                 Icon(PhosphorIcons.Regular.BookOpen, contentDescription = null, tint = badgeColor, modifier = Modifier.size(16.dp))
                                                 Text(teacher.displayCircleName, fontWeight = FontWeight.Bold, color = GlassWhite)
                                                 Box(
                                                     modifier = Modifier
                                                         .clip(RoundedCornerShape(6.dp))
                                                         .background(badgeColor.copy(alpha = 0.18f))
                                                         .padding(horizontal = 6.dp, vertical = 2.dp),
                                                 ) {
                                                     Text(
                                                         if (isFemale) "👧 بنات" else "👦 بنين",
                                                         color = badgeColor,
                                                         style = MaterialTheme.typography.labelSmall,
                                                         fontWeight = FontWeight.Bold,
                                                     )
                                                 }
                                             }
                                             Text("المعلم: ${teacher.name} • البريد: ${teacher.email}", style = MaterialTheme.typography.bodySmall, color = GlassMuted)
                                        }
                                        IconButton(onClick = { teacherToDelete = teacher }) {
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
                        }
                    }
                }

                // 4. خيار المظهر
                OmranCard {
                    Row(
                        Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Column(Modifier.weight(1f)) {
                            Text("الوضع الليلي الفاخر", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = GlassWhite)
                            Text("تصميم زجاجي داكن مريح للعينين", style = MaterialTheme.typography.bodySmall, color = GlassMuted)
                        }
                        Switch(
                            checked = dark == true,
                            onCheckedChange = viewModel::setDark,
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = GlassWhite,
                                checkedTrackColor = Color(0xFF2A303C),
                            ),
                        )
                    }
                }

                Spacer(Modifier.height(10.dp))
                PrimaryButton("تسجيل الخروج", onClick = viewModel::signOut)
                Spacer(Modifier.height(24.dp))
            }
        }

        // نافذة إنشاء حلقة جديدة
        if (state.isAddTeacherOpen) {
            AlertDialog(
                onDismissRequest = { viewModel.closeAddTeacherDialog() },
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(PhosphorIcons.Regular.BookOpen, contentDescription = null, tint = GlassWhite, modifier = Modifier.size(22.dp))
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
                        ErrorBanner(state.error) { viewModel.updateTeacherForm() }
                        OmranField(
                            value = state.circleName,
                            onValueChange = { viewModel.updateTeacherForm(circle = it) },
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
                                    .clickable { viewModel.updateTeacherForm(circleGender = "MALE") }
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
                                    .clickable { viewModel.updateTeacherForm(circleGender = "FEMALE") }
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
                        onClick = { viewModel.saveTeacher() },
                        loading = state.savingTeacher,
                    )
                },
                dismissButton = {
                    TextButton(onClick = { viewModel.closeAddTeacherDialog() }) {
                        Text("إلغاء", color = GlassMuted)
                    }
                },
            )
        }

        // نافذة تأكيد حذف الحلقة
        teacherToDelete?.let { teacher ->
            AlertDialog(
                onDismissRequest = { teacherToDelete = null },
                icon = { Icon(PhosphorIcons.Regular.Warning, contentDescription = null, tint = Color(0xFFF87171), modifier = Modifier.size(28.dp)) },
                title = { Text("تأكيد حذف الحلقة", fontWeight = FontWeight.Bold, color = GlassWhite) },
                text = { Text("هل أنت متأكد من رغبتك في حذف «${teacher.displayCircleName}» (المعلم: ${teacher.name}) من المعهد؟", color = GlassSilver) },
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
