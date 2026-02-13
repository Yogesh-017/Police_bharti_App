package com.policebharti.pyq.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.core.view.WindowCompat

// ── Premium Color Palette ──
val DeepNavy       = Color(0xFF0D1B2A)
val RoyalBlue      = Color(0xFF1B3A5C)
val AccentGold     = Color(0xFFFFC107)
val AccentAmber    = Color(0xFFFF8F00)
val SurfaceLight   = Color(0xFFF5F7FA)
val SurfaceDark    = Color(0xFF1A1A2E)
val CardLight      = Color(0xFFFFFFFF)
val CardDark       = Color(0xFF16213E)
val CorrectGreen   = Color(0xFF2E7D32)
val WrongRed       = Color(0xFFC62828)
val AnsweredAmber  = Color(0xFFF9A825)
val NotVisitedGrey = Color(0xFFBDBDBD)
val TextPrimary    = Color(0xFF212121)
val TextSecondary  = Color(0xFF757575)
val GradientStart  = Color(0xFF1A237E)
val GradientEnd    = Color(0xFF0D47A1)

// ── Dark scheme ──
private val DarkColorScheme = darkColorScheme(
    primary          = AccentGold,
    onPrimary        = DeepNavy,
    primaryContainer = RoyalBlue,
    secondary        = AccentAmber,
    background       = SurfaceDark,
    surface          = CardDark,
    onBackground     = Color.White,
    onSurface        = Color.White,
    error            = WrongRed
)

// ── Light scheme ──
private val LightColorScheme = lightColorScheme(
    primary          = RoyalBlue,
    onPrimary        = Color.White,
    primaryContainer = Color(0xFFD1E3F8),
    secondary        = AccentGold,
    background       = SurfaceLight,
    surface          = CardLight,
    onBackground     = TextPrimary,
    onSurface        = TextPrimary,
    error            = WrongRed
)

// ── Typography ──
val AppTypography = Typography(
    displayLarge  = TextStyle(fontWeight = FontWeight.Bold, fontSize = 28.sp),
    headlineLarge = TextStyle(fontWeight = FontWeight.Bold, fontSize = 24.sp),
    headlineMedium = TextStyle(fontWeight = FontWeight.SemiBold, fontSize = 20.sp),
    titleLarge    = TextStyle(fontWeight = FontWeight.SemiBold, fontSize = 18.sp),
    titleMedium   = TextStyle(fontWeight = FontWeight.Medium, fontSize = 16.sp),
    bodyLarge     = TextStyle(fontWeight = FontWeight.Normal, fontSize = 16.sp),
    bodyMedium    = TextStyle(fontWeight = FontWeight.Normal, fontSize = 14.sp),
    labelLarge    = TextStyle(fontWeight = FontWeight.SemiBold, fontSize = 14.sp),
    labelMedium   = TextStyle(fontWeight = FontWeight.Medium, fontSize = 12.sp)
)

@Composable
fun PoliceBhartiTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = GradientStart.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = false
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = AppTypography,
        content = content
    )
}
