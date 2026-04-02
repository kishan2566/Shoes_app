package com.example.shoes_app

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton

class FavouriteActivity : AppCompatActivity() {

    private lateinit var adapter: FavouriteProductAdapter
    private val favouriteList = mutableListOf<Product>()
    private lateinit var tvNoResults: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favourite)

        tvNoResults = findViewById(R.id.tvNoResults)

        val rvFavourite = findViewById<RecyclerView>(R.id.rvFavourite)
        rvFavourite.layoutManager = LinearLayoutManager(this)

        val sharedPref = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
        val userId = sharedPref.getString("userId", null)

        adapter = FavouriteProductAdapter(favouriteList, this, userId ?: "")
        rvFavourite.adapter = adapter

        loadFavourites()

        // Back button
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
            startActivity(Intent(this, NotificationsActivity::class.java))
        }

        ivProfileNav.setOnClickListener {
            startActivity(Intent(this, ProfileActivity::class.java))
        }
    }

    override fun onResume() {
        super.onResume()
        // Refresh list whenever we come back (in case user toggled a favourite)
        loadFavourites()
    }

    private fun loadFavourites() {
        val favPrefs = getSharedPreferences("Favourites", Context.MODE_PRIVATE)
        val favIds = favPrefs.getStringSet("fav_ids", emptySet()) ?: emptySet()

        favouriteList.clear()
        for (id in favIds) {
            val product = ProductRepository.getProductById(id)
            if (product != null) favouriteList.add(product)
        }

        adapter.updateProducts(favouriteList)
        tvNoResults.visibility = if (favouriteList.isEmpty()) View.VISIBLE else View.GONE
    }
}