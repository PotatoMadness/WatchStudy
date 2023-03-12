package com.example.testevinfra.presentation.theme

import android.annotation.SuppressLint
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable


private val EVIDarkColorScheme = lightColorScheme(
    primary = Nt000,
    onPrimary = Nt900,
    primaryContainer = BlueGrey30,
    onPrimaryContainer = Grey90,
    inversePrimary = Grey80,
    secondary = Nt200,
    onSecondary = Nt700,
    secondaryContainer = Nt300,
    onSecondaryContainer = DarkBlue90,
    tertiary = Nt300,
    onTertiary = Nt600,
    tertiaryContainer = Nt400,
    onTertiaryContainer = Yellow90,
    error = Red80,
    onError = Red20,
    errorContainer = Red30,
    onErrorContainer = Red90,
    background = Nt999,
    onBackground = Nt000,
    surface = Gr400,
    onSurface = Grey80,
    inverseSurface = Grey90,
    inverseOnSurface = Grey20,
    surfaceVariant = BlueGrey30,
    onSurfaceVariant = BlueGrey80,
    outline = Nt500
)

@SuppressLint("NewApi")
@Composable
fun EVITheme(
    content: @Composable () -> Unit
) {
    androidx.compose.material3.MaterialTheme(
        colorScheme = EVIDarkColorScheme,
        typography = Typography,
        content = content
    )
}
