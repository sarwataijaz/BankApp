package com.example.bankapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast

class SignUpActivity : AppCompatActivity() {

    private lateinit var nameField: EditText
    private lateinit var pinField: EditText
    private lateinit var cnicField: EditText
    private lateinit var accNo: EditText
    private lateinit var passField: EditText
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
            val name = nameField.text.toString()
            val pin = pinField.text.toString().toInt()
            val cnic = cnicField.text.toString().toInt()
            val accN = accNo.text.toString().toInt()
            val password = passField.text.toString()

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