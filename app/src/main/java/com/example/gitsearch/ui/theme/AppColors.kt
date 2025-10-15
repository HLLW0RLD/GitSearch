package com.example.gitsearch.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

object AppColors {

    val tertiary: Color
        @Composable get() = MaterialTheme.colorScheme.tertiary

    val textPrimary: Color
        @Composable get() = MaterialTheme.colorScheme.onBackground
    val textSecondary: Color
        @Composable get() = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)


    val accentPrimary: Color
        @Composable get() = MaterialTheme.colorScheme.primary
    val accentSecondary: Color
        @Composable get() = MaterialTheme.colorScheme.secondary


    val background: Color
        @Composable get() = MaterialTheme.colorScheme.background
    val surface: Color
        @Composable get() = MaterialTheme.colorScheme.surface


    val error: Color
        @Composable get() = MaterialTheme.colorScheme.error
    val success: Color
        @Composable get() = Color(0xFF4CAF50)
}

val YellowPrimary = Color(0xFFFFC107)
val YellowSecondary = Color(0xFFFFA000)
val YellowTertiary = Color(0xFFFFD54F)

val LightColorScheme = lightColorScheme(
    primary = YellowPrimary,
    primaryContainer = YellowTertiary,
    secondary = YellowSecondary,
    secondaryContainer = YellowTertiary,
    tertiary = Color(0xFFBDBDBD),
    background = Color(0xFFFFFFFF),
    surface = Color(0xFFE7E7E7),
    error = Color(0xFFB00020),
    onPrimary = Color(0xFF000000),
    onSecondary = Color(0xFF000000),
    onBackground = Color(0xFF000000),
    onSurface = Color(0xFF000000),
    onError = Color(0xFFFFFFFF)
)

val DarkColorScheme = darkColorScheme(
    primary = YellowPrimary,
    primaryContainer = YellowSecondary,
    secondary = YellowTertiary,
    secondaryContainer = YellowSecondary,
    tertiary = Color(0xFF424242),
    background = Color(0xFF121212),
    surface = Color(0xFF1E1E1E),
    error = Color(0xFFB00020),
    onPrimary = Color(0xFF000000),
    onSecondary = Color(0xFF000000),
    onBackground = Color(0xFFFFFFFF),
    onSurface = Color(0xFFFFFFFF),
    onError = Color(0xFF000000)
)

val LightRandomColorScheme = lightColorScheme(
    primary = YellowPrimary,
    primaryContainer = YellowTertiary,
    secondary = YellowSecondary,
    secondaryContainer = YellowTertiary,
    tertiary = Color(0xFFBDBDBD),
    background = Color(0xFFFFFFFF),
    surface = Color(0xFFE7E7E7),
    error = Color(0xFFB00020),
    onPrimary = Color(0xFF000000),
    onSecondary = Color(0xFF000000),
    onBackground = Color(0xFF000000),
    onSurface = Color(0xFF000000),
    onError = Color(0xFFFFFFFF)
)

val DarkRandomColorScheme = darkColorScheme(
    primary = YellowPrimary,
    primaryContainer = YellowSecondary,
    secondary = YellowTertiary,
    secondaryContainer = YellowSecondary,
    tertiary = Color(0xFF424242),
    background = Color(0xFF121212),
    surface = Color(0xFF1E1E1E),
    error = Color(0xFFB00020),
    onPrimary = Color(0xFF000000),
    onSecondary = Color(0xFF000000),
    onBackground = Color(0xFFFFFFFF),
    onSurface = Color(0xFFFFFFFF),
    onError = Color(0xFF000000)
)