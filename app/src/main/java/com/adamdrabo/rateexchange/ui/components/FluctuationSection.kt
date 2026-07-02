package com.adamdrabo.rateexchange.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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

@Composable
fun FluctuationSection(
    selectedPeriod: String,
    ratesPerDay: List<Pair<String, Double>>,
    isDarkTheme: Boolean,
    onPeriodSelected: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Fluctuation (7 jours)",
                style = TextStyle(
                    fontFamily = FontFamily.Default,
                    fontSize = 13.sp,
                    fontWeight = FontWeight(600)
                ),
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                listOf("7 J", "30 J", "1 AN").forEach { period ->
                    val isSelected = selectedPeriod == period
                    Box(
                        modifier = Modifier
                            .background(
                                color = if (isSelected) MaterialTheme.colorScheme.surfaceVariant else Color.Transparent,
                                shape = RoundedCornerShape(8.dp)
                            )
                            .clickable { onPeriodSelected(period) }
                            .padding(horizontal = 10.dp, vertical = 6.dp)
                    ) {
                        Text(
                            text = period,
                            style = TextStyle(fontFamily = FontFamily.Default, fontSize = 12.sp, fontWeight = FontWeight(700)),
                            color = if (isSelected) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
                        )
                    }
                }
            }
        }


        Box(modifier = Modifier.fillMaxWidth().height(90.dp)) {
            RateHistoryChart(ratesPerDay)
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "min 400 XOF",
                style = TextStyle(fontFamily = FontFamily.Default, fontSize = 11.5.sp, fontWeight = FontWeight(600)),
                color = if (isDarkTheme) AppColors.DarkDimText else AppColors.LightDimText
            )
            Text(
                text = "max 406 XOF",
                style = TextStyle(fontFamily = FontFamily.Default, fontSize = 11.5.sp, fontWeight = FontWeight(600)),
                color = if (isDarkTheme) AppColors.DarkDimText else AppColors.LightDimText
            )
        }
    }
}