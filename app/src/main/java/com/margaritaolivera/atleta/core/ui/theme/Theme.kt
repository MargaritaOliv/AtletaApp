package com.margaritaolivera.atleta.core.ui.theme

import androidx.compose.material3.*
import androidx.compose.runtime.Composable

private val DarkColorScheme = darkColorScheme(
    primary = NeonGreen,
    onPrimary = DarkBackground,

    secondary = NeonGreen,
    onSecondary = DarkBackground,

    background = DarkBackground,
    onBackground = White,

    surface = SurfaceDark,
    onSurface = White,

    outline = TextGray
)

@Composable
fun AtletaTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = DarkColorScheme,
        typography = Typography,
        content = content
    )
}