package com.coinconverter.bankbtg.ui.viewmodel.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.coinconverter.bankbtg.ui.viewmodel.MainViewModel
import com.desafio.btgpactual.repositories.CurrencyRepository
import com.desafio.btgpactual.repositories.QuoteRepository


@Suppress("UNCHECKED_CAST")
class MainFactory(
    private val currencyRepository: CurrencyRepository,
    private val quoteRepository: QuoteRepository

) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return MainViewModel(currencyRepository, quoteRepository) as T
    }
}