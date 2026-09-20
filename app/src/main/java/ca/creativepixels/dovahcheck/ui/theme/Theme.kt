package ca.creativepixels.dovahcheck.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColors = darkColorScheme(
    primary = Color(0xFFD6B36A),
    secondary = Color(0xFF9AA6B2),
    background = Color(0xFF121316),
    surface = Color(0xFF1B1D21),
    onPrimary = Color(0xFF241A08),
    onBackground = Color(0xFFE8E5DE),
    onSurface = Color(0xFFE8E5DE)
)

private val LightColors = lightColorScheme(
    primary = Color(0xFF6E5319),
    secondary = Color(0xFF59636D),
    background = Color(0xFFF4F0E7),
    surface = Color(0xFFFFFBF2),
    onBackground = Color(0xFF1F1C17),
    onSurface = Color(0xFF1F1C17)
)

@Composable
fun DovahCheckTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = if (darkTheme) DarkColors else LightColors,
        content = content
    )
}
