package com.adamdrabo.rateexchange.ui.theme

import androidx.compose.ui.graphics.Color

object AppColors {
    // Mode Sombre
    val DarkBackground = Color(0xFF0A0C0F)
    val DarkCard = Color(0xFF16191F)
    val DarkInput = Color(0xFF1C2027)
    val DarkText = Color(0xFFF4F6FA)
    val DarkSubText = Color(0xFF8A94A6)
    val DarkDimText = Color(0xFF7B8494)
    val DarkGreen = Color(0xFF35D07F)
    val DarkBorder = Color(0xFFFFFFFF).copy(alpha = 0.07f)

    // Mode Clair
    val LightBackground = Color(0xFFEEF0F5)
    val LightCard = Color(0xFFFFFFFF)
    val LightInput = Color(0xFFF4F6F9)
    val LightText = Color(0xFF0E1116)
    val LightSubText = Color(0xFF5A6472)
    val LightDimText = Color(0xFF8A93A1)
    val LightGreen = Color(0xFF10A055)
    val LightBorder = Color(0xFF000000).copy(alpha = 0.07f)

    // Accents Communs
    val BlueAccent = Color(0xFF4C82FB)
    val CadDot = Color(0xFFE15554)
    val XofDot = Color(0xFFF4B740)

    // Tendances
    val TrendUpBg = Color(0xFF35D07F).copy(alpha = 0.13f)
    val TrendDownText = Color(0xFFF2635A)
    val TrendDownBg = Color(0xFFF2635A).copy(alpha = 0.13f)
}