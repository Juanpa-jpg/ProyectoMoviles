package com.example.cazadorpalabras2.ui.theme
// app/src/main/java/mx/edu/cazadorpalabras/ui/theme/Theme.kt

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ShapeDefaults.Small
import androidx.compose.material3.Shapes
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

// Importa tus colores desde R.color (si quieres) o define Color(...) directamente.
// Para este ejemplo, definamos los hexadecimales manualmente.

import androidx.compose.ui.graphics.Color

private val LightColors = lightColorScheme(
    primary = Color(0xFF795548),        // brown_500
    onPrimary = Color(0xFFFFFFFF),      // blanco para texto sobre primario
    secondary = Color(0xFFA1887F),      // brown_300
    background = Color(0xFFF5F5F5),     // gris muy claro
    onBackground = Color(0xFF000000),   // negro
    surface = Color(0xFFFFFFFF),        // blanco
    onSurface = Color(0xFF000000)
)

private val DarkColors = darkColorScheme(
    primary = Color(0xFF3E2723),        // brown_900
    onPrimary = Color(0xFFFFFFFF),
    secondary = Color(0xFF795548),      // brown_500
    background = Color(0xFF121212),     // fondo oscuro
    onBackground = Color(0xFFFFFFFF),
    surface = Color(0xFF1E1E1E),
    onSurface = Color(0xFFFFFFFF)
)

@Composable
fun CazadorPalabrasTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colors = if (darkTheme) DarkColors else LightColors
    MaterialTheme(
        colorScheme = colors,
        typography = Typography,   // viene de la plantilla
        shapes = androidx.compose.material3.Shapes(),           // viene de la plantilla
        content = content
    )
}
