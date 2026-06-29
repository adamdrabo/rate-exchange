package com.adamdrabo.rateexchange.data.repository

import android.os.Build
import androidx.annotation.RequiresApi
import com.adamdrabo.rateexchange.data.datastore.ExchangeRateDataStore
import com.adamdrabo.rateexchange.data.remote.CurrencyApiService
import com.adamdrabo.rateexchange.data.remote.ExchangeRateDto
import com.adamdrabo.rateexchange.data.remote.ExchangeRateHistoryDto
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class CurrencyRepository(
   private val apiService: CurrencyApiService,
   private val dataStore: ExchangeRateDataStore
) {

    suspend fun getExchangesRates(): List<ExchangeRateDto> {

        val readTimeStamp = dataStore.readTimeStamp()
        val elapsedTime = System.currentTimeMillis()

        if( readTimeStamp == null || (elapsedTime - readTimeStamp) > 24 * 60 * 60 * 1000L) {

            val callApi = apiService.getExchangeRates().filter {
                it.quote == "CAD" || it.quote == "XOF"
            }

            dataStore.saveExchangeRate(
                lastUpdateTimestamp = elapsedTime,
                cadRate = callApi.first { it.quote == "CAD" }.rate,
                xofRate = callApi.first { it.quote == "XOF" }.rate
            )

            return callApi
        } else {
         val cache = dataStore.readExchangeRate()
         return  listOf(
         ExchangeRateDto(rate = cache.first ?: 0.0, base = "EUR", quote = "CAD"),
         ExchangeRateDto(rate = cache.second ?: 0.0, base = "EUR", quote = "XOF")
         )
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun getRatesHistory(): List<ExchangeRateHistoryDto> {

        val dateNow = LocalDate.now()
        val sevenDaysAgo = dateNow.minusDays(7)
        val formatsCurrentDate = sevenDaysAgo .format(
            DateTimeFormatter.ISO_LOCAL_DATE
        )

       return apiService.getRatesHistory(formatsCurrentDate, "CAD,XOF")
    }
}