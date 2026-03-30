package com.example.shoes_app

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Back button to Sign In
        findViewById<ImageButton>(R.id.btnBack).setOnClickListener {
            finish()
        }

        // Sign In link footer
        findViewById<TextView>(R.id.tvFooter).setOnClickListener {
            finish()
        }

        // Register button logic (for now just goes to Home)
        findViewById<Button>(R.id.btnSignIn).setOnClickListener {
            startActivity(Intent(this, HomeActivity::class.java))
        }
    }
}