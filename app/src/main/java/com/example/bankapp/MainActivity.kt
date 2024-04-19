package com.example.bankapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView

class MainActivity : AppCompatActivity() {

    lateinit var name: TextView
    lateinit var amount: TextView
    lateinit var accNo: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        name = findViewById(R.id.name)
        amount = findViewById(R.id.amount)
        accNo = findViewById(R.id.accNo)

        val username = intent.getStringExtra("username") ?: "sarwat_aq"
        val password = intent.getStringExtra("password") ?: "sarwat"

        val db = DB_Schema(this)

        val userName = db.getUserName(username, password)
        val userMoney = db.getUserAmount(username, password)
        val userAccount = db.getUserAccount(username, password)

        val nameToDisplay = userName?.first()?.uppercase() + userName?.substring(1)

        name.text = "Hello, $nameToDisplay"
        accNo.text = userAccount.toString()
        amount.text = userMoney.toString()
    }

}