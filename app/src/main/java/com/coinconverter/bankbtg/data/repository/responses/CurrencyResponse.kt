package com.coinconverter.bankbtg.data.repository.responses

data class CurrencyResponse(
    val success: Boolean,
    val terms: String,
    val privacy: String,
    var currencies: HashMap<String, String>? = null
)