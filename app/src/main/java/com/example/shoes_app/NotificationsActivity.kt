package com.example.shoes_app

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.android.material.floatingactionbutton.FloatingActionButton

class NotificationsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notifications)

        findViewById<ImageButton>(R.id.btnBack).setOnClickListener {
            finish()
        }

        // Fix: Clear All button logic
        val tvClearAll = findViewById<TextView>(R.id.tvClearAll)
        val notificationsContainer = findViewById<LinearLayout>(R.id.notificationsContainer)

        tvClearAll.setOnClickListener {
            // Since these are static/dummy notifications for now, we just hide them
            notificationsContainer.visibility = View.GONE
            Toast.makeText(this, "Notifications cleared", Toast.LENGTH_SHORT).show()
        }

        // Setup Bottom Nav
        val ivHome = findViewById<ImageView>(R.id.ivHome)
        val ivFav = findViewById<ImageView>(R.id.ivFav)
        val fabCart = findViewById<FloatingActionButton>(R.id.fabCart)
        val ivNotifications = findViewById<ImageView>(R.id.ivNotifications)
        val ivProfileNav = findViewById<ImageView>(R.id.ivProfileNav)

        // Highlight active tab (Notifications)
        ivNotifications.setColorFilter(ContextCompat.getColor(this, R.color.primary_blue))

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

        ivProfileNav.setOnClickListener {
            val intent = Intent(this, AccountSettingsActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}