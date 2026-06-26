package com.adamdrabo.rateexchange.data.remote

import com.google.gson.annotations.SerializedName

data class ExchangeRateDto(
    val rate: Double,
    val base: String,
    @SerializedName(value = "quote")
    val quote: String
)
