package com.example.bankapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView

class PaymentActivity : AppCompatActivity() {

    lateinit var senderName: TextView
    lateinit var receiverName: TextView
    lateinit var next2: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_payment)

        val sender = intent.getStringExtra("senderUsername")
        val receiver = intent.getStringExtra("receiverUsername")

        senderName = findViewById(R.id.senderName)
        receiverName = findViewById(R.id.receiverName)
        next2 = findViewById(R.id.next2)

        next2.setOnClickListener {
            Intent(this,TransferActivity::class.java).also {
                startActivity(it)
                finish()
            }
        }

        senderName.text = sender
        receiverName.text = receiver


    }
}