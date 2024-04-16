package com.example.bankapp

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.ImageView
import androidx.core.app.ActivityOptionsCompat

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
            // Code to be executed after the delay
           Intent(this,LoginActivity::class.java).also {
               val pairs = arrayOf(androidx.core.util.Pair<View, String>(app_name, "logo_text"),
                       androidx.core.util.Pair<View, String>(logo, "logo"))

               val options = ActivityOptionsCompat.makeSceneTransitionAnimation(this, *pairs)
               startActivity(it, options.toBundle())
               finish() // remove from the activity stack
           }
        }, 3000) // Delay in milliseconds

    }
}