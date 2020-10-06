package com.coinconverter.bankbtg.domain.responses

class QuotesResponse: CommonResponse(){
//    val success: Boolean,
//    val terms: String,
//    val privacy: String,
    val timestamp: Long? = null
    val source: String? = null
    val quotes: HashMap<String, Double>? = null
}