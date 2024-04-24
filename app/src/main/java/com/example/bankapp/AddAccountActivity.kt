package com.example.bankapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast

class AddAccountActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_account)

        val next: Button = findViewById(R.id.next)
        val accountNum: EditText = findViewById(R.id.accountNum)

        next.setOnClickListener {
            val enteredAccountNum = accountNum.text.toString().toInt() ?: 0  // Handle empty input
            val customerUsername = intent.getStringExtra("username")

            try {
                val db = DB_Schema(this)
                val isValid = db.accNumExist(enteredAccountNum)

                if (isValid) {
                    Toast.makeText(this, "Account exists!", Toast.LENGTH_SHORT).show()

                    val userName = db.getUserName(enteredAccountNum)

                    val intent = Intent(this, PaymentActivity::class.java)
                    intent.putExtra("receiverUsername", userName)
                    intent.putExtra("senderUsername", customerUsername)
                    finish()
                    startActivity(intent)

                } else {
                    Toast.makeText(this, "Invalid!", Toast.LENGTH_SHORT).show()
                }

                db.close() // close the database connection
            } catch (e: Exception) {
                Log.e("AddAccountActivity", "Error during login:", e)
                Toast.makeText(
                    this,
                    "An error occurred. Please try again later.",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
}