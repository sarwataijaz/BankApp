package com.example.bankapp

import android.content.Intent
import com.bumptech.glide.Glide
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView

class TransferActivity : AppCompatActivity() {

    private lateinit var db: DB_Schema
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_transfer)

        val tick: ImageView = findViewById(R.id.tick)
        val backButton: Button = findViewById(R.id.backButton)

        val customerID = intent.getIntExtra("customerID",0)

        db = DB_Schema(this)
        val userName = db.getUserLoginName(customerID)
        val password = db.getUserPassword(customerID)

        Glide.with(this)
            .asGif()
            .load(R.drawable.tick_img)
            .into(tick)

        backButton.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra("username", userName)
            intent.putExtra("password", password)

            startActivity(intent)
            finish()
        }
    }

}