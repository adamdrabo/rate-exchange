package com.adamdrabo.rateexchange.data.remote

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


object RetrofitInstance {
    const val BASE_URL = "https://api.frankfurter.dev/v2/"

    // lazy create only when needed
    val service : CurrencyApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(CurrencyApiService::class.java)
    }
}