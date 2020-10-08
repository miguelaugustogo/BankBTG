package com.coinconverter.bankbtg.ui.viewmodel

import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.coinconverter.bankbtg.R
import com.coinconverter.bankbtg.data.db.model.CurrencyModel
import com.coinconverter.bankbtg.data.db.model.QuotesModel
import com.coinconverter.bankbtg.utils.FunctionsUtils
import com.coinconverter.bankbtg.utils.FunctionsUtils.doubleToString
import com.coinconverter.bankbtg.utils.FunctionsUtils.stringReplaceComma
import com.coinconverter.bankbtg.utils.INPUT_CURRENCY
import com.coinconverter.bankbtg.utils.OUTPUT_CURRENCY
import com.desafio.btgpactual.repositories.CurrencyRepository
import com.desafio.btgpactual.repositories.QuoteRepository

class MainViewModel(
    private val repositoryCurrency: CurrencyRepository,
    private val repositoryQuote: QuoteRepository
) : ViewModel() {

    var pleaseEnterValue: MutableLiveData<Boolean> = MutableLiveData()
    var inputCurrencies: MutableLiveData<CurrencyModel> = MutableLiveData()
    var outputCurrencies: MutableLiveData<CurrencyModel> = MutableLiveData()
    var inputQuotes: MutableLiveData<QuotesModel> = MutableLiveData()
    var outputQuotes: MutableLiveData<QuotesModel> = MutableLiveData()
    var enableConvert: MutableLiveData<Boolean> = MutableLiveData()
    var convertValue: MutableLiveData<String> = MutableLiveData()

    private var hasInputCurrencies = false
    private var hasOutputCurrencies = false

    fun onCurrencySelected(flow: Int?, code: String?){
        val currency = code?.let { repositoryCurrency.findByCode(it) }

        when(flow){
            INPUT_CURRENCY -> {
                hasInputCurrencies = true
                inputCurrencies.postValue(currency)
                val quote = repositoryQuote.findByCode("USD$code")
                inputQuotes.postValue(quote)
                enableButton()
            }
            OUTPUT_CURRENCY -> {
                outputCurrencies.postValue(currency)
                val quote = repositoryQuote.findByCode("USD$code")
                outputQuotes.postValue(quote)
                hasOutputCurrencies = true
                enableButton()
            }
        }
    }

    private fun enableButton(){
        if(hasInputCurrencies && hasOutputCurrencies)
            enableConvert.postValue(true)
    }

    fun doConvert(inputText: String){
        if(inputText.isEmpty()){
//            FunctionsUtils.showMessage(context, getString(R.string.enter_value))
            pleaseEnterValue.postValue(true)
        } else {

            val inputValue = (stringReplaceComma(inputText)).toDouble()

            if(inputValue < 0){
//                FunctionsUtils.showMessage(context, context.getString(R.string.enter_value))
                pleaseEnterValue.postValue(true)
            } else {
                val qInput = inputQuotes.value?.value
                val qOutput = outputQuotes.value?.value
                val finalValue = (inputValue / qInput!!) * qOutput!!
                convertValue.postValue(doubleToString(finalValue))
            }
        }
    }

    fun setStartInfo(){
        if(repositoryQuote.getAll().isNotEmpty() && repositoryCurrency.getAll().isNotEmpty()){
            inputCurrencies.postValue(repositoryCurrency.findByCode("BRL"))
            inputQuotes.postValue(repositoryQuote.findByCode("USDBRL"))

            outputCurrencies.postValue(repositoryCurrency.findByCode("USD"))
            outputQuotes.postValue(repositoryQuote.findByCode("USDUSD"))

            enableConvert.postValue(true)
        }
    }

    fun invertCoins(){
        val inputCurrencyModel = inputCurrencies.value
        val outputCurrencyModel = outputCurrencies.value
        val inputQuotesModel = inputQuotes.value
        val outputQuotesModel = outputQuotes.value
        inputCurrencies.postValue(outputCurrencyModel)
        outputCurrencies.postValue(inputCurrencyModel)
        inputQuotes.postValue(outputQuotesModel)
        outputQuotes.postValue(inputQuotesModel)
    }

}