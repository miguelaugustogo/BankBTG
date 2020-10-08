package com.coinconverter.bankbtg.data.repository.responses

open class CommonResponse {

    var success: Boolean = false
    var terms: String? = null
    var privacy: String? = null
    var error: Error? = null
}