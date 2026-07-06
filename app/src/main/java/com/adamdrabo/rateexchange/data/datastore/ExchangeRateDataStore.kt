package com.adamdrabo.rateexchange.data.datastore

import android.content.Context
import androidx.datastore.preferences.core.doublePreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.first
import okhttp3.internal.applyConnectionSpec

// Initialisation
val Context.dataStore by preferencesDataStore(name = "exchange_rate_prefs")
class ExchangeRateDataStore(private val context: Context) {

    object PreferencesKeys {
        val lastUpdateTimestamp = longPreferencesKey("last_update_timestamp")
        val cadRate = doublePreferencesKey(name = "cad_rate")
        val xofRate = doublePreferencesKey(name = "xof_rate")

        val lastHistoryTimestamp = longPreferencesKey("last_history_timestamp")
        val historyData = stringPreferencesKey("history_data")
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

    suspend fun saveHistoryData(timestamp: Long, historyJson: String) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.lastHistoryTimestamp] = timestamp
            preferences[PreferencesKeys.historyData] = historyJson
        }
    }

    suspend fun readHistoryData(): Pair<Long?, String?> {
        val preferences = context.dataStore.data.first()
        return Pair(
            preferences[PreferencesKeys.lastHistoryTimestamp],
            preferences[PreferencesKeys.historyData]
        )
    }
}