package com.desafio.btgpactual.repositories

import android.util.Log
import com.coinconverter.bankbtg.data.api.RetrofitApi
import com.coinconverter.bankbtg.data.api.RetrofitService
import com.coinconverter.bankbtg.data.db.dao.QuoteDao
import com.coinconverter.bankbtg.data.db.model.QuotesModel
import com.coinconverter.bankbtg.data.repository.responses.QuotesResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class QuoteRepository(
    private val quoteDao: QuoteDao,
    private val service: RetrofitService = RetrofitApi().getAPI()
) {
    fun callQuotes(
        sucess: (List<QuotesModel>) -> Unit,
        error: (erro: String?) -> Unit
    ) {
        val call = service.getLive()
        call.enqueue(object : Callback<QuotesResponse> {
            override fun onResponse(
                call: Call<QuotesResponse?>,
                response: Response<QuotesResponse>?
            ) {
                val list = response
                    ?.body()
                    ?.quotes
                    ?.map {
                        QuotesModel(it.key, it.value)
                    }

                list?.let {
                    sucess(it)
                    insertQuotesDatabase(it)
                }
            }

            override fun onFailure(
                call: Call<QuotesResponse>?,
                t: Throwable?
            ) {
                Log.d("QuoteRepository", t.toString())
                    sucess(getAllQuotes())
            }
        })
    }

    private fun insertQuotesDatabase(quotes: List<QuotesModel>) = runBlocking(Dispatchers.Default){
        quoteDao.deleteAll()
        quoteDao.insertQuotes(quotes)
    }

    private fun getAllQuotes() = runBlocking(Dispatchers.Default){
        quoteDao.getAllQuotes()
    }

    fun findByCode(code: String) = runBlocking(Dispatchers.Default){
        quoteDao.searchResult(code)
    }

    fun getAll() = runBlocking(Dispatchers.Default){
        quoteDao.getAllQuotes()
    }
}