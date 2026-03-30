package com.example.shoes_app

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*

class WishlistActivity : AppCompatActivity() {
    private lateinit var adapter: SearchProductAdapter // Reusing Search adapter for simple listing
    private val wishlist = mutableListOf<Product>()
    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favourite) // Reusing existing layout

        findViewById<ImageButton>(R.id.btnBack).setOnClickListener { finish() }
        
        val rvWishlist = findViewById<RecyclerView>(R.id.rvFavourite)
        rvWishlist.layoutManager = LinearLayoutManager(this)
        adapter = SearchProductAdapter(wishlist, this)
        rvWishlist.adapter = adapter

        val sharedPref = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
        val userId = sharedPref.getString("userId", null)

        if (userId != null) {
            database = FirebaseDatabase.getInstance().getReference("wishlist").child(userId)
            fetchWishlist()
        }
    }

    private fun fetchWishlist() {
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                wishlist.clear()
                for (snap in snapshot.children) {
                    val product = snap.getValue(Product::class.java)
                    if (product != null) wishlist.add(product)
                }
                adapter.notifyDataSetChanged()
                findViewById<TextView>(R.id.tvNoResults)?.visibility = 
                    if (wishlist.isEmpty()) View.VISIBLE else View.GONE
            }
            override fun onCancelled(error: DatabaseError) {}
        })
    }
}