package com.example.shoes_app

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.database.FirebaseDatabase

class FavouriteActivity : AppCompatActivity() {

    private lateinit var adapter: FavouriteProductAdapter
    private val favouriteList = mutableListOf<Product>()
    private lateinit var tvNoResults: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favourite)

        tvNoResults = findViewById(R.id.tvNoResults)

        val rvFavourite = findViewById<RecyclerView>(R.id.rvFavourite)
        // Changed to 2-column grid to match Home page
        rvFavourite.layoutManager = GridLayoutManager(this, 2)

        val sharedPref = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
        val userId = sharedPref.getString("userId", null)

        adapter = FavouriteProductAdapter(favouriteList, this, userId ?: "")
        rvFavourite.adapter = adapter

        // Back button
        findViewById<ImageButton>(R.id.btnBack).setOnClickListener {
            finish()
        }

        setupRecommendedItems()

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

    private fun setupRecommendedItems() {
        val card1 = findViewById<View>(R.id.recommended_card_1)
        val card2 = findViewById<View>(R.id.recommended_card_2)

        card1?.setOnClickListener {
            val intent = Intent(this, ProductDetailsActivity::class.java)
            intent.putExtra("productId", "2") // Adidas Ultraboost
            startActivity(intent)
        }

        card2?.setOnClickListener {
            val intent = Intent(this, ProductDetailsActivity::class.java)
            intent.putExtra("productId", "3") // Puma RS-X
            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()
        loadFavourites()
    }

    private fun loadFavourites() {
        val favPrefs = getSharedPreferences("Favourites", Context.MODE_PRIVATE)
        val favIds = favPrefs.getStringSet("fav_ids", emptySet()) ?: emptySet()

        favouriteList.clear()
        
        if (favIds.isEmpty()) {
            adapter.updateProducts(favouriteList)
            tvNoResults.visibility = View.VISIBLE
            return
        }

        tvNoResults.visibility = View.GONE
        var loadedCount = 0
        val targetCount = favIds.size

        for (id in favIds) {
            // Check manual products first
            val manualProduct = when (id) {
                "1" -> Product("1", "Nike Air Jordan 1", "Nike", 12000.0, "Nike", "The iconic Air Jordan 1 high top sneaker.", "ic_launcher_foreground", listOf("ic_launcher_foreground"), 0, 4.8, 15)
                "2" -> Product("2", "Adidas Ultraboost", "Adidas", 15000.0, "Adidas", "Experience the ultimate comfort.", "shoes1", listOf("shoes1"), 0, 4.7, 20)
                "3" -> Product("3", "Puma RS-X", "Puma", 8000.0, "Puma", "Bold, bulky and colorful.", "shoes2", listOf("shoes2"), 0, 4.5, 10)
                "4" -> Product("4", "Jordan Retro 4", "Jordan", 18000.0, "Jordan", "One of the most popular Jordan models.", "shoes3", listOf("shoes3"), 0, 4.9, 5)
                "5" -> Product("5", "Nike Air Max 270", "Nike", 13500.0, "Nike", "Style, comfort and a big attitude.", "shoes4", listOf("shoes4"), 0, 4.9, 8)
                "6" -> Product("6", "Adidas NMD_R1", "Adidas", 14000.0, "Adidas", "A fusion of street style.", "ic_launcher_foreground", listOf("ic_launcher_foreground"), 0, 4.4, 25)
                else -> null
            }

            if (manualProduct != null) {
                favouriteList.add(manualProduct)
                loadedCount++
                if (loadedCount == targetCount) {
                    adapter.updateProducts(favouriteList)
                }
            } else {
                // Fetch from Firebase
                FirebaseDatabase.getInstance().getReference("products").child(id)
                    .get().addOnSuccessListener { snapshot ->
                        val p = snapshot.getValue(Product::class.java)
                        p?.let {
                            it.id = snapshot.key
                            favouriteList.add(it)
                        }
                        loadedCount++
                        if (loadedCount == targetCount) {
                            adapter.updateProducts(favouriteList)
                        }
                    }.addOnFailureListener {
                        loadedCount++
                        if (loadedCount == targetCount) {
                            adapter.updateProducts(favouriteList)
                        }
                    }
            }
        }
    }
}
