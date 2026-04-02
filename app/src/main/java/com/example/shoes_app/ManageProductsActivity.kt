package com.example.shoes_app

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton

class ManageProductsActivity : AppCompatActivity() {
    private lateinit var adapter: ProductAdapter
    private val productList = mutableListOf<Product>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_manage_products)

        findViewById<ImageButton>(R.id.btnBack).setOnClickListener { finish() }

        val recyclerView = findViewById<RecyclerView>(R.id.rvProducts)
        recyclerView.layoutManager = LinearLayoutManager(this)

        adapter = ProductAdapter(productList,
            onDeleteClick = { product ->
                // Delete is a no-op for hardcoded products (Firebase writes not supported)
                Toast.makeText(this, "Products are managed in code (Firebase unavailable)", Toast.LENGTH_SHORT).show()
            },
            onEditClick = { product ->
                val intent = Intent(this, AddProductActivity::class.java)
                intent.putExtra("productId", product.id)
                intent.putExtra("isEdit", true)
                startActivity(intent)
            }
        )
        recyclerView.adapter = adapter

        // FAB: inform admin that products are hardcoded
        findViewById<FloatingActionButton>(R.id.fabAddProduct).setOnClickListener {
            Toast.makeText(this, "Add products in ProductRepository.kt", Toast.LENGTH_LONG).show()
        }

        // Load from local repository
        productList.clear()
        productList.addAll(ProductRepository.products)
        adapter.updateProducts(productList)
    }
}