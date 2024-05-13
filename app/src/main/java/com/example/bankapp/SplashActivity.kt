package com.example.bankapp

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.animation.AnimationUtils
import android.widget.ImageView

@Suppress("DEPRECATION")
class SplashActivity : AppCompatActivity() {
    @SuppressLint("SuspiciousIndentation")
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        val handler = Handler(Looper.getMainLooper())

        val topAnim = AnimationUtils.loadAnimation(this,R.anim.top_anim)
        val bottomAnim = AnimationUtils.loadAnimation(this,R.anim.bottom_anim)
        val logo: ImageView = findViewById(R.id.logo)
        val app_name: ImageView = findViewById(R.id.app_name)
        logo.startAnimation(topAnim)
        app_name.startAnimation(bottomAnim)

        handler.postDelayed({
               val intent = Intent(this, LoginActivity::class.java)
               startActivity(intent)
               overridePendingTransition(R.anim.animate_in_out_enter, R.anim.animate_in_out_exit)
               finish()

        }, 3000) // Delay in milliseconds

    }
}