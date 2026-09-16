package com.flylab.ui.theme

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.material3.darkColorScheme
import android.content.Context

private val LightColors = lightColorScheme(
    primary = 0xFF6200EE,
    primaryVariant = 0xFF3700B3,
    secondary = 0xFF03DAC5,
    secondaryVariant = 0xFF018786,
    background = 0xFFFFFFFF,
    surface = 0xFFFFFFFF,
    error = 0xFFB00020,
    onPrimary = 0xFFFFFFFF,
    onSecondary = 0xFF000000,
    onBackground = 0xFF000000,
    onSurface = 0xFF000000,
    onError = 0xFFFFFFFF
)

private val DarkColors = darkColorScheme(
    primary = 0xFFBB86FC,
    primaryVariant = 0xFF3700B3,
    secondary = 0xFF03DAC5,
    secondaryVariant = 0xFF018786,
    background = 0xFF121212,
    surface = 0xFF121212,
    error = 0xFFCF6679,
    onPrimary = 0xFF000000,
    onSecondary = 0xFF000000,
    onBackground = 0xFFFFFFFF,
    onSurface = 0xFFFFFFFF,
    onError = 0xFF000000
)

@Composable
fun FlyLabTheme(
    content: @Composable () -> Unit
) {
    val context = LocalContext.current
    val colors = if (context.resources.configuration.uiMode and
        android.content.res.Configuration.UI_MODE_NIGHT_MASK ==
        android.content.res.Configuration.UI_MODE_NIGHT_YES) {
        DarkColors
    } else {
        LightColors
    }

    MaterialTheme(
        colorScheme = colors,
        content = content
    )
}