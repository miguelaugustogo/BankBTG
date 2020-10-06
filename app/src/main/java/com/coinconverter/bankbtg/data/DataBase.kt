package com.coinconverter.bankbtg.data

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import com.coinconverter.bankbtg.utils.*
import java.io.FileOutputStream
import java.io.IOException

class DataBase(private var context: Context) : SQLiteOpenHelper(context, DBNAME, null, 1) {

    override fun onCreate(db: SQLiteDatabase?) {}

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {}

    private fun checkDataBase(): Boolean {
        var db: SQLiteDatabase? = null
        try {
            val path = DBPATH + DBNAME
            db = SQLiteDatabase.openDatabase(path, null, SQLiteDatabase.OPEN_READONLY)
            db!!.close()
        } catch (e: SQLiteException) {
            Log.e("Exception","err: "+ e.message.toString()+ "\npath: "+ DBPATH + DBNAME)
        }
        return db != null
    }

    private fun createDataBase() {

        val exists = checkDataBase()

        if (!exists) {
            this.readableDatabase
            try {
                copyDatabase()
            } catch (e: IOException) {
                throw Error("Couldn't copy the file")
            }
        }
    }

    private fun copyDatabase() {

        val dbPath = DBPATH + DBNAME
        val dbStream = FileOutputStream(dbPath)
        val dbInputStream = context.assets.open(DBNAME)
        val buffer = ByteArray(1024)
        var length: Int
        var continua = true

        while (continua) {
            length = dbInputStream.read(buffer)
            if(length > 0)
                dbStream.write(buffer, 0, length)
            else
                continua = false
        }
        dbInputStream.close()
        dbStream.flush()
        dbStream.close()
    }

    fun getDatabase(): SQLiteDatabase {
        try {
            createDataBase()
            val path = DBPATH + DBNAME
            return SQLiteDatabase.openDatabase(path, null, SQLiteDatabase.OPEN_READWRITE)
        } catch (e: Exception) {
            return writableDatabase
        }
    }

}