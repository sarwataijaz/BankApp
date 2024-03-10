package com.example.bankapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageView

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        val handler = Handler(Looper.getMainLooper())

        handler.postDelayed({
            // Code to be executed after the delay
           Intent(this,MainActivity::class.java).also {
               startActivity(it)
               finish()
           }
        }, 3000) // Delay in milliseconds
        val rotate = AnimationUtils.loadAnimation(this,R.anim.rotate)
        val logo: ImageView = findViewById(R.id.logo)
        logo.startAnimation(rotate)
    }
}