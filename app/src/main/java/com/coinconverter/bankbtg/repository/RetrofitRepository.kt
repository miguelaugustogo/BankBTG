package com.coinconverter.bankbtg.repository

import com.coinconverter.bankbtg.domain.responses.CurrencyResponse
import com.coinconverter.bankbtg.domain.responses.QuotesResponse
import retrofit2.Call
import retrofit2.http.GET

interface RetrofitRepository {

//    @Headers("Content-Type: application/json", "Accept: application/json")
    @GET("list")
    fun getListCurrencies(): Call<CurrencyResponse>

//    @Headers("Content-Type: application/json", "Accept: application/json")
    @GET("live")
    fun getLive(): Call<QuotesResponse>
}