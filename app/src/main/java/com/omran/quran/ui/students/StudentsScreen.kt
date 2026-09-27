package com.omran.quran.ui.students

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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.foundation.background
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.adamglin.PhosphorIcons
import com.adamglin.phosphoricons.Regular
import com.adamglin.phosphoricons.regular.ArrowsClockwise
import com.adamglin.phosphoricons.regular.UserPlus
import com.omran.quran.data.model.Student
import com.omran.quran.ui.components.DarkGlassBackground
import com.omran.quran.ui.components.EmptyState
import com.omran.quran.ui.components.LoadingBox
import com.omran.quran.ui.components.OmranCard
import com.omran.quran.ui.components.SearchField
import com.omran.quran.ui.components.listBottomPadding
import com.omran.quran.ui.theme.GlassMuted
import com.omran.quran.ui.theme.GlassSilver
import com.omran.quran.ui.theme.GlassWhite

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StudentsScreen(
    onStudent: (String) -> Unit,
    onAdd: () -> Unit,
    viewModel: StudentsViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    DarkGlassBackground {
        Scaffold(
            containerColor = Color.Transparent,
            topBar = {
                TopAppBar(
                    title = { Text("قائمة الطلاب", fontWeight = FontWeight.Bold, color = GlassWhite) },
                    actions = {
                        IconButton(onClick = viewModel::forceRefresh) {
                            Box(
                                Modifier
                                    .size(38.dp)
                                    .clip(CircleShape)
                                    .background(Color.White.copy(alpha = 0.08f)),
                                contentAlignment = Alignment.Center,
                            ) {
                                Icon(
                                    PhosphorIcons.Regular.ArrowsClockwise,
                                    contentDescription = "تحديث ومزامنة الطلاب",
                                    tint = GlassWhite,
                                    modifier = Modifier.size(20.dp),
                                )
                            }
                        }
                    },
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
                    Icon(PhosphorIcons.Regular.UserPlus, contentDescription = "إضافة طالب", modifier = Modifier.size(24.dp))
                }
            },
        ) { padding ->
            Column(
                Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(horizontal = 16.dp),
            ) {
                SearchField(state.query, viewModel::onQuery)
                Spacer(Modifier.height(10.dp))
                when {
                    state.loading && state.items.isEmpty() -> LoadingBox()
                    state.items.isEmpty() -> EmptyState("لا يوجد طلاب مسجلون بعد")
                    else -> LazyColumn(
                        contentPadding = listBottomPadding(),
                        verticalArrangement = Arrangement.spacedBy(10.dp),
                    ) {
                        itemsIndexed(state.items, key = { _, item -> item.studentId }) { index, student ->
                            if (index == state.items.lastIndex && !state.endReached && !state.loadingMore) {
                                androidx.compose.runtime.LaunchedEffect(state.items.size) { viewModel.loadMore() }
                            }
                            StudentRow(student) { onStudent(student.studentId) }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun StudentRow(student: Student, onClick: () -> Unit) {
    OmranCard(onClick = onClick, modifier = Modifier.fillMaxWidth()) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(14.dp),
            modifier = Modifier.fillMaxWidth(),
        ) {
            Box(
                Modifier
                    .size(54.dp)
                    .clip(CircleShape)
                    .background(Color.White.copy(alpha = 0.08f))
                    .border(1.dp, Color.White.copy(alpha = 0.2f), CircleShape),
                contentAlignment = Alignment.Center,
            ) {
                AsyncImage(
                    model = student.photoUrl.ifBlank { null },
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize().clip(CircleShape),
                    contentScale = ContentScale.Crop,
                )
            }
            Column(
                Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(3.dp),
            ) {
                Text(student.name, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = GlassWhite, maxLines = 1)
                Text(
                    buildString {
                        if (student.age > 0) append("${student.age} سنة  •  ")
                        if (student.teacherName.isNotBlank()) append("المعلم: ${student.teacherName}")
                    }.ifBlank { "طالب في المعهد" },
                    style = MaterialTheme.typography.bodySmall,
                    color = GlassSilver,
                    maxLines = 1,
                )
                if (student.parentPhone.isNotBlank()) {
                    Text(
                        "ولي الأمر: ${student.parentPhone}",
                        style = MaterialTheme.typography.labelSmall,
                        color = GlassMuted,
                        maxLines = 1,
                    )
                }
            }
        }
    }
}
