package com.example.bankapp

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.SQLException
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log

class DB_Schema(context : Context) : SQLiteOpenHelper(context, DB_NAME, null, DB_VERSION) {

    companion object {
        private const val DB_NAME = "Bank_DB"
        private const val DB_VERSION = 1

        // Customer table
        private const val TABLE_CUSTOMER = "customer"
        private const val CUSTOMER_ID = "customer_id"
        private const val ATM_PIN = "atm_pin"
        private const val ACC_NO = "acc_no"
        private const val CUSTOMER_MONEY = "money"
        private const val CUSTOMER_NAME = "customer_name"
        private const val USER_NAME = "user_name"
        private const val PASSWORD = "password"


        // Beneficiary table
        private const val TABLE_BENEFICIARY = "beneficiary"
        private const val BENEFICIARY_ID = "beneficiary_id"
        private const val CUSTOMER_ID_FOREIGN_KEY = "customer_id"
        private const val BENEFICIARY_MONEY = "money"
        private const val BENEFICIARY_NAME = "beneficiary_name"
        private const val BENEFICIARY_ACC_NO = "acc_no"

    }

    override fun onCreate(db: SQLiteDatabase?) {

        val query1 = "CREATE TABLE $TABLE_CUSTOMER ( " +
                     "$CUSTOMER_ID INTEGER PRIMARY KEY, " +
                     "$CUSTOMER_NAME TEXT, " +
                     "$USER_NAME TEXT, " +
                     "$PASSWORD TEXT UNIQUE, " +
                     "$ATM_PIN INTEGER UNIQUE, " +
                     "$ACC_NO INTEGER UNIQUE, " +
                     "$CUSTOMER_MONEY INTEGER)"

        val query2 = "CREATE TABLE $TABLE_BENEFICIARY ( " +
                "$BENEFICIARY_ID INTEGER PRIMARY KEY, " +
                "$BENEFICIARY_NAME TEXT, " +
                "$BENEFICIARY_ACC_NO INTEGER, " +
                "$BENEFICIARY_MONEY INTEGER, " +
                "$CUSTOMER_ID_FOREIGN_KEY INTEGER, " +
                "FOREIGN KEY($CUSTOMER_ID_FOREIGN_KEY) REFERENCES $TABLE_CUSTOMER ($CUSTOMER_ID) )"

        db?.execSQL(query1)
        db?.execSQL(query2)

    }

    fun addCustomerDetails(name: String, username: String, pass: String, pin: Int?, accNo: Int?, amount: Int?): Boolean {

        val db : SQLiteDatabase = this.writableDatabase

        val values = ContentValues()
        values.put(CUSTOMER_NAME,name)
        values.put(USER_NAME,username)
        values.put(PASSWORD,pass)
        values.put(ATM_PIN,pin)
        values.put(ACC_NO,accNo)
        values.put(CUSTOMER_MONEY, amount)

        val newRowID = db.insert(TABLE_CUSTOMER, null, values)
        db.close()

        return (newRowID > -1)
    }

    fun addBeneficiaryDetails(cusID: Int?, moneyGiven: Int, benefName: String?, benefAccNo: Int): Boolean {

        val db : SQLiteDatabase = this.writableDatabase
        val initialFunds = 100 // initial amount to award users

        val values = ContentValues()
        values.put(BENEFICIARY_NAME,benefName)
        values.put(BENEFICIARY_ACC_NO,benefAccNo)
        values.put(BENEFICIARY_MONEY,moneyGiven)
        values.put(CUSTOMER_ID_FOREIGN_KEY,cusID)

        val newRowID = db.insert(TABLE_BENEFICIARY, null, values)
        db.close()

        return (newRowID > -1)
    }

    @SuppressLint("Range")
    fun loginValidity(username: String, pass: String) : Boolean {

        val db = this.readableDatabase
       // onUpgrade(db, DB_VERSION, DB_VERSION)

        val query = "SELECT * FROM $TABLE_CUSTOMER WHERE $USER_NAME = ? AND $PASSWORD = ?"
        val selectionArgs = arrayOf(username,pass)

        val cursor = db.rawQuery(query,selectionArgs)

        val isValid = cursor.moveToFirst()
//        cursor.close()

        do {
            // Retrieve data from Cursor for each row
            val column1Data = cursor.getString(cursor.getColumnIndex("$CUSTOMER_NAME"))
            val column2Data = cursor.getInt(cursor.getColumnIndex("$CUSTOMER_MONEY"))
            // Process or display the retrieved data as needed
            Log.d("CursorData", "Column1: $column1Data, Column2: $column2Data")
        } while (cursor.moveToNext()) // Move to the next row if availabl

        return isValid
    }

    @SuppressLint("Range")
    fun getUserAccount(username: String, pass: String): Int? {
        val db = this.readableDatabase

        val query = "SELECT * FROM $TABLE_CUSTOMER WHERE $USER_NAME = ? AND $PASSWORD = ?"
        val selectionArgs = arrayOf(username,pass)

        val cursor = db.rawQuery(query,selectionArgs)

        if(cursor.moveToFirst()) {
            return cursor.getInt(cursor.getColumnIndex(ACC_NO))
        }
        return null
    }

    fun accNumExist(accNo: Int): Boolean {
        val db = this.readableDatabase

        val query = "SELECT * FROM $TABLE_CUSTOMER WHERE $ACC_NO = ?"
        val selectionArgs = arrayOf(accNo.toString())

        val cursor = db.rawQuery(query,selectionArgs)

        return cursor.moveToFirst()
    }

    @SuppressLint("Range")
    fun getUserName(username: String, pass: String): String? {
        val db = this.readableDatabase

        val query = "SELECT * FROM $TABLE_CUSTOMER WHERE $USER_NAME = ? AND $PASSWORD = ?"
        val selectionArgs = arrayOf(username,pass)

        val cursor = db.rawQuery(query,selectionArgs)

        if(cursor.moveToFirst()) {
            return cursor.getString(cursor.getColumnIndex(CUSTOMER_NAME))
        }
        return null
    }

    @SuppressLint("Range")
    fun getUserName(accNo: Int): String? {
        val db = this.readableDatabase

        val query = "SELECT * FROM $TABLE_CUSTOMER WHERE $ACC_NO = ?"
        val selectionArgs = arrayOf(accNo.toString())

        val cursor = db.rawQuery(query,selectionArgs)

        if(cursor.moveToFirst()) {
            return cursor.getString(cursor.getColumnIndex(CUSTOMER_NAME))
        }
        return null
    }

    @SuppressLint("Range")
    fun getUserID(accNo: Int): Int? {
        val db = this.readableDatabase

        val query = "SELECT * FROM $TABLE_CUSTOMER WHERE $ACC_NO = ?"
        val selectionArgs = arrayOf(accNo.toString())

        val cursor = db.rawQuery(query,selectionArgs)

        if(cursor.moveToFirst()) {
            return cursor.getInt(cursor.getColumnIndex(CUSTOMER_ID))
        }
        return null
    }

    fun updateAmount(id: Int?, newAmount: Int): Boolean {
        val db = this.writableDatabase

        val query = "UPDATE $TABLE_CUSTOMER SET $CUSTOMER_MONEY = ? WHERE $CUSTOMER_ID = ?"
        val selectionArgs = arrayOf(newAmount.toString(), id.toString())

        try {
            db.execSQL(query, selectionArgs)
            return true
        } catch (e: SQLException) {
            // Handle any exceptions, e.g., database error
        } finally {
            db.close() // Close the database connection
        }
        return false
    }

    @SuppressLint("Range")
    fun getUserAmount(username: String, pass: String): Int? {
        val db = this.readableDatabase

        val query = "SELECT $CUSTOMER_MONEY FROM $TABLE_CUSTOMER WHERE $USER_NAME = ? AND $PASSWORD = ?"
        val selectionArgs = arrayOf(username,pass)

        val cursor = db.rawQuery(query,selectionArgs)

        if(cursor.moveToFirst()) {
            return cursor.getInt(cursor.getColumnIndex(CUSTOMER_MONEY))
        }
        return null
    }

    @SuppressLint("Range")
    fun getUserLoginName(id: Int): String? {
        val db = this.readableDatabase

        val query = "SELECT $USER_NAME FROM $TABLE_CUSTOMER WHERE $CUSTOMER_ID = ?"
        val selectionArgs = arrayOf(id.toString())

        val cursor = db.rawQuery(query,selectionArgs)

        if(cursor.moveToFirst()) {
            return cursor.getString(cursor.getColumnIndex(USER_NAME))
        }
        return null
    }

    @SuppressLint("Range")
    fun getUserPassword(id: Int): String? {
        val db = this.readableDatabase

        val query = "SELECT $PASSWORD FROM $TABLE_CUSTOMER WHERE $CUSTOMER_ID = ?"
        val selectionArgs = arrayOf(id.toString())

        val cursor = db.rawQuery(query,selectionArgs)

        if(cursor.moveToFirst()) {
            return cursor.getString(cursor.getColumnIndex(PASSWORD))
        }
        return null
    }


    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_CUSTOMER")
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_BENEFICIARY") // delete all
        onCreate(db)
    }

}