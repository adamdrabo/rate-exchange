package com.adamdrabo.rateexchange.ui.viewmodel

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.adamdrabo.rateexchange.data.ServiceUiModel
import com.adamdrabo.rateexchange.data.repository.CurrencyRepository
import com.adamdrabo.rateexchange.ui.state.CurrencyState
import com.adamdrabo.rateexchange.ui.state.HistoryState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

private data class ProviderConfig(
    val name: String,
    val fixedFee: Double,
    val rateMargin: Double,
    val deliveryTime: String
)
class CurrencyViewModel (
   private val repository: CurrencyRepository
): ViewModel() {
    private val _uiState = MutableStateFlow<CurrencyState>(value = CurrencyState.Loading)
    val uiState: StateFlow<CurrencyState> = _uiState.asStateFlow()
    private val _historyState = MutableStateFlow<HistoryState>(HistoryState.Loading)
    val historyData: StateFlow<HistoryState> = _historyState.asStateFlow()

    private val _serviceState = MutableStateFlow<List<ServiceUiModel>>(emptyList())
    val serviceState: StateFlow<List<ServiceUiModel>> = _serviceState.asStateFlow()

    fun fetchExchangeRates() {
        viewModelScope.launch {
           _uiState.update {
               CurrencyState.Loading
           }
           try {
              val rates =  repository.getExchangesRates()
               _uiState.update {
                   CurrencyState.Success(data = rates)
               }
           } catch (e: Exception) {
               _uiState.update {
                   CurrencyState.Failure(message = e.message ?: "Taux indisponible")
               }
           }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun fetchRatesHistory(period: String) {
        viewModelScope.launch {
            _historyState.update {
                HistoryState.Loading
            }

            val daysToMinus = when(period) {
                "7 J" -> 7L
                "30 J" -> 30L
                "1 AN" -> 365L
                else -> 7L
            }

            try {
                val historyRates = repository.getRatesHistory(daysToMinus)

                _historyState.update {
                    HistoryState.Success(historyRates)
                }
            } catch (e: Exception) {
                _historyState.update {
                    HistoryState.Failure(
                        message = e.message ?:
                    "Taux indisponibles pour la période : $period")
                }
            }
        }
    }



    fun updateTransfertServices(
        amountString: String,
        isReversed: Boolean,
        currentMarketRate: Double
    ) {
        val amount = amountString.toDoubleOrNull() ?: 0.0

        if (amount <= 0.0 || currentMarketRate <= 0.0) {
            _serviceState.value = emptyList()
            return
        }

        val sourceCurrency = if (isReversed) "XOF" else "CAD"
        val targetCurrency = if (isReversed) "CAD" else "XOF"

        val providers = if (!isReversed) {
            listOf(
                ProviderConfig(
                    "Taptap Send",
                    fixedFee = 0.0,
                    rateMargin = 0.012,
                    deliveryTime = "Instantané"
                ),
                ProviderConfig(
                    "Wave Mobile Money",
                    fixedFee = 0.0,
                    rateMargin = 0.01,
                    deliveryTime = "Instantané"
                ),
                ProviderConfig(
                    "Wise",
                    fixedFee = 1.50,
                    rateMargin = 0.0,
                    deliveryTime = "~ 1 heure"
                ),
                ProviderConfig(
                    "Western Union",
                    fixedFee = 2.99,
                    rateMargin = 0.02,
                    deliveryTime = "En quelques minutes"
                )
            )
        } else {
            listOf(
                ProviderConfig(
                    "Wave Mobile Money",
                    fixedFee = 0.0,
                    rateMargin = 0.01,
                    deliveryTime = "Instantané"
                ),
                ProviderConfig(
                    "Wise",
                    fixedFee = 1200.0,
                    rateMargin = 0.0,
                    deliveryTime = "~ 1 jour"
                ),
                ProviderConfig(
                    "Western Union",
                    fixedFee = 2500.0,
                    rateMargin = 0.02,
                    deliveryTime = "En quelques minutes"
                )
            )
        }

        val simulatedServices = providers.map { provider ->
            val providerRate = if (!isReversed) {
                currentMarketRate * (1.0 - provider.rateMargin)
            } else {
                currentMarketRate * (1.0 + provider.rateMargin)
            }

            val netAmount = amount - provider.fixedFee

            val totalReceived = if (netAmount > 0) {
                if (!isReversed) netAmount * providerRate else netAmount / providerRate
            } else {
                0.0
            }

            ServiceUiModel(
                name = provider.name,
                meta = "${provider.deliveryTime} · frais " +
                "${String.format(java.util.Locale.CANADA, "%,.0f", provider.fixedFee)} $sourceCurrency",
                receivedAmount = totalReceived,
                currency = targetCurrency,
                isBest = false
            )
        }

        val maxReceived = simulatedServices.maxOfOrNull { it.receivedAmount } ?: 0.0

        _serviceState.value = simulatedServices.map { service ->
            service.copy(isBest = service.receivedAmount == maxReceived && maxReceived > 0.0)
        }
    }
}
