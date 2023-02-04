package com.handen.piris.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

val navy_800 = Color(color = 0xFF071278)
val navy_850 = Color(color = 0xFF061068)
val navy_750 = Color(color = 0xFF001E88)
val blue_650 = Color(color = 0xFF162AF1)
val blue_600 = Color(color = 0xFF2639F2)
val blue_550 = Color(color = 0xFF3044FE)
val pale_blue_150 = Color(color = 0xFFC7E5FF)
val pale_blue_100 = Color(color = 0xFFD8EDFF)
val pale_blue_50 = Color(color = 0xFFE9F5FF)
val lilac_450 = Color(color = 0xFF4C56F5)
val lilac_400 = Color(color = 0xFF5C65F6)
val lilac_350 = Color(color = 0xFF6C74F7)
val coral_350 = Color(color = 0xFFFF4B4B)
val coral_300 = Color(color = 0xFFFF5C5C)
val coral_250 = Color(color = 0xFFFF7C77)
val coral_200 = Color(color = 0xFFFF9C9A)
val coral_150 = Color(color = 0xFFFFADAB)

val transparent = Color(color = 0x00000000)
val neutral_0 = Color(color = 0xFFFFFFFF)
val neutral_100 = Color(color = 0xFFF2F2F2)
val neutral_300 = Color(color = 0xFF777DB5)

val status_error = Color(color = 0xFFB00020)
val status_warning = Color(color = 0xFFFF6F00)
val status_success = Color(color = 0xFF4CAF50)

private val DarkColorPalette = darkColors(
    primary = Purple200,
    primaryVariant = Purple700,
    secondary = Teal200
)

private val LightColorPalette = lightColors(
    primary = navy_800,
    primaryVariant = navy_850,
    onPrimary = neutral_0,
    secondary = blue_600,
    secondaryVariant = blue_650,
    onSecondary = neutral_0,
    surface = neutral_0,
    onSurface = navy_800,
    background = neutral_0,
    onBackground = navy_800,
    error = status_error,
    onError = neutral_0
)

@Composable
fun PirisTheme(darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable () -> Unit) {
    MaterialTheme(
        colors = LightColorPalette,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}