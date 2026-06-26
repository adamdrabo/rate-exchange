package com.adamdrabo.rateexchange.ui.components


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.sp

@Composable
fun TransferCostRow(name: String, fee: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween, // Pousse le nom à gauche et les frais à droite
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = name,
            fontSize = 18.sp,
            fontFamily = FontFamily.Serif
        )
        Text(
            text = fee,
            fontSize = 18.sp,
            fontFamily = FontFamily.Serif
        )
    }
}