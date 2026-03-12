package com.example.eventmos

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class ConfirmationActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_confirmation)

        supportActionBar?.hide()

        val fullName    = intent.getStringExtra("fullName")  ?: ""
        val phone       = intent.getStringExtra("phone")     ?: ""
        val email       = intent.getStringExtra("email")     ?: ""
        val eventType   = intent.getStringExtra("eventType") ?: ""
        val eventDate   = intent.getStringExtra("eventDate") ?: ""
        val gender      = intent.getStringExtra("gender")    ?: ""
        val imageUriStr = intent.getStringExtra("imageUri")  ?: ""

        findViewById<TextView>(R.id.tvFullName).text  = fullName
        findViewById<TextView>(R.id.tvPhone).text     = phone
        findViewById<TextView>(R.id.tvEmail).text     = email
        findViewById<TextView>(R.id.tvEventType).text = eventType
        findViewById<TextView>(R.id.tvEventDate).text = eventDate
        findViewById<TextView>(R.id.tvGender).text    = gender

        val ivProfile = findViewById<ImageView>(R.id.ivProfileConfirm)
        if (imageUriStr.isNotEmpty()) {
            ivProfile.setImageURI(Uri.parse(imageUriStr))
            ivProfile.visibility = View.VISIBLE
        } else {
            ivProfile.visibility = View.GONE
        }

        val regId = "EV" + (100000..999999).random().toString()
        findViewById<TextView>(R.id.tvRegId).text = "#$regId"

        findViewById<Button>(R.id.btnBackHome).setOnClickListener {
            val i = Intent(this, MainActivity::class.java)
            i.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(i)
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
            finish()
        }
    }
}