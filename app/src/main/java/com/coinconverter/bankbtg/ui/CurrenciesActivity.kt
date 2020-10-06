package com.coinconverter.bankbtg.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.coinconverter.bankbtg.R
import com.coinconverter.bankbtg.ui.adapter.CurrencyAdapter
import com.coinconverter.bankbtg.ui.viewmodel.CurrenciesViewModel
import com.coinconverter.bankbtg.utils.CURRENCY_ORDER
import com.coinconverter.bankbtg.utils.CURRENCY_SELECTED
import kotlinx.android.synthetic.main.activity_currencies.*

class CurrenciesActivity : AppCompatActivity() {

    var flow: Int = 0
    var adapter: CurrencyAdapter? = null
    var viewModel: CurrenciesViewModel = CurrenciesViewModel()
    lateinit var linearLayoutManager: LinearLayoutManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_currencies)

        val bundle: Bundle ?= this.intent.extras
        flow = bundle?.getInt("flow")!!
        linearLayoutManager= LinearLayoutManager(this)
        rvCurrencies.layoutManager= linearLayoutManager

        setObservables()
        setListeners()

        viewModel.getContext(this)
        viewModel.getCurrencies()
        viewModel.getQuotes()
    }

    private fun setObservables(){
        viewModel.loading.observe(this, Observer { loading ->
            if(loading){
                edtSearch.visibility = View.GONE+100
                rvCurrencies.visibility = View.GONE
                progressBar.visibility = View.VISIBLE
            } else {
                edtSearch.visibility = View.VISIBLE
                rvCurrencies.visibility = View.VISIBLE
                progressBar.visibility = View.GONE
            }
        })

        viewModel.listCurrencies.observe(this, Observer { currencies ->
            adapter = CurrencyAdapter(currencies,viewModel)
            rvCurrencies.adapter = adapter
        })

        viewModel.currencySelected.observe(this, Observer { currencies ->
            val intent = Intent()
            val bundle = Bundle()

            bundle.putInt(CURRENCY_ORDER,flow)
            bundle.putString(CURRENCY_SELECTED,currencies)
            intent.putExtras(bundle)
            setResult(Activity.RESULT_OK, intent)
            finish()
        })
    }

    private fun setListeners(){
        rvCurrencies.addOnScrollListener(object : RecyclerView.OnScrollListener(){
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (!recyclerView.canScrollVertically(-1) && newState==RecyclerView.SCROLL_STATE_IDLE){
                    viewModel.isUpdating = true
                    viewModel.callCurrencies()
                    viewModel.getQuotes()
                }
            }
        })

        edtSearch.addTextChangedListener(object : TextWatcher{
            override fun afterTextChanged(p0: Editable?) {
                adapter?.searchElementOnList(p0.toString())
            }
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) { }
        })
    }




}