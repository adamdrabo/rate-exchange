package com.adamdrabo.rateexchange.ui.viewmodel.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.adamdrabo.rateexchange.data.repository.CurrencyRepository
import com.adamdrabo.rateexchange.ui.viewmodel.CurrencyViewModel

class CurrencyViewModelFactory (
    private val repository: CurrencyRepository
): ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {

        if (modelClass.isAssignableFrom(CurrencyViewModel::class.java)) {
            return CurrencyViewModel(repository) as T
        }

        throw IllegalArgumentException("ViewModel introuvable")
    }
}
