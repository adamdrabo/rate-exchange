package com.adamdrabo.rateexchange.data

data class ServiceUiModel(
    val name: String,
    val meta: String,
    val receivedAmount: Double,
    val currency: String,
    val isBest: Boolean = false
)