package com.coinconverter.bankbtg

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.core.app.ApplicationProvider
import com.coinconverter.bankbtg.data.DataBase
import com.coinconverter.bankbtg.data.DataBaseInitializer
import com.coinconverter.bankbtg.domain.model.CurrencyModel
import com.coinconverter.bankbtg.ui.viewmodel.CurrenciesViewModel
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.util.concurrent.CompletableFuture

class CurrenciesInstrumentedTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()
    lateinit var viewModel: CurrenciesViewModel

    @Before
    fun setUp(){
        val context = ApplicationProvider.getApplicationContext<Context>()
        DataBaseInitializer.initDataBase(context)
        DataBase(context)
        viewModel = CurrenciesViewModel()
    }

    @Test
    fun returnSucessCallCurrencies(){
        viewModel.callCurrencies()

        val future: CompletableFuture<List<CurrencyModel>> = CompletableFuture()

        viewModel.listCurrencies.observeForever {
            future.complete(it)
        }

        val fetchIsEmpty = future.get().isEmpty()
        assert(fetchIsEmpty) { "Some problem to get currencies"}
    }
}