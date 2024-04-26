package com.example.bankapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast

class PaymentActivity : AppCompatActivity() {

    lateinit var senderName: TextView
    lateinit var receiverName: TextView
    lateinit var next2: Button
    lateinit var enteredAmount: EditText
    lateinit var totalAmount: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_payment)

        val sendName = intent.getStringExtra("senderUsername")
        val receiveName = intent.getStringExtra("receiverUsername")
        val senderAccNo: Int = intent.getIntExtra("senderAccNo",0)
        val receiverAccNo = intent.getIntExtra("receiverAccNo",0)
        val senderMoney = intent.getIntExtra("senderMoney",0)

        senderName = findViewById(R.id.senderName)
        receiverName = findViewById(R.id.receiverName)
        next2 = findViewById(R.id.next2)
        enteredAmount = findViewById(R.id.enteredAmount)
        totalAmount = findViewById(R.id.totalAmount)

        senderName.text = sendName
        receiverName.text = receiveName
        totalAmount.text = "Available balance Rs. $senderMoney"

        next2.setOnClickListener {

            try {

                val db = DB_Schema(this)

                // necessary data
                val id = db.getUserID(senderAccNo)

                if (enteredAmount.text == null) {
                    Toast.makeText(this, "Enter the amount first", Toast.LENGTH_SHORT).show()
                } else {
                    val amount = enteredAmount.text.toString().toInt()

                    val newAmountSender = senderMoney - amount

                    if (newAmountSender < 0) {
                        Toast.makeText(
                            this,
                            "Enter the amount in the range of the money you have!",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {

                        val success =
                            db.addBeneficiaryDetails(id, amount, receiveName, receiverAccNo)
                        val updated = db.updateAmount(id, newAmountSender)

                        if (success && updated) {
                            val intent = Intent(this, TransferActivity::class.java)
                            intent.putExtra("updatedAmount", newAmountSender)
                            startActivity(intent)
                            finish()
                        }
                    }
                }
            } catch (e: Exception) {
                Log.e("PaymentActivity", "Error during login:", e)
                Toast.makeText(
                    this,
                    "An error occurred. Please try again later.",
                    Toast.LENGTH_SHORT
                ).show()
            }

        }
    }

}