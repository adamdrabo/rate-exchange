package com.adamdrabo.rateexchange

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import com.adamdrabo.rateexchange.data.datastore.ExchangeRateDataStore
import com.adamdrabo.rateexchange.data.remote.RetrofitInstance
import com.adamdrabo.rateexchange.data.repository.CurrencyRepository
import com.adamdrabo.rateexchange.ui.screens.CurrencyScreen
import com.adamdrabo.rateexchange.ui.theme.RateExchangeTheme
import com.adamdrabo.rateexchange.ui.viewmodel.CurrencyViewModel
import com.adamdrabo.rateexchange.ui.viewmodel.factory.CurrencyViewModelFactory

class MainActivity : ComponentActivity() {

    val exchangeRateDataStore by lazy { ExchangeRateDataStore(this) }
    val currencyRepository by lazy { CurrencyRepository(RetrofitInstance.service, exchangeRateDataStore) }
    val currencyViewModelFactory by lazy { CurrencyViewModelFactory(currencyRepository) }
    val viewmodel: CurrencyViewModel by viewModels { currencyViewModelFactory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()


        setContent {
            RateExchangeTheme {
                CurrencyScreen(viewmodel)
            }
        }
    }
}

