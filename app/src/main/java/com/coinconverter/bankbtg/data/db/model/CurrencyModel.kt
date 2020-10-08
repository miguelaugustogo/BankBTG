package com.coinconverter.bankbtg.data.db.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class CurrencyModel(
    @PrimaryKey
    val code: String,
    val country: String
){
    override fun toString(): String {
        return "$code - $country"
    }
}