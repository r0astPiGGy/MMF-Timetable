package com.rodev.mmf_timetable.presentation.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat
import com.rodev.mmf_timetable.presentation.theme.AppColors.DarkBlue
import com.rodev.mmf_timetable.presentation.theme.AppColors.Pink40
import com.rodev.mmf_timetable.presentation.theme.AppColors.LightBlueVariant
import com.rodev.mmf_timetable.presentation.theme.AppColors.PurpleGrey40
import com.rodev.mmf_timetable.presentation.theme.AppColors.LightBlue
import com.rodev.mmf_timetable.presentation.theme.AppColors.outlineColor

private val DarkColorScheme = darkColorScheme(
    primary = DarkBlue,
    secondary = LightBlue,
    tertiary = LightBlueVariant,
    outline = outlineColor,
    outlineVariant = outlineColor,
    surfaceTint = Color.White,
)

private val LightColorScheme = lightColorScheme(
    // The primary color is the color displayed most frequently across your app’s screens
    // and components.
    primary = DarkBlue,
    // onPrimary,
    // primaryContainer, The preferred tonal color of containers.
    // onPrimaryContainer,
    // inversePrimary,
    secondary = LightBlue,
    // onSecondary,
    // secondaryContainer,
    // onSecondaryContainer,
    tertiary = LightBlueVariant,
    // onTertiary,
    // tertiaryContainer,
    // onTertiaryContainer,
    background = Color.White,
    // onBackground,
    surface = Color.White,
    // onSurface,
    // surfaceVariant,
    // onSurfaceVariant,
    surfaceTint = Color.White,
    // inverseSurface,
    // inverseOnSurface,
    // error,
    // onError,
    // errorContainer,
    // onErrorContainer,
    outline = outlineColor,
    outlineVariant = outlineColor,
    // scrim,
)

@Composable
fun MMF_TimetableTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme
    val view = LocalView.current

    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.background.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}