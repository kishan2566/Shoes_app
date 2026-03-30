package com.example.shoes_app

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton

class FavouriteActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favourite)

        findViewById<ImageButton>(R.id.btnBack).setOnClickListener {
            finish()
        }

        // Setup Bottom Nav
        val ivHome = findViewById<ImageView>(R.id.ivHome)
        val ivFav = findViewById<ImageView>(R.id.ivFav)
        val fabCart = findViewById<FloatingActionButton>(R.id.fabCart)
        val ivNotifications = findViewById<ImageView>(R.id.ivNotifications)
        val ivProfileNav = findViewById<ImageView>(R.id.ivProfileNav)

        // Highlight active tab
        ivFav.setColorFilter(getColor(R.color.primary_blue))

        ivHome.setOnClickListener {
            val intent = Intent(this, HomeActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_REORDER_TO_FRONT
            startActivity(intent)
        }

        fabCart.setOnClickListener {
            startActivity(Intent(this, MyCartActivity::class.java))
        }

        ivNotifications.setOnClickListener {
            val intent = Intent(this, NotificationsActivity::class.java)
            startActivity(intent)
            finish()
        }

        ivProfileNav.setOnClickListener {
            val intent = Intent(this, AccountSettingsActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}