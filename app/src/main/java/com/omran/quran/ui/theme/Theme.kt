package com.omran.quran.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.LayoutDirection

private val DarkGlassColorScheme = darkColorScheme(
    primary = GlassWhite,
    onPrimary = GlassBlack,
    primaryContainer = Color(0xFF222630),
    onPrimaryContainer = GlassWhite,
    secondary = GlassSilver,
    onSecondary = GlassBlack,
    secondaryContainer = Color(0xFF1B1E26),
    onSecondaryContainer = GlassSilver,
    tertiary = GlassMuted,
    background = GlassDarkBg,
    onBackground = GlassWhite,
    surface = GlassSurface,
    onSurface = GlassWhite,
    surfaceVariant = GlassCardBg,
    onSurfaceVariant = GlassMuted,
    outline = Color(0xFF323846),
    outlineVariant = Color(0xFF20242E),
    error = GlassRed,
    onError = GlassBlack,
    errorContainer = Color(0xFF331618),
    onErrorContainer = Color(0xFFFFB4AB),
)

@Composable
fun OmranTheme(
    darkTheme: Boolean = true,
    content: @Composable () -> Unit,
) {
    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
        MaterialTheme(
            colorScheme = DarkGlassColorScheme,
            typography = OmranTypography,
            content = content,
        )
    }
}
