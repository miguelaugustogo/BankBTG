package com.coinconverter.bankbtg.ui

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer
import com.coinconverter.bankbtg.R
import com.coinconverter.bankbtg.data.DataBase
import com.coinconverter.bankbtg.data.DataBaseInitializer
import com.coinconverter.bankbtg.domain.model.CurrencyModel
import com.coinconverter.bankbtg.ui.viewmodel.MainViewModel
import com.coinconverter.bankbtg.utils.CURRENCY_ORDER
import com.coinconverter.bankbtg.utils.CURRENCY_SELECTED
import com.coinconverter.bankbtg.utils.FunctionsUtils.replaceCharActivity
import com.coinconverter.bankbtg.utils.INPUT_CURRENCY
import com.coinconverter.bankbtg.utils.OUTPUT_CURRENCY
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        DataBaseInitializer.initDataBase(this)
        DataBase(this)

        viewModel = MainViewModel()
        viewModel.getContext(this)
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
        viewModel.inputCurrencies.observe(this, Observer { inpCur ->
            btnFrom.text =
                replaceCharActivity(getString(R.string.from), inpCur.country, inpCur.code)
        })

        viewModel.outputCurrencies.observe(this, Observer { outCur ->
            btnTo.text = replaceCharActivity(getString(R.string.to), outCur.country, outCur.code)
        })

        viewModel.enableConvert.observe(this, Observer { enabled ->
            btnConvert.isEnabled = enabled
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