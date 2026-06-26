package com.adamdrabo.rateexchange.data.repository

import com.adamdrabo.rateexchange.data.datastore.ExchangeRateDataStore
import com.adamdrabo.rateexchange.data.remote.CurrencyApiService
import com.adamdrabo.rateexchange.data.remote.ExchangeRateDto

class CurrencyRepository(
   private val apiService: CurrencyApiService,
   private val dataStore: ExchangeRateDataStore
) {

    /*
    Vérifier si 24h se sont écoulées
    Si oui → appeler l'API, filtrer, mettre en cache, retourner
    Si non → retourner le cache
     */
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
}