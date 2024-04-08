package com.example.bankapp

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DB_Schema(context : Context) : SQLiteOpenHelper(context, DB_NAME, null, DB_VERSION) {

    companion object {
        private const val DB_NAME = "Bank_DB"
        private const val DB_VERSION = 1

        // Customer table
        private const val TABLE_CUSTOMER = "customer"
        private const val CUSTOMER_ID = "customer_id"
        private const val DEBIT_CARD_NO = "debit_card_no"
        private const val ATM_PIN = "atm_pin"
        private const val CNIC = "cnic"
        private const val ACC_NO = "acc_no"
        private const val CUSTOMER_MONEY = "money"
        private const val CUSTOMER_NAME = "customer_name"

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
                     "$DEBIT_CARD_NO INTEGER, " +
                     "$ATM_PIN INTEGER UNIQUE, " +
                     "$ACC_NO INTEGER UNIQUE, " +
                     "$CUSTOMER_MONEY INTEGER, " +
                     "$CNIC INTEGER UNIQUE )"

        val query2 = "CREATE TABLE $TABLE_BENEFICIARY ( " +
                "$BENEFICIARY_ID INTEGER PRIMARY KEY, " +
                "$BENEFICIARY_NAME TEXT, " +
                "$BENEFICIARY_ACC_NO INTEGER UNIQUE, " +
                "$BENEFICIARY_MONEY INTEGER, " +
                "FOREIGN KEY($CUSTOMER_ID_FOREIGN_KEY) REFERENCES $TABLE_CUSTOMER ($CUSTOMER_ID) )"

        db?.execSQL(query1)
        db?.execSQL(query2)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_CUSTOMER")
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_BENEFICIARY") // delete all
        onCreate(db)
    }

}