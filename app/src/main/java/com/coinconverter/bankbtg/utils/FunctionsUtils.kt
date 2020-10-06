package com.coinconverter.bankbtg.utils

import android.content.Context
import android.widget.Toast
import java.text.DecimalFormat

object FunctionsUtils {

    private val decimalFormat = DecimalFormat(CONVERT_MASK)

    fun replaceCharActivity(labelText: String, cName: String, cSymbol: String): String {
        return "$labelText $cName ($cSymbol)"
    }
    fun showMessage(context: Context, text: String) = Toast.makeText(context, text, Toast.LENGTH_LONG).show()

    fun doubleToString(value: Double): String{return decimalFormat.format(value)}

    fun stringReplaceComma(value: String): String = value.replace(",",".")
}