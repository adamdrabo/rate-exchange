package com.adamdrabo.rateexchange.data.datastore

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.datastore by preferencesDataStore(name = "settings")

class ThemeManager(private val context: Context) {
    companion object {
        val DARK_THEME_KEY = booleanPreferencesKey("dark_theme_enabled")
    }

    val isDarkThemeFlow: Flow<Boolean> = context.datastore.data.map { preferences ->
        preferences[DARK_THEME_KEY] ?: false
    }

    suspend fun saveThemePreference(isDark: Boolean) {
        context.datastore.edit { preferences ->
            preferences[DARK_THEME_KEY] = isDark
        }
    }
}