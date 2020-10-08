package com.coinconverter.bankbtg.ui

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.coinconverter.bankbtg.R
import com.coinconverter.bankbtg.data.db.DataBaseApp
import com.coinconverter.bankbtg.ui.viewmodel.MainViewModel
import com.coinconverter.bankbtg.ui.viewmodel.factory.MainFactory
import com.coinconverter.bankbtg.utils.*
import com.coinconverter.bankbtg.utils.FunctionsUtils.replaceCharActivity
import com.desafio.btgpactual.repositories.CurrencyRepository
import com.desafio.btgpactual.repositories.QuoteRepository
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private val viewModel by lazy {
        val currencyRepository = CurrencyRepository(DataBaseApp.create(this, false).currencyDao())
        val quoteRepository = QuoteRepository(DataBaseApp.create(this, false).quoteDao())
        val factory = MainFactory(currencyRepository, quoteRepository)
        val viewModel = ViewModelProvider(this, factory)
        viewModel.get(MainViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btnConvert.isEnabled = false
        setObservables()
        setClickListeners()
        viewModel.setStartInfo()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val bundle = data?.extras
        if (resultCode == Activity.RESULT_OK) {
            tvResult.text = getString(R.string.result)

            this.viewModel.onCurrencySelected(
                bundle?.getInt(CURRENCY_ORDER),
                bundle?.getString(CURRENCY_SELECTED)
            )
        }
    }

    private fun setObservables() {
        viewModel.inputCurrencies.observe(this, Observer { inputCurrrency ->
            btnFrom.text =
                replaceCharActivity(getString(R.string.from), inputCurrrency.country, inputCurrrency.code)
        })

        viewModel.outputCurrencies.observe(this, Observer { outuputCurrrency ->
            btnTo.text = replaceCharActivity(getString(R.string.to), outuputCurrrency.country, outuputCurrrency.code)
        })

        viewModel.enableConvert.observe(this, Observer { enabled ->
            btnConvert.isEnabled = enabled
        })

        viewModel.pleaseEnterValue.observe(this, Observer {enabled ->
            FunctionsUtils.showMessage(this, getString(R.string.enter_value))
        })

        viewModel.convertValue.observe(this, Observer { value ->
            tvResult.text = getString(
                R.string.result_convert,
                edtValue.text,
                viewModel.inputCurrencies.value?.country,
                value,
                viewModel.outputCurrencies.value?.country
            )
        })
    }

    private fun setClickListeners() {
        btnFrom.setOnClickListener {
            starListActivity(INPUT_CURRENCY)
        }

        btnTo.setOnClickListener {
            starListActivity(OUTPUT_CURRENCY)
        }

        btnConvert.setOnClickListener {
            viewModel.doConvert(edtValue.text.toString())
        }

        btnInvertCoins.setOnClickListener {
            viewModel.invertCoins()
        }
    }

    private fun starListActivity(requestCode: Int) {
        val intent = Intent(this, CurrenciesActivity::class.java)
        intent.putExtra("flow", requestCode)
        startActivityForResult(
            intent,
            requestCode
        )
    }
}