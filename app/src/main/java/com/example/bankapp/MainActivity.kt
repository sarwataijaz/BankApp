package com.example.bankapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.cardview.widget.CardView

class MainActivity : AppCompatActivity() {

    lateinit var name: TextView
    private lateinit var amount: TextView
    private lateinit var accNo: TextView
    private lateinit var moneyCardView: CardView
    private lateinit var debitCardView: CardView
    private lateinit var settingsCardView: CardView
    private lateinit var logout: Button

    private lateinit var db: DB_Schema
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setAccountDashBoard()
    }

    private fun setAccountDashBoard() {
        name = findViewById(R.id.name)
        amount = findViewById(R.id.amount)
        accNo = findViewById(R.id.accNo)
        moneyCardView = findViewById(R.id.moneyCardView)
        debitCardView = findViewById(R.id.debitCardView)
        settingsCardView = findViewById(R.id.settingsCardView)
        logout = findViewById(R.id.logout)

        val username = intent.getStringExtra("username") ?: " "
        val password = intent.getStringExtra("password") ?: " "

        db = DB_Schema(this)

        val userName = db.getUserName(username, password)
        val userMoney = db.getUserAmount(username, password)
        val userAccount: Int? = db.getUserAccount(username, password)

        val nameToDisplay = userName?.first()?.uppercase() + userName?.substring(1)

        name.text = "Hello, $nameToDisplay"
        accNo.text = userAccount.toString()
        amount.text = userMoney.toString()


        if (userAccount != null) {
            val getUpdatedMoney = db.moneyReceived(userAccount)
            val getSenderName = db.moneyReceivedFrom(userAccount)
            if (getSenderName != null && getUpdatedMoney != -1) {
                showCongratsDialog(getSenderName, getUpdatedMoney)
            }
        }


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

        logout.setOnClickListener {
            showConfirmationDialog()
        }
    }

    private fun showConfirmationDialog() {
        val confirmationDialog: AlertDialog
        val builder = AlertDialog.Builder(this)
        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_confirmation, null) // Inflate the layout
        builder.setView(dialogView)

        confirmationDialog = builder.create()

        // Find buttons after inflation (assuming IDs are correct in the layout)
        val yesButton = dialogView.findViewById<Button>(R.id.yesButton)
        val noButton = dialogView.findViewById<Button>(R.id.noButton)

        yesButton.setOnClickListener {
            confirmationDialog.dismiss()
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }

        noButton.setOnClickListener {
            confirmationDialog.dismiss()
        }

        confirmationDialog.show()
    }


    private fun showCongratsDialog(senderName: String, senderMoney: Int) {
        val congratsDialog: AlertDialog
        val builder = AlertDialog.Builder(this)
        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_congrats, null)
        builder.setView(dialogView)

        congratsDialog = builder.create()

        val dismiss = dialogView.findViewById<Button>(R.id.dismiss)
        val congosText = dialogView.findViewById<TextView>(R.id.congosText)

        congosText.text = "You just received Rs $senderMoney from $senderName !!"

        dismiss.setOnClickListener {
            congratsDialog.dismiss()
            Log.d("dismiss", "dismiss called")
        }

        congratsDialog.show()
    }

    override fun onResume() {
        super.onResume()
        setAccountDashBoard()
    }

}