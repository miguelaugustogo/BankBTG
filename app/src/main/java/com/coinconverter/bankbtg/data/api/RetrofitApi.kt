package com.coinconverter.bankbtg.data.api

import com.coinconverter.bankbtg.utils.API_KEY
import com.coinconverter.bankbtg.utils.BASE_URL
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class RetrofitApi {

    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .client(httpClient())
        .build()

    fun getAPI() = retrofit.create(RetrofitService::class.java)

    fun httpClient(): OkHttpClient {
        val httpClient = OkHttpClient
            .Builder()
            .addInterceptor { chain: Interceptor.Chain ->
                val request = chain
                    .request()
                    .url()
                    .newBuilder()
                    .addQueryParameter("access_key", API_KEY)
                    .build()
                val changedUrl = chain.request().newBuilder().url(request).build()

                chain.proceed(changedUrl)
            }
            .build()
        return httpClient
    }
}