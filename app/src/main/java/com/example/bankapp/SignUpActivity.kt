package com.example.bankapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import com.google.android.material.textfield.TextInputLayout

class SignUpActivity : AppCompatActivity() {

    private lateinit var nameField: TextInputLayout
    private lateinit var userNameField: TextInputLayout
    private lateinit var pinField: TextInputLayout
    private lateinit var accNo: TextInputLayout
    private lateinit var passField: TextInputLayout
    private lateinit var register: Button
    private lateinit var amountField: TextInputLayout
    private lateinit var db: DB_Schema
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        nameField = findViewById(R.id.nameField)
        userNameField = findViewById(R.id.userNameField)
        pinField = findViewById(R.id.pinField)
        accNo = findViewById(R.id.accNo)
        passField = findViewById(R.id.passField)
        amountField = findViewById(R.id.amountField)

        register = findViewById(R.id.register)

        register.setOnClickListener {
            try {
                Log.d("signup", "success1")

                val name = nameField.editText?.text.toString()
                val username = userNameField.editText?.text.toString()
                val pin = pinField.editText?.text.toString().toInt()
                val accN = accNo.editText?.text.toString().toLong()
                val password = passField.editText?.text.toString()
                val amount = amountField.editText?.text.toString().toInt()

                Log.d("signup", "Name: $name")
                Log.d("signup", "Username: $username")
                Log.d("signup", "PIN: $pin")
                Log.d("signup", "Account Number: $accN")
                Log.d("signup", "Password: $password")
                Log.d("signup", "Amount: $amount")


                Log.d("signup", "success2")

                db = DB_Schema(this)
                val dataStored = db.addCustomerDetails(name, username, password, pin, accN, amount)
                Log.d("signup", "success3")

                if (dataStored) {
                    Toast.makeText(this, "Registration successful!", Toast.LENGTH_SHORT).show()
                    finish()
                } else {
                    Toast.makeText(this, "Invalid!", Toast.LENGTH_SHORT).show()
                }

                db.close() // close the database connection
            } catch (e: Exception) {
                Log.e("signup", "Error occurred: ${e.message}", e)
                Toast.makeText(this, "Could not store data. Make sure you entered correct fields and try again", Toast.LENGTH_SHORT).show()
            }
        }
    }
}