package com.adamdrabo.rateexchange.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.adamdrabo.rateexchange.ui.theme.AppColors

import java.util.Locale

@Composable
fun ServiceRowItem(
    name: String,
    meta: String,
    isBest: Boolean,
    receivedAmount: Double,
    targetCurrency: String,
    isDarkTheme: Boolean
) {

    val formattedAmount = if (targetCurrency == "XOF") {
        String.format(Locale.FRANCE, "%,.0f", receivedAmount)
    } else {
        String.format(Locale.FRANCE, "%,.2f", receivedAmount)
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = name,
                    style = TextStyle(fontFamily = FontFamily.Default, fontSize = 15.sp, fontWeight = FontWeight(650)),
                    color = MaterialTheme.colorScheme.onSurface
                )
                if (isBest) {
                    Box(
                        modifier = Modifier
                            .background(
                                color = if (isDarkTheme) AppColors.DarkGreen else AppColors.LightGreen,
                                shape = RoundedCornerShape(6.dp)
                            )
                            .padding(horizontal = 6.dp, vertical = 3.dp)
                    ) {
                        Text(
                            text = "MEILLEUR",
                            style = TextStyle(
                                fontFamily = FontFamily.Default,
                                fontSize = 9.sp,
                                fontWeight = FontWeight(800),
                                letterSpacing = 0.5.sp
                            ),
                            color = if (isDarkTheme) AppColors.DarkBackground else Color.White
                        )
                    }
                }
            }
            Text(
                text = meta,
                style = TextStyle(fontFamily = FontFamily.Default, fontSize = 12.sp, fontWeight = FontWeight(500)),
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        Text(
            text = "$formattedAmount $targetCurrency",
            style = TextStyle(
                fontFamily = FontFamily.Default,
                fontSize = 16.sp,
                fontWeight = FontWeight(750),
                fontFeatureSettings = "tnum"
            ),
            color = if (isBest) (if (isDarkTheme) AppColors.DarkGreen else AppColors.LightGreen) else MaterialTheme.colorScheme.onSurface
        )
    }
}