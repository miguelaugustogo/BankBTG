package com.coinconverter.bankbtg.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.coinconverter.bankbtg.data.db.model.CurrencyModel
@Dao
interface CurrencyDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertCurrencies(quotes: List<CurrencyModel>)

    @Query("SELECT * FROM currencyModel")
    fun getAllCurrencies(): List<CurrencyModel>

    @Query("DELETE FROM currencyModel")
    fun deleteAll()

    @Query("SELECT code, country FROM currencyModel where code = :selection")
    fun searchResult(selection: String): CurrencyModel
}