package com.example.androidapp.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

private val Red = Color(0xFFD32F2F)
private val RedDark = Color(0xFF9A0007)

private val LightColors = lightColorScheme(
    primary = Red,
    onPrimary = Color.White,
    primaryContainer = Color(0xFFFFCDD2),
    onPrimaryContainer = Color(0xFF410002),
    secondary = Color(0xFFB71C1C),
    onSecondary = Color.White,
    background = Color(0xFFFFFBFF),
    onBackground = Color(0xFF111111),
    surface = Color(0xFFFFFBFF),
    onSurface = Color(0xFF111111),
    surfaceVariant = Color(0xFFFFEBEE),
    onSurfaceVariant = Color(0xFF5F2121),
)

private val DarkColors = darkColorScheme(
    primary = RedDark,
    onPrimary = Color.White,
    primaryContainer = Color(0xFF5C0002),
    onPrimaryContainer = Color(0xFFFFDAD5),
    secondary = Color(0xFFFF8A80),
    onSecondary = Color(0xFF400000),
    background = Color(0xFF121212),
    onBackground = Color.White,
    surface = Color(0xFF121212),
    onSurface = Color.White,
    surfaceVariant = Color(0xFF3B1818),
    onSurfaceVariant = Color(0xFFFFCDD2),
)

@Composable
fun AndroidAppTheme(
    darkTheme: Boolean = false,
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit,
) {
    val colorScheme = if (darkTheme) DarkColors else LightColors

    val typography = Typography(
        headlineMedium = TextStyle(
            fontWeight = FontWeight.SemiBold,
            letterSpacing = (-0.5).sp,
        ),
        titleLarge = TextStyle(
            fontWeight = FontWeight.SemiBold,
        ),
        titleMedium = TextStyle(
            fontWeight = FontWeight.Medium,
        ),
        labelLarge = TextStyle(
            fontWeight = FontWeight.Medium,
        ),
    )

    MaterialTheme(
        colorScheme = colorScheme,
        typography = typography,
        content = content,
    )
}

