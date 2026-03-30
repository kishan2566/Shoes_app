package com.example.shoes_app

import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.floatingactionbutton.FloatingActionButton

class HomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        val drawerLayout = findViewById<DrawerLayout>(R.id.drawerLayout)
        val btnMenu = findViewById<ImageButton>(R.id.btnMenu)
        val btnCart = findViewById<ImageButton>(R.id.btnCart)
        val etSearch = findViewById<EditText>(R.id.etSearch)
        val tvSeeAll = findViewById<TextView>(R.id.tvSeeAll)
        val cardShoe1 = findViewById<androidx.constraintlayout.widget.ConstraintLayout>(R.id.cardShoe1)
        
        // Bottom Nav Icons
        val ivHome = findViewById<ImageView>(R.id.ivHome)
        val ivFav = findViewById<ImageView>(R.id.ivFav)
        val fabCart = findViewById<FloatingActionButton>(R.id.fabCart)
        val ivNotifications = findViewById<ImageView>(R.id.ivNotifications)
        val ivProfileNav = findViewById<ImageView>(R.id.ivProfileNav)

        // Highlight active tab
        ivHome.setColorFilter(ContextCompat.getColor(this, R.color.primary_blue))

        // Drawer Menu Items
        val menuProfile = findViewById<TextView>(R.id.menu_profile)
        val menuHome = findViewById<TextView>(R.id.menu_home)
        val menuCart = findViewById<TextView>(R.id.menu_cart)
        val menuFavorite = findViewById<TextView>(R.id.menu_favorite)
        val menuNotifications = findViewById<TextView>(R.id.menu_notifications)
        val menuSignOut = findViewById<TextView>(R.id.menu_signout)

        // 1. Open Side Menu
        btnMenu.setOnClickListener {
            drawerLayout.openDrawer(GravityCompat.START)
        }

        // 2. Open Search Page
        etSearch.setOnClickListener {
            startActivity(Intent(this, SearchActivity::class.java))
        }

        // 3. Open Best Sellers Page
        tvSeeAll.setOnClickListener {
            startActivity(Intent(this, BestSellerActivity::class.java))
        }

        // 4. Open Product Details (Click on first shoe card)
        cardShoe1.setOnClickListener {
            startActivity(Intent(this, ProductDetailsActivity::class.java))
        }

        // 5. Open Cart (Header button or FAB)
        btnCart.setOnClickListener {
            startActivity(Intent(this, MyCartActivity::class.java))
        }
        
        // Bottom Nav Listeners
        fabCart.setOnClickListener {
            startActivity(Intent(this, MyCartActivity::class.java))
        }

        ivFav.setOnClickListener {
            startActivity(Intent(this, FavouriteActivity::class.java))
        }

        ivNotifications.setOnClickListener {
            startActivity(Intent(this, NotificationsActivity::class.java))
        }

        ivProfileNav.setOnClickListener {
            startActivity(Intent(this, AccountSettingsActivity::class.java))
        }

        // --- Drawer Navigation ---
        
        menuProfile.setOnClickListener {
            drawerLayout.closeDrawer(GravityCompat.START)
            startActivity(Intent(this, ProfileActivity::class.java))
        }

        menuHome.setOnClickListener {
            drawerLayout.closeDrawer(GravityCompat.START)
        }

        menuCart.setOnClickListener {
            drawerLayout.closeDrawer(GravityCompat.START)
            startActivity(Intent(this, MyCartActivity::class.java))
        }

        menuFavorite.setOnClickListener {
            drawerLayout.closeDrawer(GravityCompat.START)
            startActivity(Intent(this, FavouriteActivity::class.java))
        }

        menuNotifications.setOnClickListener {
            drawerLayout.closeDrawer(GravityCompat.START)
            startActivity(Intent(this, NotificationsActivity::class.java))
        }

        menuSignOut.setOnClickListener {
            drawerLayout.closeDrawer(GravityCompat.START)
            val intent = Intent(this, SignInActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }
    }
}