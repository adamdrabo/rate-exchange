package com.adamdrabo.rateexchange.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.adamdrabo.rateexchange.ui.theme.AppColors
@Composable
fun MarketRateCard(
    tauxMarche: Double,
    trendPercentage: Double,
    isDarkTheme: Boolean
) {
    val isTrendPositive = trendPercentage >= 0.0
    val trendText = if (isTrendPositive) {
        "▲ ${String.format(java.util.Locale.CANADA, "%.1f", trendPercentage)} %"
    } else {
        "▼ ${String.format(java.util.Locale.CANADA, "%.1f", -trendPercentage)} %"
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surfaceVariant, shape = RoundedCornerShape(15.dp))
            .border(1.dp, MaterialTheme.colorScheme.outline, shape = RoundedCornerShape(15.dp))
            .padding(horizontal = 16.dp, vertical = 16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(
                    text = "Taux du marché",
                    style = TextStyle(
                        fontFamily = FontFamily.Default,
                        fontSize = 12.sp,
                        fontWeight = FontWeight(600)
                    ),
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = "1 CAD = ${"%.0f".format(tauxMarche)} XOF",
                    style = TextStyle(
                        fontFamily = FontFamily.Default,
                        fontSize = 22.sp,
                        fontWeight = FontWeight(750),
                        fontFeatureSettings = "tnum"
                    ),
                    color = MaterialTheme.colorScheme.onSurface
                )
            }

            Box(
                modifier = Modifier
                    .background(
                        color = if (isTrendPositive) AppColors.TrendUpBg else AppColors.TrendDownBg,
                        shape = RoundedCornerShape(8.dp)
                    )
                    .padding(horizontal = 10.dp, vertical = 6.dp)
            ) {
                Text(
                    text = trendText,
                    style = TextStyle(fontFamily = FontFamily.Default, fontSize = 13.sp, fontWeight = FontWeight(720)),
                    color = if (isTrendPositive) (if (isDarkTheme) AppColors.DarkGreen else AppColors.LightGreen) else AppColors.TrendDownText
                )
            }
        }
    }
}