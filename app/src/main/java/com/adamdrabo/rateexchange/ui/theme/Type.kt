package com.adamdrabo.rateexchange.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

// Set of Material typography styles to start with
val AppTypography = Typography(
    // Titre "Convertisseur"
    titleLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontSize = 19.sp,
        fontWeight = FontWeight(680),
        letterSpacing = (-0.3).sp
    ),
    // Montants saisis et convertis (avec Chiffres Tabulaires)
    bodyLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontSize = 21.sp,
        fontWeight = FontWeight(720),
        letterSpacing = (-0.4).sp,
        fontFeatureSettings = "tnum"
    ),
    // Labels ("Montant", "Converti") et Fluctuation
    labelMedium = TextStyle(
        fontFamily = FontFamily.Default,
        fontSize = 12.sp,
        fontWeight = FontWeight(600)
    ),
    // Titre "Meilleur moyen d'envoyer"
    titleMedium = TextStyle(
        fontFamily = FontFamily.Default,
        fontSize = 15.sp,
        fontWeight = FontWeight(700)
    )
)