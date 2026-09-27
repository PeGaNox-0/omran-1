package com.omran.quran.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
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
import androidx.compose.foundation.text.KeyboardOptions
import com.adamglin.PhosphorIcons
import com.adamglin.phosphoricons.Regular
import com.adamglin.phosphoricons.regular.MagnifyingGlass
import com.adamglin.phosphoricons.regular.Tray
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.omran.quran.ui.theme.GlassBlack
import com.omran.quran.ui.theme.GlassMuted
import com.omran.quran.ui.theme.GlassSilver
import com.omran.quran.ui.theme.GlassWhite

/**
 * Extension Modifier adding High-End Frosted Dark Glass to any Composable
 * with crisp contents, gradient depth, and reflective refraction borders.
 */
fun Modifier.darkGlass(
    shape: Shape = RoundedCornerShape(20.dp),
    backgroundColor: Color = Color(0xFF161922).copy(alpha = 0.85f),
    borderWidth: Dp = 1.dp,
): Modifier = this
    .shadow(
        elevation = 8.dp,
        shape = shape,
        ambientColor = Color.Black.copy(alpha = 0.5f),
        spotColor = Color.Black.copy(alpha = 0.5f),
    )
    .clip(shape)
    .background(
        Brush.linearGradient(
            colors = listOf(
                backgroundColor,
                backgroundColor.copy(alpha = (backgroundColor.alpha * 0.88f)),
            ),
        ),
    )
    .border(
        width = borderWidth,
        brush = Brush.linearGradient(
            colors = listOf(
                Color.White.copy(alpha = 0.22f), // لمعة الحافة العلوية
                Color.White.copy(alpha = 0.04f), // تلاشي الحواف
                Color.White.copy(alpha = 0.10f), // لمعة خفيفة سفلية
            ),
        ),
        shape = shape,
    )

/**
 * Reusable Dark Glass Card Container.
 */
@Composable
fun DarkGlassCard(
    modifier: Modifier = Modifier,
    shape: Shape = RoundedCornerShape(20.dp),
    onClick: (() -> Unit)? = null,
    content: @Composable BoxScope.() -> Unit,
) {
    Box(
        modifier = modifier
            .darkGlass(shape = shape)
            .then(if (onClick != null) Modifier.clickable(onClick = onClick) else Modifier)
            .padding(horizontal = 18.dp, vertical = 16.dp),
        content = content,
    )
}

/**
 * Reusable OmranCard backed by Dark Glassmorphism.
 */
@Composable
fun OmranCard(
    modifier: Modifier = Modifier,
    shape: Shape = RoundedCornerShape(20.dp),
    onClick: (() -> Unit)? = null,
    content: @Composable ColumnScope.() -> Unit,
) {
    Box(
        modifier = modifier
            .darkGlass(shape = shape)
            .then(if (onClick != null) Modifier.clickable(onClick = onClick) else Modifier)
            .padding(horizontal = 18.dp, vertical = 16.dp),
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            content = content,
        )
    }
}

/**
 * Floating Dark Glass Bottom Navigation Bar container.
 */
@Composable
fun GlassBottomBar(
    modifier: Modifier = Modifier,
    content: @Composable RowScope.() -> Unit,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 8.dp)
            .darkGlass(
                shape = RoundedCornerShape(26.dp),
                backgroundColor = Color(0xFF13161F).copy(alpha = 0.95f),
                borderWidth = 1.dp,
            )
            .padding(horizontal = 6.dp, vertical = 6.dp),
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically,
        content = content,
    )
}

/**
 * Ambient background container with deep atmospheric lighting.
 */
@Composable
fun DarkGlassBackground(
    modifier: Modifier = Modifier,
    content: @Composable BoxScope.() -> Unit,
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF090A0E),
                        Color(0xFF0E1017),
                        Color(0xFF07080B),
                    ),
                ),
            ),
        content = content,
    )
}

@Composable
fun StatCard(
    title: String,
    value: String,
    icon: ImageVector,
    modifier: Modifier = Modifier,
    accent: Boolean = false,
) {
    OmranCard(modifier = modifier) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.fillMaxWidth(),
        ) {
            Box(
                modifier = Modifier
                    .size(46.dp)
                    .clip(CircleShape)
                    .background(Color.White.copy(alpha = if (accent) 0.16f else 0.08f))
                    .border(1.dp, Color.White.copy(alpha = if (accent) 0.35f else 0.15f), CircleShape),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    icon,
                    contentDescription = null,
                    tint = if (accent) GlassWhite else GlassSilver,
                    modifier = Modifier.size(22.dp),
                )
            }
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(2.dp),
            ) {
                Text(
                    title,
                    style = MaterialTheme.typography.labelMedium,
                    color = GlassMuted,
                    maxLines = 1,
                    overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis,
                )
                Text(
                    value,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = GlassWhite,
                    maxLines = 1,
                )
            }
        }
    }
}

@Composable
fun LoadingBox(modifier: Modifier = Modifier) {
    Box(modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        CircularProgressIndicator(color = GlassWhite, strokeWidth = 3.dp)
    }
}

@Composable
fun EmptyState(text: String, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.fillMaxWidth().padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Box(
            Modifier
                .size(64.dp)
                .clip(CircleShape)
                .background(Color.White.copy(alpha = 0.06f))
                .border(1.dp, Color.White.copy(alpha = 0.12f), CircleShape),
            contentAlignment = Alignment.Center,
        ) {
            Icon(PhosphorIcons.Regular.Tray, null, tint = GlassMuted, modifier = Modifier.size(32.dp))
        }
        Spacer(Modifier.height(14.dp))
        Text(text, textAlign = TextAlign.Center, color = GlassMuted, style = MaterialTheme.typography.bodyMedium)
    }
}

@Composable
fun SearchField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    placeholder: String = "بحث باسم الطالب...",
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier
            .fillMaxWidth()
            .darkGlass(shape = RoundedCornerShape(16.dp), backgroundColor = Color(0xFF151822).copy(alpha = 0.85f)),
        singleLine = true,
        placeholder = { Text(placeholder, color = GlassMuted, style = MaterialTheme.typography.bodyMedium) },
        leadingIcon = { Icon(PhosphorIcons.Regular.MagnifyingGlass, contentDescription = null, tint = GlassSilver, modifier = Modifier.size(20.dp)) },
        colors = OutlinedTextFieldDefaults.colors(
            focusedTextColor = GlassWhite,
            unfocusedTextColor = GlassWhite,
            focusedContainerColor = Color.Transparent,
            unfocusedContainerColor = Color.Transparent,
            focusedBorderColor = Color.Transparent,
            unfocusedBorderColor = Color.Transparent,
        ),
        shape = RoundedCornerShape(16.dp),
    )
}

@Composable
fun OmranField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    keyboardType: KeyboardType = KeyboardType.Text,
    singleLine: Boolean = true,
    enabled: Boolean = true,
    isPassword: Boolean = false,
    textStyle: androidx.compose.ui.text.TextStyle = androidx.compose.ui.text.TextStyle(color = GlassWhite, fontSize = 15.sp),
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier
            .fillMaxWidth()
            .darkGlass(shape = RoundedCornerShape(16.dp), backgroundColor = Color(0xFF151822).copy(alpha = 0.85f)),
        label = if (label.isNotBlank()) { { Text(label, color = GlassMuted, style = MaterialTheme.typography.bodyMedium) } } else null,
        singleLine = singleLine,
        enabled = enabled,
        textStyle = textStyle,
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        visualTransformation = if (isPassword) PasswordVisualTransformation() else VisualTransformation.None,
        colors = OutlinedTextFieldDefaults.colors(
            focusedTextColor = GlassWhite,
            unfocusedTextColor = GlassWhite,
            focusedContainerColor = Color.Transparent,
            unfocusedContainerColor = Color.Transparent,
            focusedBorderColor = Color.Transparent,
            unfocusedBorderColor = Color.Transparent,
            focusedLabelColor = GlassWhite,
            unfocusedLabelColor = GlassMuted,
        ),
        shape = RoundedCornerShape(16.dp),
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DropdownField(
    label: String,
    value: String,
    options: List<String>,
    modifier: Modifier = Modifier,
    onSelect: (String) -> Unit,
) {
    var expanded by remember { mutableStateOf(false) }
    ExposedDropdownMenuBox(expanded = expanded, onExpandedChange = { expanded = it }, modifier = modifier) {
        OutlinedTextField(
            value = value,
            onValueChange = {},
            readOnly = true,
            label = { Text(label, color = GlassMuted, style = MaterialTheme.typography.bodyMedium) },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded) },
            modifier = Modifier
                .fillMaxWidth()
                .darkGlass(shape = RoundedCornerShape(16.dp), backgroundColor = Color(0xFF151822).copy(alpha = 0.85f))
                .menuAnchor(MenuAnchorType.PrimaryNotEditable),
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = GlassWhite,
                unfocusedTextColor = GlassWhite,
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
                focusedBorderColor = Color.Transparent,
                unfocusedBorderColor = Color.Transparent,
            ),
            shape = RoundedCornerShape(16.dp),
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.darkGlass(shape = RoundedCornerShape(16.dp), backgroundColor = Color(0xFF1C202C).copy(alpha = 0.98f)),
        ) {
            options.forEach { option ->
                DropdownMenuItem(
                    text = { Text(option, color = GlassWhite, fontWeight = FontWeight.SemiBold, style = MaterialTheme.typography.bodyMedium) },
                    onClick = {
                        onSelect(option)
                        expanded = false
                    },
                )
            }
        }
    }
}

@Composable
fun ChoiceRow(
    options: List<String>,
    selected: String,
    onSelect: (String) -> Unit,
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState())
            .padding(vertical = 4.dp),
    ) {
        options.forEach { option ->
            val isSelected = selected == option
            FilterChip(
                selected = isSelected,
                onClick = { onSelect(option) },
                label = {
                    Text(
                        option,
                        color = if (isSelected) GlassBlack else GlassSilver,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp),
                    )
                },
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = GlassWhite,
                    containerColor = Color(0xFF161922).copy(alpha = 0.75f),
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

@Composable
fun PrimaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    loading: Boolean = false,
) {
    Button(
        onClick = onClick,
        enabled = enabled && !loading,
        modifier = modifier
            .fillMaxWidth()
            .height(52.dp)
            .shadow(6.dp, RoundedCornerShape(16.dp), spotColor = Color.White.copy(alpha = 0.15f)),
        shape = RoundedCornerShape(16.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = GlassWhite,
            contentColor = GlassBlack,
            disabledContainerColor = Color(0xFF252932),
            disabledContentColor = GlassMuted,
        ),
    ) {
        if (loading) {
            CircularProgressIndicator(Modifier.size(22.dp), strokeWidth = 2.5.dp, color = GlassBlack)
        } else {
            Text(text, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleSmall)
        }
    }
}

@Composable
fun ErrorBanner(message: String?, onDismiss: () -> Unit) {
    AnimatedVisibility(visible = !message.isNullOrBlank()) {
        if (!message.isNullOrBlank()) {
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp)
                    .darkGlass(shape = RoundedCornerShape(14.dp), backgroundColor = Color(0xFF3D1416).copy(alpha = 0.9f))
                    .padding(horizontal = 14.dp, vertical = 10.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(message, color = Color(0xFFFFB4AB), modifier = Modifier.weight(1f), style = MaterialTheme.typography.bodyMedium)
                TextButton(onClick = onDismiss) { Text("حسنًا", color = GlassWhite, fontWeight = FontWeight.Bold) }
            }
        }
    }
}

fun listBottomPadding(): PaddingValues = PaddingValues(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 104.dp)

