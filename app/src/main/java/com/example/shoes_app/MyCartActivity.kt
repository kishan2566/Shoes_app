package com.example.shoes_app

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*

class MyCartActivity : AppCompatActivity() {
    private lateinit var adapter: CartAdapter
    private lateinit var database: DatabaseReference
    private val cartList = mutableListOf<Product>()
    private lateinit var tvSubtotal: TextView
    private lateinit var tvTotalCost: TextView
    private val shippingCost = 40.90
    private var currentSubtotal = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_cart)

        tvSubtotal = findViewById(R.id.tvSubtotal)
        tvTotalCost = findViewById(R.id.tvTotalCost)
        val btnCheckout = findViewById<Button>(R.id.btnCheckout)

        findViewById<ImageButton>(R.id.btnBack).setOnClickListener {
            finish()
        }

        val sharedPref = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
        val userId = sharedPref.getString("userId", null)

        if (userId == null) {
            finish()
            return
        }

        val recyclerView = findViewById<RecyclerView>(R.id.rvCartItems)
        recyclerView.layoutManager = LinearLayoutManager(this)
        
        adapter = CartAdapter(cartList, this) { product ->
            removeFromCart(userId, product)
        }
        recyclerView.adapter = adapter

        database = FirebaseDatabase.getInstance().getReference("carts").child(userId)
        fetchCartItems()

        btnCheckout.setOnClickListener {
            if (cartList.isNotEmpty()) {
                val intent = Intent(this, PaymentActivity::class.java)
                intent.putExtra("totalAmount", currentSubtotal + shippingCost)
                startActivity(intent)
                finish()
            } else {
                Toast.makeText(this, "Cart is empty", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun fetchCartItems() {
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                cartList.clear()
                currentSubtotal = 0.0
                for (itemSnapshot in snapshot.children) {
                    val product = itemSnapshot.getValue(Product::class.java)
                    if (product != null) {
                        cartList.add(product)
                        currentSubtotal += product.price ?: 0.0
                    }
                }
                adapter.updateItems(cartList)
                updateSummary(currentSubtotal)
            }
            override fun onCancelled(error: DatabaseError) {}
        })
    }

    private fun updateSummary(subtotal: Double) {
        val total = if (subtotal > 0) subtotal + shippingCost else 0.0
        tvSubtotal.text = "₹${String.format("%.2f", subtotal)}"
        tvTotalCost.text = "₹${String.format("%.2f", total)}"
    }

    private fun removeFromCart(userId: String, product: Product) {
        product.id?.let {
            database.child(it).removeValue()
        }
    }
}