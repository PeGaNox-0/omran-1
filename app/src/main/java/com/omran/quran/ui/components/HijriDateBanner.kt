package com.omran.quran.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.omran.quran.ui.theme.GlassSilver
import com.omran.quran.util.HijriDateHelper
import java.time.LocalDate

@Composable
fun HijriDateBanner(
    modifier: Modifier = Modifier,
    compact: Boolean = false,
) {
    val today = remember { LocalDate.now() }
    val hijriDate = remember(today) { HijriDateHelper.formatHijri(today, includeDayName = true) }
    val gregorianDate = remember(today) { HijriDateHelper.formatGregorian(today, includeDayName = false) }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(
                Brush.horizontalGradient(
                    colors = listOf(
                        Color(0xFF161F30).copy(alpha = 0.95f),
                        Color(0xFF0F172A).copy(alpha = 0.92f),
                    )
                )
            )
            .border(
                1.dp,
                Brush.horizontalGradient(
                    colors = listOf(
                        Color(0xFFD97706).copy(alpha = 0.45f), // ذهبي عنبري
                        Color(0xFF059669).copy(alpha = 0.30f), // زمردي إسلامي
                    )
                ),
                RoundedCornerShape(16.dp)
            )
            .padding(horizontal = 16.dp, vertical = if (compact) 10.dp else 12.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp),
            ) {
                Box(
                    modifier = Modifier
                        .size(if (compact) 34.dp else 38.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFD97706).copy(alpha = 0.15f))
                        .border(1.dp, Color(0xFFD97706).copy(alpha = 0.4f), CircleShape),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        "🌙",
                        style = if (compact) MaterialTheme.typography.bodyMedium else MaterialTheme.typography.bodyLarge,
                    )
                }

                Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                    Text(
                        text = hijriDate,
                        style = if (compact) MaterialTheme.typography.bodyMedium else MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFFFDE68A),
                    )
                    Text(
                        text = "الموافق: $gregorianDate",
                        style = MaterialTheme.typography.bodySmall,
                        color = GlassSilver,
                    )
                }
            }

            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color(0xFF059669).copy(alpha = 0.18f))
                    .border(1.dp, Color(0xFF059669).copy(alpha = 0.35f), RoundedCornerShape(8.dp))
                    .padding(horizontal = 8.dp, vertical = 4.dp),
            ) {
                Text(
                    text = "اليوم",
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF6EE7B7),
                )
            }
        }
    }
}
