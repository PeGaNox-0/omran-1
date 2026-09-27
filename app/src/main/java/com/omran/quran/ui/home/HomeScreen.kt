package com.omran.quran.ui.home

import androidx.compose.foundation.background
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.adamglin.PhosphorIcons
import com.adamglin.phosphoricons.Regular
import com.adamglin.phosphoricons.regular.ArrowsClockwise
import com.adamglin.phosphoricons.regular.BookOpen
import com.adamglin.phosphoricons.regular.CalendarCheck
import com.adamglin.phosphoricons.regular.ChalkboardTeacher
import com.adamglin.phosphoricons.regular.Crown
import com.adamglin.phosphoricons.regular.Exam
import com.adamglin.phosphoricons.regular.Gear
import com.adamglin.phosphoricons.regular.Plus
import com.adamglin.phosphoricons.regular.UserMinus
import com.adamglin.phosphoricons.regular.UserPlus
import com.adamglin.phosphoricons.regular.Users
import com.omran.quran.ui.components.DarkGlassBackground
import com.omran.quran.ui.components.HijriDateBanner
import com.omran.quran.ui.components.LoadingBox
import com.omran.quran.ui.components.OmranCard
import com.omran.quran.ui.components.StatCard
import com.omran.quran.ui.components.darkGlass
import com.omran.quran.ui.records.RecordType
import com.omran.quran.ui.theme.GlassMuted
import com.omran.quran.ui.theme.GlassSilver
import com.omran.quran.ui.theme.GlassWhite

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onSettings: () -> Unit,
    onAddStudent: () -> Unit,
    onAddRecord: (RecordType) -> Unit,
    onAttendance: () -> Unit,
    viewModel: HomeViewModel = hiltViewModel(),
) {
    val user by viewModel.user.collectAsStateWithLifecycle()
    val state by viewModel.state.collectAsStateWithLifecycle()
    val isOnline by viewModel.isOnline.collectAsStateWithLifecycle()
    val isSyncing by viewModel.isSyncing.collectAsStateWithLifecycle()
    var sheet by remember { mutableStateOf(false) }
    val stats = state.stats

    DarkGlassBackground {
        Scaffold(
            containerColor = Color.Transparent,
            topBar = {
                TopAppBar(
                    title = {
                        Column {
                            Text("السلام عليكم ورحمة الله", style = MaterialTheme.typography.bodySmall, color = GlassMuted)
                            Text(user?.name.orEmpty().ifBlank { "معهد المقرئ" }, fontWeight = FontWeight.Bold, color = GlassWhite)
                        }
                    },
                    actions = {
                        IconButton(onClick = onSettings) {
                            Box(
                                Modifier
                                    .size(40.dp)
                                    .clip(CircleShape)
                                    .background(Color.White.copy(alpha = 0.08f)),
                                contentAlignment = Alignment.Center,
                            ) {
                                Icon(PhosphorIcons.Regular.Gear, contentDescription = "الإعدادات", tint = GlassWhite, modifier = Modifier.size(22.dp))
                            }
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent),
                )
            },
            floatingActionButton = {
                Box(
                    modifier = Modifier
                        .padding(bottom = 8.dp)
                        .darkGlass(
                            shape = RoundedCornerShape(22.dp),
                            backgroundColor = Color(0xFF222734).copy(alpha = 0.95f),
                            borderWidth = 1.dp,
                        )
                        .clickable { sheet = true }
                        .padding(horizontal = 18.dp, vertical = 12.dp),
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                    ) {
                        Icon(PhosphorIcons.Regular.Plus, contentDescription = null, tint = GlassWhite, modifier = Modifier.size(20.dp))
                        Text("تسجيل جديد", fontWeight = FontWeight.Bold, color = GlassWhite)
                    }
                }
            },
        ) { padding ->
            if (state.loading && stats.studentCount == 0) {
                LoadingBox(Modifier.padding(padding))
            } else {
                Column(
                    Modifier
                        .fillMaxSize()
                        .padding(padding)
                        .verticalScroll(rememberScrollState())
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp),
                ) {
                    // شريط حالة المزامنة والاتصال
                    if (!isOnline) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(12.dp))
                                .background(Color(0xFFB45309).copy(alpha = 0.25f))
                                .padding(horizontal = 12.dp, vertical = 8.dp),
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                            ) {
                                Icon(
                                    PhosphorIcons.Regular.ArrowsClockwise,
                                    contentDescription = null,
                                    tint = Color(0xFFFBBF24),
                                    modifier = Modifier.size(16.dp),
                                )
                                Text(
                                    text = "وضع عدم الاتصال — التطبيق يعمل محلياً وسيتم المزامنة تلقائياً عند عودة الإنترنت",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = Color(0xFFFDE68A),
                                )
                            }
                        }
                    } else if (isSyncing) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(12.dp))
                                .background(Color(0xFF0284C7).copy(alpha = 0.25f))
                                .padding(horizontal = 12.dp, vertical = 8.dp),
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                            ) {
                                Icon(
                                    PhosphorIcons.Regular.ArrowsClockwise,
                                    contentDescription = null,
                                    tint = Color(0xFF38BDF8),
                                    modifier = Modifier.size(16.dp),
                                )
                                Text(
                                    text = "جاري مزامنة التحديثات مع السحابة...",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = Color(0xFFBAE6FD),
                                )
                            }
                        }
                    }

                    // شريط التاريخ الهجري
                    HijriDateBanner(modifier = Modifier.padding(bottom = 2.dp))

                    // شارة اسم المعهد ونوع الحساب
                    OmranCard {
                        Row(
                            Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Column {
                                Text("المعهد الحالي", style = MaterialTheme.typography.labelSmall, color = GlassMuted)
                                Text(
                                    user?.instituteName?.ifBlank { "معهد المقرئ" } ?: "معهد المقرئ",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = GlassWhite,
                                )
                            }
                            Box(
                                Modifier
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(Color.White.copy(alpha = 0.1f))
                                    .padding(horizontal = 10.dp, vertical = 6.dp),
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                                ) {
                                    Icon(
                                        if (user?.isAdmin == true) PhosphorIcons.Regular.Crown else PhosphorIcons.Regular.ChalkboardTeacher,
                                        contentDescription = null,
                                        tint = if (user?.isAdmin == true) Color(0xFFFBBF24) else GlassWhite,
                                        modifier = Modifier.size(16.dp),
                                    )
                                    Text(
                                        if (user?.isAdmin == true) "إدارة المعهد" else "معلم حلقة",
                                        color = GlassWhite,
                                        fontWeight = FontWeight.SemiBold,
                                        style = MaterialTheme.typography.bodySmall,
                                    )
                                }
                            }
                        }
                    }

                    Text("إحصائيات المعهد واليوم", style = MaterialTheme.typography.titleSmall, color = GlassMuted, fontWeight = FontWeight.Bold)

                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        StatCard("الطلاب", stats.studentCount.toString(), PhosphorIcons.Regular.Users, Modifier.weight(1f), accent = true)
                        StatCard("حضور اليوم", stats.presentToday.toString(), PhosphorIcons.Regular.CalendarCheck, Modifier.weight(1f))
                    }
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        StatCard("غياب اليوم", stats.absentToday.toString(), PhosphorIcons.Regular.UserMinus, Modifier.weight(1f))
                        StatCard("الحفظ اليوم", stats.memorizationToday.toString(), PhosphorIcons.Regular.BookOpen, Modifier.weight(1f))
                    }
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        StatCard("السبر اليوم", stats.examsToday.toString(), PhosphorIcons.Regular.Exam, Modifier.weight(1f))
                        StatCard("المراجعة اليوم", stats.revisionsToday.toString(), PhosphorIcons.Regular.ArrowsClockwise, Modifier.weight(1f))
                    }
                    Spacer(Modifier.height(88.dp))
                }
            }
        }

        if (sheet) {
            ModalBottomSheet(
                onDismissRequest = { sheet = false },
                sheetState = rememberModalBottomSheetState(),
                containerColor = Color(0xFF141720),
                dragHandle = {
                    Box(
                        Modifier
                            .padding(vertical = 10.dp)
                            .size(width = 40.dp, height = 4.dp)
                            .clip(CircleShape)
                            .background(Color.White.copy(alpha = 0.25f)),
                    )
                },
            ) {
                Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text("ماذا تريد تسجيله؟", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, color = GlassWhite)
                    SheetActionItem("طالب جديد", PhosphorIcons.Regular.UserPlus) { sheet = false; onAddStudent() }
                    SheetActionItem("حفظ جديد", PhosphorIcons.Regular.BookOpen) { sheet = false; onAddRecord(RecordType.MEMORIZATION) }
                    SheetActionItem("سبر جديد", PhosphorIcons.Regular.Exam) { sheet = false; onAddRecord(RecordType.EXAM) }
                    SheetActionItem("مراجعة جديدة", PhosphorIcons.Regular.ArrowsClockwise) { sheet = false; onAddRecord(RecordType.REVISION) }
                    SheetActionItem("حضور اليوم", PhosphorIcons.Regular.CalendarCheck) { sheet = false; onAttendance() }
                    TextButton(onClick = { sheet = false }, modifier = Modifier.fillMaxWidth()) { Text("إلغاء", color = GlassMuted) }
                }
            }
        }
    }
}

@Composable
private fun SheetActionItem(
    title: String,
    icon: ImageVector,
    onClick: () -> Unit,
) {
    OmranCard(onClick = onClick) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.fillMaxWidth(),
        ) {
            Box(
                Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(Color.White.copy(alpha = 0.08f)),
                contentAlignment = Alignment.Center,
            ) {
                Icon(icon, contentDescription = null, tint = GlassWhite, modifier = Modifier.size(18.dp))
            }
            Text(title, color = GlassWhite, fontWeight = FontWeight.SemiBold, style = MaterialTheme.typography.titleMedium)
        }
    }
}
