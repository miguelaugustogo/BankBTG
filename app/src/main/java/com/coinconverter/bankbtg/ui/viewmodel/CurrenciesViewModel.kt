package com.coinconverter.bankbtg.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.coinconverter.bankbtg.data.db.model.CurrencyModel
import com.coinconverter.bankbtg.data.db.model.QuotesModel
import com.desafio.btgpactual.repositories.CurrencyRepository
import com.desafio.btgpactual.repositories.QuoteRepository

class CurrenciesViewModel(
    private val currencyRepository: CurrencyRepository,
    private val quoteRepository: QuoteRepository
) : ViewModel() {

    var listCurrencies: MutableLiveData<List<CurrencyModel>> = MutableLiveData()
    var currencySelected: MutableLiveData<String> = MutableLiveData()
    var loading: MutableLiveData<Boolean> = MutableLiveData()
    var isUpdating = false

    fun callCurrencies(): LiveData<List<CurrencyModel>> {
        val liveData = MutableLiveData<List<CurrencyModel>>()
        currencyRepository.callCurrencies(
            sucess = {
                val codes = it
                    .sortedBy { currencyModel ->
                        currencyModel.code
                    }
                liveData.value = codes
                listCurrencies.postValue(codes)
                loading.postValue(false)
            },
            error = {}
        )
        return liveData
    }

    fun callQuotes(): LiveData<List<QuotesModel>> {
        val liveData = MutableLiveData<List<QuotesModel>>()
        quoteRepository.callQuotes(
            sucess = {
                liveData.value = it
            }, error =  {

            }
        )
        return liveData
    }

    fun onSelectCurrency(currenciesCode: String){
        currencySelected.postValue(currenciesCode)
    }
}