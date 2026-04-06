package com.mbp.app.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val MBPColorScheme = darkColorScheme(
    primary = MBPGold,
    secondary = MBPBlue,
    background = MBPDark,
    surface = MBPSurface,
    error = MBPError,
    onPrimary = Color.White,
    onSecondary = Color.White,
    onBackground = Color.White,
    onSurface = MBPOnSurface,
)

@Composable
fun MBPTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = MBPColorScheme,
        typography = Typography,
        content = content
    )
}
