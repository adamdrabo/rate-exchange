package com.adamdrabo.rateexchange.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

 val CustomDarkColorScheme = darkColorScheme(
    background = AppColors.DarkBackground,
    surface = AppColors.DarkCard,
    surfaceVariant = AppColors.DarkInput,
    onBackground = AppColors.DarkText,
    onSurface = AppColors.DarkText,
    onSurfaceVariant = AppColors.DarkSubText,
    outline = AppColors.DarkBorder,
    primary = AppColors.BlueAccent,
    secondary = AppColors.DarkGreen
)

val CustomLightColorScheme = lightColorScheme(
    background = AppColors.LightBackground,
    surface = AppColors.LightCard,
    surfaceVariant = AppColors.LightInput,
    onBackground = AppColors.LightText,
    onSurface = AppColors.LightText,
    onSurfaceVariant = AppColors.LightSubText,
    outline = AppColors.LightBorder,
    primary = AppColors.BlueAccent,
    secondary = AppColors.LightGreen
)

@Composable
fun RateExchangeTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> CustomDarkColorScheme
        else -> CustomLightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = AppTypography,
        content = content
    )
}