package com.coinconverter.bankbtg.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.coinconverter.bankbtg.data.db.model.CurrencyModel
import com.coinconverter.bankbtg.data.db.model.QuotesModel

@Dao
interface QuoteDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertQuotes(quotes: List<QuotesModel>)

    @Query("SELECT * FROM quotesModel")
    fun getAllQuotes(): List<QuotesModel>

    @Query("DELETE FROM quotesModel")
    fun deleteAll()

    @Query("SELECT code, value FROM quotesModel where code = :selection")
    fun searchResult(selection: String): QuotesModel
}