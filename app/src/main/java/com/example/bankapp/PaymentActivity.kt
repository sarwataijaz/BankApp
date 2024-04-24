package com.example.bankapp

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

        next2.setOnClickListener {

        }

        senderName.text = sender
        receiverName.text = receiver


    }
}