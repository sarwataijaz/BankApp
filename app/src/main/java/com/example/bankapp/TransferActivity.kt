package com.example.bankapp

import com.bumptech.glide.Glide
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView

class TransferActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_transfer)

        val tick: ImageView = findViewById(R.id.tick)
        val backButton: Button = findViewById(R.id.backButton)

        Glide.with(this)
            .asGif()
            .load(R.drawable.tick_img) // Or use resource ID with R.drawable.my_image
            .into(tick)

        backButton.setOnClickListener {
            finish()
        }
    }
}