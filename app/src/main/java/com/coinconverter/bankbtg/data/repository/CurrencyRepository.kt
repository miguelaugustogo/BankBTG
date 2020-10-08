package com.desafio.btgpactual.repositories

import android.util.Log
import com.coinconverter.bankbtg.data.api.RetrofitApi
import com.coinconverter.bankbtg.data.api.RetrofitService
import com.coinconverter.bankbtg.data.db.dao.CurrencyDao
import com.coinconverter.bankbtg.data.db.model.CurrencyModel
import com.coinconverter.bankbtg.data.repository.responses.CurrencyResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CurrencyRepository(
    private val currencyDao: CurrencyDao,
    private val service: RetrofitService = RetrofitApi().getAPI()
) {

    fun callCurrencies(
        sucess: (List<CurrencyModel>) -> Unit,
        error: (erro: String?) -> Unit
    ) {
        val call = service.getListCurrencies()
        call.enqueue(object : Callback<CurrencyResponse> {
            override fun onResponse(
                call: Call<CurrencyResponse?>,
                response: Response<CurrencyResponse>?
            ) {
                val list = response
                    ?.body()
                    ?.currencies?.map { CurrencyModel(it.key, it.value) }

                list?.let {
                    sucess(it)
                    insertCurrencyDatabase(it)
                }
            }

            override fun onFailure(
                call: Call<CurrencyResponse>?,
                t: Throwable?
            ) {
                Log.d("CurrencyRepository", t.toString())
                    sucess(getAllCurrencies())
            }
        })
    }

    private fun insertCurrencyDatabase(currencies: List<CurrencyModel>) = runBlocking(Dispatchers.Default){
        currencyDao.deleteAll()
        currencyDao.insertCurrencies(currencies)
    }

    private fun getAllCurrencies() = runBlocking(Dispatchers.Default){
        currencyDao.getAllCurrencies()
    }

    fun getAll() = runBlocking(Dispatchers.Default){
         currencyDao.getAllCurrencies()
    }

    fun findByCode(code: String) = runBlocking(Dispatchers.Default){
        currencyDao.searchResult(code)
    }

}