package com.app.taskly.task.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.app.taskly.R

val hostFontFamilyRegular = FontFamily(
    Font(R.font.host_grotesk_regular)
)

val hostFontFamilyBold = FontFamily(
    Font(R.font.host_grotesk_bold)
)

val Typography = Typography(
    displayLarge = TextStyle(
        fontFamily = hostFontFamilyRegular,
        fontSize = 30.sp,
        fontWeight = FontWeight.SemiBold,
        letterSpacing = 0.sp,
        color = Color.White
    ),
    displayMedium = TextStyle(
        fontFamily = hostFontFamilyRegular,
        fontSize = 22.sp,
        fontWeight = FontWeight.SemiBold,
        letterSpacing = 0.sp,
        color = Color.White
    ),
    displaySmall = TextStyle(
        fontFamily = hostFontFamilyRegular,
        fontSize = 18.sp,
        fontWeight = FontWeight.Normal,
        letterSpacing = 0.sp,
        color = Color.White
    ),
    headlineLarge = TextStyle(
        fontFamily = hostFontFamilyRegular,
        fontSize = 30.sp,
        fontWeight = FontWeight.Normal,
        letterSpacing = 0.sp,
        color = Color.White
    ),
    headlineMedium = TextStyle(
        fontFamily = hostFontFamilyRegular,
        fontSize = 16.sp,
        fontWeight = FontWeight.Normal,
        letterSpacing = 0.sp,
        color = Color.White
    ),
    headlineSmall = TextStyle(
        fontFamily = hostFontFamilyRegular,
        fontSize = 14.sp,
        fontWeight = FontWeight.Normal,
        letterSpacing = 0.sp,
        color = Color.White
    ),
    titleLarge = TextStyle(
        fontFamily = hostFontFamilyRegular,
        fontSize = 30.sp,
        fontWeight = FontWeight.Normal,
        letterSpacing = 0.sp,
        color = Color.White
    ),
    titleMedium = TextStyle(
        fontFamily = hostFontFamilyRegular,
        fontSize = 16.sp,
        fontWeight = FontWeight.Normal,
        letterSpacing = 0.sp,
        color = Color.White
    ),
    titleSmall = TextStyle(
        fontFamily = hostFontFamilyRegular,
        fontSize = 12.sp,
        fontWeight = FontWeight.Medium,
        letterSpacing = 0.sp,
        color = Color.White
    ),
    bodyLarge = TextStyle(
        fontFamily = hostFontFamilyRegular,
        fontSize = 14.sp,
        fontWeight = FontWeight.Normal,
        letterSpacing = 0.sp,
        color = Color.White
    ),
    bodyMedium = TextStyle(
        fontFamily = hostFontFamilyRegular,
        fontSize = 12.sp,
        fontWeight = FontWeight.Normal,
        letterSpacing = 0.sp,
        color = Color.White
    ),
    bodySmall = TextStyle(
        fontFamily = hostFontFamilyRegular,
        fontSize = 10.sp,
        fontWeight = FontWeight.Normal,
        letterSpacing = 0.sp,
        color = Color.White
    ),
    labelLarge = TextStyle(
        fontFamily = hostFontFamilyRegular,
        fontSize = 16.sp,
        fontWeight = FontWeight.Medium,
        letterSpacing = 0.sp,
        color = Color.White
    ),
    labelMedium = TextStyle(
        fontFamily = hostFontFamilyRegular,
        fontSize = 14.sp,
        fontWeight = FontWeight.Medium,
        letterSpacing = 0.sp,
        color = Color.White
    ),
    labelSmall = TextStyle(
        fontFamily = hostFontFamilyRegular,
        fontSize = 12.sp,
        fontWeight = FontWeight.Medium,
        letterSpacing = 0.sp,
        color = Color.White
    ),
)