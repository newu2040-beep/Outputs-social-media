package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.example.data.model.GenreTheme

private val DarkOutputsColorScheme = darkColorScheme(
    primary = OutputsVioletPrimary,
    onPrimary = Color.White,
    primaryContainer = OutputsVioletSecondary,
    secondary = OutputsTealAccent,
    onSecondary = Color.Black,
    tertiary = OutputsGoldAccent,
    background = OutputsDarkBackground,
    surface = OutputsDarkSurface,
    surfaceVariant = OutputsDarkSurfaceVariant,
    onBackground = OutputsTextPrimary,
    onSurface = OutputsTextPrimary,
    onSurfaceVariant = OutputsTextSecondary,
    error = OutputsRedAccent
)

private val LightOutputsColorScheme = lightColorScheme(
    primary = Color(0xFF6B21A8),
    onPrimary = Color.White,
    primaryContainer = Color(0xFFF3E8FF),
    secondary = Color(0xFF0D9488),
    onSecondary = Color.White,
    tertiary = Color(0xFFD97706),
    background = Color(0xFFF8FAFC),
    surface = Color(0xFFFFFFFF),
    surfaceVariant = Color(0xFFE2E8F0),
    onBackground = Color(0xFF0F172A),
    onSurface = Color(0xFF0F172A),
    onSurfaceVariant = Color(0xFF64748B),
    error = Color(0xFFDC2626)
)

@Composable
fun OutputsTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    genreTheme: GenreTheme = GenreTheme.DEFAULT,
    content: @Composable () -> Unit
) {
    val baseScheme = if (darkTheme) DarkOutputsColorScheme else LightOutputsColorScheme
    
    // Override colors subtly based on selected genre theme
    val finalScheme = when (genreTheme) {
        GenreTheme.MYSTERY -> baseScheme.copy(
            background = MysteryBg,
            surface = MysterySurface,
            primary = MysteryAccent
        )
        GenreTheme.HORROR -> baseScheme.copy(
            background = HorrorBg,
            surface = HorrorSurface,
            primary = HorrorAccent
        )
        GenreTheme.NOIR -> baseScheme.copy(
            background = NoirBg,
            surface = NoirSurface,
            primary = NoirAccent
        )
        GenreTheme.CYBER -> baseScheme.copy(
            background = CyberBg,
            surface = CyberSurface,
            primary = CyberAccent
        )
        GenreTheme.ARCHIVE -> baseScheme.copy(
            background = ArchiveBg,
            surface = ArchiveSurface,
            primary = ArchiveAccent
        )
        GenreTheme.MINIMAL -> baseScheme.copy(
            background = MinimalBg,
            surface = MinimalSurface,
            primary = MinimalAccent
        )
        else -> baseScheme
    }

    MaterialTheme(
        colorScheme = finalScheme,
        typography = Typography,
        content = content
    )
}

// Alias for template compatibility
@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    OutputsTheme(darkTheme = true, content = content)
}
