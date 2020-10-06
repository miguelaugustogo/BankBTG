package com.coinconverter.bankbtg.domain.responses

open class CommonResponse {

    var success: Boolean = false
    var terms: String? = null
    var privacy: String? = null
    var error: Error? = null
}