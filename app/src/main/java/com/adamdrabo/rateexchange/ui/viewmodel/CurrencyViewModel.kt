package com.adamdrabo.rateexchange.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.adamdrabo.rateexchange.data.repository.CurrencyRepository
import com.adamdrabo.rateexchange.ui.state.CurrencyState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CurrencyViewModel (
   private val repository: CurrencyRepository
): ViewModel() {
    private val _uiState = MutableStateFlow<CurrencyState>(value = CurrencyState.Loading)

    val uiState: StateFlow<CurrencyState> = _uiState.asStateFlow()

    fun fetchExchangeRates() {
        viewModelScope.launch {
           _uiState.update {
               CurrencyState.Loading
           }
           try {
              val rates =  repository.getExchangesRates()
               _uiState.update {
                   CurrencyState.Success(data = rates)
               }
           } catch (e: Exception) {
               _uiState.update {
                   CurrencyState.Failure(message = e.message ?: "Taux indisponible")
               }
           }
        }
    }
}