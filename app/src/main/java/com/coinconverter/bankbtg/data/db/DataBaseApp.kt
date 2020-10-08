package com.coinconverter.bankbtg.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.coinconverter.bankbtg.data.db.dao.CurrencyDao
import com.coinconverter.bankbtg.data.db.dao.QuoteDao
import com.coinconverter.bankbtg.data.db.model.CurrencyModel
import com.coinconverter.bankbtg.data.db.model.QuotesModel
import com.coinconverter.bankbtg.utils.DBNAME

@Database(entities = [CurrencyModel::class, QuotesModel::class], version = 1, exportSchema = false)
abstract class DataBaseApp: RoomDatabase() {
    abstract fun currencyDao(): CurrencyDao
    abstract fun quoteDao(): QuoteDao

    companion object {
        fun create(context: Context, useInMemory : Boolean): DataBaseApp {
            val databaseBuilder = if(useInMemory) {
                Room.inMemoryDatabaseBuilder(context, DataBaseApp::class.java)
            } else {
                Room.databaseBuilder(context, DataBaseApp::class.java, DBNAME)
            }
            return databaseBuilder.fallbackToDestructiveMigration().build()
        }
    }
}