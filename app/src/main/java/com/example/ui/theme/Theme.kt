package com.example.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = ImperialGoldLight,
    onPrimary = DarkLacquer,
    primaryContainer = ImperialCrimsonDark,
    onPrimaryContainer = ImperialGoldLight,
    secondary = LotusPink,
    onSecondary = Color.White,
    secondaryContainer = DarkSurfaceVariant,
    onSecondaryContainer = SilkCream,
    tertiary = JadeGreen,
    onTertiary = Color.White,
    background = DarkLacquer,
    onBackground = SilkCream,
    surface = DarkSurface,
    onSurface = SilkCream,
    surfaceVariant = DarkSurfaceVariant,
    onSurfaceVariant = SilkCreamDark
)

private val LightColorScheme = lightColorScheme(
    primary = ImperialCrimson,
    onPrimary = Color.White,
    primaryContainer = SilkCreamDark,
    onPrimaryContainer = ImperialCrimsonDark,
    secondary = ImperialGoldDark,
    onSecondary = Color.White,
    secondaryContainer = SilkCream,
    onSecondaryContainer = DarkLacquer,
    tertiary = JadeGreen,
    onTertiary = Color.White,
    background = SilkCream,
    onBackground = DarkLacquer,
    surface = Color.White,
    onSurface = DarkLacquer,
    surfaceVariant = SilkCreamDark,
    onSurfaceVariant = DarkLacquer
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Preserve custom branding colors for traditional heritage vibe
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
