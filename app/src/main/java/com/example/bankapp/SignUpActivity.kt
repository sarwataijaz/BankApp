package com.example.bankapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.android.material.textfield.TextInputLayout

class SignUpActivity : AppCompatActivity() {

    private lateinit var nameField: TextInputLayout
    private lateinit var pinField: TextInputLayout
    private lateinit var cnicField: TextInputLayout
    private lateinit var accNo: TextInputLayout
    private lateinit var passField: TextInputLayout
    private lateinit var register: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        nameField = findViewById(R.id.nameField)
        pinField = findViewById(R.id.pinField)
        cnicField = findViewById(R.id.cnicField)
        accNo = findViewById(R.id.accNo)
        passField = findViewById(R.id.passField)

        register = findViewById(R.id.register)

        register.setOnClickListener {
            val name = nameField.editText?.text.toString()
            val pin = pinField.editText?.toString()?.toInt()
            val cnic = cnicField.editText?.toString()?.toInt()
            val accN = accNo.editText?.toString()?.toInt()
            val password = passField.editText?.text.toString()

            val db = DB_Schema(this)
            val dataStored = db.addCustomerDetails(name,password, pin,cnic,accN)

            if(dataStored) {
                Toast.makeText(this, "successful!", Toast.LENGTH_SHORT).show()
                finish()
            } else {
                Toast.makeText(this, "Invalid!", Toast.LENGTH_SHORT).show()
            }

            db.close() // close the database connection
        }
    }
}