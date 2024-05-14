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
    lateinit var amount: TextView
    lateinit var accNo: TextView
    lateinit var moneyCardView: CardView
    lateinit var debitCardView: CardView
    lateinit var settingsCardView: CardView
    lateinit var logout: Button

    lateinit var yesButton: Button
    lateinit var noButton: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setAccountDashBoard()
    }

    fun setAccountDashBoard() {
        name = findViewById(R.id.name)
        amount = findViewById(R.id.amount)
        accNo = findViewById(R.id.accNo)
        moneyCardView = findViewById(R.id.moneyCardView)
        debitCardView = findViewById(R.id.debitCardView)
        settingsCardView = findViewById(R.id.settingsCardView)
        logout = findViewById(R.id.logout)

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

  //      db.close()
//        Log.d("check","Ui executed")
        if (userAccount != null) {
            val getUpdatedMoney = db.moneyReceived(userAccount).toString() // Ensure String conversion
            val getSenderName = db.moneyReceivedFrom(userAccount)
            if (getUpdatedMoney != "-1" && getSenderName != null) {
                showCongratsDialog(getSenderName, getUpdatedMoney.toInt()) // Convert back to Int if needed
            }
        }
        db.close()

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

    private lateinit var congratsDialog: AlertDialog

    private fun showCongratsDialog(senderName: String, senderMoney: Int) {
        val builder = AlertDialog.Builder(this)
        builder.setView(LayoutInflater.from(this).inflate(R.layout.dialog_congrats, null))

        congratsDialog = builder.create()

        val dismiss = findViewById<Button>(R.id.dismiss)
        val congosText = findViewById<TextView>(R.id.congosText)

        congosText.text = "You just received Rs $senderMoney from $senderName !!"

        Log.d("check","You just received Rs $senderMoney from $senderName !!"
        )
        dismiss.setOnClickListener {
            congratsDialog.dismiss()
        }

        congratsDialog.show()
    }

    override fun onResume() {
        super.onResume()
        setAccountDashBoard()
    }
}