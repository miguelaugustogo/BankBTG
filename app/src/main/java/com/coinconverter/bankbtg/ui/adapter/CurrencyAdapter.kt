package com.coinconverter.bankbtg.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SortedList
import androidx.recyclerview.widget.SortedListAdapterCallback
import com.coinconverter.bankbtg.R
import com.coinconverter.bankbtg.domain.model.CurrencyModel
import com.coinconverter.bankbtg.ui.viewmodel.CurrenciesViewModel
import kotlinx.android.synthetic.main.currency_adapter.view.*
import kotlin.collections.ArrayList

class CurrencyAdapter (private val currencies: List<CurrencyModel>
                       , private val viewModel: CurrenciesViewModel
): RecyclerView.Adapter<CurrencyAdapter.ViewHolder>() {

    private var sList: SortedList<CurrencyModel>

    init {
        sList = SortedList(CurrencyModel::class.java, object : SortedListAdapterCallback<CurrencyModel>(this) {
            override fun compare(o1: CurrencyModel, o2: CurrencyModel): Int = o1.code.compareTo(o2.code)

            override fun areContentsTheSame(oldItem: CurrencyModel, newItem: CurrencyModel): Boolean = oldItem.country == newItem.country

            override fun areItemsTheSame(item1: CurrencyModel, item2: CurrencyModel): Boolean = item1 == item2
        })
        sList.addAll(currencies)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.currency_adapter, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int= sList.size()

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currency = sList[position]
        holder.bind(currency)
        holder.itemView.setOnClickListener {viewModel.onSelectCurrency(sList[position].code)}
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(currencyModel: CurrencyModel){
            val code = itemView.tvCode
            val country = itemView.tvCountry
            code.text = currencyModel.code
            country.text = currencyModel.country
        }
    }

    fun searchElementOnList(text: String) {
        val textUc = text.toUpperCase()
        val filteredList = ArrayList<CurrencyModel>()
        currencies.forEach {
            val nameUc = it.code.toUpperCase()
            val symbolUc = it.country.toUpperCase()
            if (nameUc.contains(textUc) || symbolUc.contains(textUc))  filteredList.add(it)
        }

        sList.beginBatchedUpdates()
        sList.clear()
        sList.addAll(filteredList)
        sList.endBatchedUpdates()
    }
}