package com.coinconverter.bankbtg.ui.viewmodel

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.coinconverter.bankbtg.data.DataBaseInitializer

open class BasicViewModel: ViewModel(){

    var loading: MutableLiveData<Boolean> = MutableLiveData()
    lateinit var context: Context
    val currenciesDataBase = DataBaseInitializer.currenciesInitializer
    val quotesDatabase = DataBaseInitializer.quotesInitializer

    fun getContext(context: Context){
        this.context = context
    }
}