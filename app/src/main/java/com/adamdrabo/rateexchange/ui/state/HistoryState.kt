package com.adamdrabo.rateexchange.ui.state

import com.adamdrabo.rateexchange.data.remote.ExchangeRateHistoryDto

sealed class HistoryState{
    data object Loading: HistoryState()
    data class Success(val data: List<ExchangeRateHistoryDto>) : HistoryState()
    data class Failure(val message: String) : HistoryState()
}
