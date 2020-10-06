package com.coinconverter.bankbtg.ui.viewmodel

import androidx.lifecycle.MutableLiveData
import com.coinconverter.bankbtg.R
import com.coinconverter.bankbtg.domain.model.CurrencyModel
import com.coinconverter.bankbtg.domain.model.QuotesModel
import com.coinconverter.bankbtg.integration.RetrofitConfig
import com.coinconverter.bankbtg.domain.responses.CurrencyResponse
import com.coinconverter.bankbtg.domain.responses.QuotesResponse
import com.coinconverter.bankbtg.utils.FunctionsUtils.showMessage
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CurrenciesViewModel: BasicViewModel() {

    var listCurrencies: MutableLiveData<List<CurrencyModel>> = MutableLiveData()
    var currencySelected: MutableLiveData<String> = MutableLiveData()
    var isUpdating = false

    fun getCurrencies() {

        val currenciesList = currenciesDataBase.getAll()
        if (currenciesList.isEmpty()) {
            callCurrencies()
        } else {
            listCurrencies.postValue(currenciesList)
            loading.postValue(false)
        }
    }

    fun getQuotes(){
        val quotesList = quotesDatabase.getAll()
        if (quotesList.isEmpty()) callQuotes()
    }

    fun callCurrencies() {
        if (isUpdating) loading.postValue(true)
        val call = RetrofitConfig().getAPI().getListCurrencies()
        call.enqueue(object : Callback<CurrencyResponse> {
            override fun onResponse(call: Call<CurrencyResponse?>, response: Response<CurrencyResponse>) {

                val respondeSucess= response.body()?.success
                if (respondeSucess!!){
                    val listCurrency = response.body()
                        ?.currencies?.map { CurrencyModel(it.key, it.value) }

                    listCurrency?.let {
                        currenciesDataBase.deleteAll()
                        currenciesDataBase.insertAll(it)
                        listCurrencies.postValue(it)
                    }
                }else{
                    val respondeError = response.body()?.error
                    showMessage(context, context.getString(R.string.request_failed,respondeError?.code, respondeError?.info ))
                }
                if (isUpdating){
                    showMessage(context,context.getString(R.string.update_sucess))
                    isUpdating= false
                }
                loading.postValue(false)
            }

            override fun onFailure(call: Call<CurrencyResponse>?, t: Throwable) {
                val listCurrency= currenciesDataBase.getAll()
                listCurrencies.postValue(listCurrency)
                if (listCurrency.isEmpty())
                    showMessage(context, context.getString(R.string.load_failed_offline))
                else
                    showMessage(context, context.getString(R.string.load_failed))

                loading.postValue(false)
            }
        })
    }

    fun callQuotes(){
        val call = RetrofitConfig().getAPI().getLive()
        call.enqueue(object : Callback<QuotesResponse>{
            override fun onResponse(call: Call<QuotesResponse>, response: Response<QuotesResponse>) {
                val respondeSucess= response.body()?.success
                if (respondeSucess!!){
                val listQuotes = response.body()
                    ?.quotes?.map { QuotesModel(it.key, it.value) }
                listQuotes?.let {
                    quotesDatabase.deleteAll()
                    quotesDatabase.insertAll(it)
                }
                }else{
                    val respondeError = response.body()?.error
                    showMessage(context, context.getString(R.string.request_failed,respondeError?.code, respondeError?.info ))
                }
            }

            override fun onFailure(call: Call<QuotesResponse>, t: Throwable) {
                t.message?.let { showMessage(context, it) }
            }
        })
    }

    fun onSelectCurrency(currenciesCode: String){
        currencySelected.postValue(currenciesCode)
    }
}