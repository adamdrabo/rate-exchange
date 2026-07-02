package com.adamdrabo.rateexchange.ui.screens

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.adamdrabo.rateexchange.data.datastore.ThemeManager
import com.adamdrabo.rateexchange.ui.components.CurrencyConverterInputs
import com.adamdrabo.rateexchange.ui.components.CurrencyHeader
import com.adamdrabo.rateexchange.ui.components.FluctuationSection
import com.adamdrabo.rateexchange.ui.components.MarketRateCard
import com.adamdrabo.rateexchange.ui.components.ServiceRowItem
import com.adamdrabo.rateexchange.ui.state.CurrencyState
import com.adamdrabo.rateexchange.ui.state.HistoryState
import com.adamdrabo.rateexchange.ui.theme.AppColors
import com.adamdrabo.rateexchange.ui.viewmodel.CurrencyViewModel
import kotlinx.coroutines.launch

@SuppressLint("DefaultLocale")
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun CurrencyScreen(
    viewModel: CurrencyViewModel,
    themeManager: ThemeManager
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val historyState by viewModel.historyData.collectAsStateWithLifecycle()
    val services by viewModel.serviceState.collectAsStateWithLifecycle()
    val isDarkTheme by themeManager.isDarkThemeFlow.collectAsStateWithLifecycle(false)
    val scope = rememberCoroutineScope()

    var userAmount by remember { mutableStateOf("100") }
    var isReversed by remember { mutableStateOf(false) }
    var selectedPeriod by remember { mutableStateOf("7 J") }

    LaunchedEffect(Unit) {
        viewModel.fetchExchangeRates()
    }

    LaunchedEffect(selectedPeriod) {
        viewModel.fetchRatesHistory(selectedPeriod)
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface)
    ) {
        when (state) {
            is CurrencyState.Loading -> {
                CircularProgressIndicator(
                    modifier = Modifier.size(36.dp).align(Alignment.Center),
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            is CurrencyState.Success -> {
                val successData = (state as CurrencyState.Success).data
                val tauxCAD = successData.firstOrNull { it.quote == "CAD" }?.rate ?: 1.0
                val tauxXOF = successData.firstOrNull { it.quote == "XOF" }?.rate ?: 1.0

                val tauxMarche = tauxXOF / tauxCAD
                val resultat = if (userAmount.isEmpty()) "0"
                else if (isReversed) (userAmount.toDoubleOrNull() ?: 0.0).let { (it / tauxXOF * tauxCAD).let { "%.2f".format(it) } }
                else (userAmount.toDoubleOrNull() ?: 0.0).let { (it / tauxCAD * tauxXOF).let { "%.0f".format(it) } }

                val sourceLabel = if (isReversed) "Converti" else "Montant"
                val targetLabel = if (isReversed) "Montant" else "Converti"
                val sourceCurrency = if (isReversed) "XOF" else "CAD"
                val targetCurrency = if (isReversed) "CAD" else "XOF"

                LaunchedEffect(userAmount, isReversed, tauxMarche) {
                    val effectiveAmount = userAmount.ifBlank {
                        if (isReversed) "50000" else "100"
                    }
                    viewModel.updateTransfertServices(effectiveAmount, isReversed, tauxMarche)
                }

                val ratesPerDay: List<Pair<String, Double>> = if (historyState is HistoryState.Success) {
                    (historyState as HistoryState.Success).data
                        .groupBy { dto -> dto.date }
                        .mapNotNull { entry ->
                            val tCAD = entry.value.firstOrNull { it.quote == "CAD" }?.rate
                            val tXOF = entry.value.firstOrNull { it.quote == "XOF" }?.rate
                            if (tCAD != null && tXOF != null && tCAD != 0.0) {
                                entry.key to (tXOF / tCAD)
                            } else null
                        }
                } else emptyList()

                val trendPercentage = if (ratesPerDay.isNotEmpty()) {
                    val ancienTaux = ratesPerDay.first().second
                    if (ancienTaux > 0.0) {
                        ((tauxMarche - ancienTaux) / ancienTaux) * 100.0
                    } else {
                        0.0
                    }
                } else {
                    0.0
                }

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .statusBarsPadding()
                        .navigationBarsPadding()
                        .verticalScroll(rememberScrollState())
                        .padding(horizontal = 16.dp, vertical = 24.dp),
                    verticalArrangement = Arrangement.spacedBy(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    CurrencyHeader(
                        isDarkTheme = isDarkTheme,
                        onThemeToggle = { scope.launch { themeManager.saveThemePreference(!isDarkTheme) } }
                    )


                    CurrencyConverterInputs(
                        userAmount = userAmount,
                        resultat = resultat,
                        sourceLabel = sourceLabel,
                        targetLabel = targetLabel,
                        sourceCurrency = sourceCurrency,
                        targetCurrency = targetCurrency,
                        onAmountChange = { userAmount = it },
                        onSwapClick = {
                            isReversed = !isReversed
                            userAmount = if (isReversed) "50000" else "100"
                        }
                    )


                    MarketRateCard(
                        tauxMarche = tauxMarche,
                        trendPercentage = trendPercentage,
                        isDarkTheme = isDarkTheme
                    )


                    FluctuationSection(
                        selectedPeriod = selectedPeriod,
                        ratesPerDay = ratesPerDay,
                        isDarkTheme = isDarkTheme,
                        onPeriodSelected = { selectedPeriod = it }
                    )


                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.Bottom
                        ) {
                            Text(
                                text = "Meilleur moyen d'envoyer",
                                style = TextStyle(fontFamily = FontFamily.Default, fontSize = 15.sp, fontWeight = FontWeight(700)),
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = "frais inclus",
                                style = TextStyle(fontFamily = FontFamily.Default, fontSize = 11.5.sp, fontWeight = FontWeight(500)),
                                color = if (isDarkTheme) AppColors.DarkDimText else AppColors.LightDimText
                            )
                        }

                        services.forEachIndexed { idx, service ->
                            Column(modifier = Modifier.fillMaxWidth()) {
                                ServiceRowItem(
                                    name = service.name,
                                    meta = service.meta,
                                    isBest = service.isBest,
                                    receivedAmount = service.receivedAmount,
                                    targetCurrency = targetCurrency,
                                    isDarkTheme = isDarkTheme
                                )

                                if (idx < services.lastIndex) {
                                    HorizontalDivider(
                                        color = MaterialTheme.colorScheme.outline,
                                        thickness = 1.dp
                                    )
                                }
                            }
                        }
                    }
                }
            }

            is CurrencyState.Failure -> {
                Text(
                    text = (state as CurrencyState.Failure).message,
                    color = Color.Red,
                    fontSize = 18.sp,
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        }
    }
}