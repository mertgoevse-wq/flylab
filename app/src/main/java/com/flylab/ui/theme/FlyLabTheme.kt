package com.flylab.ui.theme

import android.content.res.Configuration
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

// Scientific laboratory color palette: Clean neutral backgrounds with emerald / mint accents
val LabEmeraldPrimary = Color(0xFF059669)
val LabEmeraldOnPrimary = Color(0xFFFFFFFF)
val LabEmeraldContainer = Color(0xFFD1FAE5)
val LabEmeraldOnContainer = Color(0xFF064E3B)

val LabSlateSecondary = Color(0xFF475569)
val LabSlateOnSecondary = Color(0xFFFFFFFF)
val LabSlateContainer = Color(0xFFE2E8F0)
val LabSlateOnContainer = Color(0xFF0F172A)

val LabTealTertiary = Color(0xFF0D9488)
val LabTealOnTertiary = Color(0xFFFFFFFF)
val LabTealContainer = Color(0xFFCCFBF1)
val LabTealOnContainer = Color(0xFF134E4A)

val LabLightBackground = Color(0xFFF8FAFC)
val LabLightOnBackground = Color(0xFF0F172A)
val LabLightSurface = Color(0xFFFFFFFF)
val LabLightOnSurface = Color(0xFF0F172A)
val LabLightSurfaceVariant = Color(0xFFF1F5F9)
val LabLightOnSurfaceVariant = Color(0xFF334155)
val LabLightOutline = Color(0xFFCBD5E1)

// Dark mode for low-light lab conditions
val LabDarkPrimary = Color(0xFF34D399)
val LabDarkOnPrimary = Color(0xFF064E3B)
val LabDarkPrimaryContainer = Color(0xFF065F46)
val LabDarkOnPrimaryContainer = Color(0xFFA7F3D0)

val LabDarkSecondary = Color(0xFF94A3B8)
val LabDarkOnSecondary = Color(0xFF0F172A)
val LabDarkSecondaryContainer = Color(0xFF334155)
val LabDarkOnSecondaryContainer = Color(0xFFE2E8F0)

val LabDarkTertiary = Color(0xFF2DD4BF)
val LabDarkOnTertiary = Color(0xFF134E4A)
val LabDarkContainer = Color(0xFF115E59)
val LabDarkOnContainer = Color(0xFF99F6E4)

val LabDarkBackground = Color(0xFF0F172A)
val LabDarkOnBackground = Color(0xFFF8FAFC)
val LabDarkSurface = Color(0xFF1E293B)
val LabDarkOnSurface = Color(0xFFF8FAFC)
val LabDarkSurfaceVariant = Color(0xFF334155)
val LabDarkOnSurfaceVariant = Color(0xFFCBD5E1)
val LabDarkOutline = Color(0xFF475569)

private val LightColors = lightColorScheme(
    primary = LabEmeraldPrimary,
    onPrimary = LabEmeraldOnPrimary,
    primaryContainer = LabEmeraldContainer,
    onPrimaryContainer = LabEmeraldOnContainer,
    secondary = LabSlateSecondary,
    onSecondary = LabSlateOnSecondary,
    secondaryContainer = LabSlateContainer,
    onSecondaryContainer = LabSlateOnContainer,
    tertiary = LabTealTertiary,
    onTertiary = LabTealOnTertiary,
    tertiaryContainer = LabTealContainer,
    onTertiaryContainer = LabTealOnContainer,
    background = LabLightBackground,
    onBackground = LabLightOnBackground,
    surface = LabLightSurface,
    onSurface = LabLightOnSurface,
    surfaceVariant = LabLightSurfaceVariant,
    onSurfaceVariant = LabLightOnSurfaceVariant,
    outline = LabLightOutline
)

private val DarkColors = darkColorScheme(
    primary = LabDarkPrimary,
    onPrimary = LabDarkOnPrimary,
    primaryContainer = LabDarkPrimaryContainer,
    onPrimaryContainer = LabDarkOnPrimaryContainer,
    secondary = LabDarkSecondary,
    onSecondary = LabDarkOnSecondary,
    secondaryContainer = LabDarkSecondaryContainer,
    onSecondaryContainer = LabDarkOnSecondaryContainer,
    tertiary = LabDarkTertiary,
    onTertiary = LabDarkOnTertiary,
    tertiaryContainer = LabDarkContainer,
    onTertiaryContainer = LabDarkOnContainer,
    background = LabDarkBackground,
    onBackground = LabDarkOnBackground,
    surface = LabDarkSurface,
    onSurface = LabDarkOnSurface,
    surfaceVariant = LabDarkSurfaceVariant,
    onSurfaceVariant = LabDarkOnSurfaceVariant,
    outline = LabDarkOutline
)

@Composable
fun FlyLabTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colors = if (darkTheme) DarkColors else LightColors

    MaterialTheme(
        colorScheme = colors,
        content = content
    )
}
