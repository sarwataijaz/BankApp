package com.example.bankapp

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.SQLException
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log

class DB_Schema (context : Context) : SQLiteOpenHelper(context, DB_NAME, null, DB_VERSION) {

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
        private const val VERSION = "version"

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
                "$VERSION INTEGER NOT NULL, "+
                "FOREIGN KEY($CUSTOMER_ID_FOREIGN_KEY) REFERENCES $TABLE_CUSTOMER ($CUSTOMER_ID) )"

        db?.execSQL(query1)
        db?.execSQL(query2)

    }

    fun addCustomerDetails(name: String, username: String, pass: String, pin: Int?, accNo: Int?, amount: Int?): Boolean {

        val db : SQLiteDatabase = this.writableDatabase
        
        try {
            val values = ContentValues()
            values.put(CUSTOMER_NAME, name)
            values.put(USER_NAME, username)
            values.put(PASSWORD, pass)
            values.put(ATM_PIN, pin)
            values.put(ACC_NO, accNo)
            values.put(CUSTOMER_MONEY, amount)

            val newRowID = db.insert(TABLE_CUSTOMER, null, values)
            return (newRowID > -1)
            
        } catch (e: Exception) {
            Log.d("addCustomerDetails", "Could not store customer details.")
        } finally {
            db.close()
        }

       return false
    }

    fun addBeneficiaryDetails(cusID: Int?, moneyGiven: Int, benefName: String?, benefAccNo: Int): Boolean {

        val db : SQLiteDatabase = this.writableDatabase

        try {
            val values = ContentValues()
            values.put(BENEFICIARY_NAME, benefName)
            values.put(BENEFICIARY_ACC_NO, benefAccNo)
            values.put(BENEFICIARY_MONEY, moneyGiven)
            values.put(CUSTOMER_ID_FOREIGN_KEY, cusID)
            values.put(VERSION, 1) // updated recently

            val newRowID = db.insert(TABLE_BENEFICIARY, null, values)
            return (newRowID > -1)

        } catch (e: Exception) {
          Log.d("addBeneficiaryDetails", "Could not store benef details.")
        } finally {
            db.close()
        }

        return false
    }

    @SuppressLint("Range")
    fun loginValidity(username: String, pass: String) : Boolean {

            val db = this.readableDatabase

            val query = "SELECT * FROM $TABLE_CUSTOMER WHERE $USER_NAME = ? AND $PASSWORD = ?"
            val selectionArgs = arrayOf(username, pass)

            val cursor = db.rawQuery(query, selectionArgs)

            val isValid = cursor.moveToFirst()

            db.close()
            cursor.close()

        return isValid
    }

    @SuppressLint("Range")
    fun getUserAccount(username: String, pass: String): Int? {
        val db = this.readableDatabase

        val query = "SELECT $ACC_NO FROM $TABLE_CUSTOMER WHERE $USER_NAME = ? AND $PASSWORD = ?"
        val selectionArgs = arrayOf(username,pass)

        val cursor = db.rawQuery(query,selectionArgs)

        if(cursor.moveToFirst()) {
            db.close()
            return cursor.getInt(cursor.getColumnIndex(ACC_NO))
        }
        db.close()
        cursor.close()
        return null
    }

    @SuppressLint("Recycle")
    fun accNumExist(accNo: Int): Boolean {

        try {
            val db = this.readableDatabase

            val query = "SELECT * FROM $TABLE_CUSTOMER WHERE $ACC_NO = ?"
            val selectionArgs = arrayOf(accNo.toString())

            val cursor = db.rawQuery(query, selectionArgs)

            return cursor.moveToFirst()

        } catch (e: SQLException) {
            Log.d("accNumExist", "Invalid account number.")
        }

        return false
    }

    @SuppressLint("Range")
    fun getUserName(username: String, pass: String): String? {
        val db = this.readableDatabase

        val query = "SELECT * FROM $TABLE_CUSTOMER WHERE $USER_NAME = ? AND $PASSWORD = ?"
        val selectionArgs = arrayOf(username,pass)

        val cursor = db.rawQuery(query,selectionArgs)

        return if (cursor.moveToFirst()) {
            val userName = cursor.getString(cursor.getColumnIndex(CUSTOMER_NAME))
            cursor.close() // Close cursor after use
            db.close() // Close connection after processing data
            userName
        } else {
            cursor.close() // Close cursor even if no results
            db.close() // Close connection even if no results
            null
        }
    }

    @SuppressLint("Range")
    fun getUserName(accNo: Int): String? {
        val db = this.readableDatabase

        val query = "SELECT * FROM $TABLE_CUSTOMER WHERE $ACC_NO = ?"
        val selectionArgs = arrayOf(accNo.toString())

        val cursor = db.rawQuery(query,selectionArgs)

        return if (cursor.moveToFirst()) {
            val userName = cursor.getString(cursor.getColumnIndex(CUSTOMER_NAME))
            cursor.close() // Close cursor after use
            db.close() // Close connection after processing data
            userName
        } else {
            cursor.close() // Close cursor even if no results
            db.close() // Close connection even if no results
            null
        }
    }

    @SuppressLint("Range")
    fun getUserID(accNo: Int): Int? {
        val db = this.readableDatabase

        val query = "SELECT * FROM $TABLE_CUSTOMER WHERE $ACC_NO = ?"
        val selectionArgs = arrayOf(accNo.toString())

        val cursor = db.rawQuery(query,selectionArgs)

        return if (cursor.moveToFirst()) {
            val userID = cursor.getInt(cursor.getColumnIndex(CUSTOMER_ID))
            cursor.close() // Close cursor after use
            db.close() // Close connection after processing data
            userID
        } else {
            cursor.close() // Close cursor even if no results
            db.close() // Close connection even if no results
            null
        }
    }

    fun updateAmount(id: Int?, newAmount: Int): Boolean {
        val db = this.writableDatabase

        val query = "UPDATE $TABLE_CUSTOMER SET $CUSTOMER_MONEY = ? WHERE $CUSTOMER_ID = ?"
        val selectionArgs = arrayOf(newAmount.toString(), id.toString())

        try {
            db.execSQL(query, selectionArgs)
            return true
        } catch (e: SQLException) {
            Log.d("updateAmount", "Error updating amount.")
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

        return if (cursor.moveToFirst()) {
            val userMoney = cursor.getInt(cursor.getColumnIndex(CUSTOMER_MONEY))
            cursor.close() // Close cursor after use
            db.close() // Close connection after processing data
            userMoney
        } else {
            cursor.close() // Close cursor even if no results
            db.close() // Close connection even if no results
            null
        }
    }

    @SuppressLint("Range")
    fun getUserAmount(accNo: Int): Int {
        val db = this.readableDatabase

        val query = "SELECT $CUSTOMER_MONEY FROM $TABLE_CUSTOMER WHERE $ACC_NO = ?"
        val selectionArgs = arrayOf(accNo.toString())

        val cursor = db.rawQuery(query,selectionArgs)

        return if (cursor.moveToFirst()) {
            val userMoney = cursor.getInt(cursor.getColumnIndex(CUSTOMER_MONEY))
            cursor.close() // Close cursor after use
            db.close() // Close connection after processing data
            userMoney
        } else {
            cursor.close() // Close cursor even if no results
            db.close() // Close connection even if no results
            0
        }
    }

    @SuppressLint("Range")
    fun getUserLoginName(id: Int): String? {
        val db = this.readableDatabase

        val query = "SELECT $USER_NAME FROM $TABLE_CUSTOMER WHERE $CUSTOMER_ID = ?"
        val selectionArgs = arrayOf(id.toString())

        val cursor = db.rawQuery(query,selectionArgs)

        return if (cursor.moveToFirst()) {
            val userName = cursor.getString(cursor.getColumnIndex(USER_NAME))
            cursor.close() // Close cursor after use
            db.close() // Close connection after processing data
            userName
        } else {
            cursor.close() // Close cursor even if no results
            db.close() // Close connection even if no results
            null
        }
    }

    @SuppressLint("Range")
    fun getUserPassword(id: Int): String? {
        val db = this.readableDatabase

        val query = "SELECT $PASSWORD FROM $TABLE_CUSTOMER WHERE $CUSTOMER_ID = ?"
        val selectionArgs = arrayOf(id.toString())

        val cursor = db.rawQuery(query,selectionArgs)

        return if (cursor.moveToFirst()) {
            val userPass = cursor.getString(cursor.getColumnIndex(PASSWORD))
            cursor.close() // Close cursor after use
            db.close() // Close connection after processing data
            userPass
        } else {
            cursor.close() // Close cursor even if no results
            db.close() // Close connection even if no results
            null
        }
    }

    @SuppressLint("Range")
    private fun getVersion(accNo: Int): Boolean {
        val db = this.readableDatabase

        val query = "SELECT $VERSION FROM $TABLE_BENEFICIARY WHERE $BENEFICIARY_ACC_NO = ? ORDER BY $BENEFICIARY_ID DESC \n" +
                    "LIMIT 1;"

        val selectionArgs = arrayOf(accNo.toString())

        val cursor = db.rawQuery(query,selectionArgs)

        var version = 0

        if(cursor.moveToFirst())
            version = cursor.getInt(cursor.getColumnIndex(VERSION))

        if(version==1) {

            version = 0
            val db2 = this.writableDatabase
            val query = "UPDATE $TABLE_BENEFICIARY SET $VERSION = ? WHERE $BENEFICIARY_ACC_NO = ?"
            val selectionArgs = arrayOf(version.toString(), accNo.toString())

            try {
                db2.execSQL(query, selectionArgs)
                return true
            } catch (e: SQLException) {
                Log.d("getVersion", "Could not update version.")
            } finally {
                db2.close() // Close the database connection
            }

        }
        cursor.close()
        db.close()
        return false
    }
    @SuppressLint("Range")
    fun moneyReceived(accNo: Int): Int {
       // val db = this.readableDatabase

        val recentlyUpdated = getVersion(accNo)
        if (!recentlyUpdated) {
            return -1
        }

        val query = """
        SELECT $BENEFICIARY_MONEY FROM $TABLE_BENEFICIARY 
        WHERE $BENEFICIARY_ACC_NO = ? 
        ORDER BY $BENEFICIARY_ID DESC LIMIT 1;
    """.trimIndent()

        val selectionArgs = arrayOf(accNo.toString())

        var cursor: Cursor? = null
        val db = this.readableDatabase

        return try {
            if(db.isOpen) {
                cursor = db.rawQuery(query, selectionArgs)

                if (cursor.moveToFirst()) {
                    val money = cursor.getInt(cursor.getColumnIndexOrThrow(BENEFICIARY_MONEY))
                    Log.d("moneyReceived", "Money received: $money")
                    money
                } else {
                    Log.d("moneyReceived", "No records found for account $accNo")
                    -1
                }
            } else {
                -1
            }
        } catch (e: Exception) {
            Log.e("moneyReceived", "Error while fetching data", e)
            -1
        } finally {
            cursor?.close()
            db.close()
        }
    }

    @SuppressLint("Range")
    fun moneyReceivedFrom(accNo: Int): String? {
        val db = this.readableDatabase

        val query = "SELECT $CUSTOMER_ID_FOREIGN_KEY FROM $TABLE_BENEFICIARY WHERE $BENEFICIARY_ACC_NO = ? ORDER BY $BENEFICIARY_ID DESC \n" +
                "LIMIT 1;"
        val selectionArgs = arrayOf(accNo.toString())

        val cursor = db.rawQuery(query,selectionArgs)

        if(cursor.moveToFirst()) {
            val id = cursor.getInt(cursor.getColumnIndex(CUSTOMER_ID_FOREIGN_KEY))
            cursor.close()
            val query2 = "SELECT $CUSTOMER_NAME FROM $TABLE_CUSTOMER WHERE $CUSTOMER_ID = ?"
            val selectionArgs2 = arrayOf(id.toString())
            val cursor2 = db.rawQuery(query2,selectionArgs2)

            if(cursor2.moveToFirst()) {
                db.close()
                return cursor2.getString(cursor2.getColumnIndex(CUSTOMER_NAME))
            }
        }

        db.close()

        return null
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_CUSTOMER")
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_BENEFICIARY") // delete all
        onCreate(db)
    }

}