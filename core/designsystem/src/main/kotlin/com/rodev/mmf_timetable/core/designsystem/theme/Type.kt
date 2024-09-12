package com.rodev.mmf_timetable.core.designsystem.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import com.rodev.mmf_timetable.core.designsystem.R

val Inter = FontFamily(
    listOf(
        Font(R.font.inter_bold, FontWeight.Bold, FontStyle.Normal),
        Font(R.font.inter_black, FontWeight.Black, FontStyle.Normal),
        Font(R.font.inter_regular, FontWeight.Normal, FontStyle.Normal),
        Font(R.font.inter_semibold, FontWeight.SemiBold, FontStyle.Normal),
        Font(R.font.inter_thin, FontWeight.Thin, FontStyle.Normal),
        Font(R.font.inter_extrabold, FontWeight.ExtraBold, FontStyle.Normal),
        Font(R.font.inter_extralight, FontWeight.ExtraLight, FontStyle.Normal),
        Font(R.font.inter_light, FontWeight.Light, FontStyle.Normal),
        Font(R.font.inter_medium, FontWeight.Medium, FontStyle.Normal)
    )
)

private val defaultType = Typography()

// Set of Material typography styles to start with
val Typography = Typography(
    displayLarge = defaultType.displayLarge.copy(fontFamily = Inter),
    displayMedium = defaultType.displayMedium.copy(fontFamily = Inter),
    displaySmall = defaultType.displaySmall.copy(fontFamily = Inter),
    headlineLarge = defaultType.headlineLarge.copy(fontFamily = Inter),
    headlineMedium = defaultType.headlineMedium.copy(fontFamily = Inter),
    headlineSmall = defaultType.headlineSmall.copy(fontFamily = Inter),
    titleLarge = defaultType.titleLarge.copy(fontFamily = Inter),
    titleMedium = defaultType.titleMedium.copy(fontFamily = Inter),
    titleSmall = defaultType.titleSmall.copy(fontFamily = Inter),
    bodyLarge = defaultType.bodyLarge.copy(fontFamily = Inter),
    bodyMedium = defaultType.bodyMedium.copy(fontFamily = Inter),
    bodySmall = defaultType.bodySmall.copy(fontFamily = Inter),
    labelLarge = defaultType.labelLarge.copy(fontFamily = Inter),
    labelMedium = defaultType.labelMedium.copy(fontFamily = Inter),
    labelSmall = defaultType.labelSmall.copy(fontFamily = Inter)
    /* Other default text styles to override
    titleLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 22.sp,
        lineHeight = 28.sp,
        letterSpacing = 0.sp
    ),
    labelSmall = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Medium,
        fontSize = 11.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp
    )
    */
)