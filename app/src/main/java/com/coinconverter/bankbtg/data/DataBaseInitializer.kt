package com.coinconverter.bankbtg.data

import android.content.Context

class DataBaseInitializer {

    companion object{
        lateinit var quotesInitializer: QuotesDatabase
        lateinit var currenciesInitializer: CurrenciesDataBase

        fun initDataBase(context: Context){
                quotesInitializer = QuotesDatabase(context)
                currenciesInitializer = CurrenciesDataBase(context)
        }

    }
}