package com.adamdrabo.rateexchange.data.remote

import retrofit2.http.GET
import retrofit2.http.Query

interface CurrencyApiService {
    @GET(value = "rates")
    suspend fun getExchangeRates() : List<ExchangeRateDto>

    @GET(value = "rates")
    suspend fun getRatesHistory(
        @Query("from") from: String,
        @Query("quotes") quotes: String
    ): List<ExchangeRateHistoryDto>
}