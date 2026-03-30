package com.example.shoes_app

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SwitchCompat
import androidx.core.content.ContextCompat
import com.google.android.material.floatingactionbutton.FloatingActionButton

class AccountSettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_account_settings)

        val btnBack = findViewById<ImageButton>(R.id.btnBack)
        val swPushNotif = findViewById<SwitchCompat>(R.id.swPushNotif)
        val swLocation = findViewById<SwitchCompat>(R.id.swLocation)

        btnBack.setOnClickListener {
            finish()
        }

        // SharedPrefs for settings
        val sharedPref = getSharedPreferences("AppSettings", Context.MODE_PRIVATE)
        
        // Push Notification Switch
        swPushNotif.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                Toast.makeText(this, "Push notifications enabled", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Push notifications disabled", Toast.LENGTH_SHORT).show()
            }
            sharedPref.edit().putBoolean("push_notif", isChecked).apply()
        }

        // Setup Bottom Nav
        val ivHome = findViewById<ImageView>(R.id.ivHome)
        val ivFav = findViewById<ImageView>(R.id.ivFav)
        val fabCart = findViewById<FloatingActionButton>(R.id.fabCart)
        val ivNotifications = findViewById<ImageView>(R.id.ivNotifications)
        val ivProfileNav = findViewById<ImageView>(R.id.ivProfileNav)

        // Highlight active tab (Profile)
        ivProfileNav.setColorFilter(ContextCompat.getColor(this, R.color.primary_blue))

        ivHome.setOnClickListener {
            val intent = Intent(this, HomeActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_REORDER_TO_FRONT
            startActivity(intent)
            finish()
        }

        ivFav.setOnClickListener {
            val intent = Intent(this, FavouriteActivity::class.java)
            startActivity(intent)
            finish()
        }

        fabCart.setOnClickListener {
            startActivity(Intent(this, MyCartActivity::class.java))
        }

        ivNotifications.setOnClickListener {
            val intent = Intent(this, NotificationsActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}