package com.example.recipeexplorer.ui.theme

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
    primary = PrimaryOrange,
    onPrimary = Color.Black,
    primaryContainer = PrimaryOrangeDark,
    onPrimaryContainer = TextPrimary,

    secondary = PrimaryOrangeLight,
    onSecondary = Color.Black,
    secondaryContainer = SurfaceVariant,
    onSecondaryContainer = TextPrimary,

    tertiary = Gold,
    onTertiary = Color.Black,

    background = BackgroundDark,
    onBackground = TextPrimary,

    surface = SurfaceDark,
    onSurface = TextPrimary,
    surfaceVariant = SurfaceVariant,
    onSurfaceVariant = TextSecondary,

    error = DifficultyHard,
    onError = TextPrimary,

    outline = TextTertiary,
    outlineVariant = SurfaceVariant
)

private val LightColorScheme = lightColorScheme(
    primary = PrimaryOrangeDark,
    secondary = SurfaceDark,
    tertiary = PrimaryOrange
)

@Composable
fun RecipeExplorerAppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
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