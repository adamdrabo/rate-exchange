package com.adamdrabo.rateexchange

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.adamdrabo.rateexchange.data.datastore.ExchangeRateDataStore
import com.adamdrabo.rateexchange.data.datastore.ThemeManager
import com.adamdrabo.rateexchange.data.remote.RetrofitInstance
import com.adamdrabo.rateexchange.data.repository.CurrencyRepository
import com.adamdrabo.rateexchange.ui.screens.CurrencyScreen
import com.adamdrabo.rateexchange.ui.theme.RateExchangeTheme
import com.adamdrabo.rateexchange.ui.viewmodel.CurrencyViewModel
import com.adamdrabo.rateexchange.ui.viewmodel.factory.CurrencyViewModelFactory

class MainActivity : ComponentActivity() {

    private val exchangeRateDataStore by lazy { ExchangeRateDataStore(this) }
    private val currencyRepository by lazy { CurrencyRepository(RetrofitInstance.service, exchangeRateDataStore) }
    private val currencyViewModelFactory by lazy { CurrencyViewModelFactory(currencyRepository) }
    private val viewmodel: CurrencyViewModel by viewModels { currencyViewModelFactory }

    private val themeManager by lazy { ThemeManager(applicationContext) }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {

            val isDarkTheme by themeManager.isDarkThemeFlow.collectAsStateWithLifecycle(false)

            RateExchangeTheme(darkTheme = isDarkTheme) {
                CurrencyScreen(
                    viewModel = viewmodel,
                    themeManager = themeManager
                )
            }
        }
    }
}

