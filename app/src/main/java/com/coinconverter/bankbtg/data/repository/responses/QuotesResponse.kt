package com.coinconverter.bankbtg.data.repository.responses

data class QuotesResponse(
    val success: Boolean,
    val terms: String,
    val privacy: String,
    val timestamp: Long,
    val source: String,
    val quotes: HashMap<String, Double>? = null
)