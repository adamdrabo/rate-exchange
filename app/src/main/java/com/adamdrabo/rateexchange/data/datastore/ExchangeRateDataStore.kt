package com.adamdrabo.rateexchange.data.datastore

import android.content.Context
import androidx.datastore.preferences.core.doublePreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.first

// Initialisation
val Context.dataStore by preferencesDataStore(name = "exchange_rate_prefs")
class ExchangeRateDataStore(private val context: Context) {

    object PreferencesKeys {
        val lastUpdateTimestamp = longPreferencesKey("last_update_timestamp")
        val cadRate = doublePreferencesKey(name = "cad_rate")
        val xofRate = doublePreferencesKey(name = "xof_rate")
    }

    suspend fun saveExchangeRate(
       lastUpdateTimestamp: Long,
       cadRate: Double,
       xofRate: Double
    ) {
        context.dataStore.edit { preferences ->
          preferences[PreferencesKeys.lastUpdateTimestamp] = lastUpdateTimestamp
          preferences[PreferencesKeys.cadRate] = cadRate
          preferences[PreferencesKeys.xofRate] = xofRate
        }
    }

    suspend fun readTimeStamp(): Long? {
        val preferences = context.dataStore.data.first()
        return preferences[PreferencesKeys.lastUpdateTimestamp]
    }

    suspend fun readExchangeRate(): Pair<Double?, Double?> {
        val preferences = context.dataStore.data.first()
        return Pair(preferences[PreferencesKeys.cadRate], preferences[PreferencesKeys.xofRate])

    }
}