package com.coinconverter.bankbtg.ui.viewmodel

import androidx.lifecycle.MutableLiveData
import com.coinconverter.bankbtg.R
import com.coinconverter.bankbtg.domain.model.CurrencyModel
import com.coinconverter.bankbtg.domain.model.QuotesModel
import com.coinconverter.bankbtg.utils.FunctionsUtils
import com.coinconverter.bankbtg.utils.FunctionsUtils.doubleToString
import com.coinconverter.bankbtg.utils.FunctionsUtils.stringReplaceComma
import com.coinconverter.bankbtg.utils.INPUT_CURRENCY
import com.coinconverter.bankbtg.utils.OUTPUT_CURRENCY

class MainViewModel: BasicViewModel() {

    var inputCurrencies: MutableLiveData<CurrencyModel> = MutableLiveData()
    var outputCurrencies: MutableLiveData<CurrencyModel> = MutableLiveData()
    var inputQuotes: MutableLiveData<QuotesModel> = MutableLiveData()
    var outputQuotes: MutableLiveData<QuotesModel> = MutableLiveData()
    var enableConvert: MutableLiveData<Boolean> = MutableLiveData()
    var convertValue: MutableLiveData<String> = MutableLiveData()

    private var hasInputCurrencies = false
    private var hasOutputCurrencies = false

    fun onCurrencySelected(flow: Int?, code: String?){
        val currency = code?.let { currenciesDataBase.findByCode(it) }

        when(flow){
            INPUT_CURRENCY -> {
                hasInputCurrencies = true
                inputCurrencies.postValue(currency)
                val quote = quotesDatabase.findByCode("USD$code")
                inputQuotes.postValue(quote)
                enableButton()
            }
            OUTPUT_CURRENCY -> {
                outputCurrencies.postValue(currency)
                val quote = quotesDatabase.findByCode("USD$code")
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
            FunctionsUtils.showMessage(context, context.getString(R.string.enter_value))
        } else {

            val inputValue = (stringReplaceComma(inputText)).toDouble()

            if(inputValue < 0){
                FunctionsUtils.showMessage(context, context.getString(R.string.enter_value))
            } else {
                val qInput = inputQuotes.value?.value
                val qOutput = outputQuotes.value?.value
                val finalValue = (inputValue / qInput!!) * qOutput!!
                convertValue.postValue(doubleToString(finalValue))
            }
        }
    }

    fun setStartInfo(){
        if(quotesDatabase.getAll().isNotEmpty() && currenciesDataBase.getAll().isNotEmpty()){
            inputCurrencies.postValue(currenciesDataBase.findByCode("BRL"))
            inputQuotes.postValue(quotesDatabase.findByCode("USDBRL"))

            outputCurrencies.postValue(currenciesDataBase.findByCode("USD"))
            outputQuotes.postValue(quotesDatabase.findByCode("USDUSD"))

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