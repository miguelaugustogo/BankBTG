package com.coinconverter.bankbtg.domain.responses

class CurrencyResponse: CommonResponse() {
    //    val success: Boolean,
//    val terms: String,
//    val privacy: String,
    var currencies: HashMap<String, String>? = null
}