package com.example.eventmos

import android.content.Intent
import android.os.Bundle
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val ivBanner    = findViewById<ImageView>(R.id.ivEventBanner)
        val tvWelcome   = findViewById<TextView>(R.id.tvWelcome)
        val tvDesc      = findViewById<TextView>(R.id.tvDescription)
        val btnRegister = findViewById<Button>(R.id.btnRegister)

        val slideUp = AnimationUtils.loadAnimation(this, R.anim.slide_up)
        val fadeIn  = AnimationUtils.loadAnimation(this, R.anim.fade_in)

        ivBanner.startAnimation(fadeIn)
        tvWelcome.startAnimation(slideUp)
        tvDesc.startAnimation(slideUp)
        btnRegister.startAnimation(slideUp)

        btnRegister.setOnClickListener {
            startActivity(Intent(this, RegistrationActivity::class.java))
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        }
    }
}