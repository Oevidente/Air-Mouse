package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val AirMouseIosColorScheme = darkColorScheme(
    primary = AccentIceBlue,
    onPrimary = Color.Black,
    primaryContainer = GlassSurfaceTop,
    onPrimaryContainer = Color.White,
    secondary = AccentElectricBlue,
    onSecondary = Color.White,
    background = CanvasPitchBlack,
    onBackground = IosTextPrimary,
    surface = GlassSurfaceFill,
    onSurface = IosTextPrimary,
    surfaceVariant = GlassSurfaceTop,
    onSurfaceVariant = IosTextSecondary,
    outline = GlassSubtleBorder,
    error = AccentRed,
    onError = Color.White
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = AirMouseIosColorScheme,
        typography = Typography,
        content = content
    )
}
