package com.adamdrabo.rateexchange.data.remote

import retrofit2.http.GET

interface CurrencyApiService {
    @GET(value = "rates")
    suspend fun getExchangeRates() : List<ExchangeRateDto>
}