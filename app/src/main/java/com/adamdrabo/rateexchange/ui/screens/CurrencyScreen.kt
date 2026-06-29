package com.adamdrabo.rateexchange.ui.screens

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.SwapHoriz
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.adamdrabo.rateexchange.ui.components.RateHistoryChart
import com.adamdrabo.rateexchange.ui.components.TransferCostRow
import com.adamdrabo.rateexchange.ui.state.CurrencyState
import com.adamdrabo.rateexchange.ui.state.HistoryState
import com.adamdrabo.rateexchange.ui.viewmodel.CurrencyViewModel

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun CurrencyScreen(
    viewModel: CurrencyViewModel
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val historyState by viewModel.historyData.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.fetchExchangeRates()
        viewModel.fetchRatesHistory()
    }

    var userAmount by remember { mutableStateOf("") }
    var isReversed by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF141414))
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        when (state) {
            is CurrencyState.Loading -> {
                CircularProgressIndicator(
                    modifier = Modifier.size(36.dp),
                    color = Color.LightGray
                )
            }

            is CurrencyState.Success -> {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0xFF222222), shape = RoundedCornerShape(24.dp))
                        .border(1.dp, Color(0xFF333333), shape = RoundedCornerShape(24.dp))
                        .padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    Text(
                        text = "Convertisseur",
                        color = Color.LightGray,
                        textAlign = TextAlign.Center,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Medium
                    )

                    val tauxCAD = (state as CurrencyState.Success).data.first { it.quote == "CAD" }.rate
                    val tauxXOF = (state as CurrencyState.Success).data.first { it.quote == "XOF" }.rate
                    val resultat = if (userAmount.isEmpty()) "0"
                    else if (isReversed) (userAmount.toDouble() / tauxXOF * tauxCAD).let { "%.2f".format(it) }
                    else (userAmount.toDouble() / tauxCAD * tauxXOF).let { "%.2f".format(it) }

                    val sourceLabel = if (isReversed) "Converti" else "Montant"
                    val targetLabel = if (isReversed) "Montant" else "Converti"
                    val sourceCurrency = if (isReversed) "XOF" else "CAD"
                    val targetCurrency = if (isReversed) "CAD" else "XOF"


                    val ratesPerDay: List<Pair<String, Double>> = if (historyState is HistoryState.Success) {
                        (historyState as HistoryState.Success).data
                            .groupBy { dto -> dto.date }
                            .mapNotNull { entry ->
                                val tCAD = entry.value.firstOrNull { it.quote == "CAD" }?.rate
                                val tXOF = entry.value.firstOrNull { it.quote == "XOF" }?.rate
                                if (tCAD != null && tXOF != null && tCAD != 0.0) {
                                    entry.key to (tXOF / tCAD)
                                } else {
                                    null
                                }
                            }
                    } else emptyList()

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .background(Color(0xFF1A1A1A), shape = RoundedCornerShape(14.dp))
                                .border(1.dp, Color(0xFF2E2E2E), shape = RoundedCornerShape(14.dp))
                                .padding(14.dp)
                        ) {
                            Column {
                                Text(text = sourceLabel, color = Color.Gray, fontSize = 13.sp)
                                Spacer(modifier = Modifier.height(4.dp))
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    BasicTextField(
                                        value = userAmount,
                                        onValueChange = { userAmount = it },
                                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                        textStyle = TextStyle(
                                            color = Color.White,
                                            fontSize = 20.sp,
                                            fontWeight = FontWeight.Bold
                                        ),
                                        modifier = Modifier.weight(1f),
                                        singleLine = true,
                                        cursorBrush = SolidColor(Color.White)
                                    )
                                    Text(text = sourceCurrency, color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                                }
                            }
                        }

                        IconButton(
                            onClick = { isReversed = !isReversed },
                            modifier = Modifier.size(32.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.SwapHoriz,
                                contentDescription = "Inverser",
                                tint = Color.White,
                                modifier = Modifier.size(24.dp)
                            )
                        }

                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .background(Color(0xFF1A1A1A), shape = RoundedCornerShape(14.dp))
                                .border(1.dp, Color(0xFF2E2E2E), shape = RoundedCornerShape(14.dp))
                                .padding(14.dp)
                        ) {
                            Column {
                                Text(text = targetLabel, color = Color.Gray, fontSize = 13.sp)
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(text = "$resultat $targetCurrency", color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Bold, maxLines = 1)
                            }
                        }
                    }

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color(0xFF1A1A1A), shape = RoundedCornerShape(14.dp))
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text(text = "Taux actuel", color = Color.Gray, fontSize = 14.sp)
                        Text(
                            text = "1 CAD = ${"%.0f".format(tauxXOF / tauxCAD)} XOF",
                            color = Color.White,
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    RateHistoryChart(ratesPerDay)

                    HorizontalDivider(color = Color(0xFF2E2E2E), thickness = 1.dp)

                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Text(
                            text = "Coût d'un transfert",
                            color = Color.White,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Start
                        )

                        TransferCostRow(name = "Wave", fee = "Frais: 1 200 XOF")
                        TransferCostRow(name = "Western Union", fee = "Frais: 4 500 XOF")
                    }
                }
            }

            is CurrencyState.Failure -> {
                Text(
                    text = (state as CurrencyState.Failure).message,
                    color = Color.Red,
                    fontSize = 18.sp
                )
            }
        }
    }
}