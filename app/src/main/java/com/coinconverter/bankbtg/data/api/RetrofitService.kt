package com.coinconverter.bankbtg.data.api

import com.coinconverter.bankbtg.data.repository.responses.CurrencyResponse
import com.coinconverter.bankbtg.data.repository.responses.QuotesResponse
import retrofit2.Call
import retrofit2.http.GET

interface RetrofitService {

//    @Headers("Content-Type: application/json", "Accept: application/json")
    @GET("list")
    fun getListCurrencies(): Call<CurrencyResponse>

//    @Headers("Content-Type: application/json", "Accept: application/json")
    @GET("live")
    fun getLive(): Call<QuotesResponse>
}