package com.example.bankapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import com.google.android.material.textfield.TextInputLayout

class LoginActivity : AppCompatActivity() {

    private lateinit var signup: Button
    private lateinit var username: TextInputLayout
    private lateinit var password: TextInputLayout
    private lateinit var login: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        signup = findViewById(R.id.signup)
        login = findViewById(R.id.login)

        signup.setOnClickListener {
            Intent(this,SignUpActivity::class.java).also {
                startActivity(it)
            }
        }

        username = findViewById(R.id.username)
        password = findViewById(R.id.password)

        login.setOnClickListener {
            val enteredUsername = username.editText?.text.toString()
            val enteredPassword = password.editText?.text.toString()
            val db = DB_Schema(this)
            val isValid = db.loginValidity(enteredUsername,enteredPassword)

            if(isValid) {
                Toast.makeText(this, "Login successful!", Toast.LENGTH_SHORT).show()
                Intent(this,MainActivity::class.java).also {
                    finish()
                    startActivity(it)
                }
            } else {
                Toast.makeText(this, "Invalid!", Toast.LENGTH_SHORT).show()
            }

            db.close() // close the database connection
        }
    }
}