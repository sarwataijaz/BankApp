package com.example.bankapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import com.google.android.material.textfield.TextInputLayout

class LoginActivity : AppCompatActivity() {

    // usage of intent extra

    private lateinit var signup: Button
    private lateinit var username: TextInputLayout
    private lateinit var password: TextInputLayout
    private lateinit var login: Button
    private lateinit var registered: Button

    private lateinit var db: DB_Schema
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        signup = findViewById(R.id.signup)
        login = findViewById(R.id.login)
        registered = findViewById(R.id.registered)

        signup.setOnClickListener {
            Intent(this, SignUpActivity::class.java).also {
                startActivity(it)
            }
        }

        registered.setOnClickListener {
            Intent(this, SignUpActivity::class.java).also {
                startActivity(it)
            }
        }

        username = findViewById(R.id.username)
        password = findViewById(R.id.password)

        login.setOnClickListener {
            val enteredUsername = username.editText?.text.toString() ?: ""  // Handle empty input
            val enteredPassword = password.editText?.text.toString() ?: ""  // Handle empty input

            try {
                db = DB_Schema(this)
                val isValid = db.loginValidity(enteredUsername, enteredPassword)


                if (isValid) {
                    Toast.makeText(this, "Login successful!", Toast.LENGTH_SHORT).show()

                    val getAccNo = db.getUserAccount(enteredUsername,enteredPassword) ?: 0
                    val getAmount = db.getUserAmount(enteredUsername,enteredPassword) ?: 0
                    val getId = db.getUserID(getAccNo)

                    val intent = Intent(this, MainActivity::class.java)
                    intent.putExtra("username", enteredUsername)
                    intent.putExtra("password", enteredPassword)
                    finish()
                    startActivity(intent)

                } else {
                    Toast.makeText(this, "Invalid!", Toast.LENGTH_SHORT).show()
                }

                db.close() // close the database connection
            } catch (e: Exception) {
                Log.e("LoginActivity", "Error during login:", e)
                Toast.makeText(
                    this,
                    "An error occurred. Please try again later.",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

}