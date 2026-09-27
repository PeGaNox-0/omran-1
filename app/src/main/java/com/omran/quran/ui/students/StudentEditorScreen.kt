package com.omran.quran.ui.students

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDatePickerState
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.adamglin.PhosphorIcons
import com.adamglin.phosphoricons.Regular
import com.adamglin.phosphoricons.regular.ArrowRight
import com.adamglin.phosphoricons.regular.Calendar
import com.adamglin.phosphoricons.regular.Camera
import com.omran.quran.ui.components.DarkGlassBackground
import com.omran.quran.ui.components.DropdownField
import com.omran.quran.ui.components.ErrorBanner
import com.omran.quran.ui.components.OmranField
import com.omran.quran.ui.components.PrimaryButton
import com.omran.quran.ui.components.darkGlass
import com.omran.quran.ui.theme.GlassMuted
import com.omran.quran.ui.theme.GlassSilver
import com.omran.quran.ui.theme.GlassWhite
import com.omran.quran.util.toDateText

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StudentEditorScreen(
    onBack: () -> Unit,
    viewModel: StudentEditorViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val user by viewModel.user.collectAsStateWithLifecycle()
    var showDate by remember { mutableStateOf(false) }
    val picker = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        if (uri != null) viewModel.update { it.copy(localPhoto = uri) }
    }

    LaunchedEffect(Unit) {
        viewModel.saved.collect { onBack() }
    }

    DarkGlassBackground {
        Scaffold(
            containerColor = Color.Transparent,
            topBar = {
                TopAppBar(
                    title = { Text("بيانات الطالب", fontWeight = FontWeight.Bold, color = GlassWhite) },
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
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(14.dp),
            ) {
                ErrorBanner(state.error) { viewModel.update { it.copy(error = null) } }

                val photo = state.localPhoto ?: state.photoUrl.ifBlank { null }
                Box(
                    Modifier
                        .size(96.dp)
                        .clip(CircleShape)
                        .background(Color.White.copy(alpha = 0.08f))
                        .border(1.dp, Color.White.copy(alpha = 0.22f), CircleShape)
                        .clickable { picker.launch("image/*") },
                    contentAlignment = Alignment.Center,
                ) {
                    if (photo != null) {
                        AsyncImage(
                            model = photo,
                            contentDescription = null,
                            modifier = Modifier.fillMaxSize().clip(CircleShape),
                            contentScale = ContentScale.Crop,
                        )
                    } else {
                        Icon(PhosphorIcons.Regular.Camera, contentDescription = null, tint = GlassSilver, modifier = Modifier.size(32.dp))
                    }
                }
                Text(
                    "اضغط لتغيير الصورة الشخصية",
                    style = androidx.compose.material3.MaterialTheme.typography.labelMedium,
                    color = GlassMuted,
                )

                OmranField(state.name, { v -> viewModel.update { it.copy(name = v) } }, "الاسم الكامل")
                OmranField(state.age, { v -> viewModel.update { it.copy(age = v) } }, "العمر", keyboardType = KeyboardType.Number)
                OmranField(state.parentPhone, { v -> viewModel.update { it.copy(parentPhone = v) } }, "رقم ولي الأمر", keyboardType = KeyboardType.Phone)

                if (user?.isAdmin == true && state.teachers.isNotEmpty()) {
                    DropdownField(
                        label = "الحلقة القرآنية / المعلم المسؤول",
                        value = state.teachers.find { it.userId == state.teacherId }?.let { "${it.displayCircleName} (${it.name})" }
                            ?: state.circleName.ifBlank { state.teacherName },
                        options = state.teachers.map { "${it.displayCircleName} (${it.name})" },
                        onSelect = { option ->
                            val teacher = state.teachers.find { "${it.displayCircleName} (${it.name})" == option }
                            if (teacher != null) {
                                viewModel.update {
                                    it.copy(
                                        teacherId = teacher.userId,
                                        teacherName = teacher.name,
                                        circleName = teacher.displayCircleName,
                                        gender = teacher.circleGender,
                                    )
                                }
                            }
                        },
                    )
                }

                // اختيار تاريخ الالتحاق
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .darkGlass(shape = RoundedCornerShape(16.dp), backgroundColor = Color(0xFF151820).copy(alpha = 0.65f))
                        .clickable { showDate = true }
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        state.joinDate?.toDateText()?.let { "تاريخ الالتحاق: $it" } ?: "تاريخ الالتحاق (اختياري)",
                        color = if (state.joinDate != null) GlassWhite else GlassMuted,
                        fontWeight = if (state.joinDate != null) FontWeight.Bold else FontWeight.Normal,
                    )
                    Icon(PhosphorIcons.Regular.Calendar, contentDescription = null, tint = GlassSilver, modifier = Modifier.size(20.dp))
                }

                Spacer(Modifier.height(12.dp))
                PrimaryButton("حفظ بيانات الطالب", onClick = viewModel::save, loading = state.saving)
            }
        }

        if (showDate) {
            val dateState = rememberDatePickerState()
            DatePickerDialog(
                onDismissRequest = { showDate = false },
                confirmButton = {
                    TextButton(onClick = {
                        viewModel.update { it.copy(joinDate = dateState.selectedDateMillis) }
                        showDate = false
                    }) { Text("تم", color = GlassWhite, fontWeight = FontWeight.Bold) }
                },
                dismissButton = { TextButton(onClick = { showDate = false }) { Text("إلغاء", color = GlassMuted) } },
            ) { DatePicker(state = dateState) }
        }
    }
}
