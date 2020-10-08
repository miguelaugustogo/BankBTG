package com.coinconverter.bankbtg.data.db.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class QuotesModel(
    @PrimaryKey
    val code: String,
    val value: Double
)