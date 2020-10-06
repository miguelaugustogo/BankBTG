package com.coinconverter.bankbtg.data

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import com.coinconverter.bankbtg.domain.model.CurrencyModel

class CurrenciesDataBase {

    //SQLite
    private var db: SQLiteDatabase
    private var dataBase: DataBase
    private lateinit var cursor: Cursor

    private lateinit var currencies: ArrayList<CurrencyModel>
    private lateinit var currencyModel: CurrencyModel
    private var searchReturn: Array<String>
    private val tbName = "currencies"

    constructor(context: Context) {
        this.dataBase = DataBase(context)
        this.db = dataBase.getDatabase()
        this.searchReturn = arrayOf("id", "code", "country")
    }

    fun insertAll(data: List<CurrencyModel>) {
        data.forEach {
            val cv = ContentValues()
            cv.put("code", it.code)
            cv.put("country", it.country)
            db.insert(tbName, null, cv)
        }
    }

    fun getAll(): List<CurrencyModel> {
        currencies = arrayListOf()
        cursor = db.query(tbName, searchReturn,
            null, null, null, null, null
        )
        cursor.moveToFirst()
        while (!cursor.isAfterLast()) {
            currencyModel = CurrencyModel(
                cursor.getString(1),
                cursor.getString(2),
                cursor.getInt(0)
            )
            currencies.add(currencyModel)
            cursor.moveToNext()
        }
        cursor.close()
        return currencies
    }

    fun findById(id: Int): CurrencyModel {
        val selection = "id = $id"
        return searchResult(selection)
    }

    fun findByCode(code: String): CurrencyModel {
        val selection = "code = '$code'"
        return searchResult(selection)
    }

    private fun searchResult(selection: String): CurrencyModel {
        cursor = db.query(
            tbName, searchReturn, selection,
            null, null, null, null
        )
        cursor.moveToFirst()
        currencyModel = CurrencyModel(cursor.getString(1), cursor.getString(2), cursor.getInt(0))
        cursor.close()

        return currencyModel
    }

    fun deleteAll() {
        db.execSQL("DELETE FROM $tbName ")
        db.execSQL("DELETE FROM sqlite_sequence WHERE name='$tbName'");
    }
}