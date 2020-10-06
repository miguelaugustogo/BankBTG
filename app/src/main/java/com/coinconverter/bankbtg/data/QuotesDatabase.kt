package com.coinconverter.bankbtg.data

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import com.coinconverter.bankbtg.domain.model.QuotesModel

class QuotesDatabase {

    //SQLite
    private var db: SQLiteDatabase
    private var helper: DataBase
    private lateinit var cursor: Cursor

    private lateinit var quotes: ArrayList<QuotesModel>
    private lateinit var quote: QuotesModel
    private var searchReturn: Array<String>
    private val tbName = "quotes"

    constructor(context: Context){
        this.helper = DataBase(context)
        this.db = helper.getDatabase()
        this.searchReturn = arrayOf("id","code","value")
    }

    fun insertAll(data: List<QuotesModel>) {
        data.forEach {
            val cv = ContentValues()
            cv.put("code", it.code)
            cv.put("value", it.value)
            db.insert(tbName, null, cv)
        }
    }

    fun getAll(): List<QuotesModel>{
        quotes = arrayListOf()
        cursor = db.query(tbName, searchReturn,
            null, null, null, null, null)
        cursor.moveToFirst()
        while (!cursor.isAfterLast()){
            quote = QuotesModel(
                cursor.getString(1),
                cursor.getDouble(2),
                cursor.getInt(0)
            )
            quotes.add(quote)
            cursor.moveToNext()
        }
        cursor.close()
        return quotes
    }

    fun findById(id: Int): QuotesModel {
        val selection = "id = $id"
        return searchResult(selection)
    }

    fun findByCode(code: String): QuotesModel {
        val selection = "code = '$code'"
        return searchResult(selection)
    }

    private fun searchResult(selection: String): QuotesModel {
        cursor = db.query(tbName, searchReturn, selection,
            null, null, null, null)
        cursor.moveToFirst()

        quote = QuotesModel(cursor.getString(1),cursor.getDouble(2),cursor.getInt(0))
        cursor.close()

        return quote
    }

    fun deleteAll(){
        db.execSQL("DELETE FROM $tbName ")
        db.execSQL("DELETE FROM sqlite_sequence WHERE name='$tbName'");
    }
}