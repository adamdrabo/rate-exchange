package com.adamdrabo.rateexchange.data.remote

import com.google.gson.annotations.SerializedName

data class ExchangeRateHistoryDto(
    val date: String,
    val rate: Double,
    val base: String,
    val quote: String
)
