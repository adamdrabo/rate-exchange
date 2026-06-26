package com.adamdrabo.rateexchange.ui.state

import com.adamdrabo.rateexchange.data.remote.ExchangeRateDto

sealed class CurrencyState {
    data object Loading: CurrencyState()
    data class Success(val data: List<ExchangeRateDto>) : CurrencyState()
    data class Failure(val message: String) : CurrencyState()
}