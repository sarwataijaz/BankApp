package com.example.bankapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.cardview.widget.CardView

class MainActivity : AppCompatActivity() {

    lateinit var name: TextView
    lateinit var amount: TextView
    lateinit var accNo: TextView
    lateinit var moneyCardView: CardView
    lateinit var debitCardView: CardView
    lateinit var settingsCardView: CardView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        name = findViewById(R.id.name)
        amount = findViewById(R.id.amount)
        accNo = findViewById(R.id.accNo)
        moneyCardView = findViewById(R.id.moneyCardView)
        debitCardView = findViewById(R.id.debitCardView)
        settingsCardView = findViewById(R.id.settingsCardView)

        val username = intent.getStringExtra("username") ?: " "
        val password = intent.getStringExtra("password") ?: " "

        val db = DB_Schema(this)

        val userName = db.getUserName(username, password)
        val userMoney = db.getUserAmount(username, password)
        val userAccount: Int? = db.getUserAccount(username, password)

        val nameToDisplay = userName?.first()?.uppercase() + userName?.substring(1)

        name.text = "Hello, $nameToDisplay"
        accNo.text = userAccount.toString()
        amount.text = userMoney.toString()

        moneyCardView.setOnClickListener{
            val intent = Intent(this, AddAccountActivity::class.java).also {
                it.putExtra("username", userName)
                it.putExtra("accountNo", userAccount)
                it.putExtra("amount",userMoney)
            }
            startActivity(intent)
        }

        settingsCardView.setOnClickListener{

        }

        debitCardView.setOnClickListener{

        }
    }

    override fun onResume() {
        super.onResume()
        val updatedAmount = intent.getIntExtra("updatedAmount", -1)
        if (updatedAmount != -1) {
            // Update the UI to reflect the updated amount
            amount.text = updatedAmount.toString()
        }
    }
}