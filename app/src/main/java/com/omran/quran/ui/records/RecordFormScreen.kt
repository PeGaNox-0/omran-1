package com.omran.quran.ui.records

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.adamglin.PhosphorIcons
import com.adamglin.phosphoricons.Regular
import com.adamglin.phosphoricons.regular.ArrowRight
import com.adamglin.phosphoricons.regular.Check
import com.adamglin.phosphoricons.regular.Minus
import com.adamglin.phosphoricons.regular.Plus
import com.omran.quran.ui.components.ChoiceRow
import com.omran.quran.ui.components.DarkGlassBackground
import com.omran.quran.ui.components.DropdownField
import com.omran.quran.ui.components.ErrorBanner
import com.omran.quran.ui.components.OmranCard
import com.omran.quran.ui.components.OmranField
import com.omran.quran.ui.components.PrimaryButton
import com.omran.quran.ui.components.SearchField
import com.omran.quran.ui.theme.GlassMuted
import com.omran.quran.ui.theme.GlassSilver
import com.omran.quran.ui.theme.GlassWhite
import com.omran.quran.util.QuranData
import com.omran.quran.util.Ratings

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecordFormScreen(
    onBack: () -> Unit,
    viewModel: RecordFormViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val isEdit = !viewModel.recordId.isNullOrBlank()
    val title = when (viewModel.type) {
        RecordType.MEMORIZATION -> if (isEdit) "تعديل سجل الحفظ ✏️" else "تسجيل حفظ جديد"
        RecordType.EXAM -> if (isEdit) "تعديل اختبار السبر ✏️" else "تسجيل سبر جديد"
        RecordType.REVISION -> if (isEdit) "تعديل سجل المراجعة ✏️" else "تسجيل مراجعة جديدة"
    }

    LaunchedEffect(Unit) { viewModel.saved.collect { onBack() } }

    DarkGlassBackground {
        Scaffold(
            containerColor = Color.Transparent,
            topBar = {
                TopAppBar(
                    title = { Text(title, fontWeight = FontWeight.Bold, color = GlassWhite) },
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
                ErrorBanner(state.error) { viewModel.update { it.copy(error = null) } }

                // 1. اختيار الطالب
                DropdownField(
                    label = "اختر الطالب",
                    value = state.studentName,
                    options = state.students.map { it.name },
                    onSelect = { name ->
                        val student = state.students.find { it.name == name }
                        viewModel.update { it.copy(studentId = student?.studentId.orEmpty(), studentName = name) }
                    },
                )

                // 2. اختيار النمط (مفرد أو متعدد)
                if (!isEdit) {
                    SingleChoiceSegmentedButtonRow(Modifier.fillMaxWidth()) {
                        SegmentedButton(
                            selected = !state.isMultiSelect,
                            onClick = { viewModel.toggleMultiSelect(false) },
                            shape = SegmentedButtonDefaults.itemShape(index = 0, count = 2),
                            colors = SegmentedButtonDefaults.colors(
                                activeContainerColor = GlassWhite,
                                activeContentColor = Color.Black,
                                inactiveContainerColor = Color(0xFF161922).copy(alpha = 0.6f),
                                inactiveContentColor = GlassSilver,
                            ),
                        ) {
                            Text("عنصر مفرد 📄", fontWeight = if (!state.isMultiSelect) FontWeight.Bold else FontWeight.Normal)
                        }
                        SegmentedButton(
                            selected = state.isMultiSelect,
                            onClick = { viewModel.toggleMultiSelect(true) },
                            shape = SegmentedButtonDefaults.itemShape(index = 1, count = 2),
                            colors = SegmentedButtonDefaults.colors(
                                activeContainerColor = Color(0xFF10B981),
                                activeContentColor = Color.Black,
                                inactiveContainerColor = Color(0xFF161922).copy(alpha = 0.6f),
                                inactiveContentColor = GlassSilver,
                            ),
                        ) {
                            Text("تحديد متعدد (عدة سور/أجزاء) 📚", fontWeight = if (state.isMultiSelect) FontWeight.Bold else FontWeight.Normal)
                        }
                    }
                }

                // 3. نمط الإدخال (بالسورة / بالجزء الكامل)
                SingleChoiceSegmentedButtonRow(Modifier.fillMaxWidth()) {
                    SegmentedButton(
                        selected = state.entryMode == EntryMode.BY_SURAH,
                        onClick = { viewModel.update { it.copy(entryMode = EntryMode.BY_SURAH) } },
                        shape = SegmentedButtonDefaults.itemShape(index = 0, count = 2),
                        colors = SegmentedButtonDefaults.colors(
                            activeContainerColor = GlassWhite,
                            activeContentColor = Color.Black,
                            inactiveContainerColor = Color(0xFF161922).copy(alpha = 0.6f),
                            inactiveContentColor = GlassSilver,
                        ),
                    ) {
                        Text("بالسورة", fontWeight = if (state.entryMode == EntryMode.BY_SURAH) FontWeight.Bold else FontWeight.Normal)
                    }
                    SegmentedButton(
                        selected = state.entryMode == EntryMode.BY_JUZ,
                        onClick = { viewModel.update { it.copy(entryMode = EntryMode.BY_JUZ) } },
                        shape = SegmentedButtonDefaults.itemShape(index = 1, count = 2),
                        colors = SegmentedButtonDefaults.colors(
                            activeContainerColor = GlassWhite,
                            activeContentColor = Color.Black,
                            inactiveContainerColor = Color(0xFF161922).copy(alpha = 0.6f),
                            inactiveContentColor = GlassSilver,
                        ),
                    ) {
                        Text("بالجزء الكامل", fontWeight = if (state.entryMode == EntryMode.BY_JUZ) FontWeight.Bold else FontWeight.Normal)
                    }
                }

                if (state.isMultiSelect && !isEdit) {
                    // === قسم التحديد المتعدد ===
                    if (state.entryMode == EntryMode.BY_SURAH) {
                        OmranCard(Modifier.fillMaxWidth()) {
                            Text("تحديد عدة سور معاً 📚", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = GlassWhite)
                            Text("اختر السور التي أتمها الطالب لحفظها جميعاً دفعة واحدة", style = MaterialTheme.typography.bodySmall, color = GlassSilver)
                            Spacer(Modifier.height(8.dp))

                            // أزرار سريعة
                            Row(
                                Modifier
                                    .fillMaxWidth()
                                    .horizontalScroll(rememberScrollState()),
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                            ) {
                                FilterChip(
                                    selected = false,
                                    onClick = { viewModel.selectSurahsOfJuz(30) },
                                    label = { Text("✨ جزء عم (37 سورة)", color = Color(0xFF6EE7B7)) },
                                    colors = FilterChipDefaults.filterChipColors(containerColor = Color(0xFF064E3B).copy(alpha = 0.5f)),
                                    border = FilterChipDefaults.filterChipBorder(enabled = true, selected = false, borderColor = Color(0xFF10B981).copy(alpha = 0.4f)),
                                )
                                FilterChip(
                                    selected = false,
                                    onClick = { viewModel.selectSurahsOfJuz(29) },
                                    label = { Text("✨ جزء تبارك (11 سورة)", color = Color(0xFF7DD3FC)) },
                                    colors = FilterChipDefaults.filterChipColors(containerColor = Color(0xFF0C4A6E).copy(alpha = 0.5f)),
                                    border = FilterChipDefaults.filterChipBorder(enabled = true, selected = false, borderColor = Color(0xFF0284C7).copy(alpha = 0.4f)),
                                )
                                FilterChip(
                                    selected = false,
                                    onClick = { viewModel.selectSurahsOfJuz(28) },
                                    label = { Text("✨ جزء قد سمع (9 سور)", color = Color(0xFFFDE68A)) },
                                    colors = FilterChipDefaults.filterChipColors(containerColor = Color(0xFF78350F).copy(alpha = 0.5f)),
                                    border = FilterChipDefaults.filterChipBorder(enabled = true, selected = false, borderColor = Color(0xFFD97706).copy(alpha = 0.4f)),
                                )
                                FilterChip(
                                    selected = false,
                                    onClick = { viewModel.selectAllSurahs() },
                                    label = { Text("📖 كل القرآن (114)", color = GlassWhite) },
                                    colors = FilterChipDefaults.filterChipColors(containerColor = Color.White.copy(alpha = 0.1f)),
                                )
                                FilterChip(
                                    selected = false,
                                    onClick = { viewModel.clearSurahSelection() },
                                    label = { Text("مسح", color = Color(0xFFF87171)) },
                                    colors = FilterChipDefaults.filterChipColors(containerColor = Color(0xFF7F1D1D).copy(alpha = 0.4f)),
                                )
                            }

                            Spacer(Modifier.height(10.dp))
                            SearchField(state.surahFilterQuery, viewModel::setSurahFilter)
                            Spacer(Modifier.height(8.dp))

                            val filteredSurahs = QuranData.surahs.filter {
                                state.surahFilterQuery.isBlank() || it.name.contains(state.surahFilterQuery.trim(), ignoreCase = true)
                            }

                            @OptIn(ExperimentalLayoutApi::class)
                            FlowRow(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(6.dp),
                                verticalArrangement = Arrangement.spacedBy(6.dp),
                            ) {
                                filteredSurahs.forEach { surah ->
                                    val isSelected = state.selectedSurahs.contains(surah.name)
                                    FilterChip(
                                        selected = isSelected,
                                        onClick = { viewModel.toggleSurahSelection(surah.name) },
                                        label = { Text("${surah.name} (${surah.verseCount})", fontSize = 12.sp, color = if (isSelected) Color.Black else GlassSilver) },
                                        leadingIcon = if (isSelected) {
                                            { Icon(PhosphorIcons.Regular.Check, contentDescription = null, Modifier.size(14.dp), tint = Color.Black) }
                                        } else null,
                                        colors = FilterChipDefaults.filterChipColors(
                                            selectedContainerColor = Color(0xFF10B981),
                                            containerColor = Color(0xFF161922).copy(alpha = 0.6f),
                                        ),
                                        border = FilterChipDefaults.filterChipBorder(enabled = true, selected = isSelected, borderColor = if (isSelected) Color(0xFF10B981) else Color.White.copy(alpha = 0.1f)),
                                        shape = RoundedCornerShape(8.dp),
                                    )
                                }
                            }

                            Spacer(Modifier.height(10.dp))
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(Color(0xFF10B981).copy(alpha = 0.15f))
                                    .border(1.dp, Color(0xFF10B981).copy(alpha = 0.3f), RoundedCornerShape(10.dp))
                                    .padding(10.dp),
                            ) {
                                Text(
                                    "🟢 تم تحديد ${state.selectedSurahs.size} سورة من أصل 114 سورة",
                                    color = Color(0xFF6EE7B7),
                                    fontWeight = FontWeight.Bold,
                                    style = MaterialTheme.typography.bodyMedium,
                                )
                            }
                        }
                    } else {
                        // تحديد عدة أجزاء
                        OmranCard(Modifier.fillMaxWidth()) {
                            Text("تحديد عدة أجزاء معاً 🕋", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = GlassWhite)
                            Text("اختر الأجزاء التي أتمها الطالب لحفظها كاملة بنقرة واحدة", style = MaterialTheme.typography.bodySmall, color = GlassSilver)
                            Spacer(Modifier.height(8.dp))

                            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                FilterChip(
                                    selected = false,
                                    onClick = { viewModel.selectAllAjzaa() },
                                    label = { Text("🕋 تحديد كل الأجزاء (30 جزء)", color = Color(0xFFFDE68A)) },
                                    colors = FilterChipDefaults.filterChipColors(containerColor = Color(0xFF78350F).copy(alpha = 0.5f)),
                                )
                                FilterChip(
                                    selected = false,
                                    onClick = { viewModel.clearJuzSelection() },
                                    label = { Text("مسح", color = Color(0xFFF87171)) },
                                    colors = FilterChipDefaults.filterChipColors(containerColor = Color(0xFF7F1D1D).copy(alpha = 0.4f)),
                                )
                            }

                            Spacer(Modifier.height(10.dp))
                            @OptIn(ExperimentalLayoutApi::class)
                            FlowRow(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(6.dp),
                                verticalArrangement = Arrangement.spacedBy(6.dp),
                            ) {
                                QuranData.juzList.forEach { juz ->
                                    val isSelected = state.selectedAjzaa.contains(juz.startDescription)
                                    FilterChip(
                                        selected = isSelected,
                                        onClick = { viewModel.toggleJuzSelection(juz.startDescription) },
                                        label = { Text(juz.name, fontSize = 12.sp, color = if (isSelected) Color.Black else GlassSilver) },
                                        leadingIcon = if (isSelected) {
                                            { Icon(PhosphorIcons.Regular.Check, contentDescription = null, Modifier.size(14.dp), tint = Color.Black) }
                                        } else null,
                                        colors = FilterChipDefaults.filterChipColors(
                                            selectedContainerColor = Color(0xFFF59E0B),
                                            containerColor = Color(0xFF161922).copy(alpha = 0.6f),
                                        ),
                                        border = FilterChipDefaults.filterChipBorder(enabled = true, selected = isSelected, borderColor = if (isSelected) Color(0xFFF59E0B) else Color.White.copy(alpha = 0.1f)),
                                        shape = RoundedCornerShape(8.dp),
                                    )
                                }
                            }

                            Spacer(Modifier.height(10.dp))
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(Color(0xFFF59E0B).copy(alpha = 0.15f))
                                    .border(1.dp, Color(0xFFF59E0B).copy(alpha = 0.3f), RoundedCornerShape(10.dp))
                                    .padding(10.dp),
                            ) {
                                Text(
                                    "🟢 تم تحديد ${state.selectedAjzaa.size} جزء من أصل 30 جزءاً",
                                    color = Color(0xFFFDE68A),
                                    fontWeight = FontWeight.Bold,
                                    style = MaterialTheme.typography.bodyMedium,
                                )
                            }
                        }
                    }
                } else {
                    // === قسم الإدخال المفرد (العادي والتعديل) ===
                    if (state.entryMode == EntryMode.BY_JUZ) {
                        DropdownField(
                            label = "اختر الجزء",
                            value = state.selectedJuz,
                            options = QuranData.juzNames,
                            onSelect = { desc -> viewModel.selectJuz(desc) },
                        )
                        Text(
                            "سيتم تسجيل إنجاز ${state.selectedJuz.substringBefore(" (")} كاملاً للطالب",
                            style = MaterialTheme.typography.bodySmall,
                            color = GlassSilver,
                        )
                    } else {
                        DropdownField(
                            label = "السورة",
                            value = "${state.surah} (${state.currentSurahMaxVerses} آية)",
                            options = QuranData.surahs.map { "${it.name} (${it.verseCount} آية)" },
                            onSelect = { option ->
                                val surahName = option.substringBefore(" (")
                                viewModel.selectSurah(surahName)
                            },
                        )

                        // زر "السورة كاملة"
                        Row(
                            Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            FilterChip(
                                selected = state.isFullSurah,
                                onClick = { viewModel.setFullSurah() },
                                label = { Text("السورة كاملة (1 - ${state.currentSurahMaxVerses})", color = if (state.isFullSurah) Color.Black else GlassSilver) },
                                leadingIcon = if (state.isFullSurah) {
                                    { Icon(PhosphorIcons.Regular.Check, contentDescription = null, Modifier.size(16.dp), tint = Color.Black) }
                                } else null,
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = GlassWhite,
                                    containerColor = Color(0xFF161922).copy(alpha = 0.6f),
                                ),
                                border = FilterChipDefaults.filterChipBorder(enabled = true, selected = state.isFullSurah, borderColor = if (state.isFullSurah) GlassWhite else Color.White.copy(alpha = 0.15f)),
                                shape = RoundedCornerShape(10.dp),
                            )
                            Text(
                                "إجمالي الآيات: ${state.currentSurahMaxVerses}",
                                style = MaterialTheme.typography.bodySmall,
                                color = GlassMuted,
                            )
                        }

                        // اختيار أرقام الآيات
                        OmranCard(Modifier.fillMaxWidth()) {
                            Text("تحديد نطاق الآيات", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold, color = GlassWhite)
                            Spacer(Modifier.height(6.dp))

                            Row(
                                Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(16.dp),
                                verticalAlignment = Alignment.CenterVertically,
                            ) {
                                // من آية
                                Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                                    Text("من آية:", style = MaterialTheme.typography.bodySmall, color = GlassMuted)
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                                    ) {
                                        IconButton(
                                            onClick = { viewModel.adjustFromVerse(-1) },
                                            modifier = Modifier.size(36.dp),
                                        ) {
                                            Icon(PhosphorIcons.Regular.Minus, contentDescription = "إنقاص", tint = GlassWhite, modifier = Modifier.size(18.dp))
                                        }
                                        OmranField(
                                            value = state.fromVerse,
                                            onValueChange = { v ->
                                                viewModel.update {
                                                    it.copy(
                                                        fromVerse = v,
                                                        isFullSurah = v == "1" && it.toVerse == it.currentSurahMaxVerses.toString(),
                                                    )
                                                }
                                            },
                                            label = "",
                                            keyboardType = KeyboardType.Number,
                                            modifier = Modifier.weight(1f),
                                            textStyle = androidx.compose.ui.text.TextStyle(
                                                color = GlassWhite,
                                                fontWeight = FontWeight.Bold,
                                                fontSize = 16.sp,
                                                textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                                            ),
                                        )
                                        IconButton(
                                            onClick = { viewModel.adjustFromVerse(1) },
                                            modifier = Modifier.size(36.dp),
                                        ) {
                                            Icon(PhosphorIcons.Regular.Plus, contentDescription = "زيادة", tint = GlassWhite, modifier = Modifier.size(18.dp))
                                        }
                                    }
                                }

                                // إلى آية
                                Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                                    Text("إلى آية:", style = MaterialTheme.typography.bodySmall, color = GlassMuted)
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                                    ) {
                                        IconButton(
                                            onClick = { viewModel.adjustToVerse(-1) },
                                            modifier = Modifier.size(36.dp),
                                        ) {
                                            Icon(PhosphorIcons.Regular.Minus, contentDescription = "إنقاص", tint = GlassWhite, modifier = Modifier.size(18.dp))
                                        }
                                        OmranField(
                                            value = state.toVerse,
                                            onValueChange = { v ->
                                                viewModel.update {
                                                    it.copy(
                                                        toVerse = v,
                                                        isFullSurah = it.fromVerse == "1" && v == it.currentSurahMaxVerses.toString(),
                                                    )
                                                }
                                            },
                                            label = "",
                                            keyboardType = KeyboardType.Number,
                                            modifier = Modifier.weight(1f),
                                            textStyle = androidx.compose.ui.text.TextStyle(
                                                color = GlassWhite,
                                                fontWeight = FontWeight.Bold,
                                                fontSize = 16.sp,
                                                textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                                            ),
                                        )
                                        IconButton(
                                            onClick = { viewModel.adjustToVerse(1) },
                                            modifier = Modifier.size(36.dp),
                                        ) {
                                            Icon(PhosphorIcons.Regular.Plus, contentDescription = "زيادة", tint = GlassWhite, modifier = Modifier.size(18.dp))
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                // التفاصيل بحسب نوع السجل (حفظ / سبر / مراجعة)
                when (viewModel.type) {
                    RecordType.MEMORIZATION -> {
                        Text("تصنيف المحفوظ", fontWeight = FontWeight.Bold, color = GlassWhite)
                        ChoiceRow(
                            options = listOf(com.omran.quran.data.model.MemorizationCategory.NEW.label, com.omran.quran.data.model.MemorizationCategory.OLD.label),
                            selected = state.memorizationCategory.label,
                            onSelect = { v ->
                                val cat = com.omran.quran.data.model.MemorizationCategory.from(v)
                                viewModel.update { it.copy(memorizationCategory = cat) }
                            },
                        )
                        Spacer(Modifier.height(4.dp))
                        Text("التقييم", fontWeight = FontWeight.Bold, color = GlassWhite)
                        ChoiceRow(Ratings.memorization, state.rating) { v -> viewModel.update { it.copy(rating = v) } }
                    }
                    RecordType.EXAM -> {
                        OmranField(state.score, { v -> viewModel.update { it.copy(score = v) } }, "الدرجة (من 100)", keyboardType = KeyboardType.Number)
                        OmranField(state.mistakes, { v -> viewModel.update { it.copy(mistakes = v) } }, "الأخطاء والتنبيهات (اختياري)", singleLine = false)
                        Text("مستوى الإتقان", fontWeight = FontWeight.Bold, color = GlassWhite)
                        ChoiceRow(Ratings.mastery, state.mastery) { v -> viewModel.update { it.copy(mastery = v) } }
                    }
                    RecordType.REVISION -> {
                        Text("تصنيف المراجعة", fontWeight = FontWeight.Bold, color = GlassWhite)
                        ChoiceRow(
                            options = listOf(com.omran.quran.data.model.RevisionCategory.NEW.label, com.omran.quran.data.model.RevisionCategory.OLD.label),
                            selected = state.revisionCategory.label,
                            onSelect = { v ->
                                val cat = com.omran.quran.data.model.RevisionCategory.from(v)
                                viewModel.update { it.copy(revisionCategory = cat) }
                            },
                        )
                        Spacer(Modifier.height(4.dp))
                        Text("مستوى المراجعة", fontWeight = FontWeight.Bold, color = GlassWhite)
                        ChoiceRow(Ratings.revisionLevel, state.level) { v -> viewModel.update { it.copy(level = v) } }
                        Spacer(Modifier.height(4.dp))
                        Text("التقييم", fontWeight = FontWeight.Bold, color = GlassWhite)
                        ChoiceRow(Ratings.revision, state.rating) { v -> viewModel.update { it.copy(rating = v) } }
                    }
                }

                OmranField(state.note, { v -> viewModel.update { it.copy(note = v) } }, "ملاحظات المعلم (اختياري)", singleLine = false)

                Spacer(Modifier.height(14.dp))
                val buttonText = when {
                    isEdit -> "حفظ التعديلات ✅"
                    state.isMultiSelect && state.entryMode == EntryMode.BY_SURAH -> "حفظ (${state.selectedSurahs.size}) سورة للطالب بنقرة واحدة ✨"
                    state.isMultiSelect && state.entryMode == EntryMode.BY_JUZ -> "حفظ (${state.selectedAjzaa.size.coerceAtLeast(1)}) جزء للطالب بنقرة واحدة ✨"
                    else -> "حفظ الإنجاز ✨"
                }
                PrimaryButton(buttonText, onClick = viewModel::save, loading = state.saving)
                Spacer(Modifier.height(24.dp))
            }
        }
    }
}
