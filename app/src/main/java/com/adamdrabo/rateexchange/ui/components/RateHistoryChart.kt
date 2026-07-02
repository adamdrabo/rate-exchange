package com.adamdrabo.rateexchange.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun  RateHistoryChart(
     ratesPerDay : List<Pair<String, Double>>
) {

    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {


        Canvas(
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
                .padding(vertical = 4.dp)
        ) {
            if (ratesPerDay.isNotEmpty()) {
                val rates = ratesPerDay.map { it.second }
                val minRate = rates.min()
                val maxRate = rates.max()
                val rangeRate = if (maxRate - minRate == 0.0) 1.0 else maxRate - minRate

                val path = Path()
                rates.forEachIndexed { index, rate ->
                    val x = (index.toFloat() / (rates.size - 1)) * size.width
                    val y = size.height - ((rate - minRate) / rangeRate * size.height).toFloat()
                    if (index == 0) path.moveTo(x, y) else path.lineTo(x, y)
                }

                drawPath(
                    path = path,
                    color = Color(0xFF2F80ED),
                    style = Stroke(width = 3.dp.toPx(), cap = StrokeCap.Round)
                )
            }
        }
    }
}